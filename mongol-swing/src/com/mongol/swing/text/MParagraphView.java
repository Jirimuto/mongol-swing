package com.mongol.swing.text;

import java.awt.*;
import java.awt.font.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

import java.util.Arrays;

import com.mongol.swing.MSwingRotateUtilities;

/**
 * Modified ParagraphView
 * @author  jrmt@Almas
 */
public class MParagraphView extends MFlowView implements TabExpander {

    /**
     * Constructs a <code>ParagraphView</code> for the given element.
     *
     * @param elem the element that this view is responsible for
     */
    public MParagraphView(Element elem) {
        super(elem, MView.Y_AXIS);
        setPropertiesFromAttributes();
        Document doc = elem.getDocument();
        Object i18nFlag = doc.getProperty("i18n");
        if ((i18nFlag != null) && i18nFlag.equals(Boolean.TRUE)) {
            try {
                // the classname should probably come from a property file.
                strategy = new MTextLayoutStrategy();
            } catch (Throwable e) {
                throw new MStateInvariantError("ParagraphView: Can't create i18n strategy: "
                                              + e.getMessage());
            }
        }
    }

    /**
     * Constructs a <code>ParagraphView</code> for the given element.
     *
     * @param elem the element that this view is responsible for
     */
    public MParagraphView(Element elem, int direction, int hint) {
        super(elem, MView.Y_AXIS);
        super.setRotateDirection(direction);
        if( direction == MSwingRotateUtilities.ROTATE_HORIZANTAL )
        	super.setAxis(MView.Y_AXIS);
        else
        	super.setAxis(MView.X_AXIS);
        super.setRotateHint(hint);
        
        setPropertiesFromAttributes();
        Document doc = elem.getDocument();
        Object i18nFlag = doc.getProperty("i18n");
        if ((i18nFlag != null) && i18nFlag.equals(Boolean.TRUE)) {
            try {
                // the classname should probably come from a property file.
                strategy = new MTextLayoutStrategy();
            } catch (Throwable e) {
                throw new MStateInvariantError("ParagraphView: Can't create i18n strategy: "
                                              + e.getMessage());
            }
        }
    }

    /**
     * Sets the type of justification.
     *
     * @param j one of the following values:
     * <ul>
     * <li><code>StyleConstants.ALIGN_LEFT</code>
     * <li><code>StyleConstants.ALIGN_CENTER</code>
     * <li><code>StyleConstants.ALIGN_RIGHT</code>
     * </ul>
     */
    protected void setJustification(int j) {
        justification = j;
    }

    /**
     * Sets the line spacing.
     *
     * @param ls the value is a factor of the line hight
     */
    protected void setLineSpacing(float ls) {
        lineSpacing = ls;
    }

    /**
     * Sets the indent on the first line.
     *
     * @param fi the value in points
     */
    protected void setFirstLineIndent(float fi) {
        firstLineIndent = (int) fi;
    }

    /**
     * Set the cached properties from the attributes.
     */
    protected void setPropertiesFromAttributes() {
        AttributeSet attr = getAttributes();
        if (attr != null) {
            setParagraphInsets(attr);
            Integer a = (Integer)attr.getAttribute(StyleConstants.Alignment);
            int alignment;
            if (a == null) {
                Document doc = getElement().getDocument();
                Object o = doc.getProperty(TextAttribute.RUN_DIRECTION);
                if ((o != null) && o.equals(TextAttribute.RUN_DIRECTION_RTL)) {
                    alignment = StyleConstants.ALIGN_RIGHT;
                } else {
                    alignment = StyleConstants.ALIGN_LEFT;
                }
            } else {
                alignment = a.intValue();
            }
            setJustification(alignment);
            setLineSpacing(StyleConstants.getLineSpacing(attr));
            setFirstLineIndent(StyleConstants.getFirstLineIndent(attr));
        }
    }

    /**
     * Returns the number of views that this view is
     * responsible for.
     * The child views of the paragraph are rows which
     * have been used to arrange pieces of the <code>View</code>s
     * that represent the child elements.  This is the number
     * of views that have been tiled in two dimensions,
     * and should be equivalent to the number of child elements
     * to the element this view is responsible for.
     *
     * @return the number of views that this <code>ParagraphView</code>
     *          is responsible for
     */
    protected int getLayoutViewCount() {
        return layoutPool.getViewCount();
    }

    /**
     * Returns the view at a given <code>index</code>.
     * The child views of the paragraph are rows which
     * have been used to arrange pieces of the <code>Views</code>
     * that represent the child elements.  This methods returns
     * the view responsible for the child element index
     * (prior to breaking).  These are the Views that were
     * produced from a factory (to represent the child
     * elements) and used for layout.
     *
     * @param index the <code>index</code> of the desired view
     * @return the view at <code>index</code>
     */
    protected View getLayoutView(int index) {
        return layoutPool.getView(index);
    }

    /**
     * Provides a way to determine the next visually represented model
     * location that one might place a caret.  Some views may not be visible,
     * they might not be in the same order found in the model, or they just
     * might not allow access to some of the locations in the model.
     * This is a convenience method for {@link #getNextNorthSouthVisualPositionFrom}
     * and {@link #getNextEastWestVisualPositionFrom}.
     * This method enables specifying a position to convert
     * within the range of &gt;=0.  If the value is -1, a position
     * will be calculated automatically.  If the value &lt; -1,
     * the {@code BadLocationException} will be thrown.
     *
     * @param pos the position to convert
     * @param b a bias value of either <code>Position.Bias.Forward</code>
     *  or <code>Position.Bias.Backward</code>
     * @param a the allocated region to render into
     * @param direction the direction from the current position that can
     *  be thought of as the arrow keys typically found on a keyboard;
     *  this may be one of the following:
     *  <ul>
     *  <li><code>SwingConstants.WEST</code>
     *  <li><code>SwingConstants.EAST</code>
     *  <li><code>SwingConstants.NORTH</code>
     *  <li><code>SwingConstants.SOUTH</code>
     *  </ul>
     * @param biasRet an array containing the bias that was checked
     * @return the location within the model that best represents the next
     *  location visual position
     * @exception BadLocationException the given position is not a valid
     *                                 position within the document
     * @exception IllegalArgumentException if <code>direction</code> is invalid
     */
    public int getNextVisualPositionFrom(int pos, Position.Bias b, Shape a,
                                         int direction, Position.Bias[] biasRet)
      throws BadLocationException {
    	if( getRotateDirection() == MSwingRotateUtilities.ROTATE_HORIZANTAL ){
    		pos = getNextVisualPositionFromHorizantal(pos, b, a, direction, biasRet);
    	} else if( getRotateHint() != MSwingRotateUtilities.ROTATE_RIGHTTOLEFT ){
    		pos = getNextVisualPositionFromVerticalL2R(pos, b, a, direction, biasRet);
    	} else {
    		pos = getNextVisualPositionFromVerticalR2L(pos, b, a, direction, biasRet);
    	}
    	return pos;
    }


