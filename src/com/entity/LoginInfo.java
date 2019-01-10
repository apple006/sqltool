package com.entity;

import java.io.Serializable;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Date;

import javax.swing.JTextField;

import com.dao.ConnectionPool;


/**
 * 连接数据库信息
 * @author Administrator
 *
 */
public class LoginInfo implements Serializable{
	private String name ="new Connection";
	private String dataType = "";
	private String url ="";
	private String userName="";
	private String password="";
	private String driverClass="";
	private String message="";
	
	private String ip ="";
	private String port ="";
	private String example ="";
	/**
	 * 更新次连接的时间
	 * @return
	 */
	private String update;
	private String pdm="";
	private int  isLive = ConnectionPool.IS_DIE;
	
	
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getExample() {
		return example;
	}
	public void setExample(String example) {
		this.example = example;
	}
	/**
	 * 创建次连接的时间
	 */
	private String date="";
	
	public void setDate(String date){
		this.date = date;
	}
	public void setUpDate(String update){
		this.update = update;
	}
	
	public int getIsLive() {
		return isLive;
	}
	public void setIsLive(int i){
		this.isLive = i;
	}
	public LoginInfo(){
		Date d = new Date();
		date = d.toLocaleString();
		update = d.toLocaleString();
	}
	public String getDate() {
		return date;
	}
	public String getUpdate() {
		return update;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		if(name==null){
			this.name="";
		}else{
			this.name = name;
		}
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		
		this.url = url;
	}
	public String getUserName() {
		if("Oracle".equals(dataType)){
			userName = userName.toUpperCase();
		}
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String toString() {
		return  name;
	}

	
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public void setUpdate(String update) {
		this.update = update;
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		LoginInfo clon = new LoginInfo();
		clon.setDataType(this.getDataType());
		clon.setDriverClass(this.getDriverClass());
		clon.setName(this.getName());
		clon.setPassword(this.getPassword());
		clon.setUrl(this.getUrl());
		clon.setUserName(this.getUserName());
		clon.setDate(this.getDate());
		clon.setUpDate(this.getUpdate());
		clon.setUserName(this.getUserName());
		return clon;
	}
	@Override
	public boolean equals(Object obj) {
		LoginInfo o;
		if(obj instanceof LoginInfo){
			o = (LoginInfo)obj;
		}
		else{
			return false;
		}
		if (this.date.equals(o.getDate())){
			return true;
		}
		return false;
	}
	public String getDriverClass() {
		return driverClass;
	}
	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
		
	}
//	public Connection connection() throws MalformedURLException, ClassNotFoundException, SQLException {
//		System.out.println("加载驱动...");
//		DriverPathInfo driverPathInfo = XMLJdbcConfig.getXMLJdbcConfig().getDriverPaths().get(driverClass);
//		DriverUtil.getDynamic(driverPathInfo.getFileUrl(),driverPathInfo.getDriverClass());
//		Connection conn = DriverManager.getConnection(url, username, password);
//		System.out.println("连接成功...");
//		return conn;
//	}
	/**
	 * 得到创建连接时间作为线程池唯一名称
	 */
	public String getPoolName(){
		return date;
	}
	public static void main(String[] args) {
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.103:1521/ORCL", "text", "text");
		
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	public Comparator getComparator(){
//		return new Comparator<LoginInfo>(){
//			@Override
//			public int compare(LoginInfo arg0, LoginInfo arg1) {
//				return arg0.getIsLive()>arg1.getIsLive()==true?1:-1;
//			};
//	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getMessage() {
		return this.message;
	}
	public void setPdm(String pdm) {
		this.pdm = pdm;
	}
	public String getPdm() {
		return this.pdm;
	}
}
