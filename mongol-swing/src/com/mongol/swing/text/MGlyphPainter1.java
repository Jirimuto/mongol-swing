package com.mongol.swing.text;

import java.awt.Container;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.BadLocationException;
import javax.swing.text.GlyphView;
import javax.swing.text.Position;
import javax.swing.text.Segment;
import javax.swing.text.TabExpander;
import javax.swing.text.View;

import com.mongol.encode.MongolianFontUtil;
import com.mongol.swing.MSwingRotateUtilities;

import sun.font.FontDesignMetrics;
import sun.swing.SwingUtilities2;

/**
 * A class to perform rendering of the glyphs.
 * This can be implemented to be stateless, or
 * to hold some information as a cache to
 * facilitate faster rendering and model/view
 * translation.  At a minimum, the GlyphPainter
 * allows a View implementation to perform its
 * duties independent of a particular version
 * of JVM and selection of capabilities (i.e.
 * shaping for i18n, etc).
 * <p>
 * This implementation is intended for operation
 * under the JDK1.1 API of the Java Platform.
 * Since the JDK is backward compatible with
 * JDK1.1 API, this class will also function on
 * Java 2.  The JDK introduces improved
 * API for rendering text however, so the GlyphPainter2
 * is recommended for the DK.
 *
 * @author  Timothy Prinzing
 * @see GlyphView
 */
class MGlyphPainter1 extends MGlyphView.GlyphPainter {

    /**
     * Determine the span the glyphs given a start location
     * (for tab expansion).
     */
    public float getSpan(MGlyphView v, int p0, int p1,
                         TabExpander e, float x) {
        sync(v);
        Segment text = v.getText(p0, p1);
        int[] justificationData = getJustificationData(v);
        int width = MUtilities.getTabbedTextWidth(v, text, metrics, defaultmetrics, (int) x, e, p0,
                                                 justificationData);
        MSegmentCache.releaseSharedSegment(text);
        return width;
    }

    public float getHeight(MGlyphView v) {
        sync(v);
        return metrics.getHeight();
    }

    /**
     * Fetches the ascent above the baseline for the glyphs
     * corresponding to the given range in the model.
     */
    public float getAscent(MGlyphView v) {
        sync(v);
        return metrics.getAscent();
    }

    /**
     * Fetches the descent below the baseline for the glyphs
     * corresponding to the given range in the model.
     */
    public float getDescent(MGlyphView v) {
        sync(v);
        return metrics.getDescent();
    }

    /**
     * Paints the glyphs representing the given range.
     */
    public void paint(MGlyphView v, Graphics g, Shape a, int p0, int p1) {
    	if( v.getRotateDirection()==MSwingRotateUtilities.ROTATE_HORIZANTAL) {
    		paintHorizantal(v, g, a, p0, p1);
    	} else if( v.getRotateHint()!=MSwingRotateUtilities.ROTATE_RIGHTTOLEFT) {
    		
			Graphics2D g2d = (Graphics2D)g;
			AffineTransform transform = g2d.getTransform();
			
	        ((Graphics2D) g).rotate(Math.PI * 1 / 2);
	        
			paintVerticalL2R(v, g, a, p0, p1);
			
	    	g2d.setTransform(transform);
	    	
    	} else {
            
			Graphics2D g2d = (Graphics2D)g;
			AffineTransform transform = g2d.getTransform();
	        Rectangle clip = g.getClipBounds();
	        this.width = clip.width;
			
	        ((Graphics2D) g).rotate(Math.PI * 1 / 2);
            g2d.translate(0, -this.width);
	        
			paintVerticalR2L(v, g, a, p0, p1);
			
	    	g2d.setTransform(transform);
	    	
    	}
    }
    
    private void paintHorizantal(MGlyphView v, Graphics g, Shape a, int p0, int p1) {
        sync(v);
        Segment text;
        TabExpander expander = v.getTabExpander();
        Rectangle alloc = (a instanceof Rectangle) ? (Rectangle)a : a.getBounds();

        // determine the x coordinate to render the glyphs
        int x = alloc.x;
        int p = v.getStartOffset();
        int[] justificationData = getJustificationData(v);
        if (p != p0) {
            text = v.getText(p, p0);
            int width = MUtilities.getTabbedTextWidth(v, text, metrics, defaultmetrics, x, expander, p,
                                                     justificationData);
            x += width;
            MSegmentCache.releaseSharedSegment(text);
        }

        // determine the y coordinate to render the glyphs
        int y = alloc.y + metrics.getHeight() - metrics.getDescent();

        // render the glyphs
        text = v.getText(p0, p1);
        g.setFont(metrics.getFont());

        MUtilities.drawTabbedText(v, text, x, y, g, expander,p0,
                                 justificationData);
        MSegmentCache.releaseSharedSegment(text);
    }
    
