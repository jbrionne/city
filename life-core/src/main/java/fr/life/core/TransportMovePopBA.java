package fr.life.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.city.core.Building;
import fr.network.transport.api.TransportMove;
import fr.network.transport.physique.Container;

public class TransportMovePopBA implements TransportMove<PopProduct> {

	private static final Logger LOG = LoggerFactory
			.getLogger(TransportMovePopBA.class);

	private PrivateEntry dest;
	private PrivateEntry ori;

	public TransportMovePopBA(PrivateEntry ori, PrivateEntry dest) {
		this.ori = ori;
		this.dest = dest;
	}

	@Override
	public void onArrival(Container<PopProduct> container) {
		LOG.info("finish " + container);

		PopProduct p = null;
		Building b = CityRequest.getInstance().findBuildingByName(
				ori.getBuildingName());
		int height = 0;
		if (b != null) {
			height = b.getHeight();
		}
		if (height > 0) {
			p = new PopProduct(10, 2);
			int newHeight = height - p.getNbPerson() / 2;
			if (newHeight < 0) {
				newHeight = 0;
			}
			CityRequest.getInstance().updateBuilding(b.getName(), newHeight,
					b.getColor());
		}		
		launchAB(p, container, new TransportMovePopAB(dest, ori));

	}

	public void launchAB(PopProduct p, Container<PopProduct> container, TransportMove tm) {
		Building t = CityRequest.getInstance().findTransportByName(
				container.getName());

		CityRequest.getInstance().moveTransport(p, tm, container.getName(),
				t.getX(), t.getZ(),
				container.getFinalDestination().getRoadName(),
				dest.getAddress().getX(), dest.getAddress().getZ(),
				dest.getAddress().getRoadName());
	}

}
