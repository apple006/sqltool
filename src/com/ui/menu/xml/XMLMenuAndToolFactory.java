package com.ui.menu.xml;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.ico.LazyImageIcon;
import com.ui.MenuListenner;
import com.ui.menu.MenuAndTool;
import com.ui.tree.MouserListenFactory;
import com.ui.tree.Singleton;

public class XMLMenuAndToolFactory implements MenuAndTool {
	private final String property = System.getProperty("user.dir");
	private static XMLMenuAndToolFactory xmlMenuAndTool;
	public static Object o = new Object();
	private HashMap<String, Element> key_Element = new HashMap<String, Element>();

	/**
	 * 读取菜单、弹出菜单、工具栏菜单信息
	 */
	private XMLMenuAndToolFactory() {
		SAXReader saxReader = new SAXReader();
		Document document = null;
		try {
			document = saxReader.read(property + "\\config\\menu.xml");

		} catch (DocumentException e1) {
			e1.printStackTrace();
		}
		Element rootElement = document.getRootElement();
		Iterator<Element> elementIterator = rootElement.elementIterator();
		while (elementIterator.hasNext()) {
			Element e = elementIterator.next();
			key_Element.put(e.attributeValue("name"), e);
		}
	}

	public static XMLMenuAndToolFactory getXMLMenuAndToolFactory() {
		if (xmlMenuAndTool == null) {
			synchronized (o) {
				xmlMenuAndTool = new XMLMenuAndToolFactory();
			}
		}
		return xmlMenuAndTool;
	}

