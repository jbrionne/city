package fr.life.core;

import static org.testng.Assert.assertEquals;

import java.awt.Color;

import org.testng.annotations.Test;

import fr.city.core.Building;
import fr.city.core.CityColor;
import fr.city.core.Road;

public class CityBuildTest extends CityViewTest {

	@Test
	public void doubleSource() {
		CityRequest.getInstance().source(100, 0);
		CityRequest.getInstance().source(100, 0);
	}

	@Test
	public void firstRoad() {
		CityRequest.getInstance().source(100, 0);
		Road r = CityRequest.getInstance().road(100, 0, 100, 100, Color.WHITE);
		assertEquals(r.getXa(), 100);
		assertEquals(r.getZa(), 0);
		assertEquals(r.getXb(), 100);
		assertEquals(r.getZb(), 100);
	}

	@Test
	public void firstDoubleRoad() {
		CityRequest.getInstance().source(100, 0);
		Road r1 = CityRequest.getInstance().road(100, 0, 100, 100, Color.WHITE);
		Road r2 = CityRequest.getInstance().road(100, 0, 100, 100, Color.WHITE);
		assertEquals(r1.getXa(), r2.getXa());
		assertEquals(r1.getZa(), r2.getZa());
		assertEquals(r1.getXb(), r2.getXb());
		assertEquals(r1.getZb(), r2.getZb());
	}

	@Test
	public void firstBuilding() {
		Building b = CityRequest.getInstance().building(99, 90, 0,
				CityColor.BLUE);
		assertEquals(b.getX(), 99);
		assertEquals(b.getZ(), 90);
		assertEquals(b.getHeight(), 0);
	}

	@Test
	public void firstBuildingUpdate() {
		Building b = CityRequest.getInstance().building(99, 90, 0,
				CityColor.BLUE);
		assertEquals(b.getX(), 99);
		assertEquals(b.getZ(), 90);
		assertEquals(b.getHeight(), 0);

		Building b1 = CityRequest.getInstance().building(99, 90, 10,
				CityColor.BLUE);
		assertEquals(b1.getX(), 99);
		assertEquals(b1.getZ(), 90);
		assertEquals(b1.getHeight(), 10);
	}

	@Test
	public void firstTransport() {
		Building t = CityRequest.getInstance().transport(100, 0,
				CityColor.YELLOW);
		assertEquals(t.getX(), 100);
		assertEquals(t.getZ(), 0);
	}

	@Test
	public void moveTransport() throws InterruptedException {
		CityRequest.getInstance().source(100, 0);
		Building b = CityRequest.getInstance().building(99, 90, 0,
				CityColor.BLUE);
		Road r = CityRequest.getInstance().road(100, 0, 100, 100, Color.WHITE);
		int xD = 100;
		int zD = 90;
		Building t = CityRequest.getInstance().transport(100, 0,
				CityColor.YELLOW);
		TransportMovePop tm = new TransportMovePop(b.getX(), b.getZ());
		CityRequest.getInstance().moveTransport(tm, t.getName(), 100, 0,
				r.getName(), xD, zD, r.getName());
	}

}
