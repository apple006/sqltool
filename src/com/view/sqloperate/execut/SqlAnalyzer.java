package com.view.sqloperate.execut;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * sql语句分析器
 * @author Administrator
 *
 */
public class SqlAnalyzer {

	/**
	 * 从文本中提取sql语句
	 * @return
	 */
	public String[]  converter(String str){
//		str.replaceAll(regex, replacement)
		int sum = 0;
		int row= 0;
		ArrayList<String> list= new ArrayList<String>();
		char[] charArray = str.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			if('\''==charArray[i]){
				++sum;
				
			}
			if(';'==charArray[i]){
				if(sum%2!=0){
					continue;
				}
				list.add(str.substring(row, i).trim());
				row=i+1;
			}
		}
		if(sum<str.length()){
			list.add(str.substring(row, str.length()).trim());
		}
		return list.toArray(new String[0]);
	}
	public boolean isSelectSql(String str){
		return str.startsWith("SELECT");
	}
	public boolean isUpdataOrInsertSql(String str){
		return str.startsWith("INSERT")||str.startsWith("UPDATA");
	}
	public String getTableName(String sql){
		Pattern per = Pattern.compile("FROM\\s+(\\w+);*",Pattern.CASE_INSENSITIVE);
		Matcher re = per.matcher(sql);
		while(re.find()){
			String group = re.group(1);
			return group;
		}
		return "";
	}
}
