package main;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuItem;

import com.ui.MenuListenner;
import com.view.explan.ExPlanView;
import com.view.movedata.exp.ExpDataView;
import com.view.movedata.exp.MoveDataView;

public class MouseOnClick extends MenuListenner{

	@Override
	public void MouseOnClick(MouseEvent e) {
		String name = ((JMenuItem) e.getSource()).getText();
		if("导入导出数据".equals(name)){
			JFrame log = new JFrame();
			log.setSize(920,600);
			ExpDataView expDataView = new ExpDataView();
			log.add(expDataView);
			log.setLocationRelativeTo(null);
			log.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
			log.show();
		}
		
		if("数据迁移".equals(name)){
			JFrame log = new JFrame("数据迁移");
			log.setSize(1200,700);
			MoveDataView expDataView = new MoveDataView();
			log.add(expDataView);
			log.setLocationRelativeTo(null);
			log.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
			log.show();
		}
		
		
	
		if("执行计划".equals(name)){
			JFrame log = new JFrame();
			log.setSize(920,600);
			ExPlanView expDataView = new ExPlanView();
			log.add(expDataView);
			log.setLocationRelativeTo(null);
			log.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
			log.show();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("工具栏");
	}

}
