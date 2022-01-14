package com.mongol.swing.text;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.lang.ref.SoftReference;

import javax.swing.SizeRequirements;
import javax.swing.event.DocumentEvent;
import javax.swing.text.*;

import com.mongol.encode.MongolianFontUtil;
import com.mongol.swing.MSwingRotateUtilities;
import com.mongol.swing.plaf.MRotation;

public class MWrappedPlainView extends MBoxView implements TabExpander{

    float width = 0;
    float height = 0;
    int p_old = 0;
    
    /**
     * Creates a new WrappedPlainView.  Lines will be wrapped
     * on character boundaries.
     *
     * @param elem the element underlying the view
     */
    public MWrappedPlainView(Element elem) {
        this(elem, false);
    }

    /**
     * Creates a new WrappedPlainView.  Lines can be wrapped on
     * either character or word boundaries depending upon the
     * setting of the wordWrap parameter.
     *
     * @param elem the element underlying the view
     * @param wordWrap should lines be wrapped on word boundaries?
     */
    public MWrappedPlainView(Element elem, boolean wordWrap) {
        super(elem, X_AXIS);
        this.wordWrap = wordWrap;
    }

    /**
     * Creates a new WrappedPlainView.  Lines can be wrapped on
     * either character or word boundaries depending upon the
     * setting of the wordWrap parameter.
     *
     * @param elem the element underlying the view
     * @param wordWrap should lines be wrapped on word boundaries?
     */
    public MWrappedPlainView(Element elem, boolean wordWrap, int axis) {
        super(elem, axis);
        this.wordWrap = wordWrap;
    }
    /**
     * Returns the tab size set for the document, defaulting to 8.
     *
     * @return the tab size
     */
    protected int getTabSize() {
        Integer i = (Integer) getDocument().getProperty(PlainDocument.tabSizeAttribute);
        int size = (i != null) ? i.intValue() : 8;
        return size;
    }

    /**
     * Renders a line of text, suppressing whitespace at the end
     * and expanding any tabs.  This is implemented to make calls
     * to the methods <code>drawUnselectedText</code> and
     * <code>drawSelectedText</code> so that the way selected and
     * unselected text are rendered can be customized.
     *
     * @param p0 the starting document location to use >= 0
     * @param p1 the ending document location to use >= p1
     * @param g the graphics context
     * @param x the starting X position >= 0
     * @param y the starting Y position >= 0
     * @see #drawUnselectedText
     * @see #drawSelectedText
     */
    protected void drawLine(int p0, int p1, Graphics g, int x, int y) {
        Element lineMap = getElement();
        Element line = lineMap.getElement(lineMap.getElementIndex(p0));
        Element elem;

        try {
            if (line.isLeaf()) {
                 drawText(line, p0, p1, g, x, y);
            } else {
                // this line contains the composed text.
                int idx = line.getElementIndex(p0);
                int lastIdx = line.getElementIndex(p1);
                for(; idx <= lastIdx; idx++) {
                    elem = line.getElement(idx);
                    int start = Math.max(elem.getStartOffset(), p0);
                    int end = Math.min(elem.getEndOffset(), p1);
                    x = drawText(elem, start, end, g, x, y);
                }
            }
        } catch (BadLocationException e) {
            throw new MStateInvariantError("Can't render: " + p0 + "," + p1);
        }
    }

    private int drawText(Element elem, int p0, int p1, Graphics g, int x, int y) throws BadLocationException {
        p1 = Math.min(getDocument().getLength(), p1);
        AttributeSet attr = elem.getAttributes();

        if (MUtilities.isComposedTextAttributeDefined(attr)) {
            g.setColor(unselected);
            x = MUtilities.drawComposedText(this, attr, g, x, y,
                                        p0-elem.getStartOffset(),
                                        p1-elem.getStartOffset());
        } else {
            if (sel0 == sel1 || selected == unselected) {
                // no selection, or it is invisible
                x = drawUnselectedText(g, x, y, p0, p1);
            } else if ((p0 >= sel0 && p0 <= sel1) && (p1 >= sel0 && p1 <= sel1)) {
                x = drawSelectedText(g, x, y, p0, p1);
            } else if (sel0 >= p0 && sel0 <= p1) {
                if (sel1 >= p0 && sel1 <= p1) {
                    x = drawUnselectedText(g, x, y, p0, sel0);
                    x = drawSelectedText(g, x, y, sel0, sel1);
                    x = drawUnselectedText(g, x, y, sel1, p1);
                } else {
                    x = drawUnselectedText(g, x, y, p0, sel0);
                    x = drawSelectedText(g, x, y, sel0, p1);
                }
            } else if (sel1 >= p0 && sel1 <= p1) {
                x = drawSelectedText(g, x, y, p0, sel1);
                x = drawUnselectedText(g, x, y, sel1, p1);
            } else {
                x = drawUnselectedText(g, x, y, p0, p1);
            }
        }

        return x;
    }

    /**
     * Renders the given range in the model as normal unselected
     * text.
     *
     * @param g the graphics context
     * @param x the starting X coordinate >= 0
     * @param y the starting Y coordinate >= 0
     * @param p0 the beginning position in the model >= 0
     * @param p1 the ending position in the model >= p0
     * @return the X location of the end of the range >= 0
     * @exception BadLocationException if the range is invalid
     */
    protected int drawUnselectedText(Graphics g, int x, int y,
                                     int p0, int p1) throws BadLocationException {
        g.setColor(unselected);
        Document doc = getDocument();
        Segment segment = MSegmentCache.getSharedSegment();
        doc.getText(p0, p1 - p0, segment);
        int ret = MUtilities.drawTabbedText(this, segment, x, y, g, this, p0);
        MSegmentCache.releaseSharedSegment(segment);
        return ret;
    }

    /**
     * Renders the given range in the model as selected text.  This
     * is implemented to render the text in the color specified in
     * the hosting component.  It assumes the highlighter will render
     * the selected background.
     *
     * @param g the graphics context
     * @param x the starting X coordinate >= 0
     * @param y the starting Y coordinate >= 0
     * @param p0 the beginning position in the model >= 0
     * @param p1 the ending position in the model >= p0
     * @return the location of the end of the range.
     * @exception BadLocationException if the range is invalid
     */
    protected int drawSelectedText(Graphics g, int x,
                                   int y, int p0, int p1) throws BadLocationException {
        g.setColor(selected);
        Document doc = getDocument();
        Segment segment = MSegmentCache.getSharedSegment();
        doc.getText(p0, p1 - p0, segment);
        int ret = MUtilities.drawTabbedText(this, segment, x, y, g, this, p0);
        MSegmentCache.releaseSharedSegment(segment);
        return ret;
    }

    /**
     * Gives access to a buffer that can be used to fetch
     * text from the associated document.
     *
     * @return the buffer
     */
    protected final Segment getLineBuffer() {
        if (lineBuffer == null) {
            lineBuffer = new Segment();
        }
        return lineBuffer;
    }

    /**
     * This is called by the nested wrapped line
     * views to determine the break location.  This can
     * be reimplemented to alter the breaking behavior.
     * It will either break at word or character boundaries
     * depending upon the break argument given at
     * construction.
     */
    protected int calculateBreakPosition(int p0, int p1) {
        int p;
        
    	if( getRotateDirection() == MSwingRotateUtilities.ROTATE_HORIZANTAL ) {
	        Segment segment = MSegmentCache.getSharedSegment();
	        loadText(segment, p0, p1);
	        int currentWidth = getWidth();
	        if (wordWrap) {
	            p = p0 + MUtilities.getBreakLocation(segment, metrics, defaultmetrics, 
	                                                tabBase, tabBase + currentWidth,
	                                                this, p0);
	        } else {
	            p = p0 + MUtilities.getTabbedTextOffset(segment, metrics, defaultmetrics, 
	                                                   tabBase, tabBase + currentWidth,
	                                                   this, p0, false);
	        }
	        MSegmentCache.releaseSharedSegment(segment);
    	} else {
	        Segment segment = MSegmentCache.getSharedSegment();
	        loadText(segment, p0, p1);
	        int currentheight = getHeight();			// Jirimuto mark 2012/10/17
	        if( currentheight == 0 )
	        	currentheight = (int)this.height;
	        if( currentheight > 18 )
	        	currentheight = currentheight - 18;
	        if (wordWrap) {
	            p = p0 + MUtilities.getBreakLocation(segment, metrics, defaultmetrics, 
	                                                tabBase, tabBase + currentheight,
	                                                this, p0);

//if( p0==0 ) {
//    if( p > p0 && p < p1 )
// 		System.out.println("MWarppedPlainView.calculateBreakPosition--->width="+getWidth()+",currentheight="+currentheight+",p="+p +",segment="+segment.subSequence(p0, p).toString());
// 	else
// 	  	System.out.println("MWarppedPlainView.calculateBreakPosition--->width="+getWidth()+",currentheight="+currentheight+",p="+p);
//}
	        } else {
	            p = p0 + MUtilities.getTabbedTextOffset(segment, metrics, defaultmetrics, 
	                                                   tabBase, tabBase + currentheight,
	                                                   this, p0, false);
	        }
	        MSegmentCache.releaseSharedSegment(segment);
    	}
        return p;
    }

    /**
     * Loads all of the children to initialize the view.
     * This is called by the <code>setParent</code> method.
     * Subclasses can reimplement this to initialize their
     * child views in a different manner.  The default
     * implementation creates a child view for each
     * child element.
     *
     * @param f the view factory
     */
    protected void loadChildren(ViewFactory f) {
        Element e = getElement();
        int n = e.getElementCount();
        if (n > 0) {
            MView[] added = new MView[n];
            for (int i = 0; i < n; i++) {
                added[i] = new MWrappedLine(e.getElement(i), this.getRotateDirection(), this.getRotateHint());
            }
            replace(0, 0, added);
        }
    }

    /**
     * Update the child views in response to a
     * document event.
     */
    void updateChildren(DocumentEvent e, Shape a) {
        Element elem = getElement();
        DocumentEvent.ElementChange ec = e.getChange(elem);
        if (ec != null) {
            // the structure of this element changed.
            Element[] removedElems = ec.getChildrenRemoved();
            Element[] addedElems = ec.getChildrenAdded();
            View[] added = new View[addedElems.length];
            for (int i = 0; i < addedElems.length; i++) {
                added[i] = new MWrappedLine(addedElems[i], this.getRotateDirection(), this.getRotateHint());
            }
            replace(ec.getIndex(), removedElems.length, added);

            // should damge a little more intelligently.
            if (a != null) {
                preferenceChanged(null, true, true);
                getContainer().repaint();
            }
        }

        // update font metrics which may be used by the child views
        updateMetrics();
    }

