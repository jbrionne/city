package fr.city.core;

import fr.map.core.Terrain;

public class TerrainLayout {

	private static int maxheightAbs = 400;
	private static int minheightAbs = 0;
	private static int waterheight = -maxheightAbs / 5;
	private static int maxheight = maxheightAbs - minheightAbs;
	private static int[] data;

	private Terrain terrain = new Terrain();
	private String idTerrain = "myTerrain1";

	private int max;

	public TerrainLayout(int max) {
		this.max = max;
		// generate data
		data = terrain.getTerrainById(idTerrain);
		if (data.length == 0) {
			data = terrain.newTerrain(max, max, 300, 0.3, maxheightAbs, minheightAbs);
			terrain.saveTerrain(idTerrain, data);
		}

		int maxheightLoc = 0;
		int minheightLoc = maxheightAbs;
		int i = 0;
		for (int d : data) {
			if (d < waterheight) {
				data[i] = waterheight;
			}

			if (maxheightLoc < d) {
				maxheightLoc = d;
			}
			if (minheightLoc > d) {
				minheightLoc = d;
			}
			i++;
		}
		maxheight = maxheightLoc - minheightLoc;
	}

	public int getHeight(int x, int z) {
		int y = data[x + z * max];
		return y;
	}

	public int[] getData() {
		return data;
	}

	public int getMaxheight() {
		return maxheight;
	}

	public int getWaterheight() {
		return waterheight;
	}

}
