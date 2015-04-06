package fr.life.core.road;

import java.awt.Color;

import fr.city.core.Road;
import fr.life.core.CityRequest;

public class CrossRoads extends RoadSpec {

	public CrossRoads(EntryPoint entryPoint, VectorPoint vectorPoint) {
		super(entryPoint, vectorPoint);
	}

	public CrossRoadsEntry build(Color color) {

		checkVector(getVectorPoint());

		int x = getEntryPoint().getX() + getVectorPoint().getX();
		int z = getEntryPoint().getZ() + getVectorPoint().getZ();
		int xLeft = getEntryPoint().getX() + getVectorPoint().getZ();
		int zLeft = getEntryPoint().getZ() + getVectorPoint().getX();
		int xRight = getEntryPoint().getX() - getVectorPoint().getZ();
		int zRight = getEntryPoint().getZ() - getVectorPoint().getX();

		checkLimit(x, z);

		checkLimit(xLeft, zLeft);

		checkLimit(xRight, zRight);

		Road r = CityRequest.getInstance().road(getEntryPoint().getX(),
				getEntryPoint().getZ(), x, z, color);

		Road rLeft = CityRequest.getInstance().road(getEntryPoint().getX(),
				getEntryPoint().getZ(), xLeft, zLeft, color);

		Road rRight = CityRequest.getInstance().road(getEntryPoint().getX(),
				getEntryPoint().getZ(), xRight, zRight, color);

		return new CrossRoadsEntry(new EntryPoint(x, z, r.getName()),
				new EntryPoint(xLeft, zLeft, rLeft.getName()), new EntryPoint(
						xRight, zRight, rRight.getName()));
	}

}