    /**
     * Load the text buffer with the given range
     * of text.  This is used by the fragments
     * broken off of this view as well as this
     * view itself.
     */
    final void loadText(Segment segment, int p0, int p1) {
        try {
            Document doc = getDocument();
            doc.getText(p0, p1 - p0, segment);
        } catch (BadLocationException bl) {
            throw new MStateInvariantError("Can't get line text");
        }
    }

    final void updateMetrics() {
        Component host = getContainer();
        Font font = host.getFont();
        metrics = host.getFontMetrics(font);
        Font alternative_font = MongolianFontUtil.getAltFont(font, "TextField.font");
        defaultmetrics = host.getFontMetrics(alternative_font);
        if( !MongolianFontUtil.isMongolianFont(font) ) {
        	FontMetrics tmpfont = metrics;
        	metrics = defaultmetrics;
        	defaultmetrics = tmpfont;
        }
        tabSize = getTabSize() * metrics.charWidth('m');
    }

    // --- TabExpander methods ------------------------------------------

    /**
     * Returns the next tab stop position after a given reference position.
     * This implementation does not support things like centering so it
     * ignores the tabOffset argument.
     *
     * @param x the current position >= 0
     * @param tabOffset the position within the text stream
     *   that the tab occurred at >= 0.
     * @return the tab stop, measured in points >= 0
     */
    public float nextTabStop(float x, int tabOffset) {
        if (tabSize == 0)
            return x;
        int ntabs = ((int) x - tabBase) / tabSize;
        return tabBase + ((ntabs + 1) * tabSize);
    }


    // --- View methods -------------------------------------

    /**
     * Renders using the given rendering surface and area
     * on that surface.  This is implemented to stash the
     * selection positions, selection colors, and font
     * metrics for the nested lines to use.
     *
     * @param g the rendering surface to use
     * @param a the allocated region to render into
     *
     * @see MView#paint
     */
    public void paint(Graphics g, Shape a) {
    	
        Rectangle alloc = (Rectangle) a;
        tabBase = alloc.x;
        JTextComponent host = (JTextComponent) getContainer();
        sel0 = host.getSelectionStart();
        sel1 = host.getSelectionEnd();
        unselected = (host.isEnabled()) ?
            host.getForeground() : host.getDisabledTextColor();
        Caret c = host.getCaret();
        selected = c.isSelectionVisible() && host.getHighlighter() != null ?
                        host.getSelectedTextColor() : unselected;
        g.setFont(host.getFont());

        // superclass paints the children
    	super.paint(g, a);
    }

    /**
     * adjust clip for vertical right to left line mode.
     *
     * @param Shape shape
     * @param Shape allocation
     * @return Shape clipped shape
     */
    public Shape adjustedClip(Shape shape, Shape allocation) {
    	
        Rectangle clip = (shape instanceof Rectangle) ?
                (Rectangle)shape : shape.getBounds();

    	if( getRotateDirection() == MSwingRotateUtilities.ROTATE_VERTICAL 
    			&& getRotateHint() == MSwingRotateUtilities.ROTATE_RIGHTTOLEFT ) {
    		
            Rectangle alloc = (allocation instanceof Rectangle) ?
                    (Rectangle)allocation : allocation.getBounds();
            Insets insets = getContainer().getInsets();
            if( alloc.width > clip.width 
            		&& clip.x < (alloc.x+alloc.width-clip.width+insets.left+insets.right) ) {
	    		int x = alloc.x + alloc.width-clip.width - clip.x+insets.left+insets.right;
	    		clip.x = x;
            }    		
    	}
    	return clip;
	}
	
    /**
     * Sets the size of the view.  This should cause
     * layout of the view along the given axis, if it
     * has any layout duties.
     *
     * @param width the width >= 0
     * @param height the height >= 0
     */
    public void setSize(float width, float height) {
    	
    	if( getRotateDirection() == MSwingRotateUtilities.ROTATE_HORIZANTAL ) {
	        updateMetrics();
	        if ((int) width != getWidth() ) {
	            // invalidate the view itself since the childrens
	            // desired widths will be based upon this views width.
	            preferenceChanged(null, true, true);
	            widthChanging = true;
	        }
	        this.width = width;
	        this.height = height;
	        super.setSize(width, height);
	        widthChanging = false;
    	} else {
	        updateMetrics();
	        if ((int) height != getHeight() ) {
	            preferenceChanged(null, true, true);
	            widthChanging = true;
	        }
	        this.width = width;
	        this.height = height;
    		super.setSize(width, height);
	        widthChanging = false;
    	}
    }
    
    /**
     * Determines the preferred span for this view along an
     * axis.  This is implemented to provide the superclass
     * behavior after first making sure that the current font
     * metrics are cached (for the nested lines which use
     * the metrics to determine the height of the potentially
     * wrapped lines).
     *
     * @param axis may be either View.X_AXIS or View.Y_AXIS
     * @return  the span the view would like to be rendered into.
     *           Typically the view is told to render into the span
     *           that is returned, although there is no guarantee.
     *           The parent may choose to resize or break the view.
     * @see MView#getPreferredSpan
     */
    public float getPreferredSpan(int axis) {
        updateMetrics();
    	if( getRotateDirection() == MSwingRotateUtilities.ROTATE_HORIZANTAL ) {
    		return super.getPreferredSpan(axis);
    	} else {
			if( axis == MView.X_AXIS )
				return super.getPreferredSpan(MView.X_AXIS);
			else
				return super.getPreferredSpan(MView.Y_AXIS);
    	}
    }

    /**
     * Determines the minimum span for this view along an
     * axis.  This is implemented to provide the superclass
     * behavior after first making sure that the current font
     * metrics are cached (for the nested lines which use
     * the metrics to determine the height of the potentially
     * wrapped lines).
     *
     * @param axis may be either View.X_AXIS or View.Y_AXIS
     * @return  the span the view would like to be rendered into.
     *           Typically the view is told to render into the span
     *           that is returned, although there is no guarantee.
     *           The parent may choose to resize or break the view.
     * @see MView#getMinimumSpan
     */
    public float getMinimumSpan(int axis) {
        updateMetrics();
    	if( getRotateDirection() == MSwingRotateUtilities.ROTATE_HORIZANTAL ) {
    		return super.getMinimumSpan(axis);
    	} else {
			if( axis == MView.X_AXIS )
				return super.getMinimumSpan(MView.X_AXIS);
			else
				return super.getMinimumSpan(MView.Y_AXIS);
    	}
    }

    /**
     * Determines the maximum span for this view along an
     * axis.  This is implemented to provide the superclass
     * behavior after first making sure that the current font
     * metrics are cached (for the nested lines which use
     * the metrics to determine the height of the potentially
     * wrapped lines).
     *
     * @param axis may be either View.X_AXIS or View.Y_AXIS
     * @return  the span the view would like to be rendered into.
     *           Typically the view is told to render into the span
     *           that is returned, although there is no guarantee.
     *           The parent may choose to resize or break the view.
     * @see MView#getMaximumSpan
     */
    public float getMaximumSpan(int axis) {
        updateMetrics();
    	if( getRotateDirection() == MSwingRotateUtilities.ROTATE_HORIZANTAL ) {
    		return super.getMaximumSpan(axis);
    	} else {
			if( axis == MView.X_AXIS )
				return super.getMaximumSpan(MView.X_AXIS);
			else
				return super.getMaximumSpan(MView.Y_AXIS);
    	}
    }

    /**
     * Gives notification that something was inserted into the
     * document in a location that this view is responsible for.
     * This is implemented to simply update the children.
     *
     * @param e the change information from the associated document
     * @param a the current allocation of the view
     * @param f the factory to use to rebuild if the view has children
     * @see MView#insertUpdate
     */
    public void insertUpdate(DocumentEvent e, Shape a, ViewFactory f) {
        updateChildren(e, a);

        Rectangle alloc = ((a != null) && isAllocationValid()) ?
            getInsideAllocation(a) : null;
        int pos = e.getOffset();
        MView v = getViewAtPosition(pos, alloc);
        if (v != null) {
            v.insertUpdate(e, alloc, f);
        }
    }

    /**
     * Gives notification that something was removed from the
     * document in a location that this view is responsible for.
     * This is implemented to simply update the children.
     *
     * @param e the change information from the associated document
     * @param a the current allocation of the view
     * @param f the factory to use to rebuild if the view has children
     * @see MView#removeUpdate
     */
    public void removeUpdate(DocumentEvent e, Shape a, ViewFactory f) {
        updateChildren(e, a);

        Rectangle alloc = ((a != null) && isAllocationValid()) ?
            getInsideAllocation(a) : null;
        int pos = e.getOffset();
        MView v = getViewAtPosition(pos, alloc);
        if (v != null) {
            v.removeUpdate(e, alloc, f);
        }
    }

    /**
     * Gives notification from the document that attributes were changed
     * in a location that this view is responsible for.
     *
     * @param e the change information from the associated document
     * @param a the current allocation of the view
     * @param f the factory to use to rebuild if the view has children
     * @see MView#changedUpdate
     */
    public void changedUpdate(DocumentEvent e, Shape a, ViewFactory f) {
        updateChildren(e, a);
    }
    
    protected int getOffset(int axis, int childIndex) {
    	
    	if( getRotateDirection() == MSwingRotateUtilities.ROTATE_HORIZANTAL ) {
    		return super.getOffset(axis, childIndex);
    	} else {
    		// Jirimuto comment out --2020/07/19
    		//	if( axis == View.X_AXIS )
    		//		return super.getOffset(View.Y_AXIS, childIndex);
    		//	else
    		//		return super.getOffset(View.X_AXIS, childIndex);
    		return super.getOffset(axis, childIndex);
	}
    		
    }
    
    protected int getSpan(int axis, int childIndex) {
    	if( getRotateDirection() == MSwingRotateUtilities.ROTATE_HORIZANTAL ) {
    		return super.getSpan(axis, childIndex);
    	} else {
    		// Jirimuto comment out --2020/07/19
			//	if( axis == View.X_AXIS )
			//		return super.getSpan(View.Y_AXIS, childIndex);
			//	else
			//		return super.getSpan(View.X_AXIS, childIndex);
    		return super.getSpan(axis, childIndex);
    	}
    }
    
