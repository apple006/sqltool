package com;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.MenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.WindowListener;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.AbstractTableModel;

import main.SQLTool;
   
 
public class UIFactory {  
 
    private static UIFactory instance;//当前工厂的一个实例  
    private  JFileChooser fileChooser = new JFileChooser("D:\\");

    //私有的构造器  
    private UIFactory(){  
          
    }  
    
  /**
   * 
   * @param frame
   * @param message
   * @return JOptionPane.YES_OPTION,JOptionPane.NO_OPTION
   */
    public  int createConfirmDialog(String message){
    	JFrame frame = SQLTool.getSQLTool().getToolFrame().getFrame();
    	return JOptionPane.showConfirmDialog(frame, message);
    }
    
    public  JFileChooser createFileChooser(int type,String extension){
		fileChooser.setFileSelectionMode(type );
		FileNameExtensionFilter filter = new FileNameExtensionFilter(extension,extension);//建立过滤器    
		fileChooser.setFileFilter(filter);//开始过滤    
		return fileChooser;
    }
    //获得当前工厂的实例  
    public static UIFactory getInstance(){  
        if(instance==null){  
            synchronized("key1"){  
                instance=new UIFactory();  
            }  
        }  
        return instance;  
    }  
       
    //产生一个标签  
    public JLabel createJLabel(String text){  
        JLabel l=new JLabel(text);  
        return l;  
    }  
    //产生一个带图标的标签  
    public JLabel createJLabel(String text,Icon icon){  
        JLabel l=new JLabel(text);  
        l.setIcon(icon);  
        return l;  
    }  
      
      
    //专门产生按钮，而且添加快捷键      文本显示  命令  按键   辅助键  触发范围  监听器  是否显焦  
    public JButton createJButton(String text,int mn,String command,ImageIcon icon,int key_keyEvent,int input_inputEvent,int when_invoke_jComponent,ActionListener al,boolean isFocus){  
        JButton button=new JButton(text,icon);  
        //这里默认会是alt+'mn'  
        button.setMnemonic(mn);//设置辅助键  
        button.setActionCommand(command);//设置动作  
        button.addActionListener(al);  
        KeyStroke stroke=KeyStroke.getKeyStroke(key_keyEvent,input_inputEvent,isFocus);  
        button.registerKeyboardAction(al,command,stroke,when_invoke_jComponent);  
        button.setFocusable(isFocus);  
        return button;  
    }  
    //专门产生按钮                     文本显示  命令  监听器  是否显焦  
    public JButton createJButton(String text,String command,ImageIcon icon,ActionListener al,boolean isFocus){  
        JButton button=new JButton(text,icon);  
        button.setActionCommand(command);//设置动作  
        button.addActionListener(al);//添加事件监听  
        button.setFocusable(isFocus);  
        return button;  
    }  
    //专门产生按钮，而且添加快捷键 文本显示  命令  按键   辅助键  触发范围  监听器  是否显焦  
    public JButton createJButton(String text,int mn,String command,ImageIcon icon,int width,int height,int key_keyEvent,int input_inputEvent,int when_invoke_jComponent,ActionListener al,boolean isFocus){  
 
        JButton button=new JButton(text,icon);//指定按钮的  文本和图片  
        //这里默认会是alt+'mn'  a    
        button.setMnemonic(mn);//设置辅助键  
        button.setActionCommand(command);//设置动作  【添加】  
 
 
        button.setPreferredSize(new Dimension(width,height));//指定按钮的大小  
        button.addActionListener(al);  
        KeyStroke stroke=KeyStroke.getKeyStroke(key_keyEvent,input_inputEvent,isFocus);  
        button.registerKeyboardAction(al,command,stroke,when_invoke_jComponent);  
        button.setFocusable(isFocus);  
        return button;  
    }  
    //专门产生按钮                     文本显示  命令  监听器  是否显焦  
    public JButton createJButton(String text,String command,ImageIcon icon,int width,int height,ActionListener al,boolean isFocus){  
        JButton button=new JButton(text,icon);  
        button.setActionCommand(command);//设置动作  
        button.setPreferredSize(new Dimension(width,height));  
        button.addActionListener(al);//添加事件监听  
        button.setFocusable(isFocus);  
        return button;  
    }  
    //生成文本框   默认文本  文本框大小长 是否可编辑  
    public JTextField createJTextField(String text,int cols,boolean isEditable){  
        JTextField field=new JTextField(text,cols);  
        field.setEditable(isEditable);  
        return field;  
    }  
    //生成文本框   默认文本  文本框大小长 是否可编辑  设置字体  
    public JTextField createJTextField(String text,int cols,boolean isEditable,Font f){  
        JTextField field=new JTextField(text,cols);  
        field.setEditable(isEditable);  
        field.setFont(f);  
        return field;  
    }  
    //生成文本域    默认文本  列数  行数  是否可编辑  
    public JTextArea createJTextArea(String text,int cols,int rows,boolean isEditable,boolean lineWrap,boolean wrapStyleWord){  
        JTextArea area=new JTextArea(text,cols,rows);  
        area.setEditable(isEditable);  
        area.setLineWrap(lineWrap);  
        area.setWrapStyleWord(wrapStyleWord);  
        return area;  
    }  
    //生成文本域    默认文本  列数  行数  是否可编辑  设置字体   
    public JTextArea createJTextArea(String text,int cols,int rows,boolean isEditable,boolean lineWrap,boolean wrapStyleWord,Font f){  
        JTextArea area=new JTextArea(text,cols,rows);  
        area.setEditable(isEditable);  
        area.setFont(f);  
        area.setLineWrap(lineWrap);  
        area.setWrapStyleWord(wrapStyleWord);  
        return area;  
    }  
    //生成文本域    默认文本  列数  行数  是否可编辑  设置字体  背景颜色  前景颜色  
    public JTextArea createJTextArea(String text,int cols,int rows,boolean isEditable,Font f,boolean lineWrap,boolean wrapStyleWord,Color background, Color foreground){  
        JTextArea area=new JTextArea(text,cols,rows);  
        area.setEditable(isEditable);  
        area.setFont(f);  
        area.setLineWrap(lineWrap);  
        area.setWrapStyleWord(wrapStyleWord);  
        area.setBackground(background);  
        area.setForeground(foreground);  
        return area;  
    }  
    //产生一个滚动面板  
    public JScrollPane createJScrollPane(Container c){  
        JScrollPane sc=new JScrollPane(c);  
        return sc;  
    }  
    public static void main(String args[]){  
          
        UIFactory factory = UIFactory.getInstance();  
 
        //JScrollPane pan = factory.createJScrollPane(new JFrame(),JScrollPane.horizontalScrollBar,JScrollPane.verticalScrollBarPolicy);  
          
    }  
    //产生一个指定垂直和水平滚动策略的滚动面板  水平滚动策略  垂直滚动策略  
    public JScrollPane createJScrollPane(Container c,int horizontal_policy,int vertical_policy){  
        JScrollPane sc=new JScrollPane();  
        sc.setHorizontalScrollBarPolicy(horizontal_policy);  
        sc.setVerticalScrollBarPolicy(vertical_policy);  
        return sc;  
    }  
    //设置框架属性                    传入的框架  关闭策略  x,y,width,height   
    public void setJFrameAttributes(JFrame f,String title,int close_policy,int x,int y,int width,int height,boolean isResizable){  
        f.setTitle(title);  
        f.setDefaultCloseOperation(close_policy);  
        f.setLocation(x,y);  
        f.setSize(width,height);  
        f.setResizable(isResizable);  
    }  
      
