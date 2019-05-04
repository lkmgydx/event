package cn.ws.wsevent;

import java.util.EventListener;

/**
 * 事件监听接口
 * 
 * @author hyl 279241400@qq.com 2019年4月8日下午3:46:26
 * 
 */
public interface WsListener<T extends WsEventObj> extends EventListener, WsDelAfter {
	/**
	 * 执行事件
	 * 
	 * @param eventObj 事件对象
	 * @return
	 */
	public void fire(T e);

}
