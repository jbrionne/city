package fr.life.core;

import java.awt.Color;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.city.core.Building;
import fr.city.core.City;
import fr.city.core.Coordinates;
import fr.city.core.Road;
import fr.network.transport.api.TransportMove;
import fr.network.transport.application.Session;
import fr.network.transport.network.Address;

public class CityRequest {

	private static final Logger LOG = LoggerFactory
			.getLogger(CityRequest.class);

	private static CityRequest cityRequest;
	private static Object monitorCityRequest = new Object();
	private static City city;
	private static Session session;

	private CityRequest() {
	}

	public static CityRequest getInstance() {
		if (city == null) {
			throw new AssertionError("City is null");
		}
		if (session == null) {
			throw new AssertionError("Session is null");
		}
		return cityRequest;
	}

	public static CityRequest getInstance(City city, Session session) {
		if (cityRequest == null) {
			synchronized (monitorCityRequest) {
				if (cityRequest == null) {
					if (city == null) {
						throw new AssertionError("City is null");
					}
					if (session == null) {
						throw new AssertionError("Session is null");
					}
					CityRequest.city = city;
					CityRequest.session = session;
					cityRequest = new CityRequest();
				}
			}
		}
		return cityRequest;
	}

	public void source(int x, int z) {
		if (!city.checkIfSourceExists(x, z)) {
			city.createSource(x, z);
		}
	}

	public Road road(int x, int z, int xD, int zD, Color color) {
		if (!city.checkIfRoadExists(x, z, xD, zD)) {
			LOG.info("new road");
			return city.createRoad(x, z, xD, zD, color);
		} else {
			LOG.info("findRoad");
			return city.findRoad(x, z, xD, zD);
		}
	}

	public Building building(int x, int z, int height, String color) {
		return city.updateOrCreateBuilding(x, z, height, color);
	}

	public Building transport(int x, int z, String color) {
		return city.createTransport(new Coordinates(x, z), color);
	}

	public void moveTransport(TransportMove transportMove, String name, int x,
			int z, String roadNameOri, int xD, int zD, String roadNameDest) {
		Address a = new Address();
		a.setX(x);
		a.setZ(z);
		a.setRoadName(roadNameOri);

		Address aD = new Address();
		aD.setX(xD);
		aD.setZ(zD);
		aD.setRoadName(roadNameDest);

		PopProduct p = new PopProduct(10, 2);
		session.moveTransport(transportMove, name, a, aD, p);
	}

	public Building findTransportByName(String name) {
		return city.findTransportByName(name);
	}

	public int getMax() {
		return city.getMax();
	}

	public Building findBuilding(int x, int z) {
		return city.findBuilding(x, z);
	}

	public void removeTransport(String name) {
		city.removeTransport(name);
	}

	public static void reinitialize(City city, Session session) {
		CityRequest.city = city;
		CityRequest.session = session;
	}

}
