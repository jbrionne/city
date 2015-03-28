package fr.network.transport.network;

import fr.network.transport.api.PhysicalMove;
import fr.network.transport.liaison.Link;
import fr.network.transport.liaison.Product;
import fr.network.transport.physique.Cable;
import fr.network.transport.physique.Container;

public class Network {	

	private Link link;
	private PhysicalMove physicalMove;

	public Network(PhysicalMove physicalMove) {
		this.physicalMove = physicalMove;
		this.link = new Link(physicalMove);
	}

	public void send(Address origin, Address destination, Product p) {

		// choix d'un trajet / fonction du prix, rapidité, confort, du produit (volume, fragilité)
		// resolution des transferts entre deux types de liaison, réseau hétérogène		
		//gestion congestion du réseau

		//TODO
		//List<PathInfo> pathInfos = physicalMove.findPath();
		//Path path = new Path();

		Cable cable = new Cable(new Coordinates(origin.getX(), origin.getZ()), new Coordinates(destination.getX(),
				destination.getZ()), true);
		Container container = new Container();
		container.addProductsContainer(p);
		link.send(cable, container);		

	}
}
