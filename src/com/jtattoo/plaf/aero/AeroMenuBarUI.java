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
 
package com.jtattoo.plaf.aero;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;

import javax.swing.JComponent;
import javax.swing.JMenuBar;
import javax.swing.plaf.ComponentUI;

import com.jtattoo.plaf.AbstractLookAndFeel;
import com.jtattoo.plaf.AbstractTheme;
import com.jtattoo.plaf.BaseMenuBarUI;
import com.jtattoo.plaf.JTattooUtilities;

/**
 * @author Michael Hagen
 */
public class AeroMenuBarUI extends BaseMenuBarUI {

    public static ComponentUI createUI(JComponent c) {
        return new AeroMenuBarUI();
    }

    public void installUI(JComponent c) {
        super.installUI(c);
        if ((c != null) && (c instanceof JMenuBar)) {
            ((JMenuBar) c).setBorder(AeroBorders.getMenuBarBorder());
        }
    }

    public void paint(Graphics g, JComponent c) {
        if (JTattooUtilities.isMac() || !AbstractLookAndFeel.getTheme().isBackgroundPatternOn()) {
            super.paint(g, c);
            g.setColor(Color.red);
        	g.fillRect(0, 0, c.getWidth(), c.getHeight());
        } else {
        	 
    		GradientPaint p = new GradientPaint(0,0,
    				Color.white, 0, c.getHeight(), c.getBackground());
    		Graphics2D g2 = (Graphics2D) g;
    		Paint oldPaint = g2.getPaint();
     
    		g2.setPaint(p);
    		g2.fillRect(0,0,c.getWidth(),c.getHeight());
     
    		g2.setPaint(oldPaint);
     
        }
    }
}