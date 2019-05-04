package cn.ws.wsevent;

import java.util.Map;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 自动扫描
 * 
 * @author hyl 279241400@qq.com 2019年4月23日上午10:05:35
 *
 */
@Component
public class WsSpringInitScan implements ApplicationListener<ContextRefreshedEvent> {

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (event.getApplicationContext().getParent() == null) {
			Map<String, Object> beans = event.getApplicationContext().getBeansWithAnnotation(WsListenerAutowired.class);
			for (Object bean : beans.values()) {
				addList(bean.getClass(), bean);
			}
		}
	}

	private void addList(Class<?> cls, Object bean) {
		Class<?>[] interfaces = cls.getInterfaces();
		for (int i = 0; i < interfaces.length; i++) {
			if (interfaces[i] == WsListener.class) {
				WsEvent.addListers((WsListener<?>) bean);
				return;
			}
		}
		if (cls.getSuperclass() != null) {
			addList(cls.getSuperclass(), bean);
		}
	}

}
