package com.im.chatBio;

import java.io.*;
import java.net.Socket;

public class ChatServerThread implements Runnable {
  private ChatServer chatServer = null;
  private Socket socket = null;
  private int port = 0;
  private DataInputStream streamIn = null;
  private DataOutputStream streamOut = null;

  public ChatServerThread(ChatServer chatServer, Socket currentSocket) {
    super();
    this.chatServer = chatServer;
    this.socket = currentSocket;
    port = socket.getPort();
  }

  @Override
  public void run() {
    System.out.println("Server Thread " + port + " running.");
    try {
      open();
    } catch (IOException e) {
      e.printStackTrace();
    }
    while (true) {
      try {
        chatServer.handle(this, streamIn.readUTF());
      } catch (IOException ioe) {
        System.out.println(port + " ERROR reading: " + ioe.getMessage());
        chatServer.remove(this);
        //interrupt();
      }
    }
  }

  public int getPort() {
    return port;
  }

  public void send(String msg) {
    try {
      streamOut.writeUTF(msg);
      streamOut.flush();
    } catch (IOException ioe) {
      System.out.println(port + " ERROR sending: " + ioe.getMessage());
      chatServer.remove(this);
      //interrupt();
    }
  }

  public void open() throws IOException {
    streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
    streamOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
  }

  public void close() throws IOException {
    if (socket != null) {
      socket.close();
    }
    if (streamIn != null) {
      streamIn.close();
    }
    if (streamOut != null) {
      streamOut.close();
    }
  }
}