	@Override
	public JToolBar getToolBar(String name, String type) {
		JToolBar bar = new JToolBar(name);
		Iterator<Element> elementIterator = (key_Element.get(type))
				.elementIterator();
		try {
			while (elementIterator.hasNext()) {
				Element next = elementIterator.next();
				String iocnPach =  next.attributeValue("icon");
				if (iocnPach == null) {
					new Exception("工具栏图标不能为空");
				}
				JButton but = new JButton(new LazyImageIcon(iocnPach).getIcon());
				but.setToolTipText(next.attributeValue("value"));
				but.setName(next.attributeValue("value"));
				but.addActionListener((ActionListener) getListenerObject(next
						.attributeValue("class")));
				bar.add(but);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return bar;
	}

	private void earchButtons(String type, JComponent bar) {
		Iterator<Element> elementIterator = (key_Element.get(type))
				.elementIterator();
		try {
			while (elementIterator.hasNext()) {
				Element next = elementIterator.next();
				String iocnPach = property + next.attributeValue("icon");
				if (iocnPach == null) {
					new Exception("工具栏图标不能为空");
				}
				ImageIcon imageIcon = new ImageIcon(iocnPach);
				JButton but = new JButton(imageIcon);
				but.setPreferredSize(new Dimension(25, 20));
				but.setToolTipText(next.attributeValue("value"));
				but.setName(next.attributeValue("name"));
				but.addActionListener((ActionListener) getListenerObject(next
						.attributeValue("class")));
				bar.add(but);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public JPanel getButtons(String type) {
		JPanel panl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		earchButtons(type, panl);
	
		return panl;
	}

	@Override
	public JPopupMenu getPopupMenu(String type) {
		JPopupMenu popupMenu = new JPopupMenu();
		Element element = key_Element.get(type);
		String rootListenner = element.attributeValue("class");
		Iterator<Element> elementIterator = (element).elementIterator();
		try {
			while (elementIterator.hasNext()) {
				Element next = elementIterator.next();

				String attributeValue = next.attributeValue("value");
				if (next.elementIterator().hasNext()) {
					JMenu menu = new JMenu(attributeValue);
					if ("".equals(attributeValue) || attributeValue == null) {
						JSeparator split = new JSeparator(
								SwingConstants.VERTICAL);
						popupMenu.add(split);
					} else {
						popupMenu.add(menu);
					}
					if ("".equals(next.attributeValue("class"))) {
						menu.addMouseListener(MouserListenFactory
								.getMouserListenFactory().getMenuListenner(
										rootListenner));
					} else {
						menu.addMouseListener(MouserListenFactory
								.getMouserListenFactory().getMenuListenner(
										next.attributeValue("class")));
					}
					searchAll(next, menu, rootListenner);
				} else {
					JMenuItem menu = new JMenuItem(attributeValue);
					if ("".equals(attributeValue) || attributeValue == null) {
						JSeparator split = new JSeparator(
								SwingConstants.VERTICAL);
						popupMenu.add(split);
					} else {

						popupMenu.add(menu);
					}
					if ("".equals(next.attributeValue("class"))) {
						menu.addMouseListener(MouserListenFactory
								.getMouserListenFactory().getMenuListenner(
										rootListenner));
					} else {
						menu.addMouseListener(MouserListenFactory
								.getMouserListenFactory().getMenuListenner(
										next.attributeValue("class")));
					}
					searchAll(next, menu, rootListenner);
				}

			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return popupMenu;
	}

	@Override
	public JMenuBar getMenu(String type) {
		JMenuBar bar = new JMenuBar();
		Element element = key_Element.get(type);
		String rootListenner = element.attributeValue("class");
		Iterator<Element> elementIterator = (element).elementIterator();
		JMenu menu = null;
		while (elementIterator.hasNext()) {
			Element next = elementIterator.next();
			String isSplit = next.attributeValue("value");
			if ("".equals(isSplit) || isSplit == null) {
				JSeparator split = new JSeparator(SwingConstants.VERTICAL);
				bar.add(split);
			} else {
				try {
					menu = new JMenu(isSplit);
					bar.add(menu);
					searchAll(next, menu, rootListenner);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}

		}
		return bar;
	}

	/**
	 * 搜索所有菜单，将菜单信息封装成List<List>
	 * 
	 * @param rootListenner
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 */
	private void searchAll(Element e, JMenuItem item, String rootListenner)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		Iterator<Element> elementIterator = e.elementIterator();
		while (elementIterator.hasNext()) {
			Element next = elementIterator.next();
			JMenuItem temp = addIcon(next);
			// temp.setMnemonic(KeyEvent.VK_C);
			if (next.elementIterator().hasNext()) {
				JMenu menu = new JMenu(next.attributeValue("value"));
				if (next.attributeValue("class").equals("")) {
					menu.addMouseListener(MouserListenFactory
							.getMouserListenFactory().getMenuListenner(
									rootListenner));

				} else {
					menu.addMouseListener(MouserListenFactory
							.getMouserListenFactory().getMenuListenner(
									next.attributeValue("class")));
				}
				item.add(menu);
				searchAll(next, menu, rootListenner);
			} else {
				String isSplit = next.attributeValue("value");
				if ("".equals(isSplit) || isSplit == null) {
					JSeparator split = new JSeparator();
					item.add(split);
				} else {
					temp.addMouseListener(MouserListenFactory
							.getMouserListenFactory().getMenuListenner(
									next.attributeValue("class")));
					item.add(temp);
				}
			}
		}
		return;
	}

	/**
	 * 添加菜单图标
	 * 
	 * @param next
	 * @return
	 */
	private JMenuItem addIcon(Element next) {
		JMenuItem temp = null;
		String icon = next.attributeValue("icon");
		if ("".equals(icon) || icon == null) {
			temp = new JMenuItem(next.attributeValue("value"));
		} else {
			temp = new JMenuItem(next.attributeValue("value"), new ImageIcon(
					property + icon));
		}

		return temp;
	}

	/**
	 * 
	 * @param claspath
	 *            监听类路径
	 * @return Object 监听类实例
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	private Object getListenerObject(String claspath)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		if (!"".equals(claspath) && claspath != null) {
			Class forName = (Class<Singleton>) Class.forName(claspath);
			return forName.newInstance();
		}
		return new MenuListenner();
	}

}
