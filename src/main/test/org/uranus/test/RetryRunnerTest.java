package org.uranus.test;

import org.uranus.misc.executor.Executable;
import org.uranus.misc.executor.RetryExecutor;
import org.uranus.misc.executor.RetryExecutor.RetryPolicy;

public class RetryRunnerTest {

	// unit test
	public static void main(String[] args) {
		
		RetryExecutor retryExecutor = new RetryExecutor().times(Integer.MAX_VALUE).policy(new RetryPolicy() {
			
			@Override
			public boolean onRetry(int n) {
				return n % 10 != 0;
			}
			
		});
		
		
		retryExecutor.execute(new Executable() {
			
			@Override
			public boolean run() {
				System.out.println("Faild");
				return false;
			}
			
		});
		
	}

}