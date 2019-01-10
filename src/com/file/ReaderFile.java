package com.file;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * 将文本text存储指定file中
 * @author Administrator
 *
 */
public class ReaderFile {

	private String path;

	private StringBuffer bufs= new StringBuffer();

	public ReaderFile(String path ){
		this.path = path;
	}
	public String reader() {
		FileInputStream fileInputStream=null;
		BufferedReader buf=null;
		try {
			File file = new File(path);
			fileInputStream = new FileInputStream(file);
			
			InputStreamReader isr = new InputStreamReader(fileInputStream,codeString(file));
			buf = new BufferedReader(isr );
			String readLine ="" ;
			while((readLine = buf.readLine())!=null){
				bufs.append(readLine);
			}
		
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
			if(fileInputStream!=null){
				try {
					fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bufs.toString();
	}
	public  String codeString(File file){
		BufferedInputStream bin  =null;
		String code = null;
		try{
			bin = new BufferedInputStream(
			        new FileInputStream(file));
	        int p = (bin.read() << 8) + bin.read();
	        switch (p) {
	            case 0xefbb:
	                code = "UTF-8";
	                break;
	            case 0xfffe:
	                code = "Unicode";
	                break;
	            case 0xfeff:
	                code = "UTF-16BE";
	                break;
	            default:
	                code = "GBK";
	        }
	         
	        
		}catch(Exception e){
			if(bin!=null){
				try {
					bin.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		finally{
        }
        return (String) (code==null?Charset.defaultCharset():code);
    }
}