    private void paintVerticalL2R(MGlyphView v, Graphics g, Shape a, int p0, int p1) {
        sync(v);
        Segment text;
        TabExpander expander = v.getTabExpander();
        Rectangle alloc = (a instanceof Rectangle) ? new Rectangle((Rectangle)a) : a.getBounds();
        int tmp = alloc.x;
        alloc.x = alloc.y;
        alloc.y = tmp;
        
        // determine the x coordinate to render the glyphs
        int x = alloc.x;
        int p = v.getStartOffset();
        int[] justificationData = getJustificationData(v);
        if (p != p0) {
            text = v.getText(p, p0);
            int width = MUtilities.getTabbedTextWidth(v, text, metrics, defaultmetrics, x, expander, p,
                                                     justificationData);
            x += width;
            MSegmentCache.releaseSharedSegment(text);
        }

        // determine the y coordinate to render the glyphs
        int y = -alloc.y - metrics.getDescent();

        // render the glyphs
        text = v.getText(p0, p1);
        g.setFont(metrics.getFont());

        MUtilities.drawTabbedText(v, text, x, y, g, expander,p0,
                                 justificationData);
        MSegmentCache.releaseSharedSegment(text);
    }

    private void paintVerticalR2L(MGlyphView v, Graphics g, Shape a, int p0, int p1) {
        sync(v);
        Segment text;
        TabExpander expander = v.getTabExpander();
        Rectangle alloc = (a instanceof Rectangle) ? new Rectangle((Rectangle)a) : a.getBounds();
        int tmp = alloc.x;
        alloc.x = alloc.y;
        alloc.y = tmp;
        // determine the x coordinate to render the glyphs
        int x = alloc.x;
        int p = v.getStartOffset();
        int[] justificationData = getJustificationData(v);
        if (p != p0) {
            text = v.getText(p, p0);
            int width = MUtilities.getTabbedTextWidth(v, text, metrics, defaultmetrics, x, expander, p,
                                                     justificationData);
            x += width;
            MSegmentCache.releaseSharedSegment(text);
        }

        // determine the y coordinate to render the glyphs
        int y = alloc.y + metrics.getHeight() - metrics.getDescent();

        // render the glyphs
        text = v.getText(p0, p1);
        g.setFont(metrics.getFont());

        MUtilities.drawTabbedText(v, text, x, y, g, expander,p0,
                                 justificationData);
        MSegmentCache.releaseSharedSegment(text);
    }

    public Shape modelToView(MGlyphView v, int pos, Position.Bias bias,
                             Shape a) throws BadLocationException {
    	if( v.getRotateDirection()==MSwingRotateUtilities.ROTATE_HORIZANTAL ){
    		return modelToViewHorizantal(v, pos, bias, a);
    	} else if( v.getRotateHint()!=MSwingRotateUtilities.ROTATE_RIGHTTOLEFT ){
    		return modelToViewVerticalL2R(v, pos, bias, a);
    	} else {
    		Rectangle retAlloc = modelToViewVerticalR2L(v, pos, bias, a).getBounds();
    		retAlloc.x = this.width - retAlloc.x;
    		return retAlloc;
    	}
    }
    
    private Shape modelToViewHorizantal(MGlyphView v, int pos, Position.Bias bias,
            Shape a) throws BadLocationException {

        sync(v);
        Rectangle alloc = (a instanceof Rectangle) ? (Rectangle)a : a.getBounds();
        int p0 = v.getStartOffset();
        int p1 = v.getEndOffset();
        TabExpander expander = v.getTabExpander();
        Segment text;

        if(pos == p1) {
            // The caller of this is left to right and borders a right to
            // left view, return our end location.
            return new Rectangle(alloc.x + alloc.width, alloc.y, 0,
                                 metrics.getHeight());
        }
        if ((pos >= p0) && (pos <= p1)) {
            // determine range to the left of the position
            text = v.getText(p0, pos);
            int[] justificationData = getJustificationData(v);
            int width = MUtilities.getTabbedTextWidth(v, text, metrics, defaultmetrics, alloc.x, expander, p0,
                                                     justificationData);
            MSegmentCache.releaseSharedSegment(text);
            return new Rectangle(alloc.x + width, alloc.y, 0, metrics.getHeight());
        }
        throw new BadLocationException("modelToView - can't convert", p1);
    }

