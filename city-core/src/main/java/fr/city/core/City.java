package fr.city.core;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.codehaus.jackson.map.ObjectMapper;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.graph.api.GraphEntry;
import fr.graph.api.InfoNode;
import fr.graph.core.Graph;
import fr.graph.core.RelTypes;
import fr.network.transport.api.Coord2D;
import fr.network.transport.api.PathInfo;
import fr.network.transport.application.Session;

public class City {

	private static final Logger LOG = LoggerFactory.getLogger(City.class);

	private static City city;

	private static ObserverCity observerCity;
	private static Session session;

	private String cityName = "My city";

	private static int max = 200;

	private AtomicLong id = new AtomicLong();

	private static final String BUILDING = "BUILDING";
	private static final String ROAD = "ROAD";
	

	private GraphEntry graphBuilding = new GraphEntry("TEST", false, BUILDING, ROAD);
	private TerrainLayout terrainLayout;

	private Map<String, Building> transportsMap = new HashMap<>();

	private static Object monitor = new Object();

	private City() {
		this.terrainLayout = new TerrainLayout(max);
	}

	public static City getInstance(ObserverCity myObserverCity) {
		if (city == null) {
			synchronized (monitor) {
				if (city == null) {
					LOG.info("City getInstance");
					city = new City();
					observerCity = myObserverCity;
					session = new Session(new PhysicalMoveCity(city));
					return city;
				}
			}
		}
		return city;
	}
	
