/*
* Copyright (c) 2002 and later by MH Software-Entwicklung. All Rights Reserved.
*  
* JTattoo is multiple licensed. If your are an open source developer you can use
* it under the terms and conditions of the GNU General Public License version 2.0
* or later as published by the Free Software Foundation.
*  
* see: gpl-2.0.txt
* 
* If you pay for a license you will become a registered user who could use the
* software under the terms and conditions of the GNU Lesser General Public License
* version 2.0 or later with classpath exception as published by the Free Software
* Foundation.
* 
* see: lgpl-2.0.txt
* see: classpath-exception.txt
* 
* Registered users could also use JTattoo under the terms and conditions of the 
* Apache License, Version 2.0 as published by the Apache Software Foundation.
*  
* see: APACHE-LICENSE-2.0.txt
*/
 
package com.jtattoo.plaf.acryl;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;

import com.jtattoo.plaf.AbstractLookAndFeel;
import com.jtattoo.plaf.BaseTabbedPaneUI;
import com.jtattoo.plaf.ColorHelper;
import com.jtattoo.plaf.LazyImageIcon;

/**
 * author Michael Hagen
 */
public class AcrylTabbedPaneUI extends BaseTabbedPaneUI {

	private Color colorNorth = new Color(57, 181, 215);
	private Color colorSouth = new Color(145, 232, 255);
	private Color colorBorder = new Color(90, 154, 179);
	
	
    public static ComponentUI createUI(JComponent c) {
        return new AcrylTabbedPaneUI();
    }

    public void installDefaults() {
        super.installDefaults();
        tabAreaInsets.bottom = 5;
        initialize();
      
//        closable.add(true);
    }

    protected Color[] getTabColors(int tabIndex, boolean isSelected, boolean isRollover) {
        if ((tabIndex >= 0) && (tabIndex < tabPane.getTabCount())) {
            boolean isEnabled = tabPane.isEnabledAt(tabIndex);
            Color backColor = tabPane.getBackgroundAt(tabIndex);
            Color colorArr[] = AbstractLookAndFeel.getTheme().getTabColors();
            if ((backColor instanceof UIResource)) {
                if (isSelected) {
                    colorArr = AbstractLookAndFeel.getTheme().getDefaultColors();
                } else if (isRollover && isEnabled) {
                    colorArr = AbstractLookAndFeel.getTheme().getRolloverColors();
                } else {
                    colorArr = AbstractLookAndFeel.getTheme().getTabColors();
                }
            } else {
                if (isSelected) {
                    colorArr = ColorHelper.createColorArr(ColorHelper.brighter(backColor, 60), backColor, 20);
                } else if (isRollover && isEnabled) {
                    colorArr = ColorHelper.createColorArr(ColorHelper.brighter(backColor, 80), ColorHelper.brighter(backColor, 20), 20);
                } else {
                    colorArr = ColorHelper.createColorArr(ColorHelper.brighter(backColor, 40), ColorHelper.darker(backColor, 10), 20);
                }
            }
            return colorArr;
        }
        return AbstractLookAndFeel.getTheme().getTabColors();
    }

    protected Color[] getContentBorderColors(int tabPlacement) {
        Color SEP_COLORS[] = {
            ColorHelper.brighter(AbstractLookAndFeel.getControlColorLight(), 20),
            AbstractLookAndFeel.getControlColorLight(),
            ColorHelper.brighter(AbstractLookAndFeel.getControlColorDark(), 20),
            AbstractLookAndFeel.getControlColorDark(),
            ColorHelper.darker(AbstractLookAndFeel.getControlColorDark(), 20)
        };
        return SEP_COLORS;
    }

    protected Color getContentBorderColor() {
        return ColorHelper.brighter(AbstractLookAndFeel.getTheme().getFrameColor(), 50);
    }

    protected Color getLoBorderColor(int tabIndex) {
        if (tabIndex == tabPane.getSelectedIndex() && tabPane.getBackgroundAt(tabIndex) instanceof ColorUIResource) {
            return ColorHelper.brighter(AbstractLookAndFeel.getFrameColor(), 10);
        }
        return super.getLoBorderColor(tabIndex);
    }
    
