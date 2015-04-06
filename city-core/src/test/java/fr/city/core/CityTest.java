package fr.city.core;

import static org.testng.Assert.assertEquals;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.neo4j.io.fs.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CityTest {

	private static final Logger LOG = LoggerFactory.getLogger(CityTest.class);
	private static AtomicInteger index = new AtomicInteger();
	private static final String PATH = "TEST";
	private City city;

	@BeforeMethod
	public void prepareTestDatabase() {
		LOG.info("prepareTestDatabase");
		city = City.getInstance(new MockObserverCity(),
				PATH + index.incrementAndGet(), true);
	}

	@AfterMethod
	public void destroyTestDatabase() throws InterruptedException {
		LOG.info("destroyTestDatabase");
		city.destroy();
		try {
			FileUtils.deleteRecursively(new File(PATH + index));
		} catch (IOException e) {
			LOG.error("graph deletion", e);
		}
	}

	@Test
	public void firstRoadBuildings() {
		city.createSource(100, 0);
		Road r = city.createRoad(100, 0, 100, 100, Color.RED);
		assertEquals(100, r.getXa());
		assertEquals(0, r.getZa());
		assertEquals(100, r.getXb());
		assertEquals(100, r.getZb());
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void firstDoubleRoadBuildings() {
		city.createSource(100, 0);
		city.createRoad(100, 0, 100, 100, Color.RED);
		city.createRoad(100, 0, 100, 100, Color.RED);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void createBuildings() {
		Building b1 = city.createBuilding(10, 10, 10, CityColor.WHITE);
		Building b2 = city.createBuilding(10, 10, 10, CityColor.WHITE);
	}

	public void updateBuildings() {
		Building b1 = city.createBuilding(10, 10, 10, CityColor.WHITE);
		Building b2 = city.updateOrCreateBuilding(10, 10, 20, CityColor.WHITE);
		assertEquals(b1.getX(), b2.getX());
		assertEquals(b1.getZ(), b2.getZ());

	}

}
