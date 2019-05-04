package cn.ws.wsevent;

/**
 * 
 * @author hyl 279241400@qq.com 2019年4月24日上午11:31:42
 *
 * @param <T>
 */
public abstract class WsBaseListener<T extends WsEventObj> implements WsListener<T> {

	@Override
	public boolean delAfterFire() {
		return false;
	}
}
