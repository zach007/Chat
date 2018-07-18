package com.im.chatBio;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public class RejectHandler implements RejectedExecutionHandler {
  private static final Logger logger = LogManager.getLogger(RejectHandler.class);

  @Override
  public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {

    logger.error(r + "is out of server");
  }
}
