import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient {
  private Socket socket = null;
  private DataInputStream console = null;
  private DataOutputStream streamOut = null;

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
    String line = "";
    while (!line.equals(".bye")) {
      try {
        InputStreamReader in = new InputStreamReader(System.in, "utf-8");
        BufferedReader bufferedReader = new BufferedReader(in);
        String readLine = bufferedReader.readLine();
        if (readLine != null) {
          line = readLine;
        }
        streamOut.writeUTF(line);
        streamOut.flush();
      } catch (IOException ioe) {
        System.out.println("Sending error: " + ioe.getMessage());
      }
    }
  }

  public static void main(String[] args) {
    if (args.length != 2) {
      System.out.println("Usage: java ChatClient host port");
    } else {
      new ChatClient(args[0], Integer.parseInt(args[1]));
    }
  }

  private void start() throws IOException {
    console = new DataInputStream(System.in);
    streamOut = new DataOutputStream(socket.getOutputStream());
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
  }
}