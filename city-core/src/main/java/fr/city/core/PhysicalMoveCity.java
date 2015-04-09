package fr.city.core;

import java.util.List;

import fr.network.transport.api.PathInfo;
import fr.network.transport.api.PhysicalMove;
import fr.network.transport.network.Address;
import fr.network.transport.network.RoadInfo;

public class PhysicalMoveCity implements PhysicalMove {

	private CityBase city;

	public PhysicalMoveCity(CityBase city) {
		super();
		this.city = city;
	}

	@Override
	public void move(String name, Address coord) {
		city.moveTransport(name, coord);
	}

	@Override
	public void remove(String name) {
		city.removeTransport(name);
	}
	
	@Override
	public List<PathInfo> findPath(Address origin, Address destination) {
		return city.findPath(origin, destination);
	}

	@Override
	public long getPeriodRefresh() {
		return 100;
	}

	@Override
	public int getMin() {
		return 0;
	}

	@Override
	public int getMax() {
		return city.getMax() - 1;
	}

	@Override
	public RoadInfo findRoad(String roadName) {
		Road r = city.findRoadByName(roadName);
		if (r != null) {
			RoadInfo ri = new RoadInfo();
			ri.setXa(r.getXa());
			ri.setXb(r.getXb());
			ri.setZa(r.getZa());
			ri.setZb(r.getZb());
			return ri;
		} else {
			return null;
		}
	}

	

}
