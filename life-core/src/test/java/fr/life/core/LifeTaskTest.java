package fr.life.core;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.test.TestGraphDatabaseFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import fr.city.core.City;

public class LifeTaskTest {

	private City city = City.getInstance(new MockObserverCity());

	GraphDatabaseService graphDb;
	
	
	@BeforeClass
	public void prepareTestDatabase()
	{
	    graphDb = new TestGraphDatabaseFactory().newImpermanentDatabase();
	}

	
	@AfterClass
	public void destroyTestDatabase()
	{
	    graphDb.shutdown();
	}

	
	@Test
	public void firstRoadBuildings() {
		city.createRoad(100, 0, 100, 100);
		//city.createRoad(100, 0, 100, 100);
	}

}
