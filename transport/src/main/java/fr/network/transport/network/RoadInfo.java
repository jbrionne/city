package fr.network.transport.network;


public class RoadInfo {

	private int xa;
	private int za;
	private int xb;
	private int zb;	
	
	public int getXa() {
		return xa;
	}
	public void setXa(int xa) {
		this.xa = xa;
	}
	public int getZa() {
		return za;
	}
	public void setZa(int za) {
		this.za = za;
	}
	public int getXb() {
		return xb;
	}
	public void setXb(int xb) {
		this.xb = xb;
	}
	public int getZb() {
		return zb;
	}
	public void setZb(int zb) {
		this.zb = zb;
	}
	@Override
	public String toString() {
		return "RoadInfo [xa=" + xa + ", za=" + za + ", xb=" + xb + ", zb="
				+ zb + "]";
	}	

}
