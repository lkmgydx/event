package cn.ws.wsevent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 测试事件
 * 
 * @author hyl 279241400@qq.com 2019年4月21日下午1:41:55
 *
 */
public class TestOneListener implements WsListener<TestOneObject> {
	private static Logger log = LoggerFactory.getLogger(TestOneListener.class);

	@Override
	public boolean delAfterFire() {
		return false;
	}

	@Override
	public void fire(TestOneObject eventObj) {
		log.info("in TESTONEEVENT name:{} delAfterFire:{}", eventObj.gettName(), delAfterFire());
	}

}