    protected Font getTabFont(boolean isSelected) {
        if (isSelected) {
            return super.getTabFont(isSelected).deriveFont(Font.BOLD);
        } else {
            return super.getTabFont(isSelected);
        }
    }
//
//    protected void paintText(Graphics g, int tabPlacement, Font font, FontMetrics metrics, int tabIndex, String title, Rectangle textRect, boolean isSelected) {
//        Color backColor = tabPane.getBackgroundAt(tabIndex);
//        if (!(backColor instanceof UIResource)) {
//            super.paintText(g, tabPlacement, font, metrics, tabIndex, title, textRect, isSelected);
//            return;
//        }
//        g.setFont(font);
//        View v = getTextViewForTab(tabIndex);
//        if (v != null) {
//            // html
//            Graphics2D g2D = (Graphics2D) g;
//            Object savedRenderingHint = null;
//            if (AbstractLookAndFeel.getTheme().isTextAntiAliasingOn()) {
//                savedRenderingHint = g2D.getRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING);
//                g2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, AbstractLookAndFeel.getTheme().getTextAntiAliasingHint());
//            }
//            v.paint(g, textRect);
//            if (AbstractLookAndFeel.getTheme().isTextAntiAliasingOn()) {
//                g2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, savedRenderingHint);
//            }
//        } else {
//            // plain text
//            int mnemIndex = -1;
//            if (JTattooUtilities.getJavaVersion() >= 1.4) {
//                mnemIndex = tabPane.getDisplayedMnemonicIndexAt(tabIndex);
//            }
//
//            if (tabPane.isEnabled() && tabPane.isEnabledAt(tabIndex)) {
//                if (isSelected) {
//                    Color shadowColor = ColorHelper.darker(AbstractLookAndFeel.getWindowTitleColorDark(), 30);
//                    g.setColor(shadowColor);
//                    JTattooUtilities.drawStringUnderlineCharAt(tabPane, g, title, mnemIndex, textRect.x - 1, textRect.y - 1 + metrics.getAscent());
//                    JTattooUtilities.drawStringUnderlineCharAt(tabPane, g, title, mnemIndex, textRect.x - 1, textRect.y + 1 + metrics.getAscent());
//                    JTattooUtilities.drawStringUnderlineCharAt(tabPane, g, title, mnemIndex, textRect.x + 1, textRect.y - 1 + metrics.getAscent());
//                    JTattooUtilities.drawStringUnderlineCharAt(tabPane, g, title, mnemIndex, textRect.x + 1, textRect.y + 1 + metrics.getAscent());
//                    g.setColor(AbstractLookAndFeel.getTheme().getTabSelectionForegroundColor());
//                } else {
//                    g.setColor(tabPane.getForegroundAt(tabIndex));
//                }
//                JTattooUtilities.drawStringUnderlineCharAt(tabPane, g, title, mnemIndex, textRect.x, textRect.y + metrics.getAscent());
//
//            } else { // tab disabled
//                g.setColor(tabPane.getBackgroundAt(tabIndex).brighter());
//                JTattooUtilities.drawStringUnderlineCharAt(tabPane, g, title, mnemIndex, textRect.x, textRect.y + metrics.getAscent());
//                g.setColor(tabPane.getBackgroundAt(tabIndex).darker());
//                JTattooUtilities.drawStringUnderlineCharAt(tabPane, g, title, mnemIndex, textRect.x - 1, textRect.y + metrics.getAscent() - 1);
//            }
//        }
//    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
	 * UI实现类
	 * @author Sun
	 */
//		private Rectangle[] closeRects = new Rectangle[0];
		private int nowIndex = -1;
//		private int oldIndex = -1;

			
		
