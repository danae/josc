package com.dengsn.osc;

import com.dengsn.osc.util.OscMessageOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
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
  
  // Add multiple arguments
  public OscMessage addAll(Collection<? extends Object> collection)
  {
    collection.forEach(this.arguments::add);
    return this;
  }
  
  // Send the mesasge
  public void sendTo(OscSender sender)
  {
    sender.send(this);
  }
  public void sendTo(InetAddress address, int port)
  {
    new OscSender(address,port).send(this);
  }
  public void sendTo(String address, int port) throws UnknownHostException
  {
    try
    {
      new OscSender(InetAddress.getByName(address),port).send(this);
    }
    catch (UnknownHostException ex)
    {
      System.err.println(ex.getMessage());
      System.exit(-1);
    }
  }
  
  // Convert to string
  @Override public String toString()
  {
    StringBuilder sb = new StringBuilder(this.getAddress());
    for (Object o : this.getArguments())
      sb.append(" ").append(o.toString());
    return sb.toString();
  }
}
