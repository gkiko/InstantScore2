package notifier;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Queuer {
	final static Logger logger = LoggerFactory.getLogger(Queuer.class);
	private static final int workers = 50;
	private static CompletionService<Void> completionService;
	private static ExecutorService pool;
	private static MsgSender msgSender;

	public static void init() {
		pool = Executors.newFixedThreadPool(workers);
		completionService = new ExecutorCompletionService<>(pool);
		msgSender = MsgSender.getInstance();

		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						Future<Void> fut = completionService.take();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});

		t.start();
	}

	public static Future<Void> queueJob(final String txt, final String phoneNum, final Callable<Void> callable) {
		Future<Void> res = completionService.submit(new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				msgSender.sendMsgToUser(txt, phoneNum);
				callable.call();
				return null;
			}

		});

		return res;
	}

	public static void shutDown() {
		pool.shutdown();
	}
}
