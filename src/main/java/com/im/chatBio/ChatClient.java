package com.im.chatBio;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatClient implements Runnable {
  private Socket socket = null;
  private DataInputStream console = null;
  private DataOutputStream streamOut = null;
  private ChatClientThread client = null;

  public ChatClient(String serverName, int serverPort) {
    System.out.println("Establishing connection. Please wait ...");
    try {
      socket = new Socket(serverName, serverPort);
      System.out.println("Connected: " + socket);
      start();
    } catch (UnknownHostException uhe) {
      System.out.println("Host unknown: " + uhe.getMessage());
    } catch (IOException ioe) {
      System.out.println("Unexpected exception: " + ioe.getMessage());
    }
  }

  public static void main(String[] args) {
    if (args.length != 2) {
      System.out.println("Usage: java com.im.chatBio.ChatClient host port");
    } else {
      new ChatClient(args[0], Integer.parseInt(args[1]));
    }
  }

  public void run() {
    while (true) {
      try {
        if (console != null) {
          InputStreamReader in = new InputStreamReader(console, "utf-8");
          BufferedReader reader = new BufferedReader(in);
          String str = reader.readLine();
          if (str != null) {
            streamOut.writeUTF(str);
            streamOut.flush();
          }
        }
      } catch (IOException ioe) {
        System.out.println("Sending error: " + ioe.getMessage());
        stop();
      }
    }
  }

  public void stop() {
    try {
      if (console != null) {
        console.close();
      }
      if (streamOut != null) {
        streamOut.close();
      }
      if (socket != null) {
        socket.close();
      }
    } catch (IOException ioe) {
      System.out.println("Error closing ...");
    }
    client.close();
  }

  public void handle(String msg) {
    if (msg.equals(".bye")) {
      System.out.println("Good bye. Press RETURN to exit ...");
      stop();
    } else {
      System.out.println(msg);
    }
  }

  /**
   * @throws IOException
   */
  private void start() throws IOException {
    console = new DataInputStream(System.in);
    streamOut = new DataOutputStream(socket.getOutputStream());
    ExecutorService executorService = Executors.newFixedThreadPool(2);
    executorService.execute(new ChatClientThread(this, socket));
    executorService.execute(this);
  }
}