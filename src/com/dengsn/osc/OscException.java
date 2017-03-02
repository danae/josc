package com.dengsn.osc;

public class OscException extends RuntimeException
{
  // Constructor
  public OscException(String message, Throwable cause)
  {
    super(message,cause);
  }
  public OscException(String message)
  {
    super(message);
  }
}
