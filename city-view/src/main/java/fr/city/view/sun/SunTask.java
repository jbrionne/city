package fr.city.view.sun;

import java.util.Date;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class SunTask extends TimerTask {

	private static final Logger LOG = LoggerFactory.getLogger(SunTask.class);

	private int hour;
	
	@Override
	public void run() {
		LOG.info("Start time sun:" + new Date());
		doSomeWork();
		LOG.info("End time sun:" + new Date());
	}

	private void doSomeWork() {		
		SunRequest.getInstance().updateSun(hour++);
		LOG.info("Hour sun:" + hour);
	}

}
