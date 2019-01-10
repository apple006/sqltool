package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.ico.LazyImageIcon;
import com.ui.menu.MenuAndTool;
import com.ui.menu.xml.XMLMenuAndToolFactory;
import com.view.sqloperate.SqlView;
import com.view.tool.HelpProperties;

public class ToolFrame {
//	final static ToolFrame toolFrame ;
	private Object o = new Object();
	private static JFrame frame;
	private final int WINDOW_WIDE=600;
	private final int WINDOW_HEIGHT=400;
	private final String TOOL_NAME="多功能工具";
	private JLabel nc;

	private SqlView sqlView;
	private MenuAndTool menu_tool =  XMLMenuAndToolFactory.getXMLMenuAndToolFactory();

	/**
	 * 
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws UnsupportedLookAndFeelException
	 */
	public  ToolFrame() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException{
		UIManager.setLookAndFeel("com.jtattoo.plaf.aero.AeroLookAndFeel");
//		UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
//		String lookAndFeel =UIManager.getSystemLookAndFeelClassName();
//		UIManager.setLookAndFeel(lookAndFeel);
//		 try
//		    {
//		        org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
//		    }
//		    catch(Exception e)
//		    {
//		        //TODO exception
//		    }
		init();
		frame.setIconImage(((ImageIcon)new LazyImageIcon("\\imgs\\ico.png").getIcon()).getImage());
	}
	/**
	 * 窗口布局初始化
	 */
	private void init(){
		if(frame==null){
			synchronized (o) {
				frame = new JFrame();
			}
		}
		
		
		//初始界面基本功能如（菜单，工具栏）
		JToolBar bar = menu_tool.getToolBar("文件操作", "toolBarFile");
		JMenuBar menu = menu_tool.getMenu("menuBar");
	
		JPanel  bar1 = new JPanel();
		bar1.setBorder(BorderFactory.createLoweredBevelBorder()  );
		bar1.setLayout(new FlowLayout(FlowLayout.RIGHT));
		bar1.add(new JLabel("使用内存/总内存 :"));
		nc = new JLabel(" 0/0 ");
		nc.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO 自动生成的方法存根
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO 自动生成的方法存根
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO 自动生成的方法存根
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO 自动生成的方法存根
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				System.gc();
			}
		});
		bar1.add(nc);
		frame.add(bar, BorderLayout.NORTH);
		frame.setJMenuBar(menu);
	
		frame.add(bar1, BorderLayout.SOUTH);

		sqlView  = new SqlView();
		frame.add(sqlView);
		BaseInstall();
//		JOptionPane.showConfirmDialog(null, "是否打印？","提示:", JOptionPane.YES_NO_OPTION);
//		JOptionPane a = new JOptionPane("a");
//		a.setVisible(true);
	}
	/**
	 * 设置基础窗口设置（宽度、窗口标题）
	 */
	private void BaseInstall() {
		
		frame.setMinimumSize(new Dimension(WINDOW_WIDE, WINDOW_HEIGHT));
		frame.setExtendedState(JFrame.MAXIMIZED_HORIZ);
		frame.setTitle(TOOL_NAME);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		String current_fontName = HelpProperties.GetValueByKey("keyvalue.properties","current_fontName" );
    	String current_fontSize = HelpProperties.GetValueByKey("keyvalue.properties","current_fontSize" );
		Font font= new Font(current_fontName, 10, new Integer(current_fontSize));
		frame.setFont(font);
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int screenHeight = (int) screenSize.getHeight();
		int screenWidth = (int) screenSize.getWidth();
		
		//set frame width,height and location
		
//		frame.setMaximumSize(new Dimension(2048, 2048));
//		frame.setSize(screenWidth,screenHeight);
//		frame.setLocationByPlatform(true);
		
		
		frame.setVisible(true);
	}
	public JLabel getNc() {
		return nc;
	}
	public void setNc(String text) {
		this.nc.setText(text);;
	}
	public SqlView getSqlView() {
		return sqlView;
	}
	public  JFrame getFrame() {
		return frame;
	}
	
}
