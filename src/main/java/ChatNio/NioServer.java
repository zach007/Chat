package ChatNio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;

public class NioServer {
  private Selector selector;
  private Map<SocketChannel, List> dataMapper;
  private InetSocketAddress address;

  public NioServer(String address, int port) {
    this.address = new InetSocketAddress(address, port);
    this.dataMapper = new HashMap<SocketChannel, List>();
  }

  public void startServer() {
    ServerSocketChannel serverChannel = null;
    try {
      this.selector = Selector.open();
      serverChannel = ServerSocketChannel.open();
      serverChannel.configureBlocking(false);
      serverChannel.socket().bind(address);
      serverChannel.register(selector, SelectionKey.OP_ACCEPT);
      System.out.println("server starting ...");
      SelectorHandle();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (serverChannel != null) {
        try {
          serverChannel.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private void SelectorHandle() throws IOException {
    while (true) {

      //wait for events
      this.selector.select();
      //work on selector keys
      Set<SelectionKey> selectionKeys = this.selector.selectedKeys();
      Iterator<SelectionKey> keys = selectionKeys.iterator();
      while (keys.hasNext()) {
        SelectionKey key = keys.next();
        keys.remove();
        if (key.isValid()) {
          continue;
        }

        if (key.isAcceptable()) {
          this.accept(key);
        }

        if (key.isReadable()) {
          this.read(key);
        }

        if (key.isWritable()) {
          this.write(key);
        }
      }
    }
  }

  private void write(SelectionKey key) {
  }

  private void read(SelectionKey key) throws IOException {
    SocketChannel channel = (SocketChannel) key.channel();
    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
    int numRead = -1;
    int read = channel.read(byteBuffer);

    if (numRead == -1) {
      this.dataMapper.remove(channel);
      Socket socket = channel.socket();
      SocketAddress remoteAddr = socket.getRemoteSocketAddress();
      System.out.println("Connection close by Client" + remoteAddr);
      channel.close();
      key.cancel();
      return;
    }

    byte[] data = new byte[numRead];
    System.arraycopy(byteBuffer.array(), 0, data, 0, numRead);
    System.out.println("Got " + new String(data));
  }

  private void accept(SelectionKey key) throws IOException {
    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
    SocketChannel channel = serverSocketChannel.accept();
    serverSocketChannel.configureBlocking(false);
    Socket socket = channel.socket();
    SocketAddress remoteAddr = socket.getRemoteSocketAddress();
    System.out.println("Connect to " + remoteAddr);

    dataMapper.put(channel, new ArrayList());
    channel.register(selector, SelectionKey.OP_READ);
  }
}
