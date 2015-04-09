package fr.life.core;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.city.core.City;
import fr.city.core.CityBase;
import fr.city.core.ObserverCity;
import fr.city.core.PhysicalMoveCity;
import fr.network.transport.application.Session;
import fr.network.transport.application.SessionBase;

public class Ecosystem {

	private static final Logger LOG = LoggerFactory.getLogger(Ecosystem.class);

	private static Ecosystem ecosystem;

	private Session session;

	public Session getSession() {
		return session;
	}

	private static final long PERIOD = 10L * 1000L;

	private CityBase city;

	private static Object monitor = new Object();

	private Ecosystem(ObserverCity myObserverCity) {
		LOG.info("Ecosystem");
		this.city = CityBase.getInstance(myObserverCity, "TEST", false);
		this.session = SessionBase.getInstance(new PhysicalMoveCity(city));
		TimerTask timerTask = new LifeTask(city, session);
		Timer timer = new Timer(true);
		timer.scheduleAtFixedRate(timerTask, 5000, PERIOD);
		LOG.info("Life begins! : {}", new Date());
	}

	public static Ecosystem getInstance(ObserverCity myObserverCity) {
		if (ecosystem == null) {
			synchronized (monitor) {
				if (ecosystem == null) {
					ecosystem = new Ecosystem(myObserverCity);
					return ecosystem;
				}
			}
		}
		return ecosystem;

	}

	public City getCity() {
		return city;
	}
}
