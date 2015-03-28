package fr.city.view.sun;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.city.view.MyServerConfigurator;
import fr.sunlight.core.SunPosition;

class SunRequest {

	private static final Logger LOG = LoggerFactory.getLogger(SunRequest.class);

	private static final SunRequest SUNMANAGER = new SunRequest();

	public static SunRequest getInstance() {
		return SUNMANAGER;
	}

	public void updateSun(int hour) {
		LOG.info("updateSun");
		try {
			MyServerConfigurator.CITYBOARD.updateSun(SunPosition.getInstance()
					.getIncrementAndSun(hour));
		} catch (Exception e) {
			LOG.info("updateSun", e);
		}
	}

}