    /**
     * Allocates a region for a child view.
     *
     * @param index the index of the child view to
     *   allocate, >= 0 && < getViewCount()
     * @param alloc the allocated region
     */
    protected void childAllocation(int index, Rectangle alloc) {
        alloc.x += getOffset(X_AXIS, index);
        alloc.y += getOffset(Y_AXIS, index);
        alloc.width = getSpan(X_AXIS, index);
        alloc.height = getSpan(Y_AXIS, index);
    }

    public void setRotateHint(int hint) {
    	super.setRotateHint( hint );
    }

    public int getRotateHint(){
        return super.getRotateHint();
    }

    public void setRotateDirection(int direction){
    	super.setRotateDirection( direction );
		// Jirimuto comment out --2020/07/19
    	if( direction == MSwingRotateUtilities.ROTATE_HORIZANTAL )
    		super.setAxis(Y_AXIS);
   		else
    		super.setAxis(X_AXIS);
    }

    public int getRotateDirection(){
        return super.getRotateDirection();
    }

    /**
     * Calculates the size requirements for the major axis
     * <code>axis</code>.
     *
     * @param axis the axis being studied
     * @param r the <code>SizeRequirements</code> object;
     *          if <code>null</code> one will be created
     * @return the newly initialized <code>SizeRequirements</code> object
     * @see javax.swing.SizeRequirements
     */
    protected SizeRequirements calculateMajorAxisRequirements(int axis, SizeRequirements r) {
        // calculate tiled request
        float min = 0;
        float pref = 0;
        float max = 0;

    	if( getRotateDirection() == MSwingRotateUtilities.ROTATE_HORIZANTAL ) {
	        int n = getViewCount();
	        for (int i = 0; i < n; i++) {
	            MView v = getView(i);
	            min += v.getMinimumSpan(axis);
	            pref += v.getPreferredSpan(axis);
	            max += v.getMaximumSpan(axis);
	        }
	
	        if (r == null) {
	            r = new SizeRequirements();
	        }
	        r.alignment = 0.5f;
	        r.minimum = (int) min;
	        r.preferred = (int) pref;
	        r.maximum = (int) max;
	        return r;
	        
    	} else {
    		
	        int n = getViewCount();
	        for (int i = 0; i < n; i++) {
	            MView v = getView(i);
	            min += v.getMinimumSpan(axis);
	            pref += v.getPreferredSpan(axis);
	            max += v.getMaximumSpan(axis);
	        }
	
	        if (r == null) {
	            r = new SizeRequirements();
	        }
	        r.alignment = 0.5f;
	        r.minimum = (int) min;
	        r.preferred = (int) pref;
	        r.maximum = (int) max;
//	        if( r.preferred > 370 )
//	        	System.out.println("No = MajorAxis("+axis+")="+r.preferred);
//	        else
//	        	System.out.println("YES = MajorAxis("+axis+")="+r.preferred);
	        return r;
    	}
    }

    /**
     * Calculates the size requirements for the minor axis
     * <code>axis</code>.
     *
     * @param axis the axis being studied
     * @param r the <code>SizeRequirements</code> object;
     *          if <code>null</code> one will be created
     * @return the newly initialized <code>SizeRequirements</code> object
     * @see javax.swing.SizeRequirements
     */
    protected SizeRequirements calculateMinorAxisRequirements(int axis, SizeRequirements r) {
        int min = 0;
        long pref = 0;
        int max = Integer.MAX_VALUE;
        
    	if( getRotateDirection() == MSwingRotateUtilities.ROTATE_HORIZANTAL ) {
	        int n = getViewCount();
	        for (int i = 0; i < n; i++) {
	            MView v = getView(i);
	            min = Math.max((int) v.getMinimumSpan(axis), min);
	            pref = Math.max((int) v.getPreferredSpan(axis), pref);
	            max = Math.max((int) v.getMaximumSpan(axis), max);
	        }
	
	        if (r == null) {
	            r = new SizeRequirements();
	            r.alignment = 0.5f;
	        }
	        r.preferred = (int) pref;
	        r.minimum = min;
	        r.maximum = max;
	        return r;
    	} else {
	        int n = getViewCount();
	        for (int i = 0; i < n; i++) {
	            MView v = getView(i);
	            min = Math.max((int) v.getMinimumSpan(axis), min);
	            pref = Math.max((int) v.getPreferredSpan(axis), pref);
	            max = Math.max((int) v.getMaximumSpan(axis), max);
	        }
	
	        if (r == null) {
	            r = new SizeRequirements();
	            r.alignment = 0.5f;
	        }
	        r.preferred = (int) pref;
	        r.minimum = min;
	        r.maximum = max;
//	        if( r.preferred > 370 )
//	        	System.out.println("YES MinorAxis("+axis+")="+r.preferred);
//	        else
//	        	System.out.println("NO MinorAxis("+axis+")="+r.preferred);
	        return r;
    	}
    }

    /**
     * Provides a mapping from the document model coordinate space
     * to the coordinate space of the view mapped to it.  This makes
     * sure the allocation is valid before calling the superclass.
     *
     * @param pos the position to convert >= 0
     * @param a the allocated region to render into
     * @return the bounding box of the given position
     * @exception BadLocationException  if the given position does
     *  not represent a valid location in the associated document
     * @see MView#modelToView
     */
    public Shape modelToView(int pos, Shape a, Position.Bias b) throws BadLocationException {
        if (! isAllocationValid()) {
            Rectangle alloc = a.getBounds();
        	setSize(alloc.width, alloc.height);
        }
    	if( getRotateDirection() == MSwingRotateUtilities.ROTATE_HORIZANTAL ) {
    		return modelToViewHorizantal(pos, a, b);
    	} else if( getRotateHint() != MSwingRotateUtilities.ROTATE_RIGHTTOLEFT ){
    		return modelToViewVerticalL2R(pos, a, b);
    	} else {
    		return modelToViewVerticalR2L(pos, a, b);
    	}
    }

    private Shape modelToViewHorizantal(int pos, Shape a, Position.Bias b) throws BadLocationException {
        boolean isBackward = (b == Position.Bias.Backward);
        int testPos = (isBackward) ? Math.max(0, pos - 1) : pos;
        if(isBackward && testPos < getStartOffset()) {
            return null;
        }
        int vIndex = getViewIndexAtPosition(testPos);
        if ((vIndex != -1) && (vIndex < getViewCount())) {
            MView v = getView(vIndex);
            if(v != null && testPos >= v.getStartOffset() &&
               testPos < v.getEndOffset()) {
                Shape childShape = getChildAllocation(vIndex, a);
                if (childShape == null) {
                    // We are likely invalid, fail.
                    return null;
                }
                Shape retShape = v.modelToView(pos, childShape, b);
                if(retShape == null && v.getEndOffset() == pos) {
                    if(++vIndex < getViewCount()) {
                        v = getView(vIndex);
                        retShape = v.modelToView(pos, getChildAllocation(vIndex, a), b);
                    }
                }
                return retShape;
            }
        }
        throw new BadLocationException("Position not represented by view",
                                       pos);
    }

    private Shape modelToViewVerticalL2R(int pos, Shape a, Position.Bias b) throws BadLocationException {
        boolean isBackward = (b == Position.Bias.Backward);
        int testPos = (isBackward) ? Math.max(0, pos - 1) : pos;
        if(isBackward && testPos < getStartOffset()) {
            return null;
        }
        int vIndex = getViewIndexAtPosition(testPos);
        if ((vIndex != -1) && (vIndex < getViewCount())) {
            MView v = getView(vIndex);
            if(v != null && testPos >= v.getStartOffset() &&
               testPos < v.getEndOffset()) {
                Shape childShape = getChildAllocation(vIndex, a);
                if (childShape == null) {
                    // We are likely invalid, fail.
                    return null;
                }
                Shape retShape = v.modelToView(pos, childShape, b);
                if(retShape == null && v.getEndOffset() == pos) {
                    if(++vIndex < getViewCount()) {
                        v = getView(vIndex);
                        retShape = v.modelToView(pos, getChildAllocation(vIndex, a), b);
                    }
                }
                
                Rectangle alloc = retShape.getBounds();
            	JTextComponent c = (JTextComponent)this.getContainer();
                c.putClientProperty("caretWidth", new Integer(alloc.width));
                c.repaint();
                
                return retShape;
            }
        }
        throw new BadLocationException("Position not represented by view",
                                       pos);
    }

    private Shape modelToViewVerticalR2L(int pos, Shape a, Position.Bias b) throws BadLocationException {
        boolean isBackward = (b == Position.Bias.Backward);
        int testPos = (isBackward) ? Math.max(0, pos - 1) : pos;
        if(isBackward && testPos < getStartOffset()) {
            return null;
        }
        int vIndex = getViewIndexAtPosition(testPos);
        if ((vIndex != -1) && (vIndex < getViewCount())) {
            MView v = getView(vIndex);
            if(v != null && testPos >= v.getStartOffset() &&
               testPos < v.getEndOffset()) {
                Shape childShape = getChildAllocation(vIndex, a);
                if (childShape == null) {
                    // We are likely invalid, fail.
                    return null;
                }
                Shape retShape = v.modelToView(pos, childShape, b);
                if(retShape == null && v.getEndOffset() == pos) {
                    if(++vIndex < getViewCount()) {
                        v = getView(vIndex);
                        retShape = v.modelToView(pos, getChildAllocation(vIndex, a), b);
                    }
                }
                
                Rectangle alloc = retShape.getBounds();
            	JTextComponent c = (JTextComponent)this.getContainer();
                c.putClientProperty("caretWidth", new Integer(alloc.width));
                c.repaint();

                return retShape;
            }
        }
        throw new BadLocationException("Position not represented by view",
                                       pos);
    }

    /**
     * Provides a mapping from the view coordinate space to the logical
     * coordinate space of the model.
     *
     * @param x   x coordinate of the view location to convert >= 0
     * @param y   y coordinate of the view location to convert >= 0
     * @param a the allocated region to render into
     * @return the location within the model that best represents the
     *  given point in the view >= 0
     * @see MView#viewToModel
     */
    public int viewToModel(float x, float y, Shape a, Position.Bias[] bias) {
        if (! isAllocationValid()) {
            Rectangle alloc = a.getBounds();
    		setSize(alloc.width, alloc.height);
        }
    	if( getRotateDirection() == MSwingRotateUtilities.ROTATE_HORIZANTAL ) {
            return this.viewToModelHorizantal(x, y, a, bias);
    	} else if( getRotateHint() != MSwingRotateUtilities.ROTATE_RIGHTTOLEFT ){
            return this.viewToModelVerticalL2R(x, y, a, bias);
    	} else {
            return this.viewToModelVerticalR2L(x, y, a, bias);
    	}
    }