    private Shape modelToViewVerticalL2R(MGlyphView v, int pos, Position.Bias bias,
            Shape a) throws BadLocationException {

        sync(v);
        Rectangle alloc = (a instanceof Rectangle) ? new Rectangle((Rectangle)a) : a.getBounds();
        
        int p0 = v.getStartOffset();
        int p1 = v.getEndOffset();
        TabExpander expander = v.getTabExpander();
        Segment text;

        if(pos == p1) {
            // The caller of this is left to right and borders a right to
            // left view, return our end location.
            return new Rectangle(alloc.x, alloc.y+alloc.height, metrics.getHeight(), 0 );
        }
        if ((pos >= p0) && (pos <= p1)) {
            // determine range to the left of the position
            text = v.getText(p0, pos);
            int[] justificationData = getJustificationData(v);
            int width = MUtilities.getTabbedTextWidth(v, text, metrics, defaultmetrics, alloc.y, expander, p0,
                                                     justificationData);
            MSegmentCache.releaseSharedSegment(text);
            return new Rectangle(alloc.x , alloc.y + width, metrics.getHeight(), 0);
        }
        throw new BadLocationException("modelToView - can't convert", p1);
    }
    
    private Shape modelToViewVerticalR2L(MGlyphView v, int pos, Position.Bias bias,
            Shape a) throws BadLocationException {

        sync(v);
        Rectangle alloc = (a instanceof Rectangle) ? new Rectangle((Rectangle)a) : a.getBounds();
        
        int p0 = v.getStartOffset();
        int p1 = v.getEndOffset();
        TabExpander expander = v.getTabExpander();
        Segment text;

        if(pos == p1) {
            // The caller of this is left to right and borders a right to
            // left view, return our end location.
            return new Rectangle(alloc.x, alloc.y+alloc.height, metrics.getHeight(), 0 );
        }
        if ((pos >= p0) && (pos <= p1)) {
            // determine range to the left of the position
            text = v.getText(p0, pos);
            int[] justificationData = getJustificationData(v);
            int width = MUtilities.getTabbedTextWidth(v, text, metrics, defaultmetrics, alloc.y, expander, p0,
                                                     justificationData);
            MSegmentCache.releaseSharedSegment(text);
            return new Rectangle(alloc.x , alloc.y + width, metrics.getHeight(), 0);
        }
        throw new BadLocationException("modelToView - can't convert", p1);
    }
    
    /**
     * Provides a mapping from the view coordinate space to the logical
     * coordinate space of the model.
     *
     * @param v the view containing the view coordinates
     * @param x the X coordinate
     * @param y the Y coordinate
     * @param a the allocated region to render into
     * @param biasReturn always returns <code>Position.Bias.Forward</code>
     *   as the zero-th element of this array
     * @return the location within the model that best represents the
     *  given point in the view
     * @see View#viewToModel
     */
    public int viewToModel(MGlyphView v, float x, float y, Shape a,
                           Position.Bias[] biasReturn) {
    	if( v.getRotateDirection()==MSwingRotateUtilities.ROTATE_HORIZANTAL) {
    		return viewToModelHorizantal(v, x, y, a, biasReturn);
    	} else if( v.getRotateHint()!=MSwingRotateUtilities.ROTATE_RIGHTTOLEFT ){
    		return viewToModelVerticalL2R(v, x, y, a, biasReturn);    		
    	} else {
    		return viewToModelVerticalR2L(v, x, y, a, biasReturn);    		
    	}
    }

    public int viewToModelHorizantal(MGlyphView v, float x, float y, Shape a,
            Position.Bias[] biasReturn) {
        sync(v);
        Rectangle alloc = (a instanceof Rectangle) ? (Rectangle)a : a.getBounds();
        int p0 = v.getStartOffset();
        int p1 = v.getEndOffset();
        TabExpander expander = v.getTabExpander();
        Segment text = v.getText(p0, p1);
        int[] justificationData = getJustificationData(v);
        int offs = MUtilities.getTabbedTextOffset(v, text, metrics, defaultmetrics, 
                                                 alloc.x, (int) x, expander, p0,
                                                 justificationData);
        MSegmentCache.releaseSharedSegment(text);
        int retValue = p0 + offs;
        if(retValue == p1) {
            // No need to return backward bias as GlyphPainter1 is used for
            // ltr text only.
            retValue--;
        }
        biasReturn[0] = Position.Bias.Forward;
        return retValue;
    }

    public int viewToModelVerticalL2R(MGlyphView v, float x, float y, Shape a,
            Position.Bias[] biasReturn) {
        sync(v);
        Rectangle alloc = (a instanceof Rectangle) ? (Rectangle)a : a.getBounds();
        int p0 = v.getStartOffset();
        int p1 = v.getEndOffset();
        TabExpander expander = v.getTabExpander();
        Segment text = v.getText(p0, p1);
        int[] justificationData = getJustificationData(v);
        int offs = MUtilities.getTabbedTextOffset(v, text, metrics, defaultmetrics, 
                                                 alloc.y, (int) y, expander, p0,
                                                 justificationData);
        MSegmentCache.releaseSharedSegment(text);
        int retValue = p0 + offs;
        if(retValue == p1) {
            // No need to return backward bias as GlyphPainter1 is used for
            // ltr text only.
            retValue--;
        }
        biasReturn[0] = Position.Bias.Forward;
        return retValue;
    }

