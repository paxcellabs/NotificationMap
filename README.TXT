A notification supported map, which provides notification of objects added, removed, and updated. As well work as a POOL
with generic type of object.


See Sample running example in test package 
=====================================================
		// fetch a repository, true indicates to create a new repository if
		// there is no such repository for those type object exists
		NotificationMap<TestObject> repository = NotificationMapFactory
				.getInstance().getNotificationMap(TestObject.class, true);
		// a listener which will update on changes in repository. 
		repository
				.addNotificationListener(new NotificationMapListener<TestObject>() {

					@Override
					public void objectUpdated(String key, TestObject oldValue, TestObject newValue) {
						System.out.println(key
								+ " - object Updated old object " + oldValue +" - new object " + newValue);
					}

					@Override
					public void objectRemoved(String key, TestObject value) {
						System.out.println(key + " - object removed " + value);
					}

					@Override
					public void objectAdded(String key, TestObject value) {
						System.out
								.println(key + " - New object added " + value);
					}
				});

		// now add obejcts
		for (int i = 0; i < 10; i++) {
			repository.add("" + i, new TestObject(i));
		}
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// remove object with key = 9
		repository.remove("" + 9);

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// add object with same key - it will get updated with update
		// notification
		repository.add("" + 8, new TestObject(100));

		// use direct fetch from repository
		System.out.println("Object in repository accessible "
				+ NotificationMapFactory.getInstance()
						.getNotificationMap(TestObject.class, true).get("7"));

======================================

Installation -

Include project or provided jar file (notificationmap.jar) to your project, and start using it. No third party library required.

========================================================
 