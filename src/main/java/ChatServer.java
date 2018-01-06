import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer implements Runnable {

  private Socket socket = null;
  private ServerSocket server = null;
  private DataInputStream streamIn = null;
  private Thread thread = null;


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
      start();
    } catch (IOException ioe) {
      System.out.println(ioe);
    }
  }

  public void run() {
    while (thread != null) {
      try {
        System.out.println("Waiting for a client ...");
        socket = server.accept();
        System.out.println("Client accepted: " + socket);
        open();
        boolean done = false;
        while (!done) {
          try {
            String line = streamIn.readUTF();
            System.out.println(line);
            done = line.equals(".bye");
          } catch (IOException ioe) {
            done = true;
          }
        }
        close();
      } catch (IOException ie) {
        System.out.println("Acceptance Error: " + ie);
      }
    }
  }

  public void start() {
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

  public void open() throws IOException {
    streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
  }

  public void close() throws IOException {
    if (socket != null) {
      socket.close();
    }
    if (streamIn != null) {
      streamIn.close();
    }
  }
}