package fr.life.core;

import java.awt.Color;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.city.core.Building;
import fr.city.core.CityColor;
import fr.city.core.Road;
import fr.network.transport.api.TransportMove;
import fr.network.transport.network.Address;

public class CityBuild {

	private static final int ROAD_X = 99;

	private static final Logger LOG = LoggerFactory.getLogger(CityBuild.class);

	private boolean init = false;

	public CityBuild() {
		super();
	}

	public void initialize() {
		if (!init) {
			init = true;
			Building a = createBuildingA();
			Building b = createBuildingB();

			Road r = createRoadAB();

			Address ori = new Address();
			ori.setX(ROAD_X);
			ori.setZ(a.getZ());
			ori.setRoadName(r.getName());

			Address dest = new Address();
			dest.setX(ROAD_X);
			dest.setZ(b.getZ());
			dest.setRoadName(r.getName());

			PrivateEntry privateEntryA = new PrivateEntry();
			privateEntryA.setAddress(ori);
			privateEntryA.setBuildingName(a.getName());

			PrivateEntry privateEntryB = new PrivateEntry();
			privateEntryB.setAddress(dest);
			privateEntryB.setBuildingName(b.getName());

			TransportMovePopAB tm = new TransportMovePopAB(privateEntryA,
					privateEntryB);
			launchTransportAB(r.getName(), tm);
		}

	}

	public Building createBuildingA() {
		return CityRequest.getInstance().updateOrCreateBuilding(100, 50, 70, CityColor.RED);
	}

	public Building createBuildingB() {
		return CityRequest.getInstance().updateOrCreateBuilding(100, 10, 10, CityColor.BLUE);
	}

	public Road createRoadAB() {
		CityRequest.getInstance().source(99, 0);
		return CityRequest.getInstance()
				.road(ROAD_X, 0, ROAD_X, 100, Color.RED);
	}

	public Building launchTransportAB(String roadName, TransportMove tm) {
		Building t = CityRequest.getInstance().transport(ROAD_X, 10,
				CityColor.YELLOW);
		CityRequest.getInstance().moveTransport(null, tm, t.getName(), t.getX(),
				t.getZ(), roadName, ROAD_X, 50, roadName);
		return t;
	}

}
