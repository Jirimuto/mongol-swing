package com.mongol.swing.text;

import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

import com.mongol.swing.MSwingRotateUtilities;
import com.mongol.swing.plaf.MRotation;

public class MEditorBoxView extends MCompositeView {

    /**
     * Constructs a <code>BoxView</code>.
     *
     * @param elem the element this view is responsible for
     * @param axis either <code>View.X_AXIS</code> or <code>View.Y_AXIS</code>
     */
    public MEditorBoxView(Element elem, int axis) {
        super(elem);
        tempRect = new Rectangle();
        this.majorAxis = axis;

        majorOffsets = new int[0];
        majorSpans = new int[0];
        majorReqValid = false;
        majorAllocValid = false;
        minorOffsets = new int[0];
        minorSpans = new int[0];
        minorReqValid = false;
        minorAllocValid = false;
    }

    /**
     * Constructs a <code>BoxView</code>.
     *
     * @param elem the element this view is responsible for
     * @param axis either <code>View.X_AXIS</code> or <code>View.Y_AXIS</code>
     */
    public MEditorBoxView(Element elem, int direction, int hint) {
        super(elem);
        super.setRotateDirection(direction);
        if( direction == MSwingRotateUtilities.ROTATE_HORIZANTAL )
        	this.majorAxis = MView.Y_AXIS;
        else
        	this.majorAxis = MView.X_AXIS;
        
        super.setRotateHint(hint);
        tempRect = new Rectangle();

        majorOffsets = new int[0];
        majorSpans = new int[0];
        majorReqValid = false;
        majorAllocValid = false;
        minorOffsets = new int[0];
        minorSpans = new int[0];
        minorReqValid = false;
        minorAllocValid = false;
    }
    
    public void setRotateHint(int hint) {
    	super.setRotateHint(hint);
    }

    public int getRotateHint(){
        return super.getRotateHint();
    }

    public void setRotateDirection(int direction){
    	super.setRotateDirection(direction);
        if( direction == MSwingRotateUtilities.ROTATE_HORIZANTAL )
        	this.setAxis(MView.Y_AXIS);
        else
        	this.setAxis(MView.X_AXIS);
    }

    public int getRotateDirection(){
        return super.getRotateDirection();
    }

    /**
     * Fetches the tile axis property.  This is the axis along which
     * the child views are tiled.
     *
     * @return the major axis of the box, either
     *  <code>View.X_AXIS</code> or <code>View.Y_AXIS</code>
     *
     * @since 1.3
     */
    public int getAxis() {
        return majorAxis;
    }

    /**
     * Sets the tile axis property.  This is the axis along which
     * the child views are tiled.
     *
     * @param axis either <code>View.X_AXIS</code> or <code>View.Y_AXIS</code>
     *
     * @since 1.3
     */
    public void setAxis(int axis) {
        boolean axisChanged = (axis != majorAxis);
        majorAxis = axis;
        if (axisChanged) {
            preferenceChanged(null, true, true);
        }
    }

    /**
     * Invalidates the layout along an axis.  This happens
     * automatically if the preferences have changed for
     * any of the child views.  In some cases the layout
     * may need to be recalculated when the preferences
     * have not changed.  The layout can be marked as
     * invalid by calling this method.  The layout will
     * be updated the next time the <code>setSize</code> method
     * is called on this view (typically in paint).
     *
     * @param axis either <code>View.X_AXIS</code> or <code>View.Y_AXIS</code>
     *
     * @since 1.3
     */
    public void layoutChanged(int axis) {
        if (axis == majorAxis) {
            majorAllocValid = false;
        } else {
            minorAllocValid = false;
        }
    }

    /**
     * Determines if the layout is valid along the given axis.
     *
     * @param axis either <code>View.X_AXIS</code> or <code>View.Y_AXIS</code>
     *
     * @since 1.4
     */
    protected boolean isLayoutValid(int axis) {
        if (axis == majorAxis) {
            return majorAllocValid;
        } else {
            return minorAllocValid;
        }
    }

    /**
     * Paints a child.  By default
     * that is all it does, but a subclass can use this to paint
     * things relative to the child.
     *
     * @param g the graphics context
     * @param alloc the allocated region to paint into
     * @param index the child index, >= 0 && < getViewCount()
     */
    protected void paintChild(Graphics g, Rectangle alloc, int index) {
        MView child = getView(index);
        child.paint(g, alloc);
    }

    // --- View methods ---------------------------------------------

