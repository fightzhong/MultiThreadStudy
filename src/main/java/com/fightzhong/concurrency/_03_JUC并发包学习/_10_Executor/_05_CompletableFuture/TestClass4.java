package com.fightzhong.concurrency._03_JUC并发包学习._10_Executor._05_CompletableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class TestClass4 {
	public static void main (String[] args) throws InterruptedException, ExecutionException {
		CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
			return "a";
		});

		CompletableFuture<Integer> future2 = future.thenApply((v) -> {
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
