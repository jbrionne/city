package fr.network.transport.application;

import fr.network.transport.api.PhysicalMove;
import fr.network.transport.liaison.Product;
import fr.network.transport.network.Address;
import fr.network.transport.transport.Transport;

public class Session {
	
	private Transport transport;

	public Session(PhysicalMove physicalMove) {
		transport = new Transport(physicalMove);
	}

	public void send(Address origin, Address destination, Product p) {
		
		//token
		//synchronisation
		
		transport.send(origin, destination, p);
	}

}
