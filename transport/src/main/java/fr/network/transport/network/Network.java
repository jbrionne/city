package fr.network.transport.network;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.network.transport.api.PathInfo;
import fr.network.transport.api.PhysicalMove;
import fr.network.transport.api.TransportMove;
import fr.network.transport.application.SessionBase;
import fr.network.transport.liaison.Link;
import fr.network.transport.liaison.Product;
import fr.network.transport.physique.Capacity;
import fr.network.transport.physique.Container;

public class Network {	

	private static final Logger LOG = LoggerFactory
			.getLogger(Network.class);
	
	private Link link;
	private PhysicalMove physicalMove;

	public Network(PhysicalMove physicalMove) {
		this.physicalMove = physicalMove;
		this.link = new Link(physicalMove);
	}

	public void send(TransportMove transportMove, String name, Address origin, Address destination, Product p) {

		// choix d'un trajet / fonction du prix, rapidité, confort, du produit (volume, fragilité)
		// resolution des transferts entre deux types de liaison, réseau hétérogène		
		//gestion congestion du réseau
		
		List<PathInfo> pathInfos = physicalMove.findPath(origin, destination);		
//		ROAD100:0 100 0
//		14:45:20.096 [main] INFO  fr.life.core.MockSession - 100:0:100:90 0 0
//		14:45:20.096 [main] INFO  fr.life.core.MockSession - ROAD100:90 100 90/
		if(pathInfos == null || pathInfos.size() < 2) {
			throw new IllegalArgumentException("No path for " + origin + " " + destination);
		}		
		
		for(PathInfo pathInfo : pathInfos){
			LOG.info(pathInfo.toString());
		}
		
		Address ori = new Address();
		ori.setRoadName(origin.getRoadName());
		ori.setX(pathInfos.get(0).getX());
		ori.setZ(pathInfos.get(0).getZ());
		
		Address dest = new Address();
		dest.setRoadName(origin.getRoadName());
		dest.setX(pathInfos.get(1).getX());
		dest.setZ(pathInfos.get(1).getZ());
		
		Capacity capacity = new Capacity();
		capacity.setVolume(20);
		capacity.setVelocity(1);
		
		if(capacity.getVolume() - p.getVolume() < 0){
			throw new IllegalArgumentException("Container is full " + capacity.getVolume()  + " " +  p.getVolume());
		}
		
		Container container = new Container();
		capacity.setVolume(capacity.getVolume() - p.getVolume());		
		container.setCapacity(capacity);		
		container.setTransportMove(transportMove);
		container.setCoordinates(ori);
		container.setDestination(dest);		
		container.setName(name);		
		container.setFinalDestination(destination);
		container.setProduct(p);		
		
		link.send(name, container);

	}
}
