package com.mongol.swing.text.html;

import java.awt.*;
import javax.swing.SizeRequirements;
import javax.swing.event.DocumentEvent;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.AttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.html.CSS;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.StyleSheet;

/**
 * Modified paragraph view, and uses css attributes for its configuration.
 * 
 * @author jrmt@Almas
 */

public class MParagraphView extends com.mongol.swing.text.MParagraphView {

	/**
	 * Constructs a ParagraphView for the given element.
	 * 
	 * @param elem
	 *            the element that this view is responsible for
	 */
	public MParagraphView(Element elem) {
		super(elem);
	}

	/**
	 * Establishes the parent view for this view. This is guaranteed to be
	 * called before any other methods if the parent view is functioning
	 * properly.
	 * <p>
	 * This is implemented to forward to the superclass as well as call the <a
	 * href="#setPropertiesFromAttributes">setPropertiesFromAttributes</a>
	 * method to set the paragraph properties from the css attributes. The call
	 * is made at this time to ensure the ability to resolve upward through the
	 * parents view attributes.
	 * 
	 * @param parent
	 *            the new parent, or null if the view is being removed from a
	 *            parent it was previously added to
	 */
	public void setParent(View parent) {
		super.setParent(parent);
		if (parent != null) {
			setPropertiesFromAttributes();
		}
	}

	/**
	 * Fetches the attributes to use when rendering. This is implemented to
	 * multiplex the attributes specified in the model with a StyleSheet.
	 */
	public AttributeSet getAttributes() {
		if (attr == null) {
			MStyleSheet sheet = getStyleSheet();
			attr = sheet.getViewAttributes(this);
		}
		return attr;
	}

	/**
	 * Sets up the paragraph from css attributes instead of the values found in
	 * StyleConstants (i.e. which are used by the superclass). Since
	 */
	protected void setPropertiesFromAttributes() {
		MStyleSheet sheet = getStyleSheet();
		attr = sheet.getViewAttributes(this);
		painter = sheet.getBoxPainter(attr);
		if (attr != null) {
			super.setPropertiesFromAttributes();
			setInsets((short) painter.getInset(TOP, this),
					(short) painter.getInset(LEFT, this),
					(short) painter.getInset(BOTTOM, this),
					(short) painter.getInset(RIGHT, this));
			Object o = attr.getAttribute(MCSS.Attribute.TEXT_ALIGN);
			if (o != null) {
				// set horizontal alignment
				String ta = o.toString();
				if (ta.equals("left")) {
					setJustification(StyleConstants.ALIGN_LEFT);
				} else if (ta.equals("center")) {
					setJustification(StyleConstants.ALIGN_CENTER);
				} else if (ta.equals("right")) {
					setJustification(StyleConstants.ALIGN_RIGHT);
				} else if (ta.equals("justify")) {
					setJustification(StyleConstants.ALIGN_JUSTIFIED);
				}
			}
			// Get the width/height
			cssWidth = (MCSS.LengthValue) attr.getAttribute(MCSS.Attribute.WIDTH);
			cssHeight = (MCSS.LengthValue) attr
					.getAttribute(MCSS.Attribute.HEIGHT);
		}
	}

	protected MStyleSheet getStyleSheet() {
		MHTMLDocument doc = (MHTMLDocument) getDocument();
		return doc.getStyleSheet();
	}

	/**
	 * Calculate the needs for the paragraph along the minor axis. This
	 * implemented to use the requirements of the superclass, modified slightly
	 * to set a minimum span allowed. Typical html rendering doesn't let the
	 * view size shrink smaller than the length of the longest word.
	 */
	protected SizeRequirements calculateMinorAxisRequirements(int axis,
			SizeRequirements r) {
		r = super.calculateMinorAxisRequirements(axis, r);

		if (!MBlockView.spanSetFromAttributes(axis, r, cssWidth, cssHeight)) {
			// PENDING(prinz) Need to make this better so it doesn't require
			// InlineView and works with font changes within the word.

			// find the longest minimum span.
			float min = 0;
			int n = getLayoutViewCount();
			for (int i = 0; i < n; i++) {
				View v = getLayoutView(i);
				if (v instanceof MInlineView) {
					float wordSpan = ((MInlineView) v).getLongestWordSpan();
					min = Math.max(wordSpan, min);
				} else {
					min = Math.max(v.getMinimumSpan(axis), min);
				}
			}
			r.minimum = Math.max(r.minimum, (int) min);
			r.preferred = Math.max(r.minimum, r.preferred);
			r.maximum = Math.max(r.preferred, r.maximum);
		} else {
			// Offset by the margins so that pref/min/max return the
			// right value.
			int margin = (axis == X_AXIS) ? getLeftInset() + getRightInset()
					: getTopInset() + getBottomInset();
			r.minimum -= margin;
			r.preferred -= margin;
			r.maximum -= margin;
		}
		return r;
	}

