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
  private InetAddress address;
  private Integer port;
  
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
      System.err.println(ex.getMessage());
      System.exit(-1);
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
      System.err.println(ex.getMessage());
      System.exit(-1);
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
      System.err.println(ex.getMessage());
      System.exit(-1);
    }
  }
  
  // Convert to string
  @Override public String toString()
  {
    return new StringBuilder("sender ")
      .append(this.getAddress().getHostAddress())
      .append(":")
      .append(this.getPort())
      .toString();
  }
}
