package com.mongol.swing.plaf;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonListener;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.metal.MetalButtonUI;
import javax.swing.text.TabExpander;
import javax.swing.text.View;

import sun.awt.AppContext;
import sun.swing.SwingUtilities2;

import com.mongol.swing.MSwingRotateUtilities;
import com.mongol.swing.MSwingUtilities;

public class MButtonUI extends MetalButtonUI implements TabExpander, MRotation{

    private int rotate_direction = MSwingRotateUtilities.ROTATE_HORIZANTAL;
    private int rotate_hint = MSwingRotateUtilities.ROTATE_DEFAULT;
    
    private static Rectangle viewRect = new Rectangle();
    private static Rectangle textRect = new Rectangle();
    private static Rectangle iconRect = new Rectangle();
    
    protected Color focusColor;
    protected Color selectColor;
    protected Color disabledTextColor;

    private static final Object METAL_BUTTON_UI_KEY = new Object();

    // ********************************
    //          Create PLAF
    // ********************************
    public static ComponentUI createUI(JComponent c) {
        AppContext appContext = AppContext.getAppContext();
        MButtonUI mButtonUI =
                (MButtonUI) appContext.get(METAL_BUTTON_UI_KEY);
        if (mButtonUI == null) {
            mButtonUI = new MButtonUI();
            appContext.put(METAL_BUTTON_UI_KEY, mButtonUI);
        }
        return mButtonUI;
    }

    // ********************************
    //          Install
    // ********************************
    public void installDefaults(AbstractButton b) {
        super.installDefaults(b);
    }

    public void uninstallDefaults(AbstractButton b) {
        super.uninstallDefaults(b);
    }

	public MButtonUI() {
		super();
	}

    // ********************************
    //         Create Listeners
    // ********************************
    protected BasicButtonListener createButtonListener(AbstractButton b) {
        return super.createButtonListener(b);
    }


    // ********************************
    //         Default Accessors
    // ********************************
    protected Color getSelectColor() {
        selectColor = UIManager.getColor(getPropertyPrefix() + "select");
        return selectColor;
    }

    protected Color getDisabledTextColor() {
        disabledTextColor = UIManager.getColor(getPropertyPrefix() +
                                               "disabledText");
        return disabledTextColor;
    }

    protected Color getFocusColor() {
        focusColor = UIManager.getColor(getPropertyPrefix() + "focus");
        return focusColor;
    }

    // ********************************
    //          Paint
    // ********************************
    protected void paintButtonPressed(Graphics g, AbstractButton b) {
        if ( b.isContentAreaFilled() ) {
            Dimension size = b.getSize();
            g.setColor(getSelectColor());
            g.fillRect(0, 0, size.width, size.height);
        }
    }

    // ********************************
    //          Paint Methods
    // ********************************

    public void paint(Graphics g, JComponent c) 
    {
        AbstractButton b = (AbstractButton) c;
        ButtonModel model = b.getModel();

        String text = layout(b, MSwingUtilities.getFontMetrics(b, g),
               b.getWidth(), b.getHeight());

        clearTextShiftOffset();

        // perform UI specific press action, e.g. Windows L&F shifts text
        if (model.isArmed() && model.isPressed()) {
            paintButtonPressed(g,b); 
        }

        // Paint the Icon
        if(b.getIcon() != null) { 
            paintIcon(g,c,iconRect);
        }

        if (text != null && !text.equals("")){
	    View v = (View) c.getClientProperty(BasicHTML.propertyKey);
	    if (v != null) {
	    	v.paint(g, textRect);
	    } else {
	    	paintText(g, b, textRect, text);
	    }
        }

        if (b.isFocusPainted() && b.hasFocus()) {
            // paint UI specific focus
            paintFocus(g,b,viewRect,textRect,iconRect);
            
        }
    }
    
