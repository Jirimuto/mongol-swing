package com.mongol.swing;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.IOException;
import java.net.URL;

import javax.swing.CellRendererPane;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JViewport;
import javax.swing.plaf.TextUI;
import javax.swing.text.EditorKit;

import com.mongol.swing.plaf.MRotation;
import com.mongol.swing.text.MStyledEditorKit;

public class MEditorPane extends JEditorPane implements MRotation {

	public MEditorPane() {
		super();
        setEditorKit(new MStyledEditorKit());
	}

	public MEditorPane(int direction, int hint) {
		super();
		MStyledEditorKit editorKit = new MStyledEditorKit();																																																																																																																																																																																																																																																																																																																																																				
		editorKit.setRotateDirection(direction);
		editorKit.setRotateHint(hint);
        setEditorKit(editorKit);
	}

	public MEditorPane(URL initialPage) throws IOException {
		super(initialPage);
        setEditorKit(new MStyledEditorKit());
	}

	public MEditorPane(String url) throws IOException {
		super(url);
        setEditorKit(new MStyledEditorKit());
	}

	public MEditorPane(String type, String text) {
		super(type, text);
        setEditorKit(new MStyledEditorKit());
	}

    public void setRotateHint(int hint){
    	EditorKit editorKit = getEditorKit();
        if( editorKit instanceof MStyledEditorKit ){
        	((MStyledEditorKit)editorKit).setRotateHint(hint);
            setEditorKit( editorKit );
        }
    }

    public int getRotateHint(){
    	EditorKit editorKit = getEditorKit();
        if( editorKit instanceof MStyledEditorKit ){
        	return ((MStyledEditorKit)editorKit).getRotateHint();
        }
        return MSwingRotateUtilities.ROTATE_DEFAULT;
    }

    public void setRotateDirection(int direction){
    	EditorKit editorKit = getEditorKit();
        if( editorKit instanceof MStyledEditorKit ){
        	((MStyledEditorKit)editorKit).setRotateDirection(direction);
            setEditorKit( editorKit );
        }
    }

    public int getRotateDirection(){
    	EditorKit editorKit = getEditorKit();
        if( editorKit instanceof MStyledEditorKit ){
        	return ((MStyledEditorKit)editorKit).getRotateDirection();
        }
        return MSwingRotateUtilities.ROTATE_HORIZANTAL;
    }

    /**
     * Returns the preferred size for the <code>JEditorPane</code>.
     * The preferred size for <code>JEditorPane</code> is slightly altered
     * from the preferred size of the superclass.  If the size
     * of the viewport has become smaller than the minimum size
     * of the component, the scrollable definition for tracking
     * width or height will turn to false.  The default viewport
     * layout will give the preferred size, and that is not desired
     * in the case where the scrollable is tracking.  In that case
     * the <em>normal</em> preferred size is adjusted to the
     * minimum size.  This allows things like HTML tables to
     * shrink down to their minimum size and then be laid out at
     * their minimum size, refusing to shrink any further.
     *
     * @return a <code>Dimension</code> containing the preferred size
     */
    public Dimension getPreferredSize() {
	Dimension d = super.getPreferredSize();
	if (getParent() instanceof JViewport) {
	    JViewport port = (JViewport)getParent();
	    TextUI ui = getUI();
            int prefWidth = d.width;
            int prefHeight = d.height;
	    if (! getScrollableTracksViewportWidth()) {
			int w = port.getWidth();
			Dimension min = ui.getMinimumSize(this);
			if (w != 0 && w < min.width) {
	                    // Only adjust to min if we have a valid size
			    prefWidth = min.width;
			}
	    }
	    if (! getScrollableTracksViewportHeight()) {
			int h = port.getHeight();
			Dimension min = ui.getMinimumSize(this);
			if (h != 0 && h < min.height) {
	                    // Only adjust to min if we have a valid size
			    prefHeight = min.height;
			}
	    }
            if (prefWidth != d.width || prefHeight != d.height) {
                d = new Dimension(prefWidth, prefHeight);
            }
	}
	return d;
    }

    /**
     * Forwards the <code>scrollRectToVisible()</code> message to the
     * <code>JComponent</code>'s parent. Components that can service
     * the request, such as <code>JViewport</code>,
     * override this method and perform the scrolling.
     *
     * @param aRect the visible <code>Rectangle</code>
     * @see JViewport
     */
    public void scrollRectToVisible(Rectangle aRect) {
        Container parent;
        int dx = getX(), dy = getY();

        for (parent = getParent();
                 !(parent == null) &&
                 !(parent instanceof JComponent) &&
                 !(parent instanceof CellRendererPane);
             parent = parent.getParent()) {
             Rectangle bounds = parent.getBounds();

             dx += bounds.x;
             dy += bounds.y;
        }

        if (!(parent == null) && !(parent instanceof CellRendererPane)) {
        	
            if( getRotateDirection() == MSwingRotateUtilities.ROTATE_HORIZANTAL ) {
            	
            	aRect.x += dx;
                aRect.y += dy;

                ((JComponent)parent).scrollRectToVisible(aRect);
                aRect.x -= dx;
                aRect.y -= dy;

            } else if ( getRotateHint() != MSwingRotateUtilities.ROTATE_RIGHTTOLEFT )  {
            	
            	aRect.x += (dx - aRect.width/2);
                aRect.y += dy;

                ((JComponent)parent).scrollRectToVisible(aRect);
                aRect.x -= (dx - aRect.width/2);
                aRect.y -= dy;

            } else {
            	
            	aRect.x += (dx - aRect.width/2);
                aRect.y += dy;

                ((JComponent)parent).scrollRectToVisible(aRect);
                aRect.x -= (dx - aRect.width/2);
                aRect.y -= dy;

            }
        }
        
    }

}
