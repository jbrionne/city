package fr.network.transport.transport;

import fr.network.transport.api.PhysicalMove;
import fr.network.transport.liaison.Product;
import fr.network.transport.network.Address;
import fr.network.transport.network.Network;

public class Transport {
	

	private Network network;

	public Transport(PhysicalMove physicalMove) {
		network = new Network(physicalMove);
	}

	public void send(Address origin, Address destination, Product p) {

		//multiple connexion
		//Découpage du produit		
	
		network.send(origin, destination, p);

	}

}
