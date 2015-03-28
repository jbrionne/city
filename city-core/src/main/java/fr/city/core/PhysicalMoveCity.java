package fr.city.core;

import java.util.List;

import fr.network.transport.api.Coord2D;
import fr.network.transport.api.PathInfo;
import fr.network.transport.api.PhysicalMove;

public class PhysicalMoveCity implements PhysicalMove {
	
	private City city;

	public PhysicalMoveCity(City city) {
		super();
		this.city = city;
	}

	@Override
	public void move(String name, Coord2D coord) {
		city.moveTransport(name, coord);
	}

	@Override
	public String create(Coord2D coord) {
		return city.createTransport(coord);
	}

	@Override
	public void remove(String name) {
		city.removeTransport(name);
	}

	@Override
	public long getPeriodRefresh() {
		return 100;
	}

	@Override
	public List<PathInfo> findPath(String nameA, String nameB) {
		return 	city.findPath(nameA, nameB);
	}

}
