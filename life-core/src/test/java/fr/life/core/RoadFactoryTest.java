package fr.life.core;

import static org.testng.Assert.assertEquals;

import java.awt.Color;

import org.testng.annotations.Test;

import fr.life.core.road.CrossRoads;
import fr.life.core.road.CrossRoadsEntry;
import fr.life.core.road.EntryPoint;
import fr.life.core.road.Rectangle;
import fr.life.core.road.RectangleEntry;
import fr.life.core.road.StraightRoad;
import fr.life.core.road.VectorPoint;

public class RoadFactoryTest extends CityViewTest {

	@Test
	public void createRoadCrossRoad() {
		EntryPoint entryPoint = new EntryPoint(100, 100, "");
		CityRequest.getInstance().source(100, 100);
		VectorPoint vectorPoint = new VectorPoint(0, 5);
		CrossRoads c = new CrossRoads(entryPoint, vectorPoint);
		CrossRoadsEntry es = c.build(Color.WHITE);

		assertEquals(es.getEntryPoint().getX(), 100);
		assertEquals(es.getEntryPoint().getZ(), 105);

		assertEquals(es.getEntryPointLeft().getX(), 105);
		assertEquals(es.getEntryPointLeft().getZ(), 100);

		assertEquals(es.getEntryPointRight().getX(), 95);
		assertEquals(es.getEntryPointRight().getZ(), 100);
	}

	@Test
	public void createRoadStraightRoad() {
		EntryPoint entryPoint = new EntryPoint(100, 100, "");
		CityRequest.getInstance().source(100, 100);
		VectorPoint vectorPoint = new VectorPoint(0, 5);
		StraightRoad c = new StraightRoad(entryPoint, vectorPoint);
		EntryPoint es = c.build(Color.WHITE);

		assertEquals(es.getX(), 100);
		assertEquals(es.getZ(), 105);

	}

	@Test
	public void createRectangle() {

		EntryPoint entryPoint = new EntryPoint(100, 100, "");
		CityRequest.getInstance().source(100, 100);
		VectorPoint vectorPoint = new VectorPoint(6, 5);
		Rectangle c = new Rectangle(entryPoint, vectorPoint);
		RectangleEntry es = c.build(Color.WHITE);

		assertEquals(es.getEntryPoint1().getX(), 106);
		assertEquals(es.getEntryPoint1().getZ(), 100);

		assertEquals(es.getEntryPoint2().getX(), 100);
		assertEquals(es.getEntryPoint2().getZ(), 105);

		assertEquals(es.getEntryPoint3().getX(), 106);
		assertEquals(es.getEntryPoint3().getZ(), 105);

	}

}
