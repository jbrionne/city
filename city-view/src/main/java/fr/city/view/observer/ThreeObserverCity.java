package fr.city.view.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.city.core.Building;
import fr.city.core.ObserverCity;
import fr.city.core.Road;
import fr.city.view.MyServerConfigurator;

public class ThreeObserverCity implements ObserverCity {
	
	private static final Logger LOG = LoggerFactory
			.getLogger(ThreeObserverCity.class);
	
	private static final ThreeObserverCity THREEOBSERVERCITY = new ThreeObserverCity();
	
	public static ThreeObserverCity getInstance(){
		return THREEOBSERVERCITY;
	}		

	@Override
	public void createBuilding(Building b) {
		LOG.debug("createBuilding");
		try {
			MyServerConfigurator.CITYBOARD.createBuilding(b, "create");
		} catch (Exception e) {
			LOG.error("createBuilding", e);
		}
	}

	@Override
	public void removeBuilding(Building b) {
		LOG.debug("removeBuilding");
		try {
			MyServerConfigurator.CITYBOARD.removeBuilding(b);
		} catch (Exception e) {
			LOG.error("removeBuilding", e);
		}		
	}

	@Override
	public void createTransport(Building b) {
		LOG.debug("createTransport");
		try {
			MyServerConfigurator.CITYBOARD.createTransport(b);
		} catch (Exception e) {
			LOG.error("createTransport", e);
		}		
	}

	@Override
	public void moveTransport(Building b) {
		LOG.debug("moveTransport");
		try {
			MyServerConfigurator.CITYBOARD.moveTransport(b);
		} catch (Exception e) {
			LOG.error("moveTransport", e);
		}			
	}

	@Override
	public void createRoad(Road r) {
		LOG.debug("createRoad");
		try {
			MyServerConfigurator.CITYBOARD.createRoad(r);
		} catch (Exception e) {
			LOG.error("removeBuilding", e);
		}	
		
	}

	@Override
	public void removeTransport(Building b) {
		LOG.debug("removeTransport");
		try {
			MyServerConfigurator.CITYBOARD.removeTransport(b);
		} catch (Exception e) {
			LOG.error("removeTransport", e);
		}
	}

	@Override
	public void removeRoad(Road r) {
		LOG.debug("removeRoad");
		try {
			MyServerConfigurator.CITYBOARD.removeRoad(r);
		} catch (Exception e) {
			LOG.error("removeRoad", e);
		}
		
	}

}
