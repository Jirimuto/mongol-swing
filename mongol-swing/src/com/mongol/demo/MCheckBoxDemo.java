package com.mongol.demo;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.mongol.swing.MCheckBox;
import com.mongol.swing.MSwingRotateUtilities;

public class MCheckBoxDemo extends JPanel {

    //Four accessory choices provide for 16 different combinations
	MCheckBox jcbChin;
    MCheckBox jcbGlasses;
    MCheckBox jcbHair;
    MCheckBox jcbTeeth;

 /* The image for each combination is contained in a
     separate image file whose name indicates the accessories.
     The filenames are "geek-XXXX.gif" where XXXX can be one
     * of the following 16 choices.
     */

    StringBuffer choices;
    JLabel jlbPicture;
    CheckBoxListener myListener = null;

    public MCheckBoxDemo() {

    	// Add an item listener for each of the check boxes.
    	// This is the listener class which contains business logic
        myListener = new CheckBoxListener();

        // Create check boxes with default selection true 

        jcbChin = new MCheckBox("ᠮᠠᠯ ᠰᠦᠷᠦᠭ᠌　①");
        jcbChin.setFont(new Font("Mongolian White", Font.PLAIN, 24));
        jcbChin.setRotateDirection(MSwingRotateUtilities.ROTATE_VERTICAL);
        jcbChin.setRotateHint(MSwingRotateUtilities.ROTATE_LEFTTORIGHT);
        jcbChin.setMnemonic(KeyEvent.VK_C);	 	//Alt+C Checks/Unchecks the check Box
        jcbChin.setSelected(true);
        jcbChin.addItemListener(myListener);

        jcbGlasses = new MCheckBox("ᠪᠣᠳᠠᠲᠠᠢ　②");
        jcbGlasses.setFont(new Font("Mongolian White", Font.PLAIN, 24));
        jcbGlasses.setRotateDirection(MSwingRotateUtilities.ROTATE_VERTICAL);
        jcbGlasses.setRotateHint(MSwingRotateUtilities.ROTATE_LEFTTORIGHT);
        jcbGlasses.setMnemonic(KeyEvent.VK_G); 	 	//Alt+G Checks/Unchecks the check Box
        jcbGlasses.setSelected(true);
        jcbGlasses.addItemListener(myListener);

        jcbHair = new MCheckBox("ᠣᠨᠴᠠᠯᠢᠭ　③");
        jcbHair.setFont(new Font("Mongolian White", Font.PLAIN, 24));
        jcbHair.setRotateDirection(MSwingRotateUtilities.ROTATE_VERTICAL);
        jcbHair.setRotateHint(MSwingRotateUtilities.ROTATE_LEFTTORIGHT);
        jcbHair.setMnemonic(KeyEvent.VK_H); 	 	//Alt+H Checks/Unchecks the check Box
        jcbHair.setSelected(true);
        jcbHair.addItemListener(myListener);

        jcbTeeth = new MCheckBox("ᠠᠮᠢᠳᠤᠷᠠᠯ　④");
        jcbTeeth.setFont(new Font("Mongolian White", Font.PLAIN, 24));
        jcbTeeth.setRotateDirection(MSwingRotateUtilities.ROTATE_VERTICAL);
        jcbTeeth.setRotateHint(MSwingRotateUtilities.ROTATE_LEFTTORIGHT);
        jcbTeeth.setMnemonic(KeyEvent.VK_T); 		 	//Alt+T Checks/Unchecks the check Box
        jcbTeeth.setSelected(true);
        jcbTeeth.addItemListener(myListener);

        // Indicates what's on the geek.
        choices = new StringBuffer("cght");//Default Image has all the parts.

        // Set up the picture label
        jlbPicture = new JLabel(new ImageIcon("geek-" +
			choices.toString().trim() + ".gif"));
        jlbPicture.setToolTipText(choices.toString().trim());

        // Put the check boxes in a column in a panel
        JPanel jplCheckBox = new JPanel();
        jplCheckBox.setLayout(new GridLayout(1, 0));	//0 rows, 1 Column
        jplCheckBox.add(jcbChin);
        jplCheckBox.add(jcbGlasses);
        jplCheckBox.add(jcbHair);
        jplCheckBox.add(jcbTeeth);

        setLayout(new BorderLayout());
        add(jplCheckBox, BorderLayout.WEST);
        add(jlbPicture, BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
    }

    //Listens to the check boxes events
    class CheckBoxListener implements ItemListener {
        public void itemStateChanged(ItemEvent e) {
            int index = 0;
            char c = '-';
            Object source = e.getSource();
            if (source == jcbChin) {
                index = 0;
                c = 'c';
            } else if (source == jcbGlasses) {
                index = 1;
                c = 'g';
            } else if (source == jcbHair) {
                index = 2;
                c = 'h';
            } else if (source == jcbTeeth) {
                index = 3;
                c = 't';
            }

            if (e.getStateChange() == ItemEvent.DESELECTED)
                c = '-';

            choices.setCharAt(index, c);
            jlbPicture.setIcon(new ImageIcon("geek-"
		+ choices.toString().trim() + ".gif"));
            jlbPicture.setToolTipText(choices.toString());
        }
    }

    public static void main(String s[]) {
         JFrame frame = new JFrame("JCheckBox Usage Demo");
         frame.addWindowListener(new WindowAdapter() {
             public void windowClosing(WindowEvent e) {
                 System.exit(0);
             }
         });

         frame.setContentPane(new MCheckBoxDemo());
         frame.pack();
         frame.setVisible(true);
    }

}