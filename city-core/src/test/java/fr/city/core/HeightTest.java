package fr.city.core;

import org.testng.annotations.Test;

public class HeightTest {

	@Test
	public void f() {

		City city = City.getInstance(null);

		int[] data = city.getTerrainLayout().getData();

	}
}