    private int viewToModelHorizantal(float x, float y, Shape a, Position.Bias[] bias) {
        Rectangle alloc = getInsideAllocation(a);
        if (isBefore((int) x, (int) y, alloc)) {
            // point is before the range represented
            int retValue = -1;

            try {
                retValue = getNextVisualPositionFrom(-1, Position.Bias.Forward,
                                                     a, EAST, bias);
            } catch (BadLocationException ble) { }
            catch (IllegalArgumentException iae) { }
            if(retValue == -1) {
                retValue = getStartOffset();
                bias[0] = Position.Bias.Forward;
            }
            return retValue;
        } else if (isAfter((int) x, (int) y, alloc)) {
            // point is after the range represented.
            int retValue = -1;
            try {
                retValue = getNextVisualPositionFrom(-1, Position.Bias.Forward,
                                                     a, WEST, bias);
            } catch (BadLocationException ble) { }
            catch (IllegalArgumentException iae) { }

            if(retValue == -1) {
                // NOTE: this could actually use end offset with backward.
                retValue = getEndOffset() - 1;
                bias[0] = Position.Bias.Forward;
            }
            return retValue;
        } else {
            // locate the child and pass along the request
            MView v = getViewAtPoint((int) x, (int) y, alloc);
            if (v != null) {
              return v.viewToModel(x, y, alloc, bias);
            }
        }
        return -1;
    }

    private int viewToModelVerticalL2R(float x, float y, Shape a, Position.Bias[] bias) {
        Rectangle alloc = getInsideAllocation(a);
        if (isBefore((int) x, (int) y, alloc)) {
            // point is before the range represented
            int retValue = -1;

            try {
                retValue = getNextVisualPositionFrom(-1, Position.Bias.Forward,
                                                     a, EAST, bias);
            } catch (BadLocationException ble) { }
            catch (IllegalArgumentException iae) { }
            if(retValue == -1) {
                retValue = getStartOffset();
                bias[0] = Position.Bias.Forward;
            }
            return retValue;
        } else if (isAfter((int) x, (int) y, alloc)) {
            // point is after the range represented.
            int retValue = -1;
            try {
                retValue = getNextVisualPositionFrom(-1, Position.Bias.Forward,
                                                     a, WEST, bias);
            } catch (BadLocationException ble) { }
            catch (IllegalArgumentException iae) { }

            if(retValue == -1) {
                // NOTE: this could actually use end offset with backward.
                retValue = getEndOffset() - 1;
                bias[0] = Position.Bias.Forward;
            }
            return retValue;
        } else {
            // locate the child and pass along the request
            MView v = getViewAtPoint((int) x, (int) y, alloc);
            if (v != null) {
              return v.viewToModel(x, y, alloc, bias);
            }
        }
        return -1;
    }

    private int viewToModelVerticalR2L(float x, float y, Shape a, Position.Bias[] bias) {
        Rectangle alloc = getInsideAllocation(a);
        if (isAfter((int) x, (int) y, alloc)) {
            // point is before the range represented
            int retValue = -1;

            try {
                retValue = getNextVisualPositionFrom(-1, Position.Bias.Forward,
                                                     a, EAST, bias);
            } catch (BadLocationException ble) { }
            catch (IllegalArgumentException iae) { }
            if(retValue == -1) {
                retValue = getStartOffset();
                bias[0] = Position.Bias.Forward;
            }
            return retValue;
        } else if (isBefore((int) x, (int) y, alloc)) {
            // point is after the range represented.
            int retValue = -1;
            try {
                retValue = getNextVisualPositionFrom(-1, Position.Bias.Forward,
                                                     a, WEST, bias);
            } catch (BadLocationException ble) { }
            catch (IllegalArgumentException iae) { }

            if(retValue == -1) {
                // NOTE: this could actually use end offset with backward.
                retValue = getEndOffset() - 1;
                bias[0] = Position.Bias.Forward;
            }
            return retValue;
        } else {
            // locate the child and pass along the request
            MView v = getViewAtPoint((int) x, (int) y, alloc);
            if (v != null) {
              return v.viewToModel(x, y, alloc, bias);
            }
        }
        return -1;
    }


    /**
     * Provides a mapping from the document model coordinate space
     * to the coordinate space of the view mapped to it.
     *
     * @param p0 the position to convert >= 0
     * @param b0 the bias toward the previous character or the
     *  next character represented by p0, in case the
     *  position is a boundary of two views; either
     *  <code>Position.Bias.Forward</code> or
     *  <code>Position.Bias.Backward</code>
     * @param p1 the position to convert >= 0
     * @param b1 the bias toward the previous character or the
     *  next character represented by p1, in case the
     *  position is a boundary of two views
     * @param a the allocated region to render into
     * @return the bounding box of the given position is returned
     * @exception BadLocationException  if the given position does
     *   not represent a valid location in the associated document
     * @exception IllegalArgumentException for an invalid bias argument
     * @see MView#viewToModel
     */
    public Shape modelToView(int p0, Position.Bias b0, int p1, Position.Bias b1, Shape a) throws BadLocationException {
    	
    	if( getRotateDirection() == MSwingRotateUtilities.ROTATE_HORIZANTAL ) {
            return this.modelToViewHorizantal(p0, b0, p1, b1, a);
    	} else if( getRotateHint() != MSwingRotateUtilities.ROTATE_RIGHTTOLEFT ){
            return this.modelToViewVerticalL2R(p0, b0, p1, b1, a);
    	} else {
            return this.modelToViewVerticalR2L(p0, b0, p1, b1, a);
    	}
    }
    
    private Shape modelToViewHorizantal(int p0, Position.Bias b0, int p1, Position.Bias b1, Shape a) throws BadLocationException {
        if (p0 == getStartOffset() && p1 == getEndOffset()) {
            return a;
        }
        Rectangle alloc = getInsideAllocation(a);
        Rectangle r0 = new Rectangle(alloc);
        MView v0 = getViewAtPosition((b0 == Position.Bias.Backward) ?
                                    Math.max(0, p0 - 1) : p0, r0);
        Rectangle r1 = new Rectangle(alloc);
        MView v1 = getViewAtPosition((b1 == Position.Bias.Backward) ?
                                    Math.max(0, p1 - 1) : p1, r1);
        if (v0 == v1) {
            if (v0 == null) {
                return a;
            }
            // Range contained in one view
            return v0.modelToView(p0, b0, p1, b1, r0);
        }
        // Straddles some views.
        int viewCount = getViewCount();
        int counter = 0;
        while (counter < viewCount) {
            MView v;
            // Views may not be in same order as model.
            // v0 or v1 may be null if there is a gap in the range this
            // view contains.
            if ((v = getView(counter)) == v0 || v == v1) {
                MView endView;
                Rectangle retRect;
                Rectangle tempRect = new Rectangle();
                if (v == v0) {
                    retRect = v0.modelToView(p0, b0, v0.getEndOffset(),
                                             Position.Bias.Backward, r0).
                              getBounds();
                    endView = v1;
                }
                else {
                    retRect = v1.modelToView(v1.getStartOffset(),
                                             Position.Bias.Forward,
                                             p1, b1, r1).getBounds();
                    endView = v0;
                }

                // Views entirely covered by range.
                while (++counter < viewCount &&
                       (v = getView(counter)) != endView) {
                    tempRect.setBounds(alloc);
                    childAllocation(counter, tempRect);
                    retRect.add(tempRect);
                }

                // End view.
                if (endView != null) {
                    Shape endShape;
                    if (endView == v1) {
                        endShape = v1.modelToView(v1.getStartOffset(),
                                                  Position.Bias.Forward,
                                                  p1, b1, r1);
                    }
                    else {
                        endShape = v0.modelToView(p0, b0, v0.getEndOffset(),
                                                  Position.Bias.Backward, r0);
                    }
                    if (endShape instanceof Rectangle) {
                        retRect.add((Rectangle)endShape);
                    }
                    else {
                        retRect.add(endShape.getBounds());
                    }
                }
                return retRect;
            }
            counter++;
        }
        throw new BadLocationException("Position not represented by view", p0);
    }

    private Shape modelToViewVerticalL2R(int p0, Position.Bias b0, int p1, Position.Bias b1, Shape a) throws BadLocationException {
        if (p0 == getStartOffset() && p1 == getEndOffset()) {
            return a;
        }
        Rectangle alloc = getInsideAllocation(a);
        Rectangle r0 = new Rectangle(alloc);
        MView v0 = getViewAtPosition((b0 == Position.Bias.Backward) ?
                                    Math.max(0, p0 - 1) : p0, r0);
        int tmp = r0.x;
        r0.x = r0.y;
        r0.y = tmp;
        tmp = r0.width;
        r0.width = r0.height;
        r0.height = tmp;
        
        Rectangle r1 = new Rectangle(alloc);
        MView v1 = getViewAtPosition((b1 == Position.Bias.Backward) ?
                                    Math.max(0, p1 - 1) : p1, r1);
        tmp = r1.x;
        r1.x = r1.y;
        r1.y = tmp;
        tmp = r1.width;
        r1.width = r1.height;
        r1.height = tmp;
        
        if (v0 == v1) {
            if (v0 == null) {
                return a;
            }
            // Range contained in one view
            return v0.modelToView(p0, b0, p1, b1, r0);
        }
        // Straddles some views.
        int viewCount = getViewCount();
        int counter = 0;
        while (counter < viewCount) {
            MView v;
            // Views may not be in same order as model.
            // v0 or v1 may be null if there is a gap in the range this
            // view contains.
            if ((v = getView(counter)) == v0 || v == v1) {
                MView endView;
                Rectangle retRect;
                Rectangle tempRect = new Rectangle();
                if (v == v0) {
                    retRect = v0.modelToView(p0, b0, v0.getEndOffset(),
                                             Position.Bias.Backward, r0).
                              getBounds();
                    endView = v1;
                }
                else {
                    retRect = v1.modelToView(v1.getStartOffset(),
                                             Position.Bias.Forward,
                                             p1, b1, r1).getBounds();
                    endView = v0;
                }

                // Views entirely covered by range.
                while (++counter < viewCount &&
                       (v = getView(counter)) != endView) {
                    tempRect.setBounds(alloc);
                    childAllocation(counter, tempRect);
                    retRect.add(tempRect);
                }

                // End view.
                if (endView != null) {
                    Shape endShape;
                    if (endView == v1) {
                        endShape = v1.modelToView(v1.getStartOffset(),
                                                  Position.Bias.Forward,
                                                  p1, b1, r1);
                    }
                    else {
                        endShape = v0.modelToView(p0, b0, v0.getEndOffset(),
                                                  Position.Bias.Backward, r0);
                    }
                    if (endShape instanceof Rectangle) {
                        retRect.add((Rectangle)endShape);
                    }
                    else {
                        retRect.add(endShape.getBounds());
                    }
                }
                return retRect;
            }
            counter++;
        }
        throw new BadLocationException("Position not represented by view", p0);
    }

