package com.mongol.demo;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

import com.mongol.swing.MLabel;
import com.mongol.swing.MSwingRotateUtilities;
import com.mongol.swing.MTextArea;

public class MTextAreaDemo extends JFrame {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		MTextAreaDemo frame = new MTextAreaDemo();

	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    // frame.setBounds(10, 10, 300, 200);
	    frame.setTitle("Mongolian Swing Component example");
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
		
	}
	
	public MTextAreaDemo() {
		
		String context =  "ᠮᠠᠯ ᠰᠦᠷᠦᠭ ᠦᠨ ᠪᠣᠳᠠᠲᠠᠢ ᠨᠢᠭᠡ ᠣᠨᠴᠠᠯᠢᠭ ᠪᠣᠶᠣ ᠮᠣᠯᠵᠢᠬᠤ ᠠᠮᠢᠳᠤᠷᠠᠯ  " ;
		
	    GridBagLayout layout = new GridBagLayout();
	    JPanel p = new JPanel();
	    p.setLayout(layout);
	    p.setBorder(new EtchedBorder(EtchedBorder.LOWERED));

	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.insets = new Insets(5, 5, 5, 5);

	    int line = 0;
        MLabel lblTxt0 = new MLabel( "Mongolian" );
        lblTxt0.setRotateDirection(MSwingRotateUtilities.ROTATE_VERTICAL);
        lblTxt0.setRotateHint(MSwingRotateUtilities.ROTATE_LEFTTORIGHT);
        lblTxt0.setFont(new Font("MS明朝", Font.PLAIN, 16));
	    gbc.gridx = line;
	    gbc.gridy = 0;
	    gbc.gridwidth = 1;
	    gbc.gridheight = 1;
	    layout.setConstraints(lblTxt0, gbc);
	    p.add(lblTxt0);

		context =  "ᠮᠠᠯ ᠰᠦᠷᠦᠭ ᠦᠨ ᠪᠣᠳᠠᠲᠠᠢ \nᠨᠢᠭᠡ ᠣᠨᠴᠠᠯᠢᠭ ᠪᠣᠶᠣ ᠮᠣᠯᠵᠢᠬᠤ ᠠᠮᠢᠳᠤᠷᠠᠯ  " ;
		MTextArea txtArea0 = new MTextArea( context, 10, 20 );
		txtArea0.setRotateDirection(MSwingRotateUtilities.ROTATE_HORIZANTAL);
		txtArea0.setRotateHint(MSwingRotateUtilities.ROTATE_LEFTTORIGHT);
		// txtArea0.setFont(new Font("Mongolian White", Font.PLAIN, 24));
		txtArea0.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		JScrollPane scrollingArea = new JScrollPane(txtArea0);
		scrollingArea.getHorizontalScrollBar().setUnitIncrement(26);

	    gbc.gridx = line;
	    gbc.gridy = 1;
	    gbc.gridwidth = 1;
	    gbc.gridheight = 1;
	    layout.setConstraints(scrollingArea, gbc);
	    p.add(scrollingArea);
                
	    line++;
        MLabel lblTxt8 = new MLabel( "Mongolian" );
        lblTxt8.setRotateDirection(MSwingRotateUtilities.ROTATE_HORIZANTAL);
        lblTxt8.setRotateHint(MSwingRotateUtilities.ROTATE_LEFTTORIGHT);
        lblTxt8.setFont(new Font("MS明朝", Font.PLAIN, 16));
	    gbc.gridx = line;
	    gbc.gridy = 0;
	    gbc.gridwidth = 1;
	    gbc.gridheight = 1;
	    layout.setConstraints(lblTxt8, gbc);
	    p.add(lblTxt8);

		context =  "ᠲᠤᠰ ᠦᠭᠦᠯᠡᠯ ᠦᠨ ᠤᠳᠤᠷᠢᠳᠭᠠᠯ ᠤᠨ ᠬᠡᠰᠡᠭ ᠲᠥ ᠰᠡᠳᠦᠪ ᠰᠤᠩᠭ᠋ᠤᠭᠰᠠᠨ ᠰᠢᠯᠲᠠᠭᠠᠨ᠂ \n " +
//				"《  ᠨᠢᠭᠡ 》᠂ ＜ ᠬᠣᠶᠠᠷ ＞᠂ 《 ᠭᠤᠷᠪᠠ 》᠂ 『 ᠳᠥᠷᠪᠡ 』᠂〖 ᠲᠠᠪᠤ 〗" + 
//				"《 一 》᠂ ＜ 二 ＞᠂ 《  三　》᠂ 『 四 』᠂〖 五 〗" + 
				"ᠠᠴᠢ ᠬᠣᠯᠪᠣᠭᠳᠠᠯ᠂ ᠵᠣᠷᠢᠯᠭ᠎ᠠ᠂ ᠠᠷᠭ᠎ᠠ ᠪᠠ ᠰᠣᠳᠤᠯᠤᠯ ᠤᠨ ᠲᠣᠢ᠌ᠮᠣ ᠶᠢ ᠲᠠᠨᠢᠯᠴᠠᠭᠤᠯᠪᠠ᠃ \n" +
				"ᠨᠢᠭᠡᠳᠦᠭᠡᠷ ᠪᠦᠯᠦᠭ ᠲᠥ᠂ ᠮᠣᠩᠭᠣᠯ ᠣᠨᠢᠰᠤᠭ᠎ᠠ ᠶᠢᠨ ᠡᠭᠦᠰᠦᠯ ᠦᠨ ᠱᠠᠲᠤ ᠦᠶ᠎ᠡ ᠨᠡᠯᠢᠶᠡᠳ ᠡᠷᠲᠡ ᠦᠶ᠎ᠡ ᠲᠠᠢ ᠬᠣᠯᠪᠣᠭᠳᠠᠬᠤ ᠪᠣᠯᠪᠠᠴᠣ ᠵᠢᠩᠭᠢᠨᠢ ᠲᠦᠯᠦᠪᠰᠢᠷᠡᠵᠦ ᠬᠦᠭ᠍ᠵᠢᠭᠰᠡᠨ ᠨᠢ ᠮᠣᠩᠭ᠋ᠣᠯᠴᠣᠳ ᠤᠨ ᠮᠠᠯᠵᠢᠯ ᠠᠵᠤ ᠠᠬᠤᠢ ᠨᠡᠯᠢᠶᠡᠳ " +
				"ᠬᠦᠭ᠍ᠵᠢᠯᠲᠡ ᠲᠠᠢ ᠪᠣᠯᠤᠭᠰᠠᠨ ᠦᠶ᠎ᠡ ᠲᠠᠢ ᠬᠣᠯᠪᠣᠭᠳᠠᠨ᠎ᠠ ᠭᠡᠰᠡᠨ ᠢ ᠮᠣᠩᠭᠣᠯ ᠣᠨᠢᠰᠤᠭ᠎ᠠ ᠶᠢᠨ ᠠᠭᠤᠯᠭ᠎ᠠ ᠡᠴᠡ ᠬᠠᠢ᠌ᠪᠠ᠃ ᠮᠠᠯᠵᠢᠯ ᠰᠣᠶᠣᠯ ᠨᠢ ᠮᠣᠩᠭᠣᠯ ᠣᠨᠢᠰᠤᠭ᠎ᠠ ᠶᠢᠨ ᠲᠦᠯᠦᠪᠰᠢᠷᠡᠯ ᠢ ᠪᠣᠢ ᠪᠣᠯᠭᠠᠭᠰᠠᠨ ᠢᠶᠡᠷ ᠪᠠᠷᠠᠬᠤ ᠦᠭᠡᠢ ᠮᠣᠩᠭᠣᠯ ᠣᠨᠢᠰᠤᠭ᠎ᠠ ᠶᠢᠨ ᠵᠠᠨ " +
				"ᠦᠢᠯᠡ᠂ ᠰᠡᠳᠭᠢᠮᠵᠢ ᠶᠢ ᠢᠯᠡᠳᠲᠡ ᠨᠥᠯᠥᠭᠡᠯᠡᠵᠦ᠂ ᠰᠠᠭᠤᠷᠢ ᠪᠣᠯᠬᠤᠢ᠌ᠴᠠ ᠦᠢᠯᠡᠳᠦᠯ ᠭᠠᠷᠭᠠᠭᠰᠠᠨ ᠪᠠᠢ᠌ᠨ᠎ᠠ. ᠮᠠᠯ ᠰᠦᠷᠦᠭ ᠦᠨ ᠪᠣᠳᠠᠲᠠᠢ ᠨᠢᠭᠡ ᠣᠨᠴᠠᠯᠢᠭ ᠪᠣᠶᠣ ᠮᠠᠯᠵᠢᠬᠤ ᠠᠮᠢᠳᠤᠷᠠᠯ ᠤᠨ ᠠᠭᠤᠯᠭ᠎ᠠ ᠪᠠᠷ ᠢᠶᠡᠨ ᠶᠠᠮᠠᠷ ᠨᠢᠭᠡᠨ ᠶᠠᠭᠤᠮ᠎ᠠ ᠦᠵᠡᠭᠳᠡᠯ᠂ " +
				"ᠬᠦᠮᠦᠨ ᠠᠮᠢᠲᠠᠨ ᠢ ᠳᠦᠷᠰᠦᠯᠡᠳᠡᠭ ᠨᠢ ᠮᠣᠩᠭᠣᠯ ᠣᠨᠢᠰᠤᠭ᠎ᠠ ᠶᠢᠨ ᠰᠡᠳᠭᠢᠮᠵᠢ ᠶᠢᠨ ᠳᠣᠪᠤᠢ᠌ᠮ᠎ᠠ ᠣᠨᠴᠠᠯᠢᠭ ᠪᠣᠯᠵᠠᠢ᠃ ᠡᠳᠡᠭᠡᠷ ᠣᠨᠢᠰᠤᠭ᠎ᠠ ᠳᠤ ᠮᠣᠩᠭᠣᠯ ᠬᠦᠮᠦᠨ ᠦ ᠮᠠᠯ ᠤᠨᠤᠭ᠎ᠠ ᠳᠤ ᠪᠠᠨ ᠬᠠᠢ᠌ᠷᠠᠲᠠᠢ ᠪᠠᠢ᠌ᠳᠠᠭ ᠲᠦᠭᠡᠮᠡᠯ ᠪᠣᠳᠤᠯ ᠰᠡᠳᠭᠢᠯᠭᠡ ᠪᠠ ᠡᠬᠡ " +
				"ᠪᠠᠢ᠌ᠭᠠᠯᠢ ᠪᠠᠨ ᠳᠡᠭᠡᠳᠦᠯᠡᠵᠦ ᠰᠢᠳᠦᠳᠡᠭ ᠦᠵᠡᠯ ᠰᠡᠳᠭᠢᠴᠡ ᠴᠣᠬᠤᠯᠵᠠᠭᠰᠠᠨ ᠪᠠᠢ᠌ᠭ᠎ᠠ ᠶᠢ ᠣᠯᠵᠤ ᠬᠠᠷᠠᠨ᠎ᠠ᠃ ᠳᠠᠷᠤᠢ ᠮᠠᠯᠵᠢᠯ ᠰᠣᠶᠣᠯ ᠨᠢ ᠮᠣᠩᠭᠣᠯ ᠪᠠᠢ᠌ᠭᠠᠯᠢ ᠶᠢᠨ ᠦᠪᠡᠷᠮᠢᠴᠡ ᠳᠦᠷᠢ ᠲᠦᠷᠬᠦ ᠪᠠ ᠮᠣᠩᠭᠣᠯ ᠬᠦᠮᠦᠨ ᠦ ᠰᠠᠨᠠᠵᠤ ᠰᠡᠳᠭᠢᠬᠦ ᠣᠨᠴᠠᠯᠢᠭ ᠪᠠᠢ᠌ᠳᠠᠯ ᠢ " +
				"ᠨᠥᠬᠦᠴᠡᠯᠳᠦᠭᠦᠯᠦᠭᠰᠡᠨ ᠪᠠᠢ᠌ᠭ᠎ᠠ ᠶᠢ ᠮᠣᠩᠭᠣᠯ ᠣᠨᠢᠰᠤᠭ᠎ᠠ ᠨᠡᠯᠢᠶᠡᠳ ᠬᠡᠮᠵᠢᠶᠡᠨ᠎ᠡ ᠬᠠᠷᠠᠭᠤᠯᠵᠤ ᠳᠡᠢ᠌ᠯᠬᠦ ᠶᠢ ᠪᠣᠳᠠᠲᠠᠢ ᠣᠨᠢᠰᠤᠭ᠎ᠠ ᠪᠠᠷ ᠲᠠᠢ᠌ᠯᠪᠤᠷᠢᠯᠠᠪᠠ᠃" ;
		MTextArea txtArea2 = new MTextArea( context, 15, 25 );
		txtArea2.setRotateDirection(MSwingRotateUtilities.ROTATE_VERTICAL);
		txtArea2.setRotateHint(MSwingRotateUtilities.ROTATE_LEFTTORIGHT);
//		txtArea2.setFont(new Font("MS 明朝", Font.PLAIN, 22));
		// txtArea2.setFont(new Font("Mongolian White", Font.PLAIN, 24));
		txtArea2.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		txtArea2.setLineWrap(true);
		txtArea2.setWrapStyleWord(true);
		JScrollPane scrollingArea2 = new JScrollPane(txtArea2);
		Dimension preferredSize = new Dimension(800, 400);
		// scrollingArea2.setPreferredSize(preferredSize);

	    gbc.gridx = line;
	    gbc.gridy = 1;
	    gbc.gridwidth = 1;
	    gbc.gridheight = 1;
	    layout.setConstraints(scrollingArea2, gbc);
	    p.add(scrollingArea2);
        
	    line++;
        MLabel lblTxt9 = new MLabel( "Mongolian" );
        lblTxt9.setRotateDirection(MSwingRotateUtilities.ROTATE_HORIZANTAL);
        lblTxt9.setRotateHint(MSwingRotateUtilities.ROTATE_LEFTTORIGHT);
        lblTxt9.setFont(new Font("MS明朝", Font.PLAIN, 16));
	    gbc.gridx = line;
	    gbc.gridy = 0;
	    gbc.gridwidth = 1;
	    gbc.gridheight = 1;
	    layout.setConstraints(lblTxt9, gbc);
	    p.add(lblTxt9);

		context =  "ᠲᠤᠰ ᠦᠭᠦᠯᠡᠯ ᠦᠨ ᠤᠳᠤᠷᠢᠳᠭᠠᠯ ᠤᠨ ᠬᠡᠰᠡᠭ ᠲᠥ ᠰᠡᠳᠦᠪ ᠰᠤᠩᠭ᠋ᠤᠭᠰᠠᠨ ᠰᠢᠯᠲᠠᠭᠠᠨ᠂ " +
				"ᠠᠴᠢ ᠬᠣᠯᠪᠣᠭᠳᠠᠯ᠂ ᠵᠣᠷᠢᠯᠭ᠎ᠠ᠂ ᠠᠷᠭ᠎ᠠ ᠪᠠ ᠰᠣᠳᠤᠯᠤᠯ ᠤᠨ ᠲᠣᠢ᠌ᠮᠣ ᠶᠢ ᠲᠠᠨᠢᠯᠴᠠᠭᠤᠯᠪᠠ᠃ " +
				"ᠨᠢᠭᠡᠳᠦᠭᠡᠷ ᠪᠦᠯᠦᠭ ᠲᠥ᠂ ᠮᠣᠩᠭᠣᠯ ᠣᠨᠢᠰᠤᠭ᠎ᠠ ᠶᠢᠨ ᠡᠭᠦᠰᠦᠯ ᠦᠨ ᠱᠠᠲᠤ ᠦᠶ᠎ᠡ ᠨᠡᠯᠢᠶᠡᠳ ᠡᠷᠲᠡ ᠦᠶ᠎ᠡ ᠲᠠᠢ ᠬᠣᠯᠪᠣᠭᠳᠠᠬᠤ ᠪᠣᠯᠪᠠᠴᠣ ᠵᠢᠩᠭᠢᠨᠢ ᠲᠦᠯᠦᠪᠰᠢᠷᠡᠵᠦ ᠬᠦᠭ᠍ᠵᠢᠭᠰᠡᠨ ᠨᠢ ᠮᠣᠩᠭ᠋ᠣᠯᠴᠣᠳ ᠤᠨ ᠮᠠᠯᠵᠢᠯ ᠠᠵᠤ ᠠᠬᠤᠢ ᠨᠡᠯᠢᠶᠡᠳ " +
				"ᠬᠦᠭ᠍ᠵᠢᠯᠲᠡ ᠲᠠᠢ ᠪᠣᠯᠤᠭᠰᠠᠨ ᠦᠶ᠎ᠡ ᠲᠠᠢ ᠬᠣᠯᠪᠣᠭᠳᠠᠨ᠎ᠠ ᠭᠡᠰᠡᠨ ᠢ ᠮᠣᠩᠭᠣᠯ ᠣᠨᠢᠰᠤᠭ᠎ᠠ ᠶᠢᠨ ᠠᠭᠤᠯᠭ᠎ᠠ ᠡᠴᠡ ᠬᠠᠢ᠌ᠪᠠ᠃ ᠮᠠᠯᠵᠢᠯ ᠰᠣᠶᠣᠯ ᠨᠢ ᠮᠣᠩᠭᠣᠯ ᠣᠨᠢᠰᠤᠭ᠎ᠠ ᠶᠢᠨ ᠲᠦᠯᠦᠪᠰᠢᠷᠡᠯ ᠢ ᠪᠣᠢ ᠪᠣᠯᠭᠠᠭᠰᠠᠨ ᠢᠶᠡᠷ ᠪᠠᠷᠠᠬᠤ ᠦᠭᠡᠢ ᠮᠣᠩᠭᠣᠯ ᠣᠨᠢᠰᠤᠭ᠎ᠠ ᠶᠢᠨ ᠵᠠᠨ " +
				"ᠦᠢᠯᠡ᠂ ᠰᠡᠳᠭᠢᠮᠵᠢ ᠶᠢ ᠢᠯᠡᠳᠲᠡ ᠨᠥᠯᠥᠭᠡᠯᠡᠵᠦ᠂ ᠰᠠᠭᠤᠷᠢ ᠪᠣᠯᠬᠤᠢ᠌ᠴᠠ ᠦᠢᠯᠡᠳᠦᠯ ᠭᠠᠷᠭᠠᠭᠰᠠᠨ ᠪᠠᠢ᠌ᠨ᠎ᠠ. ᠮᠠᠯ ᠰᠦᠷᠦᠭ ᠦᠨ ᠪᠣᠳᠠᠲᠠᠢ ᠨᠢᠭᠡ ᠣᠨᠴᠠᠯᠢᠭ ᠪᠣᠶᠣ ᠮᠠᠯᠵᠢᠬᠤ ᠠᠮᠢᠳᠤᠷᠠᠯ ᠤᠨ ᠠᠭᠤᠯᠭ᠎ᠠ ᠪᠠᠷ ᠢᠶᠡᠨ ᠶᠠᠮᠠᠷ ᠨᠢᠭᠡᠨ ᠶᠠᠭᠤᠮ᠎ᠠ ᠦᠵᠡᠭᠳᠡᠯ᠂ " +
				"ᠬᠦᠮᠦᠨ ᠠᠮᠢᠲᠠᠨ ᠢ ᠳᠦᠷᠰᠦᠯᠡᠳᠡᠭ ᠨᠢ ᠮᠣᠩᠭᠣᠯ ᠣᠨᠢᠰᠤᠭ᠎ᠠ ᠶᠢᠨ ᠰᠡᠳᠭᠢᠮᠵᠢ ᠶᠢᠨ ᠳᠣᠪᠤᠢ᠌ᠮ᠎ᠠ ᠣᠨᠴᠠᠯᠢᠭ ᠪᠣᠯᠵᠠᠢ᠃ ᠡᠳᠡᠭᠡᠷ ᠣᠨᠢᠰᠤᠭ᠎ᠠ ᠳᠤ ᠮᠣᠩᠭᠣᠯ ᠬᠦᠮᠦᠨ ᠦ ᠮᠠᠯ ᠤᠨᠤᠭ᠎ᠠ ᠳᠤ ᠪᠠᠨ ᠬᠠᠢ᠌ᠷᠠᠲᠠᠢ ᠪᠠᠢ᠌ᠳᠠᠭ ᠲᠦᠭᠡᠮᠡᠯ ᠪᠣᠳᠤᠯ ᠰᠡᠳᠭᠢᠯᠭᠡ ᠪᠠ ᠡᠬᠡ " +
				"ᠪᠠᠢ᠌ᠭᠠᠯᠢ ᠪᠠᠨ ᠳᠡᠭᠡᠳᠦᠯᠡᠵᠦ ᠰᠢᠳᠦᠳᠡᠭ ᠦᠵᠡᠯ ᠰᠡᠳᠭᠢᠴᠡ ᠴᠣᠬᠤᠯᠵᠠᠭᠰᠠᠨ ᠪᠠᠢ᠌ᠭ᠎ᠠ ᠶᠢ ᠣᠯᠵᠤ ᠬᠠᠷᠠᠨ᠎ᠠ᠃ ᠳᠠᠷᠤᠢ ᠮᠠᠯᠵᠢᠯ ᠰᠣᠶᠣᠯ ᠨᠢ ᠮᠣᠩᠭᠣᠯ ᠪᠠᠢ᠌ᠭᠠᠯᠢ ᠶᠢᠨ ᠦᠪᠡᠷᠮᠢᠴᠡ ᠳᠦᠷᠢ ᠲᠦᠷᠬᠦ ᠪᠠ ᠮᠣᠩᠭᠣᠯ ᠬᠦᠮᠦᠨ ᠦ ᠰᠠᠨᠠᠵᠤ ᠰᠡᠳᠭᠢᠬᠦ ᠣᠨᠴᠠᠯᠢᠭ ᠪᠠᠢ᠌ᠳᠠᠯ ᠢ " +
				"ᠨᠥᠬᠦᠴᠡᠯᠳᠦᠭᠦᠯᠦᠭᠰᠡᠨ ᠪᠠᠢ᠌ᠭ᠎ᠠ ᠶᠢ ᠮᠣᠩᠭᠣᠯ ᠣᠨᠢᠰᠤᠭ᠎ᠠ ᠨᠡᠯᠢᠶᠡᠳ ᠬᠡᠮᠵᠢᠶᠡᠨ᠎ᠡ ᠬᠠᠷᠠᠭᠤᠯᠵᠤ ᠳᠡᠢ᠌ᠯᠬᠦ ᠶᠢ ᠪᠣᠳᠠᠲᠠᠢ ᠣᠨᠢᠰᠤᠭ᠎ᠠ ᠪᠠᠷ ᠲᠠᠢ᠌ᠯᠪᠤᠷᠢᠯᠠᠪᠠ᠃" ;
		MTextArea txtArea3 = new MTextArea( context, 15, 25 );
		txtArea3.setRotateDirection(MSwingRotateUtilities.ROTATE_VERTICAL);
		txtArea3.setRotateHint(MSwingRotateUtilities.ROTATE_RIGHTTOLEFT);
		// txtArea3.setFont(new Font("Mongolian White", Font.PLAIN, 24));
		txtArea3.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		txtArea3.setLineWrap(true);
		txtArea3.setWrapStyleWord(true);
		JScrollPane scrollingArea3 = new JScrollPane(txtArea3);
		// Dimension preferredSize = new Dimension(800, 400);
		// scrollingArea2.setPreferredSize(preferredSize);

	    gbc.gridx = line;
	    gbc.gridy = 1;
	    gbc.gridwidth = 1;
	    gbc.gridheight = 1;
	    layout.setConstraints(scrollingArea3, gbc);
	    p.add(scrollingArea3);
	    
	    getContentPane().add(p, BorderLayout.CENTER);
        p.setSize(980, 680);

	}

}
