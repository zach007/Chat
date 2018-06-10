package com.im.chatBio;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChatServerTest {
  private ChatServer chatServer;

  @BeforeEach
  void setUp() {
    chatServer = new ChatServer(8088);
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void run() {
  }

  @Test
  void handle() {
  }

  @Test
  void keepServerAlive() {
  }

  @Test
  void remove() {
  }

  @Test
  void stop() {
  }
}