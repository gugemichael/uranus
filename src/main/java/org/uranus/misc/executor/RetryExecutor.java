package org.uranus.misc.executor;

/**
 * Retry executor, run an loop with retry policy
 * under user defined
 */
public class RetryExecutor {

    /**
     * retry strategy when failed or exception occured
     *
     * @author Michael
     */
    public static abstract class RetryPolicy {

        public abstract boolean onRetry(int n);

        public boolean onException(Throwable e) {
            return true;
        }
    }

    // execute max time
    private int times;

    // retry policy
    RetryPolicy policy;

    public RetryExecutor times(int times) {
        this.times = times;
        return this;
    }

    public RetryExecutor policy(RetryPolicy policy) {
        this.policy = policy;
        return this;
    }

    public void execute(Executable runner) {
        int n = times;
        while (n-- != 0) {
            try {
                if (!runner.run() && (policy == null || policy.onRetry(n)))
                    continue;
                return;
            } catch (Throwable e) {
                if (policy.onException(e))
                    break;
            }
        }
    }


}
