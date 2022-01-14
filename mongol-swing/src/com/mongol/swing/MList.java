package com.mongol.swing;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.JViewport;
import javax.swing.ListModel;
import javax.swing.SwingConstants;
import javax.swing.plaf.LabelUI;
import javax.swing.plaf.ListUI;

import com.mongol.swing.plaf.MButtonUI;
import com.mongol.swing.plaf.MListUI;
import com.mongol.swing.plaf.MRotation;

/**
 * Mongolian Encoding and vertical line supported Swing List Component,
 * or both.
 * @author jrmt@Almas
 */
public class MList extends JList implements MRotation{

	protected MDefaultListCellRenderer cellRenderer;
	private MListUI listUI;
    /**
     * @see #getUIClassID
     * @see #readObject
     */
	public MList() {
		super();
		
        cellRenderer = new MDefaultListCellRenderer();
        super.setCellRenderer(cellRenderer);
        if( listUI == null )
        	listUI = new MListUI();
        super.setUI(listUI);
	}
	
    public MList(ListModel dataModel) {
    	super(dataModel);
    	
        cellRenderer = new MDefaultListCellRenderer();
        super.setCellRenderer(cellRenderer);
        if( listUI == null )
        	listUI = new MListUI();
        super.setUI(listUI);
        
    }
    
    public MList(final Object[] listData) {
    	super(listData);
    	
        cellRenderer = new MDefaultListCellRenderer();
        super.setCellRenderer(cellRenderer);
        if( listUI == null )
        	listUI = new MListUI();
        super.setUI(listUI);
    }    
    
    public MList(final Vector<?> listData) {
    	super(listData);
    	
        cellRenderer = new MDefaultListCellRenderer();
        super.setCellRenderer(cellRenderer);
        if( listUI == null )
        	listUI = new MListUI();
        super.setUI(listUI);
    }

    public void setUI(ListUI ui) {
        super.setUI(ui);
		if( listUI== null )
			listUI = new MListUI();
		if( !(ui instanceof MListUI) )
			super.setUI(listUI);
    }
    
    public void setSelectionInterval(int anchor, int lead) {
		super.setSelectionInterval(anchor, lead);
        repaint();
    }
    
