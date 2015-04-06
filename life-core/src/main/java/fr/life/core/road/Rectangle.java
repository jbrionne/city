package fr.life.core.road;

import java.awt.Color;

import fr.city.core.Road;
import fr.life.core.CityRequest;

public class Rectangle extends RoadSpec {

	public Rectangle(EntryPoint entryPoint, VectorPoint vectorPoint) {
		super(entryPoint, vectorPoint);
	}

	public RectangleEntry build(Color color) {

		checkVectorRectangle(getVectorPoint());

		int x1 = getEntryPoint().getX() + getVectorPoint().getX();
		int z1 = getEntryPoint().getZ();

		int x2 = getEntryPoint().getX();
		int z2 = getEntryPoint().getZ() + getVectorPoint().getZ();

		int x3 = getEntryPoint().getX() + getVectorPoint().getX();
		int z3 = getEntryPoint().getZ() + getVectorPoint().getZ();

		checkLimit(x1, z1);

		checkLimit(x2, z2);

		checkLimit(x3, z3);

		Road r1 = CityRequest.getInstance().road(getEntryPoint().getX(),
				getEntryPoint().getZ(), x1, z1, color);

		Road r2 = CityRequest.getInstance().road(getEntryPoint().getX(),
				getEntryPoint().getZ(), x2, z2, color);

		Road r3 = CityRequest.getInstance().road(x1, z1, x3, z3, color);

		Road r4 = CityRequest.getInstance().road(x2, z2, x3, z3, color);

		return new RectangleEntry(new EntryPoint(x1, z1, r1.getName()),
				new EntryPoint(x2, z2, r2.getName()), new EntryPoint(x3, z3,
						r3.getName()));
	}

}
