package com.dengsn.osc;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class OscReceiver implements Runnable
{
  // Variables
  private final int port;
  
  // Constructor
  public OscReceiver(int port)
  {
    this.port = port;
    
    Thread thread = new Thread(this);
    thread.setDaemon(true);
    thread.start();
  }

  // Run the server
  @Override public void run()
  {
    try
    {
      // Get the buffer from the message
      byte[] buffer = new byte[1024];
      
      // Create a new UDP socket
      DatagramSocket socket = new DatagramSocket(this.port);
    
      // Create a new UDP packet and send it
      DatagramPacket packet = new DatagramPacket(buffer,buffer.length);
      
      // Loop
      while (true)
      {
        // Receive the packet
        socket.receive(packet);
        
        // Parse the packet
        OscMessage message = new OscMessage("/dummy");
        
        // Send the message to the listeners
      }
    }
    catch (IOException ex)
    {
      
    }
  }
}
