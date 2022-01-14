package com.mongol.swing.plaf;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.basic.BasicLabelUI;
import javax.swing.text.TabExpander;
import javax.swing.text.View;

import com.mongol.swing.MSwingRotateUtilities;
import com.mongol.swing.MSwingUtilities;

public class MLabelUI extends BasicLabelUI implements TabExpander, MRotation {

	private Rectangle paintIconR = new Rectangle();
    private Rectangle paintTextR = new Rectangle();
    private Rectangle paintViewR = new Rectangle();

    private int rotate_direction = MSwingRotateUtilities.ROTATE_HORIZANTAL;
    private int rotate_hint = MSwingRotateUtilities.ROTATE_DEFAULT;
    
    public MLabelUI() {
        super();
    }

    public MLabelUI(int rotate) {
        super();
        setRotateHint(rotate);
    }

    public void setRotateHint(int hint) {
        this.rotate_hint = hint;
    }

    public int getRotateHint(){
        return this.rotate_hint;
    }

    public void setRotateDirection(int direction){
        this.rotate_direction = direction;
    }

    public int getRotateDirection(){
        return this.rotate_direction;
    }

    /**
     * Paint clippedText at textX, textY with the labels foreground color.
     *
     * @see #paint
     * @see #paintDisabledText
     */
    protected void paintEnabledText(JLabel l, Graphics g, String s, int textX, int textY)
    {
        int mnemIndex = l.getDisplayedMnemonicIndex();
        g.setColor(l.getForeground());
        
        // MongolianConverter converter = new MongolianConverter();
        // String t = converter.convert(s);
        String t = s;
        
        MSwingUtilities.drawStringUnderlineCharAt(l, g, t, mnemIndex,
                                                     textX, textY, this, rotate_direction, rotate_hint);
        
    }


    /**
     * Paint clippedText at textX, textY with background.lighter() and then
     * shifted down and to the right by one pixel with background.darker().
     *
     * @see #paint
     * @see #paintEnabledText
     */
    protected void paintDisabledText(JLabel l, Graphics g, String s, int textX, int textY)
    {
        int accChar = l.getDisplayedMnemonicIndex();
        Color background = l.getBackground();
        g.setColor(background.brighter());
        // MongolianConverter converter = new MongolianConverter();
        // String t = converter.convert(s);
        String t = s;
        
        MSwingUtilities.drawStringUnderlineCharAt(l, g, t, accChar,
                                                   textX + 1, textY + 1, this, rotate_direction, rotate_hint);
        g.setColor(background.darker());
        MSwingUtilities.drawStringUnderlineCharAt(l, g, t, accChar,
                                                   textX, textY, this, rotate_direction, rotate_hint);
    }

    /**
     * Paints the label text with the foreground color, if the label is opaque
     * then paints the entire background with the background color. The Label
     * text is drawn by {@link #paintEnabledText} or {@link #paintDisabledText}.
     * The locations of the label parts are computed by {@link #layoutCL}.
     *
     * @see #paintEnabledText
     * @see #paintDisabledText
     * @see #layoutCL
     */
    public void paint(Graphics g, JComponent c)
    {
        JLabel label = (JLabel)c;
        String text = label.getText();
        Icon icon = (label.isEnabled()) ? label.getIcon() : label.getDisabledIcon();

        if ((icon == null) && (text == null)) {
            return;
        }

        if( rotate_direction == MSwingRotateUtilities.ROTATE_HORIZANTAL ) {
	        FontMetrics fm = MSwingUtilities.getFontMetrics(label, g);
	        String clippedText = layout(label, fm, c.getWidth(), c.getHeight());
	
	        if (icon != null) {
	            icon.paintIcon(c, g, paintIconR.x, paintIconR.y);
	        }
	
	        if (text != null) {
	            View v = (View) c.getClientProperty(BasicHTML.propertyKey);
	            if (v != null) {
	                v.paint(g, paintTextR);
	            } else {
	                int textX = paintTextR.x;
	                int textY = paintTextR.y + fm.getAscent();
	
	                if (label.isEnabled()) {
	                    paintEnabledText(label, g, clippedText, textX, textY);
	                }
	                else {
	                    paintDisabledText(label, g, clippedText, textX, textY);
	                }
	            }
	        }
	        
        } else {
        	
            Insets insets = c.getInsets(new Insets(0, 0, 0, 0));
            // MongolianConverter converter = new MongolianConverter();
            // String target = converter.convert(text);
            String target = text;
            
    		paintViewR = new Rectangle();

            paintViewR.y = insets.left;
            paintViewR.x = insets.top;
            paintViewR.height = c.getWidth() - (insets.left + insets.right);
            paintViewR.width = c.getHeight() - (insets.top + insets.bottom);

            paintIconR.x = paintIconR.y = paintIconR.width = paintIconR.height = 0;
            paintTextR.x = paintTextR.y = paintTextR.width = paintTextR.height = 0;

	        FontMetrics fm = MSwingUtilities.getFontMetrics(label, g);
	        String clippedText = layoutCL(label, fm, target, icon, paintViewR, paintIconR, paintTextR);

	        if (icon != null) {
	            icon.paintIcon(c, g, paintIconR.y, paintIconR.x);
	        }
	        
	        Graphics2D g2d = (Graphics2D)g;
	        Shape clip = g.getClip();
	        AffineTransform transform = g2d.getTransform();
	        
	        g2d.rotate(Math.PI * 1 / 2);
	        
	        Shape clip1 = g2d.getClip();
	        
	        if (text != null) {
	            View v = (View) c.getClientProperty(BasicHTML.propertyKey);
	            if (v != null) {
	                v.paint(g, paintTextR);
	            } else {
	                int textX = paintTextR.x;
	                int textY = -paintTextR.y-fm.getDescent();

	                if (label.isEnabled()) {
	                    paintEnabledText(label, g, clippedText, textX, textY);
	                }
	                else {
	                    paintDisabledText(label, g, clippedText, textX, textY);
	                }
	            }
	        }
	        
	        g2d.setTransform(transform);

        }
    }
	
