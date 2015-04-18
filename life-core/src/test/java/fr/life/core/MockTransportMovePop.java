package fr.life.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.network.transport.api.TransportMove;
import fr.network.transport.physique.Container;

public class MockTransportMovePop implements TransportMove<PopProduct> {

	private static final Logger LOG = LoggerFactory
			.getLogger(MockTransportMovePop.class);

	@Override
	public void onArrival(Container<PopProduct> container) {
		LOG.info("finish " + container.getName());
	}

}
