package com.view.sqloperate;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.Date;

import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.TextAction;
import javax.swing.text.Utilities;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.UndoManager;

import org.apache.log4j.Logger;

import com.dao.entity.Table;
import com.dnd.FileToTabDnd;
import com.entity.LoginInfo;
import com.ico.LazyImageIcon;
import com.prompt.DBKeyTextPane;
import com.prompt.Intellisense;
import com.prompt.PromptLabel;
import com.ui.extensible.RefreIcon;
import com.ui.extensible.RunIcon;
import com.ui.extensible.UITabbedPane;
import com.ui.extensible.listener.TabActionListener;
import com.ui.panel.border.VerticalSplitPane;
import com.view.tool.HelpProperties;
import com.view.tool.SQLFormatterUtil;

import main.SQLTool;

/**
 * sql执行界面管理，包括执行结果信息展示与错误信息纰漏
 * 
 * @author Administrator
 * 
 */
public class QuerySqlTab extends VerticalSplitPane implements RunIcon{
	private static final long serialVersionUID = 1L;
	private DBKeyTextPane editSql;
	private TabActionListener action;
	private JComboBox connectionList;
	private UITabbedPane resultTab;
	private MutableAttributeSet erro, messg;
	private LineNumberHeaderView lineNumberHeaderView ;
	private String file="";
	private String text="";

	private boolean isChange =true;
	private int index;
	private UITabbedPane parents;
	public DBKeyTextPane getEditSqlText() {
		erro = new SimpleAttributeSet();
		StyleConstants.setForeground(erro, Color.red);
		messg = new SimpleAttributeSet();
		StyleConstants.setForeground(messg, Color.black);
		return editSql;
	}
	public void addTabActionListener(TabActionListener action){
		this.action = action;
	}
	
	public void setIndex(int index){
		this.index = index;
	}

	public QuerySqlTab( ) {
		super(300);
		log = new JTextPane();
	}
	public QuerySqlTab(UITabbedPane parents) {
		this();
		new DropTarget(editSql, DnDConstants.ACTION_COPY_OR_MOVE,new FileToTabDnd());

		this.parents = parents;
	}

	public String getFile(){
		return file;
	}
	public void setFile(String file){
		this.file =  file;
	}

