package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ScanMain {
	public static void main(String[] args) {
    int codeLine = 0;
    //代码扫描路径
//    String scanPath = "D:/tool/eclipse/sqltool/sqltool/src/com/jtattoo/plaf";
    String scanPath = "D:/tool/eclipse/workspace/wallet-junior";
 
    File rootFile = new File(scanPath);   // 这里其实不必初始化，方法全部写成static的就行了，此处是之前设置了全局变量没来得及改
    ScanMain sm = new ScanMain();
    List<File> files = new ArrayList<>();
    files = sm.scan(getAllFiles(rootFile),files);
    System.out.println(files.size());
    for(File fn :files){
     codeLine+=sm.countCode(fn);
    }
    System.out.println(codeLine);
 } // 按行读取文件，并累计行数

	public int countCode(File file) {
		int i = 0;
		try {
			InputStreamReader read = new InputStreamReader(new FileInputStream(file), "UTF-8");// 考虑到编码格式
			BufferedReader bufferedReader = new BufferedReader(read);
			String line = "";
			while ((line = bufferedReader.readLine()) != null) {
				i++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return i;
	} // 这一步好像多此一举了。

	public static File[] getAllFiles(File f) {
		File[] files = f.listFiles();
		return files;
	} // 使用递归方式扫描文件夹，所有目录向下继续查找，所有文件存入result中。

	public List<File> scan(File[] files, List<File> result) {
		for (File fn : files) {
			if (fn.isDirectory()) {
				scan(fn.listFiles(), result);
			} else {
//				System.out.println("shi");
				result.add(fn);
			}
		}
		return result;
	}
}