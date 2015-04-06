package fr.life.core.road;

import java.awt.Color;

import fr.city.core.Road;
import fr.life.core.CityRequest;

public class StraightRoad extends RoadSpec {

	public StraightRoad(EntryPoint entryPoint, VectorPoint vectorPoint) {
		super(entryPoint, vectorPoint);
	}

	public EntryPoint build(Color color) {

		checkVector(getVectorPoint());

		int x = getEntryPoint().getX() + getVectorPoint().getX();
		int z = getEntryPoint().getZ() + getVectorPoint().getZ();
		checkLimit(x, z);

		Road r = CityRequest.getInstance().road(getEntryPoint().getX(),
				getEntryPoint().getZ(), x, z, color);

		return new EntryPoint(x, z, r.getName());
	}

}
