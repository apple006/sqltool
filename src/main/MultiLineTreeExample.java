package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
 
 
public class MultiLineTreeExample extends JFrame {
  public MultiLineTreeExample() {
    super("Multi-Line JTree Example");
 
    String[] strs = { "swing", // 0
        "package", // 1
        "java.awt.swingn" + "com.sun.java.swing", // 2
        "javax.swing", // 3
        "JTree" }; // 4
 
    DefaultMutableTreeNode[] nodes = new DefaultMutableTreeNode[strs.length];
    for (int i = 0; i < strs.length; i++) {
      nodes[i] = new DefaultMutableTreeNode(strs[i]);
    }
    nodes[0].add(nodes[1]);
    nodes[1].add(nodes[2]);
    nodes[1].add(nodes[3]);
    nodes[0].add(nodes[4]);
 
    JTree tree = new JTree(nodes[0]);
    tree.setEditable(true);
    tree.setCellRenderer(new MultiLineCellRenderer());
    JScrollPane sp = new JScrollPane();
    sp.getViewport().add(tree);
    getContentPane().add(sp, BorderLayout.CENTER);
  }
 
  public static void main(String args[]) {
    try {
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
    } catch (Exception evt) {}
 
    MultiLineTreeExample frame = new MultiLineTreeExample();
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });
    frame.setSize(300, 150);
    frame.setVisible(true);
  }
}
//实现接口
class MultiLineCellRenderer extends JPanel implements TreeCellRenderer {
  protected JLabel icon;
 
  protected TreeTextArea text;
 
  public MultiLineCellRenderer() {
    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    icon = new JLabel() {
      public void setBackground(Color color) {
        if (color instanceof ColorUIResource)
          color = null;
        super.setBackground(color);
      }
    };
    add(icon);
    add(Box.createHorizontalStrut(4));
    add(text = new TreeTextArea());
  }
 
  public Component getTreeCellRendererComponent(JTree tree, Object value,
      boolean isSelected, boolean expanded, boolean leaf, int row,
      boolean hasFocus) {
    String stringValue = tree.convertValueToText(value, isSelected,
        expanded, leaf, row, hasFocus);
    setEnabled(tree.isEnabled());
    text.setText(stringValue);
    text.setSelect(isSelected);
    text.setFocus(hasFocus);
    if (leaf) {
      icon.setIcon(UIManager.getIcon("Tree.leafIcon"));
    } else if (expanded) {
      icon.setIcon(UIManager.getIcon("Tree.openIcon"));
    } else {
      icon.setIcon(UIManager.getIcon("Tree.closedIcon"));
    }
    return this;
  }
 
  public Dimension getPreferredSize() {
    Dimension iconD = icon.getPreferredSize();
    Dimension textD = text.getPreferredSize();
    int height = iconD.height < textD.height ? textD.height : iconD.height;
    return new Dimension(iconD.width + textD.width, height);
  }
 
  public void setBackground(Color color) {
    if (color instanceof ColorUIResource)
      color = null;
    super.setBackground(color);
  }
 
  class TreeTextArea extends JTextArea {
    Dimension preferredSize;
 
    TreeTextArea() {
      setLineWrap(true);
      setWrapStyleWord(true);
      setOpaque(true);
      this.setText("dasfasdfasdfadsfa");
    }
 
    public void setBackground(Color color) {
      if (color instanceof ColorUIResource)
        color = null;
      super.setBackground(color);
    }
 
    public void setPreferredSize(Dimension d) {
      if (d != null) {
        preferredSize = d;
      }
    }
 
    public Dimension getPreferredSize() {
      return preferredSize;
    }
 
    public void setText(String str) {
      FontMetrics fm = getToolkit().getFontMetrics(getFont());
      BufferedReader br = new BufferedReader(new StringReader(str));
      String line;
      int maxWidth = 0, lines = 0;
      try {
        while ((line = br.readLine()) != null) {
          int width = SwingUtilities.computeStringWidth(fm, line);
          if (maxWidth < width) {
            maxWidth = width;
          }
          lines++;
        }
      } catch (IOException ex) {
        ex.printStackTrace();
      }
      lines = (lines < 1) ? 1 : lines;
      int height = fm.getHeight() * lines;
      setPreferredSize(new Dimension(maxWidth + 6, height));
      super.setText(str);
    }
 
    void setSelect(boolean isSelected) {
      Color bColor;
      if (isSelected) {
        bColor = UIManager.getColor("Tree.selectionBackground");
      } else {
        bColor = UIManager.getColor("Tree.textBackground");
      }
      super.setBackground(bColor);
    }
 
    void setFocus(boolean hasFocus) {
      if (hasFocus) {
        Color lineColor = UIManager
            .getColor("Tree.selectionBorderColor");
        setBorder(BorderFactory.createLineBorder(lineColor));
      } else {
        setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
      }
    }
  }
}