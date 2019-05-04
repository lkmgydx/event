package cn.ws.wsevent;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 微声事件管理类(线程安全的)
 * 
 * @author hyl 279241400@qq.com 2019年4月20日下午10:42:59
 *
 * @param <T>
 */
public class WsEvent<T extends WsEventObj> {
	private static final Logger log = LoggerFactory.getLogger(WsEvent.class);
	private static final String EVENT_PACKAGE = WsEvent.class.getPackage().getName();
	private static HashMap<String, WsEvent<?>> MAP_EVENT = new HashMap<String, WsEvent<?>>();

	private HashMap<String, CopyOnWriteArrayList<WsListener<T>>> map_Listener = new HashMap<String, CopyOnWriteArrayList<WsListener<T>>>();
	private ThreadLocal<T> TL_T = new ThreadLocal<T>();
	private ThreadLocal<String> TL_T_cache = new ThreadLocal<String>();
	private ThreadLocal<T> TL_T_pri = new ThreadLocal<T>();
	private ThreadLocal<ArrayList<WsEventRunnable<T>>> TL_Run = new ThreadLocal<ArrayList<WsEventRunnable<T>>>();
	private Class<? extends WsEventObj> eventObjCls;

	public static void main(String[] args) {
		// BasicConfigurator.configure();
		WsEvent.addListers(new TestTwoListener());
		WsEvent<TestTwoEventObj> event = WsEvent.getEvent(TestTwoEventObj.class);

		//event.addLister(new TestTwoListener());
		TestTwoEventObj eventObj = event.getEventObj();
		eventObj.setName("YYYY");
		event.fire();
		eventObj.setName("KKK");
		event.fire();

		eventObj = event.getEventObj();
		eventObj.setName("ZZZZZZ");
		event.fire();

		final TestTwoEventObj fa = eventObj ;
		// event.fire();

		new Thread(new Runnable() {

			@Override
			public void run() {
				fa.fire();
				log.info("in run 1");// 新线程,THREADobj为空,将重新创建
				event.fire();
			}
		}).start();
	}

	private WsEvent(Class<T> list) {
		eventObjCls = list;
		map_Listener.put(list.getName(), new CopyOnWriteArrayList<WsListener<T>>());
		MAP_EVENT.put(list.getName(), this);
		log.info("new Event {}", list);
	}

	public T gerPriEventObj() {
		return TL_T_pri.get();
	}

	/**
	 * 添加事件监听实体
	 * 
	 * @param <EventLister>
	 * @param listener
	 */
	public void addLister(WsListener<T> listener) {
		map_Listener.get(eventObjCls.getName()).add(listener);
		log.info("add EventLister {} -> {}", eventObjCls.getName(), listener);
	}

	/**
	 * 直接触发某事件
	 * 
	 * @param <T>
	 * @param t
	 */
	public static <T extends WsEventObj> void fire(Class<T> t) {
		WsEvent.getEvent(t).fire();
	}

	/**
	 * 添加事件
	 * 
	 * @param <V>
	 * @param listener
	 * @return
	 */
	public static <V extends WsEventObj> WsEvent<V> addListers(WsListener<V> listener) {
		@SuppressWarnings("unchecked")
		WsEvent<V> v = WsEvent.getEvent((Class<V>) getParmeter(listener.getClass()));
		v.addLister(listener);
		return v;
	}

	private static Class<?> getExtendCls(Class<?> cls) {
		Class<?>[] interfaces = cls.getInterfaces();
		for (int i = 0; i < interfaces.length; i++) {
			if (interfaces[i] == WsListener.class) {
				return cls;
			}
		}
		if (Modifier.isAbstract(cls.getSuperclass().getModifiers())) {
			return cls;
		}
		return getExtendCls(cls.getSuperclass());
	}

	private static Object getParmeter(Class<?> cls) {
		Class<?> c = getExtendCls(cls);
		ParameterizedType pt = null;
		if (c.getAnnotatedInterfaces().length > 0) {
			pt = ((ParameterizedType) c.getAnnotatedInterfaces()[0].getType());
		} else {
			pt = ((ParameterizedType) c.getAnnotatedSuperclass().getType());
		}
		return pt.getActualTypeArguments()[0];
	}

	public void addCallBack(WsEventRunnable<T> run) {
		if (run == null) {
			return;
		}
		if (TL_Run.get() == null) {
			TL_Run.set(new ArrayList<WsEventRunnable<T>>());
		}
		TL_Run.get().add(run);
	}

	@SuppressWarnings("unchecked")
	public T getEventObj() {
		try {
			Constructor<?> ct = eventObjCls.getConstructors()[0];
			T tobj = (T) ct.newInstance("微声技术");
			if (TL_T.get() != null && getPro(tobj).equals(TL_T_cache.get())) {
				return TL_T.get();
			}
			TL_T.set(tobj);
			TL_T_cache.set(getPro(tobj));
			if (TL_Run.get() == null) {
				TL_Run.set(new ArrayList<WsEventRunnable<T>>());
			}
			log.info("creat [{}] - [{}]", tobj.getEventName(), tobj);
		} catch (Exception e) {
			log.error("creat event error!{}", e);
		}
		return TL_T.get();
	}

