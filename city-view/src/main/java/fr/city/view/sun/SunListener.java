package fr.city.view.sun;

import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.city.view.Cityboard;

public class SunListener implements ServletContextListener {

	private static final Logger LOG = LoggerFactory.getLogger(Cityboard.class);

	private static final int PERIOD = 1000 * 30;
	private static final int DELAY = 1000 * 30;
	private TimerTask sunTask;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		sunTask = new SunTask();
		Timer timer = new Timer(true);
		timer.scheduleAtFixedRate(sunTask, DELAY, PERIOD);
		LOG.info("Listener sun");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		if (sunTask != null) {
			sunTask.cancel();
		}
	}
}
