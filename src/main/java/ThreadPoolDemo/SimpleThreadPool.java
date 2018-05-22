package ThreadPoolDemo;

import java.util.concurrent.*;

public class SimpleThreadPool {
  @Deprecated
  public static void test(String args[]) {
    ExecutorService executorService = Executors.newFixedThreadPool(5);
    for (int i = 0; i < 10; i++) {
      Runnable worker = new WorkThread("" + i);
      executorService.execute(worker);
    }
    executorService.shutdown();
    while (!executorService.isTerminated()) {
    }
    System.out.println("Finish All threads");
  }

  public static void main(String args[]) throws InterruptedException {
//RejectedExecutionHandler implementation
    RejectedExecutionHandlerImpl rejectionHandler = new RejectedExecutionHandlerImpl();
//Get the ThreadFactory implementation to use
    ThreadFactory threadFactory = Executors.defaultThreadFactory();
//creating the ThreadPoolExecutor
    ThreadPoolExecutor executorPool = new ThreadPoolExecutor(2, 4, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(2), threadFactory, rejectionHandler);
//start the monitoring thread
    MyMonitorThread monitor = new MyMonitorThread(executorPool, 3);
    Thread monitorThread = new Thread(monitor);
    monitorThread.start();
//submit work to the thread pool
    for (int i = 0; i < 10; i++) {
      executorPool.execute(new WorkThread("cmd" + i));
    }
    Thread.sleep(30000);
//shut down the pool
    executorPool.shutdown();
//shut down the monitor thread
    Thread.sleep(5000);
    monitor.shutdown();
  }
}
