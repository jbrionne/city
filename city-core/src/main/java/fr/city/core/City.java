package fr.city.core;

import java.awt.Color;
import java.util.List;

import fr.network.transport.api.Coord2D;

public interface City {

	/**
	 * Stop database
	 */
	void destroy();

	/**
	 * Get all roads
	 * @return list of roads
	 */
	List<Road> getAllRoads();

	/**
	 * Get all buildings
	 * @return list of building
	 */
	List<Building> getAllBuildings();

	/**
	 * Remove a building
	 * @param name name of the bulding
	 */
	void removeBuilding(String name);

	/**
	 * Remove a building.
	 * 
	 * @param bOld
	 *            building to remove
	 * @param height
	 *            the height must be positive
	 * @return the building
	 */
	void removeBuilding(Building bOld);

	/**
	 * Create a building. x, z [0, max - 1]
	 * 
	 * @param x
	 *            coordinate x start point
	 * @param z
	 *            coordinate z start point
	 * @param height
	 *            the height must be positive
	 * @param color
	 *            use CityColor or color hex like "0xffffff"
	 * @return the building
	 */
	Building createBuilding(int x, int z, int height,
			String color);

	/**
	 * Find a road. If the road exists then the function return a new Object
	 * Road with the coordinates
	 * 
	 * @param x
	 *            coordinate x start point
	 * @param z
	 *            coordinate z start point
	 * @param xD
	 *            coordinate x end point
	 * @param zD
	 *            coordinate z end point
	 * @return the road
	 */
	Road findRoad(int x, int z, int xD, int zD);

	/**
	 * Find a road. If the road exists then the function return a new Object
	 * Road with the coordinates
	 * 
	 * @param name
	 *          	Name of the road
	 * @return the road
	 */
	Road findRoadByName(String name);

	/**
	 * Check if a road exists
	 * 
	 * @param x
	 *            coordinate x start point
	 * @param z
	 *            coordinate z start point
	 * @param xD
	 *            coordinate x end point
	 * @param zD
	 *            coordinate z end point
	 * @return boolean true if the road exists
	 */
	boolean checkIfRoadExists(int x, int z, int xD, int zD);

	/**
	 * Create a source
	 * You can connect a road to a source
	 * @param x coordinate x start point
	 * @param z coordinate z start point
	 */
	void createSource(int x, int z);

	/**
	 * Check if a source exists
	 * @param x coordinate x start point
	 * @param z coordinate z start point
	 */
	boolean checkIfSourceExists(int x, int z);

	/**
	 * Create a road x, z, xD, zD [0, max - 1] x == xD or z == zD
	 * 
	 * @param x
	 *            coordinate x start point
	 * @param z
	 *            coordinate z start point
	 * @param xD
	 *            coordinate x end point
	 * @param zD
	 *            coordinate z end point
	 * @param color
	 *            Color of the road
	 * @return a road
	 */
	Road createRoad(int x, int z, int xD, int zD, Color color);

	/**
	 * Update or create a building. x, z [0, max - 1]
	 * 
	 * @param x
	 *            coordinate x start point
	 * @param z
	 *            coordinate z start point
	 * @param height
	 *            the height must be positive
	 * @param color
	 *            use CityColor or color hex like "0xffffff"
	 * @return the building
	 */
	Building updateOrCreateBuilding(int x, int z, int height,
			String color);

	/**
	 * Find a building with coordinates
	 * 
	 * @param x
	 *            coordinate x start point
	 * @param z
	 *            coordinate z start point
	 * @return the building
	 */
	Building findBuilding(int x, int z);

	/**
	 * Remove a transport by name
	 * 
	 * @param name
	 *            name of the transport
	 */
	void removeTransport(String name);

	/**
	 * Find a transport by name
	 * @param name name of the transport
	 * @return the transport
	 */
	Building findTransportByName(String name);

	/**
	 * Create a transport
	 * 
	 * @param coord
	 *            coordinates of the transport
	 * 
	 * @param color
	 *            use CityColor or color hex like "0xffffff"
	 * @return a building which is a transport
	 */
	Building createTransport(Coord2D coord, String color);

	/**
	 * Get the city name
	 * @return city name
	 */
	String getCityName();

	/**
	 * Max of element in the map...[1 to max]
	 * @return
	 */
	int getMax();

	/**
	 * Get Terrain layout
	 * @return Terrain layout
	 */
	TerrainLayout getTerrainLayout();


}