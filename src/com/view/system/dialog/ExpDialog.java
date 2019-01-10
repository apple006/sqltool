package com.view.system.dialog;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.TransferHandler;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * 新建连接数据库对话框
 * 
 * @author Administrator
 * 
 */
public class ExpDialog extends JDialog {

	private  int width = 400;
	private  int height = 230;
	private final JFrame parent;
	private File file = null;
	private JButton exp;
	private JRadioButton b1 = new JRadioButton("导出SQL");
	private JRadioButton b2 = new JRadioButton("导出EXCEL");
	private JRadioButton b3 = new JRadioButton("导出到编辑窗口", true);
	private String tableName;
	public static int SQL = 0;
	public static int XLS = 1;
	public static int EDIT = 2;
	public ExpDialog(JFrame jFrame,String tableName,int width,int height) {
		super(jFrame, "导出数据", true);
		this.parent = jFrame;
		this.width = width;
		this.height = height;
		this.tableName = tableName;

		init();
	}

	public ExpDialog(String tableName) {
		this(null,tableName,400,300);
	}

	private static final long serialVersionUID = 1L;

	private void init() {
		setSize(width, height);
		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 4));
		JPanel panel = new JPanel(new GridLayout(1, 4));
		Border picBorder = BorderFactory.createTitledBorder("导出格式");
		panel.setBorder(picBorder);
		

		final ButtonGroup group = new ButtonGroup();
		group.add(b1);
		group.add(b2);
		group.add(b3);
		panel.add(b3);
		panel.add(b1);
		panel.add(b2);
		add(panel);

		JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 10));

		panel2.add(new JLabel("路径:"));
		final JTextField field = new JTextField(20);
		field.setEditable(false);
		panel2.add(field);
		final JButton expBut = new JButton("选择文件");
		expBut.setEnabled(false);
		panel2.add(expBut);
		exp = new JButton("导出");
		
		JButton cancel = new JButton("取消");
		
		b3.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (b3.isSelected()) {
					exp.setEnabled(true);
					expBut.setEnabled(false);
				} else {
					if(file==null){
						exp.setEnabled(false);
					}
					expBut.setEnabled(true);

				}
			}
		});
		
		add(panel2);
		
		JPanel but = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
		but.add(exp);
		but.add(cancel);
		add(but);
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ExpDialog.this.setVisible(false);
			}
		});

		JLabel label = new JLabel("Label Text");
		final String propertyName = "text";
		label.setTransferHandler(new TransferHandler(propertyName));

		// Listen for mouse clicks
		label.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				JComponent comp = (JComponent) evt.getSource();
				TransferHandler th = comp.getTransferHandler();
				th.exportAsDrag(comp, evt, TransferHandler.COPY);
			}
		});

		final JFileChooser fileChooser = new JFileChooser("D:\\");
	
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setApproveButtonText("确定");
		// fileChooser.addChoosableFileFilter(new );
		fileChooser.setSelectedFile(new File("保存.xml"));

		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		expBut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String sufix = "";
				if (b1.isSelected()) {
					sufix = "sql";
				}
				
				if (b2.isSelected()) {
					sufix = "xls";
				}
				// if(b3.isSelected()){
				// sufix = ".edit";
				// }
				
				fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("."+sufix,
						sufix));
				fileChooser.setSelectedFile(new File(tableName.toUpperCase()+"." + sufix));
				int showSaveDialog = fileChooser.showSaveDialog(null);
				if (JFileChooser.APPROVE_OPTION == showSaveDialog) {
					file = null;
					field.setText(fileChooser.getSelectedFile().getPath());
					file = fileChooser.getSelectedFile();
					exp.setEnabled(true);
				}
			}
		});
	}
	
	public void addActionListener(ActionListener a) {
		exp.addActionListener(a);
	}


	public int getExpType(){
		if (b1.isSelected()) {
			return SQL;
		}
		if (b2.isSelected()) {
			return XLS;
		}
		return EDIT;
	}

	public File getFile() {
		return file;
	}
	
}
