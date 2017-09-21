import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer implements Runnable {
    private ServerSocket server = null;
    private Thread thread = null;
    private ChatServerThread client = null;

    public ChatServer(int port) {

        System.out.println("Binding to port " + port + ", please wait  ...");
        try {
            server = new ServerSocket(port);
            System.out.println("Server start" + server);
            start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void run() {
        while (thread != null) {
            try {
                System.out.println("waiting for a client");
                Socket accept = server.accept();
                addThread(accept);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addThread(Socket socket) {
        System.out.println("Client accpted " + socket);
        client = new ChatServerThread(this, socket);
        try {
            client.open();
            client.start();
        } catch (Exception e) {
            e.printStackTrace();
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

    public static void main(String args[]) {
        if(args.length != 1)
            System.out.println("Usage: java ChatServer port");
        else
            new ChatServer(Integer.parseInt(args[0]));
    }

}

