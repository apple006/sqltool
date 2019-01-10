package com.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
/**
 * 将文本text存储指定file中
 * @author Administrator
 *
 */
public class SaveFile implements Runnable{

	private String path;
	private String text;

	public SaveFile(String path , String text){
		this.path = path;
		this.text = text;
	}
	@Override
	public void run() {
		FileWriter fileOutputStream=null;
		BufferedWriter buf=null;
		try {
			File file = new File(path);
			file.createNewFile();
			OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file),Charset.defaultCharset());
			buf = new BufferedWriter(writer );
			buf.write(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		finally {
			if(buf!=null){
				try {
					buf.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(fileOutputStream!=null){
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		
		}
	}
	public void save() {
		Thread th = new Thread(this);
		th.start();
	}		

}
