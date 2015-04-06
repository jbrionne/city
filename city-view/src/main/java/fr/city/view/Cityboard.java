package fr.city.view;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.city.core.Building;
import fr.city.core.Road;
import fr.city.view.observer.ThreeObserverCity;
import fr.city.view.sun.SunView;
import fr.life.core.Ecosystem;
import fr.sunlight.core.Sun;
import fr.sunlight.core.SunPosition;

@ServerEndpoint(value = "/cityboardendpoint", configurator = MyServerConfigurator.class)
public class Cityboard {

	private static final Logger LOG = LoggerFactory.getLogger(Cityboard.class);

	private Ecosystem ecosystem = Ecosystem.getInstance(ThreeObserverCity.getInstance());

	private static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());

	@OnMessage
	public void broadcastSample(String figure, Session session) throws IOException {

		ObjectMapper mapper = new ObjectMapper();
		Command command = null;
		try {
			command = mapper.readValue(figure, Command.class);
			LOG.info("" + command);
		} catch (Exception e) {
			LOG.error("convert Json to object", e);
		}
		if ("house".equals(command.getValue())) {
//			Building b = ecosystem.getCity().randomBuilding();
//			createBuilding(b, "create");
			//reactivate if you want
		} else if ("sun".equals(command.getValue())) {
			updateSun(SunPosition.getInstance().getSun());			
		}
	}
	
	
	public void createBuilding(Building b, String command) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		
		BuildingView house = new BuildingView();
		house.setX(b.getX());
		house.setY(b.getY());
		house.setZ(b.getZ());
		house.setName(b.getName());
		house.setHeight(b.getHeight());	
		house.setColor(b.getColor());
		
		CoordinatesView c =	CityView.getNewXZ(house.getX(), house.getZ());
		house.setX(c.getX());
		house.setZ(c.getZ());
		house.setCommand(command);

		StringWriter writer = new StringWriter();
		try {
			mapper.writeValue(writer, house);
		} catch (Exception e) {
			LOG.error("convert object to Json", e);
		}

		for (Session peer : peers) {
			try {
				peer.getBasicRemote().sendObject(writer.toString());
			} catch (EncodeException e) {
				LOG.error("sendObject object to Json", e);
			}
		}
	}
	
	public void createRoad(Road r, String command) throws IOException {
		LOG.info("Cityboard createRoad {}:{},{}:{}", r.getXa(),r.getZa(), r.getXb(), r.getZb());
		ObjectMapper mapper = new ObjectMapper();
		
		RoadView road = new RoadView();
		road.setXa(r.getXa());
		road.setXb(r.getXb());
		road.setZa(r.getZa());
		road.setZb(r.getZb());	
		road.setBlue(r.getBlue());
		road.setGreen(r.getGreen());
		road.setRed(r.getRed());
		
		
		CoordinatesView ca =	CityView.getNewXZ(road.getXa(), road.getZa());
		road.setXa(ca.getX());
		road.setZa(ca.getZ());
		CoordinatesView cb =	CityView.getNewXZ(road.getXb(), road.getZb());
		road.setXb(cb.getX());
		road.setZb(cb.getZ());
		
		road.setCommand(command);

		StringWriter writer = new StringWriter();
		try {
			mapper.writeValue(writer, road);
		} catch (Exception e) {
			LOG.error("convert object to Json", e);
		}

		for (Session peer : peers) {
			try {
				peer.getBasicRemote().sendObject(writer.toString());
			} catch (EncodeException e) {
				LOG.error("sendObject object to Json", e);
			}
		}
	}
	
	public void moveBuilding(Building b) throws IOException {
		createBuilding(b, "move");
	}
	
	public void removeBuilding(Building b) throws IOException {
		createBuilding(b, "remove");
	}
	
	public void createRoad(Road r) throws IOException {
		createRoad(r, "create_road");
	}
	
	public void removeRoad(Road r) throws IOException {
		createRoad(r, "remove_road");
	}

	public void createTransport(Building b) throws IOException {
		createBuilding(b, "create_transport");
	}

	public void moveTransport(Building b) throws IOException {
		moveBuilding(b);
	}
	
	public void removeTransport(Building b) throws IOException {
		removeBuilding(b);
	}
	

	public void updateSun(Sun b) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		SunView sunview = new SunView();		
		sunview.setAzimuth(b.getAzimuth());
		sunview.setInclination(b.getInclination());
		sunview.setLuminance(b.getLuminance());
		sunview.setMieCoefficient(b.getMieCoefficient());
		sunview.setMieDirectionalG(b.getMieDirectionalG());
		sunview.setReileigh(b.getReileigh());
		sunview.setTurbidity(b.getTurbidity());		
		sunview.setSun(b.isSun());
		
		sunview.setCommand("updateSun");
		StringWriter writer = new StringWriter();
		try {
			mapper.writeValue(writer, sunview);
		} catch (Exception e) {
			LOG.error("convert object to Json", e);
		}
		for (Session peer : peers) {
			try {
				peer.getBasicRemote().sendObject(writer.toString());
			} catch (EncodeException e) {
				LOG.error("sendObject object to Json", e);
			}
		}
	}

	@OnOpen
	public void onOpen(Session peer) throws IOException {
		LOG.info("onOpen: " + peer);
		peers.add(peer);

		List<Building> mp = ecosystem.getCity().getAllBuildings();
		for (Building b : mp) {
			createBuilding(b, "create");
		}
		
		List<Road> mpr = ecosystem.getCity().getAllRoads();
		for (Road b : mpr) {
			createRoad(b, "create_road");
		}
		

	}

	@OnClose
	public void onClose(Session peer) {
		peers.remove(peer);
	}

}
