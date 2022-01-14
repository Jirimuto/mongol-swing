package com.mongol.demo;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import com.mongol.encode.MongolianFontUtil;
import com.mongol.swing.MLabel;
import com.mongol.swing.MSwingRotateUtilities;
import com.mongol.swing.MTextField;

public class TextFieldTest extends JFrame {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		TextFieldTest frame = new TextFieldTest();

	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    // frame.setBounds(10, 10, 300, 200);
	    frame.setTitle("Mongolian Swing Component example");
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
		
	}
	
	public TextFieldTest() {
		
		String context =  "ᠮᠠᠯ ᠰᠦᠷᠦᠭ ᠦᠨ ᠪᠣᠳᠠᠲᠠᠢ ᠨᠢᠭᠡ ᠣᠨᠴᠠᠯᠢᠭ ᠪᠣᠶᠣ ᠮᠣᠯᠵᠢᠬᠤ ᠠᠮᠢᠳᠤᠷᠠᠯ  " ;
		
	    GridBagLayout layout = new GridBagLayout();
	    JPanel p = new JPanel();
	    p.setLayout(layout);
	    p.setBorder(new EtchedBorder(EtchedBorder.LOWERED));

	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.insets = new Insets(5, 5, 5, 5);

	    int line = 0;
        JLabel lblTxt7 = new JLabel( "Mongolian ᠮᠠᠯ ᠰᠦᠷᠦᠭ ᠦᠨ ᠪᠣᠳᠠᠲᠠᠢ ᠨᠢᠭᠡ ᠣᠨᠴᠠᠯᠢᠭ ᠪᠣᠶᠣ ᠮᠣᠯᠵᠢᠬᠤ ᠠᠮᠢᠳᠤᠷᠠᠯ  " );
        // lblTxt7.setRotateDirection(MSwingRotateUtilities.ROTATE_VERTICAL);
        // lblTxt7.setRotateHint(MSwingRotateUtilities.ROTATE_LEFTTORIGHT);
		Font mfont =new Font("Mongolian Writing", Font.PLAIN, 16);
        // lblTxt7.setFont(new Font("Mongolian Art", Font.PLAIN, 16));
        
        lblTxt7.setFont(mfont);
	    gbc.gridx = line;
	    gbc.gridy = 0;
	    gbc.gridwidth = 1;
	    gbc.gridheight = 1;
	    layout.setConstraints(lblTxt7, gbc);
	    p.add(lblTxt7);

		context =  "ᠡᠷᠡᠨ ᠵᠠᠭᠣᠨ ᠪ ᠨᠠᠷᠠᠨ ᠳᠧᠩᠯᠦ ᠳᠠᠳᠭᠠᠯ ᠳᠠᠰᠭᠠᠯ  日本語です。" ;
		
		JTextField txtField2 = new JTextField( context, 40 );
		//txtField2.setRotateDirection(MSwingRotateUtilities.ROTATE_VERTICAL);
		//txtField2.setRotateHint(MSwingRotateUtilities.ROTATE_LEFTTORIGHT);
		// Font font = MongolianFontUtil.getFont("Mongolian Art");
		//Font mfont = font.deriveFont(Font.PLAIN, 24); 
		txtField2.setFont(mfont);
		// txtField2.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
	    gbc.gridx = line;
	    gbc.gridy = 1;
	    gbc.gridwidth = 1;
	    gbc.gridheight = 1;
	    layout.setConstraints(txtField2, gbc);
	    p.add(txtField2);
        
	    getContentPane().add(p, BorderLayout.CENTER);
        p.setSize(780, 580);

	}

}
