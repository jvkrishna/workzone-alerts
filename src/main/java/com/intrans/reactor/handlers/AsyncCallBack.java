package com.intrans.reactor.handlers;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Handler class for asynchronous processing.
 * 
 * @author Vamsi Krishna J <br />
 *         <b>Date:</b> Feb 6, 2017
 *
 */
public class AsyncCallBack {

	private Callback callback;

	public AsyncCallBack(Callback callback) {
		this.callback = callback;
	}

	public void execute(ThreadPoolTaskExecutor taskExecutor) {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				callback.call();
			}
		};
		taskExecutor.submit(runnable);
	}

	public void execute() {
		new Runnable() {

			@Override
			public void run() {
				callback.call();

			}
		}.run();
	}
}
