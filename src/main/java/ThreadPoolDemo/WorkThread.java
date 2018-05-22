package ThreadPoolDemo;

public class WorkThread implements Runnable {
  private String command;

  public WorkThread(String command) {
    this.command = command;
  }

  public void run() {
    System.out.println(Thread.currentThread().getName() + " Start Command= " + command);
    proceeCommand();
    System.out.println(Thread.currentThread().getName() + " End");

  }

  private void proceeCommand() {
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Override
  public String toString() {
    return this.command;
  }
}
