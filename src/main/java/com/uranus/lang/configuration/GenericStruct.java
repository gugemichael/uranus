package com.uranus.lang.configuration;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class GenericStruct {

	public static List<?> makeGenericList(Type generic) {
		
		if (generic.equals(String.class))
			return new LinkedList<String>();
		else if (generic.equals(Short.class))
			return new LinkedList<Short>();
		else if (generic.equals(Integer.class))
			return new LinkedList<Integer>();
		else if (generic.equals(Long.class))
			return new LinkedList<Long>();
		else if (generic.equals(Float.class))
			return new LinkedList<Float>();
		else if (generic.equals(Double.class))
			return new LinkedList<Double>();
		else if (generic.equals(Byte.class))
			return new LinkedList<Byte>();
		
		return null;
	}
}
