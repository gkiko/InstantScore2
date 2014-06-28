package parsing;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import utils.config.ConfigUtils;

public class AsyncDataSaver {
	private ScheduledExecutorService scheduledExecutorService;
	
	public static void main(String[] args) {
		ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
			
			@Override
			public void run() {
				ConfigObject conf = new ConfigObject();
				try {
					conf.createFrom("config.json");
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				
				ConfigUtils.modifyUrlFields(conf);
				AsyncDataSaver asyncDataSaver = new AsyncDataSaver();
				asyncDataSaver.downloadAndSaveData(conf);
				
			}
		}, 0, 1, TimeUnit.DAYS);
	}
	
	public void downloadAndSaveData(ConfigObject conf) {
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		for(ConfigElement elem : conf) {
			MyThread t1 = new MyThread(elem.getFileName(), elem.getListOfUrls(), elem.getParser());
			
			long period = Long.parseLong(elem.getPeriod());
			TimeUnit timeUnt = TimeUnit.valueOf(elem.getTimeUnit());
			scheduledExecutorService.scheduleAtFixedRate(t1, 0, period, timeUnt);
		}
	}
	
	public void stop(){
		if(scheduledExecutorService==null){
			return;
		}
		scheduledExecutorService.shutdown();
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
