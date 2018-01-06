import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatServerThread extends Thread {
  private Socket socket = null;
  private int threadId = 0;
  private DataInputStream streamIn = null;

  public ChatServerThread(Socket currentSocket) {
    this.socket = currentSocket;
    threadId = socket.getPort();
  }

  public void run() {
    System.out.println("Server Thread " + threadId + " running.");
    while (true) {
      try {
        System.out.println(streamIn.readUTF());
      } catch (IOException ioe) {
        System.out.println(ioe.getMessage());
      }
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