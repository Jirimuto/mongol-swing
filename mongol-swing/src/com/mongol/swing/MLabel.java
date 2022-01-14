package com.mongol.swing;

import java.awt.Font;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JToolTip;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.LabelUI;

import com.mongol.swing.plaf.MComboBoxUI;
import com.mongol.swing.plaf.MLabelUI;
import com.mongol.swing.plaf.MRotation;

/**
 * Mongolian Encoding and vertical line supported Swing Label Component,
 * or both.
 * @author jrmt@Almas
 */
public class MLabel extends JLabel implements MRotation{

    public boolean selected = false;
    private MLabelUI labelUI;
    /**
     * @see #getUIClassID
     * @see #readObject
     */
	public MLabel() {
		super();
		if( labelUI == null )
			labelUI = new MLabelUI();
        super.setUI(labelUI);
	}
	
    public MLabel(String text, Icon icon, int horizontalAlignment) {
    	super(text, icon, horizontalAlignment);
		if( labelUI == null )
			labelUI = new MLabelUI();
        super.setUI(labelUI);
    }
    
    public MLabel(String text, int horizontalAlignment) {
    	super(text, horizontalAlignment);
		if( labelUI == null )
			labelUI = new MLabelUI();
        super.setUI(labelUI);
    }    
    
    public MLabel(String text) {
    	super(text);
		if( labelUI == null )
			labelUI = new MLabelUI();
        super.setUI(labelUI);
    }

    public MLabel(Icon image, int horizontalAlignment) {
    	super(image, horizontalAlignment);
		if( labelUI == null )
			labelUI = new MLabelUI();
        super.setUI(labelUI);
    }
    
    public MLabel(Icon image) {
    	super(image);
		if( labelUI == null )
			labelUI = new MLabelUI();
        super.setUI(labelUI);
    }
    
    public void setUI(LabelUI ui) {
        super.setUI(ui);
		if( labelUI== null )
			labelUI = new MLabelUI();
		if( !(ui instanceof MLabelUI) )
			super.setUI(labelUI);
    }
    
    public void setText(String text) {
    	super.setText(text);
    }
    
    public void setRotateHint(int hint){
    	LabelUI labelUI = getUI();
        if( labelUI instanceof MLabelUI ){
        	((MLabelUI)labelUI).setRotateHint(hint);
        }
    }

    public int getRotateHint(){
    	LabelUI labelUI = getUI();
        if( labelUI instanceof MLabelUI ){
        	return ((MLabelUI)labelUI).getRotateHint();
        }
        return MSwingRotateUtilities.ROTATE_DEFAULT;
    }

    public void setRotateDirection(int direction){
    	LabelUI labelUI = getUI();
        if( labelUI instanceof MLabelUI ){
        	((MLabelUI)labelUI).setRotateDirection(direction);
        }
    }

    public int getRotateDirection(){
    	LabelUI labelUI = getUI();
        if( labelUI instanceof MLabelUI ){
        	return ((MLabelUI)labelUI).getRotateDirection();
        }
        return MSwingRotateUtilities.ROTATE_HORIZANTAL;
    }

    /**
     * Returns the instance of <code>JToolTip</code> that should be used
     * to display the tooltip.
     * Components typically would not override this method,
     * but it can be used to
     * cause different tooltips to be displayed differently.
     *
     * @return the <code>JToolTip</code> used to display this toolTip
     */
    public JToolTip createToolTip() {
        MToolTip tip = new MToolTip();
        Font font = this.getFont();
        font = font.deriveFont(Font.PLAIN, font.getSize()-4 );
        tip.setFont(font);
        tip.setComponent(this);
        return tip;
    }

}
