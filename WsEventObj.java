package cn.ws.wsevent;

import java.util.EventObject;

/**
 * 事件传递数据抽象对象
 * 
 * @author hyl 279241400@qq.com 2019年4月8日下午9:46:20
 * 
 */
public abstract class WsEventObj extends EventObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WsEventObj(Object source) {
		super(source);
	}

	/**
	 * 获取被监听对象名称
	 * 
	 * @return
	 */
	public abstract String getEventName();

	@SuppressWarnings("unchecked")
	public void fire() {
		@SuppressWarnings("rawtypes")
		WsEvent cl = WsEvent.getEvent(this.getClass());
		cl.fire(this);
	}
}
