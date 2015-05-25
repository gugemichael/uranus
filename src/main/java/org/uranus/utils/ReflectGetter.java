package org.uranus.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ReflectGetter
{
	private static final char LINEBREAK = String.format("%n").charAt(0);

	/**
	 * bit map for expect and unexpect modifier
	 */
	private static int typeMap = 0;
	
	protected static void checkModifier(int... types) {
		for (int type : types)
			typeMap |= type;
	}

	protected static boolean expectModifier(int modifiers) {
		return (modifiers & typeMap) == typeMap;
	}
	
	static {
		// only deal static and public member
		checkModifier(Modifier.STATIC, Modifier.PUBLIC);
	}

	
	/**
	 * parse class
	 */
	private final Class<?> target;
	
	/**
	 * key value separator
	 */
	private final String SEPARATOR;
	
	
	public ReflectGetter(Class<?> target) {
		this(target, " = ");
	}
	
	public ReflectGetter(Class<?> target, String separator) {
		this.target = target;
		this.SEPARATOR = separator;
	}
	
	public String extract() {
		if (target == null)
			return "NULL";
		StringBuilder classString = new StringBuilder();
		for (Field member : target.getDeclaredFields()) {
			if (!expectModifier(member.getModifiers()))
				continue;
			try {
				classString.append(member.getName().toLowerCase());
				classString.append(SEPARATOR);
				if (member.getType() == AtomicLong.class)
					classString.append(String.format("%,d", ((AtomicLong) member.get(null)).get()));
				else if (member.getType() == AtomicInteger.class)
					classString.append(String.format("%,d", ((AtomicInteger) member.get(null)).get()));
				else if (member.getType() == AtomicBoolean.class)
					classString.append(((AtomicBoolean) member.get(null)).get());
				else if (member.getType() == String.class)
					classString.append((String) member.get(null));
				classString.append(LINEBREAK);
			} catch (Exception e) {
				return e.getMessage();
			}
		}
	
		return classString.toString();
	}
}
