package com.mongol.swing.text;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.text.*;

import javax.swing.*;
import javax.swing.text.*;

import com.mongol.encode.MCodeConstant;
import com.mongol.encode.MongolianFontUtil;
import com.mongol.swing.MSwingRotateUtilities;
import com.mongol.swing.MSwingUtilities;
import com.mongol.swing.plaf.MRotation;

import sun.swing.SwingUtilities2;

public class MUtilities {

    final static int SPACE_ADDON = 0;
    final static int SPACE_ADDON_LEFTOVER_END = 1;
    final static int START_JUSTIFIABLE = 2;
    //this should be the last index in justificationData
    final static int END_JUSTIFIABLE = 3;

    /**
     * If <code>view</code>'s container is a <code>JComponent</code> it
     * is returned, after casting.
     */
    public static JComponent getJComponent(View mView) {
        if (mView != null) {
            Component component = mView.getContainer();
            if (component instanceof JComponent) {
                return (JComponent)component;
            }
        }
        return null;
    }

    /**
     * Draws the given text, expanding any tabs that are contained
     * using the given tab expansion technique.  This particular
     * implementation renders in a 1.1 style coordinate system
     * where ints are used and 72dpi is assumed.
     *
     * @param s  the source of the text
     * @param x  the X origin >= 0
     * @param y  the Y origin >= 0
     * @param g  the graphics context
     * @param e  how to expand the tabs.  If this value is null,
     *   tabs will be expanded as a space character.
     * @param startOffset starting offset of the text in the document >= 0
     * @return  the X location at the end of the rendered text
     */
    public static final int drawTabbedText(Segment s, int x, int y, Graphics g,
                                           TabExpander e, int startOffset) {
        return drawTabbedText(null, s, x, y, g, e, startOffset);
    }

    /**
     * Draws the given text, expanding any tabs that are contained
     * using the given tab expansion technique.  This particular
     * implementation renders in a 1.1 style coordinate system
     * where ints are used and 72dpi is assumed.
     *
     * @param mView View requesting rendering, may be null.
     * @param s  the source of the text
     * @param x  the X origin >= 0
     * @param y  the Y origin >= 0
     * @param g  the graphics context
     * @param e  how to expand the tabs.  If this value is null,
     *   tabs will be expanded as a space character.
     * @param startOffset starting offset of the text in the document >= 0
     * @return  the X location at the end of the rendered text
     */
    protected static final int drawTabbedText(MView mView,
                                Segment s, int x, int y, Graphics g,
                                TabExpander e, int startOffset) {
        return drawTabbedText(mView, s, x, y, g, e, startOffset, null);
    }

    // In addition to the previous method it can extend spaces for
    // justification.
    //
    // all params are the same as in the preious method except the last
    // one:
    // @param justificationData justificationData for the row.
    // if null not justification is needed
    public static final int drawTabbedText(View mView,
                                Segment s, int x, int y, Graphics g,
                                TabExpander e, int startOffset,
                                int [] justificationData) {
        JComponent component = getJComponent(mView);
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    	Font font = g.getFont();
        Font alternative_font = MongolianFontUtil.getAltFont(font, "TextField.font");
        if( !MongolianFontUtil.isMongolianFont(font) ) {
        	Font tmpfont = font;
        	font = alternative_font;
        	alternative_font = tmpfont;
        }
        FontMetrics defaultMetrics = component.getFontMetrics(alternative_font);
        boolean rotate_vertical = false;
        Font rotated_font = alternative_font;
        if( mView instanceof MRotation ) {
        	MRotation rotation = (MRotation)mView;
        	if ( rotation.getRotateDirection() != MSwingRotateUtilities.ROTATE_HORIZANTAL ){
        		rotate_vertical = true;
        		AffineTransform transform = alternative_font.getTransform();
        		transform.rotate(- Math.PI * 1 / 2 );
        		rotated_font = alternative_font.deriveFont(transform);
        	}
        }
        
        FontMetrics metrics = component.getFontMetrics(font);
        int FontAscentDiff = (metrics.getAscent() - defaultMetrics.getAscent())/2 - ( metrics.getHeight()-defaultMetrics.getHeight())/2;
        
        int nextX = x;
        // MongolianConverter converter = new MongolianConverter();
        int start = s.offset;
        int count = s.count;
        // String target = converter.convert(new String(s.array, start, count));
        String target = new String(s.array, start, count);
        if( start < s.offset )
        	target = target.substring(s.offset-start);
        
        char[] txt = target.toCharArray();
        int txtOffset = 0;
        int flushLen = 0;
        int flushIndex = 0;
        int spaceAddon = 0;
        int spaceAddonLeftoverEnd = -1;
        int startJustifiableContent = 0;
        int endJustifiableContent = 0;
        if (justificationData != null) {
            int offset = - startOffset + txtOffset;
            View parent = null;
            if (mView != null
                  && (parent = mView.getParent()) != null) {
                offset += parent.getStartOffset();
            }
            spaceAddon =
                justificationData[SPACE_ADDON];
            spaceAddonLeftoverEnd =
                justificationData[SPACE_ADDON_LEFTOVER_END] + offset;
            startJustifiableContent =
                justificationData[START_JUSTIFIABLE] + offset;
            endJustifiableContent =
                justificationData[END_JUSTIFIABLE] + offset;
        }
        int n = s.count;
        boolean candisplay = true;
        for (int i = 0; i < n && i < txt.length; i++) {
            if (txt[i] == '\t'
                || ((spaceAddon != 0 || i <= spaceAddonLeftoverEnd)
                    && (txt[i] == ' ')
                    && startJustifiableContent <= i
                    && i <= endJustifiableContent
                    )) {
                if (flushLen > 0) {
                	if( candisplay )
                		nextX = MSwingUtilities.drawCharsWithFont(component, g, font, txt,
                                                flushIndex, flushLen, x, y);
                	else {
                		if( !rotate_vertical ) {
                    		nextX = MSwingUtilities.drawCharsWithFont(component, g, alternative_font, txt,
                                    flushIndex, flushLen, x, y-FontAscentDiff);
                		} else {
	                		for( int k=0; k<flushLen; k++){
	        		            int dy = (alternative_font.getSize() - defaultMetrics.charsWidth(txt, flushIndex+k, 1))/2;
	        		            int dx = 2 - getDownIntend(txt[flushIndex+k]) * alternative_font.getSize()/6;
	        		            dy = dy + getRightIntend(txt[flushIndex+k]) * alternative_font.getSize()/6;
	        		            if( isRotated( txt[flushIndex+k] ) ){
		        		            x = x + alternative_font.getSize();
	        		            	dy = FontAscentDiff + dy;
		        		            nextX = MSwingUtilities.drawCharsWithFont(component, g, rotated_font, txt, flushIndex+k,
		        	                        1, x-dx, y-dy);
	        		            	nextX = x;
	        		            } else {
		        		            nextX = MSwingUtilities.drawCharsWithFont(component, g, alternative_font, txt, flushIndex+k,
		        	                        1, x, y);
	        		            }
	                		}
                		}
                	}
                    flushLen = 0;
                }
                flushIndex = i + 1;
                if (txt[i] == '\t') {
                    if (e != null) {
                        nextX = (int) e.nextTabStop((float) nextX, startOffset + i - txtOffset);
                    } else {
                        nextX += metrics.charWidth(' ');
                    }
                } else if (txt[i] == ' ') {
                    nextX += metrics.charWidth(' ') + spaceAddon;
                    if (i <= spaceAddonLeftoverEnd) {
                        nextX++;
                    }
                }
                x = nextX;
            } else if ((txt[i] == '\n') || (txt[i] == '\r')) {
                if (flushLen > 0) {
                	if( candisplay )
                		nextX = MSwingUtilities.drawCharsWithFont(component, g, font, txt,
                                                flushIndex, flushLen, x, y);
                	else {
                		if( !rotate_vertical ) {
                    		nextX = MSwingUtilities.drawCharsWithFont(component, g, alternative_font, txt,
                                    flushIndex, flushLen, x, y-FontAscentDiff);
                		} else {
	                		for( int k=0; k<flushLen; k++){
	        		            int dy = (alternative_font.getSize() - defaultMetrics.charsWidth(txt, flushIndex+k, 1))/2;
	        		            int dx = 2 - getDownIntend(txt[flushIndex+k]) * alternative_font.getSize()/6;
	        		            dy = dy + getRightIntend(txt[flushIndex+k]) * alternative_font.getSize()/12;
	        		            if( isRotated( txt[flushIndex+k] ) ){
		        		            x = x + alternative_font.getSize();
	        		            	dy = FontAscentDiff + dy;
		        		            nextX = MSwingUtilities.drawCharsWithFont(component, g, rotated_font, txt, flushIndex+k,
		        	                        1, x-dx, y-dy);
	        		            	nextX = x;
	        		            } else {
		        		            nextX = MSwingUtilities.drawCharsWithFont(component, g, alternative_font, txt, flushIndex+k,
		        	                        1, x, y);
	        		            }
	                		}
                		}
                	}
                		
                    flushLen = 0;
                }
                flushIndex = i + 1;
                x = nextX;
            } else if( candisplay ){
            	if( canDisplay(font, txt[i]) ) {
            		flushLen += 1;
            	} else {
                    if (flushLen > 0) {
                        nextX = MSwingUtilities.drawCharsWithFont(component, g, font, txt,
                                                    flushIndex, flushLen, x, y);
                        flushLen = 0;
                    }
                    flushIndex = i;
                    x = nextX;
                    candisplay = false;
                    
            		flushLen += 1;
            	}
            } else {
                if (flushLen > 0) {
            		if( !rotate_vertical ) {
                		nextX = MSwingUtilities.drawCharsWithFont(component, g, alternative_font, txt,
                                flushIndex, flushLen, x, y-FontAscentDiff);
            		} else {
	            		for( int k=0; k<flushLen; k++){
        		            int dy = (alternative_font.getSize() - defaultMetrics.charsWidth(txt, flushIndex+k, 1))/2;
        		            int dx = 2 - getDownIntend(txt[flushIndex+k]) * alternative_font.getSize()/6;
        		            dy = dy + getRightIntend(txt[flushIndex+k]) * alternative_font.getSize()/12;
        		            if( isRotated( txt[flushIndex+k] ) ){
            		            x = x + alternative_font.getSize();
            		            dy = FontAscentDiff + dy;
	        		            nextX = MSwingUtilities.drawCharsWithFont(component, g, rotated_font, txt, flushIndex+k,
	        	                        1, x-dx, y-dy);
        		            	nextX = x;
        		            } else {
	        		            nextX = MSwingUtilities.drawCharsWithFont(component, g, alternative_font, txt, flushIndex+k,
	        	                        1, x, y);
        		            }
	            		}
            		}
                    flushLen = 0;
                }
            	if( canDisplay(font, txt[i]) ) {
                    candisplay = true;
            	}
                flushIndex = i;
                x = nextX;
        		flushLen += 1;
            }
        }
        if (flushLen > 0) {
        	if( candisplay ) {
	            nextX = MSwingUtilities.drawCharsWithFont(component, g, font, txt, flushIndex,
	                                              flushLen, x, y);
        	}
        	else {
        		if( !rotate_vertical ) {
            		nextX = MSwingUtilities.drawCharsWithFont(component, g, alternative_font, txt,
                            flushIndex, flushLen, x, y-FontAscentDiff);
        		} else {
	        		for( int k=0; k<flushLen; k++){
    		            int dy = (alternative_font.getSize() - defaultMetrics.charsWidth(txt, flushIndex+k, 1))/2;
    		            int dx = 2 - getDownIntend(txt[flushIndex+k]) * alternative_font.getSize()/6;
    		            dy = dy + getRightIntend(txt[flushIndex+k]) * alternative_font.getSize()/12;
    		            if( isRotated( txt[flushIndex+k] ) ){
        		            x = x + alternative_font.getSize();
        		            dy = FontAscentDiff + dy;
        		            nextX = MSwingUtilities.drawCharsWithFont(component, g, rotated_font, txt, flushIndex+k,
        	                        1, x-dx, y-dy);
    		            	nextX = x;
    		            } else {
        		            nextX = MSwingUtilities.drawCharsWithFont(component, g, alternative_font, txt, flushIndex+k,
        	                        1, x, y);
    		            }
	        		}
        		}
        	}
        }
        return nextX;
    }

