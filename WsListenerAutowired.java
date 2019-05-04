package cn.ws.wsevent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 事件自动注入
 * 
 * @author hyl 279241400@qq.com 2019年4月23日上午9:56:50
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface WsListenerAutowired {

}
