package com.ui.extensible;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.TextArea;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.TreePath;

import main.DragAndDropDragSourceListener;

import com.dnd.FileToTabDnd;
import com.jtattoo.plaf.LazyImageIcon;
import com.ui.extensible.listener.TabActionListener;
import com.view.sqloperate.QuerySqlTab;

public class UITabbedPane extends JTabbedPane implements Serializable {

	private boolean isOverMin;
	private boolean isOverMax;

	private Map<Integer,RefreIcon> tabIcons ;
	final Rectangle minInset = new Rectangle(0, 0, 20, 15);
	final Rectangle maxInset = new Rectangle(0, 0, 20, 15);
	final Rectangle replace = new Rectangle(0, 0, 20, 15);
	private List<QuerySqlTab> comp = new ArrayList<QuerySqlTab>();
	private static final long serialVersionUID = 1L;

	public UITabbedPane(int top, int scrollTabLayout) {
		super(top, scrollTabLayout);

		this.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent paramMouseEvent) {
				isOverMin = false;
				isOverMax = false;
			}

			@Override
			public void mousePressed(MouseEvent paramMouseEvent) {
				System.out.println(333333333);
			}

			@Override
			public void mouseExited(MouseEvent paramMouseEvent) {
			}

			@Override
			public void mouseEntered(MouseEvent paramMouseEvent) {

			}

