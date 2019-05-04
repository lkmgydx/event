package cn.ws.wsevent;

/**
 * 微声动态运行接口
 * 
 * @author hyl 279241400@qq.com 2019年4月21日下午6:16:15
 *
 */
public abstract class WsEventRunnable<T extends WsEventObj> implements WsDelAfter {
	public abstract void Run(T t);

	@Override
	public boolean delAfterFire() {
		return true;
	}

}
