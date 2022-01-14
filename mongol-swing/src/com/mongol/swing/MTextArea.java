package com.mongol.swing;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.CellRendererPane;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.TextUI;
import javax.swing.text.Document;

import com.mongol.encode.MongolianFontUtil;
import com.mongol.swing.plaf.MRotation;
import com.mongol.swing.plaf.MTextAreaUI;

public class MTextArea extends JTextArea implements MRotation{

    private MTextAreaUI areaUI;
    
	public MTextArea() {
		super();
		if( areaUI == null )
			areaUI = new MTextAreaUI();
        super.setUI(areaUI);
	}

	public MTextArea(String text) {
		super(text);
		if( areaUI == null )
			areaUI = new MTextAreaUI();
        super.setUI(areaUI);
	}

	public MTextArea(Document doc) {
		super(doc);
		if( areaUI == null )
			areaUI = new MTextAreaUI();
        super.setUI(areaUI);
	}

	public MTextArea(int rows, int columns) {
		super(rows, columns);
		if( areaUI == null )
			areaUI = new MTextAreaUI();
        super.setUI(areaUI);
	}

	public MTextArea(String text, int rows, int columns) {
		super(text, rows, columns);
		if( areaUI == null )
			areaUI = new MTextAreaUI();
        super.setUI(areaUI);
	}

	public MTextArea(Document doc, String text, int rows, int columns) {
		super(doc, text, rows, columns);
		if( areaUI == null )
			areaUI = new MTextAreaUI();
        super.setUI(areaUI);
	}

    public void setUI(TextUI ui) {
        super.setUI(ui);
		if( areaUI == null )
			areaUI = new MTextAreaUI();
        if( !(ui instanceof MTextAreaUI) )
            super.setUI(areaUI);
    }
    
    private int columnWidth;
    private int rowHeight;
    
    
    public Dimension getPreferredSize() {
    	
        Dimension d = null;
        if (isPreferredSizeSet()) {
            return super.getPreferredSize();
        }
        if (ui != null) {
            d = ui.getPreferredSize(this);
        }
		
        d = (d == null) ? new Dimension(400,400) : d;
        Insets insets = getInsets();
        int columns = super.getColumns();
        int rows = super.getRows();
        d = (d == null) ? super.getPreferredSize(): d;
        
        if( getRotateDirection() == MSwingRotateUtilities.ROTATE_HORIZANTAL ) {
	        if (columns != 0) {
	            d.width = Math.max(d.width, columns * getColumnWidth() +
	                    insets.left + insets.right);
	        }
	        if (rows != 0) {
	            d.height = Math.max(d.height, rows * getRowHeight() +
	                                insets.top + insets.bottom);
	        }
	        
        } else {
        	
	        if (columns != 0) {
	            d.height = Math.max(d.height, columns * getColumnWidth() +
	                    insets.top + insets.bottom);
	        }
	        if (rows != 0) {
	            d.width = Math.max(d.width, rows * getRowHeight() +
	                                insets.left + insets.right);
	        }
        	
        }
        return d;
    }

    /**
     * Returns the size <code>Dimensions</code> needed for this
     * <code>TextArea</code>.  If a non-zero number of columns has been
     * set, the width is set to the columns multiplied by
     * the column width.
     *
     * @return the dimension 
     */
    public Dimension getSize() {
        Dimension size = super.getSize();
		//        if( getRotateDirection() != MSwingRotateUtilities.ROTATE_HORIZANTAL ) {
		//	        int tmp = size.width;
		//	        size.width = size.height;
		//        	size.height = tmp;
		//	        	
		//        }
        return size;
    }
    /**
     * Returns true if a viewport should always force the width of this
     * <code>Scrollable</code> to match the width of the viewport.
     * For example a normal text view that supported line wrapping
     * would return true here, since it would be undesirable for
     * wrapped lines to disappear beyond the right
     * edge of the viewport.  Note that returning true for a
     * <code>Scrollable</code> whose ancestor is a <code>JScrollPane</code>
     * effectively disables horizontal scrolling.
     * <p>
     * Scrolling containers, like <code>JViewport</code>,
     * will use this method each time they are validated.
     *
     * @return true if a viewport should force the <code>Scrollable</code>s
     *   width to match its own
     */
    public boolean getScrollableTracksViewportWidth() {
        Container parent = getParent();
        if (parent instanceof JViewport) {
            if( getRotateDirection() == MSwingRotateUtilities.ROTATE_HORIZANTAL ) {
            	return parent.getWidth() > getPreferredSize().width;
            } else if ( getRotateHint() != MSwingRotateUtilities.ROTATE_RIGHTTOLEFT )  {
            	if( this.getLineWrap() ) {
            		return parent.getWidth() > getPreferredSize().width;
            	} else {
                	return parent.getWidth() > getPreferredSize().width;
            	}
            } else {
            	return parent.getWidth() > getPreferredSize().width;
            }
        }
        return false;
    }

