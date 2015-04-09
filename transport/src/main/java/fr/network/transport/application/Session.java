package fr.network.transport.application;

import fr.network.transport.api.TransportMove;
import fr.network.transport.liaison.Product;
import fr.network.transport.network.Address;

public interface Session {

	/**
	 * Move a transport from origin to destination with a product
	 * 
	 * @param transportMove callback
	 * @param origin
	 *            coordinates of origin
	 * @param destination
	 *            coordinates of destination
	 * @param p product
	 */
	void moveTransport(TransportMove transportMove, String name, Address origin, Address destination, Product p);

}