    private int getNextVisualPositionFromHorizantal(int pos, Position.Bias b, Shape a,
            int direction, Position.Bias[] biasRet)
		throws BadLocationException {
		if (pos < -1 || pos > getDocument().getLength()) {
			throw new BadLocationException("invalid position", pos);
		}
		Rectangle inside = getInsideAllocation(a);
		Rectangle alloc = (a instanceof Rectangle) ? (Rectangle)a : a.getBounds();
		
		switch (direction) {
			case NORTH:
				return getNextNorthSouthVisualPositionFrom(pos, b, alloc, direction,
			                          biasRet);
			case SOUTH:
				return getNextNorthSouthVisualPositionFrom(pos, b, alloc, direction,
			                          biasRet);
			case EAST:
				return getNextEastWestVisualPositionFrom(pos, b, alloc, direction,
			                        biasRet);
			case WEST:
				return getNextEastWestVisualPositionFrom(pos, b, alloc, direction,
			                        biasRet);
			default:
				throw new IllegalArgumentException("Bad direction: " + direction);
		}
    }

    private int getNextVisualPositionFromVerticalL2R(int pos, Position.Bias b, Shape a,
            int direction, Position.Bias[] biasRet)
		throws BadLocationException {
		if (pos < -1 || pos > getDocument().getLength()) {
			throw new BadLocationException("invalid position", pos);
		}
		Shape alloc = MUtilities.getRotateVertical(a);
		Rectangle inside = getInsideAllocation(alloc);
		
		switch (direction) {
			case NORTH:
				return getNextEastWestVisualPositionFrom(pos, b, alloc, direction,
                        biasRet);
			case SOUTH:
				return getNextEastWestVisualPositionFrom(pos, b, alloc, direction,
                        biasRet);
			case EAST:
				return getNextNorthSouthVisualPositionFrom(pos, b, alloc, direction,
                        biasRet);
			case WEST:
				return getNextNorthSouthVisualPositionFrom(pos, b, alloc, direction,
                        biasRet);
			default:
				throw new IllegalArgumentException("Bad direction: " + direction);
		}
    }

    private int getNextVisualPositionFromVerticalR2L(int pos, Position.Bias b, Shape a,
            int direction, Position.Bias[] biasRet)
		throws BadLocationException {
		if (pos < -1 || pos > getDocument().getLength()) {
			throw new BadLocationException("invalid position", pos);
		}
		Shape alloc = MUtilities.getRotateVertical(a);
		Rectangle inside = getInsideAllocation(alloc);
		
		switch (direction) {
			case NORTH:
				return getNextEastWestVisualPositionFrom(pos, b, a, direction,
                        biasRet);
			case SOUTH:
				return getNextEastWestVisualPositionFrom(pos, b, a, direction,
                        biasRet);
			case EAST:
				return getNextNorthSouthVisualPositionFrom(pos, b, a, direction,
                        biasRet);
			case WEST:
				return getNextNorthSouthVisualPositionFrom(pos, b, a, direction,
                        biasRet);
			default:
				throw new IllegalArgumentException("Bad direction: " + direction);
		}
    }

    
    /**
     * Returns the next visual position for the cursor, in
     * either the east or west direction.
     * Overridden from <code>CompositeView</code>.
     * @param pos position into the model
     * @param b either <code>Position.Bias.Forward</code> or
     *          <code>Position.Bias.Backward</code>
     * @param a the allocated region to render into
     * @param direction either <code>SwingConstants.NORTH</code>
     *          or <code>SwingConstants.SOUTH</code>
     * @param biasRet an array containing the bias that were checked
     *  in this method
     * @return the location in the model that represents the
     *  next location visual position
     */
    protected int getNextNorthSouthVisualPositionFrom(int pos, Position.Bias b,
                                                      Shape a, int direction,
                                                      Position.Bias[] biasRet)
                                                throws BadLocationException {
    	
    	int newpos = -1;
    	if( getRotateDirection()==MSwingRotateUtilities.ROTATE_HORIZANTAL  ) {
    		newpos = getNextNorthSouthVisualPositionFromHorizantal(pos, b, a, direction, biasRet);
    	} else if( getRotateHint() != MSwingRotateUtilities.ROTATE_RIGHTTOLEFT ){
    		newpos = getNextNorthSouthVisualPositionFromVerticalL2R(pos, b, a, direction, biasRet);
    	} else {
    		newpos = getNextNorthSouthVisualPositionFromVerticalR2L(pos, b, a, direction, biasRet);
    	}
    	
		JTextComponent target = (JTextComponent) getContainer();
		Caret c = (target != null) ? target.getCaret() : null;
		Rectangle r;
		if( newpos == -1 || newpos == getEndOffset())
			r = target.modelToView(pos);
		else
			r = target.modelToView(newpos);
		
		Point mcp;
		if (c != null && r != null ) {
			mcp = c.getMagicCaretPosition();
			if( mcp != null ){
	    		mcp.y = r.y;
				c.setMagicCaretPosition(mcp);
			}
		}
    	
		return newpos;
    }
    
    private int getNextNorthSouthVisualPositionFromHorizantal(int pos, Position.Bias b,
            Shape a, int direction,
            Position.Bias[] biasRet)
      throws BadLocationException {
        int vIndex;
        if(pos == -1) {
            vIndex = (direction == NORTH || direction == WEST ) ?
                     getViewCount() - 1 : 0;
        }
        else {
            if(b == Position.Bias.Backward && pos > 0) {
                vIndex = getViewIndexAtPosition(pos - 1);
            }
            else {
                vIndex = getViewIndexAtPosition(pos);
            }
            if(direction == NORTH || direction == WEST ) {
                if(vIndex == 0) {
                    return -1;
                }
                vIndex--;
            }
            else 
            	if(++vIndex >= getViewCount()) {
                return getEndOffset();
            }
        }
        // vIndex gives index of row to look in.
        JTextComponent text = (JTextComponent)getContainer();
        Caret c = text.getCaret();
        Point magicPoint;
        magicPoint = (c != null) ? c.getMagicCaretPosition() : null;
        int x;
        if(magicPoint == null) {
            Shape posBounds;
            try {
                posBounds = text.getUI().modelToView(text, pos, b);
            } catch (BadLocationException exc) {
                posBounds = null;
            }
            if(posBounds == null) {
                x = 0;
            }
            else {
                x = posBounds.getBounds().x;
            }
        }
        else {
            x = magicPoint.x;
        }
        return getClosestPositionToHorizantal(pos, b, a, direction, biasRet, vIndex, x);
    }

