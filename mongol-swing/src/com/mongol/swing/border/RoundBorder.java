package com.mongol.swing.border;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.border.AbstractBorder;

public class RoundBorder extends AbstractBorder {
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		Graphics2D g2 = (Graphics2D)g;      
		g2.setColor(Color.GRAY);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);      
		g2.drawRoundRect(x, y, width-1, height-1, height, height);    
	}
	public Insets getBorderInsets(Component c) {
		return new Insets(4, 8, 4, 8);    
	}    
	public Insets getBorderInsets(Component c, Insets insets) {
		insets.left = insets.right = 8;      
		insets.top = insets.bottom = 4;      
		return insets;    
	}
}
