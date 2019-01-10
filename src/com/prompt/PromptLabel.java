package com.prompt;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.entity.LoginInfo;
import com.ico.ImagesManager;
import com.view.tool.HelpProperties;
import com.view.tool.PowerDesigner;
import com.view.tool.entity.PDColumnEntity;

public class PromptLabel {
		private final static  Icon functionIcon =  ImagesManager.getImagesManager().getIcon("/imgs/dataBaseImgs/function.png");
		private final static  Icon tableIcon =  ImagesManager.getImagesManager().getIcon("/imgs/dataBaseImgs/table.gif");
		private final static  Icon columnIcon =  ImagesManager.getImagesManager().getIcon("/imgs/dataBaseImgs/column.png");
		private final static  Icon onlogdatabase =  ImagesManager.getImagesManager().getIcon("/imgs/dataBaseImgs/onlogdatabase.gif");
		private final static  Icon nologiindatabse =  ImagesManager.getImagesManager().getIcon("/imgs/dataBaseImgs/nologiindatabse.gif");

		public final static int TABLE = 1;
		public final static int FUNCTION = 2;
		public final static int COLUMN = 3;
		public final static int ONLINE = 4;
		public final static int NOONLINE = 5;
		private Icon icon;
		private Object code;
		private String text;
		private boolean isSelect ;
		private int type;
		private final static String CODE_H = "<html><font color='red'>";
		private final static String CODE_D = "</font></html>";
		public static final int ERROR = -1;
		public PromptLabel(Object code,String text,int type){
			super();
			switch (type) {
			case 1:
				icon = tableIcon;
				break;
			case 2:
				icon = functionIcon;
				break;
			case 3:
				icon = columnIcon;
				break;
			case 4:
				icon = onlogdatabase;
				break;
			case 5:
				icon = nologiindatabse;
				break;
			default:
				icon = nologiindatabse;
				break;
			}
			this.type = type;
			this.code = code;
			this.text = text;
		}
		
		public String getCode(){
			return code.toString();
		}
		
		public Icon getIcon(){
			return icon;
		}
		
		public Object getUser(){
			return code;
		}
		
		public String getText(){
			return text;
		}
		
		public void setSelect(boolean isSelect){
			this.isSelect = isSelect;
		}
		public int getType(){
			return this.type;
		}
		@Override
		public String toString() {
			if(type==ERROR){
				return  "<html> <font color='red'>"+code+"</font></html>";
			}
			if(null==text||"".equals(text)){
				return code.toString();
			}
			if(isSelect){
				return  "<html> "+code+" - <font color=''>"+text+"</font></html>";
			}
			return  "<html> "+code+" - <font color='#A8A8A8'>"+text+"</font></html>";
		}
	}