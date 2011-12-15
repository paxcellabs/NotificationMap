package net.paxcel.labs.notificationmap;

import java.util.HashMap;
import java.util.Map;

/**
 * This is used by application to create repository
 * 
 * Note - it does not has remove of repository yet now, in case of remove have
 * to update listeners too, with Repository remove listeners which is not in
 * scope of this basic implementation.
 * 
 * @author Kuldeep
 * 
 */
public class NotificationMapFactory {

	/**
	 * singleton
	 */
	private final static NotificationMapFactory instance = new NotificationMapFactory();

	/**
	 * {@link NotificationMap} storage
	 */
	@SuppressWarnings("rawtypes")
	private Map<Class, NotificationMap> map = new HashMap<Class, NotificationMap>();

	/**
	 * Create and returns {@link NotificationMap} with storage of object type passed Class<T>
	 * 
	 * @param <T>
	 *            {@link NotificationMap} with type of object requested in {@link NotificationMap}
	 * @param type
	 *            type of object requested in {@link NotificationMap}
	 * @param create
	 *            to create {@link NotificationMap} if not exists? if true it will create new
	 *            {@link NotificationMap} if not already existed, false will return
	 *            {@link NotificationMap} or null on basis of whether it was previously
	 *            created or not
	 * 
	 * @return
	 */
	public synchronized <T> NotificationMap<T> getNotificationMap(
			Class<T> type, boolean create) {
		@SuppressWarnings("unchecked")
		NotificationMap<T> rep = map.get(type);
		if (rep == null && create) {
			rep = new NotificationMap<T>();
			map.put(type, rep);
		}
		return rep;
	}

	private NotificationMapFactory() {
	}

	/**
	 * Default access for this factory to create various repositories
	 * 
	 * @return factory
	 */
	public static NotificationMapFactory getInstance() {
		return instance;
	}

}
