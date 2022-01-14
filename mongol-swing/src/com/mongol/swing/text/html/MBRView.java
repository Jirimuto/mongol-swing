package com.mongol.swing.text.html;

import javax.swing.text.*;

/**
 * Modified BRView
 * 
 * @author jrmt@Almas
 */
class MBRView extends MInlineView {

	/**
	 * Creates a new view that represents a &lt;BR&gt; element.
	 * 
	 * @param elem
	 *            the element to create a view for
	 */
	public MBRView(Element elem) {
		super(elem);
	}

	/**
	 * Forces a line break.
	 * 
	 * @return View.ForcedBreakWeight
	 */
	public int getBreakWeight(int axis, float pos, float len) {
		if (axis == X_AXIS) {
			return ForcedBreakWeight;
		} else {
			return super.getBreakWeight(axis, pos, len);
		}
	}
}