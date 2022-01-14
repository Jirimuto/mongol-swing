package com.mongol.demo;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

import com.mongol.encode.MongolianFontUtil;
import com.mongol.swing.MEditorPane;

/* 
 * TODO need to include liquid.jar for this demo
 */
public class JEditorPaneDemo extends JFrame {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/**
	 * @param args
	 */
	final public static void main(String[] args) {

		MongolianFontUtil.init();
		JEditorPaneDemo frame = new JEditorPaneDemo();

	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    // frame.setBounds(10, 10, 300, 200);
	    frame.setTitle("Mongolian Swing Component example");
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
		
	}
	
	public JEditorPaneDemo() {
		super();
		setLnF();
		
		String context =  "ᠮᠠᠯ ᠰᠦᠷᠦᠭ ᠦᠨ ᠪᠣᠳᠠᠲᠠᠢ ᠨᠢᠭᠡ ᠣᠨᠴᠠᠯᠢᠭ ᠪᠣᠶᠣ ᠮᠣᠯᠵᠢᠬᠤ ᠠᠮᠢᠳᠤᠷᠠᠯ  " ;
		
		BorderLayout layout = new BorderLayout();
	    JPanel p = new JPanel();
	    p.setLayout(layout);
	    p.setBorder(new EtchedBorder(EtchedBorder.LOWERED));

		context =  "ᠲᠤᠰ ᠦᠭᠦᠯᠡᠯ ᠦᠨ ᠤᠳᠤᠷᠢᠳᠭᠠᠯ ᠤᠨ ᠬᠡᠰᠡᠭ ᠲᠥ ᠰᠡᠳᠦᠪ ᠰᠤᠩᠭ᠋ᠤᠭᠰᠠᠨ ᠰᠢᠯᠲᠠᠭᠠᠨ᠂ " +
				"ᠠᠴᠢ ᠬᠣᠯᠪᠣᠭᠳᠠᠯ᠂ ᠵᠣᠷᠢᠯᠭ᠎ᠠ᠂ ᠠᠷᠭ᠎ᠠ ᠪᠠ ᠰᠣᠳᠤᠯᠤᠯ ᠤᠨ ᠲᠣᠢ᠌ᠮᠣ ᠶᠢ ᠲᠠᠨᠢᠯᠴᠠᠭᠤᠯᠪᠠ᠃ 日本語です。\n" +
				"ᠨᠢᠭᠡᠳᠦᠭᠡᠷ ᠪᠦᠯᠦᠭ ᠲᠥ᠂ ᠮᠣᠩᠭᠣᠯ ᠣᠨᠢᠰᠤᠭ᠎ᠠ ᠶᠢᠨ ᠡᠭᠦᠰᠦᠯ ᠦᠨ ᠱᠠᠲᠤ ᠦᠶ᠎ᠡ ᠨᠡᠯᠢᠶᠡᠳ ᠡᠷᠲᠡ ᠦᠶ᠎ᠡ ᠲᠠᠢ ᠬᠣᠯᠪᠣᠭᠳᠠᠬᠤ ᠪᠣᠯᠪᠠᠴᠣ ᠵᠢᠩᠭᠢᠨᠢ ᠲᠦᠯᠦᠪᠰᠢᠷᠡᠵᠦ ᠬᠦᠭᠵᠢᠭᠰᠡᠨ ᠨᠢ ᠮᠣᠩᠭ᠋ᠣᠯᠴᠣᠳ ᠤᠨ ᠮᠠᠯᠵᠢᠯ ᠠᠵᠤ ᠠᠬᠤᠢ ᠨᠡᠯᠢᠶᠡᠳ " +
				"ᠬᠦᠭ᠍ᠵᠢᠯᠲᠡ ᠲᠠᠢ ᠪᠣᠯᠤᠭᠰᠠᠨ ᠦᠶ᠎ᠡ ᠲᠠᠢ ᠬᠣᠯᠪᠣᠭᠳᠠᠨ᠎ᠠ ᠭᠡᠰᠡᠨ ᠢ ᠮᠣᠩᠭᠣᠯ ᠣᠨᠢᠰᠤᠭ᠎ᠠ ᠶᠢᠨ ᠠᠭᠤᠯᠭ᠎ᠠ ᠡᠴᠡ ᠬᠠᠢ᠌ᠪᠠ᠃ ᠮᠠᠯᠵᠢᠯ ᠰᠣᠶᠣᠯ ᠨᠢ ᠮᠣᠩᠭᠣᠯ ᠣᠨᠢᠰᠤᠭ᠎ᠠ ᠶᠢᠨ ᠲᠦᠯᠦᠪᠰᠢᠷᠡᠯ ᠢ ᠪᠣᠢ ᠪᠣᠯᠭᠠᠭᠰᠠᠨ ᠢᠶᠡᠷ ᠪᠠᠷᠠᠬᠤ ᠦᠭᠡᠢ ᠮᠣᠩᠭᠣᠯ ᠣᠨᠢᠰᠤᠭ᠎ᠠ ᠶᠢᠨ ᠵᠠᠨ " +
				"ᠦᠢᠯᠡ᠂ ᠰᠡᠳᠭᠢᠮᠵᠢ ᠶᠢ ᠢᠯᠡᠳᠲᠡ ᠨᠥᠯᠥᠭᠡᠯᠡᠵᠦ᠂ ᠰᠠᠭᠤᠷᠢ ᠪᠣᠯᠬᠤᠢ᠌ᠴᠠ ᠦᠢᠯᠡᠳᠦᠯ ᠭᠠᠷᠭᠠᠭᠰᠠᠨ ᠪᠠᠢ᠌ᠨ᠎ᠠ. ᠮᠠᠯ ᠰᠦᠷᠦᠭ ᠦᠨ ᠪᠣᠳᠠᠲᠠᠢ ᠨᠢᠭᠡ ᠣᠨᠴᠠᠯᠢᠭ ᠪᠣᠶᠣ ᠮᠠᠯᠵᠢᠬᠤ ᠠᠮᠢᠳᠤᠷᠠᠯ ᠤᠨ ᠠᠭᠤᠯᠭ᠎ᠠ ᠪᠠᠷ ᠢᠶᠡᠨ ᠶᠠᠮᠠᠷ ᠨᠢᠭᠡᠨ ᠶᠠᠭᠤᠮ᠎ᠠ ᠦᠵᠡᠭᠳᠡᠯ᠂ " +
				"ᠬᠦᠮᠦᠨ ᠠᠮᠢᠲᠠᠨ ᠢ ᠳᠦᠷᠰᠦᠯᠡᠳᠡᠭ ᠨᠢ ᠮᠣᠩᠭᠣᠯ ᠣᠨᠢᠰᠤᠭ᠎ᠠ ᠶᠢᠨ ᠰᠡᠳᠭᠢᠮᠵᠢ ᠶᠢᠨ ᠳᠣᠪᠤᠢ᠌ᠮ᠎ᠠ ᠣᠨᠴᠠᠯᠢᠭ ᠪᠣᠯᠵᠠᠢ᠃ ᠡᠳᠡᠭᠡᠷ ᠣᠨᠢᠰᠤᠭ᠎ᠠ ᠳᠤ ᠮᠣᠩᠭᠣᠯ ᠬᠦᠮᠦᠨ ᠦ ᠮᠠᠯ ᠤᠨᠤᠭ᠎ᠠ ᠳᠤ ᠪᠠᠨ ᠬᠠᠢ᠌ᠷᠠᠲᠠᠢ ᠪᠠᠢ᠌ᠳᠠᠭ ᠲᠦᠭᠡᠮᠡᠯ ᠪᠣᠳᠤᠯ ᠰᠡᠳᠭᠢᠯᠭᠡ ᠪᠠ ᠡᠬᠡ " +
				"ᠪᠠᠢ᠌ᠭᠠᠯᠢ ᠪᠠᠨ ᠳᠡᠭᠡᠳᠦᠯᠡᠵᠦ ᠰᠢᠳᠦᠳᠡᠭ ᠦᠵᠡᠯ ᠰᠡᠳᠭᠢᠴᠡ ᠴᠣᠬᠤᠯᠵᠠᠭᠰᠠᠨ ᠪᠠᠢ᠌ᠭ᠎ᠠ ᠶᠢ ᠣᠯᠵᠤ ᠬᠠᠷᠠᠨ᠎ᠠ᠃ ᠳᠠᠷᠤᠢ ᠮᠠᠯᠵᠢᠯ ᠰᠣᠶᠣᠯ ᠨᠢ ᠮᠣᠩᠭᠣᠯ ᠪᠠᠢ᠌ᠭᠠᠯᠢ ᠶᠢᠨ ᠦᠪᠡᠷᠮᠢᠴᠡ ᠳᠦᠷᠢ ᠲᠦᠷᠬᠦ ᠪᠠ ᠮᠣᠩᠭᠣᠯ ᠬᠦᠮᠦᠨ ᠦ ᠰᠠᠨᠠᠵᠤ ᠰᠡᠳᠭᠢᠬᠦ ᠣᠨᠴᠠᠯᠢᠭ ᠪᠠᠢ᠌ᠳᠠᠯ ᠢ " +
				"ᠨᠥᠬᠦᠴᠡᠯᠳᠦᠭᠦᠯᠦᠭᠰᠡᠨ ᠪᠠᠢ᠌ᠭ᠎ᠠ ᠶᠢ ᠮᠣᠩᠭᠣᠯ ᠣᠨᠢᠰᠤᠭ᠎ᠠ ᠨᠡᠯᠢᠶᠡᠳ ᠬᠡᠮᠵᠢᠶᠡᠨ᠎ᠡ ᠬᠠᠷᠠᠭᠤᠯᠵᠤ ᠳᠡᠢ᠌ᠯᠬᠦ ᠶᠢ ᠪᠣᠳᠠᠲᠠᠢ ᠣᠨᠢᠰᠤᠭ᠎ᠠ ᠪᠠᠷ ᠲᠠᠢ᠌ᠯᠪᠤᠷᠢᠯᠠᠪᠠ᠃" ;
		JEditorPane txtPane2 = new JEditorPane( "text/html", "");
//		JEditorPane txtPane2 = new JEditorPane( );
		txtPane2.putClientProperty(MEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
//		txtPane2.setFont( new Font("Dialog", Font.PLAIN, 20));
		txtPane2.setFont( new Font("Mongolian White", Font.PLAIN, 24));
		
		txtPane2.setText( context );
		txtPane2.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		JScrollPane scrollingArea2 = new JScrollPane(txtPane2);
	    p.add(scrollingArea2, BorderLayout.CENTER);
        	    
	    getContentPane().add(p, BorderLayout.CENTER);
        p.setSize(980, 680);
        
	}

	  /**  Set the Swing Look and Feel */
final private void setLnF()

	  {
	    try
	    {

	      UIManager.getInstalledLookAndFeels();
	      
	      try
	      {
	    	  // TODO need to include liquid.jar
	    	  UIManager.setLookAndFeel( "com.birosoft.liquid.LiquidLookAndFeel" );
	    	  // UIManager.setLookAndFeel( "javax.swing.plaf.metal.MetalLookAndFeel" );
	    	  // com.birosoft.liquid.LiquidLookAndFeel.setLiquidDecorations( false );
	          // DockingUISettings.getInstance().updateUI();
	          SwingUtilities.updateComponentTreeUI( this );
	    	
	      }
	      catch( final Exception e )
	      { 
	          UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
	      }
	      
	      
	    }
	    //  ToDo: log proper exception messages
	    catch( final ClassNotFoundException cnfe )
	    {  }
	    catch( final IllegalAccessException iae )
	    {  }
	    catch( final InstantiationException ie )
	    {  }
	    catch( final UnsupportedLookAndFeelException ulafe )
	    {  }
	  }
	  

}
