package com.fightzhong.concurrency._03_JUC并发包学习._10_Executor._05_CompletableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class TestClass5 {
	public static void main (String[] args) throws InterruptedException {
		CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "A");

		CompletableFuture<Integer> handle = future.handle((v, t) -> {
			return 1;
		});

		Thread.currentThread().join();
	}

	public static void sleep (int seconds) {
		try {
			TimeUnit.SECONDS.sleep( seconds );
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
