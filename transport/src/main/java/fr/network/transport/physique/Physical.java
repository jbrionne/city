package fr.network.transport.physique;

import java.util.Timer;

import fr.network.transport.api.PhysicalMove;
import fr.network.transport.api.TransportMove;

public class Physical {

	private TransportTask timerTask;

	public static final long PERIOD = 50;

	public Physical(PhysicalMove physicalMove) {
		timerTask = new TransportTask(physicalMove);
		Timer timer = new Timer(true);
		timer.scheduleAtFixedRate(timerTask, 0, PERIOD);
	}

	public void send(String name, Container container) {		
		timerTask.addContainer(container);
	}

}
