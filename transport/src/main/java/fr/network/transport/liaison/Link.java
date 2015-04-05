package fr.network.transport.liaison;

import fr.network.transport.api.PhysicalMove;
import fr.network.transport.api.TransportMove;
import fr.network.transport.physique.Container;
import fr.network.transport.physique.Physical;

public class Link {

	// C'est la fonction de contrôle de flux. Une technique très simple,
	// employée dans les réseaux Ethernet, qui interdit les émissions continues
	// à tous les hôtes du réseau. Une émission ne peut avoir lieu que si tous
	// les hôtes ont eu le temps matériel de détecter que le média partagé est
	// libre.

	private Physical physical;

	public Link(PhysicalMove physicalMove) {
		physical = new Physical(physicalMove);
	}

	public void send(String name, Container container) {		
		physical.send(name, container);
	}

}
