package com.mongol.demo;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import com.mongol.swing.MButton;
import com.mongol.swing.MLabel;
import com.mongol.swing.MSwingRotateUtilities;

public class MButtonDemo extends JFrame {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		MButtonDemo frame = new MButtonDemo();

	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    // frame.setBounds(10, 10, 300, 200);
	    frame.setTitle("Mongolian Swing Component example");
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
		
	}
	
	public MButtonDemo() {
		
		String context =  "ᠮᠠᠯ ᠰᠦᠷᠦᠭ ᠦᠨ ᠪᠣᠳᠠᠲᠠᠢ ᠨᠢᠭᠡ ᠣᠨᠴᠠᠯᠢᠭ ᠪᠣᠶᠣ ᠮᠣᠯᠵᠢᠬᠤ ᠠᠮᠢᠳᠤᠷᠠᠯ  " ;
		
	    GridBagLayout layout = new GridBagLayout();
	    JPanel p = new JPanel();
	    p.setLayout(layout);
	    p.setBorder(new EtchedBorder(EtchedBorder.LOWERED));

	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.insets = new Insets(5, 5, 5, 5);

	    int line = 0;
        MLabel lblTxt7 = new MLabel( "Mongolian" );
        lblTxt7.setRotateDirection(MSwingRotateUtilities.ROTATE_VERTICAL);
        lblTxt7.setRotateHint(MSwingRotateUtilities.ROTATE_LEFTTORIGHT);
        lblTxt7.setFont(new Font("MS明朝", Font.PLAIN, 16));
	    gbc.gridx = line;
	    gbc.gridy = 0;
	    gbc.gridwidth = 1;
	    gbc.gridheight = 1;
	    layout.setConstraints(lblTxt7, gbc);
	    p.add(lblTxt7);

		context =  "ᠡᠷᠡᠨ ᠵᠠᠭᠣᠨ ᠪ  ABCD 中文 助かります。" ;

		MButton button1 = new MButton( context );
		// button2.setRotateDirection(MSwingRotateUtilities.ROTATE_VERTICAL);
		// button2.setRotateHint(MSwingRotateUtilities.ROTATE_RIGHTTOLEFT);
		button1.setFont(new Font("Mongolian White", Font.PLAIN, 20));
		button1.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		Dimension size = new Dimension(300, 100);
		button1.setPreferredSize(size);
	    gbc.gridx = line;
	    gbc.gridy = 1;
	    gbc.gridwidth = 1;
	    gbc.gridheight = 1;
	    layout.setConstraints(button1, gbc);
	    p.add(button1);
        
		MButton button2 = new MButton( "2."+context );
		button2.setRotateDirection(MSwingRotateUtilities.ROTATE_VERTICAL);
		button2.setRotateHint(MSwingRotateUtilities.ROTATE_LEFTTORIGHT);
		button2.setFont(new Font("Mongolian White", Font.PLAIN, 20));
		button2.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		size = new Dimension(60, 300);
		button2.setPreferredSize(size);
	    gbc.gridx = line;
	    gbc.gridy = 2;
	    gbc.gridwidth = 1;
	    gbc.gridheight = 1;
	    layout.setConstraints(button2, gbc);
	    p.add(button2);
        
	    getContentPane().add(p, BorderLayout.CENTER);
        p.setSize(780, 580);

	}

}
