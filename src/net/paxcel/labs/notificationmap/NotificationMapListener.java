package net.paxcel.labs.notificationmap;

/**
 * This can be implemented by classes looking for object addition, removal,
 * update in map
 * 
 * @author Kuldeep
 * 
 * @param <T>
 *            type of object in Map
 */
public interface NotificationMapListener<T> {

	/**
	 * Called when object is added in {@link NotificationMap<T>}
	 * 
	 * @param key
	 *            for the object
	 * @param value
	 *            new object
	 */
	public void objectAdded(String key, T value);

	/**
	 * Called when object is removed from {@link NotificationMap<T>}
	 * 
	 * @param key
	 *            for the object
	 * @param value
	 *            removed object
	 */
	public void objectRemoved(String key, T value);

	/**
	 * Called when same key is updated
	 * 
	 * @param key
	 *            for the object
	 * @param value
	 *            new value
	 */
	public void objectUpdated(String key, T oldValue, T newValue);
}
