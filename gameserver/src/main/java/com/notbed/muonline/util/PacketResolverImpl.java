/**
 *
 */
package com.notbed.muonline.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Alexandru Bledea
 * @since Jul 30, 2014
 */
public final class PacketResolverImpl<T> implements PacketResolver<T> {

	private static final Logger log = LoggerFactory.getLogger(PacketResolverImpl.class);

	private final Map<Integer, Object> packets = new HashMap<>();

	private final Class<T> packetClasz;

	/**
	 * @param packetClasz
	 */
	public PacketResolverImpl(final Class<T> packetClasz) {
		this.packetClasz = packetClasz;
	}

	/**
	 * @param object
	 * @throws RegistrationException
	 */
	public void register(final T object) throws RegistrationException {
		log.debug("Request to register {}", HeaderUtil.getName(object));
		checkValidFormat(object);
		add(object);
	}

	/**
	 * @param o
	 * @throws RegistrationException
	 */
	@SuppressWarnings ("unchecked")
	private void add(final T object) throws RegistrationException {
		final Class<? extends Object> class1 = object.getClass();
		final int[] value = HeaderUtil.getHeader(class1);
		final int lastIndex = value.length - 1;

		Map<Integer, Object> map = packets;
		for (int i = 0; i < lastIndex; i++) {
			final int currentHeader = value[i];
			if (!map.containsKey(currentHeader)) {
				final Map<Integer, Object> auxMap = new HashMap<>();
				map.put(currentHeader, auxMap);
				map = auxMap;
				continue; // no need to check, we already know it's a map
			}
			final Object mapValue = map.get(currentHeader);
			if (mapValue instanceof Map<?, ?>) {
				map = (Map<Integer, Object>) mapValue;
			} else {
				throw new RegistrationConflictException(object, getPackets(mapValue));
			}
		}
		final int lastValue = value[lastIndex];
		if (map.containsKey(lastValue)) {
			throw new RegistrationConflictException(object, getPackets(packets.get(value[0])));
		}
		map.put(lastValue, object);
	}

	/**
	 * @param object
	 * @return
	 */
	@SuppressWarnings ("unchecked")
	private Collection<T> getPackets(final Object object) {
		if (packetClasz.isAssignableFrom(object.getClass())) {
			return Collections.singleton((T) object);
		}
		if (object instanceof Map<?, ?>) {
			final List<T> packets = new ArrayList<>();
			for (final Object obj : ((Map<?, ?>) object).values()) {
				packets.addAll(getPackets(obj));
			}
			return packets;
		}
		throw new IllegalArgumentException(String.valueOf(object.getClass()) + " / " + object);
	}

	/**
	 * @param object
	 */
	private static void checkValidFormat(final Object object) throws RegistrationException {
		final Class<? extends Object> class1 = object.getClass();
		if (!class1.isAnnotationPresent(Header.class)) {
			throw new RegistrationException(class1 + " does not have any header defined!");
		}
		final Header annotation = class1.getAnnotation(Header.class);
		final int[] value = annotation.value();
		if (value.length == 0) {
			throw new RegistrationException(class1 + " does not define any bytes for the header!");
		}
	}

	/* (non-Javadoc)
	 * @see com.PacketResolver#resolvePacket(byte[])
	 */
	@SuppressWarnings ("unchecked")
	@Override
	public T resolvePacket(final Data data) {
		Map<Integer, Object> map = packets;
		while (data.hasNext()) {
			final int current = data.readUnsignedByte();
			if (!map.containsKey(current)) {
				break;
			}
			final Object object = map.get(current);
			if (packetClasz.isAssignableFrom(object.getClass())) {
				log.debug("Identified {}", HeaderUtil.getName(object));
				return (T) object; // found it!
			}
			if (object instanceof Map<?, ?>) {
				map = (Map<Integer, Object>) object; // identified part of header
			} else { // impossible... maybe?
				log.debug("Unexpected object in map: {}", object.getClass());
				break;
			}
		}
		return null;
	}
}
