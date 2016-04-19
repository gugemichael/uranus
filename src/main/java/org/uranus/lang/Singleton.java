package org.uranus.lang;


/**
 * Declared only. single instance class interface. we should
 * suply a static member field named "instance". {@link SingletonHolder}
 * will invoke newInstance() of the class. useage as follows:
 *
 * class SingletonClass implements Singleton {
 *
 *     private static SingletonClass instance;
 *
 *     ....
 * }
 *
 */
public interface Singleton {

}