    /**
     * Invalidates the layout and resizes the cache of
     * requests/allocations.  The child allocations can still
     * be accessed for the old layout, but the new children
     * will have an offset and span of 0.
     *
     * @param index the starting index into the child views to insert
     *   the new views; this should be a value >= 0 and <= getViewCount
     * @param length the number of existing child views to remove;
     *   This should be a value >= 0 and <= (getViewCount() - offset)
     * @param elems the child views to add; this value can be
     *   <code>null</code>to indicate no children are being added
     *   (useful to remove)
     */
    public void replace(int index, int length, View[] elems) {
        super.replace(index, length, elems);

        // invalidate cache
        int nInserted = (elems != null) ? elems.length : 0;
        majorOffsets = updateLayoutArray(majorOffsets, index, nInserted);
        majorSpans = updateLayoutArray(majorSpans, index, nInserted);
        majorReqValid = false;
        majorAllocValid = false;
        minorOffsets = updateLayoutArray(minorOffsets, index, nInserted);
        minorSpans = updateLayoutArray(minorSpans, index, nInserted);
        minorReqValid = false;
        minorAllocValid = false;
        Container c = this.getContainer();
        c.repaint();
    }

    /**
     * Resizes the given layout array to match the new number of
     * child views.  The current number of child views are used to
     * produce the new array.  The contents of the old array are
     * inserted into the new array at the appropriate places so that
     * the old layout information is transferred to the new array.
     *
     * @param oldArray the original layout array
     * @param offset location where new views will be inserted
     * @param nInserted the number of child views being inserted;
     *          therefore the number of blank spaces to leave in the
     *          new array at location <code>offset</code>
     * @return the new layout array
     */
    int[] updateLayoutArray(int[] oldArray, int offset, int nInserted) {
        int n = getViewCount();
        int[] newArray = new int[n];

        System.arraycopy(oldArray, 0, newArray, 0, offset);
        System.arraycopy(oldArray, offset,
                         newArray, offset + nInserted, n - nInserted - offset);
        return newArray;
    }

    /**
     * Forwards the given <code>DocumentEvent</code> to the child views
     * that need to be notified of the change to the model.
     * If a child changed its requirements and the allocation
     * was valid prior to forwarding the portion of the box
     * from the starting child to the end of the box will
     * be repainted.
     *
     * @param ec changes to the element this view is responsible
     *  for (may be <code>null</code> if there were no changes)
     * @param e the change information from the associated document
     * @param a the current allocation of the view
     * @param f the factory to use to rebuild if the view has children
     * @see #insertUpdate
     * @see #removeUpdate
     * @see #changedUpdate
     * @since 1.3
     */
    protected void forwardUpdate(DocumentEvent.ElementChange ec,
                                 DocumentEvent e, Shape a, ViewFactory f) {
        boolean wasValid = isLayoutValid(majorAxis);
        super.forwardUpdate(ec, e, a, f);

        // determine if a repaint is needed
        if (wasValid && (! isLayoutValid(majorAxis))) {
            // Repaint is needed because one of the tiled children
            // have changed their span along the major axis.  If there
            // is a hosting component and an allocated shape we repaint.
            Component c = getContainer();
            if ((a != null) && (c != null)) {
                int pos = e.getOffset();
                int index = getViewIndexAtPosition(pos);
                Rectangle alloc = getInsideAllocation(a);
                if (majorAxis == X_AXIS) {
                    alloc.x += majorOffsets[index];
                    alloc.width -= majorOffsets[index];
                } else {
                    alloc.y += minorOffsets[index];
                    alloc.height -= minorOffsets[index];
                }
                c.repaint(alloc.x, alloc.y, alloc.width, alloc.height);
            }
        }
    }

    /**
     * This is called by a child to indicate its
     * preferred span has changed.  This is implemented to
     * throw away cached layout information so that new
     * calculations will be done the next time the children
     * need an allocation.
     *
     * @param child the child view
     * @param width true if the width preference should change
     * @param height true if the height preference should change
     */
    public void preferenceChanged(View child, boolean width, boolean height) {
        boolean majorChanged = (majorAxis == X_AXIS) ? width : height;
        boolean minorChanged = (majorAxis == X_AXIS) ? height : width;
        if (majorChanged) {
            majorReqValid = false;
            majorAllocValid = false;
        }
        if (minorChanged) {
            minorReqValid = false;
            minorAllocValid = false;
        }
        super.preferenceChanged(child, width, height);
    }

    /**
     * Gets the resize weight.  A value of 0 or less is not resizable.
     *
     * @param axis may be either <code>View.X_AXIS</code> or
     *          <code>View.Y_AXIS</code>
     * @return the weight
     * @exception IllegalArgumentException for an invalid axis
     */
    public int getResizeWeight(int axis) {
        checkRequests(axis);
        if (axis == majorAxis) {
            if ((majorRequest.preferred != majorRequest.minimum) ||
                (majorRequest.preferred != majorRequest.maximum)) {
                return 1;
            }
        } else {
            if ((minorRequest.preferred != minorRequest.minimum) ||
                (minorRequest.preferred != minorRequest.maximum)) {
                return 1;
            }
        }
        return 0;
    }

