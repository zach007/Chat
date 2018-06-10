package com.im;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.im.mapper")
public class ImApplication implements CommandLineRunner {
  private static final Logger logger = LogManager.getLogger(ImApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(ImApplication.class, args);
  }


  @Override
  public void run(String... args) {
    logger.info("spring boot testing ");
  }
}
