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

import fr.city.core.City;
import fr.city.core.Road;

public class LifeTaskTest {

	private static final Logger LOG = LoggerFactory.getLogger(LifeTaskTest.class);
	private static AtomicInteger index = new AtomicInteger();
	private static final String PATH = "TEST";
	private City city;
	
	@BeforeMethod
	public void prepareTestDatabase()
	{
		LOG.info("prepareTestDatabase");
	    city = City.getInstance(new MockObserverCity(), PATH + index.incrementAndGet(), true);	
	}
	
	@AfterMethod
	public void destroyTestDatabase() throws InterruptedException
	{
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
		Road r = city.createRoad(100, 0, 100, 100);
		assertEquals(100, r.getXa());
		assertEquals(0, r.getZa());
		assertEquals(100, r.getXb());
		assertEquals(100, r.getZb());
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void firstDoubleRoadBuildings() {
		city.createRoad(100, 0, 100, 100);
		city.createRoad(100, 0, 100, 100);			
	}

}