    private Shape modelToViewVerticalR2L(int p0, Position.Bias b0, int p1, Position.Bias b1, Shape a) throws BadLocationException {
        if (p0 == getStartOffset() && p1 == getEndOffset()) {
            return a;
        }
        Rectangle alloc = getInsideAllocation(a);
        Rectangle r0 = new Rectangle(alloc);
        MView v0 = getViewAtPosition((b0 == Position.Bias.Backward) ?
                                    Math.max(0, p0 - 1) : p0, r0);
        Rectangle r1 = new Rectangle(alloc);
        MView v1 = getViewAtPosition((b1 == Position.Bias.Backward) ?
                                    Math.max(0, p1 - 1) : p1, r1);
        if (v0 == v1) {
            if (v0 == null) {
                return a;
            }
            // Range contained in one view
            return v0.modelToView(p0, b0, p1, b1, r0);
        }
        // Straddles some views.
        int viewCount = getViewCount();
        int counter = 0;
        while (counter < viewCount) {
            MView v;
            // Views may not be in same order as model.
            // v0 or v1 may be null if there is a gap in the range this
            // view contains.
            if ((v = getView(counter)) == v0 || v == v1) {
                MView endView;
                Rectangle retRect;
                Rectangle tempRect = new Rectangle();
                if (v == v0) {
                    retRect = v0.modelToView(p0, b0, v0.getEndOffset(),
                                             Position.Bias.Backward, r0).
                              getBounds();
                    endView = v1;
                }
                else {
                    retRect = v1.modelToView(v1.getStartOffset(),
                                             Position.Bias.Forward,
                                             p1, b1, r1).getBounds();
                    endView = v0;
                }

                // Views entirely covered by range.
                while (++counter < viewCount &&
                       (v = getView(counter)) != endView) {
                    tempRect.setBounds(alloc);
                    childAllocation(counter, tempRect);
                    retRect.add(tempRect);
                }

                // End view.
                if (endView != null) {
                    Shape endShape;
                    if (endView == v1) {
                        endShape = v1.modelToView(v1.getStartOffset(),
                                                  Position.Bias.Forward,
                                                  p1, b1, r1);
                    }
                    else {
                        endShape = v0.modelToView(p0, b0, v0.getEndOffset(),
                                                  Position.Bias.Backward, r0);
                    }
                    if (endShape instanceof Rectangle) {
                        retRect.add((Rectangle)endShape);
                    }
                    else {
                        retRect.add(endShape.getBounds());
                    }
                }
                return retRect;
            }
            counter++;
        }
        throw new BadLocationException("Position not represented by view", p0);
    }


    /**
     * Fetches the child view at the given coordinates.
     *
     * @param x the X coordinate >= 0
     * @param y the Y coordinate >= 0
     * @param alloc the parents inner allocation on entry, which should
     *   be changed to the childs allocation on exit
     * @return the view
     */
    protected MView getViewAtPoint(int x, int y, Rectangle alloc) {
        int n = getViewCount();
    	if( getRotateDirection() == MSwingRotateUtilities.ROTATE_HORIZANTAL ) {
            if (y < (alloc.y + majorOffsets[0])) {
                childAllocation(0, alloc);
                return getView(0);
            }
            for (int i = 0; i < n; i++) {
                if (y < (alloc.y + majorOffsets[i])) {
                    childAllocation(i - 1, alloc);
                    return getView(i - 1);
                }
            }
            childAllocation(n - 1, alloc);
            return getView(n - 1);
    	} else if( getRotateHint() != MSwingRotateUtilities.ROTATE_RIGHTTOLEFT ){
	        if (x < (alloc.x + majorOffsets[0])) {
	            childAllocation(0, alloc);
	            return getView(0);
	        }
	        for (int i = 0; i < n; i++) {
	            if (x < (alloc.x + majorOffsets[i])) {
	                childAllocation(i - 1, alloc);
	                return getView(i - 1);
	            }
	        }
	        childAllocation(n - 1, alloc);
	        return getView(n - 1);
    	} else {
    		int w = (int)width;
            if (x > (alloc.x + w - majorOffsets[0])) {
                childAllocation(0, alloc);
                return getView(0);
            }
            for (int i = 0; i < n; i++) {
                if (x > (alloc.x + w - majorOffsets[i])) {
                    childAllocation(i-1, alloc);
                    return getView(i-1);
                }
            }
            childAllocation(n - 1, alloc);
            return getView(n - 1);
    		
    	}
    }

    /**
     * Provides a way to determine the next visually represented model
     * location at which one might place a caret.
     * Some views may not be visible,
     * they might not be in the same order found in the model, or they just
     * might not allow access to some of the locations in the model.
     *
     * @param pos the position to convert >= 0
     * @param a the allocated region in which to render
     * @param direction the direction from the current position that can
     *  be thought of as the arrow keys typically found on a keyboard.
     *  This will be one of the following values:
     * <ul>
     * <li>SwingConstants.WEST
     * <li>SwingConstants.EAST
     * <li>SwingConstants.NORTH
     * <li>SwingConstants.SOUTH
     * </ul>
     * @return the location within the model that best represents the next
     *  location visual position
     * @exception BadLocationException
     * @exception IllegalArgumentException if <code>direction</code>
     *          doesn't have one of the legal values above
     */
    public int getNextVisualPositionFrom(int pos, Position.Bias b, Shape a,
                                         int direction, Position.Bias[] biasRet)
      throws BadLocationException {
        if (pos < -1) {
            // -1 is a reserved value, see the code below
            throw new BadLocationException("Invalid position", pos);
        }
    	if( getRotateDirection() == MSwingRotateUtilities.ROTATE_HORIZANTAL ) {
    		pos = getNextVisualPositionFromHorizantal(pos, b, a, direction, biasRet);
    		
    	} else if( getRotateHint() != MSwingRotateUtilities.ROTATE_RIGHTTOLEFT ) {
    		
    		pos = getNextVisualPositionFromVerticalL2R(pos, b, a, direction, biasRet);
    	} else {
    		
    		pos = getNextVisualPositionFromVerticalR2L(pos, b, a, direction, biasRet);
    	}

		JTextComponent target = (JTextComponent) getContainer();
		Caret c = (target != null) ? target.getCaret() : null;
		Rectangle r = target.modelToView(pos);
		
		Point mcp;
		if (c != null && r != null ) {
			mcp = c.getMagicCaretPosition();
			if( mcp != null ){
				mcp.y = r.y;
				c.setMagicCaretPosition(mcp);
			}
		}
    	
    	return pos;
    }
    
    protected int getNextVisualPositionFromHorizantal(int pos, Position.Bias b,
			Shape a, int direction, Position.Bias[] biasRet)
			throws BadLocationException {
		if (pos < -1) {
			// -1 is a reserved value, see the code below
			throw new BadLocationException("Invalid position", pos);
		}

		biasRet[0] = Position.Bias.Forward;
		switch (direction) {
		case NORTH:
		case SOUTH: {
			if (pos == -1) {
				pos = (direction == NORTH) ? Math.max(0, getEndOffset() - 1)
						: getStartOffset();
				break;
			}
			JTextComponent target = (JTextComponent) getContainer();
			Caret c = (target != null) ? target.getCaret() : null;
			// YECK! Ideally, the x location from the magic caret position
			// would be passed in.
			Point mcp;
			if (c != null) {
				mcp = c.getMagicCaretPosition();
			} else {
				mcp = null;
			}
			int x;
			if (mcp == null) {
				Rectangle loc = target.modelToView(pos);
				x = (loc == null) ? 0 : loc.x;
			} else {
				x = mcp.x;
			}
			if (direction == NORTH) {
				pos = MUtilities.getPositionAbove(target, pos, x);
			} else {
				pos = MUtilities.getPositionBelow(target, pos, x);
			}
		}
			break;
		case WEST:
			if (pos == -1) {
				pos = Math.max(0, getEndOffset() - 1);
			} else {
				pos = Math.max(0, pos - 1);
			}
			break;
		case EAST:
			if (pos == -1) {
				pos = getStartOffset();
			} else {
				pos = Math.min(pos + 1, getDocument().getLength());
			}
			break;
		default:
			throw new IllegalArgumentException("Bad direction: " + direction);
		}
		return pos;
	}
		
    private int getNextVisualPositionFromVerticalL2R(int pos, Position.Bias b, Shape a,
                                         int direction, Position.Bias[] biasRet)
      throws BadLocationException {
        if (pos < -1) {
            // -1 is a reserved value, see the code below
            throw new BadLocationException("Invalid position", pos);
        }

        biasRet[0] = Position.Bias.Forward;
        switch (direction) {
        case EAST:
        case WEST:
        {
            if (pos == -1) {
                pos = (direction == WEST) ? Math.max(0, getEndOffset() - 1) :
                    getStartOffset();
                break;
            }
            JTextComponent target = (JTextComponent) getContainer();
            Caret c = (target != null) ? target.getCaret() : null;
            // YECK! Ideally, the x location from the magic caret position
            // would be passed in.
            Point mcp;
            if (c != null) {
                mcp = c.getMagicCaretPosition();
            }
            else {
                mcp = null;
            }
            int y;
            if (mcp == null) {
                Rectangle loc = target.modelToView(pos);
                y = (loc == null) ? 0 : loc.y;
            }
            else {
                y = mcp.y;
            }
            if (direction == WEST) {
                pos = MUtilities.getPositionAboveL2R(target, pos, y);
            }
            else {
                pos = MUtilities.getPositionBelowL2R(target, pos, y);
            }
        }
            break;
        case NORTH:
            if(pos == -1) {
                pos = Math.max(0, getEndOffset() - 1);
            }
            else {
                pos = Math.max(0, pos - 1);
            }
            break;
        case SOUTH:
            if(pos == -1) {
                pos = getStartOffset();
            }
            else {
                pos = Math.min(pos + 1, getDocument().getLength());
            }
            break;
        default:
            throw new IllegalArgumentException("Bad direction: " + direction);
        }
        return pos;
    }

