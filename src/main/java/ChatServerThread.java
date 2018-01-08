import java.io.*;
import java.net.Socket;

public class ChatServerThread extends Thread {
  private ChatServer server = null;
  private Socket socket = null;
  private int threadId = 0;
  private DataInputStream streamIn = null;
  private DataOutputStream streamOut = null;

  public ChatServerThread(ChatServer chatServer, Socket currentSocket) {
    super();
    this.server = chatServer;
    this.socket = currentSocket;
    threadId = socket.getPort();
  }

  public int getThreadId() {
    return threadId;
  }

  public void send(String msg) {
    try {
      streamOut.writeUTF(msg);
      streamOut.flush();
    } catch (IOException ioe) {
      System.out.println(threadId + " ERROR sending: " + ioe.getMessage());
      server.remove(threadId);
      interrupt();
    }
  }

  public void run() {
    System.out.println("Server Thread " + threadId + " running.");
    while (true) {
      try {
        server.handle(threadId, streamIn.readUTF());
      } catch (IOException ioe) {
        System.out.println(threadId + " ERROR reading: " + ioe.getMessage());
        server.remove(threadId);
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