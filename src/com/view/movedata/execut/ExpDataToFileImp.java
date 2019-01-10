package com.view.movedata.execut;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.view.movedata.ISaveData;

public class ExpDataToFileImp implements ISaveData{

	private ExpDataToFileCtr ctr;
	public ExpDataToFileImp(ExpDataToFileCtr ctr){
		this.ctr = ctr;
	}
	
	@Override
	public void run() {
		File file = new File(ctr.getFile().getAbsolutePath()+"\\"+ctr.getTableName()+".sql");
		try {
			file.createNewFile();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		BufferedWriter write = null;
		try {
			write = new BufferedWriter( new FileWriter(file));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
				while(true){
					String pop = ctr.getCath().pop();
					synchronized (ctr.getCath()) {
						ctr.getCath().notify();
					}
					write.write(pop);
					write.newLine();
					write.flush();
					
//					System.out.println("写入");

					synchronized (ctr.getCath()) {
						ctr.getCath().notify();
					}
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
	
	
}

