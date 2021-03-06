package fr.city.view;

import fr.network.transport.api.Coord2D;

class CoordinatesView implements Coord2D {

	private int x;
	private int z;

	public CoordinatesView(int x, int z) {
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
