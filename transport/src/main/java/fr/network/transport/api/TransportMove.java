package fr.network.transport.api;

import fr.network.transport.liaison.Product;
import fr.network.transport.physique.Container;

public interface TransportMove<T extends Product> {

	void onArrival(Container<T> container);
}
