package com.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.crypto.Cipher;

public class ImportImage {
	
	List ls = null;
	private String filePath = null;
	private String fileName = null;
	private String fileId = null;
	private String uuid;
	public static final String FILE_SEPARATOR = "/";
	public boolean saveFile(String orgId,String savePath,String fileName, InputStream inputStream) throws IOException {
		if(inputStream==null){
			return false;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		SimpleDateFormat yearSdf = new SimpleDateFormat("yyyy");
		Date nowDate = new Date(System.currentTimeMillis());
		StringBuffer prmSavePath = new StringBuffer(savePath);
		if (prmSavePath == null || prmSavePath.equals("")) {
			return false;
		}

		String prmSaveName = null;
		this.fileName = fileName;

		if (!FileUtil.createFolder(prmSavePath.toString())) {
			return false;
		}
		uuid = GUIDUtil.getKey();//changed by gs
		prmSaveName = uuid + "_" + sdf.format(nowDate);
		if (prmSaveName == null || prmSaveName.equals(""))
			return false;

		if (fileName != null && !"".equals(fileName)) {
			File newFile = new File(prmSavePath.toString() + FILE_SEPARATOR + prmSaveName);
			
			if(!newFile.getParentFile().isDirectory()){
				newFile.mkdirs();
			}
			if (newFile.exists()) {
				newFile.delete();
			}
			EncryptUtil eu = new EncryptUtil();
			FileOutputStream out = new FileOutputStream(newFile);
			try {
				eu.init(null, Cipher.ENCRYPT_MODE);
				byte[] b = new byte[1024];
				int n=0;
				while((n=inputStream.read(b))!=-1){
					out.write(b, 0, n);
				}
				if (eu.encryptfile(newFile, new File(prmSavePath.toString() + FILE_SEPARATOR
						+ prmSaveName + ".aes")))
					newFile.delete(); 
				else {
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			} finally {
				if (newFile.exists())
					newFile.delete();
					out.close();
			}
			filePath = (prmSavePath + FILE_SEPARATOR + prmSaveName + ".aes").replaceFirst(savePath, "");
			
			
			return true;
		} else {
			return false;
		}
	}
	
	
}
