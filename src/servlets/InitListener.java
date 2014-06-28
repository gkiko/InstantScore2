package servlets;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import parsing.AsyncDataSaver;
import parsing.ConfigObject;
import utils.ConfigUtils;

/**
 * Application Lifecycle Listener implementation class InitListener
 *
 */
@WebListener
public class InitListener implements ServletContextListener {
	private static final Logger LOGGER = Logger.getLogger(InitListener.class.getName());
	private final AsyncDataSaver asyncDataSaver = new AsyncDataSaver();;
	
    /**
     * Default constructor. 
     */
    public InitListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0) {
    	ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
			
			@Override
			public void run() {
				ConfigObject conf = new ConfigObject();
				conf.createFrom(getClass().getResourceAsStream("/utils/config.json"));
				
				ConfigUtils.modifyUrlFields(conf);
				asyncDataSaver.stop();
				asyncDataSaver.downloadAndSaveData(conf);
				
			}
		}, 0, 1, TimeUnit.DAYS);
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
        // TODO Auto-generated method stub
    }
	
}
