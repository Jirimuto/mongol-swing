package com.mongol.swing.plaf;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextAreaUI;
import javax.swing.text.AbstractDocument;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.View;

import com.mongol.swing.MSwingRotateUtilities;
import com.mongol.swing.text.MPlainView;
import com.mongol.swing.text.MWrappedPlainView;

public class MTextAreaUI extends BasicTextAreaUI  implements MRotation {
	
    private int rotate_direction = MSwingRotateUtilities.ROTATE_HORIZANTAL;
    private int rotate_hint = MSwingRotateUtilities.ROTATE_DEFAULT;
    private MRotation rotation_view = null;
    private View root_view = null;
    private boolean rootViewInitialized;
    transient JTextComponent editor;
    
    /**
     * Creates a UI for a JTextArea.
     *
     * @param ta a text area
     * @return the UI
     */
    public static ComponentUI createUI(JComponent ta) {
        return new MTextAreaUI();
    }

    /**
     * Constructs a new BasicTextAreaUI object.
     */
    public MTextAreaUI() {
        super();
    }


    public MTextAreaUI(int rotate) {
        super();
        setRotateHint(rotate);
    }

    /**
     * Creates the view for an element.  Returns a WrappedPlainView or
     * PlainView.
     *
     * @param elem the element
     * @return the view
     */
    public View create(Element elem) {
        Document doc = elem.getDocument();
        // TODO
//        Object i18nFlag = doc.getProperty("i18n"/*AbstractDocument.I18NProperty*/);
//        if ((i18nFlag != null) && i18nFlag.equals(Boolean.TRUE)) {
//            // build a view that support bidi
//            return super.create(elem);
//        } else {
            JTextComponent c = getComponent();
            editor = c;
            if (c instanceof JTextArea) {
                JTextArea area = (JTextArea) c;
                if (area.getLineWrap()) {
                	MWrappedPlainView mview = new MWrappedPlainView(elem, area.getWrapStyleWord());
                	rotation_view = mview;
                	rotation_view.setRotateDirection(rotate_direction);
                	rotation_view.setRotateHint(rotate_hint);
                	root_view = mview;
                } else {
                	MPlainView mview = new MPlainView(elem);
                	rotation_view = mview;
                	rotation_view.setRotateDirection(rotate_direction);
                	rotation_view.setRotateHint(rotate_hint);
                	root_view = mview;
                }
                return root_view;
            }
//        }
        return null;
    }


    /**
     * Returns the preferred size <code>Dimensions</code> needed for this
     * <code>TextArea</code>.  If a non-zero number of columns has been
     * set, the width is set to the columns multiplied by
     * the column width.
     *
     * @return the dimension 
     */
    public Dimension getPreferredSize(JComponent c) {
        Document doc = editor.getDocument();
        Insets i = c.getInsets();
        Dimension d = c.getSize();

        if (doc instanceof AbstractDocument) {
            ((AbstractDocument)doc).readLock();
        }
        try {
            if ((d.width > (i.left + i.right)) && (d.height > (i.top + i.bottom))) {
            	root_view.setSize(d.width - i.left - i.right
            				, d.height - i.top - i.bottom);
            }
            else if (!rootViewInitialized && (d.width <= 0 || d.height <= 0)) {
                // Probably haven't been layed out yet, force some sort of
                // initial sizing.
                rootViewInitialized = true;
                root_view.setSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
            }
            d.width = (int) Math.min((long) root_view.getPreferredSpan(View.X_AXIS) +
                         (long) i.left + (long) i.right, Integer.MAX_VALUE);
            d.height = (int) Math.min((long) root_view.getPreferredSpan(View.Y_AXIS) +
                                      (long) i.top + (long) i.bottom, Integer.MAX_VALUE);
        } finally {
            if (doc instanceof AbstractDocument) {
                ((AbstractDocument)doc).readUnlock();
            }
        }
        return d;
    }

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
    	JTextComponent editor = super.getComponent();
        Rectangle alloc = editor.getBounds();
        if ((alloc.width > 0) && (alloc.height > 0)) {
            alloc.x = alloc.y = 0;
            Insets insets = editor.getInsets();
            alloc.x += insets.left;
            alloc.y += insets.top;
            alloc.width -= insets.left + insets.right;
            alloc.height -= (insets.top + insets.bottom);
            return alloc;
        }
        return null;
    }

    public void setRotateHint(int hint) {
        this.rotate_hint = hint;
        if( rotation_view != null ){
        	rotation_view.setRotateHint(hint);
        }
    }

    public int getRotateHint(){
        return this.rotate_hint;
    }

    public void setRotateDirection(int direction){
        this.rotate_direction = direction;
        if( rotation_view != null ){
        	rotation_view.setRotateDirection(direction);
        }
    }

    public int getRotateDirection(){
        return this.rotate_direction;
    }

}
