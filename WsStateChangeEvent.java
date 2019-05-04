package cn.ws.wsevent;

/**
 * 状态变更事件
 * 
 * @author hyl 279241400@qq.com 2019年4月29日下午5:45:29
 *
 */
public abstract class WsStateChangeEvent extends WsSucEvent {

	private String guid;

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -5428391582724592854L;

	public WsStateChangeEvent(Object source) {
		super(source);
	}

}
