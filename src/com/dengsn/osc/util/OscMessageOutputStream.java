package com.dengsn.osc.util;

import com.dengsn.osc.OscMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.List;

public class OscMessageOutputStream extends ByteArrayOutputStream
{
  // Write a message
  public OscMessageOutputStream writeMessage(OscMessage message) throws IOException
  {
    this.writeString(message.getAddress());
    this.writeString(OscMessageOutputStream.writeTypes(message.getArguments()));
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
    ByteBuffer buffer = ByteBuffer.allocate(4);
    buffer.putInt(value);
    this.write(buffer.array());
  }
  
  // Write a 64-bit value to the stream
  private void writeLong(long value) throws IOException
  {
    ByteBuffer buffer = ByteBuffer.allocate(8);
    buffer.putLong(value);
    this.write(buffer.array());
  }
  
  // Write a string to the stream
  private void writeString(String s) throws IOException
  {
    // Write all characters
    byte[] stringBytes = s.getBytes(Charset.forName("US-ASCII"));
    this.write(stringBytes);
    this.write('\0');
    
    // Pad with null characters
    while (this.size() % 4 != 0)
      this.write('\0');
  }
  
  // Returns a type string for the collection of objects
  private static String writeTypes(List<Object> objects)
  {
    StringBuilder sb = new StringBuilder(",");  
    for (Object o : objects)
    {
      if (o == null)
        continue;
      
      Class c = o.getClass();
      
      if (c.equals(Integer.class))
        sb.append('i');
      else if (c.equals(Long.class))
        sb.append('h');
      else if (c.equals(Float.class))
        sb.append('f');
      else if (c.equals(Double.class))
        sb.append('d');
      else if (c.equals(String.class))
        sb.append('s');
      else
        throw new RuntimeException("Objects of type " + o.getClass().getName() + " are not supported");
    }
    return sb.toString();
  }
}
