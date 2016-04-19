package org.uranus.test;

import org.uranus.lang.SingletonProxy;

/**
 * Author : Michael
 * Date : March 31, 2016
 */
public class Test {

    public static void main(String[] args) throws IllegalAccessException {
//        SingletonTest test = (SingletonTest) SingletonHolder.getInstance(SingletonTest.class);

        SingletonTest test = SingletonProxy.getInstance(SingletonTest.class);

        test.foo();
    }
}
