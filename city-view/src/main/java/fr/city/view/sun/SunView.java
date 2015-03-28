package fr.city.view.sun;

public class SunView {	

	
	private String command;	
	private boolean sun;
	private double turbidity;
	private double reileigh;
	private double mieCoefficient;
	private double mieDirectionalG;
	private double luminance;
	private double inclination;
	private double azimuth;	
	
	public double getTurbidity() {
		return turbidity;
	}
	public void setTurbidity(double turbidity) {
		this.turbidity = turbidity;
	}
	public double getReileigh() {
		return reileigh;
	}
	public void setReileigh(double reileigh) {
		this.reileigh = reileigh;
	}
	public double getMieCoefficient() {
		return mieCoefficient;
	}
	public void setMieCoefficient(double mieCoefficient) {
		this.mieCoefficient = mieCoefficient;
	}
	public double getMieDirectionalG() {
		return mieDirectionalG;
	}
	public void setMieDirectionalG(double mieDirectionalG) {
		this.mieDirectionalG = mieDirectionalG;
	}
	public double getLuminance() {
		return luminance;
	}
	public void setLuminance(double luminance) {
		this.luminance = luminance;
	}
	public double getInclination() {
		return inclination;
	}
	public void setInclination(double inclination) {
		this.inclination = inclination;
	}
	public double getAzimuth() {
		return azimuth;
	}
	public void setAzimuth(double azimuth) {
		this.azimuth = azimuth;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public boolean isSun() {
		return sun;
	}
	public void setSun(boolean sun) {
		this.sun = sun;
	}
	
	
}
