package com.mongol.demo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.*; 

import com.mongol.swing.MComboBox;
import com.mongol.swing.MLabel;
import com.mongol.swing.MSwingRotateUtilities;

public class MComboBoxDemo extends JFrame { 

    private MComboBox comboBox; 
    private MLabel lbl; 

    public MComboBoxDemo() { 

        comboBox = new MComboBox(); 
        
        comboBox.setRotateDirection(MSwingRotateUtilities.ROTATE_VERTICAL);
        comboBox.setRotateHint(MSwingRotateUtilities.ROTATE_RIGHTTOLEFT);
        comboBox.setFont(new Font("Mongolian White", Font.PLAIN, 20));
        
        comboBox.addItem("ItemA ᠨᠢᠭᠡᠳᠦᠭᠡᠷ　第一"); 
        comboBox.addItem("ItemB ᠬᠣᠶᠠᠳᠤᠭᠠᠷ　第二"); 
        comboBox.addItem("ItemC ᠭᠤᠷᠪᠠᠳᠤᠭᠠᠷ　第三"); 
        comboBox.addItem("ItemD ᠳᠥᠷᠪᠡᠳᠦᠭᠡᠷ　第四"); 
        comboBox.addItem("ItemE ᠲᠠᠪᠤᠳᠤᠭᠠᠷ　第五"); 
        comboBox.addItem("ItemF ᠵᠢᠷᠭᠤᠳᠤᠭᠠᠷ　第六"); 
        comboBox.addItem("ItemG ᠳᠣᠯᠤᠳᠤᠭᠠᠷ　第七"); 
        comboBox.addItem("ItemH ᠨᠠᠢ᠍ᠮᠠᠳᠤᠭᠠᠷ　第八"); 
        comboBox.addItem("ItemI ᠶᠢᠰᠦᠳᠦᠭᠡᠷ　第九"); 
        comboBox.addItem("ItemJ ᠠᠷᠪᠠᠳᠤᠭᠠᠷ　第十"); 
        comboBox.addItem("ItemK ᠠᠷᠪᠠᠨ ᠨᠢᠭᠡᠳᠦᠭᠡᠷ　十一"); 
        comboBox.addItem("ItemL ᠠᠷᠪᠠᠨ ᠬᠣᠶᠠᠳᠤᠭᠠᠷ　十二"); 
        comboBox.addItem("ItemM ᠠᠷᠪᠠᠨ ᠭᠤᠷᠪᠠᠳᠤᠭᠠᠷ　十三"); 

        Dimension size = new Dimension(24, 360);
        comboBox.setPreferredSize(size);

        lbl = new MLabel(); 
        lbl.setText(""); 
        lbl.setFont(new Font("Mongolian White", Font.PLAIN, 20));

        comboBox.addActionListener(new java.awt.event.ActionListener() { 
            public void actionPerformed(java.awt.event.ActionEvent evt) { 
                comboBoxMouseClicked(evt); 
            } 
        }); 

        getContentPane().add(comboBox, BorderLayout.NORTH); 
        getContentPane().add(lbl, BorderLayout.SOUTH); 

        getContentPane().setLayout(new java.awt.FlowLayout()); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        setTitle("ComboBox"); 
        setSize(560, 400); 
    } 

    private void comboBoxMouseClicked(java.awt.event.ActionEvent evt) { 
        lbl.setText(String.valueOf(comboBox.getSelectedItem()) + " was selected"); 
    } 

    public static void main(String[] args) { 
        java.awt.EventQueue.invokeLater(new Runnable() { 
            public void run() { 
                new MComboBoxDemo().setVisible(true); 
            } 
        }); 
    } 
} 
