package com.kodewerk.util;


import java.lang.management.LockInfo;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author kirk
 * @since Jun 30, 2011
 * @version 1.0
 *
 * Copyright 2011 KodeWerk, All rights reserved.
 *
 */

public class LockMonitor {
	
	private static void runProfile() {
		try {
			final int noSeconds = 100;
			final int sleepMillis = 50;
			final int noSamples = noSeconds * 1000 / sleepMillis;
			
			ThreadMXBean thb = ManagementFactory.getThreadMXBean();
			Map<String,Integer> blockCounts = new HashMap<String, Integer>(50);
			for (int i = 0; i < noSamples ; i++) {
				long[] ids = thb.getAllThreadIds();
				ThreadInfo[] infs = thb.getThreadInfo(ids, 0);
				for (ThreadInfo ti : infs) {
					LockInfo lockInf = ti.getLockInfo();
					if (lockInf != null) {
						String key = lockInf.toString();
						Integer cnt = blockCounts.get(key);
						blockCounts.put(key, cnt == null ? 1 : cnt+1);
					}
				}
				
				Thread.sleep(sleepMillis);
			}
			
			System.out.println("Locks:");
			for (String lockName : blockCounts.keySet()) {
				System.out.println(lockName + " : " + blockCounts.get(lockName));
			}
		} catch (InterruptedException iex) {
			Thread.currentThread().interrupt();
    }
}


}
