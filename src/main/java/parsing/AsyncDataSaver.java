package parsing;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsyncDataSaver {
	private static final Logger LOGGER = LoggerFactory.getLogger(AsyncDataSaver.class);
	private ScheduledExecutorService scheduledExecutorService;
	private Observer observer;
	private List<Observable> observables;
	
	public AsyncDataSaver(){
		observables = new ArrayList<Observable>();
	}
	
	public void addObserver(Observer observer){
		this.observer = observer;
	}
	
	public void removeObserver(){
		for(Observable obs : observables){
			obs.deleteObservers();
		}
	}
	
	public void downloadAndSaveData(ConfigObject conf) {
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		for(ConfigElement elem : conf) {
			LOGGER.info(elem.getListOfUrls().toString());
			
			DataSaver dataSaver = createDataSaver(elem);
			observables.add(dataSaver);
			MyThread t1 = new MyThread(dataSaver);
			
			long period = Long.parseLong(elem.getPeriod());
			TimeUnit timeUnt = TimeUnit.valueOf(elem.getTimeUnit());
			scheduledExecutorService.scheduleAtFixedRate(t1, 0, period, timeUnt);
		}
	}
	
	private DataSaver createDataSaver(ConfigElement elem){
		DataSaver dataSaver = null;
		
		Observer observer = null;
		if(elem.isObservable()){
			observer = this.observer;
		}
		
		File f = new File(elem.getFileName());
		try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Parser p = null;
		try {
			p = createParser(elem.getParser());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		dataSaver = new DataSaver(f, elem.getListOfUrls(), p);
		dataSaver.addObserver(observer);
		return dataSaver;
	}
	
	private Parser createParser(String parser) throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		Class<?> c = Class.forName(parser);
		return (Parser) c.newInstance();
	}
	
	public void stop(){
		LOGGER.info("shutting down shceduler");
		if(scheduledExecutorService==null){
			return;
		}
		scheduledExecutorService.shutdown();
	}
	
	private class MyThread implements Runnable{
		DataSaver dataSaver;
		
		public MyThread(DataSaver dataSaver){
			this.dataSaver = dataSaver;
		}

		@Override
		public void run() {
			try {
				dataSaver.downloadParseSave();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}
}