    public int viewToModelVerticalR2L(MGlyphView v, float x, float y, Shape a,
            Position.Bias[] biasReturn) {
        sync(v);
        Rectangle alloc = (a instanceof Rectangle) ? (Rectangle)a : a.getBounds();
        int p0 = v.getStartOffset();
        int p1 = v.getEndOffset();
        TabExpander expander = v.getTabExpander();
        Segment text = v.getText(p0, p1);
        int[] justificationData = getJustificationData(v);
        int offs = MUtilities.getTabbedTextOffset(v, text, metrics, defaultmetrics, 
                                                 alloc.y, (int) y, expander, p0,
                                                 justificationData);
        MSegmentCache.releaseSharedSegment(text);
        int retValue = p0 + offs;
        if(retValue == p1) {
            // No need to return backward bias as GlyphPainter1 is used for
            // ltr text only.
            retValue--;
        }
        biasReturn[0] = Position.Bias.Forward;
        return retValue;
    }

    /**
     * Determines the best location (in the model) to break
     * the given view.
     * This method attempts to break on a whitespace
     * location.  If a whitespace location can't be found, the
     * nearest character location is returned.
     *
     * @param v the view
     * @param p0 the location in the model where the
     *  fragment should start its representation >= 0
     * @param pos the graphic location along the axis that the
     *  broken view would occupy >= 0; this may be useful for
     *  things like tab calculations
     * @param len specifies the distance into the view
     *  where a potential break is desired >= 0
     * @return the model location desired for a break
     * @see View#breakView
     */
    public int getBoundedPosition(MGlyphView v, int p0, float x, float len) {
        sync(v);
        TabExpander expander = v.getTabExpander();
        Segment s = v.getText(p0, v.getEndOffset());
        int[] justificationData = getJustificationData(v);
        int index = MUtilities.getTabbedTextOffset(v, s, metrics, defaultmetrics, (int)x, (int)(x+len),
                                                  expander, p0, false,
                                                  justificationData);
        MSegmentCache.releaseSharedSegment(s);
        int p1 = p0 + index;
        return p1;
    }

    void sync(MGlyphView v) {
        Container c = v.getContainer();
        Font font = v.getFont();
        if( c instanceof JComponent ) {
        	JComponent comp = (JComponent)c;
	        if ((metrics == null) || (! font.equals(metrics.getFont()))) {
	        	Font mfont = font;
	            if( (font instanceof FontUIResource) ) {
	            	mfont = new Font(font.getFontName(), font.getStyle(), font.getSize() );
	            }
	            // fetch a new FontMetrics
	            metrics = SwingUtilities2.getFontMetrics(comp, mfont);
	        }
	        Font alternative_font = MongolianFontUtil.getAltFont(font, "TextField.font");
	        defaultmetrics = SwingUtilities2.getFontMetrics(comp, alternative_font);
	        
        } else {
        	
	        if ((metrics == null) || (! font.equals(metrics.getFont()))) {
	        	Font mfont = font;
	            if( (font instanceof FontUIResource) ) {
	            	mfont = new Font(font.getFontName(), font.getStyle(), font.getSize() );
	            }
	            // fetch a new FontMetrics
	            metrics = (c != null) ? c.getFontMetrics(mfont) :
	                Toolkit.getDefaultToolkit().getFontMetrics(mfont);
	
	        }
	        Font alternative_font = MongolianFontUtil.getAltFont(font, "TextField.font");
	        defaultmetrics = (c != null) ? c.getFontMetrics(alternative_font):
                Toolkit.getDefaultToolkit().getFontMetrics(font);
        }
        
        if( !MongolianFontUtil.isMongolianFont(font) ) {
        	FontMetrics tmpfont = metrics;
        	metrics = defaultmetrics;
        	defaultmetrics = tmpfont;
        }
    }



    /**
     * @return justificationData from the ParagraphRow this GlyphView
     * is in or {@code null} if no justification is needed
     */
    private int[] getJustificationData(MGlyphView v) {
        View parent = v.getParent();
        int [] ret = null;
        // TODO Jirimuto
//        if (parent instanceof ParagraphView.Row) {
//            ParagraphView.Row row = ((ParagraphView.Row) parent);
//            ret = row.justificationData;
//        }
        return ret;
    }

    // --- variables ---------------------------------------------

    protected FontMetrics metrics;
    protected FontMetrics defaultmetrics;
    
    protected int width;
}
