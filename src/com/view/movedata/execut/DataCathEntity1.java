package com.view.movedata.execut;

import java.util.Stack;

public class DataCathEntity1 {
	private Stack<String> sql =new Stack<String>();

	private boolean die ;
	public synchronized void push(String string) {
		sql.push(string);
	}

	public synchronized int size() {
		return sql.size();
	}

	public synchronized String pop() {
		return sql.pop();
	}

	public synchronized boolean isEmpty() {
		return sql.empty();
	}

	public synchronized boolean isEnd() {
		return die;
	}

	public synchronized void setDie() {
		die = true;
	}
	public synchronized boolean isDie() {
		return die;
	}
	

}
