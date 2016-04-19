package org.uranus.lang;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class SingletonProxy {

    public static <T> T getInstance(Class clazz) throws IllegalAccessException {
        try {
            Field field = clazz.getDeclaredField("instance");
            field.setAccessible(true);
            // without lock
            if (field.get(null) != null)
                return (T) field.get(null);

            // must static
            if ((field.getModifiers() & Modifier.STATIC) == 0)
                throw new IllegalAccessException(String.format("%s member 'instance' must has modifier STATIC", clazz.getName()));

            // must implements Singleton
            if (!Arrays.asList(clazz.getInterfaces()).contains(Singleton.class))
                throw new IllegalAccessException(String.format("%s must extends Singleton insterface", clazz.getName()));

            // newInstance()
            synchronized (clazz.getClass()) {
                // double check
                if (field.get(null) == null)
                    field.set(null, clazz.newInstance());
                return (T) field.get(null);
            }
        } catch (InstantiationException e) {
            throw new IllegalAccessException(String.format("%s newInstance() failed %s", clazz.getName(), e.getMessage()));
        } catch (NoSuchFieldException e) {
            throw new IllegalAccessException(String.format("%s must has static member of 'T instance'", clazz.getName()));
        }
    }
}