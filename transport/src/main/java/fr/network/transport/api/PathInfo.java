package fr.network.transport.api;

public class PathInfo {

	
	private int x;
	private int z;
	private String roadName;
	
	public int getX() {
		return x;
	}
	public String getRoadName() {
		return roadName;
	}
	public void setRoadName(String roadName) {
		this.roadName = roadName;
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
	@Override
	public String toString() {
		return "PathInfo [x=" + x + ", z=" + z + ", roadName=" + roadName + "]";
	}
	
	
}
