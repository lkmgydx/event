package cn.ws.wsevent;

/**
 * 执行后,删除绑定
 * 
 * @author hyl 279241400@qq.com 2019年4月21日下午6:15:10
 *
 */
public interface WsDelAfter {
	/**
	 * 触发后,自动删除此绑定
	 * 
	 * @return
	 */
	public boolean delAfterFire();
}
