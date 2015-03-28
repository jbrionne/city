package fr.map.core;

import org.testng.annotations.Test;

public class GenerateTest {
	
  @Test
  public void generate() {
	  Terrain terrain = new Terrain();
	  int[] map = terrain.newTerrain(256, 256, 300, 0.45, 150, 0);	  
  }
  
}
