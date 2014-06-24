package parsing;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.sun.tools.javac.util.List;

public class AsyncDataSaver {
	private static ConfigObject conf;
	
	public static void main(String[] args) throws IOException {
		conf = new ConfigObject();
		conf.createFrom("config.json");
		
		AsyncDataSaver asyncDataSaver = new AsyncDataSaver();
		asyncDataSaver.downloadAndSaveData();
	}
	
	public void downloadAndSaveData() {
		ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		for(ConfigElement elem : conf) {
			System.out.println(elem.fileName);
			MyThread t1 = new MyThread(elem.fileName, elem.listOfUrls, "");
			
			scheduledExecutorService.scheduleAtFixedRate(t1, 0, 5, TimeUnit.SECONDS);
		}
	}
	
	private class MyThread implements Runnable{
		private String file, parser;
		
		private java.util.List<String> listOfUrls;
		
		public MyThread(String fileName, java.util.List<String> listOfUrls2, String parser){
			file = fileName;
			this.listOfUrls = listOfUrls2;
			this.parser = parser;
		}

		@Override
		public void run() {
			File f = new File(file);
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			DataSaver saver = new DataSaver(f, listOfUrls, new BasicParser());
			try {
				saver.downloadParseSave();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}
}
