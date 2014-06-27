package parsing;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AsyncDataSaver {
	
	public static void main(String[] args) throws IOException {
		ConfigObject conf = new ConfigObject();
		conf.createFrom("config.json");
		
		AsyncDataSaver asyncDataSaver = new AsyncDataSaver();
		asyncDataSaver.downloadAndSaveData(conf);
	}
	
	public void downloadAndSaveData(ConfigObject conf) {
		ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		for(ConfigElement elem : conf) {
			MyThread t1 = new MyThread(elem.fileName, elem.listOfUrls, elem.parser);
			
			long period = Long.parseLong(elem.period);
			TimeUnit timeUnt = TimeUnit.valueOf(elem.timeUnit);
			scheduledExecutorService.scheduleAtFixedRate(t1, 0, period, timeUnt);
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
			
			Parser p = null;
			try {
				p = createParser(parser);
			} catch (Exception e) {
				e.printStackTrace();
			}
			DataSaver saver = new DataSaver(f, listOfUrls, p);
			try {
				saver.downloadParseSave();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		private Parser createParser(String parser) throws InstantiationException, IllegalAccessException, ClassNotFoundException{
			Class<?> c = Class.forName(parser);
			return (Parser) c.newInstance();
		}
		
	}
}