    private String layout(AbstractButton b, FontMetrics fm,
            int width, int height) {
		Insets i = b.getInsets();
		
        if( rotate_direction == MSwingRotateUtilities.ROTATE_HORIZANTAL ) {
        	
			viewRect.x = i.left;
			viewRect.y = i.top;
			viewRect.width = width - (i.right + viewRect.x);
			viewRect.height = height - (i.bottom + viewRect.y);
			
			textRect.x = textRect.y = textRect.width = textRect.height = 0;
			iconRect.x = iconRect.y = iconRect.width = iconRect.height = 0;
			
			// layout the text and icon
			return MSwingUtilities.layoutCompoundLabel(
				b, fm, b.getText(), b.getIcon(), 
				b.getVerticalAlignment(), b.getHorizontalAlignment(),
				b.getVerticalTextPosition(), b.getHorizontalTextPosition(),
				viewRect, iconRect, textRect, 
				b.getText() == null ? 0 : b.getIconTextGap());
			
        } else {
        	
			viewRect.y = i.left;
			viewRect.x = i.top;
			viewRect.height = width - (i.right + viewRect.x);
			viewRect.width = height - (i.bottom + viewRect.y);
			
			textRect.x = textRect.y = textRect.width = textRect.height = 0;
			iconRect.x = iconRect.y = iconRect.width = iconRect.height = 0;
			
			// layout the text and icon
			return MSwingUtilities.layoutCompoundLabel(
				b, fm, b.getText(), b.getIcon(), 
				b.getVerticalAlignment(), b.getHorizontalAlignment(),
				b.getVerticalTextPosition(), b.getHorizontalTextPosition(),
				viewRect, iconRect, textRect, 
				b.getText() == null ? 0 : b.getIconTextGap());
			
        }
    }

    
    
    protected void paintFocus(Graphics g, AbstractButton b,
                              Rectangle viewRect, Rectangle textRect, Rectangle iconRect){

        Rectangle focusRect = new Rectangle();
        String text = b.getText();
        boolean isIcon = b.getIcon() != null;

        // If there is text
        if ( text != null && !text.equals( "" ) ) {
            if ( !isIcon ) {
                focusRect.setBounds( textRect );
            }
            else {
                focusRect.setBounds( iconRect.union( textRect ) );
            }
        }
        // If there is an icon and no text
        else if ( isIcon ) {
            focusRect.setBounds( iconRect );
        }

        if( rotate_direction == MSwingRotateUtilities.ROTATE_HORIZANTAL ) {
        	
	        g.setColor(getFocusColor());
	        g.drawRect((focusRect.x-1), (focusRect.y-1),
	                  focusRect.width+1, focusRect.height+1);
        } else {
        	
	        g.setColor(getFocusColor());
	        g.drawRect((focusRect.y-1), (focusRect.x-1),
	                  focusRect.height+1, focusRect.width+1);
        	
        }

    }


    protected void paintText(Graphics g, JComponent c, Rectangle textRect, String text) {
        AbstractButton b = (AbstractButton) c;
        ButtonModel model = b.getModel();
        
        if( rotate_direction == MSwingRotateUtilities.ROTATE_HORIZANTAL ) {
	        FontMetrics fm = MSwingUtilities.getFontMetrics(c, g);
	        int mnemIndex = b.getDisplayedMnemonicIndex();
	
	        /* Draw the Text */
	        if(model.isEnabled()) {
	            /*** paint the text normally */
	            g.setColor(b.getForeground());
	        }
	        else {
	            /*** paint the text disabled ***/
	            g.setColor(getDisabledTextColor());
	        }
	        // MongolianConverter converter = new MongolianConverter();
	        // String target = converter.convert(text);
	        String target = text;
	        
	        MSwingUtilities.drawStringUnderlineCharAt(c, g, target, mnemIndex,
	                                  textRect.x, textRect.y + fm.getAscent(), this, rotate_direction, rotate_hint);
        } else {
        	
            Insets insets = c.getInsets(new Insets(0, 0, 0, 0));
	        FontMetrics fm = MSwingUtilities.getFontMetrics(c, g);
	        int mnemIndex = b.getDisplayedMnemonicIndex();
	
	        /* Draw the Text */
	        if(model.isEnabled()) {
	            /*** paint the text normally */
	            g.setColor(b.getForeground());
	        }
	        else {
	            /*** paint the text disabled ***/
	            g.setColor(getDisabledTextColor());
	        }
	        // MongolianConverter converter = new MongolianConverter();
	        // String target = converter.convert(text);
	        String target = text;
	        
    		Graphics2D g2d = (Graphics2D)g;
    		AffineTransform transform = g2d.getTransform();
    		
	        ((Graphics2D) g).rotate(Math.PI * 1 / 2);
	        	        
	        MSwingUtilities.drawStringUnderlineCharAt(c, g, target, mnemIndex,
	                                  textRect.x, - textRect.y - fm.getDescent(), this, rotate_direction, rotate_hint);
        	
        	g2d.setTransform(transform);
        	
        }
    }

    public float nextTabStop(float x, int tabOffset) {
        return x;
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

}
