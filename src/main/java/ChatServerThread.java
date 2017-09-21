import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatServerThread extends Thread {
    private Socket socket;
    private int Id = -1;
    private DataInputStream streamIn = null;

    public ChatServerThread(ChatServer server, Socket socket) {
        this.socket = socket;
        Id = socket.getPort();
    }


    @Override
    public void run() {
        System.out.println("Server Thread" + Id + " is running");
        while (true) {
            try {
                System.out.println(streamIn.readUTF());
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        }
    }

    public void open() throws IOException {
        streamIn = new DataInputStream(socket.getInputStream());
    }

    public void close() throws IOException {
        if(socket != null) {
            socket.close();
        }
        if(streamIn != null) {
            streamIn.close();
        }
    }
}
