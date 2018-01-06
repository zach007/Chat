import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer implements Runnable {

  private ServerSocket server = null;
  private Thread thread = null;
  private ChatServerThread client = null;

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

  public void run() {
    while (thread != null) {
      try {
        System.out.println("Waiting for a client ...");
        Socket socket = server.accept();
        addThread(socket);
      } catch (IOException ie) {
        System.out.println("Acceptance Error: " + ie);
      }
    }
  }

  private void addThread(Socket socket) {
    System.out.println("Client accepted: " + socket);
    client = new ChatServerThread(socket);
    try {
      client.open();
      client.start();
    } catch (IOException ioe) {
      System.out.println("Error opening thread: " + ioe);
    }
  }

  public void keepServerAlive() {
    if (thread == null) {
      thread = new Thread(this);
      thread.start();
    }
  }

  public static void main(String[] args) {
    if (args.length != 1) {
      System.out.println("Usage: java ChatServer port");
    } else {
      new ChatServer(Integer.parseInt(args[0]));
    }
  }

}