    /**
     * Sets the size of the view along an axis.  This should cause
     * layout of the view along the given axis.
     *
     * @param axis may be either <code>View.X_AXIS</code> or
     *          <code>View.Y_AXIS</code>
     * @param span the span to layout to >= 0
     */
    void setSpanOnAxis(int axis, float span) {
        if (axis == majorAxis) {
            if (majorSpan != (int) span) {
                majorAllocValid = false;
            }
            if (! majorAllocValid) {
                // layout the major axis
                majorSpan = (int) span;
                checkRequests(majorAxis);
                layoutMajorAxis(majorSpan, axis, majorOffsets, majorSpans);
                majorAllocValid = true;

                // flush changes to the children
                updateChildSizes();
            }
        } else {
            if (((int) span) != minorSpan) {
                minorAllocValid = false;
            }
            if (! minorAllocValid) {
                // layout the minor axis
                minorSpan = (int) span;
                checkRequests(axis);
                layoutMinorAxis(minorSpan, axis, minorOffsets, minorSpans);
                minorAllocValid = true;

                // flush changes to the children
                updateChildSizes();
            }
        }
    }

    /**
     * Propagates the current allocations to the child views.
     */
    void updateChildSizes() {
        int n = getViewCount();
        if (majorAxis == X_AXIS) {
            for (int i = 0; i < n; i++) {
                MView v = getView(i);
                v.setSize((float) majorSpans[i], (float) minorSpans[i]);
            }
        } else {
            for (int i = 0; i < n; i++) {
                MView v = getView(i);
                v.setSize((float) minorSpans[i], (float) majorSpans[i]);
            }
        }
    }

    /**
     * Returns the size of the view along an axis.  This is implemented
     * to return zero.
     *
     * @param axis may be either <code>View.X_AXIS</code> or
     *          <code>View.Y_AXIS</code>
     * @return the current span of the view along the given axis, >= 0
     */
    float getSpanOnAxis(int axis) {
        if (axis == majorAxis) {
            return majorSpan;
        } else {
            return minorSpan;
        }
    }

    /**
     * Sets the size of the view.  This should cause
     * layout of the view if the view caches any layout
     * information.  This is implemented to call the
     * layout method with the sizes inside of the insets.
     *
     * @param width the width >= 0
     * @param height the height >= 0
     */
    public void setSize(float width, float height) {
        layout(Math.max(0, (int)(width - getLeftInset() - getRightInset())),
               Math.max(0, (int)(height - getTopInset() - getBottomInset())));
    }

    /**
     * Renders the <code>BoxView</code> using the given
     * rendering surface and area
     * on that surface.  Only the children that intersect
     * the clip bounds of the given <code>Graphics</code>
     * will be rendered.
     *
     * @param g the rendering surface to use
     * @param allocation the allocated region to render into
     * @see MView#paint
     */
    public void paint(Graphics g, Shape allocation) {
    	
        Rectangle alloc = (allocation instanceof Rectangle) ?
                           (Rectangle)allocation : allocation.getBounds();
        int n = getViewCount();
        int x = alloc.x + getLeftInset();
        int y = alloc.y + getTopInset();
        Rectangle clip = g.getClipBounds();
        for (int i = 0; i < n; i++) {
            tempRect.x = x + getOffset(X_AXIS, i);
            tempRect.y = y + getOffset(Y_AXIS, i);
            tempRect.width = getSpan(X_AXIS, i);
            tempRect.height = getSpan(Y_AXIS, i);
            int trx0 = tempRect.x, trx1 = trx0 + tempRect.width;
            int try0 = tempRect.y, try1 = try0 + tempRect.height;
            int crx0 = clip.x, crx1 = crx0 + clip.width;
            int cry0 = clip.y, cry1 = cry0 + clip.height;
            // We should paint views that intersect with clipping region
            // even if the intersection has no inside points (is a line).
            // This is needed for supporting views that have zero width, like
            // views that contain only combining marks.
            if ((trx1 >= crx0) && (try1 >= cry0) && (crx1 >= trx0) && (cry1 >= try0)) {
                paintChild(g, tempRect, i);
            }
        }
    }

