package com.util;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.Reader;

/**
 * 
 * <p>Description: [鎿嶄綔鏂囦欢鐨勫姪鎵嬬被]</p>
 * @author  <a href="mailto: zhangjj@neusoft.com">寮犲缓鍐�</a>
 * @version $Revision$
 */

public class FileUtil {

	/**
	 * 
	 * @param fileName
	 */
	public static void readFileByBytes(String fileName) {
		File file = new File(fileName);
		InputStream in = null;
		try {
			in = new FileInputStream(file);
			int tempbyte;
			while ((tempbyte = in.read()) != -1) {
				System.out.write(tempbyte);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		try {
			byte[] tempbytes = new byte[100];
			int byteread = 0;
			in = new FileInputStream(fileName);
			while ((byteread = in.read(tempbytes)) != -1) {
				System.out.write(tempbytes, 0, byteread);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 
	 * @param fileName
	 */
	public static void readFileByChars(String fileName) {
		File file = new File(fileName);
		Reader reader = null;
		try {
			reader = new InputStreamReader(new FileInputStream(file));

			int tempchar;
			while ((tempchar = reader.read()) != -1) {
				if (((char) tempchar) != '\r') {
					System.out.print((char) tempchar);
				}
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			reader = new InputStreamReader(new FileInputStream(file));
			char[] tempchars = new char[30];

			int charread = 0;
			while ((charread = reader.read(tempchars)) != -1) {

				if ((charread == tempchars.length) && (tempchars[tempchars.length - 1] != '\r')) {
					System.out.print(tempchars);
				} else {
					for (int i = 0; i < charread; i++) {
						if (tempchars[i] == '\r') {
							continue;
						} else {
							System.out.print(tempchars[i]);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	/**

	 * 
	 * @param fileName
	 */
	public static void readFileByLines(String fileName) {
		File file = new File(fileName);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));

			String tempString = null;
			int line = 1;
			while ((tempString = reader.readLine()) != null) {
				System.out.println("line " + line + ": " + tempString);
				line++;
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	/**
	 * 
	 * @param fileName
	 */
	public static void readFileByRandomAccess(String fileName) {
		RandomAccessFile randomFile = null;
		try {
			randomFile = new RandomAccessFile(fileName, "r");
			long fileLength = randomFile.length();
			int beginIndex = (fileLength > 4) ? 4 : 0;
			randomFile.seek(beginIndex);
			byte[] bytes = new byte[10];
			int byteread = 0;
			while ((byteread = randomFile.read(bytes)) != -1) {
				System.out.write(bytes, 0, byteread);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (randomFile != null) {
				try {
					randomFile.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	/**
	 * @param fileName
	 * @param content
	 */
	public static void appendToFileByRandomAccess(String fileName, String content) {
		try {
			RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
			long fileLength = randomFile.length();
			randomFile.seek(fileLength);
			randomFile.writeBytes(content);
			randomFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param fileName
	 * @param content
	 */
	public static void appendToFileByFileWriter(String fileName, String content) {
		try {
			FileWriter writer = new FileWriter(fileName, true);
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * 鏂板缓鐩綍
	 * 
	 * @param a
	 */
	public static boolean createFolder(String folderPath) {
		try {
			File filePath = new File(folderPath);
			if (!filePath.exists()) {
				filePath.mkdir();
			}
			return true; 
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 鏂板缓鏂囦欢
	 * 
	 * @param a
	 */
	public static void createFile(String filePathAndName, String fileContent) {
		try {
			File filePath = new File(filePathAndName);
			if (!filePath.exists()) {
				filePath.createNewFile();
			}
			FileWriter writer = new FileWriter(filePath);
			PrintWriter pw = new PrintWriter(writer);
			pw.println(fileContent);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 鍒犻櫎鏂囦欢
	 * 
	 * @param a
	 */
	public static void deleteFile(String filePathAndName) {
		try {
			File delFile = new File(filePathAndName);
			delFile.delete();
			//System.out.println("鍒犻櫎鏂囦欢鎴愬姛");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 鍒犻櫎绌烘枃浠跺す
	 */
	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath);
			File filePath = new File(folderPath);
			if (filePath.delete()) {
				System.out.println("鍒犻櫎鏂囦欢澶�" + folderPath + "鎿嶄綔 鎴愬姛鎵ц");
			} else {
				System.out.println("鍒犻櫎鏂囦欢澶�" + folderPath + "鎿嶄綔 鎵ц澶辫触");
			}
		} catch (Exception e) {
			System.out.println("鍒犻櫎鏂囦欢澶规搷浣滃嚭閿�");
			e.printStackTrace();
		}
	}

	/**

	 * 
	 * @param a
	 */
	public static void delAllFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delFolder(path + File.separatorChar + tempList[i]);
			}
		}
	}

	/**
	 * 澶嶅埗鍗曚釜鏂囦欢
	 * 
	 * @param a
	 */
	public static void copyFile(String oldFile, String newFile) {
		try {
			int byteSum = 0;
			int byteRead = 0;
			File oFile = new File(oldFile);
			if (oFile.exists()) {
				InputStream inStream = new FileInputStream(oldFile);
				FileOutputStream fos = new FileOutputStream(newFile);
				byte[] buffer = new byte[1024];
				while ((byteRead = inStream.read(buffer)) != -1) {
					byteSum += byteRead;
					fos.write(buffer, 0, byteRead);
				}
				inStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 澶嶅埗鏁翠釜鏂囦欢澶�
	 * 
	 * @param a
	 */
	public static void copyFolder(String oldPath, String newPath) {
		try {
			(new File(newPath)).mkdirs();
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + File.separator + file[i]);
				}
				if (temp.isFile()) {
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(newPath + "/" + (temp.getName()).toString());
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if (temp.isDirectory()) {
					copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 
	 * 绉诲姩鏂囦欢鍒扮洰褰� 
	 * @param a 
	 */
	public static void moveFolder(String oldPath, String newPath) {
		copyFile(oldPath, newPath);
		deleteFile(oldPath);
		
	}
	
	public static void main(String[] argc){
		for(int i=1001;i<100000;i++){
			deleteFile("c:\\d\\20090901\\"+i);
			System.out.println("娴嬭瘯涓�涓嬶紝杩欐槸绗�"+i+"涓枃浠讹紒");
		}
		/*System.out.println(TimeUtil.getCurrentTime());
		File file  =  new File("c:\\d\\20090901\\9998");
		if(file.exists()){
			System.out.println("鎴愬姛");
		}
		System.out.println(TimeUtil.getCurrentTime());*/
	}
	

}
