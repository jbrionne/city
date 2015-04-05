package fr.life.core;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.neo4j.io.fs.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import fr.city.core.Building;
import fr.city.core.City;
import fr.city.core.PhysicalMoveCity;
import fr.city.core.Road;

public class LifeTaskTest {

	private static final Logger LOG = LoggerFactory
			.getLogger(LifeTaskTest.class);
	private static AtomicInteger index = new AtomicInteger();
	private static final String PATH = "TEST";
	private LifeTask life;
	private City city;

	@BeforeMethod
	public void prepareTestDatabase() {
		LOG.info("prepareTestDatabase");
		city = City.getInstance(new MockObserverCity(),
				PATH + index.incrementAndGet(), true);
		life = new LifeTask(city, new MockSession(new PhysicalMoveCity(city)));
	}

	@AfterMethod
	public void destroyTestDatabase() throws InterruptedException {
		LOG.info("destroyTestDatabase");
		life.destroy();
		try {
			FileUtils.deleteRecursively(new File(PATH + index));
		} catch (IOException e) {
			LOG.error("graph deletion", e);
		}
	}

	@Test
	public void firstRoad() {
		Road r = life.road(100, 0, 100, 100);
		assertEquals(100, r.getXa());
		assertEquals(0, r.getZa());
		assertEquals(100, r.getXb());
		assertEquals(100, r.getZb());
	}

	@Test
	public void firstDoubleRoad() {
		Road r1 = life.road(100, 0, 100, 100);
		Road r2 = life.road(100, 0, 100, 100);
		assertEquals(r1.getXa(), r2.getXa());
		assertEquals(r1.getZa(), r2.getZa());
		assertEquals(r1.getXb(), r2.getXb());
		assertEquals(r1.getZb(), r2.getZb());
	}

	@Test
	public void firstBuilding() {
		Building b = life.building(99, 90, 0);
		assertEquals(99, b.getX());
		assertEquals(90, b.getZ());
		assertEquals(0, b.getHeight());
	}

	@Test
	public void firstBuildingUpdate() {
		Building b = life.building(99, 90, 0);
		assertEquals(99, b.getX());
		assertEquals(90, b.getZ());
		assertEquals(0, b.getHeight());

		Building b1 = life.building(99, 90, 10);
		assertEquals(99, b1.getX());
		assertEquals(90, b1.getZ());
		assertEquals(10, b1.getHeight());
	}

	@Test
	public void firstTransport() {
		Building t = life.transport(100, 0);
		assertEquals(100, t.getX());
		assertEquals(0, t.getZ());
	}

	@Test
	public void moveTransport() throws InterruptedException {
		Building b = life.building(99, 90, 0);
		Road r = life.road(100, 0, 100, 100);
		int xD = 100;
		int zD = 90;
		Building t = life.transport(100, 0);
		TransportMovePop tm = new TransportMovePop(city, b.getX(), b.getZ());
		life.moveTransport(tm, t.getName(), 100, 0, r.getName(), xD, zD,
				r.getName());		
	}

}
