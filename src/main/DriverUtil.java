package main;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * description
 * 
 * @author
 * 
 */
public class DriverUtil {

	/**
	 * description:由一个jar文件中取出所有的驱动类
	 * 
	 * @param file
	 *            *.jar，绝对路径
	 * @return 返回所有驱动类
	 * @throws MalformedURLException 
	 */
	private static URLClassLoader classloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
	private static Method addURL = initAddMethod();
	 private static Method initAddMethod() {
	        try {
	            Method add = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });
	            add.setAccessible(true);
	            return add;
	        }
	        catch (Exception e) {
	            throw new RuntimeException(e);
	        }
	    }
	/**
	 * description:判断是否是数据驱动
	 * 
	 * @param clazz
	 * @return
	 */
	public static boolean isDriver(Class<?> clazz) {
		Class<?>[] ccc = clazz.getInterfaces();
		boolean flag = false;
		for (Class<?> aa : ccc) {
			if (aa.getName().equals("java.sql.Driver")) {
				flag = true;
			}
		}
		if (!flag) {
			for (; clazz != null; clazz = clazz.getSuperclass()) {
				Class<?>[] interfaces = clazz.getInterfaces();
				for (Class<?> aa : interfaces) {
					if (aa.getName().equals("java.sql.Driver")) {
						flag = true;
					}
				}
			}
		}
		return flag;
	}


	/**
	 * description:
	 * 
	 * @param file
	 *            一个jar文件(绝对路径)
	 * @param clazz
	 *            需要加载的class路径com.mysql.jdbc.MysqlDriver
	 * @return
	 * @throws MalformedURLException 
	 * @throws ClassNotFoundException 
	 */
	public static void getDynamic(String file){
		addURL(new File(file));
	}

	 private static void addURL(File file) {
	        try {
	            addURL.invoke(classloader, new Object[] { file.toURI().toURL() });
	        }
	        catch (Exception e) {
	        }
	    }
	public static void main(String[] args) throws ClassNotFoundException {
		
//		System.out.println("");
//		Class<?> dynamic = DriverUtil.getDynamic("E:/Bank_products/NC/workspace/sqlTool/sqltool/src/lib/ojdbc6.jar","oracle.jdbc.driver.OracleDriver");
//		Set<String> drivers = getDrivers("E:\\Bank_products\\NC\\workspace\\new\\sqltool\\src\\lib\\ojdbc6.jar");
//		Class.forName("oracle.jdbc.driver.OracleDriver");
//		System.out.println(drivers);
//		System.out.println(dynamic);
//		try {
//			Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@banktax2011:1521/bankdb","tjyh1212","1");
//			Statement createStatement = connection.createStatement();
//			boolean execute = createStatement.execute("select * from rm_port");
//			System.out.println(execute);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

}
