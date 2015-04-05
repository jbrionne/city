package fr.city.core;

import static fr.city.core.TypeBuilding.BUILDING;
import static fr.city.core.TypeBuilding.ROAD;
import static fr.city.core.TypeBuilding.TRANSPORT;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.codehaus.jackson.map.ObjectMapper;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.graph.api.GraphEntry;
import fr.graph.api.InfoAddress;
import fr.graph.api.InfoNode;
import fr.graph.api.InfoRelationShip;
import fr.graph.core.Graph;
import fr.graph.core.RelTypes;
import fr.network.transport.api.Coord2D;
import fr.network.transport.api.PathInfo;
import fr.network.transport.network.Address;

public class City {

	private static final Logger LOG = LoggerFactory.getLogger(City.class);

	private static City city;

	private static ObserverCity observerCity;

	private String cityName = "My city";

	private static int max = 200;

	private AtomicLong id = new AtomicLong();

	private GraphEntry graphBuilding;
	private TerrainLayout terrainLayout;

	private Map<String, Building> transportsMap = new HashMap<>();
	private Object monitorMaptransportsMap = new Object();

	private static Object monitor = new Object();

	private City() {
		this.terrainLayout = new TerrainLayout(max);
	}

	public static City getInstance(ObserverCity myObserverCity, String repo,
			boolean test) {
		if (city == null) {
			synchronized (monitor) {
				if (city == null) {
					LOG.info("City getInstance");
					city = new City();
					city.graphBuilding = new GraphEntry(repo, test,
							BUILDING.name(), ROAD.name());
					observerCity = myObserverCity;
					return city;
				}
			}
		}
		return city;
	}

	public void destroy() {
		synchronized (monitor) {
			city.graphBuilding.shutdown();
			city = null;
		}
	}

	public List<Road> getAllRoads() {
		// warning double
		List<Road> lstBs = new ArrayList<>();
		List<Relationship> allRoads = graphBuilding
				.findAllRelation(RelTypes.EVENT);
		for (Relationship n : allRoads) {
			Node start = graphBuilding.getStartNode(n);
			LOG.info("start {}", start.toString());
			Node end = graphBuilding.getEndNode(n);
			LOG.info("end {}", end.toString());
			Road r = new Road();
			r.setXa((int) graphBuilding.getProperty(start, Graph.X));
			r.setZa((int) graphBuilding.getProperty(start, Graph.Y));
			r.setXb((int) graphBuilding.getProperty(end, Graph.X));
			r.setZb((int) graphBuilding.getProperty(end, Graph.Y));
			lstBs.add(r);
		}
		LOG.info("getAllRoads size " + allRoads.size());
		return lstBs;
	}

	public List<Building> getAllBuildings() {
		List<Building> lstBs = new ArrayList<>();
		List<Object> allBuildings = graphBuilding.findAll(BUILDING.name(),
				Graph.CUSTOM);
		ObjectMapper mapper = new ObjectMapper();
		for (Object o : allBuildings) {
			Building b = null;
			try {
				b = mapper.readValue((String) o, Building.class);
			} catch (IOException e) {
				LOG.error("convert object to Json", e);
			}
			if (b == null) {
				throw new IllegalArgumentException("Building is null");
			}
			lstBs.add(b);
		}
		LOG.info("getAllBuildings size " + allBuildings.size());
		return lstBs;
	}

	public void removeBuilding(String name) {
		if (name == null || name.equals("")) {
			throw new IllegalArgumentException("name must not be null or empty");
		}
		String jsonBuilding = (String) graphBuilding.find(BUILDING.name(),
				name, Graph.CUSTOM);
		ObjectMapper mapper = new ObjectMapper();
		Building bOld = null;
		try {
			bOld = mapper.readValue(jsonBuilding, Building.class);
		} catch (IOException e) {
			LOG.error("convert object to Json", e);
		}
		if (bOld == null) {
			throw new IllegalArgumentException("Doesn't exist " + name);
		}
		removeBuilding(bOld);
	}

	public List<PathInfo> findPath(Address origin, Address destination) {
		if (origin == null) {
			throw new IllegalArgumentException(
					"origin must not be null or empty");
		}
		if (destination == null) {
			throw new IllegalArgumentException(
					"destination must not be null or empty");
		}
		
		InfoAddress iA = new InfoAddress();
		iA.setRoadName(origin.getRoadName());
		iA.setX(origin.getX());
		iA.setZ(origin.getZ());
		
		
		InfoAddress iB = new InfoAddress();
		iB.setRoadName(destination.getRoadName());
		iB.setX(destination.getX());
		iB.setZ(destination.getZ());		
		
		List<PathInfo> path = new ArrayList<>();
		List<InfoNode> infonodes = graphBuilding.findPath(ROAD.name(),
				iA, iB);
		for (InfoNode i : infonodes) {
			if(i.getName() != null && i.getName().startsWith(ROAD.name())) {
				PathInfo pathInfo = new PathInfo();
				pathInfo.setX(i.getX());
				pathInfo.setZ(i.getZ());
				path.add(pathInfo);
			}
		}
		return path;
	}

