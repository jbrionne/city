package fr.network.transport.physique;

import java.util.LinkedList;
import java.util.List;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.network.transport.api.PhysicalMove;

public class TransportTask extends TimerTask {

	private PhysicalMove physicalMove;
	private List<Container> containers = new LinkedList<>();	
	
	private long time = 0;
	private long lastRefresh = 0;
	
	private Object monitor = new Object();

	private static final Logger LOG = LoggerFactory.getLogger(TransportTask.class);

	public TransportTask(PhysicalMove physicalMove) {
		this.physicalMove = physicalMove;		
	}

	@Override
	public void run() {
		time = time + Physical.PERIOD;
		lastRefresh = lastRefresh + Physical.PERIOD;		
		synchronized (monitor) {		
			for (Container container : containers) {
				
				//TODO
				
				container.getCoordinates().setX((int) (container.getCoordinates().getX() + (int) (Math.round(Math.random() * 2 - 1))));
				container.getCoordinates().setZ((int) (container.getCoordinates().getZ() + (int) (Math.round(Math.random() * 2 - 1))));
				
				if(container.getCoordinates().getX() < 0){
					container.getCoordinates().setX(0);
				}
				if(container.getCoordinates().getZ() < 0){
					container.getCoordinates().setZ(0);
				}
				if(container.getCoordinates().getX() > 199){
					container.getCoordinates().setX(199);
				}
				if(container.getCoordinates().getZ() > 199){
					container.getCoordinates().setZ(199);
				}
				
			}
			if(lastRefresh >= physicalMove.getPeriodRefresh()){
				lastRefresh = 0;
				for (Container container : containers) {
					physicalMove.move(container.getName(), container.getCoordinates());
				}
			}
		}		
	}

	public void addContainer(Container c){
		synchronized (monitor) {
			containers.add(c);
		}
	}
}
