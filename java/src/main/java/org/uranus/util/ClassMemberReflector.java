package org.uranus.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.uranus.lang.Constants;

public class ClassMemberReflector {

	class ClassModifier {

		/**
		 * bit map for expect and unexpect modifier
		 */
		private int modifier = 0;

		public ClassModifier(int modifier) {
			this.modifier = modifier;
		}

		public ClassModifier set(int modifier) {
			this.modifier |= modifier;
			return this;
		}

		public ClassModifier unset(int modifier) {
			this.modifier &= ~modifier;
			return this;
		}

		public boolean is(int modifier) {
			return this.modifier  == modifier;
		}

		public int getModifier() {
			return this.modifier;
		}
	}

	/**
	 * Only care about public member
	 * 
	 */
	private final ClassModifier modifier = new ClassModifier(Modifier.PUBLIC);

	/**
	 * key value separator
	 */
	private final String SEPARATOR;

	public ClassMemberReflector() {
		this(" = ");
	}

	public ClassMemberReflector(String separator, int... modifiers) {
		this.SEPARATOR = separator;
		for (int one : modifiers)
			modifier.set(one);
	}

	/**
	 * Extra class static and public members
	 * 
	 */
	public String toString(Class<?> target) {
		return toString(target, null, new ClassModifier(modifier.getModifier()).set(Modifier.STATIC));
	}

	/**
	 * Extra class instance object non-static and public members
	 * 
	 */
	public String toString(Object instance) {
		return toString(instance.getClass(), instance, new ClassModifier(modifier.getModifier()).unset(Modifier.STATIC));
	}

	private String toString(Class<?> target, Object instance, ClassModifier modifier) {

		StringBuilder classString = new StringBuilder();

		for (Field member : target.getDeclaredFields()) {
			if (!modifier.is(member.getModifiers()))
				continue;
			try {
				classString.append(member.getName().toLowerCase());
				classString.append(SEPARATOR);
				if (member.getType() == int.class)
					classString.append(String.format("%,d",  member.getInt(instance)));
				else if (member.getType() == short.class)
					classString.append(String.format("%,d",  member.getShort(instance)));
				else if (member.getType() == byte.class)
					classString.append(String.format("%,d",  member.getByte(instance)));
				else if (member.getType() == long.class)
					classString.append(String.format("%,d",  member.getLong(instance)));
				else if (member.getType() == long.class)
					classString.append(member.getBoolean(instance));
				else if (member.getType() == AtomicLong.class)
					classString.append(String.format("%,d", ((AtomicLong) member.get(instance)).get()));
				else if (member.getType() == AtomicInteger.class)
					classString.append(String.format("%,d", ((AtomicInteger) member.get(instance)).get()));
				else if (member.getType() == AtomicBoolean.class)
					classString.append(((AtomicBoolean) member.get(instance)).get());
				else if (member.getType() == String.class)
					classString.append((String) member.get(instance));
				classString.append(Constants.DELIMITER);
			} catch (Exception e) {
				return e.getMessage();
			}
		}

		return classString.toString();
	}
}
