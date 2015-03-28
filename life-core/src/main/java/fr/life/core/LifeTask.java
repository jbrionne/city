package fr.life.core;

import java.util.Date;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.city.core.Building;
import fr.city.core.City;
import fr.network.transport.liaison.Product;
import fr.network.transport.network.Address;

public class LifeTask extends TimerTask {

	private static final Logger LOG = LoggerFactory.getLogger(LifeTask.class);

	private City city;
	private int max;

	public LifeTask(City city) {
		this.city = city;
		this.max = city.getMax();
	}

	@Override
	public void run() {
		LOG.info("LifeTask Start time:" + new Date());
		doSomeWork();
		LOG.info("LifeTask End time:" + new Date());
	}

	private void doSomeWork() {		
		randomBuilding();
		randomRoad();
		randomTransport();
	}	
	
	

	public void randomRoad() {
		double d = Math.random();
		int x, z, xD, zD;
		if (d < 0.5) {
			x = (int) (Math.random() * max);
			z = (int) (Math.random() * max);
			xD = (int) (Math.random() * max);
			zD = z;
		} else {
			x = (int) (Math.random() * max);
			z = (int) (Math.random() * max);
			xD = x;
			zD = (int) (Math.random() * max);
		}
		
		city.createRoad(x, z, xD, zD);				
	}

	public Building randomBuilding() {
		int x = (int) (Math.random() * max);
		int z = (int) (Math.random() * max);
		int height = 100;	

		return city.updateBuilding(x, z, height);
	}

	public void randomTransport() {
		int x = (int) (Math.random() * max);
		int z = (int) (Math.random() * max);

		int xD = (int) (Math.random() * max);
		int zD = (int) (Math.random() * max);

		Address a = new Address();
		a.setX(x);
		a.setZ(z);

		Address aD = new Address();
		aD.setX(xD);
		aD.setZ(zD);

		Product p = new Product();
		city.getSession().send(a, aD, p);
	}

}
