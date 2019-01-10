package com.view.system;

import org.apache.log4j.Logger;



public  class ToolLogger {
	final static Logger logger = Logger.getLogger("sql");
//	static {
//		  Properties props = new Properties();
//		  String filePath = System.getProperty("user.dir");
//	      FileInputStream istream ;
//		try {
//			File file = new File(filePath+"\\log");
//			file.isDirectory();
//			istream = new FileInputStream(file);
//			props.load(istream);
//			istream.close();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//          String logFile = props.getProperty("log4j.appender.file.File");//设置路径
//          props.setProperty("log4j.appender.file.File",logFile);
//          PropertyConfigurator.configure(props);
//	}
	public static void error(String message){
		logger.error(message);
	}
	public static void info(String message){
		logger.info(message);
	}
	public static void warn(String message){
		logger.warn(message);
	}
	public static void main(String[] args) {
		  String filePath = System.getProperty("user.dir");

		new ToolLogger();
		
	}
}
