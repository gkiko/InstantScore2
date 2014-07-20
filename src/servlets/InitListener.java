package servlets;

import java.io.InputStreamReader;
import java.util.Observer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import notifier.UpdateNotifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import parsing.AsyncDataSaver;
import parsing.ConfigObject;
import subscribtion.SubscribtionManager;
import utils.ConfigUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import database.ConnectionPooler;

/**
 * Application Lifecycle Listener implementation class InitListener
 *
 */
@WebListener
public class InitListener implements ServletContextListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(InitListener.class);
	private final AsyncDataSaver asyncDataSaver = new AsyncDataSaver();
	private ScheduledExecutorService scheduledExecutorService;
	private Observer observer = new UpdateNotifier();
	private SubscribtionManager subscribtionManager = SubscribtionManager.getInstance();
	
    /**
     * Default constructor. 
     */
    public InitListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(final ServletContextEvent arg0) {
    	arg0.getServletContext().setAttribute("subscribtion_manager", subscribtionManager);
    	ConnectionPooler.InitializePooler();
    	
    	scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
			
			@Override
			public void run() {
				Gson gson = new GsonBuilder().create();
				ConfigObject conf = gson.fromJson(new InputStreamReader(getClass().getResourceAsStream("/utils/config.json")), ConfigObject.class);
				arg0.getServletContext().setAttribute("config", conf);
				
				ConfigUtils.modifyUrlFields(conf);
				asyncDataSaver.stop();
				asyncDataSaver.addObserver(observer);
				asyncDataSaver.downloadAndSaveData(conf);
				
			}
		}, 0, 1, TimeUnit.DAYS);
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
    	asyncDataSaver.removeObserver();
    	asyncDataSaver.stop();
    	scheduledExecutorService.shutdown();
    	ConnectionPooler.ReleasePooler();
    }
	
}
