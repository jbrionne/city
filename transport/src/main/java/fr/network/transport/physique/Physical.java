package fr.network.transport.physique;

import java.util.Timer;

import fr.network.transport.api.PhysicalMove;


public class Physical {

	private PhysicalMove physicalMove;	
	private TransportTask timerTask;
	
	public static final long PERIOD = 50;	
	
	public Physical(PhysicalMove physicalMove){
		this.physicalMove = physicalMove;		
		timerTask = new TransportTask(physicalMove);
		Timer timer = new Timer(true);
		timer.scheduleAtFixedRate(timerTask, 0, PERIOD);
	}
	
	public void send(Cable cable, Container container) {
		container.setCoordinates(cable.getOri());
		String name = physicalMove.create(cable.getOri());	
		container.setName(name);
		timerTask.addContainer(container);		
	}
	
	
}