	/**
	 * Indicates whether or not this view should be displayed. If none of the
	 * children wish to be displayed and the only visible child is the break
	 * that ends the paragraph, the paragraph will not be considered visible.
	 * Otherwise, it will be considered visible and return true.
	 * 
	 * @return true if the paragraph should be displayed
	 */
	public boolean isVisible() {

		int n = getLayoutViewCount() - 1;
		for (int i = 0; i < n; i++) {
			View v = getLayoutView(i);
			if (v.isVisible()) {
				return true;
			}
		}
		if (n > 0) {
			View v = getLayoutView(n);
			if ((v.getEndOffset() - v.getStartOffset()) == 1) {
				return false;
			}
		}
		// If it's the last paragraph and not editable, it shouldn't
		// be visible.
		if (getStartOffset() == getDocument().getLength()) {
			boolean editable = false;
			Component c = getContainer();
			if (c instanceof JTextComponent) {
				editable = ((JTextComponent) c).isEditable();
			}
			if (!editable) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Renders using the given rendering surface and area on that surface. This
	 * is implemented to delgate to the superclass after stashing the base
	 * coordinate for tab calculations.
	 * 
	 * @param g
	 *            the rendering surface to use
	 * @param a
	 *            the allocated region to render into
	 * @see View#paint
	 */
	public void paint(Graphics g, Shape a) {
		if (a == null) {
			return;
		}

		Rectangle r;
		if (a instanceof Rectangle) {
			r = (Rectangle) a;
		} else {
			r = a.getBounds();
		}
		painter.paint(g, r.x, r.y, r.width, r.height, this);
		super.paint(g, a);
	}

	/**
	 * Determines the preferred span for this view. Returns 0 if the view is not
	 * visible, otherwise it calls the superclass method to get the preferred
	 * span. axis.
	 * 
	 * @param axis
	 *            may be either View.X_AXIS or View.Y_AXIS
	 * @return the span the view would like to be rendered into; typically the
	 *         view is told to render into the span that is returned, although
	 *         there is no guarantee; the parent may choose to resize or break
	 *         the view
	 * @see javax.swing.text.ParagraphView#getPreferredSpan
	 */
	public float getPreferredSpan(int axis) {
		if (!isVisible()) {
			return 0;
		}
		return super.getPreferredSpan(axis);
	}

	/**
	 * Determines the minimum span for this view along an axis. Returns 0 if the
	 * view is not visible, otherwise it calls the superclass method to get the
	 * minimum span.
	 * 
	 * @param axis
	 *            may be either <code>View.X_AXIS</code> or
	 *            <code>View.Y_AXIS</code>
	 * @return the minimum span the view can be rendered into
	 * @see javax.swing.text.ParagraphView#getMinimumSpan
	 */
	public float getMinimumSpan(int axis) {
		if (!isVisible()) {
			return 0;
		}
		return super.getMinimumSpan(axis);
	}

	/**
	 * Determines the maximum span for this view along an axis. Returns 0 if the
	 * view is not visible, otherwise it calls the superclass method ot get the
	 * maximum span.
	 * 
	 * @param axis
	 *            may be either <code>View.X_AXIS</code> or
	 *            <code>View.Y_AXIS</code>
	 * @return the maximum span the view can be rendered into
	 * @see javax.swing.text.ParagraphView#getMaximumSpan
	 */
	public float getMaximumSpan(int axis) {
		if (!isVisible()) {
			return 0;
		}
		return super.getMaximumSpan(axis);
	}

	private AttributeSet attr;
	private MStyleSheet.BoxPainter painter;
	private MCSS.LengthValue cssWidth;
	private MCSS.LengthValue cssHeight;
}
