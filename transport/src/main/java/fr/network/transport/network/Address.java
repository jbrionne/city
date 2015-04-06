package fr.network.transport.network;

public class Address {

	
	private int x;
	private int z;
	private String roadName;
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getZ() {
		return z;
	}
	public void setZ(int z) {
		this.z = z;
	}
	
	public String getRoadName() {
		return roadName;
	}
	public void setRoadName(String roadName) {
		this.roadName = roadName;
	}
	@Override
	public String toString() {
		return "Address [x=" + x + ", z=" + z + ", roadName=" + roadName + "]";
	}	
	
}
