package com.mongol.swing;

import java.awt.*;

import javax.swing.JTextField;
import javax.swing.plaf.TextUI;
import javax.swing.text.Document;

import com.mongol.swing.plaf.MListUI;
import com.mongol.swing.plaf.MRotation;
import com.mongol.swing.plaf.MTextFieldUI;

public class MTextField extends JTextField implements MRotation {
	
    private MTextFieldUI fieldUI;
    
    public MTextField() {
        super();
        if( fieldUI==null )
        	fieldUI = new MTextFieldUI();
        super.setUI(fieldUI);
    }

    public MTextField(String text) {
        super(text);
        if( fieldUI==null )
        	fieldUI = new MTextFieldUI();
        super.setUI(fieldUI);
    }

    public MTextField(int columns) {
        super(columns);
        if( fieldUI==null )
        	fieldUI = new MTextFieldUI();
        super.setUI(fieldUI);
    }

    public MTextField(String text, int columns) {
        super(text, columns);
        if( fieldUI==null )
        	fieldUI = new MTextFieldUI();
        super.setUI(fieldUI);
    }

    public MTextField(Document doc, String text, int columns) {
    	super(doc, text, columns);
        if( fieldUI==null )
        	fieldUI = new MTextFieldUI();
        super.setUI(fieldUI);
    }

    public void setUI(TextUI ui) {
        super.setUI(ui);
		if( fieldUI== null )
			fieldUI = new MTextFieldUI();
		if( !(ui instanceof MTextFieldUI) )
			super.setUI(fieldUI);
    }
    
    public void repaint(int x, int y, int width, int height) {
        super.repaint(0, 0, getWidth(), getHeight());
    }

    /**
     * Returns the preferred size <code>Dimensions</code> needed for this
     * <code>TextField</code>.  If a non-zero number of columns has been
     * set, the width is set to the columns multiplied by
     * the column width.
     *
     * @return the dimension of this textfield
     */
    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();
        if( getRotateDirection() != MSwingRotateUtilities.ROTATE_HORIZANTAL ) {
	        int tmp = size.width;
	        size.width = size.height;
	        size.height = tmp;
        }
        return size;
    }

    public void setRotateHint(int hint){
    	TextUI textUI = getUI();
        if( textUI instanceof MTextFieldUI ){
        	((MTextFieldUI)textUI).setRotateHint(hint);
        }
    }

    public int getRotateHint(){
    	TextUI textUI = getUI();
        if( textUI instanceof MTextFieldUI ){
        	return ((MTextFieldUI)textUI).getRotateHint();
        }
        return MSwingRotateUtilities.ROTATE_DEFAULT;
    }

    public void setRotateDirection(int direction){
    	TextUI textUI = getUI();
        if( textUI instanceof MTextFieldUI ){
        	((MTextFieldUI)textUI).setRotateDirection(direction);
        }
    }

    public int getRotateDirection(){
    	TextUI textUI = getUI();
        if( textUI instanceof MTextFieldUI ){
        	return ((MTextFieldUI)textUI).getRotateDirection();
        }
        return MSwingRotateUtilities.ROTATE_HORIZANTAL;
    }

}
