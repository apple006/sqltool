package com.ui.extensible.listener;


import com.prompt.DBKeyTextPane;

public interface TabActionListener {
	boolean colse(DBKeyTextPane editSql);
	void open(DBKeyTextPane editSql);
}
