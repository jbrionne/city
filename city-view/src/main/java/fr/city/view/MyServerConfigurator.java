package fr.city.view;

import javax.websocket.server.ServerEndpointConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyServerConfigurator extends
		ServerEndpointConfig.Configurator {
	
	public static final Cityboard CITYBOARD = new Cityboard();
	
	private static final Logger LOG = LoggerFactory
			.getLogger(MyServerConfigurator.class);
	@Override
	public <T> T getEndpointInstance(Class<T> endpointClass)
			throws InstantiationException {
		if (endpointClass.equals(Cityboard.class)) {
			LOG.info("cityboard");
			return (T) CITYBOARD;
		} 
		throw new InstantiationException();
	}
}