    /**
     * Returns true if a viewport should always force the height of this
     * <code>Scrollable</code> to match the height of the viewport.
     * For example a columnar text view that flowed text in left to
     * right columns could effectively disable vertical scrolling by
     * returning true here.
     * <p>
     * Scrolling containers, like <code>JViewport</code>,
     * will use this method each time they are validated.
     *
     * @return true if a viewport should force the Scrollables height
     *   to match its own
     */
    public boolean getScrollableTracksViewportHeight() {
        Container parent = getParent();
        if (parent instanceof JViewport) {
            if( getRotateDirection() == MSwingRotateUtilities.ROTATE_HORIZANTAL ) {
            	return parent.getHeight() > getPreferredSize().height;
            } else if ( getRotateHint() != MSwingRotateUtilities.ROTATE_RIGHTTOLEFT )  {
            	if( this.getLineWrap() ) {
            		return parent.getHeight() > getPreferredSize().height;
            	} else {
                   	return parent.getHeight() > getPreferredSize().height;
            	}
            } else {
               	return parent.getHeight() > getPreferredSize().height;
            }
        }
        return false;
    }


    public void setRotateHint(int hint){
    	TextUI textUI = getUI();
        if( textUI instanceof MTextAreaUI ){
        	((MTextAreaUI)textUI).setRotateHint(hint);
        }
    }

    public int getRotateHint(){
    	TextUI textUI = getUI();
        if( textUI instanceof MTextAreaUI ){
        	return ((MTextAreaUI)textUI).getRotateHint();
        }
        return MSwingRotateUtilities.ROTATE_DEFAULT;
    }

    public void setRotateDirection(int direction){
    	TextUI textUI = getUI();
        if( textUI instanceof MTextAreaUI ){
        	((MTextAreaUI)textUI).setRotateDirection(direction);
        }
    }

    public int getRotateDirection(){
    	TextUI textUI = getUI();
        if( textUI instanceof MTextAreaUI ){
        	return ((MTextAreaUI)textUI).getRotateDirection();
        }
        return MSwingRotateUtilities.ROTATE_HORIZANTAL;
    }

   
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
    	
        if( getRotateDirection() == MSwingRotateUtilities.ROTATE_HORIZANTAL ) {
	        switch (orientation) {
	        case SwingConstants.VERTICAL:
	            return getRowHeight();
	        case SwingConstants.HORIZONTAL:
	            return getColumnWidth();
	        default:
	            throw new IllegalArgumentException("Invalid orientation: " + orientation);
	        }
        } else if ( getRotateHint() != MSwingRotateUtilities.ROTATE_RIGHTTOLEFT )  {
	        switch (orientation) {
	        case SwingConstants.VERTICAL:
	            return getColumnWidth();
	        case SwingConstants.HORIZONTAL:
	            return getRowHeight();
	        default:
	            throw new IllegalArgumentException("Invalid orientation: " + orientation);
	        }
        } else {
	        switch (orientation) {
	        case SwingConstants.VERTICAL:
	            return getColumnWidth();
	        case SwingConstants.HORIZONTAL:
	            return getRowHeight();
	        default:
	            throw new IllegalArgumentException("Invalid orientation: " + orientation);
	        }
        }
    }

