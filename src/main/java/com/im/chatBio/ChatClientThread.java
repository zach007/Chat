package com.im.chatBio;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatClientThread implements Runnable {
  private Socket socket = null;
  private ChatClient client = null;
  private DataInputStream streamIn = null;

  public ChatClientThread(ChatClient chatClient, Socket chatSocket) {
    client = chatClient;
    socket = chatSocket;
    open();
  }

  public void run() {
    while (true) {
      try {
        client.handle(streamIn.readUTF());
      } catch (IOException ioe) {
        System.out.println("Listening error: " + ioe.getMessage());
        client.stop();
      }
    }
  }

  public void open() {
    try {
      streamIn = new DataInputStream(socket.getInputStream());
    } catch (IOException ioe) {
      System.out.println("Error getting input stream: " + ioe);
      client.stop();
    }
  }

  public void close() {
    try {
      if (streamIn != null) {
        streamIn.close();
      }
    } catch (IOException ioe) {
      System.out.println("Error closing input stream: " + ioe);
    }
  }
}