    private int getNextNorthSouthVisualPositionFromVerticalL2R(int pos, Position.Bias b,
            Shape a, int direction,
            Position.Bias[] biasRet)
            throws BadLocationException {
        int vIndex;
        if(pos == -1) {
            vIndex = (direction == NORTH || direction == WEST ) ?
                     getViewCount() - 1 : 0;
        }
        else {
            if(b == Position.Bias.Backward && pos > 0) {
                vIndex = getViewIndexAtPosition(pos - 1);
            }
            else {
                vIndex = getViewIndexAtPosition(pos);
            }
            if(direction == NORTH || direction == WEST ) {
                if(vIndex == 0) {
                    return -1;
                }
                vIndex--;
            }
            else if(++vIndex >= getViewCount()) {
                return getEndOffset();
            }
        }
        // vIndex gives index of row to look in.
        JTextComponent text = (JTextComponent)getContainer();
        Caret c = text.getCaret();
        Point magicPoint;
        magicPoint = (c != null) ? c.getMagicCaretPosition() : null;
        int y;
        if(magicPoint == null) {
            Shape posBounds;
            try {
                posBounds = text.getUI().modelToView(text, pos, b);
            } catch (BadLocationException exc) {
                posBounds = null;
            }
            if(posBounds == null) {
                y = 0;
            }
            else {
                y = posBounds.getBounds().y;
            }
        }
        else {
            y = magicPoint.y;
        }
        return getClosestPositionToVertical(pos, b, a, direction, biasRet, vIndex, y);
    }

    private int getNextNorthSouthVisualPositionFromVerticalR2L(int pos, Position.Bias b,
            Shape a, int direction,
            Position.Bias[] biasRet)
            throws BadLocationException {
        int vIndex;
        if(pos == -1) {
            vIndex = (direction == NORTH || direction == EAST ) ?
                     getViewCount() - 1 : 0;
        }
        else {
            if(b == Position.Bias.Backward && pos > 0) {
                vIndex = getViewIndexAtPosition(pos - 1);
            }
            else {
                vIndex = getViewIndexAtPosition(pos);
            }
            if(direction == NORTH || direction == EAST ) {
                if(vIndex == 0) {
                    return -1;
                }
                vIndex--;
            }
            else if(++vIndex >= getViewCount()) {
                return getEndOffset();
            }
        }
        // vIndex gives index of row to look in.
        JTextComponent text = (JTextComponent)getContainer();
        Caret c = text.getCaret();
        Point magicPoint;
        magicPoint = (c != null) ? c.getMagicCaretPosition() : null;
        int y;
        if(magicPoint == null) {
            Shape posBounds;
            try {
                posBounds = text.getUI().modelToView(text, pos, b);
            } catch (BadLocationException exc) {
                posBounds = null;
            }
            if(posBounds == null) {
                y = 0;
            }
            else {
                y = posBounds.getBounds().y;
            }
        }
        else {
            y = magicPoint.y;
        }
        return getClosestPositionToVertical(pos, b, a, direction, biasRet, vIndex, y);
    }

    /**
     * Returns the closest model position to <code>x</code>.
     * <code>rowIndex</code> gives the index of the view that corresponds
     * that should be looked in.
     * @param pos  position into the model
     * @param a the allocated region to render into
     * @param direction one of the following values:
     * <ul>
     * <li><code>SwingConstants.NORTH</code>
     * <li><code>SwingConstants.SOUTH</code>
     * </ul>
     * @param biasRet an array containing the bias that were checked
     *  in this method
     * @param rowIndex the index of the view
     * @param x the x coordinate of interest
     * @return the closest model position to <code>x</code>
     */
    // NOTE: This will not properly work if ParagraphView contains
    // other ParagraphViews. It won't raise, but this does not message
    // the children views with getNextVisualPositionFrom.
    protected int getClosestPositionToHorizantal(int pos, Position.Bias b, Shape a,
                                       int direction, Position.Bias[] biasRet,
                                       int rowIndex, int x)
              throws BadLocationException {
        JTextComponent text = (JTextComponent)getContainer();
        Document doc = getDocument();
        AbstractDocument aDoc = (doc instanceof AbstractDocument) ?
                                (AbstractDocument)doc : null;
        MView row = getView(rowIndex);
        int lastPos = -1;
        // This could be made better to check backward positions too.
        biasRet[0] = Position.Bias.Forward;
        for(int vc = 0, numViews = row.getViewCount(); vc < numViews; vc++) {
            View v = row.getView(vc);
            int start = v.getStartOffset();
            boolean ltr = true; // (aDoc != null) ? aDoc.isLeftToRight(start, start + 1) : true;
            if(ltr) {
                lastPos = start;
                for(int end = v.getEndOffset(); lastPos < end; lastPos++) {
                    float xx = text.modelToView(lastPos).getBounds().x;
                    if(xx >= x) {
                        while (++lastPos < end &&
                               text.modelToView(lastPos).getBounds().x == xx) {
                        }
                        return --lastPos;
                    }
                }
                lastPos--;
            }
            else {
                for(lastPos = v.getEndOffset() - 1; lastPos >= start;
                    lastPos--) {
                    float xx = text.modelToView(lastPos).getBounds().x;
                    if(xx >= x) {
                        while (--lastPos >= start &&
                               text.modelToView(lastPos).getBounds().x == xx) {
                        }
                        return ++lastPos;
                    }
                }
                lastPos++;
            }
        }
        if(lastPos == -1) {
            return getStartOffset();
        }
        return lastPos;
    }

	protected int getClosestPositionToVertical(int pos, Position.Bias b, Shape a,
			int direction, Position.Bias[] biasRet, int rowIndex, int y)
			throws BadLocationException {
		JTextComponent text = (JTextComponent) getContainer();
		Document doc = getDocument();
		AbstractDocument aDoc = (doc instanceof AbstractDocument) ? (AbstractDocument) doc
				: null;
		MView row = getView(rowIndex);
		int lastPos = -1;
		// This could be made better to check backward positions too.
		biasRet[0] = Position.Bias.Forward;
		for (int vc = 0, numViews = row.getViewCount(); vc < numViews; vc++) {
			View v = row.getView(vc);
			int start = v.getStartOffset();
			boolean ltr = true; // (aDoc != null) ? aDoc.isLeftToRight(start,
								// start + 1) : true;
			if (ltr) {
				lastPos = start;
				for (int end = v.getEndOffset(); lastPos < end; lastPos++) {
					float yy = text.modelToView(lastPos).getBounds().y;
					if (yy >= y) {
						while (++lastPos < end
								&& text.modelToView(lastPos).getBounds().y == yy) {
						}
						return --lastPos;
					}
				}
				lastPos--;
			} else {
				for (lastPos = v.getEndOffset() - 1; lastPos >= start; lastPos--) {
					float yy = text.modelToView(lastPos).getBounds().y;
					if (yy >= y) {
						while (--lastPos >= start
								&& text.modelToView(lastPos).getBounds().y == yy) {
						}
						return ++lastPos;
					}
				}
				lastPos++;
			}
		}
		if (lastPos == -1) {
			return getStartOffset();
		}
		return lastPos;
	}

