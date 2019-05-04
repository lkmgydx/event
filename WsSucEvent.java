package cn.ws.wsevent;

/**
 * 带有成功与失败的事件
 * 
 * @author hyl 279241400@qq.com 2019年4月21日下午4:40:26
 *
 */
public abstract class WsSucEvent extends WsEventObj {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8857734450886858649L;

	public WsSucEvent(Object source) {
		super(source);
	}

	private Exception exception;

	private String errorMsg;

	private String sucMsg;

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getSucMsg() {
		return sucMsg;
	}

	public void setSucMsg(String sucMsg) {
		this.sucMsg = sucMsg;
	}

	/**
	 * 触发错误事件
	 * 
	 * @param errorMsg
	 */
	public void fireError(String errorMsg) {
		this.errorMsg = errorMsg;
		this.exception = new Exception(errorMsg);
		super.fire();
	}

	public void fireSuc(String msg) {
		this.errorMsg = null;
		this.exception = null;
		this.sucMsg = msg;
		super.fire();
	}

	public boolean isSuc() {
		return exception == null;
	}

	/**
	 * 事件异常
	 * 
	 * @return
	 */
	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

}
