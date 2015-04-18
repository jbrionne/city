package fr.life.core;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.city.core.PhysicalMoveCity;
import fr.network.transport.api.PathInfo;
import fr.network.transport.api.TransportMove;
import fr.network.transport.application.Session;
import fr.network.transport.application.SessionBase;
import fr.network.transport.liaison.Product;
import fr.network.transport.network.Address;
import fr.network.transport.network.RoadInfo;
import fr.network.transport.physique.Container;

public class MockSession implements Session {

	private static final Logger LOG = LoggerFactory
			.getLogger(MockSession.class);

	
		private PhysicalMoveCity physicalMove;

	public MockSession(PhysicalMoveCity physicalMoveCity) {
		this.physicalMove = physicalMoveCity;
	}
	

	@Override
	public void moveTransport(TransportMove transportMove, String name, Address origin, Address destination, Product p) {
		LOG.info("MockSession moveTransport to {} [}", destination.getX(), destination.getZ());
		
		if (origin.getRoadName() == null || "".equals(origin.getRoadName())) {
			throw new IllegalArgumentException("No road name for " + origin);
		}

		if (destination.getRoadName() == null
				|| "".equals(destination.getRoadName())) {
			throw new IllegalArgumentException("No road name for "
					+ destination);
		}

		// verification Address
		RoadInfo r = physicalMove.findRoad(origin.getRoadName());
		if (r == null) {
			throw new IllegalArgumentException("No road for " + origin);
		}		
		
		if(!SessionBase.checkPointOnRoad(origin.getX(), origin.getZ(), r)) {
			throw new IllegalArgumentException("Not on the road for " + origin.getX() + " " + origin.getZ() + " " + r);
		}
		
		RoadInfo rD = physicalMove.findRoad(destination.getRoadName());
		if (rD == null) {
			throw new IllegalArgumentException("No road for " + destination);
		}
		
		if(!SessionBase.checkPointOnRoad(destination.getX(), destination.getZ(), rD)) {
			throw new IllegalArgumentException("Not on the road for " + destination.getX() + " " + destination.getZ() + " " + rD);
		}
		
		List<PathInfo> pathInfos = physicalMove.findPath(origin, destination);
		LOG.info("Path ***");
		for(PathInfo info : pathInfos){
			LOG.info("{} {}", info.getX(), info.getZ());
		}
		
		LOG.info(r.toString());
		LOG.info(rD.toString());
		Address a = new Address();
		a.setRoadName("roadName");
		a.setX(destination.getX());
		a.setZ(destination.getZ());
		physicalMove.move(name, a);
		
		Container container = new Container();
		container.setCoordinates(destination);
		container.setDestination(destination);
		container.setFinalDestination(destination);
		container.setName(name);
		container.setTransportMove(transportMove);
		container.setProduct(p);
		
		transportMove.onArrival(container);
	}

}
