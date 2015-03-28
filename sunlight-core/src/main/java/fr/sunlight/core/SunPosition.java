package fr.sunlight.core;

public class SunPosition {
	
	private static final SunPosition SUNPOSITION = new SunPosition();
	
	private SunPosition(){		
	}
	
	public static SunPosition getInstance(){
		return SUNPOSITION;
	}

	private double az = 0.25;
	/**
	 * 
	 * @param hour
	 *            0 - 23
	 * @return Sun parameters
	 */
	public Sun getIncrementAndSun(int hour) {		
		Sun s = new Sun();
		s.setInclination(az);
		az = az - 0.01;
		if(az <= -1) {
			az = 1;
		}
		return s;
	}
	
	public Sun getSun() {		
		Sun s = new Sun();
		s.setInclination(az);		
		return s;
	}
}