	@SuppressWarnings("unchecked")
	public static <T extends WsEventObj> WsEvent<T> getEvent(Class<T> cls) {
		if (MAP_EVENT.containsKey(cls.getName())) {
			return (WsEvent<T>) MAP_EVENT.get((cls.getName()));
		}
		return new WsEvent<T>(cls);
	}

	public void fire(T content) {
		for (Entry<String, CopyOnWriteArrayList<WsListener<T>>> list : map_Listener.entrySet()) {
			List<WsListener<T>> waitToDel = new ArrayList<WsListener<T>>();// 等待删除监听
			for (WsListener<T> v : list.getValue()) {
				try {
					Throwable t = new Throwable();
					StackTraceElement se = t.getStackTrace()[1];
					for (StackTraceElement s : t.getStackTrace()) {
						if (!s.getClassName().startsWith(EVENT_PACKAGE)) {
							se = s;
							break;
						}
					}
					log.info("fire event ({}:{}) [del:{}][name:{}] ", se.getFileName(), se.getLineNumber(), v.delAfterFire(), v);
					v.fire(content);
				} catch (Exception e) {
					log.error("fire error! ", e);
				}
				if (v.delAfterFire()) {
					waitToDel.add(v);
				}
			}
			list.getValue().removeAll(waitToDel);
		}

		List<WsEventRunnable<T>> waitToDel = new ArrayList<WsEventRunnable<T>>();// 等待回调
		if (TL_Run.get() != null) {
			TL_Run.get().forEach(new Consumer<WsEventRunnable<T>>() {
				@Override
				public void accept(WsEventRunnable<T> t) {
					log.info("fire callBack event [del:{}][name:{}]", t.delAfterFire(), t);
					try {
						t.Run(content);
					} catch (Exception e) {
						log.error("fire callBack error!", e);
					}

					if (t.delAfterFire()) {
						waitToDel.add(t);
					}
				}
			});
			TL_Run.get().removeAll(waitToDel);
		}
	}

	public void fire() {
		T content = null;
		if (TL_T.get() == null) {
			if (TL_T_pri.get() == null) {
				content = this.getEventObj();
			} else {
				content = TL_T_pri.get();
			}
		} else {
			content = TL_T.get();
		}
		if (TL_T_pri.get() != null) {
			// 循环调用检查
			if (TL_T_pri.get().equals(TL_T.get())) {
				log.error("fire cross!");
				return;
			}
		}
		this.TL_T_pri.set(content);// 前置对象缓存
		for (Entry<String, CopyOnWriteArrayList<WsListener<T>>> list : map_Listener.entrySet()) {
			List<WsListener<T>> waitToDel = new ArrayList<WsListener<T>>();// 等待删除监听
			for (WsListener<T> v : list.getValue()) {
				try {
					StackTraceElement se = new Throwable().getStackTrace()[1];
					log.info("fire event ({}:{}) [del:{}][name:{}] ", se.getFileName(), se.getLineNumber(), v.delAfterFire(), v);
					v.fire(content);
				} catch (Exception e) {
					log.error("fire error! ", e);
				}
				if (v.delAfterFire()) {
					waitToDel.add(v);
				}
			}
			list.getValue().removeAll(waitToDel);
		}

		final T temp = content;
		List<WsEventRunnable<T>> waitToDel = new ArrayList<WsEventRunnable<T>>();// 等待回调
		if (TL_Run.get() != null) {
			TL_Run.get().forEach(new Consumer<WsEventRunnable<T>>() {

				@Override
				public void accept(WsEventRunnable<T> t) {
					log.info("fire callBack event [del:{}][name:{}]", t.delAfterFire(), t);
					t.Run(temp);
					if (t.delAfterFire()) {
						waitToDel.add(t);
					}
				}
			});
			TL_Run.get().removeAll(waitToDel);
		}
		this.TL_T.set(null);// 当前事件对象清空
	}

	private String getPro(Object obj) {
		if (obj == null) {
			return "";
		}
		return getRst(obj);
	}

	private String getRst(Object obj) {
		Class<?> cls = obj.getClass();
		if (cls == java.lang.Class.class || cls == java.lang.Object.class) {
			return "";
		}
		if (cls.getSuperclass() != null && cls.getSuperclass() != java.lang.Object.class) {
			return getFiledName(cls, obj) + getRst(cls.getSuperclass());
		}
		return getFiledName(cls, obj);
	}

	private String getFiledName(Class<?> cls, Object obj) {
		StringBuffer s = new StringBuffer(cls.getSimpleName());
		Field[] fields = cls.getDeclaredFields();

		for (int i = 0; i < fields.length; i++) {
			try {
				Object p = new PropertyDescriptor(fields[i].getName(), cls).getReadMethod().invoke(obj);
				if (p != null) {
					s.append(" ").append(fields[i].getName()).append(":").append(p);
				}
			} catch (Exception ex) {
			}
		}
		return s.toString();
	}

}
