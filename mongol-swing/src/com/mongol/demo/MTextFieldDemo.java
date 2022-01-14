package com.mongol.demo;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import com.mongol.encode.MongolianFontUtil;
import com.mongol.swing.MLabel;
import com.mongol.swing.MSwingRotateUtilities;
import com.mongol.swing.MTextField;

public class MTextFieldDemo extends JFrame {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		MTextFieldDemo frame = new MTextFieldDemo();

	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    // frame.setBounds(10, 10, 300, 200);
	    frame.setTitle("Mongolian Swing Component example");
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
		
	}
	
	public MTextFieldDemo() {
		
		String context =  "ᠮᠠᠯ ᠰᠦᠷᠦᠭ ᠦᠨ ᠪᠣᠳᠠᠲᠠᠢ ᠨᠢᠭᠡ ᠣᠨᠴᠠᠯᠢᠭ ᠪᠣᠶᠣ ᠮᠠᠯᠵᠢᠬᠤ ᠠᠮᠢᠳᠤᠷᠠᠯ  " ;
		
	    GridBagLayout layout = new GridBagLayout();
	    JPanel p = new JPanel();
	    p.setLayout(layout);
	    p.setBorder(new EtchedBorder(EtchedBorder.LOWERED));

	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.insets = new Insets(5, 5, 5, 5);

	    int line = 0;
	    int y = 0;
        MLabel lblTxt7 = new MLabel( "Mongolian" );
        lblTxt7.setRotateDirection(MSwingRotateUtilities.ROTATE_VERTICAL);
        lblTxt7.setRotateHint(MSwingRotateUtilities.ROTATE_LEFTTORIGHT);
        lblTxt7.setFont(new Font("MS明朝", Font.PLAIN, 16));
	    gbc.gridx = line;
	    gbc.gridy = y++;
	    gbc.gridwidth = 1;
	    gbc.gridheight = 1;
	    layout.setConstraints(lblTxt7, gbc);
	    p.add(lblTxt7);

		context =  "ᠡᠷᠡᠨ ᠵᠠᠭᠣᠨ ᠪ ᠨᠠᠷᠠᠨ ᠳᠧᠩᠯᠦ ᠳᠠᠳᠭᠠᠯ ᠳᠠᠰᠭᠠᠯ  日本語です。" ;
		
		MTextField txtField2 = new MTextField( context, 40 );
		txtField2.setRotateDirection(MSwingRotateUtilities.ROTATE_VERTICAL);
		txtField2.setRotateHint(MSwingRotateUtilities.ROTATE_LEFTTORIGHT);
		Font font = txtField2.getFont();
//		Font font = MongolianFontUtil.getFont("Dialog");
//		if( font!=null ) {
//			Font mfont = font.deriveFont(Font.PLAIN, 16); 
//			txtField2.setFont(mfont);
//		}
		txtField2.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
	    gbc.gridx = line;
	    gbc.gridy = y++;
	    gbc.gridwidth = 1;
	    gbc.gridheight = 1;
	    layout.setConstraints(txtField2, gbc);
	    p.add(txtField2);
        
	    getContentPane().add(p, BorderLayout.CENTER);
        p.setSize(780, 1080);

	}

}