    private int getNextVisualPositionFromVerticalR2L(int pos, Position.Bias b,
			Shape a, int direction, Position.Bias[] biasRet)
			throws BadLocationException {
		if (pos < -1) {
			// -1 is a reserved value, see the code below
			throw new BadLocationException("Invalid position", pos);
		}

		biasRet[0] = Position.Bias.Forward;
		switch (direction) {
		case WEST:
		case EAST: {
			if (pos == -1) {
				pos = (direction == EAST) ? Math.max(0, getEndOffset() - 1)
						: getStartOffset();
				break;
			}
			JTextComponent target = (JTextComponent) getContainer();
			Caret c = (target != null) ? target.getCaret() : null;
			// YECK! Ideally, the x location from the magic caret position
			// would be passed in.
			Point mcp;
			if (c != null) {
				mcp = c.getMagicCaretPosition();
			} else {
				mcp = null;
			}
			int y;
			if (mcp == null) {
				Rectangle loc = target.modelToView(pos);
				y = (loc == null) ? 0 : loc.y;
			} else {
				y = mcp.y;
			}
			if (direction == EAST) {
				pos = MUtilities.getPositionAboveR2L(target, pos, y);
			} else {
				pos = MUtilities.getPositionBelowR2L(target, pos, y);
			}
		}
			break;
		case NORTH:
			if (pos == -1) {
				pos = Math.max(0, getEndOffset() - 1);
			} else {
				pos = Math.max(0, pos - 1);
			}
			break;
		case SOUTH:
			if (pos == -1) {
				pos = getStartOffset();
			} else {
				pos = Math.min(pos + 1, getDocument().getLength());
			}
			break;
		default:
			throw new IllegalArgumentException("Bad direction: " + direction);
		}
		return pos;
	}

    // --- variables -------------------------------------------

    FontMetrics metrics;
    FontMetrics defaultmetrics;
    Segment lineBuffer;
    boolean widthChanging;
    int tabBase;
    int tabSize;
    boolean wordWrap;

    int sel0;
    int sel1;
    Color unselected;
    Color selected;


    /**
     * Simple view of a line that wraps if it doesn't
     * fit withing the horizontal space allocated.
     * This class tries to be lightweight by carrying little
     * state of it's own and sharing the state of the outer class
     * with it's sibblings.
     */
    class MWrappedLine extends MView {

        MWrappedLine(Element elem, int rotate_direction, int rotate_hint) {
            super(elem);
            lineCount = -1;
            super.setRotateDirection(rotate_direction);
            super.setRotateHint(rotate_hint);
        }

        /**
         * Determines the preferred span for this view along an
         * axis.
         *
         * @param axis may be either X_AXIS or Y_AXIS
         * @return   the span the view would like to be rendered into.
         *           Typically the view is told to render into the span
         *           that is returned, although there is no guarantee.
         *           The parent may choose to resize or break the view.
         * @see MView#getPreferredSpan
         */
        public float getPreferredSpan(int axis) {
        	if( super.getRotateDirection() == MSwingRotateUtilities.ROTATE_HORIZANTAL ) {
	            switch (axis) {
	            case MView.X_AXIS:
	                float width = getWidth();
	                if (width == Integer.MAX_VALUE) {
	                    // We have been initially set to MAX_VALUE, but we don't
	                    // want this as our preferred.
	                    return 100f;
	                }
	                return width;
	            case MView.Y_AXIS:
	                if (lineCount < 0 || widthChanging) {
	                	breakLines(getStartOffset());
	                }
	                return lineCount * metrics.getHeight();
	            default:
	                throw new IllegalArgumentException("Invalid axis: " + axis);
	            }
        	} else {
	            switch (axis) {
	            case MView.Y_AXIS:
	                float height = getHeight();
	                if (height == Integer.MAX_VALUE) {
	                    // We have been initially set to MAX_VALUE, but we don't
	                    // want this as our preferred.
	                    return 100f;
	                }
	                return height;
	            case MView.X_AXIS:
	                if (lineCount < 0 || widthChanging) {
	                	breakLines(getStartOffset());
	                }
//	                System.out.println("linecount="+lineCount);
	                return lineCount * metrics.getHeight();
	            default:
	                throw new IllegalArgumentException("Invalid axis: " + axis);
	            }
        	}
        }

        /**
         * Renders using the given rendering surface and area on that
         * surface.  The view may need to do layout and create child
         * views to enable itself to render into the given allocation.
         *
         * @param g the rendering surface to use
         * @param a the allocated region to render into
         * @see MView#paint
         */
        public void paint(Graphics g, Shape a) {
        	
        	if( super.getRotateDirection() == MSwingRotateUtilities.ROTATE_HORIZANTAL ) {
        		paintHorizantal(g, a);
	        } else if ( super.getRotateHint() != MSwingRotateUtilities.ROTATE_RIGHTTOLEFT ){
	        	
	    		Graphics2D g2d = (Graphics2D)g;
	    		AffineTransform transform = g2d.getTransform();
	    		
		        ((Graphics2D) g).rotate(Math.PI * 1 / 2);
		        
		        paintVerticalL2R(g, a);
	    		
	        	g2d.setTransform(transform);
	        	
	        } else {
	        	
	    		Graphics2D g2d = (Graphics2D)g;
	    		AffineTransform transform = g2d.getTransform();
	    		
		        ((Graphics2D) g).rotate(Math.PI * 1 / 2);
		        float w = width;
	            g2d.translate(0, -w);
	    		
	            paintVerticalR2L(g, a);
	        	
	        	g2d.setTransform(transform);
	        	
	        }
	    	
        }
        /**
         * Renders using the given rendering surface and area on that
         * surface.  The view may need to do layout and create child
         * views to enable itself to render into the given allocation.
         *
         * @param g the rendering surface to use
         * @param a the allocated region to render into
         * @see MView#paint
         */
        private void paintHorizantal(Graphics g, Shape a) {
        	
            Rectangle alloc = (Rectangle) a;
            int y = alloc.y + metrics.getAscent();
            int x = alloc.x;

            JTextComponent host = (JTextComponent)getContainer();
            Highlighter h = host.getHighlighter();
            LayeredHighlighter dh = (h instanceof LayeredHighlighter) ?
                                     (LayeredHighlighter)h : null;

            int start = getStartOffset();
            int end = getEndOffset();
            int p0 = start;
            int[] lineEnds = getLineEnds();
            for (int i = 0; i < lineCount; i++) {
                int p1 = (lineEnds == null) ? end :
                                             start + lineEnds[i];
                if (dh != null) {
                    int hOffset = (p1 == end)
                                  ? (p1 - 1)
                                  : p1;
                    dh.paintLayeredHighlights(g, p0, hOffset, a, host, this);
                }
                drawLine(p0, p1, g, x, y);

                p0 = p1;
                y += metrics.getHeight();
            }
        }

        private void paintVerticalL2R(Graphics g, Shape a) {
        	
            Rectangle alloc = new Rectangle((Rectangle) a);
            int tmp = alloc.x;
            alloc.x = alloc.y;
            alloc.y = tmp;
            tmp = alloc.width;
            alloc.width = alloc.height;
            alloc.height = tmp;
            
            int y = -alloc.y ;
            int x = alloc.x;

            JTextComponent host = (JTextComponent)getContainer();
            Highlighter h = host.getHighlighter();
            LayeredHighlighter dh = (h instanceof LayeredHighlighter) ?
                                     (LayeredHighlighter)h : null;

            int start = getStartOffset();
            int end = getEndOffset();
            int p0 = start;
            int[] lineEnds = getLineEnds();
            for (int i = 0; i < lineCount; i++) {
                int p1 = (lineEnds == null) ? end :
                                             start + lineEnds[i];
                if (dh != null) {
                    int hOffset = (p1 == end)
                                  ? (p1 - 1)
                                  : p1;
                    dh.paintLayeredHighlights(g, p0, hOffset, a, host, this);
                }
                drawLine(p0, p1, g, x, y);

                p0 = p1;
                y -= metrics.getHeight();
            }
        }
        
        private void paintVerticalR2L(Graphics g, Shape a) {
        	
            Rectangle alloc = (Rectangle) a;
            int tmp = alloc.x;
            alloc.x = alloc.y;
            alloc.y = tmp;
            tmp = alloc.width;
            alloc.width = alloc.height;
            alloc.height = tmp;
            
            int y = alloc.y + metrics.getAscent();
            int x = alloc.x;

            JTextComponent host = (JTextComponent)getContainer();
            Highlighter h = host.getHighlighter();
            LayeredHighlighter dh = (h instanceof LayeredHighlighter) ?
                                     (LayeredHighlighter)h : null;

            int start = getStartOffset();
            int end = getEndOffset();
            int p0 = start;
            int[] lineEnds = getLineEnds();
            for (int i = 0; i < lineCount; i++) {
                int p1 = (lineEnds == null) ? end :
                                             start + lineEnds[i];
                if (dh != null) {
                    int hOffset = (p1 == end)
                                  ? (p1 - 1)
                                  : p1;
                    dh.paintLayeredHighlights(g, p0, hOffset, a, host, this);
                }
                drawLine(p0, p1, g, x, y);

                p0 = p1;
                y += metrics.getHeight();
            }
        }
        /**
         * Provides a mapping from the document model coordinate space
         * to the coordinate space of the view mapped to it.
         *
         * @param pos the position to convert
         * @param a the allocated region to render into
         * @return the bounding box of the given position is returned
         * @exception BadLocationException  if the given position does not represent a
         *   valid location in the associated document
         * @see MView#modelToView
         */
        public Shape modelToView(int pos, Shape a, Position.Bias b)
                throws BadLocationException {
        	
        	if( super.getRotateDirection() == MSwingRotateUtilities.ROTATE_HORIZANTAL ) {
        		return this.modelToViewHorizantal(pos, a, b);
        	} else if ( super.getRotateHint() != MSwingRotateUtilities.ROTATE_RIGHTTOLEFT ) {
        		return this.modelToViewVerticalL2R(pos, a, b);
        	} else {
        		return this.modelToViewVerticalR2L(pos, a, b);
        	}
        }
        
