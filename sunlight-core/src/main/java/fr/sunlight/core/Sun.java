package fr.sunlight.core;

public class Sun {
	
//	gui.add( effectController, "turbidity", 1.0, 20.0, 0.1 ).onChange( guiChanged );
//	gui.add( effectController, "reileigh", 0.0, 4, 0.001 ).onChange( guiChanged );
//	gui.add( effectController, "mieCoefficient", 0.0, 0.1, 0.001 ).onChange( guiChanged );
//	gui.add( effectController, "mieDirectionalG", 0.0, 1, 0.001 ).onChange( guiChanged );
//	gui.add( effectController, "luminance", 0.0, 2).onChange( guiChanged );;
//	gui.add( effectController, "inclination", 0, 1, 0.0001).onChange( guiChanged );
//	gui.add( effectController, "azimuth", 0, 1, 0.0001).onChange( guiChanged );
//	gui.add( effectController, "sun").onChange( guiChanged );
	
	private boolean sun = false;
	private double turbidity = 10.0;
	private double reileigh = 2.0;
	private double mieCoefficient = 0.005;
	private double mieDirectionalG = 0.8;
	private double luminance = 1;
	private double inclination = 0.49;
	private double azimuth = 0.25;	
	
	
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
	public boolean isSun() {
		return sun;
	}
	public void setSun(boolean sun) {
		this.sun = sun;
	}
	
	
}
