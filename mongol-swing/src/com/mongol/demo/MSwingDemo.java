package com.mongol.demo;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import com.mongol.swing.MLabel;
import com.mongol.swing.MSwingRotateUtilities;
import com.mongol.swing.MTextArea;
import com.mongol.swing.MTextField;

public class MSwingDemo extends JFrame {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		MSwingDemo frame = new MSwingDemo();

	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    // frame.setBounds(10, 10, 300, 200);
	    frame.setTitle("Mongolian Swing Component example");
        frame.setSize(980, 780);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
		
	}
	
	public MSwingDemo() {
		
		String context =  "ᠮᠠᠯ ᠰᠦᠷᠦᠭ ᠦᠨ ᠪᠣᠳᠠᠲᠠᠢ ᠨᠢᠭᠡ ᠣᠨᠴᠠᠯᠢᠭ ᠪᠣᠶᠣ ᠮᠠᠯᠵᠢᠬᠤ ᠠᠮᠢᠳᠤᠷᠠᠯ  " ;
		
	    GridBagLayout layout = new GridBagLayout();
	    JPanel p = new JPanel();
	    p.setLayout(layout);
	    p.setBorder(new EtchedBorder(EtchedBorder.LOWERED));

	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.insets = new Insets(5, 5, 5, 5);

	    int col = 0;
	    int row = 0;
	    MLabel lblTxt0  = new MLabel( context );
        lblTxt0.setRotateDirection(MSwingRotateUtilities.ROTATE_VERTICAL);
        lblTxt0.setRotateHint(MSwingRotateUtilities.ROTATE_LEFTTORIGHT);
        lblTxt0.setFont(new Font("Mongolian White", Font.PLAIN, 24));
	    gbc.gridx = col;
	    gbc.gridy = row;
	    gbc.gridwidth = 1;
	    gbc.gridheight = 10;
	    layout.setConstraints(lblTxt0, gbc);
	    p.add(lblTxt0);
        
	    col++;
        MLabel lblTxt1 = new MLabel( "Mongolian" );
        lblTxt1.setFont(new Font("Mongolian White", Font.PLAIN, 16));
	    gbc.gridx = col;
	    gbc.gridy = row;
	    gbc.gridwidth = 1;
	    gbc.gridheight = 1;
	    layout.setConstraints(lblTxt1, gbc);
	    p.add(lblTxt1);

	    MLabel lblTxt2  = new MLabel( context );
        lblTxt2.setRotateDirection(MSwingRotateUtilities.ROTATE_HORIZANTAL);
        lblTxt2.setRotateHint(MSwingRotateUtilities.ROTATE_LEFTTORIGHT);
        lblTxt2.setFont(new Font("Mongolian White", Font.PLAIN, 24));
	    gbc.gridx = col+1;
	    gbc.gridy = row;
	    gbc.gridwidth = 1;
	    gbc.gridheight = 1;
	    layout.setConstraints(lblTxt2, gbc);
	    p.add(lblTxt2);

		context =  "日本語正しいですか。" ;
	    MLabel lblTxt3  = new MLabel( context );
        lblTxt3.setRotateDirection(MSwingRotateUtilities.ROTATE_VERTICAL);
        lblTxt3.setRotateHint(MSwingRotateUtilities.ROTATE_NONE);
        lblTxt3.setFont(new Font("MS明朝", Font.PLAIN, 16));
	    gbc.gridx = col+2;
	    gbc.gridy = 0;
	    gbc.gridwidth = 1;
	    gbc.gridheight = 10;
	    layout.setConstraints(lblTxt3, gbc);
	    p.add(lblTxt3);
        
		context =  "This is English Sentence." ;
		MLabel lblTxt4 = new MLabel( context );
        lblTxt4.setRotateDirection(MSwingRotateUtilities.ROTATE_VERTICAL);
        lblTxt4.setRotateHint(MSwingRotateUtilities.ROTATE_DEFAULT);
        lblTxt4.setFont(new Font(null, Font.PLAIN, 16));
	    gbc.gridx = col+3;
	    gbc.gridy = 0;
	    gbc.gridwidth = 1;
	    gbc.gridheight = 10;
	    layout.setConstraints(lblTxt4, gbc);
	    p.add(lblTxt4);

		context =  "ᠮᠠᠯ ᠰᠦᠷᠦᠭ ᠦᠨ ᠪᠣᠳᠠᠲᠠᠢ ᠨᠢᠭᠡ ᠣᠨᠴᠠᠯᠢᠭ ᠪᠣᠶᠣ  " ;
		MTextField txtField3 = new MTextField( context, 20 );
		txtField3.setRotateDirection(MSwingRotateUtilities.ROTATE_VERTICAL);
		txtField3.setRotateHint(MSwingRotateUtilities.ROTATE_LEFTTORIGHT);
		txtField3.setFont(new Font("Mongolian White", Font.PLAIN, 24));
		// txtField2.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
	    gbc.gridx = col+4;
	    gbc.gridy = 0;
	    gbc.gridwidth = 1;
	    gbc.gridheight = 10;
	    layout.setConstraints(txtField3, gbc);
	    p.add(txtField3);
        
	    
		context =  "ᠮᠠᠯ ᠰᠦᠷᠦᠭ ᠦᠨ ᠪᠣᠳᠠᠲᠠᠢ \nᠨᠢᠭᠡ ᠣᠨᠴᠠᠯᠢᠭ ᠪᠣᠶᠣ ᠮᠠᠯᠵᠢᠬᠤ ᠠᠮᠢᠳᠤᠷᠠᠯ  " ;
		MTextArea txtArea2 = new MTextArea( context, 5, 20 );
		txtArea2.setRotateDirection(MSwingRotateUtilities.ROTATE_VERTICAL);
		txtArea2.setRotateHint(MSwingRotateUtilities.ROTATE_LEFTTORIGHT);
		txtArea2.setFont(new Font("Mongolian White", Font.PLAIN, 24));
		txtArea2.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		JScrollPane scrollingArea = new JScrollPane(txtArea2);
		scrollingArea.getHorizontalScrollBar().setUnitIncrement(26);

	    gbc.gridx = col+5;
	    gbc.gridy = 0;
	    gbc.gridwidth = 1;
	    gbc.gridheight = 10;
	    layout.setConstraints(scrollingArea, gbc);
	    p.add(scrollingArea);
        
	    row++;
        MLabel lblTxt5 = new MLabel( "Japanese" );
        lblTxt5.setFont(new Font("MS明朝", Font.PLAIN, 16));
	    gbc.gridx = col;
	    gbc.gridy = row;
	    gbc.gridwidth = 1;
	    gbc.gridheight = 1;
	    layout.setConstraints(lblTxt5, gbc);
	    p.add(lblTxt5);

		context =  "日本語正しいですか。" ;
		MLabel lblTxt6 = new MLabel( context );
        lblTxt6.setRotateDirection(MSwingRotateUtilities.ROTATE_HORIZANTAL);
        lblTxt6.setRotateHint(MSwingRotateUtilities.ROTATE_RIGHTTOLEFT);
        lblTxt6.setFont(new Font("MS明朝", Font.PLAIN, 32));
	    gbc.gridx = col+1;
	    gbc.gridy = row;
	    gbc.gridwidth = 1;
	    gbc.gridheight = 1;
	    layout.setConstraints(lblTxt6, gbc);
	    p.add(lblTxt6);
        
	    row++;
	    MLabel lblTxt7 = new MLabel( "TextField" );
        lblTxt7.setFont(new Font("MS明朝", Font.PLAIN, 16));
	    gbc.gridx = col;
	    gbc.gridy = row;
	    gbc.gridwidth = 1;
	    gbc.gridheight = 1;
	    layout.setConstraints(lblTxt7, gbc);
	    p.add(lblTxt7);

		context =  "ᠮᠠᠯ ᠰᠦᠷᠦᠭ ᠦᠨ ᠪᠣᠳᠠᠲᠠᠢ ᠨᠢᠭᠡ ᠣᠨᠴᠠᠯᠢᠭ ᠪᠣᠶᠣ  " ;
		MTextField txtField1 = new MTextField( context, 29 );
		txtField1.setFont(new Font("Mongolian White", Font.PLAIN, 16));
		txtField1.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
	    gbc.gridx = col+1;
	    gbc.gridy = row;
	    gbc.gridwidth = 1;
	    gbc.gridheight = 1;
	    layout.setConstraints(txtField1, gbc);
	    p.add(txtField1);
	    
	    row++;
        lblTxt7 = new MLabel( "TextField" );
        lblTxt7.setFont(new Font("MS明朝", Font.PLAIN, 16));
	    gbc.gridx = col;
	    gbc.gridy = row;
	    gbc.gridwidth = 1;
	    gbc.gridheight = 1;
	    layout.setConstraints(lblTxt7, gbc);
	    p.add(lblTxt7);

		context =  "ᠮᠠᠯ ᠰᠦᠷᠦᠭ ᠦᠨ ᠪᠣᠳᠠᠲᠠᠢ  " ;
		MTextField txtField2 = new MTextField( context, 20 );
		txtField2.setFont(new Font("Mongolian White", Font.PLAIN, 24));
		// txtField2.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
	    gbc.gridx = col+1;
	    gbc.gridy = row;
	    gbc.gridwidth = 1;
	    gbc.gridheight = 1;
	    layout.setConstraints(txtField2, gbc);
	    p.add(txtField2);
        
	    row++;
        MLabel lblTxt8 = new MLabel( "TextArea" );
        lblTxt8.setFont(new Font("MS明朝", Font.PLAIN, 16));
	    gbc.gridx = col;
	    gbc.gridy = row;
	    gbc.gridwidth = 1;
	    gbc.gridheight = 1;
	    layout.setConstraints(lblTxt8, gbc);
	    p.add(lblTxt8);

		context =  "ᠮᠠᠯ ᠰᠦᠷᠦᠭ ᠦᠨ ᠪᠣᠳᠠᠲᠠᠢ ᠨᠢᠭᠡ ᠣᠨᠴᠠᠯᠢᠭ ᠪᠣᠶᠣ ᠮᠠᠯᠵᠢᠬᠤ ᠠᠮᠢᠳᠤᠷᠠᠯ  " ;
		MTextArea txtField4 = new MTextArea( context, 3, 20 );
		txtField4.setFont(new Font("Mongolian White", Font.PLAIN, 16));
		txtField4.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
	    gbc.gridx = col+1;
	    gbc.gridy = row;
	    gbc.gridwidth = 1;
	    gbc.gridheight = 1;
	    layout.setConstraints(txtField4, gbc);
	    p.add(txtField4);
	    
	    row++;
        MLabel lblTxt9 = new MLabel( "TextArea" );
        lblTxt9.setFont(new Font("MS明朝", Font.PLAIN, 16));
	    gbc.gridx = col;
	    gbc.gridy = row;
	    gbc.gridwidth = 1;
	    gbc.gridheight = 1;
	    layout.setConstraints(lblTxt9, gbc);
	    p.add(lblTxt9);

		context =  "ᠮᠠᠯ ᠰᠦᠷᠦᠭ ᠦᠨ ᠪᠣᠳᠠᠲᠠᠢ \nᠨᠢᠭᠡ ᠣᠨᠴᠠᠯᠢᠭ ᠪᠣᠶᠣ ᠮᠠᠯᠵᠢᠬᠤ ᠠᠮᠢᠳᠤᠷᠠᠯ  " ;
		MTextArea txtArea3 = new MTextArea( context, 5, 20 );
		txtArea3.setFont(new Font("Mongolian White", Font.PLAIN, 24));
		txtArea3.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		JScrollPane scrollingArea3 = new JScrollPane(txtArea3);

	    gbc.gridx = col+1;
	    gbc.gridy = row;
	    gbc.gridwidth = 1;
	    gbc.gridheight = 1;
	    layout.setConstraints(scrollingArea3, gbc);
	    p.add(scrollingArea3);
        
	    getContentPane().add(p, BorderLayout.CENTER);
        p.setSize(980, 780);

	}

}
