package com.mongol.swing;

import javax.swing.*;
import javax.swing.plaf.*;

import com.mongol.swing.plaf.MButtonUI;
import com.mongol.swing.plaf.MRotation;

public class MButton extends JButton implements MRotation{

	private MButtonUI buttonUI;
    
	public MButton() {
		super();
		if( buttonUI== null )
			buttonUI = new MButtonUI();
        super.setUI(buttonUI);
	}

	public MButton(Icon icon) {
		super(icon);
		if( buttonUI== null )
			buttonUI = new MButtonUI();
        super.setUI(buttonUI);
	}

	public MButton(String text) {
		super(text);
		if( buttonUI== null )
			buttonUI = new MButtonUI();
        super.setUI(buttonUI);
	}

	public MButton(Action a) {
		super(a);
		if( buttonUI== null )
			buttonUI = new MButtonUI();
        super.setUI(buttonUI);
	}

	public MButton(String text, Icon icon) {
		super(text, icon);
		if( buttonUI== null )
			buttonUI = new MButtonUI();
        super.setUI(buttonUI);
	}
	
    public void setUI(ButtonUI ui) {
        super.setUI(ui);
		if( buttonUI== null )
			buttonUI = new MButtonUI();
		if( !(ui instanceof MButtonUI) )
			super.setUI(buttonUI);
    }

    public void setRotateHint(int hint){
    	ButtonUI buttonUI = getUI();
        if( buttonUI instanceof MButtonUI ){
        	((MButtonUI)buttonUI).setRotateHint(hint);
        }
    }

    public int getRotateHint(){
    	ButtonUI buttonUI = getUI();
        if( buttonUI instanceof MButtonUI ){
        	return ((MButtonUI)buttonUI).getRotateHint();
        }
        return MSwingRotateUtilities.ROTATE_DEFAULT;
    }

    public void setRotateDirection(int direction){
    	ButtonUI buttonUI = getUI();
        if( buttonUI instanceof MButtonUI ){
        	((MButtonUI)buttonUI).setRotateDirection(direction);
        }
    }

    public int getRotateDirection(){
    	ButtonUI buttonUI = getUI();
        if( buttonUI instanceof MButtonUI ){
        	return ((MButtonUI)buttonUI).getRotateDirection();
        }
        return MSwingRotateUtilities.ROTATE_HORIZANTAL;
    }

   
}
