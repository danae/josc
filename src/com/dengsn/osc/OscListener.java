package com.dengsn.osc;

@FunctionalInterface
public interface OscListener
{
  public void handle(OscMessage message, OscSender sender);
}