    /**
     * Determines in which direction the next view lays.
     * Consider the <code>View</code> at index n.
     * Typically the <code>View</code>s are layed out
     * from left to right, so that the <code>View</code>
     * to the EAST will be at index n + 1, and the
     * <code>View</code> to the WEST will be at index n - 1.
     * In certain situations, such as with bidirectional text,
     * it is possible that the <code>View</code> to EAST is not
     * at index n + 1, but rather at index n - 1,
     * or that the <code>View</code> to the WEST is not at
     * index n - 1, but index n + 1.  In this case this method
     * would return true, indicating the <code>View</code>s are
     * layed out in descending order.
     * <p>
     * This will return true if the text is layed out right
     * to left at position, otherwise false.
     *
     * @param position position into the model
     * @param bias either <code>Position.Bias.Forward</code> or
     *          <code>Position.Bias.Backward</code>
     * @return true if the text is layed out right to left at
     *         position, otherwise false.
     */
    protected boolean flipEastAndWestAtEnds(int position,
                                            Position.Bias bias) {
        Document doc = getDocument();
        if(doc instanceof AbstractDocument && false
           // !((AbstractDocument)doc).isLeftToRight(getStartOffset(), getStartOffset() + 1)
        		) {
            return true;
        }
        return false;
    }

    // --- FlowView methods ---------------------------------------------

    /**
     * Fetches the constraining span to flow against for
     * the given child index.
     * @param index the index of the view being queried
     * @return the constraining span for the given view at
     *  <code>index</code>
     * @since 1.3
     */
    public int getFlowSpan(int index) {
        MView child = getView(index);
        int adjust = 0;
        if (child instanceof Row) {
            Row row = (Row) child;
            adjust = row.getLeftInset() + row.getRightInset();
        }
        return (layoutSpan == Integer.MAX_VALUE) ? layoutSpan
                                                 : (layoutSpan - adjust);
    }

    /**
     * Fetches the location along the flow axis that the
     * flow span will start at.
     * @param index the index of the view being queried
     * @return the location for the given view at
     *  <code>index</code>
     * @since 1.3
     */
    public int getFlowStart(int index) {
        MView child = getView(index);
        int adjust = 0;
        if (child instanceof Row) {
            Row row = (Row) child;
            adjust = row.getLeftInset();
        }
        return tabBase + adjust;
    }

    /**
     * Create a <code>View</code> that should be used to hold a
     * a row's worth of children in a flow.
     * @return the new <code>View</code>
     * @since 1.3
     */
    protected View createRow() {
        return new Row(getElement(), this.getRotateDirection(), this.getRotateHint());
    }

    // --- TabExpander methods ------------------------------------------

    /**
     * Returns the next tab stop position given a reference position.
     * This view implements the tab coordinate system, and calls
     * <code>getTabbedSpan</code> on the logical children in the process
     * of layout to determine the desired span of the children.  The
     * logical children can delegate their tab expansion upward to
     * the paragraph which knows how to expand tabs.
     * <code>LabelView</code> is an example of a view that delegates
     * its tab expansion needs upward to the paragraph.
     * <p>
     * This is implemented to try and locate a <code>TabSet</code>
     * in the paragraph element's attribute set.  If one can be
     * found, its settings will be used, otherwise a default expansion
     * will be provided.  The base location for for tab expansion
     * is the left inset from the paragraphs most recent allocation
     * (which is what the layout of the children is based upon).
     *
     * @param x the X reference position
     * @param tabOffset the position within the text stream
     *   that the tab occurred at >= 0
     * @return the trailing end of the tab expansion >= 0
     * @see TabSet
     * @see TabStop
     * @see LabelView
     */
    public float nextTabStop(float x, int tabOffset) {
        // If the text isn't left justified, offset by 10 pixels!
        if(justification != StyleConstants.ALIGN_LEFT)
            return x + 10.0f;
        x -= tabBase;
        TabSet tabs = getTabSet();
        if(tabs == null) {
            // a tab every 72 pixels.
            return (float)(tabBase + (((int)x / 72 + 1) * 72));
        }
        TabStop tab = tabs.getTabAfter(x + .01f);
        if(tab == null) {
            // no tab, do a default of 5 pixels.
            // Should this cause a wrapping of the line?
            return tabBase + x + 5.0f;
        }
        int alignment = tab.getAlignment();
        int offset;
        switch(alignment) {
        default:
        case TabStop.ALIGN_LEFT:
            // Simple case, left tab.
            return tabBase + tab.getPosition();
        case TabStop.ALIGN_BAR:
            // PENDING: what does this mean?
            return tabBase + tab.getPosition();
        case TabStop.ALIGN_RIGHT:
        case TabStop.ALIGN_CENTER:
            offset = findOffsetToCharactersInString(tabChars,
                                                    tabOffset + 1);
            break;
        case TabStop.ALIGN_DECIMAL:
            offset = findOffsetToCharactersInString(tabDecimalChars,
                                                    tabOffset + 1);
            break;
        }
        if (offset == -1) {
            offset = getEndOffset();
        }
        float charsSize = getPartialSize(tabOffset + 1, offset);
        switch(alignment) {
        case TabStop.ALIGN_RIGHT:
        case TabStop.ALIGN_DECIMAL:
            // right and decimal are treated the same way, the new
            // position will be the location of the tab less the
            // partialSize.
            return tabBase + Math.max(x, tab.getPosition() - charsSize);
        case TabStop.ALIGN_CENTER:
            // Similar to right, but half the partialSize.
            return tabBase + Math.max(x, tab.getPosition() - charsSize / 2.0f);
        }
        // will never get here!
        return x;
    }

    /**
     * Gets the <code>Tabset</code> to be used in calculating tabs.
     *
     * @return the <code>TabSet</code>
     */
    protected TabSet getTabSet() {
        return StyleConstants.getTabSet(getElement().getAttributes());
    }

