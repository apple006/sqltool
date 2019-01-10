package com.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.SecureRandom;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class EncryptUtil {

	private static final String AES = "AES";
	private static final String PWD="www.sfit-fiss.com";
	private Cipher cipher;

	public void init(String prmPassword, int prmMode) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
		String password=null;
		String mode=null;
		if(prmPassword==null ||prmPassword.equals(""))
			password=PWD;
		else
			password=prmPassword;
		
		KeyGenerator kgen = KeyGenerator.getInstance(AES);

		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
		secureRandom.setSeed(password.getBytes());
		kgen.init(128, secureRandom);
		SecretKey skey = kgen.generateKey();
		byte[] raw = skey.getEncoded();
		SecretKeySpec skeySpec = new SecretKeySpec(raw, AES);
		cipher = Cipher.getInstance(AES);
		cipher.init(prmMode, skeySpec);
	}
	

	public boolean encryptfile(File src, File dest) throws Exception {
		FileInputStream fis =null;
		FileOutputStream fos=null;
		try {
			fis= new FileInputStream(src);
			fos= new FileOutputStream(dest);

			int blockSize = cipher.getBlockSize();
			byte[] buffer = new byte[blockSize];
			byte[] bytes;

			int i;
			for (i = fis.read(buffer); i == blockSize; i = fis.read(buffer)) {
				bytes = cipher.update(buffer);
				fos.write(bytes);
			}
			if (i == -1) {
				bytes = cipher.doFinal();
			} else if (i == 0) {
				bytes = cipher.doFinal(new byte[0]);
			} else {
				bytes = cipher.doFinal(buffer, 0, i);
			}
			fos.write(bytes);
			
			return true;
		} catch (Exception e) {			
			e.printStackTrace();			
			throw e;			
		}finally{
			fos.close();
			fis.close();
		}
		
	}
	public static void main(String[] args) throws Exception {	
		
	/*	File dir = new File("z:\\x\\");
        File file[] = dir.listFiles();
        for (int i=0;i<file.length;i++){
        	File dest = new File(file[i].getPath()+".xls");
        	final EncryptUtil encryptor = new EncryptUtil();
    		encryptor.init(PWD, Cipher.DECRYPT_MODE);
    		encryptor.encryptfile(file[i], dest);
        	
        }*/

        EncryptUtil encryptor = new EncryptUtil();
        if("e".equals(args[0]))
        	encryptor.init(PWD, Cipher.ENCRYPT_MODE);
        else
        	encryptor.init(PWD, Cipher.DECRYPT_MODE);
		System.out.println("arg[1]="+args[1]);
		System.out.println("arg[2]="+args[2]);
		encryptor.encryptfile(new File(args[1]), new File(args[2]));
    	
		
		
		
		/*File src = new File("z:\\x1\\B906_6.xls.aes");
		File dest = new File("C:\\200904\\Java锟斤拷锟斤拷锟�.rar.aes.rar");
		File srcB = new File("C:\\200904\\Java锟斤拷锟斤拷锟�.rar.aes.rar");

		System.out.println(TimeUtil.timestampToString(new Date()));
		final EncryptUtil encryptor = new EncryptUtil();
		encryptor.init(PWD, Ciphacryptor.encryptfile(src, dest);

		System.out.println(TimeUtil.timestampToString(new Date()));*/
		
	/*	encryptor.init(PWD, Cipher.DECRYPT_MODE);
		encryptor.encryptfile(dest, srcB);

		System.out.println(TimeUtil.timestampToString(new Date()));*/
		
		
	}

}
