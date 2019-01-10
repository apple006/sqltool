package com.view.movedata.execut;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Stack;
import java.util.concurrent.ExecutionException;

import com.view.movedata.ISaveData;

public class SaveDataToFileImp implements ISaveData{

	private ExpDataToFileCtr ctr;
	public SaveDataToFileImp(ExpDataToFileCtr ctr){
		this.ctr = ctr;
	}
	
	@Override
	public void run() {
		File file = new File(ctr.getFile().getAbsolutePath()+"\\"+ctr.getTableName()+".sql");
		BufferedWriter write = null;
		try {
			write = new BufferedWriter( new FileWriter(file));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
				while(true){
					String pop = ctr.getCath().pop();
					write.write(pop);
					write.newLine();
					write.flush();
					
//					System.out.println("写入");

//					synchronized (ctr.getCath()) {
//						ctr.getCath().notify();
//					}
					write.write(ctr.getCath().pop());
					write.flush();
				System.out.println("写入线程结束");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			try {
				write.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void a() throws MyException{
		int a = 10;
		if(a<100){
			throw new MyException("a不能小于100");
		}
	}
	public static void main(String[] args) {
		try {
			a();
		} catch (MyException e) {
			e.printStackTrace();
		}
	}
	
	
}

class MyException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MyException(String a){
		super(a);
	}
	
	
}
