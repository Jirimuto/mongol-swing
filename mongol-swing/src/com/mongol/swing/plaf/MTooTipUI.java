package com.mongol.swing.plaf;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.JToolTip;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.basic.BasicToolTipUI;
import javax.swing.text.TabExpander;
import javax.swing.text.View;

import com.mongol.swing.MSwingRotateUtilities;
import com.mongol.swing.MSwingUtilities;

public class MTooTipUI extends BasicToolTipUI implements TabExpander {
	
    public void paint(Graphics g, JComponent c) {
        Font font = c.getFont();
        FontMetrics metrics = MSwingUtilities.getFontMetrics(c, g, font);
        Dimension size = c.getSize();

        g.setColor(c.getForeground());
        // fix for bug 4153892
        String tipText = ((JToolTip)c).getTipText();
        if (tipText == null) {
            tipText = "";
        }

        Insets insets = c.getInsets();
        Rectangle paintTextR = new Rectangle(
            insets.left + 3,
            insets.top,
            size.width - (insets.left + insets.right) - 6,
            size.height - (insets.top + insets.bottom));
        View v = (View) c.getClientProperty(BasicHTML.propertyKey);
        if (v != null) {
            v.paint(g, paintTextR);
        } else {
            g.setFont(font);
            // MongolianConverter converter = new MongolianConverter();
            // String target = converter.convert(tipText);
            String target = tipText;
            // MSwingUtilities.drawString(c, g, target, paintTextR.x, paintTextR.y + metrics.getAscent());
            
		    MSwingUtilities.drawStringUnderlineCharAt(c, g, target, -1,
		    		paintTextR.x, paintTextR.y + metrics.getAscent(), 
		    		this, MSwingRotateUtilities.ROTATE_HORIZANTAL, MSwingRotateUtilities.ROTATE_DEFAULT);
            
        }
    }

    public float nextTabStop(float x, int tabOffset) {
        return x;
    }

}
