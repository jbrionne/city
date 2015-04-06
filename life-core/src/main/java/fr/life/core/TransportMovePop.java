package fr.life.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.city.core.Building;
import fr.city.core.City;
import fr.city.core.CityColor;
import fr.network.transport.api.TransportMove;
import fr.network.transport.physique.Container;

public class TransportMovePop implements TransportMove<PopProduct> {

	private static final Logger LOG = LoggerFactory
			.getLogger(TransportMovePop.class);

	private int x;
	private int z;
	private City city;

	public TransportMovePop(City city, int x, int z) {
		this.x = x;
		this.z = z;
		this.city = city;
	}

	@Override
	public void finish(Container<PopProduct> container) {
		LOG.info("finish " + container.getName());
		PopProduct o = container.getProduct();
		Building b = city.findBuilding(x, z);
		int height = 0;
		if (b != null) {
			height = b.getHeight();
		}
		city.updateOrCreateBuilding(x, z, height + o.getNbPerson(),
				CityColor.RED);
		city.removeTransport(container.getName());
	}

}
