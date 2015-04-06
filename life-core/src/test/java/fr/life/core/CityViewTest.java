package fr.life.core;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.neo4j.io.fs.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import fr.city.core.City;
import fr.city.core.PhysicalMoveCity;
import fr.network.transport.application.Session;

public abstract class CityViewTest {

	private static final Logger LOG = LoggerFactory
			.getLogger(CityBuildTest.class);

	private static AtomicInteger index = new AtomicInteger();
	private static final String PATH = "TEST";
	protected LifeTask life;
	protected City city;

	@BeforeMethod
	public void prepareTestDatabase() {
		LOG.info("prepareTestDatabase");
		city = City.getInstance(new MockObserverCity(),
				PATH + index.incrementAndGet(), true);
		Session session = new MockSession(new PhysicalMoveCity(city));
		life = new LifeTask(city, session);		
		CityRequest.reinitialize(city, session);
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
}
