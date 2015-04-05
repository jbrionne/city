package fr.network.transport.api;

import java.util.List;

import fr.network.transport.network.Address;
import fr.network.transport.network.RoadInfo;

public interface PhysicalMove {

	void move(String name, Address c);
	
	void remove(String name);
	
	long getPeriodRefresh();
	
	int getMin();
	
	int getMax();
	
	List<PathInfo> findPath(Address origin, Address destination);
	
	RoadInfo findRoad(String roadName);

}