    //设置框架属性                    传入的框架  关闭策略  x,y,width,height   
    public void setJFrameAttributes(JFrame f,String title,int close_policy,int x,int y,int width,int height,boolean isResizable,WindowListener wl){  
        f.setTitle(title);  
        f.setDefaultCloseOperation(close_policy);  
        f.setLocation(x,y);  
        f.setSize(width,height);  
        f.setResizable(isResizable);  
        f.addWindowListener(wl);  
    }  
    //产生一条列表组件   条目宽度  
    public JList createJList(int cell_width){  
        JList list=new JList();  
        list.setFixedCellWidth(cell_width);  
        return list;  
    }  
    //产生一条列表组件   条目宽度  列表选择监听器  
    public JList createJList(int cell_width,ListSelectionListener lsl){  
        JList list=new JList();  
        list.setFixedCellWidth(cell_width);  
        list.addListSelectionListener(lsl);  
        return list;  
    }  
    //产生一条列表组件   条目宽度  条目间距  源数据  
    public JList createJList(int cell_width,int cell_height,Vector<Object> v,ListSelectionListener lsl){  
        JList list=new JList(v);  
        list.setFixedCellWidth(cell_width);  
        list.setFixedCellHeight(cell_height);  
        list.addListSelectionListener(lsl);  
        return list;  
    }  
    //产生一条列表组件   条目宽度  条目间距  源数据  
    public JList createJList(int cell_width,int cell_height,Object[] data,ListSelectionListener lsl){  
        JList list=new JList(data);  
        list.setFixedCellWidth(cell_width);  
        list.setFixedCellHeight(cell_height);  
        list.addListSelectionListener(lsl);  
        return list;  
    }  
    //获得一个具有BorderLayout布局的面板  
    public JPanel createBorderJPanel(){  
        JPanel p=new JPanel();  
        p.setLayout(new BorderLayout());  
        return p;  
    }  
    //获得一个具有BorderLayout布局的面板  
    public JPanel createBorderJPanel(int row_distance,int col_distance){  
        JPanel p=new JPanel();  
        p.setLayout(new BorderLayout(row_distance,col_distance));  
        return p;  
    }  
    //获得一个具有GridLayout布局的面板  指定行数，列数，行距，列距  
    public JPanel createGridJPanel(int rows,int cols,int col_distance,int row_distance){  
        JPanel p=new JPanel();  
        p.setLayout(new GridLayout(rows,cols,col_distance,row_distance));  
        return p;  
    }  
    //获得一个具有GridLayout布局的面板  
    public JPanel createGridJPanel(int rows,int cols){  
        JPanel p=new JPanel();  
        p.setLayout(new GridLayout(rows,cols));  
        return p;  
    }  
    //获得一个具有卡片布局的面板  
    public JPanel createCardJPanel(CardLayout card){  
        JPanel p=new JPanel();  
        p.setLayout(card);  
        return p;  
    }  
    //获得一个具有绝对布局的的面板  
    public JPanel createAbsoluteJPanel(){  
        JPanel p=new JPanel();  
        p.setLayout(null);  
        return p;  
    }  
    //获得一个具有FlowLayout流式布局的面板  
    public JPanel createFlowJPanel(){  
        JPanel p=new JPanel();  
        p.setLayout(new FlowLayout());  
        return p;  
    }  
    //获得一个具有FlowLayout流式布局的面板  
    public JPanel createFlowJPanel(int policy,int col_distance,int row_distance){  
        JPanel p=new JPanel();  
        p.setLayout(new FlowLayout( policy, col_distance, row_distance));  
        return p;  
    }  
    //获得一个具有FlowLayout流式布局的面板  指定左右对齐方式  
    public JPanel createFlowJPanel(int leftOrRight_policy){  
        JPanel p=new JPanel();  
        p.setLayout(new FlowLayout(leftOrRight_policy));  
        return p;  
    }  
    //获得一个带有监听器的菜单选项  
    public JMenuItem createJMenuItem(String text,String command,ActionListener al){  
        JMenuItem item=new JMenuItem(text);  
        item.addActionListener(al);  
        item.setActionCommand(command);  
        return item;  
    }  
    //获得一个JRadioButton按钮  
    public JRadioButton createJRadioButton(){  
        JRadioButton jrb=new JRadioButton();  
        return jrb;  
    }  
    //获得一个带ItemListener监听器的JRadioButton按钮  
    public JRadioButton createJRadioButton(ItemListener il){  
        JRadioButton jrb=new JRadioButton();  
        jrb.addItemListener(il);  
        return jrb;  
    }  
    //获得一个复选框  
    public JCheckBox createJCheckBox(){  
        JCheckBox box=new JCheckBox();  
        return box;  
    }  
    //获得一个下拉列表框  
    public JComboBox createJComboBox(String[] data){  
        JComboBox box=new JComboBox(data);  
        return box;  
    }  
    //获得一个下拉列表框  
    public JComboBox createJComboBox(Vector data){  
        JComboBox box=new JComboBox(data);  
        return box;  
    }  
    //获得一个下拉列表框  
    public JComboBox createJComboBox(String[] data,int width,int height){  
        JComboBox box=new JComboBox(data);  
        box.setPreferredSize(new Dimension(width,height));  
        return box;  
    }  
    //获得一个下拉列表框  
    public JComboBox createJComboBox(Vector data,int width,int height){  
        JComboBox box=new JComboBox(data);  
        box.setPreferredSize(new Dimension(width,height));  
        return box;  
    }  
    //获得一个带ItemListener的下拉列表框  
    public JComboBox createJComboBox(Vector data,ItemListener il){  
        JComboBox box=new JComboBox(data);  
        box.addItemListener(il);  
        return box;  
    }  
    //获得一个带ItemListener的下拉列表框  
    public JComboBox createJComboBox(String[] data,ItemListener il){  
        JComboBox box=new JComboBox(data);  
        box.addItemListener(il);  
        return box;  
    }  
    //获得一个带ItemListener的下拉列表框  
    public JComboBox createJComboBox(Vector data,int width,int height,ItemListener il){  
        JComboBox box=new JComboBox(data);  
        box.setMaximumSize(new Dimension(width,height));  
        box.setMinimumSize(new Dimension(width,height));  
        box.addItemListener(il);  
        return box;  
    }  
    //获得一个带ItemListener的下拉列表框  
    public JComboBox createJComboBox(String[] data,int width,int height,ItemListener il){  
        JComboBox box=new JComboBox(data);  
        box.setMaximumSize(new Dimension(width,height));  
        box.setMinimumSize(new Dimension(width,height));  
        box.addItemListener(il);  
        return box;  
    }  
    //产生一个选项卡面板  
    public JTabbedPane createJTabbedPane(){  
        JTabbedPane tab=new JTabbedPane();  
        return tab;  
    }  
    //产生一个指定位置的选项卡面板  
    public JTabbedPane createJTabbedPane(int pos){  
        JTabbedPane tab=new JTabbedPane(pos);  
        return tab;  
    }  
    //产生一个带ChangeListener的选项卡面板  
    public JTabbedPane createJTabbedPane(ChangeListener cl){  
        JTabbedPane tab=new JTabbedPane();  
        tab.addChangeListener(cl);  
        return tab;  
    }  
    //产生一个是否可以浮动的工具栏  
    public JToolBar createJToolBar(boolean isFloatable){  
        JToolBar bar =new JToolBar();  
        bar.setFloatable(isFloatable);  
        return bar;  
    }  
    //产生一个JTextPane面板  
    public JTextPane createJTextPane(boolean isEditable){  
        JTextPane p=new JTextPane();  
        p.setEditable(isEditable);  
        return p;  
    }  
    //产生一个JTextPane面板  
    public JTextPane createJTextPane(boolean isEditable,Color background,Color foreground){  
        JTextPane p=new JTextPane();  
        p.setEditable(isEditable);  
        p.setBackground(background);  
        p.setForeground(foreground);  
        return p;  
    }  
    //产生一个进度条  
    public JProgressBar createJProgressBar(int min,int max,int value,boolean stringPainted,boolean borderPainted){  
        JProgressBar bar=new JProgressBar();  
        bar.setMinimum(min);  
        bar.setMaximum(max);  
        bar.setBorderPainted(borderPainted);  
        bar.setStringPainted(stringPainted);  
        bar.setValue(value);  
        return bar;  
    }  
    //产生一个指定大小的进度条  
    public JProgressBar createJProgressBar(int min,int max,int value,boolean stringPainted,boolean borderPainted,Dimension d){  
        JProgressBar bar=new JProgressBar();  
        bar.setMinimum(min);  
        bar.setMaximum(max);  
        bar.setBorderPainted(borderPainted);  
        bar.setStringPainted(stringPainted);  
        bar.setValue(value);  
        bar.setPreferredSize(d);  
        return bar;  
    }  
    //产生一个分隔面板                       分隔面板1  分隔面板2   水平或垂直分割  分割条位置   分割条宽度  是否可收起  
    public JSplitPane createJSplitPane(Container c1,Container c2,int horizontalOrVertical,int dividerLoaction,int dividerSize,boolean expandable){  
        JSplitPane splitPane=new JSplitPane(horizontalOrVertical,  
                true,c1,c2);  
        splitPane.setDividerLocation(dividerLoaction);  
        splitPane.setOneTouchExpandable(expandable);  
        splitPane.setDividerSize(dividerSize);  
        return splitPane;  
    }  
    //产生一个JToggleButton  
    public JToggleButton createJToggleButton(String text,String command,Icon icon,int width,int height,ActionListener al, boolean selected){  
        JToggleButton button=new JToggleButton(text,icon,selected);  
        button.setActionCommand(command);  
        button.addActionListener(al);  
        button.setPreferredSize(new Dimension(width,height));  
        return button;  
    }  
    //产生一个JToggleButton  
    public JToggleButton createJToggleButton(String text,String command,Icon icon,int width,int height,ActionListener al){  
        JToggleButton button=new JToggleButton(text,icon);  
        button.setActionCommand(command);  
        button.addActionListener(al);  
        button.setPreferredSize(new Dimension(width,height));  
        return button;  
    }  
    //产生将组件结合起来的面板  
    public JPanel createJPanelWithComponents(JLabel label,Container field,int policy){  
        JPanel p=this.createFlowJPanel(policy);  
        p.add(label);  
        p.add(field);  
        return p;  
    }  
    //产生将两个面板整合起来的面板  
    public JPanel createJPanelWithComponents(Container c1,Container c2,int policy){  
        JPanel p=this.createFlowJPanel(policy,0,0);  
        p.add(c1);  
        p.add(c2);  
        return p;  
    }  
    //产生将三个面板整合起来的面板  
    public JPanel createJPanelWithComponents(Container c1,Container c2,Container c3,int policy){  
        JPanel p=this.createFlowJPanel(policy,0,0);  
        p.add(c1);  
        p.add(c2);  
        p.add(c3);  
        return p;  
    }  
    //产生将四个面板整合起来的面板  
    public JPanel createJPanelWithComponents(Container c1,Container c2,Container c3,Container c4,int policy){  
        JPanel p=this.createFlowJPanel(policy,0,0);  
        p.add(c1);  
        p.add(c2);  
        p.add(c3);  
        p.add(c4);  
        return p;  
    }  
    //产生一个普通表格  
    public JTable createJTable(){  
        JTable table=new JTable();  
        return table;  
    }  
    //产生一个带AbstractTableModel的表格  
    public JTable createJTable(AbstractTableModel model){  
        JTable table=new JTable(model);  
        return table;  
    }  
    //产生一个密码框  
    //生成文本框   默认文本  文本框大小长 是否可编辑  
    public JPasswordField createJPasswordField(String text,int cols,boolean isEditable){  
        JPasswordField field=new JPasswordField(text,cols);  
        field.setEditable(isEditable);  
        return field;  
    }  
       
    //产生一个带监听器的菜单项  
    public MenuItem createMenuItem(String text,ActionListener al){  
        MenuItem mi=new MenuItem(text);  
        mi.addActionListener(al);  
        return mi;  
    }  
    //产生一个带监听器的菜单项  
    public MenuItem createMenuItem(String text,String command,ActionListener al){  
        MenuItem mi=new MenuItem(text);  
        mi.setActionCommand(command);  
        mi.addActionListener(al);  
        return mi;  
    }  
}  