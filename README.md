# ws
1.JAVA事件,使用方式(测试代码)

   		WsEvent.addListers(new TestTwoListener());
		WsEvent<TestTwoEventObj> event = WsEvent.getEvent(TestTwoEventObj.class);

		//event.addLister(new TestTwoListener());
		TestTwoEventObj eventObj = event.getEventObj();
		eventObj.setName("YYYY");
		event.fire();
		eventObj.setName("KKK");
		eventObj.fire(); //可通过全局对象fire触发,也可以通过返回的对象fire触发

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
    
    2.支持spring注解注入 @WsListenerAutowired
    
    
