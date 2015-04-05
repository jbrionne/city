package fr.network.transport.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.network.transport.api.PhysicalMove;
import fr.network.transport.api.TransportMove;
import fr.network.transport.liaison.Product;
import fr.network.transport.network.Address;
import fr.network.transport.network.RoadInfo;
import fr.network.transport.transport.Transport;

public class SessionBase implements Session {

	private static final Logger LOG = LoggerFactory
			.getLogger(SessionBase.class);

	private Transport transport;
	private PhysicalMove physicalMove;
	
	private static SessionBase sessionBase;
	private static Object monitorSession = new Object();

	private SessionBase(PhysicalMove physicalMove) {
		this.physicalMove = physicalMove;
		transport = new Transport(physicalMove);
	}
	
	public static SessionBase getInstance(PhysicalMove physicalMove){
		if(sessionBase == null) {
			synchronized(monitorSession) {
				if(sessionBase == null) {
					sessionBase = new SessionBase(physicalMove);
				}
			}
		}
		return sessionBase;
	}

	@Override
	public void send(TransportMove transportMove, String name, Address origin, Address destination, Product p) {

		// token
		// synchronisation

		if (origin.getRoadName() == null || "".equals(origin.getRoadName())) {
			throw new IllegalArgumentException("No road name for " + origin);
		}

		if (destination.getRoadName() == null
				|| "".equals(destination.getRoadName())) {
			throw new IllegalArgumentException("No road name for "
					+ destination);
		}		
		
		RoadInfo r = checkRoad(origin);

		RoadInfo rD = checkRoad(destination);

		LOG.info(r.toString());
		LOG.info(rD.toString());

		transport.send(transportMove, name, origin, destination, p);
	}

	private RoadInfo checkRoad(Address destination) {
		LOG.info("xx {}" ,destination.getRoadName());
		RoadInfo rD = physicalMove.findRoad(destination.getRoadName());
		if (rD == null) {
			throw new IllegalArgumentException("No road for " + destination);
		}

		if (!SessionBase.checkPointOnRoad(destination.getX(),
				destination.getZ(), rD)) {
			throw new IllegalArgumentException("Not on the road for "
					+ destination.getX() + " " + destination.getZ() + " " + rD);
		}
		return rD;
	}

	public static boolean checkPointOnRoad(int x, int y, RoadInfo r) {
		boolean res = false;
		if (r.getXa() == r.getXb()) {
			if (x == r.getXa()) {
				res = localCheckMinMax(r.getZa(), r.getZb(), y);
			}

		} else if (r.getZa() == r.getZb()) {
			if (y == r.getZa()) {
				res = localCheckMinMax(r.getXa(), r.getXb(), x);
			}
		}
		return res;
	}

	private static boolean localCheckMinMax(int xA, int Xb, int y) {
		boolean res = false;
		int minZ = xA;
		int maxZ = Xb;
		if (xA > Xb) {
			minZ = Xb;
			maxZ = xA;
		}
		if (y >= minZ && y <= maxZ) {
			res = true;
		}
		return res;
	}

}
