package com.mongol.swing.text;

import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;

import javax.swing.BoundedRangeModel;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.Position;
import javax.swing.text.Segment;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

import com.mongol.swing.MSwingRotateUtilities;
import com.mongol.swing.MSwingUtilities;

public class MFieldView extends MPlainView {

    /**
     * Constructs a new FieldView wrapped on an element.
     *
     * @param elem the element
     */
    public MFieldView(Element elem) {
        super(elem);
    }

    /**
     * Adjusts the allocation given to the view
     * to be a suitable allocation for a text field.
     * If the view has been allocated more than the
     * preferred span vertically, the allocation is
     * changed to be centered vertically.  Horizontally
     * the view is adjusted according to the horizontal
     * alignment property set on the associated JTextField
     * (if that is the type of the hosting component).
     *
     * @param a the allocation given to the view, which may need
     *  to be adjusted.
     * @return the allocation that the superclass should use.
     */
    protected Shape adjustAllocation(Shape a) {
    	
    	if( this.getRotateDirection() == MSwingRotateUtilities.ROTATE_HORIZANTAL ){
    		return adjustAllocationHorzantal(a);
    	} else {
    		return adjustAllocationVertical(a);
    	}
    }
    /**
     * Adjusts the allocation given to the view
     * to be a suitable allocation for a text field.
     * If the view has been allocated more than the
     * preferred span vertically, the allocation is
     * changed to be centered vertically.  Horizontally
     * the view is adjusted according to the horizontal
     * alignment property set on the associated JTextField
     * (if that is the type of the hosting component).
     *
     * @param a the allocation given to the view, which may need
     *  to be adjusted.
     * @return the allocation that the superclass should use.
     */
    protected Shape adjustAllocationHorzantal(Shape a) {
        if (a != null) {
            Rectangle bounds = a.getBounds();
            int vspan = (int) getPreferredSpan(Y_AXIS);
            int hspan = (int) getPreferredSpan(X_AXIS);
            if (bounds.height != vspan) {
                int slop = bounds.height - vspan;
                bounds.y += slop / 2;
                bounds.height -= slop;
            }

            // horizontal adjustments
            Component c = getContainer();
            if (c instanceof JTextField) {
                JTextField field = (JTextField) c;
                BoundedRangeModel vis = field.getHorizontalVisibility();
                int max = Math.max(hspan, bounds.width);
                int value = vis.getValue();
                int extent = Math.min(max, bounds.width - 1);
                if ((value + extent) > max) {
                    value = max - extent;
                }
                vis.setRangeProperties(value, extent, vis.getMinimum(),
                                       max, false);
                if (hspan < bounds.width) {
                    // horizontally align the interior
                    int slop = bounds.width - 1 - hspan;

                    int align = ((JTextField)c).getHorizontalAlignment();
                    if(MUtilities.isLeftToRight(c)) {
                        if(align==LEADING) {
                            align = LEFT;
                        }
                        else if(align==TRAILING) {
                            align = RIGHT;
                        }
                    }
                    else {
                        if(align==LEADING) {
                            align = RIGHT;
                        }
                        else if(align==TRAILING) {
                            align = LEFT;
                        }
                    }

                    switch (align) {
                    case SwingConstants.CENTER:
                        bounds.x += slop / 2;
                        bounds.width -= slop;
                        break;
                    case SwingConstants.RIGHT:
                        bounds.x += slop;
                        bounds.width -= slop;
                        break;
                    }
                } else {
                    // adjust the allocation to match the bounded range.
                    bounds.width = hspan;
                    bounds.x -= vis.getValue();
                }
            }
            return bounds;
        }
        return null;
    }
    /**
     * Adjusts the allocation given to the view
     * to be a suitable allocation for a text field.
     * If the view has been allocated more than the
     * preferred span vertically, the allocation is
     * changed to be centered vertically.  Horizontally
     * the view is adjusted according to the horizontal
     * alignment property set on the associated JTextField
     * (if that is the type of the hosting component).
     *
     * @param a the allocation given to the view, which may need
     *  to be adjusted.
     * @return the allocation that the superclass should use.
     */
    protected Shape adjustAllocationVertical(Shape a) {
        if (a != null) {
            Rectangle bounds = a.getBounds();
            int vspan = (int) getPreferredSpan(X_AXIS);
            int hspan = (int) getPreferredSpan(Y_AXIS);
            if (bounds.width != hspan) {
                int slop = bounds.width - hspan;
                bounds.x += slop / 2;
                bounds.width -= slop;
            }

            // horizontal adjustments
            Component c = getContainer();
            if (c instanceof JTextField) {
                JTextField field = (JTextField) c;
                BoundedRangeModel vis = field.getHorizontalVisibility();
                int max = Math.max(vspan, bounds.height);
                int value = vis.getValue();
                int extent = Math.min(max, bounds.height - 1);
                if ((value + extent) > max) {
                    value = max - extent;
                }
                vis.setRangeProperties(value, extent, vis.getMinimum(),
                                       max, false);
                if (vspan < bounds.height) {
                    // horizontally align the interior
                    int slop = bounds.height - 1 - vspan;

                    int align = ((JTextField)c).getHorizontalAlignment();
                    if(MUtilities.isLeftToRight(c)) {
                        if(align==LEADING) {
                            align = LEFT;
                        }
                        else if(align==TRAILING) {
                            align = RIGHT;
                        }
                    }
                    else {
                        if(align==LEADING) {
                            align = RIGHT;
                        }
                        else if(align==TRAILING) {
                            align = LEFT;
                        }
                    }

                    switch (align) {
                    case SwingConstants.CENTER:
                        bounds.y += slop / 2;
                        bounds.height -= slop;
                        break;
                    case SwingConstants.RIGHT:
                        bounds.y += slop;
                        bounds.height -= slop;
                        break;
                    }
                } else {
                    // adjust the allocation to match the bounded range.
                    bounds.height = vspan;
                    bounds.y -= vis.getValue();
                }
            }
            return bounds;
        }
        return null;
    }

