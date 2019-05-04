package cn.ws.wsevent;

/**
 * 下载文件事件
 * 
 * @author hyl 279241400@qq.com 2019年4月20日下午10:31:12
 *
 */
public class TestTwoEventObj extends WsEventObj {

	private static final long serialVersionUID = -5396561706380001804L;

	private String url;
	private String name;
	private Integer time;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	public TestTwoEventObj(Object source) {
		super(source);
	}

	@Override
	public String getEventName() {
		return "test two";
	}

}
