package com.mongol.swing.plaf;

import java.awt.*;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextFieldUI;

import com.mongol.swing.text.*;
import com.mongol.swing.MSwingRotateUtilities;
import com.mongol.swing.border.RoundBorder;

public class MTextFieldUI extends BasicTextFieldUI implements MRotation {

    private int rotate_direction = MSwingRotateUtilities.ROTATE_HORIZANTAL;
    private int rotate_hint = MSwingRotateUtilities.ROTATE_DEFAULT;
    private MView view = null;
    
    /**
     * Creates a UI for a JTextField.
     *
     * @param c the text field
     * @return the UI
     */
    public static ComponentUI createUI(JComponent c) {
        return new MTextFieldUI();
    }

    public MTextFieldUI() {
        super();
    }
    
    public void installUI(JComponent c) {
    	super.installUI(c);
    }
    
    protected void paintSafely(Graphics g) {
    	
    	JTextComponent editor = getComponent();
    	if(!editor.isOpaque()) {
    		Graphics2D g2d = (Graphics2D)g.create();
    		g2d.setColor(UIManager.getColor("TextField.background"));
    		Border border = this.getComponent().getBorder();
    		if( rotate_direction == MSwingRotateUtilities.ROTATE_HORIZANTAL && border instanceof RoundBorder ) {
        		g2d.fillRoundRect(0, 0, editor.getWidth()-1, editor.getHeight()-1, editor.getHeight(),  editor.getHeight());
    		}
    		g2d.dispose();
    	}
    	
    	if( rotate_direction == MSwingRotateUtilities.ROTATE_VERTICAL ) {
    		
    		super.paintSafely(g);
    		
    	} else {
    		
    		super.paintSafely(g);
    		
    	}
    }
    
    // Jirimuto added --start 2020/07/17
    /**
     * Gets the allocation to give the root View.  Due
     * to an unfortunate set of historical events this
     * method is inappropriately named.  The Rectangle
     * returned has nothing to do with visibility.
     * The component must have a non-zero positive size for
     * this translation to be computed.
     *
     * @return the bounding box for the root view
     */
    protected Rectangle getVisibleEditorRect() {
    	JTextComponent editor = (JTextComponent)view.getContainer();
    	if( editor != null ) {
	        Rectangle alloc = editor.getBounds();
	        if( alloc!=null ) {
		        if ((alloc.width > 0) && (alloc.height > 0)) {
		            alloc.x = alloc.y = 0;
		            
		            Insets insets = editor.getInsets();
		            alloc.x += insets.left;
		            alloc.y += insets.top;
		            alloc.width -= insets.left + insets.right;
		            alloc.height -= insets.top + insets.bottom;
		            return alloc;
		        }
	        }
    	}
        return null;
    }
    // Jirimuto added --end 2020/07/17

    /**
     * Creates a view (FieldView) based on an element.
     *
     * @param elem the element
     * @return the view
     */
    public View create(Element elem) {
        Document doc = elem.getDocument();
        
        Object i18nFlag = doc.getProperty("i18n"/*AbstractDocument.I18NProperty*/);
        if (Boolean.TRUE.equals(i18nFlag)) {
        	
            // To support bidirectional text, we build a more heavyweight
            // representation of the field.
            String kind = elem.getName();
            
            if (kind != null) {
                if (kind.equals(AbstractDocument.ContentElementName)) {
                    view = new MGlyphView(elem){
                        @Override
                        public float getMinimumSpan(int axis) {
                            // no wrap
                            return getPreferredSpan(axis);
                        }
                    };
                    return view;
                } else if (kind.equals(AbstractDocument.ParagraphElementName)) {
                    // return new I18nFieldView(elem);
                }
            }
            // this shouldn't happen, should probably throw in this case.
        }
        view = new MFieldView(elem);
        view.setRotateDirection(rotate_direction);
        view.setRotateHint(rotate_hint);
        return view;
    }

    /**
     * {@inheritDoc}
     */
    public int getNextVisualPositionFrom(JTextComponent t, int pos,
                    Position.Bias b, int direction, Position.Bias[] biasRet)
                    throws BadLocationException{
    	int dot = super.getNextVisualPositionFrom(t, pos, b, direction, biasRet);
    	
        return dot;
    }

    public MTextFieldUI(int rotate) {
        super();
        setRotateHint(rotate);
    }

    public void setRotateHint(int hint) {
        this.rotate_hint = hint;
        if( view != null ){
        	view.setRotateHint(hint);
        }
    }

    public int getRotateHint(){
        return this.rotate_hint;
    }

    public void setRotateDirection(int direction){
        this.rotate_direction = direction;
        if( view != null ){
        	view.setRotateDirection(direction);
        }
    }

    public int getRotateDirection(){
        return this.rotate_direction;
    }

}
