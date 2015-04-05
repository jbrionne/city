package fr.graph.api;

public class InfoRelationShip {

	private String name;
	private int x;
	private int z;
	private int xD;
	private int zD;
	private Object custom;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
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
	public Object getCustom() {
		return custom;
	}
	public void setCustom(Object custom) {
		this.custom = custom;
	}
	public int getxD() {
		return xD;
	}
	public void setxD(int xD) {
		this.xD = xD;
	}
	public int getzD() {
		return zD;
	}
	public void setzD(int zD) {
		this.zD = zD;
	}
	
}
