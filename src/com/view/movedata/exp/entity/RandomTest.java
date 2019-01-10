package com.view.movedata.exp.entity;

import java.io.Serializable;
import java.util.Random;

public class RandomTest<T> implements Serializable{

	private T[] t;
	private Random random;
	public RandomTest(T[] t){
		this.t = t;
		random = new Random();
	}
	
	public T getRandomTest(){
		return t[random.nextInt(t.length)];
	}
	public T[] getArr(){
		return t;
	}
	@Override
	public String toString() {
		return "Random";
	}
}
