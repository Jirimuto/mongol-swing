package com.mongol.swing;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.plaf.ButtonUI;
import javax.swing.plaf.TextUI;

import com.mongol.swing.plaf.MButtonUI;
import com.mongol.swing.plaf.MCheckBoxUI;
import com.mongol.swing.plaf.MRotation;

public class MCheckBox extends JCheckBox implements MRotation{

	private MCheckBoxUI buttonUI;
    
    /**
     * @see #getUIClassID
     * @see #readObject
     */
	public MCheckBox() {
		super();
		if( buttonUI == null )
			buttonUI = new MCheckBoxUI();
        super.setUI(buttonUI);
	}
		
	public MCheckBox(Icon icon) {
		super(icon);
		if( buttonUI == null )
			buttonUI = new MCheckBoxUI();
        super.setUI(buttonUI);
	}
	
	public MCheckBox(Icon icon, boolean selected) {
		super(icon, selected);
		if( buttonUI == null )
			buttonUI = new MCheckBoxUI();
        super.setUI(buttonUI);
	}
	
	public MCheckBox(String text) {
		super(text);
		if( buttonUI == null )
			buttonUI = new MCheckBoxUI();
        super.setUI(buttonUI);
	}
	
	public MCheckBox(String text, boolean selected) {
		super(text, selected);
		if( buttonUI == null )
			buttonUI = new MCheckBoxUI();
        super.setUI(buttonUI);
	}
	
	public MCheckBox(Action a) {
		super(a);
		if( buttonUI == null )
			buttonUI = new MCheckBoxUI();
        super.setUI(buttonUI);
	}
	
	public MCheckBox(String text, Icon icon) {
		super(text, icon);
		if( buttonUI == null )
			buttonUI = new MCheckBoxUI();
        super.setUI(buttonUI);
	}
	
	public MCheckBox(String text, Icon icon, boolean selected) {
		super(text, icon);
		if( buttonUI == null )
			buttonUI = new MCheckBoxUI();
        super.setUI(buttonUI);
	}
	
    public void setUI(ButtonUI ui) {
        super.setUI(ui);
		if( buttonUI== null )
			buttonUI = new MCheckBoxUI();
		if( !(ui instanceof MCheckBoxUI) )
			super.setUI(buttonUI);
    }
    
    public void setRotateHint(int hint){
    	ButtonUI buttonUI = getUI();
        if( buttonUI instanceof MCheckBoxUI ){
        	((MCheckBoxUI)buttonUI).setRotateHint(hint);
        }
    }

    public int getRotateHint(){
    	ButtonUI buttonUI = getUI();
        if( buttonUI instanceof MCheckBoxUI ){
        	return ((MCheckBoxUI)buttonUI).getRotateHint();
        }
        return MSwingRotateUtilities.ROTATE_DEFAULT;
    }

    public void setRotateDirection(int direction){
    	ButtonUI buttonUI = getUI();
        if( buttonUI instanceof MCheckBoxUI ){
        	((MCheckBoxUI)buttonUI).setRotateDirection(direction);
        }
    }

    public int getRotateDirection(){
    	ButtonUI buttonUI = getUI();
        if( buttonUI instanceof MCheckBoxUI ){
        	return ((MCheckBoxUI)buttonUI).getRotateDirection();
        }
        return MSwingRotateUtilities.ROTATE_HORIZANTAL;
    }

	
}
