package org.uranus.lang;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SingletonHolder {

    /**
     * class instance map holder. no locking on read op (get)
     * when get class instance from key
     */
    private static ConcurrentMap<Class, Object> singletonHolder = new ConcurrentHashMap<Class, Object>(128);

    public static Object getInstance(Class clazz) throws IllegalAccessException {
        Object target;
        try {
            // without lock
            if ((target = singletonHolder.get(clazz)) != null)
                return target;
            if (!Arrays.asList(clazz.getInterfaces()).contains(Singleton.class))
                throw new IllegalAccessException(String.format("%s must extends Singleton insterface", clazz.getName()));
            else {
                synchronized (singletonHolder.getClass()) {
                    // double check
                    if ((target = singletonHolder.get(clazz)) == null)
                        singletonHolder.put(clazz, target = clazz.newInstance());
                    return target;
                }
            }
        } catch (InstantiationException e) {
            throw new IllegalAccessException(String.format("%s newInstance() failed %s", clazz.getName(), e.getMessage()));
        }
    }
}