        private Shape modelToViewHorizantal(int pos, Shape a, Position.Bias b)
                throws BadLocationException {
        
            Rectangle alloc = a.getBounds();
            alloc.height = metrics.getHeight();
            alloc.width = 1;

            int p0 = getStartOffset();
            if (pos < p0 || pos > getEndOffset()) {
                throw new BadLocationException("Position out of range", pos);
            }

            int testP = (b == Position.Bias.Forward) ? pos :
                        Math.max(p0, pos - 1);
            int line = 0;
            int[] lineEnds = getLineEnds();
            if (lineEnds != null) {
                line = findLine(testP - p0);
                if (line > 0) {
                    p0 += lineEnds[line - 1];
                }
                alloc.y += alloc.height * line;
            }

            if (pos > p0) {
                Segment segment = MSegmentCache.getSharedSegment();
                loadText(segment, p0, pos);
                alloc.x += MUtilities.getTabbedTextWidth(segment, metrics,
                        alloc.x, MWrappedPlainView.this, p0);
                MSegmentCache.releaseSharedSegment(segment);
            }
            return alloc;
        }

        private Shape modelToViewVerticalL2R(int pos, Shape a, Position.Bias b)
                throws BadLocationException {
        
            Rectangle alloc = a.getBounds();
            
            alloc.width = metrics.getHeight();
            alloc.height = 1;

            int p0 = getStartOffset();
            if (pos < p0 || pos > getEndOffset()) {
                throw new BadLocationException("Position out of range", pos);
            }

            int testP = (b == Position.Bias.Forward) ? pos :
                        Math.max(p0, pos - 1);
            int line = 0;
            int[] lineEnds = getLineEnds();
            if (lineEnds != null) {
                line = findLine(testP - p0);
                if (line > 0) {
                    p0 += lineEnds[line - 1];
                }
                alloc.x += metrics.getHeight()/2 +(alloc.width * line);
            } else {
                alloc.x += metrics.getHeight()/2;
            }

            if (pos > p0) {
                Segment segment = MSegmentCache.getSharedSegment();
                loadText(segment, p0, pos);
                alloc.y += MUtilities.getTabbedTextWidth(MWrappedPlainView.this, segment, metrics, defaultmetrics,
                        alloc.y, MWrappedPlainView.this, p0, null );
                MSegmentCache.releaseSharedSegment(segment);
            }
            return alloc;
        }

        private Shape modelToViewVerticalR2L(int pos, Shape a, Position.Bias b)
                throws BadLocationException {
        
            Rectangle alloc = a.getBounds();
            
            alloc.width = metrics.getHeight();
            alloc.height = 1;

            int p0 = getStartOffset();
            if (pos < p0 || pos > getEndOffset()) {
                throw new BadLocationException("Position out of range", pos);
            }

            int testP = (b == Position.Bias.Forward) ? pos :
                        Math.max(p0, pos - 1);
            int line = 0;
            int[] lineEnds = getLineEnds();
            if (lineEnds != null) {
                line = findLine(testP - p0);
                if (line > 0) {
                    p0 += lineEnds[line - 1];
                }
                alloc.x += metrics.getHeight()/2 + (alloc.width * line);
            } else {
                alloc.x += metrics.getHeight()/2;
            }
            int w = (int)width;
            alloc.x = w - alloc.x;

            if (pos > p0) {
                Segment segment = MSegmentCache.getSharedSegment();
                loadText(segment, p0, pos);
                alloc.y += MUtilities.getTabbedTextWidth(segment, metrics,
                        alloc.y, MWrappedPlainView.this, p0);
                MSegmentCache.releaseSharedSegment(segment);
            }
            return alloc;
        }

        /**
         * Provides a mapping from the view coordinate space to the logical
         * coordinate space of the model.
         *
         * @param fx the X coordinate
         * @param fy the Y coordinate
         * @param a the allocated region to render into
         * @return the location within the model that best represents the
         *  given point in the view
         * @see MView#viewToModel
         */
        public int viewToModel(float fx, float fy, Shape a, Position.Bias[] bias) {
        	
        	if( getRotateDirection() == MSwingRotateUtilities.ROTATE_HORIZANTAL ) {
        		return this.viewToModelHorizantal(fx, fy, a, bias);
        	} else if ( getRotateHint() != MSwingRotateUtilities.ROTATE_RIGHTTOLEFT ) {
        		return this.viewToModelVerticalL2R(fx, fy, a, bias);
        	} else {
        		return this.viewToModelVerticalR2L(fx, fy, a, bias);
        	}
        }
        
        private int viewToModelHorizantal(float fx, float fy, Shape a, Position.Bias[] bias) {
            // PENDING(prinz) implement bias properly
            bias[0] = Position.Bias.Forward;

            Rectangle alloc = (Rectangle) a;
            int x = (int) fx;
            int y = (int) fy;
            if (y < alloc.y) {
                // above the area covered by this icon, so the the position
                // is assumed to be the start of the coverage for this view.
                return getStartOffset();
            } else if (y > alloc.y + alloc.height) {
                // below the area covered by this icon, so the the position
                // is assumed to be the end of the coverage for this view.
                return getEndOffset() - 1;
            } else {
                // positioned within the coverage of this view vertically,
                // so we figure out which line the point corresponds to.
                // if the line is greater than the number of lines contained, then
                // simply use the last line as it represents the last possible place
                // we can position to.
                alloc.height = metrics.getHeight();
                int line = (alloc.height > 0 ?
                            (y - alloc.y) / alloc.height : lineCount - 1);
                if (line >= lineCount) {
                    return getEndOffset() - 1;
                } else {
                    int p0 = getStartOffset();
                    int p1;
                    if (lineCount == 1) {
                        p1 = getEndOffset();
                    } else {
                        int[] lineEnds = getLineEnds();
                        p1 = p0 + lineEnds[line];
                        if (line > 0) {
                            p0 += lineEnds[line - 1];
                        }
                    }

                    if (x < alloc.x) {
                        // point is to the left of the line
                        return p0;
                    } else if (x > alloc.x + alloc.width) {
                        // point is to the right of the line
                        return p1 - 1;
                    } else {
                        // Determine the offset into the text
                        Segment segment = MSegmentCache.getSharedSegment();
                        loadText(segment, p0, p1);
                        int n = MUtilities.getTabbedTextOffset(segment, metrics,
                                                   alloc.x, x,
                                                   MWrappedPlainView.this, p0);
                        MSegmentCache.releaseSharedSegment(segment);
                        return Math.min(p0 + n, p1 - 1);
                    }
                }
            }
        }

        private int viewToModelVerticalL2R(float fx, float fy, Shape a, Position.Bias[] bias) {
            // PENDING(prinz) implement bias properly
            bias[0] = Position.Bias.Forward;

            Rectangle alloc = new Rectangle((Rectangle) a);
            
            int x = (int) fx;
            int y = (int) fy;
            if (y < alloc.y) {
                // above the area covered by this icon, so the the position
                // is assumed to be the start of the coverage for this view.
                return getStartOffset();
            } else if (y > alloc.y + alloc.height) {
                // below the area covered by this icon, so the the position
                // is assumed to be the end of the coverage for this view.
                return getEndOffset() - 1;
            } else {
                // positioned within the coverage of this view vertically,
                // so we figure out which line the point corresponds to.
                // if the line is greater than the number of lines contained, then
                // simply use the last line as it represents the last possible place
                // we can position to.
                alloc.width = metrics.getHeight();
                int line = (alloc.width > 0 ?
                            (x - alloc.x) / alloc.width : lineCount - 1);
                if (line >= lineCount) {
                    return getEndOffset() - 1;
                } else {
                    int p0 = getStartOffset();
                    int p1;
                    if (lineCount == 1) {
                        p1 = getEndOffset();
                    } else {
                        int[] lineEnds = getLineEnds();
                        p1 = p0 + lineEnds[line];
                        if (line > 0) {
                            p0 += lineEnds[line - 1];
                        }
                    }

                    if (y < alloc.y) {
                        // point is to the left of the line
                        return p0;
                    } else if (y > alloc.y + alloc.height) {
                        // point is to the right of the line
                        return p1 - 1;
                    } else {
                        // Determine the offset into the text
                        Segment segment = MSegmentCache.getSharedSegment();
                        loadText(segment, p0, p1);
                        int n = MUtilities.getTabbedTextOffset(MWrappedPlainView.this, segment, metrics, defaultmetrics,
                                                   alloc.y, y,
                                                   MWrappedPlainView.this, p0, null);
                        MSegmentCache.releaseSharedSegment(segment);
                        return Math.min(p0 + n, p1 - 1);
                    }
                }
            }
        }

        private int viewToModelVerticalR2L(float fx, float fy, Shape a, Position.Bias[] bias) {
            // PENDING(prinz) implement bias properly
            bias[0] = Position.Bias.Forward;

            Rectangle alloc = (Rectangle) a;
            
            int x = (int) fx;
            int y = (int) fy;
            if (y < alloc.y) {
                // above the area covered by this icon, so the the position
                // is assumed to be the start of the coverage for this view.
                return getStartOffset();
            } else if (y > alloc.y + alloc.height) {
                // below the area covered by this icon, so the the position
                // is assumed to be the end of the coverage for this view.
                return getEndOffset() - 1;
            } else {
                // positioned within the coverage of this view vertically,
                // so we figure out which line the point corresponds to.
                // if the line is greater than the number of lines contained, then
                // simply use the last line as it represents the last possible place
                // we can position to.
                alloc.width = metrics.getHeight();
                int w = (int)width;
                x = x + alloc.x;
                int line = (alloc.width > 0 ?
                            (w - x ) / alloc.width : lineCount - 1);
                if (line >= lineCount) {
                    return getEndOffset() - 1;
                } else {
                    int p0 = getStartOffset();
                    int p1;
                    if (lineCount == 1) {
                        p1 = getEndOffset();
                    } else {
                        int[] lineEnds = getLineEnds();
                        p1 = p0 + lineEnds[line];
                        if (line > 0) {
                            p0 += lineEnds[line - 1];
                        }
                    }

                    if (y < alloc.y) {
                        // point is to the left of the line
                        return p0;
                    } else if (y > alloc.y + alloc.height) {
                        // point is to the right of the line
                        return p1 - 1;
                    } else {
                        // Determine the offset into the text
                        Segment segment = MSegmentCache.getSharedSegment();
                        loadText(segment, p0, p1);
                        int n = MUtilities.getTabbedTextOffset(segment, metrics,
                                                   alloc.y, y,
                                                   MWrappedPlainView.this, p0);
                        MSegmentCache.releaseSharedSegment(segment);
                        return Math.min(p0 + n, p1 - 1);
                    }
                }
            }
        }