    /**
     * Returns the size used by the views between
     * <code>startOffset</code> and <code>endOffset</code>.
     * This uses <code>getPartialView</code> to calculate the
     * size if the child view implements the
     * <code>TabableView</code> interface. If a
     * size is needed and a <code>View</code> does not implement
     * the <code>TabableView</code> interface,
     * the <code>preferredSpan</code> will be used.
     *
     * @param startOffset the starting document offset >= 0
     * @param endOffset the ending document offset >= startOffset
     * @return the size >= 0
     */
    protected float getPartialSize(int startOffset, int endOffset) {
        float size = 0.0f;
        int viewIndex;
        int numViews = getViewCount();
        View mView;
        int viewEnd;
        int tempEnd;

        // Have to search layoutPool!
        // PENDING: when ParagraphView supports breaking location
        // into layoutPool will have to change!
        viewIndex = getElement().getElementIndex(startOffset);
        numViews = layoutPool.getViewCount();
        while(startOffset < endOffset && viewIndex < numViews) {
            mView = layoutPool.getView(viewIndex++);
            viewEnd = mView.getEndOffset();
            tempEnd = Math.min(endOffset, viewEnd);
            if(mView instanceof TabableView)
                size += ((TabableView)mView).getPartialSpan(startOffset, tempEnd);
            else if(startOffset == mView.getStartOffset() &&
                    tempEnd == mView.getEndOffset())
                size += mView.getPreferredSpan(MView.X_AXIS);
            else
                // PENDING: should we handle this better?
                return 0.0f;
            startOffset = viewEnd;
        }
        return size;
    }

    /**
     * Finds the next character in the document with a character in
     * <code>string</code>, starting at offset <code>start</code>. If
     * there are no characters found, -1 will be returned.
     *
     * @param string the string of characters
     * @param start where to start in the model >= 0
     * @return the document offset, or -1 if no characters found
     */
    protected int findOffsetToCharactersInString(char[] string,
                                                 int start) {
        int stringLength = string.length;
        int end = getEndOffset();
        Segment seg = new Segment();
        try {
            getDocument().getText(start, end - start, seg);
        } catch (BadLocationException ble) {
            return -1;
        }
        for(int counter = seg.offset, maxCounter = seg.offset + seg.count;
            counter < maxCounter; counter++) {
            char currentChar = seg.array[counter];
            for(int subCounter = 0; subCounter < stringLength;
                subCounter++) {
                if(currentChar == string[subCounter])
                    return counter - seg.offset + start;
            }
        }
        // No match.
        return -1;
    }

    /**
     * Returns where the tabs are calculated from.
     * @return where tabs are calculated from
     */
    protected float getTabBase() {
        return (float)tabBase;
    }

    // ---- View methods ----------------------------------------------------

    /**
     * Renders using the given rendering surface and area on that
     * surface.  This is implemented to delgate to the superclass
     * after stashing the base coordinate for tab calculations.
     *
     * @param g the rendering surface to use
     * @param a the allocated region to render into
     * @see MView#paint
     */
    public void paint(Graphics g, Shape a) {
        Rectangle alloc = (a instanceof Rectangle) ? (Rectangle)a : a.getBounds();
        tabBase = alloc.x + getLeftInset();
        super.paint(g, a);	// MBoxView.paint()

        // line with the negative firstLineIndent value needs
        // special handling
        if (firstLineIndent < 0) {
            Shape sh = getChildAllocation(0, a);
            if ((sh != null) &&  sh.intersects(alloc)) {
                int x = alloc.x + getLeftInset() + firstLineIndent;
                int y = alloc.y + getTopInset();

                Rectangle clip = g.getClipBounds();
                tempRect.x = x + getOffset(X_AXIS, 0);
                tempRect.y = y + getOffset(Y_AXIS, 0);
                tempRect.width = getSpan(X_AXIS, 0) - firstLineIndent;
                tempRect.height = getSpan(Y_AXIS, 0);
                if (tempRect.intersects(clip)) {
                    tempRect.x = tempRect.x - firstLineIndent;
                    paintChild(g, tempRect, 0);
                }
            }
        }
    }

    /**
     * Determines the desired alignment for this view along an
     * axis.  This is implemented to give the alignment to the
     * center of the first row along the y axis, and the default
     * along the x axis.
     *
     * @param axis may be either <code>View.X_AXIS</code> or
     *   <code>View.Y_AXIS</code>
     * @return the desired alignment.  This should be a value
     *   between 0.0 and 1.0 inclusive, where 0 indicates alignment at the
     *   origin and 1.0 indicates alignment to the full span
     *   away from the origin.  An alignment of 0.5 would be the
     *   center of the view.
     */
    public float getAlignment(int axis) {
    	if( this.getRotateDirection()==MSwingRotateUtilities.ROTATE_HORIZANTAL){
	        switch (axis) {
	        case Y_AXIS:
	            float a = 0.5f;
	            if (getViewCount() != 0) {
	                int paragraphSpan = (int) getPreferredSpan(MView.Y_AXIS);
	                MView v = getView(0);
	                int rowSpan = (int) v.getPreferredSpan(MView.Y_AXIS);
	                a = (paragraphSpan != 0) ? ((float)(rowSpan / 2)) / paragraphSpan : 0;
	            }
	            return a;
	        case X_AXIS:
	            return 0.5f;
	        default:
	            throw new IllegalArgumentException("Invalid axis: " + axis);
	        }
    	} else {
	        switch (axis) {
	        case X_AXIS:
	            float a = 0.5f;
	            if (getViewCount() != 0) {
	                int paragraphSpan = (int) getPreferredSpan(MView.X_AXIS);
	                MView v = getView(0);
	                int rowSpan = (int) v.getPreferredSpan(MView.X_AXIS);
	                a = (paragraphSpan != 0) ? ((float)(rowSpan / 2)) / paragraphSpan : 0;
	            }
	            return a;
	        case Y_AXIS:
	            return 0.5f;
	        default:
	            throw new IllegalArgumentException("Invalid axis: " + axis);
	        }
    		
    	}
    }

    /**
     * Breaks this view on the given axis at the given length.
     * <p>
     * <code>ParagraphView</code> instances are breakable
     * along the <code>Y_AXIS</code> only, and only if
     * <code>len</code> is after the first line.
     *
     * @param axis may be either <code>View.X_AXIS</code>
     *  or <code>View.Y_AXIS</code>
     * @param len specifies where a potential break is desired
     *  along the given axis >= 0
     * @param a the current allocation of the view
     * @return the fragment of the view that represents the
     *  given span, if the view can be broken; if the view
     *  doesn't support breaking behavior, the view itself is
     *  returned
     * @see MView#breakView
     */
    public View breakView(int axis, float len, Shape a) {
    	if( this.getRotateDirection()==MSwingRotateUtilities.ROTATE_HORIZANTAL){
	        if(axis == MView.Y_AXIS) {
	            if(a != null) {
	                Rectangle alloc = a.getBounds();
	                setSize(alloc.width, alloc.height);
	            }
	            // Determine what row to break on.
	
	            // PENDING(prinz) add break support
	            return this;
	        }
	        return this;
    	} else {
	        if(axis == MView.X_AXIS) {
	            if(a != null) {
	                Rectangle alloc = a.getBounds();
	                setSize(alloc.width, alloc.height);
	            }
	            // Determine what row to break on.
	
	            // PENDING(prinz) add break support
	            return this;
	        }
	        return this;
    	}
    }

