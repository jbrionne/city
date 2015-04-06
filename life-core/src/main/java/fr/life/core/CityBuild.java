package fr.life.core;

import java.awt.Color;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.city.core.Building;
import fr.city.core.CityColor;
import fr.life.core.road.EntryPoint;
import fr.life.core.road.Rectangle;
import fr.life.core.road.RectangleEntry;
import fr.life.core.road.VectorPoint;

public class CityBuild {

	private static final Logger LOG = LoggerFactory.getLogger(CityBuild.class);

	private CityState cityState = new CityState();

	private boolean init = false;

	public CityBuild() {
		super();
	}

	public void initialize() {
		if (!init) {
			init = true;
			CityRequest.getInstance().source(100, 0);
			cityState.setRoad(CityRequest.getInstance().road(100, 0, 100, 100,
					Color.RED));
			EntryPoint entryPoint = new EntryPoint(100, 100, cityState
					.getRoad().getName());
			VectorPoint vectorPoint = new VectorPoint(6, 5);
			Rectangle c = new Rectangle(entryPoint, vectorPoint);
			RectangleEntry es = c.build(Color.WHITE);
		}

		Building b = CityRequest.getInstance()
				.transport(100, 0, CityColor.BLUE);
		CityRequest.getInstance().moveTransport(new TransportMovePop(99, 90),
				b.getName(), 100, 0, cityState.getRoad().getName(), 100, 90,
				cityState.getRoad().getName());
	}

}
