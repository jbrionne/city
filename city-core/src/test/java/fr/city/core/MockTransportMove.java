package fr.city.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.network.transport.api.TransportMove;
import fr.network.transport.physique.Container;

public class MockTransportMove implements TransportMove {

	private static final Logger LOG = LoggerFactory
			.getLogger(MockTransportMove.class);

	@Override
	public void finish(Container container) {
		LOG.info("finish " + container.getName());
	}

}