    // In addition to the previous method it can extend spaces for
    // justification.
    //
    // all params are the same as in the preious method except the last
    // one:
    // @param justificationData justificationData for the row.
    // if null not justification is needed
    public static final int drawTabbedText(JComponent component,
                                Segment s, int x, int y, Graphics g,
                                TabExpander e, int startOffset,
                                int [] justificationData) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    	Font font = g.getFont();
        Font alternative_font = MongolianFontUtil.getAltFont(font, "TextField.font");
        if( !MongolianFontUtil.isMongolianFont(font) ) {
        	Font tmpfont = font;
        	font = alternative_font;
        	alternative_font = tmpfont;
        }
        FontMetrics defaultMetrics = component.getFontMetrics(alternative_font);
        boolean rotate_vertical = false;
        Font rotated_font = alternative_font;
        if( component instanceof MRotation ) {
        	MRotation rotation = (MRotation)component;
        	if ( rotation.getRotateDirection() != MSwingRotateUtilities.ROTATE_HORIZANTAL ){
        		rotate_vertical = true;
        		AffineTransform transform = alternative_font.getTransform();
        		transform.rotate(- Math.PI * 1 / 2 );
        		rotated_font = alternative_font.deriveFont(transform);
        	}
        }
        
        FontMetrics metrics = component.getFontMetrics(font);
        int FontAscentDiff = metrics.getAscent() - defaultMetrics.getAscent() - ( metrics.getHeight()-defaultMetrics.getHeight())/2;
        
        int nextX = x;
        // MongolianConverter converter = new MongolianConverter();
        int start = s.offset;
        int count = s.count;
        // String target = converter.convert(new String(s.array, start, count));
        String target = new String(s.array, start, count);
        if( start < s.offset )
        	target = target.substring(s.offset-start);
        
