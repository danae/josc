package com.dengsn.osc;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class OscSender
{
  // Variables
  private final InetAddress address;
  private final int port;
  
  // Constructor
  public OscSender(InetAddress address, int port)
  {
    this.address = address;
    this.port = port;
  }
  public OscSender(int port) throws UnknownHostException
  {
    this(InetAddress.getLocalHost(),port);
  }
  
  // Send message
  public void send(OscMessage message) throws IOException
  {
    // Get the buffer from the message
    byte[] buffer = message.bytes();
      
    // Create a new UDP socket
    DatagramSocket socket = new DatagramSocket();
      
    // Create a new UDP packet and send it
    DatagramPacket packet = new DatagramPacket(buffer,buffer.length,this.address,this.port);
    socket.send(packet);
  }
  
  // Convert to string
  @Override public String toString()
  {
    return this.address.toString() + ":" + this.port;
  }
}