        /**
         * Provides a mapping, for a given region,
         * from the document model coordinate space
         * to the view coordinate space. The specified region is
         * created as a union of the first and last character positions.
         *
         * @param p0 the position of the first character (>=0)
         * @param b0 the bias of the first character position,
         *  toward the previous character or the
         *  next character represented by the offset, in case the
         *  position is a boundary of two views; <code>b0</code> will have one
         *  of these values:
         * <ul>
         * <li> <code>Position.Bias.Forward</code>
         * <li> <code>Position.Bias.Backward</code>
         * </ul>
         * @param p1 the position of the last character (>=0)
         * @param b1 the bias for the second character position, defined
         *          one of the legal values shown above
         * @param a the area of the view, which encompasses the requested region
         * @return the bounding box which is a union of the region specified
         *          by the first and last character positions
         * @exception BadLocationException  if the given position does
         *   not represent a valid location in the associated document
         * @exception IllegalArgumentException if <code>b0</code> or
         *          <code>b1</code> are not one of the
         *          legal <code>Position.Bias</code> values listed above
         * @see MView#viewToModel
         */
        public Shape modelToView(int p0, Position.Bias b0, int p1, Position.Bias b1, Shape a) throws BadLocationException {
        	
        	if( super.getRotateDirection() == MSwingRotateUtilities.ROTATE_HORIZANTAL ) {
                return this.modelToViewHorizantal(p0, b0, p1, b1, a);
        	} else if( super.getRotateHint() != MSwingRotateUtilities.ROTATE_RIGHTTOLEFT ){
                return this.modelToViewVerticalL2R(p0, b0, p1, b1, a);
        	} else {
                return this.modelToViewVerticalR2L(p0, b0, p1, b1, a);
        	}
        	
        }
        private Shape modelToViewHorizantal(int p0, Position.Bias b0, int p1, Position.Bias b1, Shape a) throws BadLocationException {
            Shape s0 = modelToView(p0, a, b0);
            Shape s1;
            if (p1 == getEndOffset()) {
                try {
                    s1 = modelToView(p1, a, b1);
                } catch (BadLocationException ble) {
                    s1 = null;
                }
                if (s1 == null) {
                    // Assume extends left to right.
                    Rectangle alloc = (a instanceof Rectangle) ? (Rectangle)a :
                                      a.getBounds();
                    s1 = new Rectangle(alloc.x + alloc.width - 1, alloc.y,
                                       1, alloc.height);
                }
            }
            else {
                s1 = modelToView(p1, a, b1);
            }
            Rectangle r0 = s0.getBounds();
            Rectangle r1 = (s1 instanceof Rectangle) ? (Rectangle) s1 :
                                                       s1.getBounds();
            if (r0.y != r1.y) {
                // If it spans lines, force it to be the width of the view.
                Rectangle alloc = (a instanceof Rectangle) ? (Rectangle)a :
                                  a.getBounds();
                r0.x = alloc.x;
                r0.width = alloc.width;
            }
            r0.add(r1);
            return r0;
        }

        private Shape modelToViewVerticalL2R(int p0, Position.Bias b0, int p1, Position.Bias b1, Shape a) throws BadLocationException {
            Shape s0 = modelToView(p0, a, b0);
            Shape s1;
            if (p1 == getEndOffset()) {
                try {
                    s1 = modelToView(p1, a, b1);
                } catch (BadLocationException ble) {
                    s1 = null;
                }
                if (s1 == null) {
                    // Assume extends left to right.
                    Rectangle alloc = (a instanceof Rectangle) ? (Rectangle)a :
                                      a.getBounds();
                    s1 = new Rectangle(alloc.x + alloc.width - 1, alloc.y,
                                       1, alloc.height);
                }
            }
            else {
                s1 = modelToView(p1, a, b1);
            }
            
            Rectangle r0 = s0.getBounds();
            int tmp = r0.x;
            r0.x = r0.y;
            r0.y = -tmp - r0.width/2;
            tmp = r0.width;
            r0.width = r0.height;
            r0.height = tmp;
            
            Rectangle r1 = (s1 instanceof Rectangle) ? (Rectangle) s1 : s1.getBounds();
            tmp = r1.x;
            r1.x = r1.y;
            r1.y = -tmp - r1.width/2;
            tmp = r1.width;
            r1.width = r1.height;
            r1.height = tmp;
            
            if (r0.y != r1.y) {
                // If it spans lines, force it to be the width of the view.
                Rectangle alloc = (a instanceof Rectangle) ? (Rectangle)a :
                                  a.getBounds();
                r0.x = alloc.x;
                r0.width = alloc.width;
            }
            r0.add(r1);
            return r0;
        }
        
        private Shape modelToViewVerticalR2L(int p0, Position.Bias b0, int p1, Position.Bias b1, Shape a) throws BadLocationException {
            Shape s0 = modelToView(p0, a, b0);
            Shape s1;
            if (p1 == getEndOffset()) {
                try {
                    s1 = modelToView(p1, a, b1);
                } catch (BadLocationException ble) {
                    s1 = null;
                }
                if (s1 == null) {
                    // Assume extends left to right.
                    Rectangle alloc = (a instanceof Rectangle) ? (Rectangle)a :
                                      a.getBounds();
                    s1 = new Rectangle(alloc.x + alloc.width - 1, alloc.y,
                                       1, alloc.height);
                }
            }
            else {
                s1 = modelToView(p1, a, b1);
            }
            // If it spans lines, force it to be the width of the view.
            Rectangle alloc = (a instanceof Rectangle) ? (Rectangle)a :
                              a.getBounds();
            int w = (int)width;
            
            Rectangle r0 = s0.getBounds();
//            r0.x = r0.x - alloc.x + alloc.y;
            r0.y = r0.y - alloc.y + alloc.x;
            
            int tmp = r0.x;
            r0.x = r0.y;
            r0.y = w- (tmp + r0.width/2) + alloc.y;
            tmp = r0.width;
            r0.width = r0.height;
            r0.height = tmp;
            
            Rectangle r1 = (s1 instanceof Rectangle) ? (Rectangle) s1 : s1.getBounds();
//            r1.x = r1.x - alloc.x + alloc.y;
            r1.y = r1.y - alloc.y + alloc.x;
            
            tmp = r1.x;
            r1.x = r1.y;
            r1.y = w - (tmp + r1.width/2) + alloc.y;
            tmp = r1.width;
            r1.width = r1.height;
            r1.height = tmp;
            
            if (r0.y != r1.y) {
                r0.x = alloc.x;
                r0.width = alloc.width;
            }
            r0.add(r1);
            return r0;
        }
        
        public void insertUpdate(DocumentEvent e, Shape a, ViewFactory f) {
            update(e, a);
        }

        public void removeUpdate(DocumentEvent e, Shape a, ViewFactory f) {
            update(e, a);
        }

        private void update(DocumentEvent ev, Shape a) {
            int oldCount = lineCount;
            breakLines(ev.getOffset());
            if (oldCount != lineCount) {
                MWrappedPlainView.this.preferenceChanged(this, false, true);
                // have to repaint any views after the receiver.
                getContainer().repaint();
            } else if (a != null) {
                Component c = getContainer();
                Rectangle alloc = (Rectangle) a;
                c.repaint(alloc.x, alloc.y, alloc.width, alloc.height);
            }
        }

        /**
         * Returns line cache. If the cache was GC'ed, recreates it.
         * If there's no cache, returns null
         */
        final int[] getLineEnds() {
            if (lineCache == null) {
                return null;
            } else {
                int[] lineEnds = lineCache.get();
                if (lineEnds == null) {
                    // Cache was GC'ed, so rebuild it
                    return breakLines(getStartOffset());
                } else {
                    return lineEnds;
                }
            }
        }

        /**
         * Creates line cache if text breaks into more than one physical line.
         * @param startPos position to start breaking from
         * @return the cache created, ot null if text breaks into one line
         */
        final int[] breakLines(int startPos) {
            int[] lineEnds = (lineCache == null) ? null : lineCache.get();
            int[] oldLineEnds = lineEnds;
            int start = getStartOffset();
            int lineIndex = 0;
            if (lineEnds != null) {
                lineIndex = findLine(startPos - start);
                if (lineIndex > 0) {
                    lineIndex--;
                }
            }

            int p0 = (lineIndex == 0) ? start : start + lineEnds[lineIndex - 1];
            int p1 = getEndOffset();
            while (p0 < p1) {
                int p = calculateBreakPosition(p0, p1);
                p0 = (p == p0) ? ++p : p;      // 4410243

                if (lineIndex == 0 && p0 >= p1) {
                    // do not use cache if there's only one line
                    lineCache = null;
                    lineEnds = null;
                    lineIndex = 1;
                    break;
                } else if (lineEnds == null || lineIndex >= lineEnds.length) {
                    // we have 2+ lines, and the cache is not big enough
                    // we try to estimate total number of lines
                    double growFactor = ((double)(p1 - start) / (p0 - start));
                    int newSize = (int)Math.ceil((lineIndex + 1) * growFactor);
                    newSize = Math.max(newSize, lineIndex + 2);
                    int[] tmp = new int[newSize];
                    if (lineEnds != null) {
                        System.arraycopy(lineEnds, 0, tmp, 0, lineIndex);
                    }
                    lineEnds = tmp;
                }
                lineEnds[lineIndex++] = p0 - start;
            }

            lineCount = lineIndex;
            if (lineCount > 1) {
                // check if the cache is too big
                int maxCapacity = lineCount + lineCount / 3;
                if (lineEnds.length > maxCapacity) {
                    int[] tmp = new int[maxCapacity];
                    System.arraycopy(lineEnds, 0, tmp, 0, lineCount);
                    lineEnds = tmp;
                }
            }

            if (lineEnds != null && lineEnds != oldLineEnds) {
                lineCache = new SoftReference<int[]>(lineEnds);
            }
            return lineEnds;
        }

        /**
         * Binary search in the cache for line containing specified offset
         * (which is relative to the beginning of the view). This method
         * assumes that cache exists.
         */
        private int findLine(int offset) {
            int[] lineEnds = lineCache.get();
            if (offset < lineEnds[0]) {
                return 0;
            } else if (offset > lineEnds[lineCount - 1]) {
                return lineCount;
            } else {
                return findLine(lineEnds, offset, 0, lineCount - 1);
            }
        }

        private int findLine(int[] array, int offset, int min, int max) {
            if (max - min <= 1) {
                return max;
            } else {
                int mid = (max + min) / 2;
                return (offset < array[mid]) ?
                        findLine(array, offset, min, mid) :
                        findLine(array, offset, mid, max);
            }
        }

        int lineCount;
        SoftReference<int[]> lineCache = null;
        
    }
    
}