    /**
     * Gets the break weight for a given location.
     * <p>
     * <code>ParagraphView</code> instances are breakable
     * along the <code>Y_AXIS</code> only, and only if
     * <code>len</code> is after the first row.  If the length
     * is less than one row, a value of <code>BadBreakWeight</code>
     * is returned.
     *
     * @param axis may be either <code>View.X_AXIS</code>
     *  or <code>View.Y_AXIS</code>
     * @param len specifies where a potential break is desired >= 0
     * @return a value indicating the attractiveness of breaking here;
     *  either <code>GoodBreakWeight</code> or <code>BadBreakWeight</code>
     * @see MView#getBreakWeight
     */
    public int getBreakWeight(int axis, float len) {
        if(axis == MView.Y_AXIS) {
            // PENDING(prinz) make this return a reasonable value
            // when paragraph breaking support is re-implemented.
            // If less than one row, bad weight value should be
            // returned.
            //return GoodBreakWeight;
            return BadBreakWeight;
        }
        return BadBreakWeight;
    }

    /**
     * Calculate the needs for the paragraph along the minor axis.
     *
     * <p>This uses size requirements of the superclass, modified to take into
     * account the non-breakable areas at the adjacent views edges.  The minimal
     * size requirements for such views should be no less than the sum of all
     * adjacent fragments.</p>
     *
     * <p>If the {@code axis} parameter is neither {@code View.X_AXIS} nor
     * {@code View.Y_AXIS}, {@link IllegalArgumentException} is thrown.  If the
     * {@code r} parameter is {@code null,} a new {@code SizeRequirements}
     * object is created, otherwise the supplied {@code SizeRequirements}
     * object is returned.</p>
     *
     * @param axis  the minor axis
     * @param r     the input {@code SizeRequirements} object
     * @return      the new or adjusted {@code SizeRequirements} object
     * @throws IllegalArgumentException  if the {@code axis} parameter is invalid
     */
    @Override
    protected SizeRequirements calculateMinorAxisRequirements(int axis,
                                                        SizeRequirements r) {
        r = super.calculateMinorAxisRequirements(axis, r);

        float min = 0;
        float glue = 0;
        int n = getLayoutViewCount();
        for (int i = 0; i < n; i++) {
            View v = getLayoutView(i);
            float span = v.getMinimumSpan(axis);
            if (v.getBreakWeight(axis, 0, v.getMaximumSpan(axis)) > MView.BadBreakWeight) {
                // find the longest non-breakable fragments at the view edges
                int p0 = v.getStartOffset();
                int p1 = v.getEndOffset();
                float start = findEdgeSpan(v, axis, p0, p0, p1);
                float end = findEdgeSpan(v, axis, p1, p0, p1);
                glue += start;
                min = Math.max(min, Math.max(span, glue));
                glue = end;
            } else {
                // non-breakable view
                glue += span;
                min = Math.max(min, glue);
            }
        }
        r.minimum = Math.max(r.minimum, (int) min);
        r.preferred = Math.max(r.minimum, r.preferred);
        r.maximum = Math.max(r.preferred, r.maximum);

        return r;
    }

    /**
     * Binary search for the longest non-breakable fragment at the view edge.
     */
    private float findEdgeSpan(View v, int axis, int fp, int p0, int p1) {
        int len = p1 - p0;
        if (len <= 1) {
            // further fragmentation is not possible
            return v.getMinimumSpan(axis);
        } else {
            int mid = p0 + len / 2;
            boolean startEdge = mid > fp;
            // initial view is breakable hence must support fragmentation
            View f = startEdge ?
                v.createFragment(fp, mid) : v.createFragment(mid, fp);
            boolean breakable = f.getBreakWeight(
                    axis, 0, f.getMaximumSpan(axis)) > MView.BadBreakWeight;
            if (breakable == startEdge) {
                p1 = mid;
            } else {
                p0 = mid;
            }
            return findEdgeSpan(f, axis, fp, p0, p1);
        }
    }

    /**
     * Gives notification from the document that attributes were changed
     * in a location that this view is responsible for.
     *
     * @param changes the change information from the
     *  associated document
     * @param a the current allocation of the view
     * @param f the factory to use to rebuild if the view has children
     * @see MView#changedUpdate
     */
    public void changedUpdate(DocumentEvent changes, Shape a, ViewFactory f) {
        // update any property settings stored, and layout should be
        // recomputed
        setPropertiesFromAttributes();
        layoutChanged(X_AXIS);
        layoutChanged(Y_AXIS);
        super.changedUpdate(changes, a, f);
    }


    // --- variables -----------------------------------------------

    private int justification;
    private float lineSpacing;
    /** Indentation for the first line, from the left inset. */
    protected int firstLineIndent = 0;

    /**
     * Used by the TabExpander functionality to determine
     * where to base the tab calculations.  This is basically
     * the location of the left side of the paragraph.
     */
    private int tabBase;

    /**
     * Used to create an i18n-based layout strategy
     */
    static Class<?> i18nStrategy;

    /** Used for searching for a tab. */
    static char[] tabChars;
    /** Used for searching for a tab or decimal character. */
    static char[] tabDecimalChars;

    static {
        tabChars = new char[1];
        tabChars[0] = '\t';
        tabDecimalChars = new char[2];
        tabDecimalChars[0] = '\t';
        tabDecimalChars[1] = '.';
    }

    /** used in paint. */
    Rectangle tempRect;
    
    /**
     * Internally created view that has the purpose of holding
     * the views that represent the children of the paragraph
     * that have been arranged in rows.
     */
    class Row extends MBoxView {

        Row(Element elem, int direction, int hint) {
            super(elem, MView.X_AXIS);
            super.setRotateDirection(direction);
            super.setRotateHint(hint);
            if( direction != MSwingRotateUtilities.ROTATE_HORIZANTAL )
            	setAxis(MView.Y_AXIS);
        }

        /**
         * This is reimplemented to do nothing since the
         * paragraph fills in the row with its needed
         * children.
         */
        protected void loadChildren(ViewFactory f) {
        }

        /**
         * Fetches the attributes to use when rendering.  This view
         * isn't directly responsible for an element so it returns
         * the outer classes attributes.
         */
        public AttributeSet getAttributes() {
            View p = getParent();
            return (p != null) ? p.getAttributes() : null;
        }

