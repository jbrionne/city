package fr.city.core;

import static fr.city.core.TypeBuilding.BUILDING;
import static fr.city.core.TypeBuilding.ROAD;
import static fr.city.core.TypeBuilding.TRANSPORT;

import java.awt.Color;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.graph.api.GraphEntry;
import fr.graph.api.InfoAddress;
import fr.graph.api.InfoNode;
import fr.graph.core.Graph;
import fr.graph.core.RelTypes;
import fr.network.transport.api.Coord2D;
import fr.network.transport.api.PathInfo;
import fr.network.transport.network.Address;

public class CityBase implements City {

	private static final Logger LOG = LoggerFactory.getLogger(CityBase.class);

	private static CityBase city;

	private static ObserverCity observerCity;

	private String cityName = "My city";

	private static int max = 200;

	private AtomicLong id = new AtomicLong();

	private GraphEntry graphBuilding;
	private TerrainLayout terrainLayout;

	private Map<String, Building> transportsMap = new HashMap<>();
	private Object monitorMaptransportsMap = new Object();

	private static Object monitor = new Object();

	private CityBase() {
		this.terrainLayout = new TerrainLayout(max);
	}

	public static CityBase getInstance(ObserverCity myObserverCity,
			String repo, boolean test) {
		if (city == null) {
			synchronized (monitor) {
				if (city == null) {
					LOG.info("City getInstance");
					city = new CityBase();
					city.graphBuilding = new GraphEntry(repo, test,
							BUILDING.name(), ROAD.name());
					observerCity = myObserverCity;
					return city;
				}
			}
		}
		return city;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.city.core.City#destroy()
	 */
	@Override
	public void destroy() {
		synchronized (monitor) {
			city.graphBuilding.shutdown();
			city = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.city.core.City#getAllRoads()
	 */
	@Override
	public List<Road> getAllRoads() {
		List<Road> lstBs = new ArrayList<>();
		List<Object> allBuildings = graphBuilding
				.findAllRelationProperty(RelTypes.EVENT);
		ObjectMapper mapper = new ObjectMapper();
		for (Object o : allBuildings) {
			Road b = null;
			try {
				b = mapper.readValue((String) o, Road.class);
			} catch (IOException e) {
				LOG.error("convert object to Json", e);
			}
			if (b == null) {
				throw new IllegalArgumentException("Building is null");
			}
			lstBs.add(b);
		}
		LOG.info("getAllRoads size " + allBuildings.size());
		return lstBs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.city.core.City#getAllBuildings()
	 */
	@Override
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.city.core.City#removeBuilding(java.lang.String)
	 */
	@Override
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
		List<InfoNode> infonodes = graphBuilding.findPath(ROAD.name(), iA, iB);

		for (InfoNode i : infonodes) {
			LOG.info("InfoNode " + i);
			PathInfo pathInfo = new PathInfo();
			pathInfo.setX(i.getX());
			pathInfo.setZ(i.getZ());
			pathInfo.setRoadName(i.getName());
			path.add(pathInfo);
		}
		return path;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.city.core.City#removeBuilding(fr.city.core.Building)
	 */
	@Override
	public void removeBuilding(Building bOld) {
		if (bOld == null) {
			throw new IllegalArgumentException("The building dosen't exist ");
		}
		LOG.info("remove Building x:" + bOld.getName());

		graphBuilding.remove(BUILDING.name(), bOld.getName());

		observerCity.removeBuilding(bOld);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.city.core.City#createBuilding(int, int, int, java.lang.String)
	 */
	@Override
	public Building createBuilding(int x, int z, int height, String color) {
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
		b.setColor(color);

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.city.core.City#findRoad(int, int, int, int)
	 */
	@Override
	public Road findRoad(int x, int z, int xD, int zD) {
		if (graphBuilding.checkIfRoadExists(ROAD.name(), x, z, xD, zD)) {
			return findRoadByName(graphBuilding.getRelationShipName(x, z, xD,
					zD));
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.city.core.City#findRoadByName(java.lang.String)
	 */
	@Override
	public Road findRoadByName(String name) {
		Object jsonBuilding = graphBuilding.findRoad(ROAD.name(), name);
		Road b = null;
		if (jsonBuilding != null) {
			LOG.debug("find " + (String) jsonBuilding);
			ObjectMapper mapper = new ObjectMapper();

			try {
				b = mapper.readValue((String) jsonBuilding, Road.class);
			} catch (IOException e) {
				LOG.error("convert object to Json", e);
			}
			if (b == null) {
				throw new IllegalArgumentException("Road is null");
			}
		}
		return b;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.city.core.City#findBuildingByName(java.lang.String)
	 */
	@Override
	public Building findBuildingByName(String name) {
		Building b = null;
		Object jsonBuilding = graphBuilding.find(BUILDING.name(), name,
				Graph.CUSTOM);
		if (jsonBuilding != null) {
			LOG.debug("findBuildingByName " + (String) jsonBuilding);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.city.core.City#checkIfRoadExists(int, int, int, int)
	 */
	@Override
	public boolean checkIfRoadExists(int x, int z, int xD, int zD) {
		return graphBuilding.checkIfRoadExists(ROAD.name(), x, z, xD, zD);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.city.core.City#createSource(int, int)
	 */
	@Override
	public void createSource(int x, int z) {
		if (x < 0 || z < 0 || x > max || z > max) {
			throw new IllegalArgumentException("The range is  0 to "
					+ (max - 1) + " " + x + " " + z);
		}

		graphBuilding.createSource(ROAD.name(), x, z);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.city.core.City#checkIfSourceExists(int, int)
	 */
	@Override
	public boolean checkIfSourceExists(int x, int z) {
		if (x < 0 || z < 0 || x > max || z > max) {
			throw new IllegalArgumentException("The range is  0 to "
					+ (max - 1) + " " + x + " " + z);
		}
		return graphBuilding.checkIfSourceExists(ROAD.name(), x, z);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.city.core.City#createRoad(int, int, int, int, java.awt.Color)
	 */
	@Override
	public Road createRoad(int x, int z, int xD, int zD, Color color) {
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
		r.setBlue(color.getBlue());
		r.setGreen(color.getGreen());
		r.setRed(color.getRed());
		r.setName(graphBuilding.getRelationShipName(x, z, xD, zD));

		ObjectMapper mapper = new ObjectMapper();
		StringWriter writer = new StringWriter();
		try {
			mapper.writeValue(writer, r);
		} catch (Exception e) {
			LOG.error("convert object to Json", e);
		}

		graphBuilding.createRoad(ROAD.name(), x, z, xD, zD, writer.toString());

		observerCity.createRoad(r);
		return r;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.city.core.City#updateOrCreateBuilding(int, int, int,
	 * java.lang.String)
	 */
	@Override
	public Building updateOrCreateBuilding(int x, int z, int height,
			String color) {
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
			return updateBuilding(b.getName(), height, color);
		} else {
			return createBuilding(x, z, height, color);
		}
	}

	@Override
	public Building updateBuilding(String name, int height, String color) {
		Building b = findBuildingByName(name);
		if(b == null){
			throw new IllegalArgumentException("The building doesn't exist " + name);
		}
		return updateBuilding(b, height, color);
	}

	private Building updateBuilding(Building bOld, int height, String color) {
		observerCity.removeBuilding(bOld);
		bOld.setHeight(height);
		bOld.setColor(color);
		ObjectMapper mapper = new ObjectMapper();
		StringWriter writer = new StringWriter();
		try {
			mapper.writeValue(writer, bOld);
		} catch (Exception e) {
			LOG.error("convert object to Json", e);
		}
		LOG.debug(writer.toString());
		graphBuilding
				.update(BUILDING.name(), bOld.getName(), writer.toString());

		observerCity.createBuilding(bOld);
		return bOld;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.city.core.City#findBuilding(int, int)
	 */
	@Override
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
			if (b == null) {
				throw new IllegalArgumentException("No transport for name "
						+ name);
			}

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
		br.setColor(b.getColor());
		return br;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.city.core.City#removeTransport(java.lang.String)
	 */
	@Override
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.city.core.City#findTransportByName(java.lang.String)
	 */
	@Override
	public Building findTransportByName(String name) {
		Building br = null;
		synchronized (monitorMaptransportsMap) {
			Building b = transportsMap.get(name);
			if (b == null) {
				throw new IllegalArgumentException("No transport for name "
						+ name);
			}

			// copy of the building to avoid concurrency
			br = copyBuilding(b);
		}
		return br;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.city.core.City#createTransport(fr.network.transport.api.Coord2D,
	 * java.lang.String)
	 */
	@Override
	public Building createTransport(Coord2D coord, String color) {
		if (coord == null) {
			throw new IllegalArgumentException("coord must not be null");
		}
		if (coord.getX() < 0 || coord.getZ() < 0 || coord.getX() > max
				|| coord.getZ() > max) {
			throw new IllegalArgumentException("The range is  0 to "
					+ (max - 1) + " " + coord.getX() + " " + coord.getZ());
		}
		Building b = new Building();
		b.setX(coord.getX());
		b.setZ(coord.getZ());
		b.setHeight(5);
		int y = terrainLayout.getHeight(coord.getX(), coord.getZ());
		b.setY(y);
		b.setColor(color);

		String name = graphBuilding.getNodeName(TRANSPORT.name(),
				String.valueOf(id.getAndIncrement()));

		b.setName(name);

		LOG.info("new Transport x:" + coord.getX() + "  z:" + coord.getZ()
				+ " name:" + name);

		Building br = null;
		synchronized (monitorMaptransportsMap) {
			transportsMap.put(b.getName(), b);

			// copy of the building to avoid concurrency
			br = copyBuilding(b);
		}
		observerCity.createTransport(br);
		return br;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.city.core.City#getCityName()
	 */
	@Override
	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.city.core.City#getMax()
	 */
	@Override
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
