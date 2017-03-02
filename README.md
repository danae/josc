# Usage in Processing

## Installation
Download the [latest release](https://github.com/dengsn/Josc/releases) of Josc.jar and place it in a `code/` folder inside your sketch. Include the following line in your Processing sketch:

    import com.dengsn.osc.*;
    
## Sending messages

    import com.dengsn.osc.*;
    
    void setup()
    {
      // Send the message to the local host at port 12000
      new OscMessage("/address")
        .add(1)
        .add(1.0)
        .add("Hello, World!")
        .sendTo("127.0.0.1",12000);
    }
      
For multiple messages sent to the same sender, you can also use the `OscSender` class for convenience:
    
    import com.dengsn.osc.*;
    
    OscSender sender;
    
    void setup()
    {
      // Create an OSC sender to the local host at port 12000
      sender = new OscSender("127.0.0.1",12000);
    
      // Send the message to the sender
      new OscMessage("/address")
        .sendTo(sender);
    }
      
## Receiving messages

    import com.dengsn.osc.*;
    
    OscListener listener;
    OscReceiver receiver;
    
    void setup()
    {
      // Create a listener to handle messages
      listener = new OscListener()
      {
        public void handle(OscMessage message, OscSender sender)
        {
          // Get the address of the message (assuming the message that was sent previously)
          println(message.getAddress());                  // /address
          
          // Get the arguments in the message
          println(message.get(0,Integer.class));          // 1
          println(message.get(1,Float.class));            // 1.0
          println(message.get(2,String.class));           // Hello, World!
          
          // Get the IP and port of the sender that sent you the message
          println(sender.getAddress().getHostAddress());  // 127.0.0.1
          println(sender.getPort());                      // 12000
        }
      };
      
      // Create an OSC receiver listening at port 12000
      receiver = new OscReceiver(12000);
      
      // Add the listener to the OSC receiver
      receiver.addListener(listener);
    
      // Start the OSC receiver
      receiver.start();
    }
