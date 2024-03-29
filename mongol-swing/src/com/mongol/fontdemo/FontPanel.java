// File   : gui-appearance/font/fontdemo/FontPanel.java
// Purpose: A component to draw sample string with given font family, style,
//          size, and aliasing.
// Author : Fred Swartz - 28 Sep 2006 - Placed in public domain.

package com.mongol.fontdemo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;

//////////////////////////////////////////////////////////////// FontPanel class
class FontPanel extends JPanel {
    private String _fontName;
    private int    _fontStyle;
    private int    _fontSize;
    private boolean _antialiased;
    
    //============================================================== constructor
    public FontPanel(String font, int style, int size, boolean antialiased) {
        this.setPreferredSize(new Dimension(400, 100));
        this.setBackground(Color.white);
        this.setForeground(Color.black);
        _fontName  = font;
        _fontStyle = style;
        _fontSize  = size;
        _antialiased = antialiased;
    }
    
    //================================================= @Override paintComponent
    @Override public void paintComponent(Graphics g) {
        super.paintComponent(g);        // Paint background.
        
        Graphics2D g2 = (Graphics2D)g;  // Graphics2 for antialiasing.
        
        String text = " ᠮᠣᠩᠭᠤᠯ  ᠪᠢᠴᠢᠭ᠌ Font(\""
                + _fontName + "\", "
                + fontStyleCodeToFontStyleString(_fontStyle) + ", "
                + _fontSize + ");";
        Font f = new Font(_fontName, _fontStyle, _fontSize);
        g2.setFont(f);
        
        if (_antialiased) {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        }
        
        //... Find the size of this text so we can center it.
        FontMetrics fm = g2.getFontMetrics(f);  // metrics for this object
        Rectangle2D rect = fm.getStringBounds(text, g2); // size of string
        int textHeight  = (int)(rect.getHeight());
        int textWidth   = (int)(rect.getWidth());
        
        //... Center text horizontally and vertically
        int x = (this.getWidth()  - textWidth)  / 2;
        int y = (this.getHeight() - textHeight) / 2  + fm.getAscent();
        
        g2.drawString(text, x, y);
    }
    
    //================================================================== SETTERS
    public void setFontName(String fn)  { _fontName = fn;    this.repaint();}
    public void setFontSize(int size)   { _fontSize = size;  this.repaint();}
    public void setFontStyle(int style) {_fontStyle = style; this.repaint();}
    public void setAntialiasing(boolean antialiased) {
        _antialiased = antialiased;
        this.repaint();
    }
    
    //=========================================== fontStyleCodeToFontStyleString
    // Utility method for converting font codes to name.
    public static String fontStyleCodeToFontStyleString(int styleCode) {
        String styleName;
        switch (styleCode) {
            case Font.PLAIN:            styleName = "Font.PLAIN";       break;
            case Font.ITALIC:           styleName = "Font.ITALIC";      break;
            case Font.BOLD:             styleName = "Font.BOLD";        break;
            case Font.ITALIC+Font.BOLD: styleName = "ITALIC+Font.BOLD"; break;
            default: throw new IllegalArgumentException(
                    "fontStyleCodeToFontStyleString: Unknown font code: " +
                    styleCode);
        };
        return styleName;
    }
}
