package fr.map.core;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Terrain {

	private static final Logger LOG = LoggerFactory.getLogger(Terrain.class);

	private static final String FILENAME = "TERRAIN";

	public int[] getTerrainById(String id) {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILENAME + id));) {
			return (int[]) ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			LOG.error("getTerrainById " + FILENAME + id, e);
			return new int[0];
		}
	}

	public void saveTerrain(String id, int[] terrain) {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILENAME + id));) {
			oos.writeObject(terrain);
		} catch (IOException e) {
			LOG.error("saveTerrain " + FILENAME + id, e);
		}
	}

	public int[] newTerrain(int xResolution, int yResolution, int largestFeature, double persistence,
			double maxHeight, double minHeight) {

		SimplexNoise simplexNoise = new SimplexNoise(largestFeature, persistence, (int) (Math.random() * 5000));

		int[] result = new int[xResolution * yResolution];

		for (int i = 0; i < xResolution; i++) {
			for (int j = 0; j < yResolution; j++) {
				result[i + j * xResolution] = (int) ((maxHeight - minHeight) * simplexNoise.getNoise(i, j) + minHeight);
			}
		}

		LOG.info(Arrays.toString(result));
		return result;

	}

}
