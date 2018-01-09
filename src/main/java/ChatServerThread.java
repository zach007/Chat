import java.io.*;
import java.net.Socket;

public class ChatServerThread extends Thread {
  private ChatServer server = null;
  private Socket socket = null;
  private int port = 0;
  private DataInputStream streamIn = null;
  private DataOutputStream streamOut = null;

  public ChatServerThread(ChatServer chatServer, Socket currentSocket) {
    super();
    this.server = chatServer;
    this.socket = currentSocket;
    port = socket.getPort();
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
      server.remove(port);
      interrupt();
    }
  }

  public void run() {
    System.out.println("Server Thread " + port + " running.");
    while (true) {
      try {
        server.handle(port, streamIn.readUTF());
      } catch (IOException ioe) {
        System.out.println(port + " ERROR reading: " + ioe.getMessage());
        server.remove(port);
        interrupt();
      }
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