package com.dengsn.osc;

import com.dengsn.osc.util.OscMessageInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

public class OscReceiver implements Runnable
{
  // Variables
  private final Integer port;
  private final List<OscListener> listeners;
  private final Thread thread;
  
  // Constructor
  public OscReceiver(int port, boolean daemon)
  {
    this.port = port;
    this.listeners = new LinkedList<>();
    this.thread = new Thread(this);
    this.thread.setDaemon(daemon);
  }
  public OscReceiver(int port)
  {
    this(port,true);
  }
  
  // Management
  public int getPort()
  {
    return this.port;
  }
  
  // Listener management
  public boolean addListener(OscListener listener)  
  {
    return this.listeners.add(listener);
  }
  public boolean removeListener(OscListener listener)
  {
    return this.listeners.remove(listener);
  }
  
  // Start ad stop the receiver
  public void start()
  {
    this.thread.start();
  }
  public void stop()
  {
    this.thread.interrupt();
  }

  // Run the receiver
  @Override public void run()
  {
    try
    {
      // Create a new UDP socket and packet
      byte[] buffer = new byte[2048];
      DatagramSocket socket = new DatagramSocket(this.port);
      DatagramPacket packet = new DatagramPacket(buffer,buffer.length);
      
      // Loop
      while (!this.thread.isInterrupted())
      {
        // Receive the packet
        socket.receive(packet);
        
        // Create a message from the byte buffer
        OscMessage message = new OscMessageInputStream(packet.getData())
          .readMessage();
        OscSender sender = new OscSender(packet.getAddress(),packet.getPort());
        
        // Send the message to the listeners
        for (OscListener listener : this.listeners)
          listener.handle(message,sender);
      }
    }
    catch (IOException ex)
    {
      throw new RuntimeException(ex.getMessage(),ex);
    }
  }
  
  // Convert to string
  @Override public String toString()
  {
    try
    {
      return new StringBuilder("receiver ")
        .append(InetAddress.getLocalHost().getHostAddress())
        .append(":")
        .append(this.getPort())
        .toString();
    }
    catch (UnknownHostException ex)
    {
      return new StringBuilder("receiver 0.0.0.0:")
        .append(this.getPort())
        .toString();
    }
  }
}
