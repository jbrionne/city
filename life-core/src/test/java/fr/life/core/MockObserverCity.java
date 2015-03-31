package fr.life.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.city.core.Building;
import fr.city.core.ObserverCity;
import fr.city.core.Road;

public class MockObserverCity implements ObserverCity {

	private static final Logger LOG = LoggerFactory
			.getLogger(MockObserverCity.class);

	@Override
	public void createBuilding(Building b) {
		LOG.info("createBuilding");
	}

	@Override
	public void removeBuilding(Building b) {
		LOG.info("removeBuilding");
	}

	@Override
	public void createTransport(Building b) {
		LOG.info("createTransport");
	}

	@Override
	public void removeTransport(Building b) {
		LOG.info("removeTransport");
	}

	@Override
	public void moveTransport(Building b) {
		LOG.info("moveTransport");
	}

	@Override
	public void createRoad(Road r) {
		LOG.info("createRoad");
	}

	@Override
	public void removeRoad(Road r) {
		LOG.info("removeRoad");
	}

}
