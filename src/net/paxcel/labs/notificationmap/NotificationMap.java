package net.paxcel.labs.notificationmap;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Basic NotificationMap implementation, Created separate component, which
 * allows, addition of generic type of element to map, and support of
 * notification of addition, remove, and update of objects to map.
 * 
 * For internal storage it is using HashMap, and using its methods for add,
 * remove, get.
 * 
 * @author Kuldeep
 * 
 * @param <T>
 *            type class instance storage used in map
 */
public class NotificationMap<T> {

	/**
	 * Data storage
	 */
	private Map<String, T> objects = new HashMap<String, T>();
	/**
	 * Notification updator, one thread only, to manage sequence of event
	 */
	private Executor executor = Executors.newFixedThreadPool(1,
			new DaemonThreadFactory());

	/**
	 * Listeners objects registered in this {@link NotificationMap}
	 */
	private List<NotificationMapListener<T>> listeners = new LinkedList<NotificationMapListener<T>>();

	/**
	 * Not public only can be created by {@link NotificationMapFactory}
	 */
	NotificationMap() {

	}

	/**
	 * Add new object to {@link NotificationMap}. If object is already existed,
	 * it will be updated and in that case notification will be of update not
	 * add. Note: It does not provide any synchronization
	 * 
	 * @param key
	 *            - key for storage
	 * @param obj
	 *            - value for storage
	 * @return Old object if any or null
	 * @throws NullPointerException
	 *             if passed object is null
	 */
	public T add(String key, T obj) {

		if (obj == null) { // no object added
			throw new NullPointerException("Object can't be null");
		}
		T value = null;
		if (objects.containsKey(key)) { // update, check equals
			value = objects.get(key);
			objects.put(key, obj); // update new object anyway to map
			if (!value.equals(obj)) { // update listener only if new object is
										// not same as old
				updateListeners(2, key, obj, value);
			}
		} else {
			objects.put(key, obj);
			updateListeners(0, key, obj, obj);
		}
		return value;
	}

	/**
	 * Returns added object. Note: It does not provide any synchronization
	 * 
	 * @param key
	 * @return object in the store or null if it is not there
	 */
	public T get(String key) {
		return objects.get(key);
	}

	public T get(String key, T defaultValue) {
		if (objects.containsKey(key)) {
			return objects.get(key);
		}
		return defaultValue;
	}

	/**
	 * Removes object from store and returns it Note: It does not provide any
	 * synchronization
	 * 
	 */
	public T remove(String key) {
		T value = objects.remove(key);
		if (value != null) {
			updateListeners(1, key, value, value);
		}
		return value;
	}

	/**
	 * Add a new listener to this {@link NotificationMap}
	 * 
	 * @param listener
	 */
	public void addNotificationListener(NotificationMapListener<T> listener) {
		listeners.add(listener);
	}

	/**
	 * removes listener from this {@link NotificationMap}
	 */
	public void removeNotificationListener(NotificationMapListener<T> listener) {
		listeners.remove(listener);
	}

	/**
	 * Returns listeners added
	 * 
	 * @return
	 */
	public List<NotificationMapListener<T>> getListeners() {
		return listeners;
	}

	/**
	 * Update listeners added
	 * 
	 * @param transType
	 *            0 = add, 1 = remove, 2 = update
	 * @param key
	 * @param object
	 */
	private void updateListeners(final int transType, final String key,
			final T object, final T oldObject) {
		for (final NotificationMapListener<T> listener : listeners) {
			executor.execute(new Runnable() {
				@Override
				public void run() {
					if (transType == 0) {
						listener.objectAdded(key, object);
					} else if (transType == 1) {
						listener.objectRemoved(key, object);
					} else {
						listener.objectUpdated(key, oldObject, object);
					}
				}
			});
		}
	}

	public Set<String> keySet() {

		return objects.keySet();
	}

	public Collection<T> values() {
		return objects.values();
	}

	public boolean containsKey(String key) {
		return objects.containsKey(key);
	}

	private class DaemonThreadFactory implements ThreadFactory {

		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(r);
			t.setDaemon(true);
			return t;
		}

	}
}
