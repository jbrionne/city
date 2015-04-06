package fr.life.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.city.core.Building;
import fr.city.core.CityColor;
import fr.network.transport.api.TransportMove;
import fr.network.transport.physique.Container;

public class TransportMovePop implements TransportMove<PopProduct> {

	private static final Logger LOG = LoggerFactory
			.getLogger(TransportMovePop.class);

	private int x;
	private int z;

	public TransportMovePop(int x, int z) {
		this.x = x;
		this.z = z;
	}

	@Override
	public void finish(Container<PopProduct> container) {
		LOG.info("finish " + container.getName());
		PopProduct o = container.getProduct();
		Building b = CityRequest.getInstance().findBuilding(x, z);
		int height = 0;
		if (b != null) {
			height = b.getHeight();
		}
		CityRequest.getInstance().building(x, z, height + o.getNbPerson(),
				CityColor.RED);
		CityRequest.getInstance().removeTransport(container.getName());
	}

}
