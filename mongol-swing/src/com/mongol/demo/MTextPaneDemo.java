package com.mongol.demo;
import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.EtchedBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.mongol.swing.MEditorPane;
import com.mongol.swing.MSwingRotateUtilities;
import com.mongol.swing.MTextPane;

public class MTextPaneDemo extends JFrame {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		MTextPaneDemo frame = new MTextPaneDemo();

	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    // frame.setBounds(10, 10, 300, 200);
	    frame.setTitle("Mongolian Swing Component example");
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
		
	}
	
	public MTextPaneDemo() {
		
		String context =  "ᠮᠠᠯ ᠰᠦᠷᠦᠭ ᠦᠨ ᠪᠣᠳᠠᠲᠠᠢ ᠨᠢᠭᠡ ᠣᠨᠴᠠᠯᠢᠭ ᠪᠣᠶᠣ ᠮᠣᠯᠵᠢᠬᠤ ᠠᠮᠢᠳᠤᠷᠠᠯ  " ;
		
		BorderLayout layout = new BorderLayout();
	    JPanel p = new JPanel();
	    p.setLayout(layout);
	    p.setBorder(new EtchedBorder(EtchedBorder.LOWERED));

		context =  "ᠲᠤᠰ ᠦᠭᠦᠯᠡᠯ ᠦᠨ ᠤᠳᠤᠷᠢᠳᠭᠠᠯ ᠤᠨ ᠬᠡᠰᠡᠭ ᠲᠥ ᠰᠡᠳᠦᠪ ᠰᠤᠩᠭ᠋ᠤᠭᠰᠠᠨ ᠰᠢᠯᠲᠠᠭᠠᠨ᠂  日本語" +
				"ᠠᠴᠢ ᠬᠣᠯᠪᠣᠭᠳᠠᠯ᠂ ᠵᠣᠷᠢᠯᠭ᠎ᠠ᠂ ᠠᠷᠭ᠎ᠠ ᠪᠠ ᠰᠣᠳᠤᠯᠤᠯ ᠤᠨ ᠲᠣᠢ᠌ᠮᠣ ᠶᠢ ᠲᠠᠨᠢᠯᠴᠠᠭᠤᠯᠪᠠ᠃ " +
				"ᠨᠢᠭᠡᠳᠦᠭᠡᠷ ᠪᠦᠯᠦᠭ ᠲᠥ᠂ ᠮᠣᠩᠭᠣᠯ ᠣᠨᠢᠰᠤᠭ᠎ᠠ ᠶᠢᠨ ᠡᠭᠦᠰᠦᠯ ᠦᠨ ᠱᠠᠲᠤ ᠦᠶ᠎ᠡ ᠨᠡᠯᠢᠶᠡᠳ ᠡᠷᠲᠡ ᠦᠶ᠎ᠡ ᠲᠠᠢ ᠬᠣᠯᠪᠣᠭᠳᠠᠬᠤ ᠪᠣᠯᠪᠠᠴᠣ ᠵᠢᠩᠭᠢᠨᠢ ᠲᠦᠯᠦᠪᠰᠢᠷᠡᠵᠦ ᠬᠦᠭ᠍ᠵᠢᠭᠰᠡᠨ ᠨᠢ ᠮᠣᠩᠭ᠋ᠣᠯᠴᠣᠳ ᠤᠨ ᠮᠠᠯᠵᠢᠯ ᠠᠵᠤ ᠠᠬᠤᠢ ᠨᠡᠯᠢᠶᠡᠳ " +
				"ᠬᠦᠭ᠍ᠵᠢᠯᠲᠡ ᠲᠠᠢ ᠪᠣᠯᠤᠭᠰᠠᠨ ᠦᠶ᠎ᠡ ᠲᠠᠢ ᠬᠣᠯᠪᠣᠭᠳᠠᠨ᠎ᠠ ᠭᠡᠰᠡᠨ ᠢ ᠮᠣᠩᠭᠣᠯ ᠣᠨᠢᠰᠤᠭ᠎ᠠ ᠶᠢᠨ ᠠᠭᠤᠯᠭ᠎ᠠ ᠡᠴᠡ ᠬᠠᠢ᠌ᠪᠠ᠃ ᠮᠠᠯᠵᠢᠯ ᠰᠣᠶᠣᠯ ᠨᠢ ᠮᠣᠩᠭᠣᠯ ᠣᠨᠢᠰᠤᠭ᠎ᠠ ᠶᠢᠨ ᠲᠦᠯᠦᠪᠰᠢᠷᠡᠯ ᠢ ᠪᠣᠢ ᠪᠣᠯᠭᠠᠭᠰᠠᠨ ᠢᠶᠡᠷ ᠪᠠᠷᠠᠬᠤ ᠦᠭᠡᠢ ᠮᠣᠩᠭᠣᠯ ᠣᠨᠢᠰᠤᠭ᠎ᠠ ᠶᠢᠨ ᠵᠠᠨ " +
				"ᠦᠢᠯᠡ᠂ ᠰᠡᠳᠭᠢᠮᠵᠢ ᠶᠢ ᠢᠯᠡᠳᠲᠡ ᠨᠥᠯᠥᠭᠡᠯᠡᠵᠦ᠂ ᠰᠠᠭᠤᠷᠢ ᠪᠣᠯᠬᠤᠢ᠌ᠴᠠ ᠦᠢᠯᠡᠳᠦᠯ ᠭᠠᠷᠭᠠᠭᠰᠠᠨ ᠪᠠᠢ᠌ᠨ᠎ᠠ. ᠮᠠᠯ ᠰᠦᠷᠦᠭ ᠦᠨ ᠪᠣᠳᠠᠲᠠᠢ ᠨᠢᠭᠡ ᠣᠨᠴᠠᠯᠢᠭ ᠪᠣᠶᠣ ᠮᠠᠯᠵᠢᠬᠤ ᠠᠮᠢᠳᠤᠷᠠᠯ ᠤᠨ ᠠᠭᠤᠯᠭ᠎ᠠ ᠪᠠᠷ ᠢᠶᠡᠨ ᠶᠠᠮᠠᠷ ᠨᠢᠭᠡᠨ ᠶᠠᠭᠤᠮ᠎ᠠ ᠦᠵᠡᠭᠳᠡᠯ᠂ " +
				"ᠬᠦᠮᠦᠨ ᠠᠮᠢᠲᠠᠨ ᠢ ᠳᠦᠷᠰᠦᠯᠡᠳᠡᠭ ᠨᠢ ᠮᠣᠩᠭᠣᠯ ᠣᠨᠢᠰᠤᠭ᠎ᠠ ᠶᠢᠨ ᠰᠡᠳᠭᠢᠮᠵᠢ ᠶᠢᠨ ᠳᠣᠪᠤᠢ᠌ᠮ᠎ᠠ ᠣᠨᠴᠠᠯᠢᠭ ᠪᠣᠯᠵᠠᠢ᠃ ᠡᠳᠡᠭᠡᠷ ᠣᠨᠢᠰᠤᠭ᠎ᠠ ᠳᠤ ᠮᠣᠩᠭᠣᠯ ᠬᠦᠮᠦᠨ ᠦ ᠮᠠᠯ ᠤᠨᠤᠭ᠎ᠠ ᠳᠤ ᠪᠠᠨ ᠬᠠᠢ᠌ᠷᠠᠲᠠᠢ ᠪᠠᠢ᠌ᠳᠠᠭ ᠲᠦᠭᠡᠮᠡᠯ ᠪᠣᠳᠤᠯ ᠰᠡᠳᠭᠢᠯᠭᠡ ᠪᠠ ᠡᠬᠡ " +
				"ᠪᠠᠢ᠌ᠭᠠᠯᠢ ᠪᠠᠨ ᠳᠡᠭᠡᠳᠦᠯᠡᠵᠦ ᠰᠢᠳᠦᠳᠡᠭ ᠦᠵᠡᠯ ᠰᠡᠳᠭᠢᠴᠡ ᠴᠣᠬᠤᠯᠵᠠᠭᠰᠠᠨ ᠪᠠᠢ᠌ᠭ᠎ᠠ ᠶᠢ ᠣᠯᠵᠤ ᠬᠠᠷᠠᠨ᠎ᠠ᠃ ᠳᠠᠷᠤᠢ ᠮᠠᠯᠵᠢᠯ ᠰᠣᠶᠣᠯ ᠨᠢ ᠮᠣᠩᠭᠣᠯ ᠪᠠᠢ᠌ᠭᠠᠯᠢ ᠶᠢᠨ ᠦᠪᠡᠷᠮᠢᠴᠡ ᠳᠦᠷᠢ ᠲᠦᠷᠬᠦ ᠪᠠ ᠮᠣᠩᠭᠣᠯ ᠬᠦᠮᠦᠨ ᠦ ᠰᠠᠨᠠᠵᠤ ᠰᠡᠳᠭᠢᠬᠦ ᠣᠨᠴᠠᠯᠢᠭ ᠪᠠᠢ᠌ᠳᠠᠯ ᠢ " +
				"ᠨᠥᠬᠦᠴᠡᠯᠳᠦᠭᠦᠯᠦᠭᠰᠡᠨ ᠪᠠᠢ᠌ᠭ᠎ᠠ ᠶᠢ ᠮᠣᠩᠭᠣᠯ ᠣᠨᠢᠰᠤᠭ᠎ᠠ ᠨᠡᠯᠢᠶᠡᠳ ᠬᠡᠮᠵᠢᠶᠡᠨ᠎ᠡ ᠬᠠᠷᠠᠭᠤᠯᠵᠤ ᠳᠡᠢ᠌ᠯᠬᠦ ᠶᠢ ᠪᠣᠳᠠᠲᠠᠢ ᠣᠨᠢᠰᠤᠭ᠎ᠠ ᠪᠠᠷ ᠲᠠᠢ᠌ᠯᠪᠤᠷᠢᠯᠠᠪᠠ᠃" ;
//		MTextPane txtPane2 = new MTextPane( );
		MTextPane txtPane2 = new MTextPane( MSwingRotateUtilities.ROTATE_VERTICAL, MSwingRotateUtilities.ROTATE_LEFTTORIGHT);
		txtPane2.putClientProperty(MEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
		txtPane2.setFont( new Font("Mongolian White", Font.PLAIN, 24));
		
		txtPane2.setText( context );
		txtPane2.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		JScrollPane scrollingArea2 = new JScrollPane(txtPane2);
	    p.add(scrollingArea2, BorderLayout.CENTER);
        	    
	    getContentPane().add(p, BorderLayout.CENTER);
        p.setSize(980, 680);

	}

}