	private Intellisense intellisense;
	boolean key = false;
	StyledDocument doc;
	JScrollPane scrollPane;
	private TextAction enter;
	private JTextPane log;
	private TextAction up;
	private TextAction down;
	private TextAction slash;
	private TextAction shiftCtrlF;
	private TextAction period;
	private TextAction redo;
	private TextAction ctrlS;
	private TextAction undo;
	private RefreIcon refreIcon;
	Logger logger = Logger.getLogger(getClass());
	@Override
	public void init() {
		editSql = new DBKeyTextPane();
		intellisense = new Intellisense(editSql,editSql.getGraphicsConfiguration());
		period = new TextAction("period") {
			@Override
			public void actionPerformed(ActionEvent paramActionEvent) {
				String sql="";
				try {
					sql = editSql.getSql();
					intellisense.change(editSql.getCursLastWrod().trim(),getConnectionInfo(),sql);
					intellisense.setVisible(true);
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		};
		enter = new TextAction("enter") {
			@Override
			public void actionPerformed(ActionEvent paramActionEvent) {
				try {
					String cursLastWrod = editSql.getCursLastWrod();
					insertCaretKeyWord(cursLastWrod);
//					System.out.println("========");
				} catch (BadLocationException e1) {
					logger.error("", e1);
				}
			}
		};
		up = new TextAction("up") {
			@Override
			public void actionPerformed(ActionEvent paramActionEvent) {
				intellisense.upRow();
			}
		};
		slash = new TextAction("slash") {
			@Override
			public void actionPerformed(ActionEvent paramActionEvent) {
				try {
					String sql = editSql.getSql();
					intellisense.change(editSql.getCursLastWrod().trim(),getConnectionInfo(),sql);
					intellisense.setVisible(true);
				} catch (BadLocationException e) {
					logger.error("", e);
				}
			}
		};
		down = new TextAction("down") {
			@Override
			public void actionPerformed(ActionEvent paramActionEvent) {
				intellisense.nextRow();
			}
		};
		shiftCtrlF = new TextAction("shift ctrl pressed F") {
			@Override
			public void actionPerformed(ActionEvent paramActionEvent) {
				String format = SQLFormatterUtil.getSQLFormatterUtil().format(editSql.getSelectedText());
//				String format = FormatStyle.BASIC.getFormatter().format();
//				String format = FormatStyle.NONE.getFormatter().format(editSql.getSelectedText());
				int selectionStart = editSql.getSelectionStart();
				editSql.replaceSelection(format);
				try {
					editSql.setWordColor(selectionStart,editSql.getSelectionEnd());
				} catch (BadLocationException e1) {
					logger.error("", e1);
				}
			}
		};
		ctrlS = new TextAction("ctrl s") {
			@Override
			public void actionPerformed(ActionEvent paramActionEvent) {
				UITabbedPane tabQuerySql = SQLTool.getSQLTool().getToolFrame().getSqlView().getTabQuerySql();
				QuerySqlTab tab = (QuerySqlTab) tabQuerySql.getSelectedComponent();
				tab.colse();
			}
		};
		resultTab = new UITabbedPane(UITabbedPane.TOP);
		
		
		final  UndoManager undoManager = new UndoManager();
		
		editSql.getDocument().addUndoableEditListener(new UndoableEditListener() {
			public void undoableEditHappened(UndoableEditEvent evt) {
				String redoPresentationName = evt.getEdit().getPresentationName();	
//				if(redoPresentationName.endsWith(UIManager.getString("AbstractDocument.styleChangeText"))||redoPresentationName.endsWith(UIManager.getString("AbstractDocument.deletionText"))){
//				}else {
					undoManager.addEdit(evt.getEdit());
//				}
			}
		});


		redo = new TextAction("redo") {
			@Override
			public void actionPerformed(ActionEvent paramActionEvent) {
				try {
					if (undoManager.canRedo()) {
						undoManager.redo();
					}
				} catch (CannotRedoException e) {
				}
			}
		};
		undo = new TextAction("undo") {
			@Override
			public void actionPerformed(ActionEvent paramActionEvent) {
				try {
					if (undoManager.canUndo()) {
						undoManager.undo();
					}
				} catch (CannotRedoException e) {
				}
			}
		};
//			[shift ctrl pressed KP_LEFT, pressed KP_LEFT, shift ctrl pressed KP_RIGHT, shift pressed KP_RIGHT, pressed LEFT, shift ctrl pressed RIGHT, shift ctrl pressed END, shift pressed DOWN, pressed CUT, shift pressed UP, ctrl pressed KP_LEFT, pressed PAGE_UP, shift ctrl pressed HOME, shift ctrl pressed O, ctrl pressed BACK_SLASH, ctrl pressed KP_RIGHT, shift pressed HOME, ctrl pressed Z, shift pressed INSERT, pressed UP, pressed KP_DOWN, ctrl pressed SPACE, pressed PAGE_DOWN, shift pressed DELETE, shift pressed KP_DOWN, ctrl pressed INSERT, ctrl pressed END, shift pressed PAGE_UP, pressed DELETE, pressed BACK_SPACE, pressed COPY, ctrl pressed LEFT, shift pressed BACK_SPACE, ctrl pressed C, shift pressed PAGE_DOWN, ctrl pressed DELETE, shift pressed LEFT, shift ctrl pressed PAGE_DOWN, shift pressed KP_LEFT, pressed KP_UP, pressed END, ctrl pressed V, ctrl pressed Y, ctrl pressed A, pressed PASTE, ctrl pressed RIGHT, pressed TAB, pressed RIGHT, shift ctrl pressed T, shift ctrl pressed PAGE_UP, pressed HOME, ctrl pressed BACK_SPACE, shift ctrl pressed LEFT, shift pressed RIGHT, ctrl pressed H, pressed KP_RIGHT, ctrl pressed T, ctrl pressed X, pressed ENTER, shift pressed END, ctrl pressed HOME, shift pressed KP_UP, pressed DOWN]
		editSql.getInputMap().put(KeyStroke.getKeyStroke("control Y"), redo);
		editSql.getInputMap().put(KeyStroke.getKeyStroke("control Z"), undo);
		editSql.getInputMap().put(KeyStroke.getKeyStroke("alt pressed SLASH"), slash);
		editSql.getInputMap().put(KeyStroke.getKeyStroke("pressed DOWN"), down);
		editSql.getInputMap().put(KeyStroke.getKeyStroke("shift ctrl pressed F"), shiftCtrlF);
		editSql.getInputMap().put(KeyStroke.getKeyStroke("pressed PERIOD"), period);
		editSql.getInputMap().put(KeyStroke.getKeyStroke("control S"), ctrlS);
 
		
		scrollPane = new JScrollPane();
		scrollPane = new JScrollPane(editSql,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		lineNumberHeaderView = new LineNumberHeaderView();
    	String current_fontName = HelpProperties.GetValueByKey("keyvalue.properties","current_fontName" );
    	String current_fontSize = HelpProperties.GetValueByKey("keyvalue.properties","current_fontSize" );
    	String current_fontStyle = HelpProperties.GetValueByKey("keyvalue.properties","current_fontStyle" );
		Font font= new Font(current_fontName, Integer.parseInt(current_fontStyle), new Integer(current_fontSize));
		System.out.println(font.getFontName(getDefaultLocale()));
		lineNumberHeaderView.setFont(font);
		scrollPane.setFont(font);
		scrollPane.setRowHeaderView(lineNumberHeaderView);
		editSql.setMargin(new Insets(0, 0, 0, 0));
		editSql.setFont(font);
//		lineNumberHeaderView.setLineHeight(editSql.getFontMetrics(font).getHeight());

		connectionList = new JComboBox();
		referDrowDown();
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panel.add(connectionList);
		connectionList.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(connectionList.getSelectedItem()!=null){
					LoginInfo info = (LoginInfo) ((PromptLabel)connectionList.getSelectedItem()).getUser();
					Cache.getCache().setLoade(info, true);
				}
//				intellisense.clearPromps();
			}
		});
		JPanel upPanle = new JPanel(new BorderLayout());
		upPanle.add(scrollPane, BorderLayout.CENTER);
		upPanle.add(panel, BorderLayout.SOUTH);

		intellisense.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent paramMouseEvent) {
				if (intellisense.isShow()&& paramMouseEvent.getClickCount() == 2) {
					try {
						insertCaretKeyWord(editSql.getCursLastWrod());
					} catch (BadLocationException e) {
						logger.error("", e);
					}
				}
			}
		});
		editSql.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
					case KeyEvent.VK_DOWN:
						if (intellisense.isVisible()) {
							editSql.getInputMap().put(KeyStroke.getKeyStroke("pressed DOWN"), down);
						}else
							editSql.getInputMap().remove(KeyStroke.getKeyStroke("pressed DOWN"));
						break;
					case KeyEvent.VK_UP:
						if (intellisense.isVisible()) {
							editSql.getInputMap().put(KeyStroke.getKeyStroke("pressed UP"), up);
						}else
							editSql.getInputMap().remove(KeyStroke.getKeyStroke("pressed UP"));
						break;
						
					case KeyEvent.VK_ENTER:
						if (intellisense.isVisible()) {
							editSql.getInputMap().put(KeyStroke.getKeyStroke("pressed ENTER"), enter);
						}else{
							editSql.getInputMap().remove(KeyStroke.getKeyStroke("pressed ENTER"));

						}
						break;
					case KeyEvent.VK_F8:
						StringBuffer error=new StringBuffer();
						UITabbedPane tabQuerySql = SQLTool.getSQLTool().getToolFrame().getSqlView().getTabQuerySql();
						Component selectedComponent = tabQuerySql.getSelectedComponent();
						
						QuerySqlTab editSqlTab = (QuerySqlTab)selectedComponent;
						DBKeyTextPane editSqlText = editSqlTab.getEditSqlText();
						int start = editSqlText.getSelectionStart();
						int end = editSqlText.getSelectionEnd();
						editSqlText.setSelectionStart(start);
						editSqlText.setSelectionEnd(end);
						editSqlText.setSelectionColor(editSqlText.getSelectionColor());
						editSqlText.grabFocus();
						LoginInfo info = editSqlTab.getConnectionInfo();
						if("".equals(info.getDataType())){
							JOptionPane.showMessageDialog(SQLTool.getSQLTool().getToolFrame().getFrame(), "请选择  Connection!");
							return ;
						}
						try {
							editSqlTab.clearResult();
							Controller.newController().executQuerySqlTab(info, editSqlTab);
							editSqlText.setSelectText(start, end);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						} catch (SQLException e1) {
							error.append(new Date().toLocaleString());	
							error.append(" : execute ");
							error.append(" fail :  ");
							error.append(" message : "+e1.getMessage());
							error.append("cause : "+e1.getCause());
							error.append("errorCode :"+e1.getErrorCode());
							error.append("loa :"+e1.getLocalizedMessage());
							e1.printStackTrace();
						}
						break;
					}
				intellisense.referLocation();

			}
		});
		editSql.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				if(intellisense.isVisible()){
					String cursRow = "";
					String sql ="";
					try {
						cursRow = editSql.getCursLastWrod();
						sql = editSql.getSql();
					} catch (BadLocationException e1) {
						logger.error("", e1);
					}
					intellisense.change(cursRow.trim(),getConnectionInfo(),sql) ;
//					intellisense.referLocation();
					
				}
				
				if(!text.equals(editSql.getText())){
					if(index!=0){
						
						LazyImageIcon lazyImageIcon = new LazyImageIcon("/imgs/tab/tabediting.png");
						parents.setIconAt(index, lazyImageIcon);
						isChange = true;
					}
				}
			}
		});

		editSql.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent arg0) {
				intellisense.setVisible(false);
			}
			@Override
			public void focusGained(FocusEvent arg0) {
			}
		});

		editSql.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e) {
				intellisense.setVisible(false);
			}
		});
		this.setUpPanel(upPanle);
		this.setDownPanel(resultTab);
	}

	/**
	 * 向光标位置插入选中单词
	 * @param likeWord
	 * @throws BadLocationException
	 */
	private void insertCaretKeyWord(String likeWord) throws BadLocationException {
		String selectWord = intellisense.getSelectWord();
		String[] split2 = likeWord.split("\\.");
		if(likeWord==null||"".equals(likeWord.trim())||likeWord.endsWith(".")){
			if(split2.length==1){
				editSql.insertSelectWord(selectWord,"");
			}else if(split2.length==2){
				editSql.insertSelectWord(selectWord,split2[1]);
			}
		}
		else{
			int indexOf = likeWord.indexOf(".");
			if(indexOf==-1){
				editSql.insertSelectWord(selectWord,likeWord);
			}else{
				if(split2.length==1){
					editSql.insertSelectWord(selectWord,"");
				}else if(split2.length==2){
					editSql.insertSelectWord(selectWord,split2[1]);
				}
			}
		}
		intellisense.setVisible(false);
	}

	// 获取行号
	public  int getRow(int pos, JTextComponent editor) {
		int rn = (pos == 0) ? 1 : 0;
		try {
			int offs = pos;
			while (offs > 0) {
				offs = Utilities.getWordStart(editor, offs) - 1;
				rn++;
			}
		} catch (BadLocationException e) {
			logger.error("", e);
		}
		return rn;
	}

	// 获取列号
	public  int getColumn(int pos, JTextComponent editor) {
		try {
			return pos = Utilities.getRowStart(editor, pos) + 1;
		} catch (BadLocationException e) {
			logger.error("", e);
		}
		return -1;
	}

	public void referDrowDown() {
		SQLTool.getSQLTool().referDrowDown(connectionList);
	}

	public JComboBox getConnectionList() {
		return connectionList;
	}

	public void setConnectionList(JComboBox connectionList) {
		this.connectionList = connectionList;
	}

	public LoginInfo getConnectionInfo() {
		return (LoginInfo) ((PromptLabel)connectionList.getSelectedItem()).getUser();
	}

	public void clearResult() {
		resultTab.removeAll();
		
		
		
		JScrollPane scrollPane1 = new JScrollPane();
		scrollPane1 = new JScrollPane(log,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		LineNumberHeaderView lineNumberHeaderView = new LineNumberHeaderView();
		Font font = log.getFont();
		lineNumberHeaderView.setFont(font);
		scrollPane1.setRowHeaderView(lineNumberHeaderView);
		log.setFont(font);
		lineNumberHeaderView.setLineHeight(log.getFontMetrics(font).getHeight());
		resultTab.addTab("Log", scrollPane1);
	}

	/**
	 * 设置执行insert、update、delete sql语句后影响的行数
	 * 
	 * @param rows
	 * @param error
	 * @param isSuccess
	 */
	public void setResultInfluenceRows(int[] rows, String error, boolean isSuccess) {
		if (!isSuccess) {
			ResultView editSqlTab = getErrorPanl(rows, error);
			resultTab.addTab("结果", editSqlTab);
		} else {
			ResultView editSqlTab = getErrorPanl(rows, "执行成功");
			resultTab.addTab("结果", editSqlTab);
		}
	}

	/**
	 * 得到错误信息JPanl
	 * @param rows
	 * @param error
	 * @param sql
	 * @return
	 */
	private ResultView getErrorPanl(int[] rows, String error) {
		ResultView editSqlTab = new ResultView();
		JTextPane result = new JTextPane();
		result.setText(error + "  " );
		editSqlTab.setResultInfo(result);
		return editSqlTab;
	}

	/**
	 * 设置查询结果集设置
	 * @param resultSet
	 * @param error
	 * @param isSuccess
	 * @throws SQLException
	 */
	public void setResultInfo(Table resultSet,
			String error, boolean isSuccess)  {
		if (isSuccess) {
			if(resultSet.getResult_type()==Table.RESULT_UPDATA){
				try {
					log.getDocument().insertString(log.getDocument().getLength(), new Date(resultSet.getStartTime()).toLocaleString()+" : 更新 【"+resultSet.getTableCode()+"】 表成功        更新条数： "+resultSet.getUpdataCount()+"  执行时间 "+(System.currentTimeMillis()-resultSet.getStartTime())+"\n", messg);
				} catch (BadLocationException e) {
					logger.error("", e);
				}
			}else{
				ResultView editSqlTab = new ResultView();
				editSqlTab.setResultInfo(getConnectionInfo(),resultSet);
				try {
					log.getDocument().insertString(log.getDocument().getLength(), new Date(resultSet.getStartTime()).toLocaleString()+" : 查询 【"+resultSet.getTableCode()+"】 表成功        查询结果："+resultSet.getSelectConut()+"条    执行时间 "+(System.currentTimeMillis()-resultSet.getStartTime())+"\n", messg);
				} catch (BadLocationException e) {
					logger.error("", e);
				}

				resultTab.addTab("结果", editSqlTab);
				resultTab.setSelectedIndex(1);

			}
		} else {
			try {
				log.getDocument().insertString(log.getDocument().getLength(), new Date(resultSet!=null?resultSet.getStartTime():System.currentTimeMillis()).toLocaleString()+" :   "+error.toString()+"\n", erro);
			} catch (BadLocationException e) {
				logger.error("", e);
			}
		}
	}

	public UITabbedPane getResutTab() {
		return resultTab;
	}
	
	public synchronized void exected(){
		refreIcon.stop();
		refreIcon = null;
	}
	
	public synchronized boolean isExct(){
		if(refreIcon==null){
			return false;
		}
		return refreIcon.isExec();
	}
	public synchronized void execting(){
		if(refreIcon==null){
			synchronized (this) {
				refreIcon = new RefreIcon(index,this);
			}
		}
		Thread th = new Thread(refreIcon);
		th.start();
	}
	@Override
	public void refreIcon(Icon icon) {
		parents.setIconAt(index,icon);
		parents.repaint();
	}
	private void editing() {
//		parents.setIconAt(index,lazyImageIcon);
		parents.repaint();		
	}

	@Override
	public void endIcon() {
		LazyImageIcon lazyImageIcon = new LazyImageIcon("/imgs/tab/tabedit.png");
		parents.setIconAt(index,lazyImageIcon);
		parents.repaint();		
	}

	public int getIndex() {
		return index;
	}
	public boolean colse() {
		return action.colse(editSql);
	}
	public void open() {
		action.open(editSql);
	}
	public String getText() {
		return editSql.getText()==null?"":editSql.getText();
	}
	public void setText(String text) {
		this.text = text;
		editSql.setText(text);
	}
	public boolean isChange() {
		return isChange;
	}
	public boolean setChange() {
		text = editSql.getText()==null?"":editSql.getText();
		LazyImageIcon lazyImageIcon = new LazyImageIcon("/imgs/tab/tabedit.png");
		parents.setIconAt(index,lazyImageIcon);
		return isChange = false;
	}

	
	
}
