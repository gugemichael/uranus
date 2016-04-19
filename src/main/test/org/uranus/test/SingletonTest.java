package org.uranus.test;

import org.uranus.lang.Singleton;

public class SingletonTest implements Singleton {

    private static SingletonTest instance;

    public void foo() {
        System.out.println("singleton foo called");
    }
}

