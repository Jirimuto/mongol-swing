package com.mongol.swing;

import java.awt.Component;
import java.awt.Dimension;
import java.util.Vector;

import javax.swing.ComboBoxEditor;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.ListCellRenderer;
import javax.swing.plaf.ButtonUI;
import javax.swing.plaf.ComboBoxUI;

import com.mongol.swing.plaf.MCheckBoxUI;
import com.mongol.swing.plaf.MComboBoxUI;
import com.mongol.swing.plaf.MRotation;

public class MComboBox extends JComboBox implements MRotation {
	
	private MComboBoxUI comboBoxUI;
    
    public MComboBox() {
        super();
        if( comboBoxUI == null )
        	comboBoxUI = new MComboBoxUI();
        super.setUI(comboBoxUI);
    }

    public MComboBox(ComboBoxModel aModel) {
        super(aModel);
        if( comboBoxUI == null )
        	comboBoxUI = new MComboBoxUI();
        super.setUI(comboBoxUI);
    }

    public MComboBox(final Object items[]) {
        super(items);
        if( comboBoxUI == null )
        	comboBoxUI = new MComboBoxUI();
        super.setUI(comboBoxUI);
    }

    public MComboBox(Vector<?> items) {
        super(items);
        if( comboBoxUI == null )
        	comboBoxUI = new MComboBoxUI();
        super.setUI(comboBoxUI);
    }

    public void setUI(ComboBoxUI ui) {
        super.setUI(ui);
		if( comboBoxUI== null )
			comboBoxUI = new MComboBoxUI();
		if( !(ui instanceof MComboBoxUI) )
			super.setUI(comboBoxUI);
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

    public void setPreferredSize( Dimension size ) {
        if( getRotateDirection() != MSwingRotateUtilities.ROTATE_HORIZANTAL ) {
	        int tmp = size.width;
	        size.width = size.height;
	        size.height = tmp;
        }
        super.setPreferredSize( size );
    }

    public void setRotateHint(int hint){
    	ComboBoxUI comboBoxUI = getUI();
        if( comboBoxUI instanceof MComboBoxUI ){
        	((MComboBoxUI)comboBoxUI).setRotateHint(hint);
        }
    	ListCellRenderer renderrer = getRenderer();
    	if( renderrer instanceof MDefaultListCellRenderer ) {
        	((MDefaultListCellRenderer)renderrer).setRotateHint(hint);
    	}
    	Component editor = getEditor().getEditorComponent();
    	if( editor instanceof MRotation ) {
        	((MRotation)editor).setRotateHint(hint);
    	}
    }

    public int getRotateHint(){
    	ComboBoxUI comboBoxUI = getUI();
        if( comboBoxUI instanceof MComboBoxUI ){
        	return ((MComboBoxUI)comboBoxUI).getRotateHint();
        }
        return MSwingRotateUtilities.ROTATE_DEFAULT;
    }

    public void setRotateDirection(int direction){
    	ComboBoxUI comboBoxUI = getUI();
        if( comboBoxUI instanceof MComboBoxUI ){
        	((MComboBoxUI)comboBoxUI).setRotateDirection(direction);
        }
    	ListCellRenderer renderrer = getRenderer();
    	if( renderrer instanceof MDefaultListCellRenderer ) {
        	((MDefaultListCellRenderer)renderrer).setRotateDirection(direction);
    	}
    	Component editor = getEditor().getEditorComponent();
    	if( editor instanceof MRotation ) {
        	((MRotation)editor).setRotateDirection(direction);
    	}
    		
    }

    public int getRotateDirection(){
    	ComboBoxUI comboBoxUI = getUI();
        if( comboBoxUI instanceof MComboBoxUI ){
        	return ((MComboBoxUI)comboBoxUI).getRotateDirection();
        }
        return MSwingRotateUtilities.ROTATE_HORIZANTAL;
    }

}
