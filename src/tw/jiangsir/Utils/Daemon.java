package tw.jiangsir.Utils;

public class Daemon implements Runnable {

	public void run() {
		// String key = String.valueOf(new Date().getTime());
		// ENV.ThreadPool.put(key, Thread.currentThread());
		System.out.println("Daemon 啟動");
		while (true) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			}
		}
	}

	public void interrupt() {
		System.out.println("Daemon 中斷");
		Thread.currentThread().interrupt();
	}
}
