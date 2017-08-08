import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by zach on 2017/7/7.
 */
public class Server implements Runnable{
    //1 new SeverSocket(port) that the clinet will find you
    //2 listening the client and revice data throuht Socket (a kind of IO)
    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream streamIn = null;
    private Thread  thread = null;

    public Server(int port) {

        System.out.println("Binding to port " + port + ", please wait  ...");
        try {
            server = new ServerSocket(port);
            start();
        } catch(IOException e) {
            e.printStackTrace();
        }

    }

    public void run() {
        while (thread != null) {
            try {
                System.out.println("Server started: " + server);
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void start() {
        if(thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    public void stop() {
        if(thread != null) {
            thread.stop();
            thread = null;
        }
    }

    public void open() throws IOException {
        streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
    }

    public void close() throws IOException {
        if(socket != null) socket.close();
        if(streamIn != null) streamIn.close();
    }

    public static void main(String args[]) {
        Server server = new Server(9999);
    }


}