	/**
	 * Remove a building.
	 * 
	 * @param bOld
	 *            building to remove
	 * @param height
	 *            the height must be positive
	 * @return the building
	 */
	public void removeBuilding(Building bOld) {
		if (bOld == null) {
			throw new IllegalArgumentException("The building dosen't exist ");
		}
		LOG.info("remove Building x:" + bOld.getName());

		graphBuilding.remove(BUILDING.name(), bOld.getName());

		observerCity.removeBuilding(bOld);
	}

	/**
	 * Create a building. x, z [0, max - 1]
	 * 
	 * @param x
	 *            coordinate x start point
	 * @param z
	 *            coordinate z start point
	 * @param height
	 *            the height must be positive
	 * @return the building
	 */
	public Building createBuilding(int x, int z, int height) {
		if (x < 0 || z < 0 || x > max || z > max) {
			throw new IllegalArgumentException("The range is  0 to "
					+ (max - 1));
		}
		if (height < 0) {
			throw new IllegalArgumentException("The height must be positive");
		}
		String jsonBuilding = (String) graphBuilding.findEventXYNodeProperty(
				BUILDING.name(), x, z, Graph.CUSTOM);
		if (jsonBuilding != null) {
			throw new IllegalArgumentException("The building already exist "
					+ x + ":" + z);
		}
		Building b = new Building();
		b.setX(x);
		b.setZ(z);
		b.setHeight(height);
		b.setName(graphBuilding.getNodeName(BUILDING.name(),
				String.valueOf(id.getAndIncrement())));

		int y = terrainLayout.getHeight(x, z);

		b.setY(y);

		ObjectMapper mapper = new ObjectMapper();
		StringWriter writer = new StringWriter();
		try {
			mapper.writeValue(writer, b);
		} catch (Exception e) {
			LOG.error("convert object to Json", e);
		}
		LOG.debug(writer.toString());
		graphBuilding.create(BUILDING.name(), b.getName(), b.getX(), b.getZ(),
				writer.toString());

		observerCity.createBuilding(b);
		return b;
	}

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
	public Road findRoad(int x, int z, int xD, int zD) {
		if (graphBuilding.checkIfRoadExists(ROAD.name(), x, z, xD,
				zD)) {
			Road r = new Road();
			r.setXa(x);
			r.setZa(z);
			r.setXb(xD);
			r.setZb(zD);
			r.setName(graphBuilding.getRelationShipName(x, z, xD, zD));
			return r;
		}
		return null;
	}

	public Road findRoadByName(String name) {
		InfoRelationShip o = graphBuilding.findRoad(ROAD.name(), name);
		if (o != null) {
			Road r = new Road();
			r.setName(o.getName());
			r.setXa(o.getX());
			r.setXb(o.getxD());
			r.setZa(o.getZ());
			r.setZb(o.getzD());
			r.setName(graphBuilding.getRelationShipName(o.getX(), o.getZ(), o.getxD(), o.getzD()));
			return r;
		} else {
			return null;
		}
	}

	/**
	 * Check if a road exist
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
	public boolean checkIfRoadExists(int x, int z, int xD, int zD) {
		return graphBuilding.checkIfRoadExists(ROAD.name(), x, z,
				xD, zD);
	}

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
	 * @return a road
	 */
	public Road createRoad(int x, int z, int xD, int zD) {
		if (x < 0 || z < 0 || x > max || z > max) {
			throw new IllegalArgumentException("The range is  0 to "
					+ (max - 1) + " " + x + " " + z);
		}
		if (xD < 0 || zD < 0 || xD > max || zD > max) {
			throw new IllegalArgumentException("The range is  0 to "
					+ (max - 1) + " " + xD + " " + zD);
		}
		if (x != xD && z != zD) {
			throw new IllegalArgumentException(
					"you must have x == xD or z == zD");
		}
		Road r = new Road();
		r.setXa(x);
		r.setZa(z);
		r.setXb(xD);
		r.setZb(zD);
		r.setName(graphBuilding.getRelationShipName(x, z, xD, zD));

		ObjectMapper mapper = new ObjectMapper();
		StringWriter writer = new StringWriter();
		try {
			mapper.writeValue(writer, r);
		} catch (Exception e) {
			LOG.error("convert object to Json", e);
		}

		graphBuilding.createRoad(ROAD.name(), x, z, xD, zD);

		observerCity.createRoad(r);
		return r;
	}

