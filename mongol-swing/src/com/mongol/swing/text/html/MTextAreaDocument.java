package com.mongol.swing.text.html;

import javax.swing.text.*;

/**
 * Modified TextAreaDocument
 * 
 * @author jrmt@Almas
 */

class MTextAreaDocument extends PlainDocument {

	String initialText;

	/**
	 * Resets the model by removing all the data, and restoring it to its
	 * initial state.
	 */
	void reset() {
		try {
			remove(0, getLength());
			if (initialText != null) {
				insertString(0, initialText, null);
			}
		} catch (BadLocationException e) {
		}
	}

	/**
	 * Stores the data that the model is initially loaded with.
	 */
	void storeInitialText() {
		try {
			initialText = getText(0, getLength());
		} catch (BadLocationException e) {
		}
	}
}
