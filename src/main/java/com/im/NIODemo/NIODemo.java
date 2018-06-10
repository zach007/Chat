package com.im.NIODemo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIODemo {

  public static void main(String args[]) {
    RandomAccessFile aFile = null;
    try {
      aFile = new RandomAccessFile("E:\\IDE\\IntelliJ IDEA\\IM\\Chat\\src\\main\\java\\com.im.NIODemo\\test.txt", "rw");

    FileChannel inChannel = aFile.getChannel();

    ByteBuffer buf = ByteBuffer.allocate(48);

    int bytesRead = inChannel.read(buf);
    while (bytesRead != -1) {

      System.out.println("Read " + bytesRead);
      buf.flip();

      while (buf.hasRemaining()) {
        System.out.print((char) buf.get());
      }

      buf.clear();
      bytesRead = inChannel.read(buf);
    }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (aFile != null) {
        try {
          aFile.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

    }
  }

}
