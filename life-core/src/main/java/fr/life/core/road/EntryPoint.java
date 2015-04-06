package fr.life.core.road;

public class EntryPoint {

	private int x;
	private int z;
	private String roadName;
	
	public EntryPoint(int x, int z, String roadName) {
		super();
		this.x = x;
		this.z = z;
	}
	public int getX() {
		return x;
	}
	
	public int getZ() {
		return z;
	}
	
	public String getRoadName() {
		return roadName;
	}
	
	public void setRoadName(String roadName) {
		this.roadName = roadName;
	}	
}