        public float getAlignment(int axis) {
        	if( this.getRotateDirection()==MSwingRotateUtilities.ROTATE_HORIZANTAL){
	            if (axis == MView.X_AXIS) {
	                switch (justification) {
	                case StyleConstants.ALIGN_LEFT:
	                    return 0;
	                case StyleConstants.ALIGN_RIGHT:
	                    return 1;
	                case StyleConstants.ALIGN_CENTER:
	                    return 0.5f;
	                case StyleConstants.ALIGN_JUSTIFIED:
	                    float rv = 0.5f;
	                    //if we can justifiy the content always align to
	                    //the left.
	                    if (isJustifiableDocument()) {
	                        rv = 0f;
	                    }
	                    return rv;
	                }
	            }
        	} else {
	            if (axis == MView.Y_AXIS) {
	                switch (justification) {
	                case StyleConstants.ALIGN_LEFT:
	                    return 0;
	                case StyleConstants.ALIGN_RIGHT:
	                    return 1;
	                case StyleConstants.ALIGN_CENTER:
	                    return 0.5f;
	                case StyleConstants.ALIGN_JUSTIFIED:
	                    float rv = 0.5f;
	                    //if we can justifiy the content always align to
	                    //the left.
	                    if (isJustifiableDocument()) {
	                        rv = 0f;
	                    }
	                    return rv;
	                }
	            }
        	}
            return super.getAlignment(axis);
        }

        /**
         * Provides a mapping from the document model coordinate space
         * to the coordinate space of the view mapped to it.  This is
         * implemented to let the superclass find the position along
         * the major axis and the allocation of the row is used
         * along the minor axis, so that even though the children
         * are different heights they all get the same caret height.
         *
         * @param pos the position to convert
         * @param a the allocated region to render into
         * @return the bounding box of the given position
         * @exception BadLocationException  if the given position does not represent a
         *   valid location in the associated document
         * @see MView#modelToView
         */
        public Shape modelToView(int pos, Shape a, Position.Bias b) throws BadLocationException {
        	if( this.getRotateDirection()==MSwingRotateUtilities.ROTATE_HORIZANTAL){
        		return modelToViewHorizantal(pos, a, b);
        	} else if( this.getRotateHint()!=MSwingRotateUtilities.ROTATE_RIGHTTOLEFT){
        		return modelToViewVerticalL2R(pos, a, b);
        	} else {
        		return modelToViewVerticalR2L(pos, a, b);
        	}
        }
        private Shape modelToViewHorizantal(int pos, Shape a, Position.Bias b) throws BadLocationException {
            Rectangle r = a.getBounds();
            MView v = getViewAtPosition(pos, r);
            if ((v != null) && (!v.getElement().isLeaf())) {
                // Don't adjust the height if the view represents a branch.
                return super.modelToView(pos, a, b);
            }
            r = a.getBounds();
            int height = r.height;
            int y = r.y;
            Shape loc = super.modelToView(pos, a, b);
            
            // JDK 9 added, comment out and restored as previous version
            //Rectangle2D bounds = loc.getBounds2D();
            // bounds.setRect(bounds.getX(), y, bounds.getWidth(), height);
            
            r = loc.getBounds();
            r.height = height;
            r.y = y;
            return r;
        }

        private Shape modelToViewVerticalL2R(int pos, Shape a, Position.Bias b) throws BadLocationException {
            Rectangle r = a.getBounds();
            MView v = getViewAtPosition(pos, r);
            if ((v != null) && (!v.getElement().isLeaf())) {
                // Don't adjust the height if the view represents a branch.
                return super.modelToView(pos, a, b);
            }
            r = a.getBounds();
            int width = r.width;
            int x = r.x;
            Shape loc = super.modelToView(pos, a, b);
            r = loc.getBounds();
            r.width = width;
            r.x = x + width/2;
            r.height = 1;
            
        	JTextComponent c = (JTextComponent)this.getContainer();
            c.putClientProperty("caretWidth", new Integer(r.width));
            c.repaint();

            return r;
        }

        private Shape modelToViewVerticalR2L(int pos, Shape a, Position.Bias b) throws BadLocationException {
            Rectangle r = a.getBounds();
            MView v = getViewAtPosition(pos, r);
            if ((v != null) && (!v.getElement().isLeaf())) {
                // Don't adjust the height if the view represents a branch.
                return super.modelToView(pos, a, b);
            }
            r = a.getBounds();
            int width = r.width;
            int x = r.x;
            Shape loc = super.modelToView(pos, a, b);
            r = loc.getBounds();
            r.width = width;
            r.x = r.x - width/2;
            r.height = 1;
            
        	JTextComponent c = (JTextComponent)this.getContainer();
            c.putClientProperty("caretWidth", new Integer(r.width));
            c.repaint();

            return r;
        }

        /**
         * Range represented by a row in the paragraph is only
         * a subset of the total range of the paragraph element.
         * @see MView#getRange
         */
        public int getStartOffset() {
            int offs = Integer.MAX_VALUE;
            int n = getViewCount();
            for (int i = 0; i < n; i++) {
                MView v = getView(i);
                offs = Math.min(offs, v.getStartOffset());
            }
            return offs;
        }

        public int getEndOffset() {
            int offs = 0;
            int n = getViewCount();
            for (int i = 0; i < n; i++) {
                MView v = getView(i);
                offs = Math.max(offs, v.getEndOffset());
            }
            return offs;
        }

        /**
         * Perform layout for the minor axis of the box (i.e. the
         * axis orthoginal to the axis that it represents).  The results
         * of the layout should be placed in the given arrays which represent
         * the allocations to the children along the minor axis.
         * <p>
         * This is implemented to do a baseline layout of the children
         * by calling BoxView.baselineLayout.
         *
         * @param targetSpan the total span given to the view, which
         *  whould be used to layout the children.
         * @param axis the axis being layed out.
         * @param offsets the offsets from the origin of the view for
         *  each of the child views.  This is a return value and is
         *  filled in by the implementation of this method.
         * @param spans the span of each child view.  This is a return
         *  value and is filled in by the implementation of this method.
         * @return the offset and span for each child view in the
         *  offsets and spans parameters
         */
        protected void layoutMinorAxis(int targetSpan, int axis, int[] offsets, int[] spans) {
            baselineLayout(targetSpan, axis, offsets, spans);
        }

        protected SizeRequirements calculateMinorAxisRequirements(int axis,
                                                                  SizeRequirements r) {
            return baselineRequirements(axis, r);
        }


        private boolean isLastRow() {
            View parent;
            return ((parent = getParent()) == null
                    || this == parent.getView(parent.getViewCount() - 1));
        }

        private boolean isBrokenRow() {
            boolean rv = false;
            int viewsCount = getViewCount();
            if (viewsCount > 0) {
                MView lastView = getView(viewsCount - 1);
                if (lastView.getBreakWeight(X_AXIS, 0, 0) >=
                      ForcedBreakWeight) {
                    rv = true;
                }
            }
            return rv;
        }

        private boolean isJustifiableDocument() {
            return (! Boolean.TRUE.equals(getDocument().getProperty(
            		"i18n")));
        }

        /**
         * Whether we need to justify this {@code Row}.
         * At this time (jdk1.6) we support justification on for non
         * 18n text.
         *
         * @return {@code true} if this {@code Row} should be justified.
         */
        private boolean isJustifyEnabled() {
            boolean ret = (justification == StyleConstants.ALIGN_JUSTIFIED);

            //no justification for i18n documents
            ret = ret && isJustifiableDocument();

            //no justification for the last row
            ret = ret && ! isLastRow();

            //no justification for the broken rows
            ret = ret && ! isBrokenRow();

            return ret;
        }


