package fr.network.transport.api;

import java.util.List;

public interface PhysicalMove {

	void move(String name, Coord2D c);

	String create(Coord2D c);
	
	void remove(String name);
	
	long getPeriodRefresh();
	
	List<PathInfo> findPath(String nameA, String nameB);

}
