package com.view.movedata.exp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class WriteSql implements Runnable{

	private File file;
	
	public WriteSql(File file){
		this.file = file;
	}
	@Override
	public void run() {
		try {
			BufferedWriter wirter = new BufferedWriter( new FileWriter(file));
//				wirter.write(sqls[i]);
				
			wirter.flush();
			wirter.close();
			
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

}