    /**
     * Fetches the allocation for the given child view.
     * This enables finding out where various views
     * are located.  This is implemented to return
     * <code>null</code> if the layout is invalid,
     * otherwise the superclass behavior is executed.
     *
     * @param index the index of the child, >= 0 && < getViewCount()
     * @param a  the allocation to this view
     * @return the allocation to the child; or <code>null</code>
     *          if <code>a</code> is <code>null</code>;
     *          or <code>null</code> if the layout is invalid
     */
    public Shape getChildAllocation(int index, Shape a) {
        if (a != null) {
            Shape ca = super.getChildAllocation(index, a);
            if ((ca != null) && (! isAllocationValid())) {
                // The child allocation may not have been set yet.
                Rectangle r = (ca instanceof Rectangle) ?
                    (Rectangle) ca : ca.getBounds();
                if ((r.width == 0) && (r.height == 0)) {
                    return null;
                }
            }
            return ca;
        }
        return null;
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
        return super.modelToView(pos, a, b);
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
        Rectangle alloc = a.getBounds();
        if (! isAllocationValid()) {
    		setSize(alloc.width, alloc.height);
        }
        if( getRotateDirection() == MSwingRotateUtilities.ROTATE_HORIZANTAL ){
        	return viewToModelHorizantal(x, y, a, bias);
        } else if ( getRotateHint() != MSwingRotateUtilities.ROTATE_RIGHTTOLEFT ){
        	return viewToModelVerticalL2R(x, y, a, bias);
        } else {
        	return viewToModelVerticalR2L(alloc.width - x, y, a, bias);
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
     * Determines the desired alignment for this view along an
     * axis.  This is implemented to give the total alignment
     * needed to position the children with the alignment points
     * lined up along the axis orthoginal to the axis that is
     * being tiled.  The axis being tiled will request to be
     * centered (i.e. 0.5f).
     *
     * @param axis may be either <code>View.X_AXIS</code>
     *   or <code>View.Y_AXIS</code>
     * @return the desired alignment >= 0.0f && <= 1.0f; this should
     *   be a value between 0.0 and 1.0 where 0 indicates alignment at the
     *   origin and 1.0 indicates alignment to the full span
     *   away from the origin; an alignment of 0.5 would be the
     *   center of the view
     * @exception IllegalArgumentException for an invalid axis
     */
    public float getAlignment(int axis) {
        checkRequests(axis);
        if (axis == majorAxis) {
            return majorRequest.alignment;
        } else {
            return minorRequest.alignment;
        }
    }

    /**
     * Determines the preferred span for this view along an
     * axis.
     *
     * @param axis may be either <code>View.X_AXIS</code>
     *           or <code>View.Y_AXIS</code>
     * @return   the span the view would like to be rendered into >= 0;
     *           typically the view is told to render into the span
     *           that is returned, although there is no guarantee;
     *           the parent may choose to resize or break the view
     * @exception IllegalArgumentException for an invalid axis type
     */
    public float getPreferredSpan(int axis) {
        checkRequests(axis);
        float marginSpan = (axis == X_AXIS) ? getLeftInset() + getRightInset() :
            getTopInset() + getBottomInset();
        if (axis == majorAxis) {
            return ((float)majorRequest.preferred) + marginSpan;
        } else {
            return ((float)minorRequest.preferred) + marginSpan;
        }
    }

    /**
     * Determines the minimum span for this view along an
     * axis.
     *
     * @param axis may be either <code>View.X_AXIS</code>
     *           or <code>View.Y_AXIS</code>
     * @return  the span the view would like to be rendered into >= 0;
     *           typically the view is told to render into the span
     *           that is returned, although there is no guarantee;
     *           the parent may choose to resize or break the view
     * @exception IllegalArgumentException for an invalid axis type
     */
    public float getMinimumSpan(int axis) {
        checkRequests(axis);
        float marginSpan = (axis == X_AXIS) ? getLeftInset() + getRightInset() :
            getTopInset() + getBottomInset();
        if (axis == majorAxis) {
            return ((float)majorRequest.minimum) + marginSpan;
        } else {
            return ((float)minorRequest.minimum) + marginSpan;
        }
    }

    /**
     * Determines the maximum span for this view along an
     * axis.
     *
     * @param axis may be either <code>View.X_AXIS</code>
     *           or <code>View.Y_AXIS</code>
     * @return   the span the view would like to be rendered into >= 0;
     *           typically the view is told to render into the span
     *           that is returned, although there is no guarantee;
     *           the parent may choose to resize or break the view
     * @exception IllegalArgumentException for an invalid axis type
     */
    public float getMaximumSpan(int axis) {
        checkRequests(axis);
        float marginSpan = (axis == X_AXIS) ? getLeftInset() + getRightInset() :
            getTopInset() + getBottomInset();
        if (axis == majorAxis) {
            return ((float)majorRequest.maximum) + marginSpan;
        } else {
            return ((float)minorRequest.maximum) + marginSpan;
        }
    }

    // --- local methods ----------------------------------------------------

    /**
     * Are the allocations for the children still
     * valid?
     *
     * @return true if allocations still valid
     */
    protected boolean isAllocationValid() {
        return (majorAllocValid && minorAllocValid);
    }

    /**
     * Determines if a point falls before an allocated region.
     *
     * @param x the X coordinate >= 0
     * @param y the Y coordinate >= 0
     * @param innerAlloc the allocated region; this is the area
     *   inside of the insets
     * @return true if the point lies before the region else false
     */
    protected boolean isBefore(int x, int y, Rectangle innerAlloc) {
    	
        if (majorAxis == MView.X_AXIS) {
            return (x < innerAlloc.x);
        } else {
            return (y < innerAlloc.y);
        }
    }

    /**
     * Determines if a point falls after an allocated region.
     *
     * @param x the X coordinate >= 0
     * @param y the Y coordinate >= 0
     * @param innerAlloc the allocated region; this is the area
     *   inside of the insets
     * @return true if the point lies after the region else false
     */
    protected boolean isAfter(int x, int y, Rectangle innerAlloc) {
        if (majorAxis == MView.X_AXIS) {
            return (x > (innerAlloc.width + innerAlloc.x));
        } else {
            return (y > (innerAlloc.height + innerAlloc.y));
        }
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
        if( getRotateDirection() == MSwingRotateUtilities.ROTATE_HORIZANTAL ) {
        	return getViewAtPointHorizantal(x, y, alloc);
        } else if ( getRotateHint() != MSwingRotateUtilities.ROTATE_RIGHTTOLEFT ) {
        	return getViewAtPointVerticalL2R(x, y, alloc);
        } else {
        	return getViewAtPointVerticalR2L(x, y, alloc);
        }
    	
    }
    private MView getViewAtPointHorizantal(int x, int y, Rectangle alloc) {
        int n = getViewCount();
        if (majorAxis == MView.X_AXIS) {
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
        }
    }

    private MView getViewAtPointVerticalL2R(int x, int y, Rectangle alloc) {
        int n = getViewCount();
        if (majorAxis == MView.X_AXIS) {
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
        }
    }

    private MView getViewAtPointVerticalR2L(int x, int y, Rectangle alloc) {
        int n = getViewCount();
        if (majorAxis == MView.X_AXIS) {
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

    /**
     * Perform layout on the box
     *
     * @param width the width (inside of the insets) >= 0
     * @param height the height (inside of the insets) >= 0
     */
    protected void layout(int width, int height) {
        if (majorAxis == Y_AXIS) {
	        setSpanOnAxis(X_AXIS, width);
	        setSpanOnAxis(Y_AXIS, height);
        } else {
	        setSpanOnAxis(Y_AXIS, height);
	        setSpanOnAxis(X_AXIS, width);
        }
    }

    /**
     * Returns the current width of the box.  This is the width that
     * it was last allocated.
     * @return the current width of the box
     */
    public int getWidth() {
        int span;
        if (majorAxis == X_AXIS) {
            span = majorSpan;
        } else {
            span = minorSpan;
        }
        span += getLeftInset() - getRightInset();
        return span;
    }

    /**
     * Returns the current height of the box.  This is the height that
     * it was last allocated.
     * @return the current height of the box
     */
    public int getHeight() {
        int span;
        if (majorAxis == Y_AXIS) {
            span = majorSpan;
        } else {
            span = minorSpan;
        }
        span += getTopInset() - getBottomInset();
        return span;
    }

    /**
     * Performs layout for the major axis of the box (i.e. the
     * axis that it represents). The results of the layout (the
     * offset and span for each children) are placed in the given
     * arrays which represent the allocations to the children
     * along the major axis.
     *
     * @param targetSpan the total span given to the view, which
     *  would be used to layout the children
     * @param axis the axis being layed out
     * @param offsets the offsets from the origin of the view for
     *  each of the child views; this is a return value and is
     *  filled in by the implementation of this method
     * @param spans the span of each child view; this is a return
     *  value and is filled in by the implementation of this method
     */
    protected void layoutMajorAxis(int targetSpan, int axis, int[] offsets, int[] spans) {
        /*
         * first pass, calculate the preferred sizes
         * and the flexibility to adjust the sizes.
         */
        long preferred = 0;
        int n = getViewCount();
        for (int i = 0; i < n; i++) {
            MView v = getView(i);
            spans[i] = (int) v.getPreferredSpan(axis);
            preferred += spans[i];
        }

        /*
         * Second pass, expand or contract by as much as possible to reach
         * the target span.
         */

        // determine the adjustment to be made
        long desiredAdjustment = targetSpan - preferred;
        float adjustmentFactor = 0.0f;
        int[] diffs = null;

        if (desiredAdjustment != 0) {
            long totalSpan = 0;
            diffs = new int[n];
            for (int i = 0; i < n; i++) {
                MView v = getView(i);
                int tmp;
                if (desiredAdjustment < 0) {
                    tmp = (int)v.getMinimumSpan(axis);
                    diffs[i] = spans[i] - tmp;
                } else {
                    tmp = (int)v.getMaximumSpan(axis);
                    diffs[i] = tmp - spans[i];
                }
                totalSpan += tmp;
            }

            float maximumAdjustment = Math.abs(totalSpan - preferred);
                adjustmentFactor = desiredAdjustment / maximumAdjustment;
                adjustmentFactor = Math.min(adjustmentFactor, 1.0f);
                adjustmentFactor = Math.max(adjustmentFactor, -1.0f);
            }

        // make the adjustments
        int totalOffset = 0;
        for (int i = 0; i < n; i++) {
            offsets[i] = totalOffset;
            if (desiredAdjustment != 0) {
                float adjF = adjustmentFactor * diffs[i];
                spans[i] += Math.round(adjF);
            }
            totalOffset = (int) Math.min((long) totalOffset + (long) spans[i], Integer.MAX_VALUE);
        }
    }

    /**
     * Performs layout for the minor axis of the box (i.e. the
     * axis orthoginal to the axis that it represents). The results
     * of the layout (the offset and span for each children) are
     * placed in the given arrays which represent the allocations to
     * the children along the minor axis.
     *
     * @param targetSpan the total span given to the view, which
     *  would be used to layout the children
     * @param axis the axis being layed out
     * @param offsets the offsets from the origin of the view for
     *  each of the child views; this is a return value and is
     *  filled in by the implementation of this method
     * @param spans the span of each child view; this is a return
     *  value and is filled in by the implementation of this method
     */
    protected void layoutMinorAxis(int targetSpan, int axis, int[] offsets, int[] spans) {
        int n = getViewCount();
        for (int i = 0; i < n; i++) {
            MView v = getView(i);
            int max = (int) v.getMaximumSpan(axis);
            if (max < targetSpan) {
                // can't make the child this wide, align it
                float align = v.getAlignment(axis);
                offsets[i] = (int) ((targetSpan - max) * align);
                spans[i] = max;
            } else {
                // make it the target width, or as small as it can get.
                int min = (int)v.getMinimumSpan(axis);
                offsets[i] = 0;
                spans[i] = Math.max(min, targetSpan);
            }
        }
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
    }

    /**
     * Checks the request cache and update if needed.
     * @param axis the axis being studied
     * @exception IllegalArgumentException if <code>axis</code> is
     *  neither <code>View.X_AXIS</code> nor <code>View.Y_AXIS</code>
     */
    void checkRequests(int axis) {
        if ((axis != X_AXIS) && (axis != Y_AXIS)) {
            throw new IllegalArgumentException("Invalid axis: " + axis);
        }
        if (axis == majorAxis) {
            if (!majorReqValid) {
                majorRequest = calculateMajorAxisRequirements(axis,
                                                              majorRequest);
                majorReqValid = true;
            }
        } else if (! minorReqValid) {
            minorRequest = calculateMinorAxisRequirements(axis, minorRequest);
            minorReqValid = true;
        }
    }

    /**
     * Computes the location and extent of each child view
     * in this <code>BoxView</code> given the <code>targetSpan</code>,
     * which is the width (or height) of the region we have to
     * work with.
     *
     * @param targetSpan the total span given to the view, which
     *  would be used to layout the children
     * @param axis the axis being studied, either
     *          <code>View.X_AXIS</code> or <code>View.Y_AXIS</code>
     * @param offsets an empty array filled by this method with
     *          values specifying the location  of each child view
     * @param spans  an empty array filled by this method with
     *          values specifying the extent of each child view
     */
    protected void baselineLayout(int targetSpan, int axis, int[] offsets, int[] spans) {
        int totalAscent = (int)(targetSpan * getAlignment(axis));
        int totalDescent = targetSpan - totalAscent;

        int n = getViewCount();

        for (int i = 0; i < n; i++) {
            MView v = getView(i);
            float align = v.getAlignment(axis);
            float viewSpan;

            if (v.getResizeWeight(axis) > 0) {
                // if resizable then resize to the best fit

                // the smallest span possible
                float minSpan = v.getMinimumSpan(axis);
                // the largest span possible
                float maxSpan = v.getMaximumSpan(axis);

                if (align == 0.0f) {
                    // if the alignment is 0 then we need to fit into the descent
                    viewSpan = Math.max(Math.min(maxSpan, totalDescent), minSpan);
                } else if (align == 1.0f) {
                    // if the alignment is 1 then we need to fit into the ascent
                    viewSpan = Math.max(Math.min(maxSpan, totalAscent), minSpan);
                } else {
                    // figure out the span that we must fit into
                    float fitSpan = Math.min(totalAscent / align,
                                             totalDescent / (1.0f - align));
                    // fit into the calculated span
                    viewSpan = Math.max(Math.min(maxSpan, fitSpan), minSpan);
                }
            } else {
                // otherwise use the preferred spans
                viewSpan = v.getPreferredSpan(axis);
            }

            offsets[i] = totalAscent - (int)(viewSpan * align);
            spans[i] = (int)viewSpan;
        }
    }

    /**
     * Calculates the size requirements for this <code>BoxView</code>
     * by examining the size of each child view.
     *
     * @param axis the axis being studied
     * @param r the <code>SizeRequirements</code> object;
     *          if <code>null</code> one will be created
     * @return the newly initialized <code>SizeRequirements</code> object
     */
    protected SizeRequirements baselineRequirements(int axis, SizeRequirements r) {
        SizeRequirements totalAscent = new SizeRequirements();
        SizeRequirements totalDescent = new SizeRequirements();

        if (r == null) {
            r = new SizeRequirements();
        }

        r.alignment = 0.5f;

        int n = getViewCount();

        // loop through all children calculating the max of all their ascents and
        // descents at minimum, preferred, and maximum sizes
        for (int i = 0; i < n; i++) {
            MView v = getView(i);
            float align = v.getAlignment(axis);
            float span;
            int ascent;
            int descent;

            // find the maximum of the preferred ascents and descents
            span = v.getPreferredSpan(axis);
            ascent = (int)(align * span);
            descent = (int)(span - ascent);
            totalAscent.preferred = Math.max(ascent, totalAscent.preferred);
            totalDescent.preferred = Math.max(descent, totalDescent.preferred);

            if (v.getResizeWeight(axis) > 0) {
                // if the view is resizable then do the same for the minimum and
                // maximum ascents and descents
                span = v.getMinimumSpan(axis);
                ascent = (int)(align * span);
                descent = (int)(span - ascent);
                totalAscent.minimum = Math.max(ascent, totalAscent.minimum);
                totalDescent.minimum = Math.max(descent, totalDescent.minimum);

                span = v.getMaximumSpan(axis);
                ascent = (int)(align * span);
                descent = (int)(span - ascent);
                totalAscent.maximum = Math.max(ascent, totalAscent.maximum);
                totalDescent.maximum = Math.max(descent, totalDescent.maximum);
            } else {
                // otherwise use the preferred
                totalAscent.minimum = Math.max(ascent, totalAscent.minimum);
                totalDescent.minimum = Math.max(descent, totalDescent.minimum);
                totalAscent.maximum = Math.max(ascent, totalAscent.maximum);
                totalDescent.maximum = Math.max(descent, totalDescent.maximum);
            }
        }

        // we now have an overall preferred, minimum, and maximum ascent and descent

        // calculate the preferred span as the sum of the preferred ascent and preferred descent
        r.preferred = (int)Math.min((long)totalAscent.preferred + (long)totalDescent.preferred,
                                    Integer.MAX_VALUE);

        // calculate the preferred alignment as the preferred ascent divided by the preferred span
        if (r.preferred > 0) {
            r.alignment = (float)totalAscent.preferred / r.preferred;
        }


        if (r.alignment == 0.0f) {
            // if the preferred alignment is 0 then the minimum and maximum spans are simply
            // the minimum and maximum descents since there's nothing above the baseline
            r.minimum = totalDescent.minimum;
            r.maximum = totalDescent.maximum;
        } else if (r.alignment == 1.0f) {
            // if the preferred alignment is 1 then the minimum and maximum spans are simply
            // the minimum and maximum ascents since there's nothing below the baseline
            r.minimum = totalAscent.minimum;
            r.maximum = totalAscent.maximum;
        } else {
            // we want to honor the preferred alignment so we calculate two possible minimum
            // span values using 1) the minimum ascent and the alignment, and 2) the minimum
            // descent and the alignment. We'll choose the larger of these two numbers.
            r.minimum = Math.round(Math.max(totalAscent.minimum / r.alignment,
                                          totalDescent.minimum / (1.0f - r.alignment)));
            // a similar calculation is made for the maximum but we choose the smaller number.
            r.maximum = Math.round(Math.min(totalAscent.maximum / r.alignment,
                                          totalDescent.maximum / (1.0f - r.alignment)));
        }

        return r;
    }

    /**
     * Fetches the offset of a particular child's current layout.
     * @param axis the axis being studied
     * @param childIndex the index of the requested child
     * @return the offset (location) for the specified child
     */
    protected int getOffset(int axis, int childIndex) {
        int[] offsets = (axis == majorAxis) ? majorOffsets : minorOffsets;
        return offsets[childIndex];
    }

    /**
     * Fetches the span of a particular childs current layout.
     * @param axis the axis being studied
     * @param childIndex the index of the requested child
     * @return the span (width or height) of the specified child
     */
    protected int getSpan(int axis, int childIndex) {
        int[] spans = (axis == majorAxis) ? majorSpans : minorSpans;
        return spans[childIndex];
    }

    /**
     * Determines in which direction the next view lays.
     * Consider the View at index n. Typically the <code>View</code>s
     * are layed out from left to right, so that the <code>View</code>
     * to the EAST will be at index n + 1, and the <code>View</code>
     * to the WEST will be at index n - 1. In certain situations,
     * such as with bidirectional text, it is possible
     * that the <code>View</code> to EAST is not at index n + 1,
     * but rather at index n - 1, or that the <code>View</code>
     * to the WEST is not at index n - 1, but index n + 1.
     * In this case this method would return true,
     * indicating the <code>View</code>s are layed out in
     * descending order. Otherwise the method would return false
     * indicating the <code>View</code>s are layed out in ascending order.
     * <p>
     * If the receiver is laying its <code>View</code>s along the
     * <code>Y_AXIS</code>, this will will return the value from
     * invoking the same method on the <code>View</code>
     * responsible for rendering <code>position</code> and
     * <code>bias</code>. Otherwise this will return false.
     *
     * @param position position into the model
     * @param bias either <code>Position.Bias.Forward</code> or
     *          <code>Position.Bias.Backward</code>
     * @return true if the <code>View</code>s surrounding the
     *          <code>View</code> responding for rendering
     *          <code>position</code> and <code>bias</code>
     *          are layed out in descending order; otherwise false
     */
    protected boolean flipEastAndWestAtEnds(int position,
                                            Position.Bias bias) {
        if(majorAxis == Y_AXIS) {
            int testPos = (bias == Position.Bias.Backward) ?
                          Math.max(0, position - 1) : position;
            int index = getViewIndexAtPosition(testPos);
            if(index != -1) {
                MView v = getView(index);
                if(v != null && v instanceof MCompositeView) {
                    // return ((CompositeView)v).flipEastAndWestAtEnds(position, bias);
                	return false;
                }
            }
        }
        return false;
    }

    /**
     * Provides a way to determine the next visually represented model
     * location that one might place a caret.  Some views may not be visible,
     * they might not be in the same order found in the model, or they just
     * might not allow access to some of the locations in the model.
     * This is a convenience method for {@link #getNextNorthSouthVisualPositionFrom}
     * and {@link #getNextEastWestVisualPositionFrom}.
     *
     * @param pos the position to convert >= 0
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
     * @exception BadLocationException
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
    
    private int getNextVisualPositionFromHorizantal(int pos, Position.Bias b, Shape a,
            int direction, Position.Bias[] biasRet)
            		throws BadLocationException {

        switch (direction) {
        case NORTH:
            return getNextNorthSouthVisualPositionFrom(pos, b, a, direction,
                                                       biasRet);
        case SOUTH:
            return getNextNorthSouthVisualPositionFrom(pos, b, a, direction,
                                                       biasRet);
        case EAST:
            return getNextEastWestVisualPositionFrom(pos, b, a, direction,
                                                     biasRet);
        case WEST:
            return getNextEastWestVisualPositionFrom(pos, b, a, direction,
                                                     biasRet);
        default:
            throw new IllegalArgumentException("Bad direction: " + direction);
        }
    }

    private int getNextVisualPositionFromVerticalL2R(int pos, Position.Bias b, Shape a,
            int direction, Position.Bias[] biasRet)
            		throws BadLocationException {

    	
        switch (direction) {
        case WEST:
            return getNextNorthSouthVisualPositionFrom(pos, b, a, direction,
                                                       biasRet);
        case EAST:
            return getNextNorthSouthVisualPositionFrom(pos, b, a, direction,
                                                       biasRet);
        case SOUTH:
            return getNextEastWestVisualPositionFrom(pos, b, a, direction,
                                                     biasRet);
        case NORTH:
            return getNextEastWestVisualPositionFrom(pos, b, a, direction,
                                                     biasRet);
        default:
            throw new IllegalArgumentException("Bad direction: " + direction);
        }
    }

    private int getNextVisualPositionFromVerticalR2L(int pos, Position.Bias b, Shape a,
            int direction, Position.Bias[] biasRet)
            		throws BadLocationException {

        switch (direction) {
        case WEST:
            return getNextNorthSouthVisualPositionFrom(pos, b, a, direction,
                                                       biasRet);
        case EAST:
            return getNextNorthSouthVisualPositionFrom(pos, b, a, direction,
                                                       biasRet);
        case SOUTH:
            return getNextEastWestVisualPositionFrom(pos, b, a, direction,
                                                     biasRet);
        case NORTH:
            return getNextEastWestVisualPositionFrom(pos, b, a, direction,
                                                     biasRet);
        default:
            throw new IllegalArgumentException("Bad direction: " + direction);
        }
    }

    // --- variables ------------------------------------------------

    int majorAxis;

    int majorSpan;
    int minorSpan;

    /*
     * Request cache
     */
    boolean majorReqValid;
    boolean minorReqValid;
    SizeRequirements majorRequest;
    SizeRequirements minorRequest;

    /*
     * Allocation cache
     */
    boolean majorAllocValid;
    int[] majorOffsets;
    int[] majorSpans;
    boolean minorAllocValid;
    int[] minorOffsets;
    int[] minorSpans;
    
    /** used in paint. */
    Rectangle tempRect;
}
