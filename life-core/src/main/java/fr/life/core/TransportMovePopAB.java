package fr.life.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.city.core.Building;
import fr.network.transport.api.TransportMove;
import fr.network.transport.physique.Container;

public class TransportMovePopAB implements TransportMove<PopProduct> {

	private static final Logger LOG = LoggerFactory
			.getLogger(TransportMovePopAB.class);

	private PrivateEntry dest;
	private PrivateEntry ori;

	public TransportMovePopAB(PrivateEntry ori, PrivateEntry dest) {
		this.ori = ori;
		this.dest = dest;
	}

	@Override
	public void onArrival(Container<PopProduct> container) {
		LOG.info("finish " + container);

		launchBA(container, new TransportMovePopBA(dest, ori));

		PopProduct o = container.getProduct();
		if (o != null) {
			Building b = CityRequest.getInstance().findBuildingByName(
					ori.getBuildingName());
			int height = 0;
			if (b != null) {
				height = b.getHeight();
			}
			CityRequest.getInstance().updateBuilding(b.getName(),
					height + o.getNbPerson(), b.getColor());
		}
	}

	public void launchBA(Container<PopProduct> container, TransportMove tm) {
		Building t = CityRequest.getInstance().findTransportByName(
				container.getName());

		CityRequest.getInstance().moveTransport(null, tm, container.getName(),
				t.getX(), t.getZ(),
				container.getFinalDestination().getRoadName(),
				dest.getAddress().getX(), dest.getAddress().getZ(),
				dest.getAddress().getRoadName());
	}

}
