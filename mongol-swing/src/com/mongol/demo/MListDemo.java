package com.mongol.demo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.mongol.swing.MList;
import com.mongol.swing.MSwingRotateUtilities;

public class MListDemo extends JPanel {

  MList list;

  DefaultListModel model;

  int counter = 15;

  public MListDemo() {
    setLayout(new BorderLayout());
    model = new DefaultListModel();
    list = new MList(model);
    
    list.setRotateDirection(MSwingRotateUtilities.ROTATE_VERTICAL);
    list.setRotateHint(MSwingRotateUtilities.ROTATE_RIGHTTOLEFT);
    list.setFont(new Font("Mongolian White", Font.PLAIN, 20));
    Color fg = new Color( 255, 128, 128);
    list.setSelectionForeground(fg);
    
    JScrollPane pane = new JScrollPane(list);
    JButton addButton = new JButton("Add Element");
    JButton removeButton = new JButton("Remove Element");
    for (int i = 0; i < 15; i++)
      model.addElement("ᠮᠤᠩᠭᠤᠯ Element  番号：" + i);

    addButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        model.addElement("ᠮᠤᠩᠭᠤᠯ Element 番号：" + counter);
        counter++;
      }
    });
    removeButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (model.getSize() > 0)
          model.removeElementAt(0);
      }
    });

    add(pane, BorderLayout.NORTH);

    add(addButton, BorderLayout.WEST);
    add(removeButton, BorderLayout.EAST);
  }

  public static void main(String s[]) {
    JFrame frame = new JFrame("List Model Example");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setContentPane(new MListDemo());
    frame.setSize(560, 460);
    frame.setVisible(true);
  }
}