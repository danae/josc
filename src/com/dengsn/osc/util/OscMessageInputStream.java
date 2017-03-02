package com.dengsn.osc.util;

import com.dengsn.osc.OscException;
import com.dengsn.osc.OscMessage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class OscMessageInputStream extends ByteArrayInputStream
{
  // Variables
  private final byte[] intBuffer = new byte[4];
  private final byte[] longBuffer = new byte[8];
  
  // Constructor
  public OscMessageInputStream(byte[] buffer)
  {
    super(buffer);
  }
  public OscMessageInputStream(byte[] buffer, int offset, int length)
  {
    super(buffer,offset,length);
  }
  
  // Read a message
  public OscMessage readMessage() throws IOException
  {
    // Read the address
    String address = this.readString();
    OscMessage message = new OscMessage(address);
    
    // Read the type list
    List<Class> types = readTypes(this.readString());
    
    // Read the arguments
    for (Class type : types)
      message.getArguments().add(this.readObject(type));
    
    return message;
  }
  
  // Read an object from the stream
  private Object readObject(Class type) throws IOException
  {
    // Handle primitive classes
    if (type.equals(Integer.class))
      return this.readInt();
    else if (type.equals(Long.class))
      return this.readLong();
    else if (type.equals(Float.class))
      return Float.intBitsToFloat(this.readInt());
    else if (type.equals(Double.class))
      return Double.longBitsToDouble(this.readLong());
    else if (type.equals(String.class))
      return this.readString();
    else
      throw new OscException("Objects of type " + type.getName() + " are not supported");
  }

  // Read a 32-bit value from the stream
  private int readInt() throws IOException
  {
    this.read(this.intBuffer);
    ByteBuffer buffer = ByteBuffer.wrap(this.intBuffer);
    return buffer.getInt();
  }
  
  // Read a 64-bit value from the stream
  private long readLong() throws IOException
  {
    this.read(this.longBuffer);
    ByteBuffer buffer = ByteBuffer.wrap(this.longBuffer);
    return buffer.getLong();
  }
  
  // Read a string from the stream
  private String readString()
  {
    StringBuilder sb = new StringBuilder();
    
    // Read all characters
    int character, num;
    for (num = 1; (character = this.read()) > 0; num ++)
      sb.appendCodePoint(character);
    
    // Pad with null characters
    for (int i = num; i % 4 != 0; i ++)
      this.read();
    
    return sb.toString();
  }
  
  // Returns a type string for the collection of objects
  private static List<Class> readTypes(String types)
  {
    // Check if types are given
    if (types.isEmpty())
      return Collections.emptyList();
              
    // Check if the string start with a comma
    if (!types.startsWith(","))
      throw new IllegalArgumentException("Type string has to start with a comma");
    
    // Parse the types
    List<Class> list = new LinkedList<>();
    for (int i = 1; i < types.length(); i ++)
    {
      char c = types.charAt(i);
      
      if (c == 'i')
        list.add(Integer.class);
      else if (c == 'h')
        list.add(Long.class);
      else if (c == 'f')
        list.add(Float.class);
      else if (c == 'd')
        list.add(Double.class);
      else if (c == 's')
        list.add(String.class);
      else
        throw new OscException("Types of type '" + types.charAt(i) + "' are not supported");
    }
    return list;
  }
}
