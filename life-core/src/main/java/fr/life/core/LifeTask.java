package fr.life.core;

import java.util.Date;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.city.core.City;
import fr.network.transport.application.Session;

public class LifeTask extends TimerTask {

	private static final Logger LOG = LoggerFactory.getLogger(LifeTask.class);

	private City city;
	private Session session;

	public LifeTask(City city, Session session) {
		this.city = city;
		this.session = session;
	}

	public void destroy() {
		this.city.destroy();
	}

	@Override
	public void run() {
		LOG.info("LifeTask Start time: {}", new Date());
		doSomeWork();
		LOG.info("LifeTask End time: {}", new Date());
	}

	private void doSomeWork() {
		// do something
	}

}