	/**
	 * Update or create a building. x, z [0, max - 1]
	 * 
	 * @param x
	 *            coordinate x start point
	 * @param z
	 *            coordinate z start point
	 * @param height
	 *            the height must be positive
	 * @return the building
	 */
	public Building updateOrCreateBuilding(int x, int z, int height) {
		if (x < 0 || z < 0 || x > max || z > max) {
			throw new IllegalArgumentException("The range is  0 to "
					+ (max - 1) + " " + x + " " + z);
		}
		if (height < 0) {
			throw new IllegalArgumentException("The height must be positive "
					+ height);
		}
		Object jsonBuilding = graphBuilding.findEventXYNodeProperty(
				BUILDING.name(), x, z, Graph.CUSTOM);
		if (jsonBuilding != null) {
			LOG.debug("find " + (String) jsonBuilding);
			ObjectMapper mapper = new ObjectMapper();
			Building b = null;
			try {
				b = mapper.readValue((String) jsonBuilding, Building.class);
			} catch (IOException e) {
				LOG.error("convert object to Json", e);
			}
			if (b == null) {
				throw new IllegalArgumentException("Building is null");
			}
			removeBuilding(b);
		}
		return createBuilding(x, z, height);
	}
	
	/**
	 * Find a building with coordinates
	 * @param x
	 *            coordinate x start point
	 * @param z
	 *            coordinate z start point
	 * @return the building
	 */
	public Building findBuilding(int x, int z) {
		if (x < 0 || z < 0 || x > max || z > max) {
			throw new IllegalArgumentException("The range is  0 to "
					+ (max - 1) + " " + x + " " + z);
		}		
		Object jsonBuilding = graphBuilding.findEventXYNodeProperty(
				BUILDING.name(), x, z, Graph.CUSTOM);
		Building b = null;
		if (jsonBuilding != null) {
			LOG.debug("find " + (String) jsonBuilding);
			ObjectMapper mapper = new ObjectMapper();
			
			try {
				b = mapper.readValue((String) jsonBuilding, Building.class);
			} catch (IOException e) {
				LOG.error("convert object to Json", e);
			}
			if (b == null) {
				throw new IllegalArgumentException("Building is null");
			}
		}
		return b;
	}

	/**
	 * Move the transport to the coordinates
	 * 
	 * @param name
	 *            name of the transport
	 * @param coord
	 *            destination coordinates
	 */
	public void moveTransport(String name, Address coord) {
		if (name == null || name.equals("")) {
			throw new IllegalArgumentException("name must not be null or empty");
		}
		if (coord.getX() < 0 || coord.getZ() < 0 || coord.getX() > max
				|| coord.getZ() > max) {
			throw new IllegalArgumentException("The range is  0 to "
					+ (max - 1) + " " + coord.getX() + " " + coord.getZ());
		}

		Building br = null;

		synchronized (monitorMaptransportsMap) {

			Building b = transportsMap.get(name);
			b.setX(coord.getX());
			b.setZ(coord.getZ());
			int y = terrainLayout.getHeight(coord.getX(), coord.getZ());
			b.setY(y);

			// copy of the building to avoid concurrency
			br = copyBuilding(b);
		}

		observerCity.moveTransport(br);
	}

	private Building copyBuilding(Building b) {
		Building br = new Building();
		br.setHeight(b.getHeight());
		br.setName(b.getName());
		br.setX(b.getX());
		br.setY(b.getY());
		br.setZ(b.getZ());
		return br;
	}

	/**
	 * Remove a transport by name
	 * 
	 * @param name
	 *            name of the transport
	 */
	public void removeTransport(String name) {
		if (name == null || name.equals("")) {
			throw new IllegalArgumentException("name must not be null or empty");
		}

		Building br = null;
		synchronized (monitorMaptransportsMap) {
			Building b = transportsMap.remove(name);

			// copy of the building to avoid concurrency
			br = copyBuilding(b);
		}
		observerCity.removeTransport(br);
	}

	public Building findTransportByName(String name) {
		Building br = null;
		synchronized (monitorMaptransportsMap) {
			Building b = transportsMap.get(name);

			// copy of the building to avoid concurrency
			br = copyBuilding(b);
		}
		return br;
	}

	/**
	 * Create a transport
	 * 
	 * @param coord
	 *            coordinates of the transport
	 * @return a building which is a transport
	 */
	public Building createTransport(Coord2D coord) {
		if (coord == null) {
			throw new IllegalArgumentException("coord must not be null");
		}
		if (coord.getX() < 0 || coord.getZ() < 0 || coord.getX() > max
				|| coord.getZ() > max) {
			throw new IllegalArgumentException("The range is  0 to "
					+ (max - 1) + " " + coord.getX() + " " + coord.getZ());
		}
		Building b = new Building();
		LOG.info("new Transport x:" + coord.getX() + "  z:" + coord.getZ());
		b.setX(coord.getX());
		b.setZ(coord.getZ());
		b.setHeight(5);
		int y = terrainLayout.getHeight(coord.getX(), coord.getZ());
		b.setY(y);

		b.setName(graphBuilding.getNodeName(TRANSPORT.name(),
				String.valueOf(id.getAndIncrement())));

		Building br = null;
		synchronized (monitorMaptransportsMap) {
			transportsMap.put(b.getName(), b);

			// copy of the building to avoid concurrency
			br = copyBuilding(b);
		}
		observerCity.createTransport(br);
		return br;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public int getMax() {
		return max;
	}

	public TerrainLayout getTerrainLayout() {
		return terrainLayout;
	}

	public ObserverCity getObserverCity() {
		return observerCity;
	}

}