	public List<Road> getAllRoads() {
		//warning double
		List<Road> lstBs = new ArrayList<>();
		List<Relationship> allRoads = graphBuilding.findAllRelation(RelTypes.EVENT);
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
			r.setName(r.getXa() + ":" + r.getZa() + ":" + r.getXb() + ":" + r.getZb());			
			lstBs.add(r);
		}
		LOG.info("getAllRoads size " + allRoads.size());
		return lstBs;
	}

	public List<Building> getAllBuildings() {
		List<Building> lstBs = new ArrayList<>();
		List<Object> allBuildings = graphBuilding.findAll(BUILDING, Graph.CUSTOM);
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
		if(name == null || name.equals("")) {
			throw new IllegalArgumentException("name must not be null or empty");
		}
		String jsonBuilding = (String) graphBuilding.find(BUILDING, name, Graph.CUSTOM);
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
	
	public List<PathInfo> findPath(String nameA, String nameB) {
		if(nameA == null || nameA.equals("")) {
			throw new IllegalArgumentException("nameA must not be null or empty");
		}
		if(nameB == null || nameB.equals("")) {
			throw new IllegalArgumentException("nameB must not be null or empty");
		}
		List<PathInfo> path = new ArrayList<>();
		List<InfoNode> infonodes =  graphBuilding.findPath(BUILDING, nameA, nameB);
		Mapper mapperBean = new DozerBeanMapper();
		for(InfoNode i : infonodes){			
			PathInfo pathInfo = mapperBean.map(i, PathInfo.class);
			path.add(pathInfo);
		}
		return path;
	}

	public void removeBuilding(Building bOld) {
		if (bOld == null) {
			throw new IllegalArgumentException("The building dosen't exist ");
		}
		LOG.info("remove Building x:" + bOld.getName());

		graphBuilding.remove(BUILDING, bOld.getName());

		observerCity.removeBuilding(bOld);
	}

	public Building createBuilding(int x, int z, int height) {
		if(x < 0 || z < 0 || x > max || z > max){
			throw new IllegalArgumentException("The range is  0 to " + (max - 1));
		}
		if(height < 0){
			throw new IllegalArgumentException("The height must be positive");
		}
		String jsonBuilding = (String) graphBuilding.findEventXYNodeProperty(BUILDING, x, z, Graph.CUSTOM);
		if (jsonBuilding != null) {
			throw new IllegalArgumentException("The building already exist " + x + ":" + z);
		}
		Building b = new Building();
		b.setX(x);
		b.setZ(z);
		b.setHeight(height);
		b.setName(TypeBuilding.HOUSE.name() + String.valueOf(id.getAndIncrement()));

		int y = terrainLayout.getHeight(x, z);

		b.setY(y);

		ObjectMapper mapper = new ObjectMapper();
		StringWriter writer = new StringWriter();
		try {
			mapper.writeValue(writer, b);
		} catch (Exception e) {
			LOG.error("convert object to Json", e);
		}
		graphBuilding.create(BUILDING, b.getName(), b.getX(), b.getY(), writer.toString());

		observerCity.createBuilding(b);
		return b;
	}

	public Road createRoad(int x, int z, int xD, int zD) {	
		if(x < 0 || z < 0 || x > max || z > max){
			throw new IllegalArgumentException("The range is  0 to " + (max - 1) + " " + x + " " + z);
		}
		if(xD < 0 || zD < 0 || xD > max || zD > max){
			throw new IllegalArgumentException("The range is  0 to " + (max - 1) + " " + xD + " " + zD);
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
		r.setName(TypeBuilding.ROAD.name() + String.valueOf(id.getAndIncrement()));

		ObjectMapper mapper = new ObjectMapper();
		StringWriter writer = new StringWriter();
		try {
			mapper.writeValue(writer, r);
		} catch (Exception e) {
			LOG.error("convert object to Json", e);
		}
		
		graphBuilding.createRoad(TypeBuilding.ROAD.name() , "", x, z, xD, zD);
		
		observerCity.createRoad(r);
		return r;
	}

	

	public Building updateBuilding(int x, int z, int height) {
		if(x < 0 || z < 0 || x > max || z > max){
			throw new IllegalArgumentException("The range is  0 to " + (max - 1) + " " + x + " " + z);
		}
		if(height < 0){
			throw new IllegalArgumentException("The height must be positive " + height);
		}
		Object jsonBuilding = graphBuilding.findEventXYNodeProperty(BUILDING, x, z, Graph.CUSTOM);
		if (jsonBuilding != null) {
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

	public void moveTransport(String name, Coord2D coord) {
		if(name == null || name.equals("")) {
			throw new IllegalArgumentException("name must not be null or empty");
		}
		if(coord.getX() < 0 || coord.getZ() < 0 || coord.getX() > max || coord.getZ() > max){
			throw new IllegalArgumentException("The range is  0 to " + (max - 1) + " " + coord.getX() + " " + coord.getZ() );
		}
		Building b = transportsMap.get(name);
		b.setX(coord.getX());
		b.setZ(coord.getZ());

		int y = terrainLayout.getHeight(coord.getX(), coord.getZ());
		b.setY(y);

		observerCity.moveTransport(b);
	}

	public void removeTransport(String name) {
		if(name == null || name.equals("")) {
			throw new IllegalArgumentException("name must not be null or empty");
		}
		Building b = transportsMap.remove(name);
		observerCity.removeTransport(b);
	}

	public String createTransport(Coord2D coord) {
		if(coord == null) {
			throw new IllegalArgumentException("coord must not be null");
		}
		if(coord.getX() < 0 || coord.getZ() < 0 || coord.getX() > max || coord.getZ() > max){
			throw new IllegalArgumentException("The range is  0 to " + (max - 1) + " " + coord.getX() + " " + coord.getZ() );
		}
		Building b = new Building();
		LOG.info("new Transport x:" + coord.getX() + "  z:" + coord.getZ());
		b.setX(coord.getX());
		b.setZ(coord.getZ());
		b.setHeight(5);
		int y = terrainLayout.getHeight(coord.getX(), coord.getZ());
		b.setY(y);
		b.setName(TypeBuilding.TRANSPORT.name() + String.valueOf(id.getAndIncrement()));
		transportsMap.put(b.getName(), b);
		observerCity.createTransport(b);
		return b.getName();
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

	public static Session getSession() {
		return session;
	}

}
