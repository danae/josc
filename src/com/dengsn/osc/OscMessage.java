package com.dengsn.osc;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class OscMessage
{
  // Variables
  private final String address;
  private final List<Object> arguments;
  
  // Constructor
  public OscMessage(String address)
  {
    this.address = address;
    this.arguments = new ArrayList<>();
  }
  
  // Management
  public String getAddress()
  {
    return this.address;
  }
  public List<Object> getArguments()
  {
    return this.arguments;
  }
  
  // Get arguments
  public <T> T get(int index, Class<T> type)
  {
    Object o = this.arguments.get(index);
    if (o.getClass().isAssignableFrom(type))
      return (T)o;
    else
      throw new ClassCastException("Argument " + index + " is not of type " + type.toString());
  }
  
  // Add arguments
  public OscMessage add(int i)
  {
    this.arguments.add(i);
    return this;
  }
  public OscMessage add(long l)
  {
    this.arguments.add(l);
    return this;
  }
  public OscMessage add(float f)
  {
    this.arguments.add(f);
    return this;
  }
  public OscMessage add(double d)
  {
    this.arguments.add(d);
    return this;
  }
  public OscMessage add(char c)
  {
    this.arguments.add(c);
    return this;
  }
  public OscMessage add(String s)
  {
    this.arguments.add(s);
    return this;
  }
  
  // To byte array
  byte[] bytes() throws IOException
  {
    return new OscMessageOutputStream()
      .writeMessage(this)
      .toByteArray();
  }
  
  // Send the mesasge
  public void sendTo(OscSender sender) throws IOException
  {
    sender.send(this);
  }
  public void sendTo(InetAddress address, int port) throws IOException
  {
    new OscSender(address,port).send(this);
  }
  public void sendTo(String address, int port) throws IOException, UnknownHostException
  {
    new OscSender(InetAddress.getByName(address),port).send(this);
  }
}
