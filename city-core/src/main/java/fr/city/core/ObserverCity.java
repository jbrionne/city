package fr.city.core;


public interface ObserverCity {

	void createBuilding(Building b);
	
	void removeBuilding(Building b);
	
	void createTransport(Building b);
	
	void removeTransport(Building b);
	
	void moveTransport(Building b);
	
	void createRoad(Road r);	
	
	void removeRoad(Road r);
		

}