//    /**
//     * Sets the current font.  This removes cached row height and column
//     * width so the new font will be reflected, and calls revalidate().
//     *
//     * @param f the font to use as the current font
//     */
//    public void setFont(Font f) {
//        super.setFont(f);
//        rowHeight = 0;
//        columnWidth = 0;
//    }
//
    /**
     * Gets column width.
     * The meaning of what a column is can be considered a fairly weak
     * notion for some fonts.  This method is used to define the width
     * of a column.  By default this is defined to be the width of the
     * character <em>m</em> for the font used.  This method can be
     * redefined to be some alternative amount.
     *
     * @return the column width &gt;= 1
     */
    protected int getColumnWidth() {
    	int parentWidth = super.getColumnWidth();
        if (columnWidth == 0) {
        	Font font = getFont();
        	if( MongolianFontUtil.isMongolianFont(font) ) {
	            FontMetrics metrics = getFontMetrics(font);
	            columnWidth = metrics.charWidth('m');
        	} else {
        		Font altfont = MongolianFontUtil.getAltFont(font, "TextField.font");
	            FontMetrics metrics = getFontMetrics(altfont);
	            columnWidth = metrics.charWidth('m');
        	}
        }
        return columnWidth;
    }

    /**
     * Defines the meaning of the height of a row.  This defaults to
     * the height of the font.
     *
     * @return the height &gt;= 1
     */
    protected int getRowHeight() {
    	int parentHeight = super.getRowHeight();
        if (rowHeight == 0) {
        	Font font = getFont();
        	if( MongolianFontUtil.isMongolianFont(font) ) {
	            FontMetrics metrics = getFontMetrics(font);
	            rowHeight = metrics.getHeight();
        	} else {
        		Font altfont = MongolianFontUtil.getAltFont(font, "TextField.font");
	            FontMetrics metrics = getFontMetrics(altfont);
	            rowHeight = metrics.getHeight();
        	}
        }
        return rowHeight;
    }

    public Dimension getPreferredScrollableViewportSize() {
        Dimension size = super.getPreferredScrollableViewportSize();
        size = (size == null) ? new Dimension(400,400) : size;
        Insets insets = getInsets();
        
        int columns = super.getColumns();
        int rows = super.getRows();
        if( getRotateDirection() == MSwingRotateUtilities.ROTATE_HORIZANTAL ) {
	        size.width = (columns == 0) ? size.width :
	                columns * getColumnWidth() + insets.left + insets.right;
	        size.height = (rows == 0) ? size.height :
	                rows * getRowHeight() + insets.top + insets.bottom;
        } else if ( getRotateHint() != MSwingRotateUtilities.ROTATE_RIGHTTOLEFT )  {
	        size.width = (rows == 0) ? size.width :
	        	rows * getRowHeight() + insets.left + insets.right;
	        size.height = (columns == 0) ? size.height :
	        	columns * getColumnWidth() + insets.top + insets.bottom;
        } else {
	        size.width = (rows == 0) ? size.width :
	        	rows * getRowHeight() + insets.left + insets.right;
	        size.height = (columns == 0) ? size.height :
	        	columns * getColumnWidth() + insets.top + insets.bottom;
        }
        
        return size;
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
            	
            	FontMetrics fm = this.getFontMetrics(this.getFont());
            	int fontHeight = fm.getHeight();
            	
            	aRect.x += (dx - fontHeight/2);
                aRect.y += dy;

                ((JComponent)parent).scrollRectToVisible(aRect);
                aRect.x -= (dx - fontHeight/2);
                aRect.y -= dy;

            } else {
            	
            	FontMetrics fm = this.getFontMetrics(this.getFont());
            	int fontHeight = fm.getHeight();
            	
            	aRect.x += (dx - fontHeight/2);
                aRect.y += dy;

                ((JComponent)parent).scrollRectToVisible(aRect);
                aRect.x -= (dx - fontHeight/2);
                aRect.y -= dy;

            }
        }
        
    }

}
