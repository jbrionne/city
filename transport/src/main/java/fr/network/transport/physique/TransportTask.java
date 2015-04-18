package fr.network.transport.physique;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.network.transport.api.PhysicalMove;
import fr.network.transport.api.TransportMove;
import fr.network.transport.application.SessionBase;

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
	
	private List<Container> containersToRemove = new ArrayList<Container>();
		

	@Override
	public void run() {
		time = time + Physical.PERIOD;
		lastRefresh = lastRefresh + Physical.PERIOD;		
		synchronized (monitor) {		
			for (Container container : containers) {
				if(container.getCoordinates().getX() == container.getDestination().getX() && container.getCoordinates().getZ() == container.getDestination().getZ()) {
					containersToRemove.add(container);
				} else {				
					int dirX = 1;
					if(container.getCoordinates().getX() > container.getDestination().getX()){
						dirX = -1;
					}
					int dirZ = 1;
					if(container.getCoordinates().getZ() > container.getDestination().getZ()){
						dirZ = -1;
					}
					
					if(container.getCoordinates().getX() != container.getDestination().getX()){
						if(Math.abs(container.getCoordinates().getX() - container.getDestination().getX()) <= container.getCapacity().getVelocity()){
							container.getCoordinates().setX((int) (container.getDestination().getX()));
						} else {						
							container.getCoordinates().setX((int) (container.getCoordinates().getX() + (int) (dirX * container.getCapacity().getVelocity())));
						}
					}
					
					if(container.getCoordinates().getZ() != container.getDestination().getZ()){
						if(Math.abs(container.getCoordinates().getZ() - container.getDestination().getZ()) <= container.getCapacity().getVelocity()){
							container.getCoordinates().setZ((int) (container.getDestination().getZ()));
						} else {	
							container.getCoordinates().setZ((int) (container.getCoordinates().getZ() + (int) (dirZ * container.getCapacity().getVelocity())));
						}
					}
					
					if(container.getCoordinates().getX() < physicalMove.getMin()){
						container.getCoordinates().setX(physicalMove.getMin());
					}
					if(container.getCoordinates().getZ() < physicalMove.getMin()){
						container.getCoordinates().setZ(physicalMove.getMin());
					}
					if(container.getCoordinates().getX() > physicalMove.getMax()){
						container.getCoordinates().setX(physicalMove.getMax());
					}
					if(container.getCoordinates().getZ() > physicalMove.getMax()){
						container.getCoordinates().setZ(physicalMove.getMax());
					}
				}
			}
			
			for(Container container : containersToRemove){
				if(container.getCoordinates().getX() == container.getFinalDestination().getX() && container.getCoordinates().getZ() == container.getFinalDestination().getZ()) {
					LOG.info("arrive " +  container);
					container.getTransportMove().onArrival(container);
					containers.remove(container);
				} else {
					LOG.info("send " +  container.getName());
					LOG.info("getCoordinates " + container.getCoordinates());
					LOG.info("getDestination " + container.getDestination());
					LOG.info("getFinalDestination " + container.getFinalDestination());
					SessionBase.getInstance(physicalMove).moveTransport(container.getTransportMove(), container.getName(), container.getDestination(), container.getFinalDestination(), container.getProduct());
					containers.remove(container);
				}
				
			}
			containersToRemove.clear();
			
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
