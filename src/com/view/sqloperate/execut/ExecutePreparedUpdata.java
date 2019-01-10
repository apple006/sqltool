package com.view.sqloperate.execut;

import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import com.dao.entity.Table;
import com.dao.imp.Page;
import com.entity.LoginInfo;
/**
 * 执行修改；直接对查询出结果集进行修改
 * @author whh
 *
 */
public class ExecutePreparedUpdata extends Execute {

	private String sqls[];
	private Table updateData;
	public ExecutePreparedUpdata(LoginInfo loginInfo, String []sqls, Table updateData) {
		super(loginInfo);
		this.sqls = sqls;
		this.updateData = updateData;
	}

	@Override
	public void run() {
		Connection connection=null;
		try {
			connection = getConnection();
			rows = dao.executePreparedUpdata(connection, sqls,this.updateData);
			synchronized (this) {
				this.notify();
			}
		} catch (Exception e) {
			isSuccess = false;
			error.append(":     MESSAGE :"+e.getMessage());
			System.out.print(error.toString());
			e.printStackTrace();
		}
		finally{
			synchronized (this) {
				releasConnection(connection);
				this.notify();
			}
		}
		
	}
	
	@Override
	public Table getTable() {
		return this.updateData;
	}

}
