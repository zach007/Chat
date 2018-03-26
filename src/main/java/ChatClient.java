import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient implements Runnable {
  private Socket socket = null;
  private DataInputStream console = null;
  private DataOutputStream streamOut = null;
  private Thread thread = null;
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
    //new ChatClient("localhost", 9001);
    if (args.length != 2) {
      System.out.println("Usage: java ChatClient host port");
    } else {
      new ChatClient(args[0], Integer.parseInt(args[1]));
    }
  }

  private void start() throws IOException {
    console = new DataInputStream(System.in);
    streamOut = new DataOutputStream(socket.getOutputStream());
    if (thread == null) {
      client = new ChatClientThread(this, socket);
      thread = new Thread(this);
      thread.start();
    }
  }

  public void stop() {
    if (thread != null) {
      thread.interrupt();
      thread = null;
    }
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
    client.interrupt();
  }

  public void run() {
    while (thread != null) {
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

  /**
   * @param msg  testing jenkins haha
   */
  public void handle(String msg) {
    if (msg.equals(".bye")) {
      System.out.println("Good bye. Press RETURN to exit ...");
      stop();
    } else {
      System.out.println(msg);
    }
  }
}