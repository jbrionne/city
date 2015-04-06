package fr.life.core.road;


public class CrossRoadsEntry  {	
	
	private EntryPoint entryPoint;
	private EntryPoint entryPointLeft;
	private EntryPoint entryPointRight;
	
	public CrossRoadsEntry(EntryPoint entryPoint, EntryPoint entryPointLeft,
			EntryPoint entryPointRight) {
		super();
		this.entryPoint = entryPoint;
		this.entryPointLeft = entryPointLeft;
		this.entryPointRight = entryPointRight;
	}
	
	public EntryPoint getEntryPoint() {
		return entryPoint;
	}
	
	public EntryPoint getEntryPointLeft() {
		return entryPointLeft;
	}
	
	public EntryPoint getEntryPointRight() {
		return entryPointRight;
	}

	

}
