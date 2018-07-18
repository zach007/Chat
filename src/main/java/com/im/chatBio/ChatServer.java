package com.im.chatBio;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.*;

public class ChatServer implements Runnable {
  private static final Logger logger = LogManager.getLogger(ChatServer.class);
  private List<ChatServerThread> serverThreadList = Lists.newArrayList();
  private ServerSocket server = null;
  private ThreadPoolExecutor threadPoolExecutor;

  /**
   * 监听客户端
   *
   * @param port 端口
   */
  public ChatServer(int port) {
    try {
      System.out.println("Binding to port " + port + ", please wait  ...");
      server = new ServerSocket(port);
      System.out.println("Server started: " + server);
      ExecutorService executorService = Executors.newSingleThreadExecutor();
      executorService.execute(this);
    } catch (IOException ioe) {
      System.out.println(ioe);
    }
  }

  public static void main(String[] args) {
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
    ArrayBlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<>(50);
    RejectHandler rejectHandler = new RejectHandler();
    ThreadFactory threadFactory = new ThreadFactoryBuilder().setDaemon(true).setNameFormat("chat-%d").build();
    threadPoolExecutor = new ThreadPoolExecutor(2, 5, 10, TimeUnit.MINUTES, blockingQueue, threadFactory, rejectHandler);
    System.out.println("Client accepted: " + socket);
    ChatServerThread chatServerThread = new ChatServerThread(this, socket);
    serverThreadList.add(chatServerThread);
    threadPoolExecutor.execute(chatServerThread);
  }

  /**
   * 将获取当前线程id,并且将线程所输入的信息，发送给其他线程.
   *
   * @param chatServerThread 当前线程
   * @param input 线程输入信息
   */
  public synchronized void handle(ChatServerThread chatServerThread, String input) {
    int port = chatServerThread.getPort();
    if (input.equals(".bye")) {
      chatServerThread.send(".bye");
      remove(chatServerThread);
    } else {
      for (ChatServerThread serverThread : serverThreadList) {
        serverThread.send(port + ":" + input);
      }
    }
  }

  @Override
  public void run() {
    while (true) {
      try {
        System.out.println("Waiting for a client ...");
        Socket socket = server.accept();
        addConnection(socket);
      } catch (IOException ie) {
        System.out.println("Acceptance Error: " + ie);
      }
    }
  }

  /**
   * 找到当前线程数组所在位置，删除线程.
   *
   * @param chatServerThread 当前线程
   */
  public synchronized void remove(ChatServerThread chatServerThread) {
    serverThreadList.remove(chatServerThread);
    int port = chatServerThread.getPort();
    try {
      chatServerThread.close();
    } catch (IOException e) {
      System.out.println("Error closing thread :" + e);
    }
    System.out.println("Removing client thread " + port);
    /*int pos = findClient(port);
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
     */ //toTerminate.interrupt();
  }
}