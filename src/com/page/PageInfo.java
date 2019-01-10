package com.page;

public interface PageInfo {
	/**
	 * 返回分页大小
	 * @return
	 */
	int getPageSize();

	String formatPageSql(String sql);
}