    private String layout(JLabel label, FontMetrics fm,
            int width, int height) {
		Insets insets = label.getInsets(null);
		String text = label.getText();
        // MongolianConverter converter = new MongolianConverter();
        // String target = converter.convert(text);
        String target = text;
        		
		Icon icon = (label.isEnabled()) ? label.getIcon() :
		                            label.getDisabledIcon();
		Rectangle paintViewR = new Rectangle();
		paintViewR.x = insets.left;
		paintViewR.y = insets.top;
		paintViewR.width = width - (insets.left + insets.right);
		paintViewR.height = height - (insets.top + insets.bottom);
		paintIconR.x = paintIconR.y = paintIconR.width = paintIconR.height = 0;
		paintTextR.x = paintTextR.y = paintTextR.width = paintTextR.height = 0;
		return layoutCL(label, fm, target, icon, paintViewR, paintIconR,
		          paintTextR);
    }

    public Dimension getPreferredSize(JComponent c)
    {
        JLabel label = (JLabel)c;
        String text = label.getText();
        
        // MongolianConverter converter = new MongolianConverter();
        // String target = converter.convert(text);
        String target = text;
        		
        Icon icon = (label.isEnabled()) ? label.getIcon() :
                                          label.getDisabledIcon();
        Insets insets = label.getInsets(null);
        Font font = label.getFont();

        if( rotate_direction == MSwingRotateUtilities.ROTATE_HORIZANTAL ) {
	        int dx = insets.left + insets.right;
	        int dy = insets.top + insets.bottom;
	
	        if ((icon == null) &&
	            ((target == null) ||
	             ((target != null) && (font == null)))) {
	            return new Dimension(dx, dy);
	        }
	        else if ((target == null) || ((icon != null) && (font == null))) {
	            return new Dimension(icon.getIconWidth() + dx,
	                                 icon.getIconHeight() + dy);
	        }
	        else {
	            FontMetrics fm = label.getFontMetrics(font);
	            Rectangle iconR = new Rectangle();
	            Rectangle textR = new Rectangle();
	            Rectangle viewR = new Rectangle();
	
	            iconR.x = iconR.y = iconR.width = iconR.height = 0;
	            textR.x = textR.y = textR.width = textR.height = 0;
	            viewR.x = dx;
	            viewR.y = dy;
	            viewR.width = viewR.height = Short.MAX_VALUE;
	
	            layoutCL(label, fm, target, icon, viewR, iconR, textR);
	            int x1 = Math.min(iconR.x, textR.x);
	            int x2 = Math.max(iconR.x + iconR.width, textR.x + textR.width);
	            int y1 = Math.min(iconR.y, textR.y);
	            int y2 = Math.max(iconR.y + iconR.height, textR.y + textR.height);
	            Dimension rv = new Dimension(x2 - x1, y2 - y1);
	
	            rv.width += dx;
	            rv.height += dy;
	            return rv;
	        }
        } else {
	        int dx = insets.left + insets.right;
	        int dy = insets.top + insets.bottom;
	
	        if ((icon == null) &&
	            ((target == null) ||
	             ((target != null) && (font == null)))) {
	            return new Dimension(dy, dx);
	        }
	        else if ((target == null) || ((icon != null) && (font == null))) {
	            return new Dimension(icon.getIconWidth() + dy,
	                                 icon.getIconHeight() + dx);
	        }
	        else {
	            FontMetrics fm = label.getFontMetrics(font);
	            Rectangle iconR = new Rectangle();
	            Rectangle textR = new Rectangle();
	            Rectangle viewR = new Rectangle();
	
	            iconR.x = iconR.y = iconR.width = iconR.height = 0;
	            textR.x = textR.y = textR.width = textR.height = 0;
	            viewR.x = dy;
	            viewR.y = dx;
	            viewR.width = viewR.height = Short.MAX_VALUE;
	
	            layoutCL(label, fm, target, icon, viewR, iconR, textR);
	            int y1 = Math.min(iconR.x, textR.x);
	            int y2 = Math.max(iconR.x + iconR.width, textR.x + textR.width);
	            int x1 = Math.min(iconR.y, textR.y);
	            int x2 = Math.max(iconR.y + iconR.height, textR.y + textR.height);
	            Dimension rv = new Dimension(x2 - x1, y2 - y1);
	
	            rv.width += dy;
	            rv.height += dx;
	            return rv;
	        }
        }
    }

    public float nextTabStop(float x, int tabOffset) {
            return x;
    }
    
}