        char[] txt = target.toCharArray();
        int txtOffset = 0;
        int flushLen = 0;
        int flushIndex = 0;
        int spaceAddon = 0;
        int spaceAddonLeftoverEnd = -1;
        int startJustifiableContent = 0;
        int endJustifiableContent = 0;
        if (justificationData != null) {
            int offset = - startOffset + txtOffset;
            spaceAddon =
                justificationData[SPACE_ADDON];
            spaceAddonLeftoverEnd =
                justificationData[SPACE_ADDON_LEFTOVER_END] + offset;
            startJustifiableContent =
                justificationData[START_JUSTIFIABLE] + offset;
            endJustifiableContent =
                justificationData[END_JUSTIFIABLE] + offset;
        }
        int n = s.count;
        boolean candisplay = true;
        for (int i = 0; i < n && i < txt.length; i++) {
            if (txt[i] == '\t'
                || ((spaceAddon != 0 || i <= spaceAddonLeftoverEnd)
                    && (txt[i] == ' ')
                    && startJustifiableContent <= i
                    && i <= endJustifiableContent
                    )) {
                if (flushLen > 0) {
                	if( candisplay )
                		nextX = MSwingUtilities.drawCharsWithFont(component, g, font, txt,
                                                flushIndex, flushLen, x, y);
                	else {
                		if( !rotate_vertical ) {
                    		nextX = MSwingUtilities.drawCharsWithFont(component, g, alternative_font, txt,
                                    flushIndex, flushLen, x, y-FontAscentDiff);
                		} else {
	                		for( int k=0; k<flushLen; k++){
	        		            int dy = (alternative_font.getSize() - defaultMetrics.charsWidth(txt, flushIndex+k, 1))/2;
	        		            int dx = 2 - getDownIntend(txt[flushIndex+k]) * alternative_font.getSize()/6;
	        		            dy = dy + getRightIntend(txt[flushIndex+k]) * alternative_font.getSize()/6;
	        		            if( isRotated( txt[flushIndex+k] ) ){
		        		            x = x + alternative_font.getSize();
	        		            	dy = FontAscentDiff + dy;
		        		            nextX = MSwingUtilities.drawCharsWithFont(component, g, rotated_font, txt, flushIndex+k,
		        	                        1, x-dx, y-dy);
	        		            	nextX = x;
	        		            } else {
		        		            nextX = MSwingUtilities.drawCharsWithFont(component, g, alternative_font, txt, flushIndex+k,
		        	                        1, x, y);
	        		            }
	                		}
                		}
                	}
                    flushLen = 0;
                }
                flushIndex = i + 1;
                if (txt[i] == '\t') {
                    if (e != null) {
                        nextX = (int) e.nextTabStop((float) nextX, startOffset + i - txtOffset);
                    } else {
                        nextX += metrics.charWidth(' ');
                    }
                } else if (txt[i] == ' ') {
                    nextX += metrics.charWidth(' ') + spaceAddon;
                    if (i <= spaceAddonLeftoverEnd) {
                        nextX++;
                    }
                }
                x = nextX;
            } else if ((txt[i] == '\n') || (txt[i] == '\r')) {
                if (flushLen > 0) {
                	if( candisplay )
                		nextX = MSwingUtilities.drawCharsWithFont(component, g, font, txt,
                                                flushIndex, flushLen, x, y);
                	else {
                		if( !rotate_vertical ) {
                    		nextX = MSwingUtilities.drawCharsWithFont(component, g, alternative_font, txt,
                                    flushIndex, flushLen, x, y-FontAscentDiff);
                		} else {
	                		for( int k=0; k<flushLen; k++){
	        		            int dy = (alternative_font.getSize() - defaultMetrics.charsWidth(txt, flushIndex+k, 1))/2;
	        		            int dx = 2 - getDownIntend(txt[flushIndex+k]) * alternative_font.getSize()/6;
	        		            dy = dy + getRightIntend(txt[flushIndex+k]) * alternative_font.getSize()/12;
	        		            if( isRotated( txt[flushIndex+k] ) ){
		        		            x = x + alternative_font.getSize();
	        		            	dy = FontAscentDiff + dy;
		        		            nextX = MSwingUtilities.drawCharsWithFont(component, g, rotated_font, txt, flushIndex+k,
		        	                        1, x-dx, y-dy);
	        		            	nextX = x;
	        		            } else {
		        		            nextX = MSwingUtilities.drawCharsWithFont(component, g, alternative_font, txt, flushIndex+k,
		        	                        1, x, y);
	        		            }
	                		}
                		}
                	}
                		
                    flushLen = 0;
                }
                flushIndex = i + 1;
                x = nextX;
            } else if( candisplay ){
            	if( canDisplay(font, txt[i]) ) {
            		flushLen += 1;
            	} else {
                    if (flushLen > 0) {
                        nextX = MSwingUtilities.drawCharsWithFont(component, g, font, txt,
                                                    flushIndex, flushLen, x, y);
                        flushLen = 0;
                    }
                    flushIndex = i;
                    x = nextX;
                    candisplay = false;
            		flushLen += 1;
            	}
            } else {
                if (flushLen > 0) {
            		if( !rotate_vertical ) {
                		nextX = MSwingUtilities.drawCharsWithFont(component, g, alternative_font, txt,
                                flushIndex, flushLen, x, y-FontAscentDiff);
            		} else {
	            		for( int k=0; k<flushLen; k++){
        		            int dy = (alternative_font.getSize() - defaultMetrics.charsWidth(txt, flushIndex+k, 1))/2;
        		            int dx = 2 - getDownIntend(txt[flushIndex+k]) * alternative_font.getSize()/6;
        		            dy = dy + getRightIntend(txt[flushIndex+k]) * alternative_font.getSize()/12;
        		            if( isRotated( txt[flushIndex+k] ) ){
            		            x = x + alternative_font.getSize();
        		            	dy = FontAscentDiff + dy;
	        		            nextX = MSwingUtilities.drawCharsWithFont(component, g, rotated_font, txt, flushIndex+k,
	        	                        1, x-dx, y-dy);
        		            	nextX = x;
        		            } else {
	        		            nextX = MSwingUtilities.drawCharsWithFont(component, g, alternative_font, txt, flushIndex+k,
	        	                        1, x, y);
        		            }
	            		}
            		}
                    flushLen = 0;
                }
            	if( canDisplay(font, txt[i]) ) {
                    candisplay = true;
            	}
                flushIndex = i;
                x = nextX;
        		flushLen += 1;
            }
        }
        if (flushLen > 0) {
        	if( candisplay ) {
	            nextX = MSwingUtilities.drawCharsWithFont(component, g, font, txt, flushIndex,
	                                              flushLen, x, y);
        	}
        	else {
        		if( !rotate_vertical ) {
            		nextX = MSwingUtilities.drawCharsWithFont(component, g, alternative_font, txt,
                            flushIndex, flushLen, x, y-FontAscentDiff);
        		} else {
	        		for( int k=0; k<flushLen; k++){
    		            int dy = (alternative_font.getSize() - defaultMetrics.charsWidth(txt, flushIndex+k, 1))/2;
    		            int dx = 2 - getDownIntend(txt[flushIndex+k]) * alternative_font.getSize()/6;
    		            dy = dy + getRightIntend(txt[flushIndex+k]) * alternative_font.getSize()/12;
    		            if( isRotated( txt[flushIndex+k] ) ){
        		            x = x + alternative_font.getSize();
    		            	dy = FontAscentDiff + dy;
        		            nextX = MSwingUtilities.drawCharsWithFont(component, g, rotated_font, txt, flushIndex+k,
        	                        1, x-dx, y-dy);
    		            	nextX = x;
    		            } else {
        		            nextX = MSwingUtilities.drawCharsWithFont(component, g, alternative_font, txt, flushIndex+k,
        	                        1, x, y);
    		            }
	        		}
        		}
        	}
        }
        return nextX;
    }

    /**
     * Determines the width of the given segment of text taking tabs
     * into consideration.  This is implemented in a 1.1 style coordinate
     * system where ints are used and 72dpi is assumed.
     *
     * @param s  the source of the text
     * @param metrics the font metrics to use for the calculation
     * @param x  the X origin >= 0
     * @param e  how to expand the tabs.  If this value is null,
     *   tabs will be expanded as a space character.
     * @param startOffset starting offset of the text in the document >= 0
     * @return  the width of the text
     */
    public static final int getTabbedTextWidth(Segment s, FontMetrics metrics, int x,
                                               TabExpander e, int startOffset) {
        return getTabbedTextWidth(null, s, metrics, null, x, e, startOffset, null);
    }

    /**
     * Determines the width of the given segment of text taking tabs
     * into consideration.  This is implemented in a 1.1 style coordinate
     * system where ints are used and 72dpi is assumed.
     *
     * @param s  the source of the text
     * @param metrics the font metrics to use for the calculation
     * @param x  the X origin >= 0
     * @param e  how to expand the tabs.  If this value is null,
     *   tabs will be expanded as a space character.
     * @param startOffset starting offset of the text in the document >= 0
     * @return  the width of the text
     */
    public static final int getTabbedTextWidth(Segment s, FontMetrics metrics, FontMetrics defaultmetrics, int x,
                                               TabExpander e, int startOffset) {
        return getTabbedTextWidth(null, s, metrics, defaultmetrics, x, e, startOffset, null);
    }

    // In addition to the previous method it can extend spaces for
    // justification.
    //
    // all params are the same as in the preious method except the last
    // one:
    // @param justificationData justificationData for the row.
    // if null not justification is needed
    public static final int getTabbedTextWidth(MView mView, Segment s, FontMetrics metrics, FontMetrics defaultmetrics, int x,
                                        TabExpander e, int startOffset,
                                        int[] justificationData) {
        int nextX = x;
    	Font font = metrics.getFont();
    	if( defaultmetrics == null )
    		defaultmetrics = metrics;
                
        boolean rotate_vertical = false;
    	if ( mView!=null && mView.getRotateDirection() != MSwingRotateUtilities.ROTATE_HORIZANTAL ){
    		rotate_vertical = true;
    	}
        
        // MongolianConverter converter = new MongolianConverter();
        int start = s.offset;
        int count = s.count;
        // String target = converter.convert(new String(s.array, start, count));
        String target = new String(s.array, start, count);
        if( start < s.offset )
        	target = target.substring(s.offset-start);
        
        char[] txt = target.toCharArray();
        int txtOffset = 0;
        int n = s.count;
        int charCount = 0;
        int spaceAddon = 0;
        int spaceAddonLeftoverEnd = -1;
        int startJustifiableContent = 0;
        int endJustifiableContent = 0;
        if (justificationData != null) {
            int offset = - startOffset + txtOffset;
            View parent = null;
            if (mView != null
                  && (parent = mView.getParent()) != null) {
                offset += parent.getStartOffset();
            }
            spaceAddon =
                justificationData[SPACE_ADDON];
            spaceAddonLeftoverEnd =
                justificationData[SPACE_ADDON_LEFTOVER_END] + offset;
            startJustifiableContent =
                justificationData[START_JUSTIFIABLE] + offset;
            endJustifiableContent =
                justificationData[END_JUSTIFIABLE] + offset;
        }

        boolean candisplay = true;
        for (int i = 0; i < n && i < txt.length; i++) {
            if (txt[i] == '\t'
                || ((spaceAddon != 0 || i <= spaceAddonLeftoverEnd)
                    && (txt[i] == ' ')
                    && startJustifiableContent <= i
                    && i <= endJustifiableContent
                    )) {
                if( candisplay )
                	nextX += metrics.charsWidth(txt, i-charCount, charCount);
                else {
                	if( !rotate_vertical ){
                    	nextX += defaultmetrics.charsWidth(txt, i-charCount, charCount);
                	} else {
	            		for( int k=0; k<charCount; k++){
        		            if( isRotated( txt[i-charCount+k] ) ){
        		            	nextX += defaultmetrics.getFont().getSize();
        		            } else {
        		            	nextX += defaultmetrics.charsWidth(txt, i-charCount+k, 1);
        		            }
	            		}
                	}
                }
                charCount = 0;
                if (txt[i] == '\t') {
                    if (e != null) {
                        nextX = (int) e.nextTabStop((float) nextX,
                                                    startOffset + i - txtOffset);
                    } else {
                        nextX += metrics.charWidth(' ');
                    }
                } else if (txt[i] == ' ') {
                    nextX += metrics.charWidth(' ') + spaceAddon;
                    if (i <= spaceAddonLeftoverEnd) {
                        nextX++;
                    }
                }
            } else if(txt[i] == '\n') {
            // Ignore newlines, they take up space and we shouldn't be
            // counting them.
                if( candisplay )
                	nextX += metrics.charsWidth(txt, i - charCount, charCount);
                else {
                	if( !rotate_vertical ){
                    	nextX += defaultmetrics.charsWidth(txt, i-charCount, charCount);
                	} else {
	            		for( int k=0; k<charCount; k++){
        		            if( isRotated( txt[i-charCount+k] ) ){
        		            	nextX += defaultmetrics.getFont().getSize();
        		            } else {
        		            	nextX += defaultmetrics.charsWidth(txt, i-charCount+k, 1);
        		            }
	            		}
                	}
                }
                charCount = 0;
            } else if( candisplay ){
            	
            	if( canDisplay(font, txt[i]) ) {
                    charCount++;
            	} else {
            		nextX += metrics.charsWidth(txt, i - charCount, charCount);
                    charCount = 0;
                    candisplay = false;
                    charCount++;
            	}
                
            } else {
            	if( canDisplay(font, txt[i]) ) {
                	if( !rotate_vertical ){
                    	nextX += defaultmetrics.charsWidth(txt, i-charCount, charCount);
                	} else {
	            		for( int k=0; k<charCount; k++){
        		            if( isRotated( txt[i-charCount+k] ) ){
        		            	nextX += defaultmetrics.getFont().getSize();
        		            } else {
        		            	nextX += defaultmetrics.charsWidth(txt, i-charCount+k, 1);
        		            }
	            		}
                	}
                    charCount = 0;
                    candisplay = true;
            	}
                charCount++;
            }
        }
        if( candisplay )
        	nextX += metrics.charsWidth(txt, n - charCount, charCount);
        else{
        	if( !rotate_vertical ){
            	nextX += defaultmetrics.charsWidth(txt, n-charCount, charCount);
        	} else {
        		for( int k=0; k<charCount; k++){
		            if( isRotated( txt[n-charCount+k] ) ){
		            	nextX += defaultmetrics.getFont().getSize();
		            } else {
		            	nextX += defaultmetrics.charsWidth(txt, n-charCount+k, 1);
		            }
        		}
        	}
        }
        
        return nextX - x;
    }

    /**
     * Determines the relative offset into the given text that
     * best represents the given span in the view coordinate
     * system.  This is implemented in a 1.1 style coordinate
     * system where ints are used and 72dpi is assumed.
     *
     * @param s  the source of the text
     * @param metrics the font metrics to use for the calculation
     * @param x0 the starting view location representing the start
     *   of the given text >= 0.
     * @param x  the target view location to translate to an
     *   offset into the text >= 0.
     * @param e  how to expand the tabs.  If this value is null,
     *   tabs will be expanded as a space character.
     * @param startOffset starting offset of the text in the document >= 0
     * @return  the offset into the text >= 0
     */
    public static final int getTabbedTextOffset(Segment s, FontMetrics metrics, 
                                             int x0, int x, TabExpander e,
                                             int startOffset) {
        return getTabbedTextOffset(s, metrics, null, x0, x, e, startOffset);
    }

    public static final int getTabbedTextOffset(Segment s, FontMetrics metrics, FontMetrics defaultmetrics,
            int x0, int x, TabExpander e,
            int startOffset) {
    	return getTabbedTextOffset(s, metrics, defaultmetrics, x0, x, e, startOffset, true);
    }

    public static final int getTabbedTextOffset(MView mView, Segment s, FontMetrics metrics, FontMetrics defaultmetrics,
                                         int x0, int x, TabExpander e,
                                         int startOffset,
                                         int[] justificationData) {
        return getTabbedTextOffset(mView, s, metrics, defaultmetrics, x0, x, e, startOffset, true,
                                   justificationData);
    }

    public static final int getTabbedTextOffset(Segment s,
                                                FontMetrics metrics, FontMetrics defaultmetrics,
                                                int x0, int x, TabExpander e,
                                                int startOffset,
                                                boolean round) {
        return getTabbedTextOffset(null, s, metrics, defaultmetrics, x0, x, e, startOffset, round, null);
    }

    // In addition to the previous method it can extend spaces for
    // justification.
    //
    // all params are the same as in the preious method except the last
    // one:
    // @param justificationData justificationData for the row.
    // if null not justification is needed
    public static final int getTabbedTextOffset(MView mView,
                                         Segment s,
                                         FontMetrics metrics, FontMetrics defaultmetrics,
                                         int x0, int x, TabExpander e,
                                         int startOffset,
                                         boolean round,
                                         int[] justificationData) {
        if (x0 >= x) {
            // x before x0, return.
            return 0;
        }
        int nextX = x0;
        int prevWordEndX = x0;			// Jirmuto added --2020/07/20
        int prevWordEndOffset = 0;		// Jirmuto added --2020/07/20
    	Font font = metrics.getFont();
    	if( defaultmetrics == null )
    		defaultmetrics = metrics;
                
        boolean rotate_vertical = false;
        if( mView instanceof MWrappedPlainView ) {
        	MWrappedPlainView plainView = (MWrappedPlainView)mView;
        	if ( plainView.getRotateDirection() != MSwingRotateUtilities.ROTATE_HORIZANTAL ){
        		rotate_vertical = true;
        	}
        }
        
        // s may be a shared segment, so it is copied prior to calling
        // the tab expander
        // MongolianConverter converter = new MongolianConverter();
        int start = s.offset;
        int count = s.count;
        // String target = converter.convert(new String(s.array, start, count));
        String target = new String(s.array, start, count);
        if( start < s.offset )
        	target = target.substring(s.offset-start);
        
        char[] txt = target.toCharArray();
        int txtOffset = 0;
        int txtCount = s.count;
        int spaceAddon = 0 ;
        int spaceAddonLeftoverEnd = -1;
        int startJustifiableContent = 0 ;
        int endJustifiableContent = 0;
        if (justificationData != null) {
            int offset = - startOffset + txtOffset;
            View parent = null;
            if (mView != null
                  && (parent = mView.getParent()) != null) {
                offset += parent.getStartOffset();
            }
            spaceAddon =
                justificationData[SPACE_ADDON];
            spaceAddonLeftoverEnd =
                justificationData[SPACE_ADDON_LEFTOVER_END] + offset;
            startJustifiableContent =
                justificationData[START_JUSTIFIABLE] + offset;
            endJustifiableContent =
                justificationData[END_JUSTIFIABLE] + offset;
        }
        int n = s.count;
        for (int i = 0; i < n && i < txt.length; i++) {
            if (txt[i] == '\t'
                || ((spaceAddon != 0 || i <= spaceAddonLeftoverEnd)
                    && (txt[i] == ' ')
                    && startJustifiableContent <= i
                    && i <= endJustifiableContent
                    )){
                if (txt[i] == '\t') {
                    if (e != null) {
                        nextX = (int) e.nextTabStop((float) nextX,
                                                    startOffset + i - txtOffset);
                    } else {
                        nextX += metrics.charWidth(' ');
                    }
                } else if (txt[i] == ' ') {
                    nextX += metrics.charWidth(' ') + spaceAddon;
                    if (i <= spaceAddonLeftoverEnd) {
                        nextX++;
                    }
                }
                prevWordEndX = nextX;	// Jirmuto added --2020/07/20
                prevWordEndOffset = i;	// Jirmuto added --2020/07/20
            } else if( canDisplay(font, txt[i]) ) {
        		nextX += SwingUtilities2.getFontCharWidth(txt[i], metrics, true);
        		
        		if( (txt[i] < MCodeConstant.UNI1820_A || txt[i] > MCodeConstant.UNI18AA_AU) 
        				&& txt[i] != MCodeConstant.UNI202F_NOBREAK ) {
            		// Jirmuto added --start 2020/07/20
            		int width = metrics.charsWidth(txt, prevWordEndOffset, i-prevWordEndOffset);
            		if( width != nextX - prevWordEndX )
            			nextX = prevWordEndX + width;
                    prevWordEndX = nextX;
                    prevWordEndOffset = i;
        		}
        		// Jirmuto added --end 2020/07/20
            } else {
            	
        		int width = metrics.charsWidth(txt, prevWordEndOffset, i-prevWordEndOffset);
        		if( width != nextX - prevWordEndX )
        			nextX = prevWordEndX + width;
                if (x < nextX) {
                	return i -1 - txtOffset;
                }
                
            	if( !rotate_vertical ){
            		nextX += SwingUtilities2.getFontCharWidth(txt[i], defaultmetrics, true);
            	} else {
		            if( isRotated( txt[i] ) ){
		            	nextX += defaultmetrics.getFont().getSize();
		            } else {
	            		nextX += SwingUtilities2.getFontCharWidth(txt[i], defaultmetrics, true);
		            }
            	}
                if (x < nextX) {
                	return i -1 - txtOffset;
                }
                prevWordEndX = nextX;	// Jirmuto added --2020/07/20
                prevWordEndOffset = i;	// Jirmuto added --2020/07/20
            }
            if (x < nextX) {
                // found the hit position... return the appropriate side
                int offset;

                // the length of the string measured as a whole may differ from
                // the sum of individual character lengths, for example if
                // fractional metrics are enabled; and we must guard from this.
                if (round) {
                    offset = i + 1 - txtOffset;

                    // int width = metrics.charsWidth(txt, txtOffset, offset);
                    int width = (int)SwingUtilities2.getFontCharsWidth(txt, txtOffset, offset, metrics, true);
                    int span = x - x0;

                    if (span < (int)width) {
                        while (offset > 0) {
                        	int tempwidth = (int)SwingUtilities2.getFontCharsWidth(txt, txtOffset, offset-1, metrics, true);
                            int nextWidth = offset > 1 ? tempwidth : 0;
                            // int nextWidth = offset > 1 ? metrics.charsWidth(txt, txtOffset, offset - 1) : 0;

                            if (span >= nextWidth) {
                                if (span - nextWidth < width - span) {
                                    offset--;
                                }

                                break;
                            }

                            width = nextWidth;
                            offset--;
                        }
                    }
                } else {
                    offset = i - txtOffset;
                	int tempwidth = (int)SwingUtilities2.getFontCharsWidth(txt, txtOffset, offset, metrics, true);
                    while (offset > 0 && tempwidth > (x - x0)) {
                    // while (offset > 0 && metrics.charsWidth(txt, txtOffset, offset) > (x - x0)) {
                        offset--;
                        tempwidth = (int)SwingUtilities2.getFontCharsWidth(txt, txtOffset, offset, metrics, true);
                    }
                }

                return offset;
            }
        }

        // didn't find, return end offset
        return txtCount;
    }

    /**
     * Determine where to break the given text to fit
     * within the given span. This tries to find a word boundary.
     * @param s  the source of the text
     * @param metrics the font metrics to use for the calculation
     * @param x0 the starting view location representing the start
     *   of the given text.
     * @param x  the target view location to translate to an
     *   offset into the text.
     * @param e  how to expand the tabs.  If this value is null,
     *   tabs will be expanded as a space character.
     * @param startOffset starting offset in the document of the text
     * @return  the offset into the given text
     */
    public static final int getBreakLocation(Segment s, FontMetrics metrics, FontMetrics defaultmetrics,
                                             int x0, int x, TabExpander e,
                                             int startOffset) {
        char[] txt = s.array;
        int txtOffset = s.offset;
        int txtCount = s.count;
        int index = MUtilities.getTabbedTextOffset(s, metrics, defaultmetrics, x0, x,
                                                  e, startOffset, false);

        if (index >= txtCount - 1) {
            return txtCount;
        }

        for (int i = txtOffset + index; i >= txtOffset; i--) {
            char ch = txt[i];
            if (ch < 256) {
                // break on whitespace
                if (Character.isWhitespace(ch)) {
                    index = i - txtOffset + 1;
                    break;
                }
                
            } else if( (ch < MCodeConstant.UNI1800_BIRGA || ch > MCodeConstant.UNI18AA_AU) && ch != MCodeConstant.UNI202F_NOBREAK ){
            	
                // a multibyte char found; use BreakIterator to find line break
                BreakIterator bit = BreakIterator.getLineInstance();
                bit.setText(s);
                int breakPos = bit.preceding(i + 1);
                if (breakPos > txtOffset) {
                    index = breakPos - txtOffset;
                }
                break;
            }
        }
        return index;
    }

    /**
     * Determines the starting row model position of the row that contains
     * the specified model position.  The component given must have a
     * size to compute the result.  If the component doesn't have a size
     * a value of -1 will be returned.
     *
     * @param c the editor
     * @param offs the offset in the document >= 0
     * @return the position >= 0 if the request can be computed, otherwise
     *  a value of -1 will be returned.
     * @exception BadLocationException if the offset is out of range
     */
    public static final int getRowStart(JTextComponent c, int offs) throws BadLocationException {
        Rectangle r = c.modelToView(offs);
        if (r == null) {
            return -1;
        }
        int lastOffs = offs;
        int y = r.y;
        while ((r != null) && (y == r.y)) {
            // Skip invisible elements
            if(r.height !=0) {
                offs = lastOffs;
            }
            lastOffs -= 1;
            r = (lastOffs >= 0) ? c.modelToView(lastOffs) : null;
        }
        return offs;
    }

    public static final int getRowStartL2R(JTextComponent c, int offs) throws BadLocationException {
        Rectangle r = c.modelToView(offs);
        if (r == null) {
            return -1;
        }
        int lastOffs = offs;
        int x = r.x;
        while ((r != null) && (x == r.x)) {
            // Skip invisible elements
            if(r.height !=0) {
                offs = lastOffs;
            }
            lastOffs -= 1;
            r = (lastOffs >= 0) ? c.modelToView(lastOffs) : null;
        }
        return offs;
    }
    
    public static final int getRowStartR2L(JTextComponent c, int offs) throws BadLocationException {
        Rectangle r = c.modelToView(offs);
        if (r == null) {
            return -1;
        }
        int lastOffs = offs;
        int x = r.x;
        while ((r != null) && (x == r.x)) {
            // Skip invisible elements
            if(r.height !=0) {
                offs = lastOffs;
            }
            lastOffs -= 1;
            r = (lastOffs >= 0) ? c.modelToView(lastOffs) : null;
        }
        return offs;
    }
    
    /**
     * Determines the ending row model position of the row that contains
     * the specified model position.  The component given must have a
     * size to compute the result.  If the component doesn't have a size
     * a value of -1 will be returned.
     *
     * @param c the editor
     * @param offs the offset in the document >= 0
     * @return the position >= 0 if the request can be computed, otherwise
     *  a value of -1 will be returned.
     * @exception BadLocationException if the offset is out of range
     */
    public static final int getRowEnd(JTextComponent c, int offs) throws BadLocationException {
        Rectangle r = c.modelToView(offs);
        if (r == null) {
            return -1;
        }
        int n = c.getDocument().getLength();
        int lastOffs = offs;
        int y = r.y;
        while ((r != null) && (y == r.y)) {
            // Skip invisible elements
            if (r.height !=0) {
                offs = lastOffs;
            }
            lastOffs += 1;
            r = (lastOffs <= n) ? c.modelToView(lastOffs) : null;
        }
        return offs;
    }

    public static final int getRowEndL2R(JTextComponent c, int offs) throws BadLocationException {
        Rectangle r = c.modelToView(offs);
        if (r == null) {
            return -1;
        }
        int n = c.getDocument().getLength();
        int lastOffs = offs;
        int x = r.x;
        while ((r != null) && (x == r.x)) {
            // Skip invisible elements
            if (r.height !=0) {
                offs = lastOffs;
            }
            lastOffs += 1;
            r = (lastOffs <= n) ? c.modelToView(lastOffs) : null;
        }
        return offs;
    }
    public static final int getRowEndR2L(JTextComponent c, int offs) throws BadLocationException {
        Rectangle r = c.modelToView(offs);
        if (r == null) {
            return -1;
        }
        int n = c.getDocument().getLength();
        int lastOffs = offs;
        int x = r.x;
        while ((r != null) && (x == r.x)) {
            // Skip invisible elements
            if (r.height !=0) {
                offs = lastOffs;
            }
            lastOffs += 1;
            r = (lastOffs <= n) ? c.modelToView(lastOffs) : null;
        }
        return offs;
    }
    
    /**
     * Determines the position in the model that is closest to the given
     * view location in the row above.  The component given must have a
     * size to compute the result.  If the component doesn't have a size
     * a value of -1 will be returned.
     *
     * @param c the editor
     * @param offs the offset in the document >= 0
     * @param x the X coordinate >= 0
     * @return the position >= 0 if the request can be computed, otherwise
     *  a value of -1 will be returned.
     * @exception BadLocationException if the offset is out of range
     */
    public static final int getPositionAbove(JTextComponent c, int offs, int x) throws BadLocationException {
        int lastOffs = getRowStart(c, offs) - 1;
        if (lastOffs < 0) {
            return -1;
        }
        int bestSpan = Integer.MAX_VALUE;
        int y = 0;
        Rectangle r = null;
        if (lastOffs >= 0) {
            r = c.modelToView(lastOffs);
            y = r.y;
        }
        while ((r != null) && (y == r.y)) {
            int span = Math.abs(r.x - x);
            if (span < bestSpan) {
                offs = lastOffs;
                bestSpan = span;
            }
            lastOffs -= 1;
            r = (lastOffs >= 0) ? c.modelToView(lastOffs) : null;
        }
        return offs;
    }

    public static final int getPositionAboveL2R(JTextComponent c, int offs, int y) throws BadLocationException {
        int lastOffs = getRowStartL2R(c, offs) - 1;
        if (lastOffs < 0) {
            return -1;
        }
        int bestSpan = Integer.MAX_VALUE;
        int x = 0;
        Rectangle r = null;
        if (lastOffs >= 0) {
            r = c.modelToView(lastOffs);
            x = r.x;
        }
        while ((r != null) && (x == r.x)) {
            int span = Math.abs(r.y - y);
            if (span < bestSpan) {
                offs = lastOffs;
                bestSpan = span;
            }
            lastOffs -= 1;
            r = (lastOffs >= 0) ? c.modelToView(lastOffs) : null;
        }
        return offs;
    }
    
    public static final int getPositionAboveR2L(JTextComponent c, int offs, int y) throws BadLocationException {
        int lastOffs = getRowStartR2L(c, offs) - 1;
        if (lastOffs < 0) {
            return -1;
        }
        int bestSpan = Integer.MAX_VALUE;
        int x = 0;
        Rectangle r = null;
        if (lastOffs >= 0) {
            r = c.modelToView(lastOffs);
            x = r.x;
        }
        while ((r != null) && (x == r.x)) {
            int span = Math.abs(r.y - y);
            if (span < bestSpan) {
                offs = lastOffs;
                bestSpan = span;
            }
            lastOffs -= 1;
            r = (lastOffs >= 0) ? c.modelToView(lastOffs) : null;
        }
        return offs;
    }
    
    /**
     * Determines the position in the model that is closest to the given
     * view location in the row below.  The component given must have a
     * size to compute the result.  If the component doesn't have a size
     * a value of -1 will be returned.
     *
     * @param c the editor
     * @param offs the offset in the document >= 0
     * @param x the X coordinate >= 0
     * @return the position >= 0 if the request can be computed, otherwise
     *  a value of -1 will be returned.
     * @exception BadLocationException if the offset is out of range
     */
    public static final int getPositionBelow(JTextComponent c, int offs, int x) throws BadLocationException {
        int lastOffs = getRowEnd(c, offs) + 1;
        if (lastOffs <= 0) {
            return -1;
        }
        int bestSpan = Integer.MAX_VALUE;
        int n = c.getDocument().getLength();
        int y = 0;
        Rectangle r = null;
        if (lastOffs <= n) {
            r = c.modelToView(lastOffs);
            y = r.y;
        }
        while ((r != null) && (y == r.y)) {
            int span = Math.abs(x - r.x);
            if (span < bestSpan) {
                offs = lastOffs;
                bestSpan = span;
            }
            lastOffs += 1;
            r = (lastOffs <= n) ? c.modelToView(lastOffs) : null;
        }
        return offs;
    }

    public static final int getPositionBelowL2R(JTextComponent c, int offs, int y) throws BadLocationException {
        int lastOffs = getRowEndL2R(c, offs) + 1;
        if (lastOffs <= 0) {
            return -1;
        }
        int bestSpan = Integer.MAX_VALUE;
        int n = c.getDocument().getLength();
        int x = 0;
        Rectangle r = null;
        if (lastOffs <= n) {
            r = c.modelToView(lastOffs);
            x = r.x;
        }
        while ((r != null) && (x == r.x)) {
            int span = Math.abs(y - r.y);
            if (span < bestSpan) {
                offs = lastOffs;
                bestSpan = span;
            }
            lastOffs += 1;
            r = (lastOffs <= n) ? c.modelToView(lastOffs) : null;
        }
        return offs;
    }

    public static final int getPositionBelowR2L(JTextComponent c, int offs, int y) throws BadLocationException {
        int lastOffs = getRowEndR2L(c, offs) + 1;
        if (lastOffs <= 0) {
            return -1;
        }
        int bestSpan = Integer.MAX_VALUE;
        int n = c.getDocument().getLength();
        int x = 0;
        Rectangle r = null;
        if (lastOffs <= n) {
            r = c.modelToView(lastOffs);
            x = r.x;
        }
        while ((r != null) && (x == r.x)) {
            int span = Math.abs(y - r.y);
            if (span < bestSpan) {
                offs = lastOffs;
                bestSpan = span;
            }
            lastOffs += 1;
            r = (lastOffs <= n) ? c.modelToView(lastOffs) : null;
        }
        return offs;
    }
    /**
     * Determines the start of a word for the given model location.
     * Uses BreakIterator.getWordInstance() to actually get the words.
     *
     * @param c the editor
     * @param offs the offset in the document >= 0
     * @return the location in the model of the word start >= 0
     * @exception BadLocationException if the offset is out of range
     */
    public static final int getWordStart(JTextComponent c, int offs) throws BadLocationException {
        Document doc = c.getDocument();
        Element line = getParagraphElement(c, offs);
        if (line == null) {
            throw new BadLocationException("No word at " + offs, offs);
        }
        int lineStart = line.getStartOffset();
        int lineEnd = Math.min(line.getEndOffset(), doc.getLength());

        Segment seg = MSegmentCache.getSharedSegment();
        doc.getText(lineStart, lineEnd - lineStart, seg);
        if(seg.count > 0) {
            BreakIterator words = BreakIterator.getWordInstance(c.getLocale());
            words.setText(seg);
            int wordPosition = seg.offset + offs - lineStart;
            if(wordPosition >= words.last()) {
                wordPosition = words.last() - 1;
            }
            words.following(wordPosition);
            offs = lineStart + words.previous() - seg.offset;
        }
        MSegmentCache.releaseSharedSegment(seg);
        return offs;
    }

    /**
     * Determines the end of a word for the given location.
     * Uses BreakIterator.getWordInstance() to actually get the words.
     *
     * @param c the editor
     * @param offs the offset in the document >= 0
     * @return the location in the model of the word end >= 0
     * @exception BadLocationException if the offset is out of range
     */
    public static final int getWordEnd(JTextComponent c, int offs) throws BadLocationException {
        Document doc = c.getDocument();
        Element line = getParagraphElement(c, offs);
        if (line == null) {
            throw new BadLocationException("No word at " + offs, offs);
        }
        int lineStart = line.getStartOffset();
        int lineEnd = Math.min(line.getEndOffset(), doc.getLength());

        Segment seg = MSegmentCache.getSharedSegment();
        doc.getText(lineStart, lineEnd - lineStart, seg);
        if(seg.count > 0) {
            BreakIterator words = BreakIterator.getWordInstance(c.getLocale());
            words.setText(seg);
            int wordPosition = offs - lineStart + seg.offset;
            if(wordPosition >= words.last()) {
                wordPosition = words.last() - 1;
            }
            offs = lineStart + words.following(wordPosition) - seg.offset;
        }
        MSegmentCache.releaseSharedSegment(seg);
        return offs;
    }

    /**
     * Determines the start of the next word for the given location.
     * Uses BreakIterator.getWordInstance() to actually get the words.
     *
     * @param c the editor
     * @param offs the offset in the document >= 0
     * @return the location in the model of the word start >= 0
     * @exception BadLocationException if the offset is out of range
     */
    public static final int getNextWord(JTextComponent c, int offs) throws BadLocationException {
        int nextWord;
        Element line = getParagraphElement(c, offs);
        for (nextWord = getNextWordInParagraph(c, line, offs, false);
             nextWord == BreakIterator.DONE;
             nextWord = getNextWordInParagraph(c, line, offs, true)) {

            // didn't find in this line, try the next line
            offs = line.getEndOffset();
            line = getParagraphElement(c, offs);
        }
        return nextWord;
    }

    /**
     * Finds the next word in the given elements text.  The first
     * parameter allows searching multiple paragraphs where even
     * the first offset is desired.
     * Returns the offset of the next word, or BreakIterator.DONE
     * if there are no more words in the element.
     */
    static int getNextWordInParagraph(JTextComponent c, Element line, int offs, boolean first) throws BadLocationException {
        if (line == null) {
            throw new BadLocationException("No more words", offs);
        }
        Document doc = line.getDocument();
        int lineStart = line.getStartOffset();
        int lineEnd = Math.min(line.getEndOffset(), doc.getLength());
        if ((offs >= lineEnd) || (offs < lineStart)) {
            throw new BadLocationException("No more words", offs);
        }
        Segment seg = MSegmentCache.getSharedSegment();
        doc.getText(lineStart, lineEnd - lineStart, seg);
        BreakIterator words = BreakIterator.getWordInstance(c.getLocale());
        words.setText(seg);
        if ((first && (words.first() == (seg.offset + offs - lineStart))) &&
            (! Character.isWhitespace(seg.array[words.first()]))) {

            return offs;
        }
        int wordPosition = words.following(seg.offset + offs - lineStart);
        if ((wordPosition == BreakIterator.DONE) ||
            (wordPosition >= seg.offset + seg.count)) {
                // there are no more words on this line.
                return BreakIterator.DONE;
        }
        // if we haven't shot past the end... check to
        // see if the current boundary represents whitespace.
        // if so, we need to try again
        char ch = seg.array[wordPosition];
        if (! Character.isWhitespace(ch)) {
            return lineStart + wordPosition - seg.offset;
        }

        // it was whitespace, try again.  The assumption
        // is that it must be a word start if the last
        // one had whitespace following it.
        wordPosition = words.next();
        if (wordPosition != BreakIterator.DONE) {
            offs = lineStart + wordPosition - seg.offset;
            if (offs != lineEnd) {
                return offs;
            }
        }
        MSegmentCache.releaseSharedSegment(seg);
        return BreakIterator.DONE;
    }


    /**
     * Determine the start of the prev word for the given location.
     * Uses BreakIterator.getWordInstance() to actually get the words.
     *
     * @param c the editor
     * @param offs the offset in the document >= 0
     * @return the location in the model of the word start >= 0
     * @exception BadLocationException if the offset is out of range
     */
    public static final int getPreviousWord(JTextComponent c, int offs) throws BadLocationException {
        int prevWord;
        Element line = getParagraphElement(c, offs);
        for (prevWord = getPrevWordInParagraph(c, line, offs);
             prevWord == BreakIterator.DONE;
             prevWord = getPrevWordInParagraph(c, line, offs)) {

            // didn't find in this line, try the prev line
            offs = line.getStartOffset() - 1;
            line = getParagraphElement(c, offs);
        }
        return prevWord;
    }

    /**
     * Finds the previous word in the given elements text.  The first
     * parameter allows searching multiple paragraphs where even
     * the first offset is desired.
     * Returns the offset of the next word, or BreakIterator.DONE
     * if there are no more words in the element.
     */
    static int getPrevWordInParagraph(JTextComponent c, Element line, int offs) throws BadLocationException {
        if (line == null) {
            throw new BadLocationException("No more words", offs);
        }
        Document doc = line.getDocument();
        int lineStart = line.getStartOffset();
        int lineEnd = line.getEndOffset();
        if ((offs > lineEnd) || (offs < lineStart)) {
            throw new BadLocationException("No more words", offs);
        }
        Segment seg = MSegmentCache.getSharedSegment();
        doc.getText(lineStart, lineEnd - lineStart, seg);
        BreakIterator words = BreakIterator.getWordInstance(c.getLocale());
        words.setText(seg);
        if (words.following(seg.offset + offs - lineStart) == BreakIterator.DONE) {
            words.last();
        }
        int wordPosition = words.previous();
        if (wordPosition == (seg.offset + offs - lineStart)) {
            wordPosition = words.previous();
        }

        if (wordPosition == BreakIterator.DONE) {
            // there are no more words on this line.
            return BreakIterator.DONE;
        }
        // if we haven't shot past the end... check to
        // see if the current boundary represents whitespace.
        // if so, we need to try again
        char ch = seg.array[wordPosition];
        if (! Character.isWhitespace(ch)) {
            return lineStart + wordPosition - seg.offset;
        }

        // it was whitespace, try again.  The assumption
        // is that it must be a word start if the last
        // one had whitespace following it.
        wordPosition = words.previous();
        if (wordPosition != BreakIterator.DONE) {
            return lineStart + wordPosition - seg.offset;
        }
        MSegmentCache.releaseSharedSegment(seg);
        return BreakIterator.DONE;
    }

    /**
     * Determines the element to use for a paragraph/line.
     *
     * @param c the editor
     * @param offs the starting offset in the document >= 0
     * @return the element
     */
    public static final Element getParagraphElement(JTextComponent c, int offs) {
        Document doc = c.getDocument();
        if (doc instanceof StyledDocument) {
            return ((StyledDocument)doc).getParagraphElement(offs);
        }
        Element map = doc.getDefaultRootElement();
        int index = map.getElementIndex(offs);
        Element paragraph = map.getElement(index);
        if ((offs >= paragraph.getStartOffset()) && (offs < paragraph.getEndOffset())) {
            return paragraph;
        }
        return null;
    }

    static boolean isComposedTextElement(Document doc, int offset) {
        Element elem = doc.getDefaultRootElement();
        while (!elem.isLeaf()) {
            elem = elem.getElement(elem.getElementIndex(offset));
        }
        return isComposedTextElement(elem);
    }

    static boolean isComposedTextElement(Element elem) {
        AttributeSet as = elem.getAttributes();
        return isComposedTextAttributeDefined(as);
    }

    static boolean isComposedTextAttributeDefined(AttributeSet as) {
        return ((as != null) &&
                (as.isDefined(StyleConstants.ComposedTextAttribute)));
    }

    /**
     * Draws the given composed text passed from an input method.
     *
     * @param mView View hosting text
     * @param attr the attributes containing the composed text
     * @param g  the graphics context
     * @param x  the X origin
     * @param y  the Y origin
     * @param p0 starting offset in the composed text to be rendered
     * @param p1 ending offset in the composed text to be rendered
     * @return  the new insertion position
     */
    static int drawComposedText(MView mView, AttributeSet attr, Graphics g,
                                int x, int y, int p0, int p1)
                                     throws BadLocationException {
        Graphics2D g2d = (Graphics2D)g;
        AttributedString as = (AttributedString)attr.getAttribute(
            StyleConstants.ComposedTextAttribute);
        Font defaultfont = UIManager.getFont("TextField.font");
        int size = (g.getFont().getSize()+defaultfont.getSize())/2;
        defaultfont = defaultfont.deriveFont(Font.PLAIN, size);
        as.addAttribute(TextAttribute.FONT, defaultfont);

        if (p0 >= p1)
            return x;

        AttributedCharacterIterator aci = as.getIterator(null, p0, p1);
        return x + (int)MSwingUtilities.drawString(
                             getJComponent(mView), g2d,aci,x,y);
    }

    /**
     * Paints the composed text in a GlyphView
     */
    public static void paintComposedText(Graphics g, Rectangle alloc, MGlyphView v) {
        if (g instanceof Graphics2D) {
            Graphics2D g2d = (Graphics2D) g;
            int p0 = v.getStartOffset();
            int p1 = v.getEndOffset();
            AttributeSet attrSet = v.getElement().getAttributes();
            AttributedString as =
                (AttributedString)attrSet.getAttribute(StyleConstants.ComposedTextAttribute);
            int start = v.getElement().getStartOffset();
            if( v.getRotateDirection() == MSwingRotateUtilities.ROTATE_HORIZANTAL ){
	            int y = alloc.y + alloc.height - (int)v.getGlyphPainter().getDescent(v);
	            int x = alloc.x;
	
	            //Add text attributes
	            as.addAttribute(TextAttribute.FONT, v.getFont());
	            as.addAttribute(TextAttribute.FOREGROUND, v.getForeground());
	            if (StyleConstants.isBold(v.getAttributes())) {
	                as.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
	            }
	            if (StyleConstants.isItalic(v.getAttributes())) {
	                as.addAttribute(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
	            }
	            if (v.isUnderline()) {
	                as.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
	            }
	            if (v.isStrikeThrough()) {
	                as.addAttribute(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
	            }
	            if (v.isSuperscript()) {
	                as.addAttribute(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUPER);
	            }
	            if (v.isSubscript()) {
	                as.addAttribute(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUB);
	            }
	
	            // draw
	            AttributedCharacterIterator aci = as.getIterator(null, p0 - start, p1 - start);
	            MSwingUtilities.drawString(getJComponent(v), g2d, aci, x, y);
            } else if( v.getRotateHint() != MSwingRotateUtilities.ROTATE_RIGHTTOLEFT ) {
	            int x = alloc.y;
	            int y = -alloc.x - (int)v.getGlyphPainter().getDescent(v);
	
	            //Add text attributes
	            as.addAttribute(TextAttribute.FONT, v.getFont());
	            as.addAttribute(TextAttribute.FOREGROUND, v.getForeground());
	            if (StyleConstants.isBold(v.getAttributes())) {
	                as.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
	            }
	            if (StyleConstants.isItalic(v.getAttributes())) {
	                as.addAttribute(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
	            }
	            if (v.isUnderline()) {
	                as.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
	            }
	            if (v.isStrikeThrough()) {
	                as.addAttribute(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
	            }
	            if (v.isSuperscript()) {
	                as.addAttribute(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUPER);
	            }
	            if (v.isSubscript()) {
	                as.addAttribute(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUB);
	            }
	
				AffineTransform transform = g2d.getTransform();
				
		        ((Graphics2D) g).rotate(Math.PI * 1 / 2);
		        
	            // draw
	            AttributedCharacterIterator aci = as.getIterator(null, p0 - start, p1 - start);
	            MSwingUtilities.drawString(getJComponent(v), g2d, aci, x, y);
	            
		    	g2d.setTransform(transform);
            	
            } else {
	            int y = alloc.y + alloc.height - (int)v.getGlyphPainter().getDescent(v);
	            int x = alloc.x;
	
	            //Add text attributes
	            as.addAttribute(TextAttribute.FONT, v.getFont());
	            as.addAttribute(TextAttribute.FOREGROUND, v.getForeground());
	            if (StyleConstants.isBold(v.getAttributes())) {
	                as.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
	            }
	            if (StyleConstants.isItalic(v.getAttributes())) {
	                as.addAttribute(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
	            }
	            if (v.isUnderline()) {
	                as.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
	            }
	            if (v.isStrikeThrough()) {
	                as.addAttribute(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
	            }
	            if (v.isSuperscript()) {
	                as.addAttribute(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUPER);
	            }
	            if (v.isSubscript()) {
	                as.addAttribute(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUB);
	            }
	
				AffineTransform transform = g2d.getTransform();
				
		        ((Graphics2D) g).rotate(Math.PI * 1 / 2);
	            // g2d.translate(0, -height);
		        
	            // draw
	            AttributedCharacterIterator aci = as.getIterator(null, p0 - start, p1 - start);
	            MSwingUtilities.drawString(getJComponent(v), g2d, aci, x, y);
            	
		    	g2d.setTransform(transform);
            	
            }
        }
    }

    /*
     * Convenience function for determining ComponentOrientation.  Helps us
     * avoid having Munge directives throughout the code.
     */
    static boolean isLeftToRight( java.awt.Component c ) {
        return c.getComponentOrientation().isLeftToRight();
    }


    /**
     * Provides a way to determine the next visually represented model
     * location that one might place a caret.  Some views may not be visible,
     * they might not be in the same order found in the model, or they just
     * might not allow access to some of the locations in the model.
     * <p>
     * This implementation assumes the views are layed out in a logical
     * manner. That is, that the view at index x + 1 is visually after
     * the View at index x, and that the View at index x - 1 is visually
     * before the View at x. There is support for reversing this behavior
     * only if the passed in <code>View</code> is an instance of
     * <code>CompositeView</code>. The <code>CompositeView</code>
     * must then override the <code>flipEastAndWestAtEnds</code> method.
     *
     * @param v View to query
     * @param pos the position to convert >= 0
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
     * @param biasRet an array contain the bias that was checked
     * @return the location within the model that best represents the next
     *  location visual position
     * @exception BadLocationException
     * @exception IllegalArgumentException if <code>direction</code> is invalid
     */
    static int getNextVisualPositionFrom(MView v, int pos, Position.Bias b,
                                          Shape alloc, int direction,
                                          Position.Bias[] biasRet, int rotate_direction, int rotate_hint)
                             throws BadLocationException {
        if (v.getViewCount() == 0) {
            // Nothing to do.
            return pos;
        }
        if( rotate_direction == MSwingRotateUtilities.ROTATE_HORIZANTAL ) {
        	
        	return getNextVisualPositionFromHorizontal(v, pos, b, alloc, direction, biasRet);
        	
        } else if( rotate_hint != MSwingRotateUtilities.ROTATE_RIGHTTOLEFT ) {
        	
        	return getNextVisualPositionFromVerticalL2R(v, pos, b, alloc, direction, biasRet);
        	
        } else {
        	
        	return getNextVisualPositionFromVerticalR2L(v, pos, b, alloc, direction, biasRet);
        	
        }
    }
    
    private static int getNextVisualPositionFromHorizontal(MView v, int pos, Position.Bias b,
            Shape alloc, int direction, Position.Bias[] biasRet)
		throws BadLocationException {
    	
        boolean top = (direction == SwingConstants.NORTH ||
                direction == SwingConstants.WEST);
 
        int retValue;
        if (pos == -1) {
            // Start from the first View.
            int childIndex = (top) ? v.getViewCount() - 1 : 0;
            View child = v.getView(childIndex);
            Shape childBounds = v.getChildAllocation(childIndex, alloc);
            retValue = child.getNextVisualPositionFrom(pos, b, childBounds,
                                                       direction, biasRet);
            if (retValue == -1 && !top && v.getViewCount() > 1) {
                // Special case that should ONLY happen if first view
                // isn't valid (can happen when end position is put at
                // beginning of line.
                child = v.getView(1);
                childBounds = v.getChildAllocation(1, alloc);
                retValue = child.getNextVisualPositionFrom(-1, biasRet[0],
                                                           childBounds,
                                                           direction, biasRet);
            }
        }
        else {
            int increment = (top) ? -1 : 1;
            int childIndex;
            if (b == Position.Bias.Backward && pos > 0) {
                childIndex = v.getViewIndex(pos - 1, Position.Bias.Forward);
            }
            else {
                childIndex = v.getViewIndex(pos, Position.Bias.Forward);
            }
            View child = v.getView(childIndex);
            Shape childBounds = v.getChildAllocation(childIndex, alloc);
            retValue = child.getNextVisualPositionFrom(pos, b, childBounds,
                                                       direction, biasRet);
            if ((direction == SwingConstants.EAST ||
                 direction == SwingConstants.WEST) &&
                (v instanceof MCompositeView) &&
                flipEastAndWestAtEnds(pos, b)) {
                increment *= -1;
            }
            childIndex += increment;
            if (retValue == -1 && childIndex >= 0 &&
                                  childIndex < v.getViewCount()) {
                child = v.getView(childIndex);
                childBounds = v.getChildAllocation(childIndex, alloc);
                retValue = child.getNextVisualPositionFrom(
                                     -1, b, childBounds, direction, biasRet);
                // If there is a bias change, it is a fake position
                // and we should skip it. This is usually the result
                // of two elements side be side flowing the same way.
                if (retValue == pos && biasRet[0] != b) {
                    return getNextVisualPositionFromHorizontal(v, pos, biasRet[0],
                                                     alloc, direction,
                                                     biasRet);
                }
            }
            else if (retValue != -1 && biasRet[0] != b &&
                     ((increment == 1 && child.getEndOffset() == retValue) ||
                      (increment == -1 &&
                       child.getStartOffset() == retValue)) &&
                     childIndex >= 0 && childIndex < v.getViewCount()) {
                // Reached the end of a view, make sure the next view
                // is a different direction.
                child = v.getView(childIndex);
                childBounds = v.getChildAllocation(childIndex, alloc);
                Position.Bias originalBias = biasRet[0];
                int nextPos = child.getNextVisualPositionFrom(
                                    -1, b, childBounds, direction, biasRet);
                if (biasRet[0] == b) {
                    retValue = nextPos;
                }
                else {
                    biasRet[0] = originalBias;
                }
            }
        }
        return retValue;
		
    }
    
    private static int getNextVisualPositionFromVerticalL2R(MView v, int pos, Position.Bias b,
            Shape alloc, int direction, Position.Bias[] biasRet)
		throws BadLocationException {
    	
        boolean top = (direction == SwingConstants.NORTH ||
                direction == SwingConstants.WEST);
 
        int retValue;
        if (pos == -1) {
            // Start from the first View.
            int childIndex = (top) ? v.getViewCount() - 1 : 0;
            View child = v.getView(childIndex);
            Shape childBounds = v.getChildAllocation(childIndex, alloc);
            retValue = child.getNextVisualPositionFrom(pos, b, childBounds,
                                                       direction, biasRet);
            if (retValue == -1 && !top && v.getViewCount() > 1) {
                // Special case that should ONLY happen if first view
                // isn't valid (can happen when end position is put at
                // beginning of line.
                child = v.getView(1);
                childBounds = v.getChildAllocation(1, alloc);
                retValue = child.getNextVisualPositionFrom(-1, biasRet[0],
                                                           childBounds,
                                                           direction, biasRet);
            }
        }
        else {
        	
            int increment = (top) ? -1 : 1;
            int childIndex;
            if (b == Position.Bias.Backward && pos > 0) {
                childIndex = v.getViewIndex(pos - 1, Position.Bias.Forward);
            }
            else {
                childIndex = v.getViewIndex(pos, Position.Bias.Forward);
            }
            View child = v.getView(childIndex);
            Shape childBounds = v.getChildAllocation(childIndex, alloc);
            retValue = child.getNextVisualPositionFrom(pos, b, childBounds,
                                                       direction, biasRet);
            if ((direction == SwingConstants.NORTH ||
                 direction == SwingConstants.SOUTH) &&
                (v instanceof MCompositeView) &&
                flipEastAndWestAtEnds(pos, b)) {
                increment *= -1;
            }
            childIndex += increment;
            if (retValue == -1 && childIndex >= 0 &&
                                  childIndex < v.getViewCount()) {
                child = v.getView(childIndex);
                childBounds = v.getChildAllocation(childIndex, alloc);
                retValue = child.getNextVisualPositionFrom(
                                     -1, b, childBounds, direction, biasRet);
                // If there is a bias change, it is a fake position
                // and we should skip it. This is usually the result
                // of two elements side be side flowing the same way.
                if (retValue == pos && biasRet[0] != b) {
                    return getNextVisualPositionFromVerticalL2R(v, pos, biasRet[0],
                                                     alloc, direction,
                                                     biasRet);
                }
            }
            else if (retValue != -1 && biasRet[0] != b &&
                     ((increment == 1 && child.getEndOffset() == retValue) ||
                      (increment == -1 &&
                       child.getStartOffset() == retValue)) &&
                     childIndex >= 0 && childIndex < v.getViewCount()) {
                // Reached the end of a view, make sure the next view
                // is a different direction.
                child = v.getView(childIndex);
                childBounds = v.getChildAllocation(childIndex, alloc);
                Position.Bias originalBias = biasRet[0];
                int nextPos = child.getNextVisualPositionFrom(
                                    -1, b, childBounds, direction, biasRet);
                if (biasRet[0] == b) {
                    retValue = nextPos;
                }
                else {
                    biasRet[0] = originalBias;
                }
            }
        }
        return retValue;
        
    }

    private static int getNextVisualPositionFromVerticalR2L(MView v, int pos, Position.Bias b,
            Shape alloc, int direction, Position.Bias[] biasRet)
		throws BadLocationException {
    	
        boolean top = (direction == SwingConstants.NORTH ||
                direction == SwingConstants.EAST);
 
        int retValue;
        if (pos == -1) {
            // Start from the first View.
            int childIndex = (top) ? v.getViewCount() - 1 : 0;
            View child = v.getView(childIndex);
            Shape childBounds = v.getChildAllocation(childIndex, alloc);
            retValue = child.getNextVisualPositionFrom(pos, b, childBounds,
                                                       direction, biasRet);
            if (retValue == -1 && !top && v.getViewCount() > 1) {
                // Special case that should ONLY happen if first view
                // isn't valid (can happen when end position is put at
                // beginning of line.
                child = v.getView(1);
                childBounds = v.getChildAllocation(1, alloc);
                retValue = child.getNextVisualPositionFrom(-1, biasRet[0],
                                                           childBounds,
                                                           direction, biasRet);
            }
        }
        else {
        	
            int increment = (top) ? -1 : 1;
            int childIndex;
            if (b == Position.Bias.Backward && pos > 0) {
                childIndex = v.getViewIndex(pos - 1, Position.Bias.Forward);
            }
            else {
                childIndex = v.getViewIndex(pos, Position.Bias.Forward);
            }
            View child = v.getView(childIndex);
            Shape childBounds = v.getChildAllocation(childIndex, alloc);
            retValue = child.getNextVisualPositionFrom(pos, b, childBounds,
                                                       direction, biasRet);
            if ((direction == SwingConstants.NORTH ||
                 direction == SwingConstants.SOUTH) &&
                (v instanceof MCompositeView) &&
                flipEastAndWestAtEnds(pos, b)) {
                increment *= -1;
            }
            childIndex += increment;
            if (retValue == -1 && childIndex >= 0 &&
                                  childIndex < v.getViewCount()) {
                child = v.getView(childIndex);
                childBounds = v.getChildAllocation(childIndex, alloc);
                retValue = child.getNextVisualPositionFrom(
                                     -1, b, childBounds, direction, biasRet);
                // If there is a bias change, it is a fake position
                // and we should skip it. This is usually the result
                // of two elements side be side flowing the same way.
                if (retValue == pos && biasRet[0] != b) {
                    return getNextVisualPositionFromVerticalR2L(v, pos, biasRet[0],
                                                     alloc, direction,
                                                     biasRet);
                }
            }
            else if (retValue != -1 && biasRet[0] != b &&
                     ((increment == 1 && child.getEndOffset() == retValue) ||
                      (increment == -1 &&
                       child.getStartOffset() == retValue)) &&
                     childIndex >= 0 && childIndex < v.getViewCount()) {
                // Reached the end of a view, make sure the next view
                // is a different direction.
                child = v.getView(childIndex);
                childBounds = v.getChildAllocation(childIndex, alloc);
                Position.Bias originalBias = biasRet[0];
                int nextPos = child.getNextVisualPositionFrom(
                                    -1, b, childBounds, direction, biasRet);
                if (biasRet[0] == b) {
                    retValue = nextPos;
                }
                else {
                    biasRet[0] = originalBias;
                }
            }
        }
        return retValue;
        
    }
    
    protected static boolean flipEastAndWestAtEnds(int position,
            Position.Bias bias) {
    	return false;
    }
    
	// 右ぶら下げ　　文字(特殊インデント１)
	// String sepStr		= "、。，";
	private static	final	String		sepStr		= "\u3001\u3002\uFF0C";
	
	// 右ぶら下げ　　文字(特殊インデント２)
	// String smallStr 	= "ぁぃぅぇぉっゃゅょァィゥェォヵヶッャュョ";
	private static	final	String		smallStr
			= "\u3041\u3043\u3045\u3047\u3049\u3063\u3083\u3085\u3087\u30A1\u30A3"
					+"\u30A5\u30A7\u30A9\u30F5\u30F6\u30C3\u30E3\u30E5\u30E7";
	// 下ぶら下げ　　文字
	// String dangleStr		= "々ヽヾゝゞ！？";
	private static	final	String		dangleStr
			= "\u3005\u30FD\u30FE\u309D\u309E\uFF01\uFF1F";
	// 回転　　　　　　文字
	// String rotateStr0		= "＝≠≦≧∈∋⊆⊇⊂⊃∪∩←↑→↓";
	private static	final	String		rotateStr0
			= "\uFF1D\u2260\u2266\u2267\u2208\u220B\u2286\u2287\u2282\u2283\u222A"
					+	"\u2229\u2190\u2191\u2192\u2193";
	// 回転ぶら下げ　　文字
	// String rotateStr1	= "＞≫」ー－』｝）》〗｠〙〛»›】〕］〉⁆⁾₎｜―～…‥：";
	private static	final	String		rotateStr1
			= "\uFF1E\u226B\u300D\u30FC\uFF0D\u300F\uFF5D\uFF09\u300B\u3017\uFF60\u3019\u301B"
					+	"\u00BB\u203A\u3011\u3015\uFF3D\u3009\u2046\u207E\u208E\uFF5C\u2015\u301C\uFF5E\u2026\u2025\uFF1A";
	// 回転回り込み　　文字
	// String rotateStr2 = "＜≪「『｛（《〖｟〘〚«‹【〔［〈⁅⁽₍";
	private static	final	String		rotateStr2
			= "\uFF1C\u226A\u300C\u300E\uFF5B\uFF08\u300A\u3016\uFF5F\u3018\u301A\u00AB\u2039\u3010\u3014\uFF3B\u3008\u2045\u207D\u208D";
	// 回転　　　　半角文字
	// String rotateStr3 = "1234567890^\\qwertyuiop@asdfghjklzxcvbnm #$%|QWERTYUIOPASDFGHJKLZXCVBNM_ﾇﾌｱｳｴｵﾔﾕﾖﾜﾎﾍﾀﾃｲｽｶﾝﾅﾆﾗｾﾁﾄｼﾊｷｸﾏﾉﾘﾚｹﾑﾂｻｿﾋｺﾐﾓﾈﾙﾒﾛｦ";
	private static	final	String		rotateStr3
			= "1234567890^\\qwertyuiop@asdfghjklzxcvbnm #$%|QWERTYUIOPASDFGHJKLZXC"
					+	"VBNM_\uFF87\uFF8C\uFF71\uFF73\uFF74\uFF75\uFF94\uFF95\uFF96\uFF9C"
					+	"\uFF8E\uFF8D\uFF80\uFF83\uFF72\uFF7D\uFF76\uFF9D\uFF85\uFF86\uFF97"
					+	"\uFF7E\uFF81\uFF84\uFF7C\uFF8A\uFF77\uFF78\uFF8F\uFF89\uFF98\uFF9A"
					+	"\uFF79\uFF91\uFF82\uFF7B\uFF7F\uFF8B\uFF7A\uFF90\uFF93\uFF88\uFF99"
					+	"\uFF92\uFF9B\uFF66";
	// 回転ぶら下げ半角文字
	// String rotateStr4 = "-;:],./)!\"&=+*}>?ﾞﾟｩｪｫｬｭｮ｣ｨｯ｡､･";
	private static	final	String		rotateStr4
			= "-;:],./)!\"&=+*}>?\uFF9E\uFF9F\uFF69\uFF6A\uFF6B\uFF6C\uFF6D\uFF6E"
					+	"\uFF63\uFF68\uFF6F\uFF61\uFF64\uFF65";
	// 回転回り込み半角文字
	// String rotateStr5 = "(['`~{<｢";// 改行時に回り込ませる半角文字
	private static	final	String		rotateStr5	= "(['`~{<\uFF62";

    public static boolean canDisplay(Font font,  char ch ){

    	if( font.canDisplay(ch) ) {

    		Character.UnicodeBlock block = Character.UnicodeBlock.of(ch);
    		
    		if( Character.isIdeographic(ch) )
    			return false;

    		if( block == Character.UnicodeBlock.KATAKANA )
    			return false;
    		
    		if( block == Character.UnicodeBlock.HIRAGANA )
    			return false;
    		
    		if( block == Character.UnicodeBlock.CJK_COMPATIBILITY )
    			return false;
    		
    		if( block == Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS )
    			return false;
    		
    		if( block == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS )
    			return false;
    		
    		if( block == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT )
    			return false;
    		
    		if( block == Character.UnicodeBlock.CJK_RADICALS_SUPPLEMENT )
    			return false;
    		
    		if( block == Character.UnicodeBlock.CJK_STROKES )
    			return false;
    		
    		if( block == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS )
    			return false;
    		
    		if( block == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A )
    			return false;
    		
    		if( block == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B )
    			return false;
    		
    		if( block == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_C )
    			return false;
    		
    		if( block == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_D )
    			return false;
    		
    		if( block == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_E )
    			return false;
    		
    		if( block == Character.UnicodeBlock.ENCLOSED_CJK_LETTERS_AND_MONTHS )
    			return false;
    		
	    	if( sepStr.indexOf(ch) >= 0 )
	    		return false;
	    	
	    	if( smallStr.indexOf(ch) >= 0 )
	    		return false;
	    	
	    	if( dangleStr.indexOf(ch) >= 0 )
	    		return false;
	    	
	    	if( rotateStr0.indexOf(ch) >= 0 )
	    		return false;
	    	
	    	if( rotateStr1.indexOf(ch) >= 0 )
	    		return false;
	    	
	    	if( rotateStr2.indexOf(ch) >= 0 )
	    		return false;
	    	
	    	return true;
	    	
    	} else {
	    	
	    	return false;
    		
    	}
    }
    
	
	
    public static boolean isRotated( char ch ){
    	
    	if( rotateStr0.indexOf(ch) >= 0 )
    		return false;
    	
    	if( rotateStr1.indexOf(ch) >= 0 )
    		return false;
    	
    	if( rotateStr2.indexOf(ch) >= 0 )
    		return false;
    	
    	if( rotateStr3.indexOf(ch) >= 0 )
    		return false;
    	
    	if( rotateStr4.indexOf(ch) >= 0 )
    		return false;
    	
    	if( rotateStr5.indexOf(ch) >= 0 )
    		return false;
    	
    	return true;
    }
    
    public static int getRightIntend( char ch ){
    	
    	if( sepStr.indexOf(ch) >= 0 )
    		return 4;
    	
    	if( smallStr.indexOf(ch) >= 0 )
    		return 1;
    	    	
    	return 0;
    }
    
    public static int getDownIntend( char ch ){
    	
    	if( dangleStr.indexOf(ch) >= 0 )
    		return 1;
    	
    	if( sepStr.indexOf(ch) >= 0 )
    		return -3;
    	
    	return 0;
    }
    
    public static boolean isMongolian( char ch ){
    	
    	if( ch >= MCodeConstant.UNI0020_SPACE && ch <= MCodeConstant.UNI007E_TILDE )
    		return true;
    		
    	if( ch >= MCodeConstant.UNI1800_BIRGA && ch <= MCodeConstant.UNI18AA_AU )
    		return true;
    	
    	if( ch == MCodeConstant.UNI200C_ZWNJ || ch <= MCodeConstant.UNI200D_ZWJ 
    			|| ch <= MCodeConstant.UNI202F_NOBREAK )
    		return true;
    		
    	return false;
    }
    
    public static boolean isModernMongolian( char ch ){
    	
    	if( ch >= MCodeConstant.UNI0020_SPACE && ch <= MCodeConstant.UNI007E_TILDE )
    		return true;
    		
    	if( ch >= MCodeConstant.UNI1820_A && ch <= MCodeConstant.UNI1842_CHI )
    		return true;
    	
    	if( ch == MCodeConstant.UNI200C_ZWNJ || ch <= MCodeConstant.UNI200D_ZWJ 
    			|| ch <= MCodeConstant.UNI202F_NOBREAK )
    		return true;
    		
    	if( ch == MCodeConstant.UNI180E_MVS || ch <= MCodeConstant.UNI180B_FVS1 
    			|| ch <= MCodeConstant.UNI180C_FVS2 || ch <= MCodeConstant.UNI180D_FVS3 
    			|| ch <= MCodeConstant.UNI180A_NIRUGU )
    		return true;
    		
    	return false;
    }
    
    public static Shape getRotateVertical( Shape shape ){
    	
	    Rectangle rectBounds = (shape instanceof Rectangle) ? (Rectangle)shape : shape.getBounds();
	    int tmp = rectBounds.x;
	    rectBounds.x = rectBounds.y;
	    rectBounds.y = tmp;
	    tmp = rectBounds.width;
	    rectBounds.width = rectBounds.height;
	    rectBounds.height = tmp;
	    
	    return rectBounds;
    }
}
