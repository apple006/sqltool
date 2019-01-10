package com.view.tool;

import java.io.Serializable;
import java.math.BigDecimal;

public class UUID implements Serializable{
//	private char[] number  = {};
	private static char[] arrchar = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f','g','h','i','g','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
							  'A','B','C','D','E','F','G','H','I','G','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
	private int length;
	private String start ;
	private String format;
	private BigDecimal pk = BigDecimal.ZERO;
	private String exp;
	public UUID(String start,int length){
		this.length =length;
		this.start  =start;
		pk =new BigDecimal(start);
		format = "%1$0"+length+"d";
//		exp = 
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public String getStrPk() {
		pk = pk.add(BigDecimal.ONE);
		String format2 = String.format(format,pk.toBigInteger());
		return format2;
	}
	@Override
	public String toString() {
		return "UUID";
	}
	public String getStart() {
		return start;
	}
	
	public static void main(String[] args) {
		int length2 = arrchar.length;
		UUID a = new UUID("1234567111111111111111111111", 20);
		for (int i = 0; i < 100000; i++) {
			System.out.println(a.getStrPk());
		}
	}
}
