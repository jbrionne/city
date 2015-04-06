package fr.life.core.road;


public class RectangleEntry  {	
	
	private EntryPoint entryPoint1;
	private EntryPoint entryPoint2;
	private EntryPoint entryPoint3;
	
	public RectangleEntry(EntryPoint entryPoint1, EntryPoint entryPoint2,
			EntryPoint entryPoint3) {
		super();
		this.entryPoint1 = entryPoint1;
		this.entryPoint2 = entryPoint2;
		this.entryPoint3 = entryPoint3;
	}
	
	public EntryPoint getEntryPoint1() {
		return entryPoint1;
	}
	public EntryPoint getEntryPoint2() {
		return entryPoint2;
	}
	public EntryPoint getEntryPoint3() {
		return entryPoint3;
	}	

}
