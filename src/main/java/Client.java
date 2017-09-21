import java.io.*;
import java.net.Socket;

/**
 * Created by zach on 2017/7/7.
 */
public class Client {
    private Socket socket = null;
    private BufferedReader console = null;
    private DataOutputStream streamOut = null;

    public Client(String serverName, int serverPort) {
        System.out.println("Establishing connection. Please wait ...");
        try {
            socket = new Socket(serverName, serverPort);
            System.out.println("Connected: " + socket);
            start();
        } catch (IOException ioe) {
            System.out.println("Unexpected exception: " + ioe.getMessage());

        }
        String line = "";
        while (!line.equals(".bye")) {
            try {
                line = console.readLine();
                streamOut.writeUTF(line);
                streamOut.flush();
            } catch (IOException ioe) {
                System.out.println("Sending error: " + ioe.getMessage());
            }
        }
    }

    public void start() throws IOException {
        console = new BufferedReader(new InputStreamReader(System.in));
        streamOut = new DataOutputStream(socket.getOutputStream());
    }

    public void stop() {
        try {
            if (console != null) console.close();
            if (streamOut != null) streamOut.close();
            if (socket != null) socket.close();
        } catch (IOException ioe) {
            System.out.println("Error closing ...");
        }
    }

    public static void main(String args[]) {
        if(args.length != 2)
            System.out.println("Usage: java ChatClient host port");
        else
            new Client(args[0], Integer.parseInt(args[1]));
    }
}
