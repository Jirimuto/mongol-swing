package com.mongol.swing.plaf;

import java.awt.*;

import javax.swing.*;
import javax.swing.plaf.basic.BasicCheckBoxUI;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.text.TabExpander;
import javax.swing.text.View;

import sun.swing.SwingUtilities2;

import com.mongol.swing.MSwingRotateUtilities;
import com.mongol.swing.MSwingUtilities;

public class MCheckBoxUI extends BasicCheckBoxUI  implements TabExpander, MRotation {
	
    private static Dimension size = new Dimension();
    private static Rectangle viewRect = new Rectangle();
    private static Rectangle iconRect = new Rectangle();
    private static Rectangle textRect = new Rectangle();

    private int rotate_direction = MSwingRotateUtilities.ROTATE_HORIZANTAL;
    private int rotate_hint = MSwingRotateUtilities.ROTATE_DEFAULT;
    
    
    public MCheckBoxUI() {
        super();
    }

    public MCheckBoxUI(int rotate) {
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
     * paint the radio button
     */
    public synchronized void paint(Graphics g, JComponent c) {
        AbstractButton b = (AbstractButton) c;
        ButtonModel model = b.getModel();

        Font f = c.getFont();
        g.setFont(f);
        FontMetrics fm = SwingUtilities2.getFontMetrics(c, g, f);

        Insets i = c.getInsets();

        if( rotate_direction == MSwingRotateUtilities.ROTATE_HORIZANTAL ) {
	        size = b.getSize(size);
	        viewRect.x = i.left;
	        viewRect.y = i.top;
	        viewRect.width = size.width - (i.right + viewRect.x);
	        viewRect.height = size.height - (i.bottom + viewRect.y);
        } else {
	        size = b.getSize(size);
	        viewRect.x = i.top;
	        viewRect.y = i.left;
	        viewRect.height = size.width - (i.right + viewRect.x);
	        viewRect.width = size.height - (i.bottom + viewRect.y);
        }
        iconRect.x = iconRect.y = iconRect.width = iconRect.height = 0;
        textRect.x = textRect.y = textRect.width = textRect.height = 0;
        
        Icon altIcon = b.getIcon();
        Icon selectedIcon = null;
        Icon disabledIcon = null;

        String text = SwingUtilities.layoutCompoundLabel(
            c, fm, b.getText(), altIcon != null ? altIcon : getDefaultIcon(),
            b.getVerticalAlignment(), b.getHorizontalAlignment(),
            b.getVerticalTextPosition(), b.getHorizontalTextPosition(),
            viewRect, iconRect, textRect,
	    b.getText() == null ? 0 : b.getIconTextGap());

        // fill background
        if(c.isOpaque()) {
            g.setColor(b.getBackground());
            g.fillRect(0,0, size.width, size.height); 
        }


        // Paint the radio button
        if(altIcon != null) { 

            if(!model.isEnabled()) {
	        if(model.isSelected()) {
                   altIcon = b.getDisabledSelectedIcon();
		} else {
                   altIcon = b.getDisabledIcon();
		}
            } else if(model.isPressed() && model.isArmed()) {
                altIcon = b.getPressedIcon();
                if(altIcon == null) {
                    // Use selected icon
                    altIcon = b.getSelectedIcon();
                } 
            } else if(model.isSelected()) {
                if(b.isRolloverEnabled() && model.isRollover()) {
                        altIcon = (Icon) b.getRolloverSelectedIcon();
                        if (altIcon == null) {
                                altIcon = (Icon) b.getSelectedIcon();
                        }
                } else {
                        altIcon = (Icon) b.getSelectedIcon();
                }
            } else if(b.isRolloverEnabled() && model.isRollover()) {
                altIcon = (Icon) b.getRolloverIcon();
            } 

            if(altIcon == null) {
                altIcon = b.getIcon();
            }

            altIcon.paintIcon(c, g, iconRect.x, iconRect.y);

        } else {
            getDefaultIcon().paintIcon(c, g, iconRect.x, iconRect.y);
        }


        // Draw the Text
        if(text != null) {
            View v = (View) c.getClientProperty(BasicHTML.propertyKey);
            if (v != null) {
            	v.paint(g, textRect);
            } else {
            	paintText(g, b, textRect, text);
            }
		    if(b.hasFocus() && b.isFocusPainted() && 
		    		textRect.width > 0 && textRect.height > 0 ) {
		    	paintFocus(g, textRect, size);
		    }
        }
    }

    protected void paintText(Graphics g, AbstractButton b, Rectangle textRect, String text) {
    	paintText(g, (JComponent)b, textRect, text);
    }

    /**
     * As of Java 2 platform v 1.4 this method should not be used or overriden.
     * Use the paintText method which takes the AbstractButton argument.
     */
    protected void paintText(Graphics g, JComponent c, Rectangle textRect, String text) {
        AbstractButton b = (AbstractButton) c;                       
        ButtonModel model = b.getModel();
        FontMetrics fm = SwingUtilities2.getFontMetrics(c, g);
        int mnemonicIndex = b.getDisplayedMnemonicIndex();

        // MongolianConverter converter = new MongolianConverter();
        // String target = converter.convert(text);
        String target = text;
        
        if( rotate_direction == MSwingRotateUtilities.ROTATE_HORIZANTAL ) {
			/* Draw the Text */
			if(model.isEnabled()) {
			    /*** paint the text normally */
			    g.setColor(b.getForeground());
			    MSwingUtilities.drawStringUnderlineCharAt(c, g, target, mnemonicIndex,
							  textRect.x + getTextShiftOffset(),
							  textRect.y + fm.getAscent() + getTextShiftOffset(), this, rotate_direction, rotate_hint);
			}
			else {
			    /*** paint the text disabled ***/
			    g.setColor(b.getBackground().brighter());
			    MSwingUtilities.drawStringUnderlineCharAt(c, g, target, mnemonicIndex,
							  textRect.x, textRect.y + fm.getAscent(), this, rotate_direction, rotate_hint);
			    g.setColor(b.getBackground().darker());
			    MSwingUtilities.drawStringUnderlineCharAt(c, g, target, mnemonicIndex,
							  textRect.x - 1, textRect.y + fm.getAscent() - 1, this, rotate_direction, rotate_hint);
			}
        } else {
        	
	        ((Graphics2D) g).rotate(Math.PI * 1 / 2);
	        
	        // int temp = textRect.x;
	        // textRect.x = textRect.y;
	        // textRect.y = temp;
	        // temp = textRect.width;
	        // textRect.width = textRect.height;
	        // textRect.height = temp;
	        
			/* Draw the Text */
			if(model.isEnabled()) {
			    /*** paint the text normally */
			    g.setColor(b.getForeground());
			    MSwingUtilities.drawStringUnderlineCharAt(c, g, target, mnemonicIndex,
							  textRect.x + getTextShiftOffset()  + (int)iconRect.getWidth(),
							  -(textRect.y - fm.getDescent()), 
							  this, rotate_direction, rotate_hint);
			}
			else {
			    /*** paint the text disabled ***/
			    g.setColor(b.getBackground().brighter());
			    MSwingUtilities.drawStringUnderlineCharAt(c, g, target, mnemonicIndex,
							  textRect.x + getTextShiftOffset() + (int)iconRect.getWidth(), -(textRect.y - fm.getDescent()), 
							  this, rotate_direction, rotate_hint);
			    g.setColor(b.getBackground().darker());
			    MSwingUtilities.drawStringUnderlineCharAt(c, g, target, mnemonicIndex,
							  textRect.x + getTextShiftOffset() - 1 + (int)iconRect.getWidth(), -(textRect.y - fm.getDescent() - 1), 
							  this, rotate_direction, rotate_hint);
			}
        	
        }
    }
    
    
    private static Rectangle prefViewRect = new Rectangle();
    private static Rectangle prefIconRect = new Rectangle();
    private static Rectangle prefTextRect = new Rectangle();
    private static Insets prefInsets = new Insets(0, 0, 0, 0);

    /**
     * The preferred size of the radio button
     */
    public Dimension getPreferredSize(JComponent c) {
        if(c.getComponentCount() > 0) {
            return null;
        }

        AbstractButton b = (AbstractButton) c;

        String text = b.getText();

        Icon buttonIcon = (Icon) b.getIcon();
        if(buttonIcon == null) {
            buttonIcon = getDefaultIcon();
        }

        Font font = b.getFont();
        FontMetrics fm = b.getFontMetrics(font);

        prefViewRect.x = prefViewRect.y = 0;
        prefViewRect.width = Short.MAX_VALUE;
        prefViewRect.height = Short.MAX_VALUE;
        prefIconRect.x = prefIconRect.y = prefIconRect.width = prefIconRect.height = 0;
        prefTextRect.x = prefTextRect.y = prefTextRect.width = prefTextRect.height = 0;

        SwingUtilities.layoutCompoundLabel(
            c, fm, text, buttonIcon,
            b.getVerticalAlignment(), b.getHorizontalAlignment(),
            b.getVerticalTextPosition(), b.getHorizontalTextPosition(),
            prefViewRect, prefIconRect, prefTextRect, 
            text == null ? 0 : b.getIconTextGap());

        // find the union of the icon and text rects (from Rectangle.java)
        int x1 = Math.min(prefIconRect.x, prefTextRect.x);
        int x2 = Math.max(prefIconRect.x + prefIconRect.width, 
                          prefTextRect.x + prefTextRect.width);
        int y1 = Math.min(prefIconRect.y, prefTextRect.y);
        int y2 = Math.max(prefIconRect.y + prefIconRect.height, 
                          prefTextRect.y + prefTextRect.height);
        
        Dimension size = new Dimension(0, 0);
        if( rotate_direction == MSwingRotateUtilities.ROTATE_HORIZANTAL ) {
	        
            int width = x2 - x1;
            int height = y2 - y1;

	        prefInsets = b.getInsets(prefInsets);
	        width += prefInsets.left + prefInsets.right;
	        height += prefInsets.top + prefInsets.bottom;
	        size = new Dimension(width, height);
	        
        } else {
        	
            int width = y2 - y1;
            int height = x2 - x1;

	        prefInsets = b.getInsets(prefInsets);
	        width +=  prefInsets.top + prefInsets.bottom;
	        height += prefInsets.left + prefInsets.right;
	        size = new Dimension(width, height);
        	
        }
        
        return size;
    }

    public float nextTabStop(float x, int tabOffset) {
        return x;
    }

}