		private void initialize() {
//			UIManager.put("TabbedPane.contentAreaColor", colorSouth);
//			addMouseListener(new MouseAdapter() {
//				public void mousePressed(MouseEvent e) {
//					for (int i = 0; i < getTabCount(); i++) {
//						if (closeRects[i].contains(e.getPoint())
//								&& closable.get(i)) {
//							removeTab(i);
//						}
//					}
//				}
//			});
//			addMouseMotionListener(new MouseAdapter() {
//				public void mouseMoved(MouseEvent e) {
//					nowIndex = -1;
//					for (int i = 0; i < getTabCount(); i++) {
//						if (closeRects[i].contains(e.getPoint())
//								&& closable.get(i)) {
//							nowIndex = i;
//							break;
//						}
//					}
//					if (oldIndex != nowIndex) {
//						if (nowIndex != -1) {
//							repaint(closeRects[nowIndex]);// 控制重绘区域
//						} else {
//							if (oldIndex < getTabCount()) {
//								repaint(closeRects[oldIndex]);// 控制重绘区域
//							}
//						}
//						oldIndex = nowIndex;
//					}
//				}
//			});
		}
		
//		@Override
//		protected void paintContentBorderTopEdge(Graphics g, int tabPlacement,
//				int selectedIndex, int x, int y, int w, int h) {
//			Rectangle selRect = selectedIndex < 0 ? null : getTabBounds(
//					selectedIndex, calcRect);
//			g.setColor(tabPane.getBorder());
//			if (tabPlacement != TOP || selectedIndex < 0
//					|| (selRect.y + selRect.height + 1 < y)
//					|| (selRect.x < x || selRect.x > x + w)) {
//				g.drawLine(x, y, x + w - 2, y);
//			} else {
//				g.drawLine(x, y, selRect.x - 1, y);
//				if (selRect.x + selRect.width < x + w - 2) {
//					g.drawLine(selRect.x + selRect.width, y, x + w - 2, y);
//				} else {
//					g.drawLine(x + w - 2, y, x + w - 2, y);
//				}
//			}
//		}
		
//		@Override
//		protected void paintContentBorderLeftEdge(Graphics g, int tabPlacement,
//				int selectedIndex, int x, int y, int w, int h) {
//			Rectangle selRect = selectedIndex < 0 ? null : getTabBounds(
//					selectedIndex, calcRect);
//			g.setColor(AbstractLookAndFeel.getTheme().getBackgroundColor());
//			if (tabPlacement != LEFT || selectedIndex < 0
//					|| (selRect.x + selRect.width + 1 < x)
//					|| (selRect.y < y || selRect.y > y + h)) {
//				g.drawLine(x, y, x, y + h - 2);
//			} else {
//				g.drawLine(x, y, x, selRect.y - 1);
//				if (selRect.y + selRect.height < y + h - 2) {
//					g.drawLine(x, selRect.y + selRect.height, x, y + h - 2);
//				}
//			}
//		}
//		
//		@Override
//		protected void paintContentBorderBottomEdge(Graphics g, int tabPlacement,
//				int selectedIndex, int x, int y, int w, int h) {
//			Rectangle selRect = selectedIndex < 0 ? null : getTabBounds(
//					selectedIndex, calcRect);
//			g.setColor(AbstractLookAndFeel.getTheme().getBackgroundColor());
//			if (tabPlacement != BOTTOM || selectedIndex < 0 || (selRect.y - 1 > h)
//					|| (selRect.x < x || selRect.x > x + w)) {
//				g.drawLine(x, y + h - 1, x + w - 1, y + h - 1);
//			} else {
//				g.drawLine(x, y + h - 1, selRect.x - 1, y + h - 1);
//				if (selRect.x + selRect.width < x + w - 2) {
//					g.drawLine(selRect.x + selRect.width, y + h - 1, x + w - 1, y
//							+ h - 1);
//				}
//			}
//		}
//
//		@Override
//		protected void paintContentBorderRightEdge(Graphics g, int tabPlacement,
//				int selectedIndex, int x, int y, int w, int h) {
//			Rectangle selRect = selectedIndex < 0 ? null : getTabBounds(
//					selectedIndex, calcRect);
//			g.setColor(AbstractLookAndFeel.getTheme().getBackgroundColor());
//			if (tabPlacement != RIGHT || selectedIndex < 0 || (selRect.x - 1 > w)
//					|| (selRect.y < y || selRect.y > y + h)) {
//				g.drawLine(x + w - 1, y, x + w - 1, y + h - 1);
//			} else {
//				g.drawLine(x + w - 1, y, x + w - 1, selRect.y - 1);
//				if (selRect.y + selRect.height < y + h - 2) {
//					g.drawLine(x + w - 1, selRect.y + selRect.height, x + w - 1, y
//							+ h - 2);
//				}
//			}
//		}
		
		
		
		
		
//		@Override
//		protected void paintTab(Graphics g, int tabPlacement,
//				Rectangle[] rects, int tabIndex, Rectangle iconRect,
//				Rectangle textRect,Rectangle close) {
//			super.paintTab(g, tabPlacement, rects, tabIndex, iconRect, textRect,close);
//			paintCloseIcon(g, tabIndex, nowIndex);
//			if (closable.get(tabIndex)
//					&& (showClose || tabIndex == tabPane.getSelectedIndex())) {
//			}
			
//		}
		
//		@Override
//	    protected void paintTabBorder(Graphics g, int tabPlacement,
//                int tabIndex, int x, int y, int w, int h, boolean isSelected ) {
//			g.setColor(tabPane.getBackground());
//	        switch (tabPlacement) {
//	          case LEFT:
//	              g.drawLine(x, y, x, y+h-1);
//	              g.drawLine(x, y, x+w-1, y);
//	              g.drawLine(x, y+h-1, x+w-1, y+h-1);
//	              break;
//	          case RIGHT:
//	              g.drawLine(x, y, x+w-1, y);
//	              g.drawLine(x, y+h-1, x+w-1, y+h-1);
//	              g.drawLine(x+w-1, y, x+w-1, y+h-1);
//	              break;              
//	          case BOTTOM:
//	        	  g.drawLine(x, y, x, y+h-1);
//	              g.drawLine(x+w-1, y, x+w-1, y+h-1);
//	              g.drawLine(x, y+h-1, x+w-1, y+h-1);
//	              break;
//	          case TOP:
//	          default:           
//	              g.drawLine(x, y, x, y+h-1);
//	              g.drawLine(x, y, x+w-1, y);
//	              g.drawLine(x+w-1, y, x+w-1, y+h-1);
//	        }
//	    }

//		@Override
//	    protected void paintFocusIndicator(Graphics g, int tabPlacement,
//	    	    Rectangle[] rects, int tabIndex, 
//	    	    Rectangle iconRect, Rectangle textRect,
//	    	    boolean isSelected) {}

//		@Override
//		protected void paintTabBackground(Graphics g, int tabPlacement,
//				int tabIndex, int x, int y, int w, int h, boolean isSelected) {
//			GradientPaint gradient;
//			Graphics2D g2d = (Graphics2D)g;
//			switch(tabPlacement) {
//			case LEFT:
//				if (isSelected) {
//					gradient = new GradientPaint(x+1, y, colorNorth, 
//							x+w, y, colorSouth, true);
//				} else {
//					gradient = new GradientPaint(x+1, y, Color.LIGHT_GRAY, 
//							x+w, y, Color.WHITE, true);
//				}
//				g2d.setPaint(gradient);
//				g.fillRect(x+1, y+1, w-1, h-2);
//				break;
//			case RIGHT:
//				if (isSelected) {
//					gradient = new GradientPaint(x+w, y, colorNorth, 
//							x+1, y, colorSouth, true);
//				} else {
//					gradient = new GradientPaint(x+w, y, Color.LIGHT_GRAY, 
//							x+1, y, Color.WHITE, true);
//				}
//				g2d.setPaint(gradient);
//				g.fillRect(x, y+1, w-1, h-2);
//				break;
//			case BOTTOM:
//				if (isSelected) {
//					gradient = new GradientPaint(x+1, y+h, colorNorth, 
//							x+1, y, colorSouth, true);
//				} else {
//					gradient = new GradientPaint(x+1, y+h, Color.LIGHT_GRAY, 
//							x+1, y, Color.WHITE, true);
//				}
//				g2d.setPaint(gradient);
//				g.fillRect(x+1, y, w-2, h-1);
//				break;
//			case TOP:
//			default:
//				if (isSelected) {
//					gradient = new GradientPaint(x+1, y, colorNorth, 
//							x+1, y+h, colorSouth, true);
//				} else {
//					gradient = new GradientPaint(x+1, y, Color.LIGHT_GRAY, 
//							x+1, y+h, Color.WHITE, true);
//				}
//				g2d.setPaint(gradient);
//				g2d.fillRect(x+1, y+1, w-2, h-1);
//			}
//		}

//		private void paintCloseIcon(Graphics g, int tabIndex,int nowIndex) {
//			Rectangle rect = closeRects[tabIndex];
//			rect.width=20;
//			rect.height=20;
//			int x = rect.x;
//			int y = rect.y;
//			LazyImageIcon close = new LazyImageIcon("icons/notification-close.gif");
//			LazyImageIcon closeActive = new LazyImageIcon("icons/notification-close-active.gif");
//			rect.width = close.getIconWidth();
//			rect.height = close.getIconHeight();
//			if(tabPane.getSelectedIndex() == tabIndex){
//				
//				if(isActive){
//					closeActive.paintIcon(tabPane.getTabComponentAt(tabIndex), g, x,y);
//				}
//				else{
//					close.paintIcon(tabPane.getTabComponentAt(tabIndex), g, x, y);
//				}
//			}else{
//				if(isActive){
//					closeActive.paintIcon(tabPane.getTabComponentAt(tabIndex), g, x,y);
//				}else
//				if (rolloverIndex==tabIndex&&(rolloverIndex >= 0) && (rolloverIndex < tabPane.getTabCount())) {
//					close.paintIcon(tabPane.getTabComponentAt(rolloverIndex), g, x, y);
//				}
//				
//			}
//		}



	

		
}
