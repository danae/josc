package com.dengsn.osc;

import com.dengsn.osc.OscMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;

public class OscMessageOutputStream extends ByteArrayOutputStream
{
  // Variables
  private final byte[] intBuffer = new byte[4];
  private final byte[] longBuffer = new byte[8];
  private final byte[] stringBuffer = new byte[2048];
  
  // Write a message
  public OscMessageOutputStream writeMessage(OscMessage message) throws IOException
  {
    this.writeString(message.getAddress());
    this.writeString(OscMessageOutputStream.getTypes(message.getArguments()));
    for (Object o : message.getArguments())
      this.writeObject(o);
    
    return this;
  }
  
  // Write an object to the stream
  private void writeObject(Object o) throws IOException
  {
    // Handle null
    if (o == null)
      return;
    
    // Handle primitive classes
    if (o instanceof Integer)
      this.writeInt((Integer)o);
    else if (o instanceof Long)
      this.writeLong((Long)o);
    else if (o instanceof Float)
      this.writeInt(Float.floatToIntBits((Float)o));
    else if (o instanceof Double)
      this.writeLong(Double.doubleToLongBits((Double)o));
    else if (o instanceof String)
      this.writeString((String)o);
    else
      throw new RuntimeException("Objects of type " + o.getClass().getName() + " are not supported");
  }
  
  // Write a 32-bit value to the stream
  private void writeInt(int value) throws IOException
  {
    for (int i = 3; i >= 0; i --)
    {
      this.intBuffer[i] = (byte)value;
      value >>>= 8;
    }
    this.write(this.intBuffer);
  }
  
  // Write a 64-bit value to the stream
  private void writeLong(long value) throws IOException
  {
    for (int i = 7; i >= 0; i --)
    {
      this.longBuffer[i] = (byte)value;
      value >>>= 8;
    }
    this.write(this.longBuffer);
  }
  
  // Write a string to the stream
  private void writeString(String s)
  {
    try
    {
      // Get the string bytes
      byte[] bytes = s.getBytes("US-ASCII");
      int length = bytes.length;
      System.arraycopy(bytes,0,this.stringBuffer,0,length);
    
      // Pad with zeroes
      int padding = 4 - (length % 4);
      for (int i = 0; i < padding; i ++)
        this.stringBuffer[length++] = 0;
    
      this.write(this.stringBuffer,0,length);
    }
    catch (UnsupportedEncodingException ex)
    {
      throw new RuntimeException("This system does not support ASCIII encoding");
    }
  }
  
  // Returns a type string for the collection of objects
  private static String getTypes(Collection<Object> objects)
  {
    StringBuilder sb = new StringBuilder(",");  
    for (Object o : objects)
    {
      if (o.getClass().equals(Integer.class))
        sb.append('i');
      else if (o.getClass().equals(Long.class))
        sb.append('h');
      else if (o.getClass().equals(Float.class))
        sb.append('f');
      else if (o.getClass().equals(Double.class))
        sb.append('d');
      else if (o.getClass().equals(String.class))
        sb.append('s');
      else
        throw new RuntimeException("Objects of type " + o.getClass().getName() + " are not supported");
    }
    return sb.toString();
  }
}
