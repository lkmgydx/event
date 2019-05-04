package cn.ws.wsevent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestTwoListener implements WsListener<TestTwoEventObj> {
	private static Logger log = LoggerFactory.getLogger(TestTwoListener.class);

	@Override
	public void fire(TestTwoEventObj eventObj) {
		log.info("in DOWNFILEEVENT name:{} delAfterFire:{}", eventObj.getName(), delAfterFire());
	}

	@Override
	public boolean delAfterFire() {
		return false;
	}

}
