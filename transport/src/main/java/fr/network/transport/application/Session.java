package fr.network.transport.application;

import fr.network.transport.api.TransportMove;
import fr.network.transport.liaison.Product;
import fr.network.transport.network.Address;

public interface Session {

	/**
	 * Create and move a transport from origin to destination with a product
	 * 
	 * @param transportMove callback
	 * @param origin
	 *            coordinates of origin
	 * @param destination
	 *            coordinates of destination
	 * @param p
	 */
	void send(TransportMove transportMove, String name, Address origin, Address destination, Product p);

}
