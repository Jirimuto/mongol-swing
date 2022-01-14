package com.mongol.swing;

import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JComponent;

public class MSwingRotateUtilities {

	public String[] fCharStrings; // for efficiency, break the fLabel into one-char strings to be passed to drawString
	public int[] fCharWidths; // Roman characters should be centered when not rotated (Japanese fonts are monospaced)
	public int[] fPosition; // Japanese half-height characters need to be shifted when drawn vertically
	public int fWidth, fHeight, fCharHeight, fDescent; // Cached for speed
	
	public void calcDimensions(JComponent c, Graphics g, String text, int fRotation ) {
		FontMetrics fm = c.getFontMetrics(c.getFont());
		fCharHeight = fm.getAscent() + fm.getDescent();
		fDescent = fm.getDescent();
		if (fRotation == ROTATE_NONE) {
			int len = text.length();
			char data[] = new char[len];
			text.getChars(0, len, data, 0);
			// if not rotated, width is that of the widest char in the string
			fWidth = 0;
			// we need an array of one-char strings for drawString
			fCharStrings = new String[len];
			fCharWidths = new int[len];
			fPosition = new int[len];
			char ch;
			for (int i = 0; i < len; i++) {
				ch = data[i];
				fCharWidths[i] = fm.charWidth(ch);
				if (fCharWidths[i] > fWidth)
					fWidth = fCharWidths[i];
				fCharStrings[i] = new String(data, i, 1);				
				// small kana and punctuation
				if (sDrawsInTopRight.indexOf(ch) >= 0) // if ch is in sDrawsInTopRight
					fPosition[i] = POSITION_TOP_RIGHT;
				else if (sDrawsInFarTopRight.indexOf(ch) >= 0)
					fPosition[i] = POSITION_FAR_TOP_RIGHT;
				else
					fPosition[i] = POSITION_NORMAL;
			}
			// and height is the font height * the char count, + one extra leading at the bottom
			fHeight = fCharHeight * len + fDescent;
		}		
		else {
			// if rotated, width is the height of the string
			fWidth = fCharHeight;
			// and height is the width, plus some buffer space 
			fHeight = fm.stringWidth(text) + 2*kBufferSpace;
		}
	}
	
	static final int POSITION_NORMAL = 0;
	static final int POSITION_TOP_RIGHT = 1;
	static final int POSITION_FAR_TOP_RIGHT = 2;
	
	public static final int ROTATE_HORIZANTAL = 0;
	public static final int ROTATE_VERTICAL = 1;
	
	public static final int ROTATE_DEFAULT = 0x00;
	public static final int ROTATE_NONE = 0x01;
	public static final int ROTATE_LEFTTORIGHT = 0x02;
	public static final int ROTATE_RIGHTTOLEFT = 0x04;
	
	/** 
	 verifyRotation
	
	returns the best rotation for the string (ROTATE_NONE, ROTATE_LEFT, ROTATE_RIGHT)
	
	This is public static so you can use it to test a string without creating a Vertical Text
	
	from http://www.unicode.org/unicode/reports/tr9/tr9-3.html
	When setting text using the Arabic script in vertical lines, 
	it is more common to employ a horizontal baseline that 
	is rotated by 90� counterclockwise so that the characters 
	are ordered from top to bottom. Latin text and numbers 
	may be rotated 90� clockwise so that the characters 
	are also ordered from top to bottom.
	
	Rotation rules
	- Roman can rotate left, right, or none - default right (counterclockwise)
	- CJK can't rotate
	- Arabic must rotate - default left (clockwise)

	from the online edition of _The Unicode Standard, Version 3.0_, file ch10.pdf page 4
	Ideographs are found in three blocks of the Unicode Standard...
	U+4E00-U+9FFF, U+3400-U+4DFF, U+F900-U+FAFF
	
	Hiragana is U+3040-U+309F, katakana is U+30A0-U+30FF
	
	from http://www.unicode.org/unicode/faq/writingdirections.html
	East Asian scripts are frequently written in vertical lines 
	which run from top-to-bottom and are arrange columns either 
	from left-to-right (Mongolian) or right-to-left (other scripts). 
	Most characters use the same shape and orientation when displayed 
	horizontally or vertically, but many punctuation characters 
	will change their shape when displayed vertically.
	
	Letters and words from other scripts are generally rotated through 
	ninety degree angles so that they, too, will read from top to bottom. 
	That is, letters from left-to-right scripts will be rotated clockwise 
	and letters from right-to-left scripts counterclockwise, both 
	through ninety degree angles.
	
	Unlike the bidirectional case, the choice of vertical layout 
	is usually treated as a formatting style; therefore, 
	the Unicode Standard does not define default rendering behavior 
	for vertical text nor provide directionality controls designed to override such behavior

	*/
	public static int verifyRotation(String label, int rotateHint) {
		boolean hasCJK = false;
		boolean hasMustRotate = false; // Arabic, etc
		
		int len = label.length();
		char data[] = new char[len];
		char ch;
		label.getChars(0, len, data, 0);
		for (int i = 0; i < len; i++) {
			ch = data[i];
			if ((ch >= '\u4E00' && ch <= '\u9FFF') ||
				(ch >= '\u3400' && ch <= '\u4DFF') ||
				(ch >= '\uF900' && ch <= '\uFAFF') ||
				(ch >= '\u3040' && ch <= '\u309F') ||
				(ch >= '\u30A0' && ch <= '\u30FF') )
			   hasCJK = true;
			if ((ch >= '\u0590' && ch <= '\u05FF') || // Hebrew
				(ch >= '\u0600' && ch <= '\u06FF') || // Arabic
				(ch >= '\u0700' && ch <= '\u074F') ) // Syriac
			   hasMustRotate = true;
		}
		// If you mix Arabic with Chinese, you're on your own
		if (hasCJK)
			return DEFAULT_CJK;
		
		int legal = hasMustRotate ? LEGAL_MUST_ROTATE : LEGAL_ROMAN;
		if ((rotateHint & legal) > 0)
			return rotateHint;
		
		// The hint wasn't legal, or it was zero
		return hasMustRotate ? DEFAULT_MUST_ROTATE : DEFAULT_ROMAN;
	}
	
	// The small kana characters and Japanese punctuation that draw in the top right quadrant:
	// small a, i, u, e, o, tsu, ya, yu, yo, wa  (katakana only) ka ke
	static final String sDrawsInTopRight = 
		"\u3041\u3043\u3045\u3047\u3049\u3063\u3083\u3085\u3087\u308E" + // hiragana 
		"\u30A1\u30A3\u30A5\u30A7\u30A9\u30C3\u30E3\u30E5\u30E7\u30EE\u30F5\u30F6"; // katakana
	static final String sDrawsInFarTopRight = "\u3001\u3002"; // comma, full stop
	
	static final int DEFAULT_CJK = ROTATE_NONE;
	static final int LEGAL_ROMAN = ROTATE_NONE | ROTATE_LEFTTORIGHT | ROTATE_RIGHTTOLEFT;
	static final int DEFAULT_ROMAN = ROTATE_RIGHTTOLEFT; 
	static final int LEGAL_MUST_ROTATE = ROTATE_LEFTTORIGHT | ROTATE_RIGHTTOLEFT;
	static final int DEFAULT_MUST_ROTATE = ROTATE_LEFTTORIGHT;
	
	static final double NINETY_DEGREES = Math.toRadians(90.0);
	static final int kBufferSpace = 5;
	
}
