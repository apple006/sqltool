package main;

import java.awt.Color;
import java.awt.Component;
import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.ThreadMXBean;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;

import com.entity.LoginInfo;
import com.prompt.PromptLabel;
import com.ui.tree.UITree;

public class SQLTool {
	private static Object o = new Object();
	private final static SQLTool sqlTool = new SQLTool();
	private ToolFrame frame;

	private Logger logger = Logger.getLogger(getClass());

	
	private SQLTool() {
		try {
			frame = new ToolFrame();
		} catch (Exception e) {
			logger.error("启动失败",e);
		}
		final ClassLoadingMXBean classLoadingMXBean = ManagementFactory.getClassLoadingMXBean();

		Thread th = new Thread() {
			ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean() ;
			MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
			public void run() {
				while (true) {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					long[] allThreadIds = threadMXBean.getAllThreadIds();
//					System.out.println("线程数："+allThreadIds.length);
					MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
					frame.setNc(heapMemoryUsage.getUsed()/1024/1024+"M / "+heapMemoryUsage.getMax()/1024/1024+"M");
//					long totalLoadedClassCount = classLoadingMXBean.getTotalLoadedClassCount();
//					System.out.println("到此时为止以加载class类的总数："+ totalLoadedClassCount);
//					System.out.println("内存heap   "+heapMemoryUsage.getMax()/1024/1034+"M 使用  "+heapMemoryUsage.getUsed()/1024/1024+"M");
				}
			};
		};
		th.start();
	}

	public static void main(String[] args) {

	}

	public ToolFrame getToolFrame() {
		return frame;
	}

	public static SQLTool getSQLTool() {
		return sqlTool;
	}
	
	public void referDrowDown(JComboBox connectionList) {
		connectionList.setRenderer(new LoginCell());
		LoginInfo user = null;
		PromptLabel promptLabel = (PromptLabel) connectionList.getSelectedItem();
		if(promptLabel!=null){
			 user = (LoginInfo) promptLabel.getUser();
		}
		connectionList.removeAllItems();
		UITree dataBaseTree = SQLTool.getSQLTool().getToolFrame().getSqlView()
				.getDataBaseTree();
		DefaultTreeModel model = (DefaultTreeModel) dataBaseTree.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
		int childCount = root.getChildCount();
		LoginInfo[] please = new LoginInfo[childCount];
		LoginInfo login = new LoginInfo();
		login.setName("---请选择---");
		connectionList.addItem(new PromptLabel(login,"",-1));

	
		LoginInfo conn;
		for (int i = 0; i < childCount; i++) {
			DefaultMutableTreeNode childAt = (DefaultMutableTreeNode) root
					.getChildAt(i);
			please[i] = (LoginInfo) childAt.getUserObject();
			
		}
		Arrays.sort(please, new Comparator<LoginInfo>(){

			@Override
			public int compare(LoginInfo o1, LoginInfo o2) {
				int i = o1.getIsLive()>o2.getIsLive()==true?1:-1;
				return i;
			}
			
		});
		int selectIndex=1;
		for (int i = 0; i < childCount; i++) {
			PromptLabel promptLabel2 = new PromptLabel(please[i],"",please[i].getIsLive());
			connectionList.addItem(promptLabel2);
			if(user!=null&&please[i].equals(user)){
				selectIndex =selectIndex+ i;
			}
		}
		for (int i = 0; i < connectionList.getItemCount(); i++) {
			PromptLabel itemAt = (PromptLabel) connectionList.getItemAt(i);
			Object user2 = itemAt.getUser();
			if(user2.equals(user)){
				connectionList.setSelectedIndex(selectIndex);
			}
		}
		connectionList.repaint();
	}
	
}


class LoginCell extends DefaultListCellRenderer {

	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		 setComponentOrientation(list.getComponentOrientation());

	        Color bg = null;
	        Color fg = null;

	        JList.DropLocation dropLocation = list.getDropLocation();
	        if (dropLocation != null
	                && !dropLocation.isInsert()
	                && dropLocation.getIndex() == index) {
	            bg = UIManager.getColor("List.dropCellBackground");
	            fg = UIManager.getColor("List.dropCellForeground");

	            isSelected = true;
	        }

		if (isSelected) {
	            setBackground(bg == null ? list.getSelectionBackground() : bg);
		    setForeground(fg == null ? list.getSelectionForeground() : fg);
		}
		else {
		    setBackground(list.getBackground());
		    setForeground(list.getForeground());
		}
		if(value instanceof PromptLabel){
			PromptLabel p = (PromptLabel) value;
			if(p.getIcon()!=null){
				setIcon(p.getIcon());
			}else{
				setIcon(null);
			}
			setText(p.getUser().toString());
			return this;
		}
		return this;
	}
	
}
