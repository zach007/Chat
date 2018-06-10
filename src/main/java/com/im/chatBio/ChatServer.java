package com.im.chatBio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer implements Runnable {

  private ChatServerThread[] clients = new ChatServerThread[50];
  private ServerSocket server = null;
  private Thread thread = null;
  private int clientCount = 0;

  /**
   * 监听客户端.
   *
   * @param port 端口
   */
  public ChatServer(int port) {
    try {
      System.out.println("Binding to port " + port + ", please wait  ...");
      server = new ServerSocket(port);
      System.out.println("Server started: " + server);
      keepServerAlive();//保持server永驻
    } catch (IOException ioe) {
      System.out.println(ioe);
    }
  }

  public static void main(String[] args) {
    //new com.im.chatBio.ChatServer(9001);
    if (args.length != 1) {
      System.out.println("Usage: java com.im.chatBio.ChatServer port");
    } else {
      new ChatServer(Integer.parseInt(args[0]));
    }
  }

  /**
   * @param socket 每一个建立的socket连接都创建一个新线程，并且确保客户端最大连接数.
   */
  private void addConnection(Socket socket) {
    if (clientCount < clients.length) {
      System.out.println("Client accepted: " + socket);
      clients[clientCount] = new ChatServerThread(this, socket);
      try {
        clients[clientCount].open();
        clients[clientCount].start();
        clientCount++;
      } catch (IOException ioe) {
        System.out.println("Error opening thread: " + ioe);
      }
    } else {
      System.out.println("Client refused: maximum " + clients.length + " reached.");
    }
  }

  private int findClient(int port) {
    for (int i = 0; i < clientCount; i++) {
      if (clients[i].getPort() == port) {
        return i;
      }
    }
    return -1;
  }

  /**
   * 将获取当前线程id,并且将线程所输入的信息，发送给其他线程.
   *
   * @param port    线程id
   * @param input 线程输入信息
   */
  public synchronized void handle(int port, String input) {
    if (input.equals(".bye")) {
      clients[findClient(port)].send(".bye");
      remove(port);
    } else {
      for (int i = 0; i < clientCount; i++) {
        clients[i].send(port + ": " + input);
      }
    }
  }

  @Override
  public void run() {
    while (thread != null) {
      try {
        System.out.println("Waiting for a client ...");
        Socket socket = server.accept();
        addConnection(socket);
      } catch (IOException ie) {
        System.out.println("Acceptance Error: " + ie);
        stop();
      }
    }
  }

  /**
   * 找到当前线程数组所在位置，删除线程.
   *
   * @param port 当前线程id
   */
  public synchronized void remove(int port) {
    int pos = findClient(port);
    if (pos >= 0) {
      ChatServerThread toTerminate = clients[pos];
      System.out.println("Removing client thread " + port + " at " + pos);
      if (pos < clientCount - 1) {
        for (int i = pos + 1; i < clientCount; i++) {
          clients[i - 1] = clients[i];
        }
      }
      clientCount--;
      try {
        toTerminate.close();
      } catch (IOException ioe) {
        System.out.println("Error closing thread: " + ioe);
      }
      toTerminate.interrupt();
    }
  }

  public void stop() {
    if (thread != null) {
      thread.interrupt();
      thread = null;
    }
  }

  /**
   * make ChatServer run in Thread
   */
  public void keepServerAlive() {
    if (thread == null) {
      thread = new Thread(this);
      thread.start();
    }
  }
}