package fr.city.core;

import fr.network.transport.api.Coord2D;

public class Coordinates implements Coord2D {

	private int x;
	private int z;

	public Coordinates(int x, int z) {
		super();
		this.x = x;
		this.z = z;
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public void setX(int x) {
		this.x = x;
	}

	@Override
	public int getZ() {
		return z;
	}

	@Override
	public void setZ(int z) {
		this.z = z;
	}
	

}
