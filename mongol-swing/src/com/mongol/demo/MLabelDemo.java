package com.mongol.demo;
import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.mongol.swing.MLabel;
import com.mongol.swing.MSwingRotateUtilities;

public class MLabelDemo extends JFrame {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new MLabelDemo();
	}
	
	MLabelDemo() {
		
		String context =  "ᠮᠠᠯ ᠰᠦᠷᠦᠭ ᠦᠨ ᠪᠣᠳᠠᠲᠠᠢ ᠨᠢᠭᠡ ᠣᠨᠴᠠᠯᠢᠭ ᠪᠣᠶᠣ ᠮᠠᠯᠵᠢᠬᠤ ᠠᠮᠢᠳᠤᠷᠠᠯ ABCD 中文、 日本語。abcdfg" ;
		
        JFrame frame = new JFrame("Mongolian Encoding example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
        MLabel lblTxt = new MLabel( context );
        lblTxt.setToolTipText( context );
        lblTxt.setRotateDirection(MSwingRotateUtilities.ROTATE_HORIZANTAL);
        lblTxt.setRotateHint(MSwingRotateUtilities.ROTATE_LEFTTORIGHT);
        Font font = lblTxt.getFont();
        if( font != null ) {
        	// Font newfont = font.deriveFont(font.getStyle(), 24);
        	Font newfont = new Font("Mongolian White", Font.PLAIN, 24);
        	lblTxt.setFont(newfont);
        }
        frame.getContentPane().add(lblTxt, BorderLayout.NORTH);
        
        lblTxt = new MLabel( context );
        lblTxt.setRotateDirection(MSwingRotateUtilities.ROTATE_VERTICAL);
        lblTxt.setRotateHint(MSwingRotateUtilities.ROTATE_LEFTTORIGHT);
        lblTxt.setFont(new Font("Mongolian White", Font.PLAIN, 24));
        frame.getContentPane().add(lblTxt, BorderLayout.WEST);
        
		context =  "中文正确吗？日本語正しいですか。" ;
        lblTxt = new MLabel( context );
        lblTxt.setRotateDirection(MSwingRotateUtilities.ROTATE_VERTICAL);
        lblTxt.setRotateHint(MSwingRotateUtilities.ROTATE_NONE);
        lblTxt.setFont(new Font("MS明朝", Font.PLAIN, 24));
        frame.getContentPane().add(lblTxt, BorderLayout.CENTER);
        
		context =  "This is English Sentence." ;
        lblTxt = new MLabel( context );
        lblTxt.setRotateDirection(MSwingRotateUtilities.ROTATE_VERTICAL);
        lblTxt.setRotateHint(MSwingRotateUtilities.ROTATE_DEFAULT);
        lblTxt.setFont(new Font(null, Font.PLAIN, 24));
        frame.getContentPane().add(lblTxt, BorderLayout.EAST);
        
		context =  "中文正确吗？日本語正しいですか。" ;
        lblTxt = new MLabel( context );
        lblTxt.setRotateDirection(MSwingRotateUtilities.ROTATE_HORIZANTAL);
        lblTxt.setRotateHint(MSwingRotateUtilities.ROTATE_RIGHTTOLEFT);
        lblTxt.setFont(new Font("MS明朝", Font.PLAIN, 24));
        frame.getContentPane().add(lblTxt, BorderLayout.SOUTH);
        
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
	}

}
