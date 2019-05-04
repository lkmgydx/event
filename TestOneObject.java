package cn.ws.wsevent;

/**
 * 测试obj
 * 
 * @author hyl 279241400@qq.com 2019年4月20日下午10:33:43
 *
 */
public class TestOneObject extends WsEventObj {

	private String tName;

	public String gettName() {
		return tName;
	}

	public void settName(String tName) {
		this.tName = tName;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 3770497215186789632L;

	public TestOneObject(Object source) {
		super(source);
	}

	@Override
	public String getEventName() {
		return "test on";
	}

}
