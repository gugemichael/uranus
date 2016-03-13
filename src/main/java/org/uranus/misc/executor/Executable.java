package org.uranus.misc.executor;

/**
 * Retry Execute entity
 *
 * @author Michael
 */
public interface Executable {

    /**
     * execute method body
     *
     * @return false on retry required or true no more retry
     */
    boolean run();
}