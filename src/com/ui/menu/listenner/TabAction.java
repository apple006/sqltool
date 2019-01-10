package com.ui.menu.listenner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import main.SQLTool;

import com.UIFactory;
import com.file.SaveFile;
import com.prompt.DBKeyTextPane;
import com.ui.extensible.listener.TabActionListener;
import com.view.sqloperate.QuerySqlTab;

public class TabAction implements TabActionListener {
	private QuerySqlTab querySqlTab;
	public  TabAction(QuerySqlTab tab) {
		this.querySqlTab = tab;
	}

	@Override
	public void open(DBKeyTextPane editSql) {

	}

	

	@Override
	public boolean colse(DBKeyTextPane editSql) {
		editSql.getSaveFile();
		String path = querySqlTab.getFile();
		int createConfirmDialog = JOptionPane.OK_OPTION;
		if ( "".equals(path)||querySqlTab.isChange()) {
			createConfirmDialog = UIFactory.getInstance().createConfirmDialog("文件尚未保存，是否进行保存！");
		}
		if (JOptionPane.NO_OPTION == createConfirmDialog) {
			return true;
		}
		if (JOptionPane.OK_OPTION == createConfirmDialog) {
			if (!"".equals(path)) {
				if(querySqlTab.isChange()){
					SaveFile saveFile = new SaveFile(path, querySqlTab.getText());
					saveFile.save();
					querySqlTab.setChange();
				}
				return true;
			}
			JFileChooser fileChooser = UIFactory.getInstance().createFileChooser(JFileChooser.FILES_ONLY, "sql");
			JFrame frame = SQLTool.getSQLTool().getToolFrame().getFrame();
			int showSaveDialog = fileChooser.showSaveDialog(frame);
			if (JFileChooser.APPROVE_OPTION == showSaveDialog) {
				if ("".equals(path)) {
					path = fileChooser.getSelectedFile().getPath();
					if (!path.endsWith(".sql")) {
						path = fileChooser.getSelectedFile().getPath()
								+ ".sql";
					}
				}
				querySqlTab.setFile(path);
				SaveFile saveFile = new SaveFile(path, querySqlTab.getText());
				saveFile.save();
				querySqlTab.setChange();
				return true;
			}
		}
		return false;
	}
}