    /**
     * Update the visibility model with the associated JTextField
     * (if there is one) to reflect the current visibility as a
     * result of changes to the document model.  The bounded
     * range properties are updated.  If the view hasn't yet been
     * shown the extent will be zero and we just set it to be full
     * until determined otherwise.
     */
    void updateVisibilityModel() {
        Component c = getContainer();
        if (c instanceof JTextField) {
            JTextField field = (JTextField) c;
            BoundedRangeModel vis = field.getHorizontalVisibility();
            int hspan = (int) getPreferredSpan(X_AXIS);
            int extent = vis.getExtent();
            int maximum = Math.max(hspan, extent);
            extent = (extent == 0) ? maximum : extent;
            int value = maximum - extent;
            int oldValue = vis.getValue();
            if ((oldValue + extent) > maximum) {
                oldValue = maximum - extent;
            }
            value = Math.max(0, Math.min(value, oldValue));
            vis.setRangeProperties(value, extent, 0, maximum, false);
        }
    }

    // --- View methods -------------------------------------------

    /**
     * Renders using the given rendering surface and area on that surface.
     * The view may need to do layout and create child views to enable
     * itself to render into the given allocation.
     *
     * @param g the rendering surface to use
     * @param a the allocated region to render into
     *
     * @see View#paint
     */
    public void paint(Graphics g, Shape a) {
        Rectangle r = (Rectangle) a;
        g.clipRect(r.x, r.y, r.width, r.height);
        super.paint(g, a);
    }

    /**
     * Adjusts <code>a</code> based on the visible region and returns it.
     */
    Shape adjustPaintRegion(Shape a) {
        return adjustAllocation(a);
    }

    /**
     * Determines the preferred span for this view along an
     * axis.
     *
     * @param axis may be either View.X_AXIS or View.Y_AXIS
     * @return   the span the view would like to be rendered into >= 0.
     *           Typically the view is told to render into the span
     *           that is returned, although there is no guarantee.
     *           The parent may choose to resize or break the view.
     */
    public float getPreferredSpan(int axis) {
    	
        switch (axis) {
        case View.X_AXIS:
            Segment buff = MSegmentCache.getSharedSegment();
            Document doc = getDocument();
            int width;
            try {
                FontMetrics fm = getFontMetrics();
                doc.getText(0, doc.getLength(), buff);
                width = MUtilities.getTabbedTextWidth(buff, fm, 0, this, 0);
                if (buff.count > 0) {
                    Component c = getContainer();
                    firstLineOffset = MSwingUtilities.
                        getLeftSideBearing((c instanceof JComponent) ?
                                           (JComponent)c : null, fm,
                                           buff.array[buff.offset]);
                    firstLineOffset = Math.max(0, -firstLineOffset);
                }
                else {
                    firstLineOffset = 0;
                }
            } catch (BadLocationException bl) {
                width = 0;
            }
            MSegmentCache.releaseSharedSegment(buff);
            return width + firstLineOffset;
        default:
            return super.getPreferredSpan(axis);
        }
    }

    /**
     * Determines the resizability of the view along the
     * given axis.  A value of 0 or less is not resizable.
     *
     * @param axis View.X_AXIS or View.Y_AXIS
     * @return the weight -> 1 for View.X_AXIS, else 0
     */
    public int getResizeWeight(int axis) {
        if (axis == View.X_AXIS) {
            return 1;
        }
        return 0;
    }

    /**
     * Provides a mapping from the document model coordinate space
     * to the coordinate space of the view mapped to it.
     *
     * @param pos the position to convert >= 0
     * @param a the allocated region to render into
     * @return the bounding box of the given position
     * @exception BadLocationException  if the given position does not
     *   represent a valid location in the associated document
     * @see View#modelToView
     */
    public Shape modelToView(int pos, Shape a, Position.Bias b) throws BadLocationException {
        return super.modelToView(pos, adjustAllocation(a), b);
    }

    /**
     * Provides a mapping from the view coordinate space to the logical
     * coordinate space of the model.
     *
     * @param fx the X coordinate >= 0.0f
     * @param fy the Y coordinate >= 0.0f
     * @param a the allocated region to render into
     * @return the location within the model that best represents the
     *  given point in the view
     * @see View#viewToModel
     */
    public int viewToModel(float fx, float fy, Shape a, Position.Bias[] bias) {
        return super.viewToModel(fx, fy, adjustAllocation(a), bias);
    }

    /**
     * Gives notification that something was inserted into the document
     * in a location that this view is responsible for.
     *
     * @param changes the change information from the associated document
     * @param a the current allocation of the view
     * @param f the factory to use to rebuild if the view has children
     * @see View#insertUpdate
     */
    public void insertUpdate(DocumentEvent changes, Shape a, ViewFactory f) {
        super.insertUpdate(changes, adjustAllocation(a), f);
        updateVisibilityModel();
    }

    /**
     * Gives notification that something was removed from the document
     * in a location that this view is responsible for.
     *
     * @param changes the change information from the associated document
     * @param a the current allocation of the view
     * @param f the factory to use to rebuild if the view has children
     * @see View#removeUpdate
     */
    public void removeUpdate(DocumentEvent changes, Shape a, ViewFactory f) {
        super.removeUpdate(changes, adjustAllocation(a), f);
        updateVisibilityModel();
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
		pos = getNextVisualPositionFromHorizantal(pos, b, a, direction, biasRet);
		
		return pos;
	}
    
}
