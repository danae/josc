package com.dengsn.osc;

import com.dengsn.osc.util.OscMessageOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class OscSender
{
  // Variables
  private final InetAddress address;
  private final Integer port;
  
  // Constructor
  public OscSender(InetAddress address, int port)
  {
    this.address = address;
    this.port = port;
  }
  public OscSender(String address, int port)
  {
    try
    {
      this.address = InetAddress.getByName(address);
      this.port = port;
    }
    catch (UnknownHostException ex)
    {
      throw new OscException("Could not find host: " + ex.getMessage(),ex);
    }
  }
  public OscSender(int port)
  {
    try
    {
      this.address = InetAddress.getLocalHost();
      this.port = port;
    }
    catch (UnknownHostException ex)
    {
      throw new OscException("Could not find host: " + ex.getMessage(),ex);
    }
  }
  
  // Management
  public InetAddress getAddress()
  {
    return this.address;
  }
  public int getPort()
  {
    return this.port;
  }
  
  // Send message
  public void send(OscMessage message)
  {
    try
    {
      // Create a byte buffer from the message
      byte[] buffer = new OscMessageOutputStream()
        .writeMessage(message)
        .toByteArray();
    
      // Create a new UDP socket and packet and send it
      DatagramSocket socket = new DatagramSocket();
      DatagramPacket packet = new DatagramPacket(buffer,buffer.length,this.address,this.port);
      socket.send(packet);
    }
    catch (IOException ex)
    {
      throw new OscException("Could not send message: " + ex.getMessage(),ex);
    }
  }
  
  // Convert to string
  @Override public String toString()
  {
    return new StringBuilder("Sender ")
      .append(this.getAddress().getHostAddress())
      .append(":")
      .append(this.getPort())
      .toString();
  }
}
