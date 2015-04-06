package fr.life.core.road;

import fr.life.core.CityRequest;

public abstract class RoadSpec {

	private EntryPoint entryPoint;
	private VectorPoint vectorPoint;
	
	public RoadSpec(EntryPoint entryPoint, VectorPoint vectorPoint) {
		super();
		this.entryPoint = entryPoint;
		this.vectorPoint = vectorPoint;
		checkLimit(entryPoint.getX(), entryPoint.getZ());
	}	
	
	public EntryPoint getEntryPoint() {
		return entryPoint;
	}
	public void setEntryPoint(EntryPoint entryPoint) {
		this.entryPoint = entryPoint;
	}
	public VectorPoint getVectorPoint() {
		return vectorPoint;
	}
	public void setVectorPoint(VectorPoint vectorPoint) {
		this.vectorPoint = vectorPoint;
	}	
	
	protected void checkVectorRectangle(VectorPoint vector) throws AssertionError {
		if(vector.getX() == 0 && vector.getZ() == 0){
			throw new AssertionError("The vector point must be (X,Z)");
		}
		if(vector.getX() == 0 || vector.getZ() == 0){
			throw new AssertionError("X and Z must be different from 0");
		}
	}
	
	protected void checkVector(VectorPoint vector) throws AssertionError {
		if(vector.getX() == 0 && vector.getZ() == 0){
			throw new AssertionError("The vector point must be (X,Z)");
		}
		if(vector.getX() != 0 && vector.getZ() != 0){
			throw new AssertionError("The vector point must be (X,0) or (0,Z)");
		}
	}
	
	protected void checkLimit(int x, int z) {
		if (x < 0 || z < 0 || x > CityRequest.getInstance().getMax() || z > CityRequest.getInstance().getMax()) {
			throw new IllegalArgumentException("The range is  0 to "
					+ (CityRequest.getInstance().getMax() - 1));
		}
	}

}
