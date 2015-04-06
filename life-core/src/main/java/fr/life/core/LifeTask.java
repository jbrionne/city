package fr.life.core;

import java.awt.Color;
import java.util.Date;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.city.core.Building;
import fr.city.core.City;
import fr.city.core.CityColor;
import fr.city.core.Coordinates;
import fr.city.core.Road;
import fr.network.transport.api.TransportMove;
import fr.network.transport.application.Session;
import fr.network.transport.network.Address;

public class LifeTask extends TimerTask {

	private static final Logger LOG = LoggerFactory.getLogger(LifeTask.class);

	private City city;
	private Session session;
	private int max;

	public LifeTask(City city, Session session) {
		this.city = city;
		this.session = session;
		this.max = city.getMax();
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
		source(100, 0);
		Road r = road(100, 0, 100, 100);
		Building b = transport(100, 0, CityColor.BLUE);
		moveTransport(new TransportMovePop(city, 99, 90), b.getName(), 100, 0,
				r.getName(), 100, 90, r.getName());
	}

	public void source(int x, int z) {
		if (!city.checkIfSourceExists(x, z)) {
			city.createSource(x, z);
		}
	}

	public Road road(int x, int z, int xD, int zD) {
		if (!city.checkIfRoadExists(x, z, xD, zD)) {
			LOG.info("new road");
			return city.createRoad(x, z, xD, zD, Color.RED);
		} else {
			LOG.info("findRoad");
			return city.findRoad(x, z, xD, zD);
		}
	}

	public Building building(int x, int z, int height) {
		return city.updateOrCreateBuilding(x, z, height, CityColor.WHITE);
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
		session.send(transportMove, name, a, aD, p);
	}

	public Building findTransportByName(String name) {
		return city.findTransportByName(name);
	}

}