    public void addSelectionInterval(int anchor, int lead) {
		super.addSelectionInterval(anchor, lead);
        repaint();
    }
    public void clearSelection() {
		super.clearSelection();
        repaint();
    }
    public void removeSelectionInterval(int index0, int index1) {
		super.removeSelectionInterval(index0, index1);
        repaint();
    }
    public void setSelectedIndex(int index) {
		super.setSelectedIndex(index);
        repaint();
    }
    public void setSelectedIndices(int[] indices) {
		super.setSelectedIndices(indices);
		repaint();
    }

    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();
        if( getRotateDirection() != MSwingRotateUtilities.ROTATE_HORIZANTAL ) {
	        int tmp = size.width;
	        size.width = size.height;
	        size.height = tmp;
        }
        return size;
    }

    public Dimension getSize() {
        Dimension size = super.getSize();
        return size;
    }
    
    public Dimension getMaximumSize() {
        Dimension size = super.getMaximumSize();
        return size;
    }

    public void setMinimumSize(Dimension minimumSize) {
        super.setMinimumSize(minimumSize);
    }


    public Dimension getMinimumSize() {
        Dimension size = super.getMinimumSize();
        return size;
    }

    public void setMaximumSize(Dimension maximumSize) {
        super.setMaximumSize(maximumSize);
    }

    

    public void scrollRectToVisible(Rectangle aRect) {
        	super.scrollRectToVisible(aRect);
    }

    
    public Dimension getPreferredScrollableViewportSize()
    {
        if (getLayoutOrientation() != VERTICAL) {
            return getPreferredSize();
        }
        
        if( getRotateDirection() == MSwingRotateUtilities.ROTATE_HORIZANTAL ) {
	        Insets insets = getInsets();
	        int dx = insets.left + insets.right;
	        int dy = insets.top + insets.bottom;
	
	        int visibleRowCount = getVisibleRowCount();
	        int fixedCellWidth = getFixedCellWidth();
	        int fixedCellHeight = getFixedCellHeight();
	
	        if ((fixedCellWidth > 0) && (fixedCellHeight > 0)) {
	            int width = fixedCellWidth + dx;
	            int height = (visibleRowCount * fixedCellHeight) + dy;
	            return new Dimension(width, height);
	        }
	        else if (getModel().getSize() > 0) {
	            int width = getPreferredSize().width;
	            int height;
	            Rectangle r = getCellBounds(0, 0);
	            if (r != null) {
	                height = (visibleRowCount * r.height) + dy;
	            }
	            else {
	                // Will only happen if UI null, shouldn't matter what we return
	                height = 1;
	            }
	            return new Dimension(width, height);
	        }
	        else {
	            fixedCellWidth = (fixedCellWidth > 0) ? fixedCellWidth : 256;
	            fixedCellHeight = (fixedCellHeight > 0) ? fixedCellHeight : 16;
	            return new Dimension(fixedCellWidth, fixedCellHeight * visibleRowCount);
	        }
	        
        } else {
        	
	        Insets insets = getInsets();
	        int dx = insets.left + insets.right;
	        int dy = insets.top + insets.bottom;
	
	        int visibleRowCount = getVisibleRowCount();
	        int fixedCellWidth = getFixedCellWidth();
	        int fixedCellHeight = getFixedCellHeight();
	
	        if ((fixedCellWidth > 0) && (fixedCellHeight > 0)) {
	            int height = fixedCellWidth + dx;
	            int width = (visibleRowCount * fixedCellHeight) + dy;
	            return new Dimension(width, height);
	        }
	        else if (getModel().getSize() > 0) {
	            int height = getPreferredSize().height + 32 ;
	            int width;
	            Rectangle r = getCellBounds(0, 0);
	            if (r != null) {
	                width = (visibleRowCount * r.width) + dy;
	            }
	            else {
	                // Will only happen if UI null, shouldn't matter what we return
	                width = 1;
	            }
	            return new Dimension(width, height);
	        }
	        else {
	            fixedCellWidth = (fixedCellWidth > 0) ? fixedCellWidth : 256;
	            fixedCellHeight = (fixedCellHeight > 0) ? fixedCellHeight : 16;
	            return new Dimension(fixedCellHeight * visibleRowCount, fixedCellWidth );
	        }
        }
    }

    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction)
    {
        checkScrollableParameters(visibleRect, orientation);

        int increment = 0;
        if( getRotateDirection() == MSwingRotateUtilities.ROTATE_HORIZANTAL ) {
        	
        	increment =  getScrollableUnitIncrementHorizantal(visibleRect, orientation, direction);
        	
        } else if( getRotateHint() == MSwingRotateUtilities.ROTATE_LEFTTORIGHT ) {
        	
        	increment = getScrollableUnitIncrementL2RVertical(visibleRect, orientation, direction);
	        
        } else {		
        	
        	increment = getScrollableUnitIncrementL2RVertical(visibleRect, orientation, direction);
	        
        }
        
        return increment;
        
    }
    
    public int getScrollableUnitIncrementHorizantal(Rectangle visibleRect, int orientation, int direction)
    {
        if (orientation == SwingConstants.VERTICAL) {
            int row = locationToIndex(visibleRect.getLocation());

            if (row == -1) {
                return 0;
            }
            else {
                /* Scroll Down */
                if (direction > 0) {
                    Rectangle r = getCellBounds(row, row);
                    return (r == null) ? 0 : r.height - (visibleRect.y - r.y);
                }
                /* Scroll Up */
                else {
                    Rectangle r = getCellBounds(row, row);

                    /* The first row is completely visible and it's row 0.
                     * We're done.
                     */
                    if ((r.y == visibleRect.y) && (row == 0))  {
                        return 0;
                    }
                    /* The first row is completely visible, return the
                     * height of the previous row or 0 if the first row
                     * is the top row of the list.
                     */
                    else if (r.y == visibleRect.y) {
                        Point loc = r.getLocation();
                        loc.y--;
                        int prevIndex = locationToIndex(loc);
                        Rectangle prevR = getCellBounds(prevIndex, prevIndex);

                        if (prevR == null || prevR.y >= r.y) {
                            return 0;
                        }
                        return prevR.height;
                    }
                    /* The first row is partially visible, return the
                     * height of hidden part.
                     */
                    else {
                        return visibleRect.y - r.y;
                    }
                }
            }
            
        } else if (orientation == SwingConstants.HORIZONTAL &&
                           getLayoutOrientation() != JList.VERTICAL) {
            boolean leftToRight = getComponentOrientation().isLeftToRight();
            int index;
            Point leadingPoint;
            
            if (leftToRight) {
                leadingPoint = visibleRect.getLocation();
            }
            else {
                leadingPoint = new Point(visibleRect.x + visibleRect.width -1,
                                         visibleRect.y);
            }
            index = locationToIndex(leadingPoint);

            if (index != -1) {
                Rectangle cellBounds = getCellBounds(index, index);
                if (cellBounds != null && cellBounds.contains(leadingPoint)) {
                    int leadingVisibleEdge;
                    int leadingCellEdge;

                    if (leftToRight) {
                        leadingVisibleEdge = visibleRect.x;
                        leadingCellEdge = cellBounds.x;
                    }
                    else {
                        leadingVisibleEdge = visibleRect.x + visibleRect.width;
                        leadingCellEdge = cellBounds.x + cellBounds.width;
                    }

                    if (leadingCellEdge != leadingVisibleEdge) {
                        if (direction < 0) {
                            // Show remainder of leading cell
                            return Math.abs(leadingVisibleEdge - leadingCellEdge);

                        }
                        else if (leftToRight) {
                            // Hide rest of leading cell
                            return leadingCellEdge + cellBounds.width - leadingVisibleEdge;
                        }
                        else {
                            // Hide rest of leading cell
                            return leadingVisibleEdge - cellBounds.x;
                        }
                    }
                    // ASSUME: All cells are the same width
                    return cellBounds.width;
                }
            }
        }
        Font f = getFont();
        return (f != null) ? f.getSize() : 1;
        
    }

    public int getScrollableUnitIncrementL2RVertical(Rectangle visibleRect, int orientation, int direction)
    {
        if (orientation == SwingConstants.HORIZONTAL) {
            int row = locationToIndex(visibleRect.getLocation());

            if (row == -1) {
                return 0;
            }
            else {
                /* Scroll Down */
                if (direction > 0) {
                    Rectangle r = getCellBounds(row, row);
                    return (r == null) ? 0 : r.width - (visibleRect.x - r.x);
                }
                /* Scroll Up */
                else {
                    Rectangle r = getCellBounds(row, row);

                    /* The first row is completely visible and it's row 0.
                     * We're done.
                     */
                    if ((r.x == visibleRect.x) && (row == 0))  {
                        return 0;
                    }
                    /* The first row is completely visible, return the
                     * height of the previous row or 0 if the first row
                     * is the top row of the list.
                     */
                    else if (r.x == visibleRect.x) {
                        Point loc = r.getLocation();
                        loc.x--;
                        int prevIndex = locationToIndex(loc);
                        Rectangle prevR = getCellBounds(prevIndex, prevIndex);

                        if (prevR == null || prevR.x >= r.x) {
                            return 0;
                        }
                        return prevR.width;
                    }
                    /* The first row is partially visible, return the
                     * height of hidden part.
                     */
                    else {
                        return visibleRect.x - r.x;
                    }
                }
            }
            
        } else if (orientation == SwingConstants.VERTICAL &&
                           getLayoutOrientation() != JList.VERTICAL) {
            boolean leftToRight = getComponentOrientation().isLeftToRight();
            int index;
            Point leadingPoint;
            
            if (leftToRight) {
                leadingPoint = visibleRect.getLocation();
            }
            else {
                leadingPoint = new Point(visibleRect.x + visibleRect.width -1,
                                         visibleRect.y);
            }
            index = locationToIndex(leadingPoint);

            if (index != -1) {
                Rectangle cellBounds = getCellBounds(index, index);
                if (cellBounds != null && cellBounds.contains(leadingPoint)) {
                    int leadingVisibleEdge;
                    int leadingCellEdge;

                    if (leftToRight) {
                        leadingVisibleEdge = visibleRect.x;
                        leadingCellEdge = cellBounds.x;
                    }
                    else {
                        leadingVisibleEdge = visibleRect.x + visibleRect.width;
                        leadingCellEdge = cellBounds.x + cellBounds.width;
                    }

                    if (leadingCellEdge != leadingVisibleEdge) {
                        if (direction < 0) {
                            // Show remainder of leading cell
                            return Math.abs(leadingVisibleEdge - leadingCellEdge);

                        }
                        else if (leftToRight) {
                            // Hide rest of leading cell
                            return leadingCellEdge + cellBounds.width - leadingVisibleEdge;
                        }
                        else {
                            // Hide rest of leading cell
                            return leadingVisibleEdge - cellBounds.x;
                        }
                    }
                    // ASSUME: All cells are the same width
                    return cellBounds.width;
                }
            }
        }
        Font f = getFont();
        return (f != null) ? f.getSize() : 1;
        
    }

    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction)
    {
        checkScrollableParameters(visibleRect, orientation);

        int increment = 0;
        if( getRotateDirection() == MSwingRotateUtilities.ROTATE_HORIZANTAL ) {
        	
        	increment =  getScrollableBlockIncrementHorizantal(visibleRect, orientation, direction);
        	
        } else if( getRotateHint() == MSwingRotateUtilities.ROTATE_LEFTTORIGHT ) {
        	
        	increment = getScrollableBlockIncrementL2RVertical(visibleRect, orientation, direction);
	        
        } else {		
        	
        	increment = getScrollableBlockIncrementL2RVertical(visibleRect, orientation, direction);
	        
        }
        
        return increment;
        
    }
    
    public int getScrollableBlockIncrementHorizantal(Rectangle visibleRect, int orientation, int direction) {
    	
    	checkScrollableParameters(visibleRect, orientation);
        if (orientation == SwingConstants.VERTICAL) {
            int inc = visibleRect.height;
            /* Scroll Down */
            if (direction > 0) {
                // last cell is the lowest left cell
                int last = locationToIndex(new Point(visibleRect.x, visibleRect.y+visibleRect.height-1));
                if (last != -1) {
				    Rectangle lastRect = getCellBounds(last,last);
				    if (lastRect != null) {
						inc = lastRect.y - visibleRect.y;
						if ( (inc == 0) && (last < getModel().getSize()-1) ) {
						    inc = lastRect.height;
						}
				    }
                }
            }
            /* Scroll Up */
            else {
                int newFirst = locationToIndex(new Point(visibleRect.x, visibleRect.y-visibleRect.height));
                int first = getFirstVisibleIndex();
                if (newFirst != -1) {
				    if (first == -1) {
				    	first = locationToIndex(visibleRect.getLocation());
				    }
                    Rectangle newFirstRect = getCellBounds(newFirst,newFirst);
                    Rectangle firstRect = getCellBounds(first,first);
				    if ((newFirstRect != null) && (firstRect!=null)) {
						while ( (newFirstRect.y + visibleRect.height <
							 firstRect.y + firstRect.height) &&
							(newFirstRect.y < firstRect.y) ) {
						    newFirst++;
						    newFirstRect = getCellBounds(newFirst,newFirst);
						}
						inc = visibleRect.y - newFirstRect.y;
						if ( (inc <= 0) && (newFirstRect.y > 0)) {
						    newFirst--;
						    newFirstRect = getCellBounds(newFirst,newFirst);
						    if (newFirstRect != null) {
								inc = visibleRect.y - newFirstRect.y;
						    }
						}
				    }    
                }
            }
            return inc;
        }
        else if (orientation == SwingConstants.HORIZONTAL &&
		 getLayoutOrientation() != JList.VERTICAL) {
            boolean leftToRight = getComponentOrientation().isLeftToRight();
            int inc = visibleRect.width;
		    /* Scroll Right (in ltr mode) or Scroll Left (in rtl mode) */
		    if (direction > 0) {
	            // position is upper right if ltr, or upper left otherwise
	            int x = visibleRect.x + (leftToRight ? (visibleRect.width - 1) : 0);
	            int last = locationToIndex(new Point(x, visibleRect.y));
	
				if (last != -1) {
				    Rectangle lastRect = getCellBounds(last,last);
				    if (lastRect != null) {
		                if (leftToRight) {
		                    inc = lastRect.x - visibleRect.x;
		                } else {
		                    inc = visibleRect.x + visibleRect.width
		                              - (lastRect.x + lastRect.width);
		                }
						if (inc < 0) {
						    inc += lastRect.width;
						} else if ( (inc == 0) && (last < getModel().getSize()-1) ) {
						    inc = lastRect.width;
						}
				    }
				}
		    }
		    /* Scroll Left (in ltr mode) or Scroll Right (in rtl mode) */
		    else {
	            // position is upper left corner of the visibleRect shifted
	            // left by the visibleRect.width if ltr, or upper right shifted
	            // right by the visibleRect.width otherwise
	            int x = visibleRect.x + (leftToRight
	                                     ? -visibleRect.width
	                                     : visibleRect.width - 1 + visibleRect.width);
	            int first = locationToIndex(new Point(x, visibleRect.y));
	
				if (first != -1) {
				    Rectangle firstRect = getCellBounds(first,first);
				    if (firstRect != null) {
                        // the right of the first cell
                        int firstRight = firstRect.x + firstRect.width;

                        if (leftToRight) {
                            if ((firstRect.x < visibleRect.x - visibleRect.width)
                                    && (firstRight < visibleRect.x)) {
                                inc = visibleRect.x - firstRight;
                            } else {
                                inc = visibleRect.x - firstRect.x;
                            }
                        } else {
                            int visibleRight = visibleRect.x + visibleRect.width;

                            if ((firstRight > visibleRight + visibleRect.width)
                                    && (firstRect.x > visibleRight)) {
                                inc = firstRect.x - visibleRight;
                            } else {
                                inc = firstRight - visibleRight;
                            }
                        }
				    }
				}
		    }
		    return inc;
        }
        return visibleRect.width;
    }

    public int getScrollableBlockIncrementL2RVertical(Rectangle visibleRect, int orientation, int direction) {
    	
    	checkScrollableParameters(visibleRect, orientation);
        if (orientation == SwingConstants.HORIZONTAL) {
            int inc = visibleRect.width;
            /* Scroll right */
            if (direction > 0) {
                // last cell is the lowest left cell
                int last = locationToIndex(new Point( visibleRect.x+visibleRect.width-1, visibleRect.y));
                if (last != -1) {
				    Rectangle lastRect = getCellBounds(last,last);
				    if (lastRect != null) {
						inc = lastRect.x - visibleRect.x;
						if ( (inc == 0) && (last < getModel().getSize()-1) ) {
						    inc = lastRect.width;
						}
				    }
                }
            }
            /* Scroll left */
            else {
                int newFirst = locationToIndex(new Point(visibleRect.x-visibleRect.width, visibleRect.y ));
                int first = getFirstVisibleIndex();
                if (newFirst != -1) {
				    if (first == -1) {
				    	first = locationToIndex(visibleRect.getLocation());
				    }
                    Rectangle newFirstRect = getCellBounds(newFirst,newFirst);
                    Rectangle firstRect = getCellBounds(first,first);
				    if ((newFirstRect != null) && (firstRect!=null)) {
						while ( (newFirstRect.x + visibleRect.width <
							 firstRect.x + firstRect.width) &&
							(newFirstRect.x < firstRect.x) ) {
						    newFirst++;
						    newFirstRect = getCellBounds(newFirst,newFirst);
						}
						inc = visibleRect.x - newFirstRect.x;
						if ( (inc <= 0) && (newFirstRect.x > 0)) {
						    newFirst--;
						    newFirstRect = getCellBounds(newFirst,newFirst);
						    if (newFirstRect != null) {
								inc = visibleRect.x - newFirstRect.x;
						    }
						}
				    }    
                }
            }
            return inc;
        }
        else if (orientation == SwingConstants.VERTICAL && getLayoutOrientation() != JList.VERTICAL) {
            boolean leftToRight = getComponentOrientation().isLeftToRight();
            int inc = visibleRect.height;
		    /* Scroll down (in ltr mode) or Scroll Left (in rtl mode) */
		    if (direction > 0) {
	            // position is upper right if ltr, or upper left otherwise
	            int y = visibleRect.y + (leftToRight ? (visibleRect.height - 1) : 0);
	            int last = locationToIndex(new Point(visibleRect.x, y));
	
				if (last != -1) {
				    Rectangle lastRect = getCellBounds(last,last);
				    if (lastRect != null) {
		                if (leftToRight) {
		                    inc = lastRect.y - visibleRect.y;
		                } else {
		                    inc = visibleRect.y + visibleRect.height
		                              - (lastRect.y + lastRect.height);
		                }
						if (inc < 0) {
						    inc += lastRect.height;
						} else if ( (inc == 0) && (last < getModel().getSize()-1) ) {
						    inc = lastRect.height;
						}
				    }
				}
		    }
		    /* Scroll up (in ltr mode) or Scroll Right (in rtl mode) */
		    else {
	            // position is upper left corner of the visibleRect shifted
	            // left by the visibleRect.width if ltr, or upper right shifted
	            // right by the visibleRect.width otherwise
	            int y = visibleRect.y + (leftToRight
	                                     ? -visibleRect.height
	                                     : visibleRect.height - 1 + visibleRect.height);
	            int first = locationToIndex(new Point(visibleRect.x, y));
	
				if (first != -1) {
				    Rectangle firstRect = getCellBounds(first,first);
				    if (firstRect != null) {
                        // the right of the first cell
                        int firstRight = firstRect.y + firstRect.height;

                        if (leftToRight) {
                            if ((firstRect.y < visibleRect.y - visibleRect.height)
                                    && (firstRight < visibleRect.y)) {
                                inc = visibleRect.y - firstRight;
                            } else {
                                inc = visibleRect.y - firstRect.y;
                            }
                        } else {
                            int visibleRight = visibleRect.y + visibleRect.height;

                            if ((firstRight > visibleRight + visibleRect.height)
                                    && (firstRect.y > visibleRight)) {
                                inc = firstRect.y - visibleRight;
                            } else {
                                inc = firstRight - visibleRight;
                            }
                        }
				    }
				}
		    }
		    return inc;
        }
        return visibleRect.height;
    }
    

    /**
     * --- The Scrollable Implementation ---
     */

    private void checkScrollableParameters(Rectangle visibleRect, int orientation) {
	if (visibleRect == null) {
	    throw new IllegalArgumentException("visibleRect must be non-null");
	}
        switch (orientation) {
        case SwingConstants.VERTICAL:
        case SwingConstants.HORIZONTAL:
            break;
        default:
            throw new IllegalArgumentException("orientation must be one of: VERTICAL, HORIZONTAL");
        }
    }


    public boolean getScrollableTracksViewportWidth() {
    	
        if (getLayoutOrientation() == HORIZONTAL_WRAP && getVisibleRowCount() <= 0) {
            return true;
        }
		if (getParent() instanceof JViewport) {
		    return (((JViewport)getParent()).getWidth() > getPreferredSize().width);
		}
		
		return false;
    }

    public boolean getScrollableTracksViewportHeight() {
    	
        if (getLayoutOrientation() == VERTICAL_WRAP &&
                getVisibleRowCount() <= 0) {
       return true;
	   }
		if (getParent() instanceof JViewport) {
		    return (((JViewport)getParent()).getHeight() > getPreferredSize().height);
		}
		return false;
    	
    }
    
    public void setRotateHint(int hint){
    	ListUI listUI = getUI();
        if( listUI instanceof MListUI ){
        	((MListUI)listUI).setRotateHint(hint);
        }
        if( cellRenderer != null )
        	cellRenderer.setRotateHint(hint);
    }

    public int getRotateHint(){
    	ListUI listUI = getUI();
        if( listUI instanceof MListUI ){
        	return ((MListUI)listUI).getRotateHint();
        }
        return MSwingRotateUtilities.ROTATE_DEFAULT;
    }

    public void setRotateDirection(int direction){
    	ListUI listUI = getUI();
        if( listUI instanceof MListUI ){
        	((MListUI)listUI).setRotateDirection(direction);
        }
        if( cellRenderer != null )
        	cellRenderer.setRotateDirection(direction);
    }

    public int getRotateDirection(){
    	ListUI listUI = getUI();
        if( listUI instanceof MListUI ){
        	return ((MListUI)listUI).getRotateDirection();
        }
        return MSwingRotateUtilities.ROTATE_HORIZANTAL;
    }

}