			@Override
			public void mouseClicked(MouseEvent paramMouseEvent) {
				System.out.println(1111111);

			}
		});
		this.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {
				if (minInset.contains(e.getPoint())) {
					isOverMin = true;
					repaint(replace);
				} else {
					isOverMin = false;
					repaint(replace);
				}
				if (maxInset.contains(e.getPoint())) {
					isOverMax = true;
					repaint(replace);
				} else {
					isOverMax = false;
					repaint(replace);
				}

			}

			
			@Override
			public void mouseDragged(MouseEvent e) {

			}
		});
	}
	
	class DragAndDropTransferable12 implements Transferable {
		private Component treeNode;

		public DragAndDropTransferable12(Component treeNode) {
			this.treeNode = treeNode;
		}

		DataFlavor FLAVOR = new DataFlavor(DataFlavor.javaRemoteObjectMimeType, "javaRemoteObjectMimeType");
		DataFlavor[] flavors = {FLAVOR};
		public DataFlavor[] getTransferDataFlavors() {
			return flavors;
		}

		public boolean isDataFlavorSupported(DataFlavor flavor) {
			return flavor.isMimeTypeEqual(DataFlavor.javaRemoteObjectMimeType);
		}

		public Object getTransferData(DataFlavor df)
				throws UnsupportedFlavorException, IOException {
			return treeNode;
		}
		
	}
	
	class DragAndDropDragGestureListener1 implements DragGestureListener {
		public void dragGestureRecognized(DragGestureEvent dge) {
			UITabbedPane tab = (UITabbedPane) dge.getComponent();
			if (tab != null) {
				DragAndDropTransferable12 dragAndDropTransferable = new DragAndDropTransferable12(tab);
				dge.startDrag(DragSource.DefaultCopyDrop,
						dragAndDropTransferable,
						new DragAndDropDragSourceListener());
			}
			else{
				DragAndDropTransferable12 dragAndDropTransferable = new DragAndDropTransferable12(tab);
				dge.startDrag(DragSource.DefaultLinkDrop,
						dragAndDropTransferable,
						new DragAndDropDragSourceListener());
			}
		}
	}

	@Override
	public void remove(int paramInt) {
		QuerySqlTab querySqlTab = comp.get(paramInt);
		if(querySqlTab.colse()){
			comp.remove(paramInt);
			for (int i = paramInt+1; i < comp.size(); i++) {
				comp.get(i).setIndex(comp.get(i).getIndex()-1);
			}
			super.remove(paramInt);
		}
	}
	public UITabbedPane(int top) {
		super(top,JTabbedPane.SCROLL_TAB_LAYOUT);
	}

	
	@Override
	public void addTab(String paramString, Icon paramIcon,
			Component paramComponent) {
		super.addTab(paramString, paramIcon, paramComponent);
		QuerySqlTab tab = (QuerySqlTab) paramComponent;
		tab.setIndex(this.getTabCount()-1);
		comp.add(tab);
		tab.open();
		this.repaint(50);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
	}

	@Override
	protected void paintBorder(Graphics g) {
		super.paintBorder(g);
	}

	class MouseMotionHandler extends MouseAdapter {
		@Override
		public void mouseDragged(MouseEvent paramMouseEvent) {
		}

		@Override
		public void mouseClicked(MouseEvent paramMouseEvent) {

		}
	}

	static class SubtleSquareBorder1 implements javax.swing.border.Border {
		protected int m_w = 5;
		protected int m_h = 5;
		protected Color m_topColor = Color.cyan;
		protected Color m_bottomColor = Color.cyan;
		protected boolean roundc = false; // Do we want rounded corners on the
											// border?

		public SubtleSquareBorder1(boolean round_corners) {
			roundc = round_corners;
		}

		public Insets getBorderInsets(Component c) {
			return new Insets(m_w, m_w, m_w, m_w);
		}

		public boolean isBorderOpaque() {
			return true;
		}

		public void paintBorder(Component c, Graphics g, int x, int y, int w,
				int h) {
//			x++;
//			y++;
//			g.setColor(m_topColor);
//			// // Rounded corners
//			Insets tabAreaInsets = UIManager
//					.getInsets("TabbedPane.contentBorderInsets");
//			int sepHeight = tabAreaInsets.bottom;
//			Color selColors[] = AbstractLookAndFeel.getTheme()
//					.getSelectedColors();
//			Color loColor = selColors[selColors.length - 1];
//			Color darkLoColor = ColorHelper.darker(loColor, 20);
//			Color[] colors = ColorHelper.createColorArr(loColor, darkLoColor,
//					sepHeight);

//			for (int i = 0; i < colors.length; i++) {
//				g.setColor(colors[i]);
//				g.drawLine(x - 1, y + i - 1, w + x - 1, y + i - 1);
//				g.drawLine(x + i - 1, y - 1, x + i - 1, h);
//				g.drawLine(w + i - 5, y - 1, w + i - 5, h);
//				g.drawLine(x - 1, h + i - 5, w - 1, h + i - 5);
//			}
		}
	}

	class SubtleSquareBorder implements Border {
		protected int m_w = 0;
		protected int m_h = 0;
		protected Color m_topColor = Color.gray;
		protected Color m_bottomColor = Color.gray;
		protected boolean roundc = false; // Do we want rounded corners on the
											// border?

		public SubtleSquareBorder(boolean round_corners) {
			roundc = round_corners;
		}

		public Insets getBorderInsets(Component c) {
			return new Insets(m_h, m_w, m_h, m_w);
		}

		public boolean isBorderOpaque() {
			return true;
		}

		public void paintBorder(Component c, Graphics g, int x, int y, int w,
				int h) {
			w = w - 3;
			h = h - 3;

			minInset.setRect(w - 45, y + 4, 20, 15);
			maxInset.setRect(w - 25, y + 4, 20, 15);
			replace.setRect(w - 45, y + 4, 45, 20);
			LazyImageIcon max = new LazyImageIcon("icons/thin_max_view.gif");
			LazyImageIcon min = new LazyImageIcon("icons/thin_min_view.gif");
			min.paintIcon(c, g, w - 40, y + 4);
			max.paintIcon(c, g, w - 20, y + 4);
			if (isOverMax)
				g.drawRoundRect(maxInset.x, maxInset.y, maxInset.width,
						maxInset.height, 5, 5);
			if (isOverMin) {
				g.drawRoundRect(minInset.x, minInset.y, minInset.width,
						minInset.height, 5, 5);

			}
			x++;
			y++;
			// Rounded corners
			int a = 2;
			if (roundc) {
				g.setColor(m_topColor);
				g.drawLine(x, y + 3, x, y + h - 3);
				g.drawLine(x + 3, y, x + w - 3, y);
				g.drawLine(x, y + 3, x + 3, y); // Top left diagonal
				g.drawLine(x, y + h - 3, x + 3, y + h); // Bottom left diagonal
				g.setColor(m_bottomColor);
				g.drawLine(x + w, y + 3, x + w, y + h - 3);
				g.drawLine(x + 3, y + h, x + w - 3, y + h);
				g.drawLine(x + w - 3, y, x + w, y + 3); // Top right diagonal
				g.drawLine(x + w, y + h - 3, x + w - 3, y + h); // Bottom right
																// diagonal
			}
			// Square corners
			else {
				g.setColor(m_topColor);
				g.drawLine(x, y, x, y + h);
				g.drawLine(x, y, x + w, y);
				g.setColor(m_bottomColor);
				g.drawLine(x + w, y, x + w, y + h);
				g.drawLine(x, y + h, x + w, y + h);
			}
		}
	}
}
