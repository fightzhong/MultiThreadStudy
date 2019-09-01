package com.fightzhong.concurrency._02_线程的进阶知识.ThreadLocal.ThreadContextTest;

public class Context {
	private String name;
	private String id;

	public String getName () {
		return name;
	}

	public void setName (String name) {
		this.name = name;
	}

	public String getId () {
		return id;
	}

	public void setId (String id) {
		this.id = id;
	}

	@Override
	public String toString () {
		return "Context{" +
		"name='" + name + '\'' +
		", id='" + id + '\'' +
		'}';
	}
}
