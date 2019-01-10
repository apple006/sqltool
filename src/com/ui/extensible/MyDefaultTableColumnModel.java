package com.ui.extensible;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.dao.entity.Column;
import com.ico.LazyImageIcon;

public class MyDefaultTableColumnModel extends JTableHeader {

	private LazyImageIcon lazy = new LazyImageIcon("/imgs/table/key.png");
	
	public MyDefaultTableColumnModel(TableColumnModel mode){
		super(mode);
	}
	@Override
	protected TableCellRenderer createDefaultRenderer() {
		TableCellRenderer iconHeaderRenderer = new DefaultTableCellRenderer() {
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				if (table != null) {
					JTableHeader header = table.getTableHeader();
					if (header != null) {
						setForeground(header.getForeground());
						setBackground(header.getBackground());
						setFont(header.getFont());
					}
				}
				
				
				
				if(((UITable)table).getHeaderColumn(column) .isPrimaryKey()){
					setIcon(lazy.getIcon());
					setText(((UITable)table).getHeaderColumn(column).toString());
				}else{
					setText(((UITable)table).getHeaderColumn(column).toString());
					setIcon(null);
				}
				setBorder(UIManager.getBorder("TableHeader.cellBorder"));
				setHorizontalAlignment(JLabel.LEADING);
				return this;
			}
		};
		return iconHeaderRenderer;
	}
}
