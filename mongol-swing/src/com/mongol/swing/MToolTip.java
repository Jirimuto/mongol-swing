package com.mongol.swing;

import javax.swing.JToolTip;
import javax.swing.plaf.ToolTipUI;

import com.mongol.swing.plaf.MTooTipUI;

public class MToolTip extends JToolTip {

	private MTooTipUI toolTipUI;
    /** Creates a tool tip. */
    public MToolTip() {
    	super();
    	if( toolTipUI == null )
    		toolTipUI = new MTooTipUI();
        super.setUI(toolTipUI);
    }

    public void setUI(ToolTipUI ui) {
        super.setUI(ui);
		if( toolTipUI== null )
			toolTipUI = new MTooTipUI();
		if( !(ui instanceof MTooTipUI) )
			super.setUI(toolTipUI);
    }
    
}