        //Calls super method after setting spaceAddon to 0.
        //Justification should not affect MajorAxisRequirements
        @Override
        protected SizeRequirements calculateMajorAxisRequirements(int axis,
                SizeRequirements r) {
            int oldJustficationData[] = justificationData;
            justificationData = null;
            SizeRequirements ret = super.calculateMajorAxisRequirements(axis, r);
            if (isJustifyEnabled()) {
                justificationData = oldJustficationData;
            }
            return ret;
        }

        @Override
        protected void layoutMajorAxis(int targetSpan, int axis,
                                       int[] offsets, int[] spans) {
            int oldJustficationData[] = justificationData;
            justificationData = null;
            super.layoutMajorAxis(targetSpan, axis, offsets, spans);
            if (! isJustifyEnabled()) {
                return;
            }

            int currentSpan = 0;
            for (int span : spans) {
                currentSpan += span;
            }
            if (currentSpan == targetSpan) {
                //no need to justify
                return;
            }

            // we justify text by enlarging spaces by the {@code spaceAddon}.
            // justification is started to the right of the rightmost TAB.
            // leading and trailing spaces are not extendable.
            //
            // GlyphPainter1 uses
            // justificationData
            // for all painting and measurement.

            int extendableSpaces = 0;
            int startJustifiableContent = -1;
            int endJustifiableContent = -1;
            int lastLeadingSpaces = 0;

            int rowStartOffset = getStartOffset();
            int rowEndOffset = getEndOffset();
            int spaceMap[] = new int[rowEndOffset - rowStartOffset];
            Arrays.fill(spaceMap, 0);
            for (int i = getViewCount() - 1; i >= 0 ; i--) {
                MView mView = getView(i);
                if (mView instanceof MGlyphView) {
                    MGlyphView.JustificationInfo justificationInfo =
                        ((MGlyphView) mView).getJustificationInfo(rowStartOffset);
                    final int viewStartOffset = mView.getStartOffset();
                    final int offset = viewStartOffset - rowStartOffset;
                    for (int j = 0; j < justificationInfo.spaceMap.length(); j++) {
                        if (justificationInfo.spaceMap.get(j)) {
                            spaceMap[j + offset] = 1;
                        }
                    }
                    if (startJustifiableContent > 0) {
                        if (justificationInfo.end >= 0) {
                            extendableSpaces += justificationInfo.trailingSpaces;
                        } else {
                            lastLeadingSpaces += justificationInfo.trailingSpaces;
                        }
                    }
                    if (justificationInfo.start >= 0) {
                        startJustifiableContent =
                            justificationInfo.start + viewStartOffset;
                        extendableSpaces += lastLeadingSpaces;
                    }
                    if (justificationInfo.end >= 0
                          && endJustifiableContent < 0) {
                        endJustifiableContent =
                            justificationInfo.end + viewStartOffset;
                    }
                    extendableSpaces += justificationInfo.contentSpaces;
                    lastLeadingSpaces = justificationInfo.leadingSpaces;
                    if (justificationInfo.hasTab) {
                        break;
                    }
                }
            }
            if (extendableSpaces <= 0) {
                //there is nothing we can do to justify
                return;
            }
            int adjustment = (targetSpan - currentSpan);
            int spaceAddon = (extendableSpaces > 0)
                ?  adjustment / extendableSpaces
                : 0;
            int spaceAddonLeftoverEnd = -1;
            for (int i = startJustifiableContent - rowStartOffset,
                     leftover = adjustment - spaceAddon * extendableSpaces;
                     leftover > 0;
                     leftover -= spaceMap[i],
                     i++) {
                spaceAddonLeftoverEnd = i;
            }
            if (spaceAddon > 0 || spaceAddonLeftoverEnd >= 0) {
                justificationData = (oldJustficationData != null)
                    ? oldJustficationData
                    : new int[END_JUSTIFIABLE + 1];
                justificationData[SPACE_ADDON] = spaceAddon;
                justificationData[SPACE_ADDON_LEFTOVER_END] =
                    spaceAddonLeftoverEnd;
                justificationData[START_JUSTIFIABLE] =
                    startJustifiableContent - rowStartOffset;
                justificationData[END_JUSTIFIABLE] =
                    endJustifiableContent - rowStartOffset;
                super.layoutMajorAxis(targetSpan, axis, offsets, spans);
            }
        }

        //for justified row we assume the maximum horizontal span
        //is MAX_VALUE.
        @Override
        public float getMaximumSpan(int axis) {
            float ret;
        	if( getRotateDirection()==MSwingRotateUtilities.ROTATE_HORIZANTAL){
	            if (MView.X_AXIS == axis
	                  && isJustifyEnabled()) {
	                ret = Float.MAX_VALUE;
	            } else {
	              ret = super.getMaximumSpan(axis);
	            }
        	} else {
	            if (MView.Y_AXIS == axis
		              && isJustifyEnabled()) {
	                ret = Float.MAX_VALUE;
	            } else {
	              ret = super.getMaximumSpan(axis);
	            }
        	}
	            return ret;
        }

        /**
         * Fetches the child view index representing the given position in
         * the model.
         *
         * @param pos the position >= 0
         * @return  index of the view representing the given position, or
         *   -1 if no view represents that position
         */
        protected int getViewIndexAtPosition(int pos) {
            // This is expensive, but are views are not necessarily layed
            // out in model order.
            if(pos < getStartOffset() || pos >= getEndOffset())
                return -1;
            for(int counter = getViewCount() - 1; counter >= 0; counter--) {
                MView v = getView(counter);
                if(pos >= v.getStartOffset() &&
                   pos < v.getEndOffset()) {
                    return counter;
                }
            }
            return -1;
        }

        /**
         * Gets the left inset.
         *
         * @return the inset
         */
        protected short getLeftInset() {
            View parentView;
            int adjustment = 0;
            if ((parentView = getParent()) != null) { //use firstLineIdent for the first row
                if (this == parentView.getView(0)) {
                    adjustment = firstLineIndent;
                }
            }
            return (short)(super.getLeftInset() + adjustment);
        }

        protected short getBottomInset() {
            return (short)(super.getBottomInset() +
                           ((minorRequest != null) ? minorRequest.preferred : 0) *
                           lineSpacing);
        }

        final static int SPACE_ADDON = 0;
        final static int SPACE_ADDON_LEFTOVER_END = 1;
        final static int START_JUSTIFIABLE = 2;
        //this should be the last index in justificationData
        final static int END_JUSTIFIABLE = 3;

        int justificationData[] = null;
    }

}
