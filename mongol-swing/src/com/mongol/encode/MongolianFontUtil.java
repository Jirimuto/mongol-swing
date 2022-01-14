package com.mongol.encode;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

public class MongolianFontUtil {

	private static final Map<String, Font> logicalFont = new HashMap<String, Font>();
	private static boolean initialized = false;
	private static final int font_size_adjust = 2;
	
	
    public static Font getAltFont(Font font, String uiname) {
        Font alt_font = null;
        if( font != null ){
	        if( font instanceof FontUIResource) {
	        	if( isMongolianFont(font) ) {
	                alt_font = UIManager.getFont(uiname);	// uiname="TextField.font"
	                int size = font.getSize();
	                if( size >= 10 )
	                	size -= font_size_adjust;
	                alt_font = alt_font.deriveFont(font.getStyle(), size);
	        	} else {
		        	FontUIResource resourcefont = (FontUIResource)font;
		        	String family = resourcefont.getFamily();
		        	alt_font = getFont(family);
		        	if( alt_font == null )
		            	alt_font = getFont("Dialog");
	                int size = font.getSize() + font_size_adjust;
	                alt_font = alt_font.deriveFont(font.getStyle(), size);
	        	}
	        } else {
	        	if( isMongolianFont(font) ) {
	        		if( uiname == null )
	        			uiname = "TextField.font";
	                alt_font = UIManager.getFont(uiname);	// uiname="TextField.font"
	                int size = font.getSize();
	                if( size >= 10 )
	                	size -= font_size_adjust;
	                if( alt_font != null )
	                	alt_font = alt_font.deriveFont(font.getStyle(), size);
	                else
	                	alt_font = font;
	        	} else {
	            	alt_font = getFont("Dialog");
	                int size = font.getSize() + font_size_adjust;
	                alt_font = alt_font.deriveFont(font.getStyle(), size);
	        	}
	        }
	    } else {
        	alt_font = getFont("Dialog");
            int size = font.getSize() + font_size_adjust;
            alt_font = alt_font.deriveFont(font.getStyle(), size);
	    }
        return alt_font;
    }
    
    public static boolean isMongolianFont(Font font) {
    	
        if( font != null && !Font.DIALOG.equalsIgnoreCase(font.getName()) 
        		&& !Font.DIALOG_INPUT.equalsIgnoreCase(font.getName()) 
        		&& !Font.SERIF.equalsIgnoreCase(font.getName()) 
        		&& !Font.SANS_SERIF.equalsIgnoreCase(font.getName()) 
        		&& !Font.MONOSPACED.equalsIgnoreCase(font.getName()) ) {
        	for( char c=MCodeConstant.UNI1820_A; c<=MCodeConstant.UNI1842_CHI; c++){
        		if( !font.canDisplay(c) )
        			return false;
        	}
        	// Jirimuto comment out --2020/07/17
        	// for( char c=MCodeConstant.UNIE820_A_ISOL; c<=MCodeConstant.UNIE85F_UE_ISOL_VAR1; c++){
        	// 	if( !font.canDisplay(c) )
        	// 		return false;
        	// }

        	return true;
        }
        return false;
    }
    
	public static void init() {
		if( initialized != true ){
			MongolianFontUtil util = new MongolianFontUtil();
			ClassLoader loader = MongolianFontUtil.class.getClassLoader();
			
			// Mongolian Universal White Font Registration
			String mnglUniversalWhite = "com/mongol/encode/font/MongolianUniversalWhite.ttf";
			URL url = loader.getResource(mnglUniversalWhite);
			// System.out.println("URL=" + url.toString());
			Font font = makeFont(url);
			// System.out.println("Font Name=" + font.getName());
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
			
			// Mongolian White Universal Tester Font Registration
			String mnglUniversalTester = "com/mongol/encode/font/MongolianUniversalTest.ttf";
			url = loader.getResource(mnglUniversalTester);
			font = makeFont(url);
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
						
			// Mongolian Art Font Registration
			String mnglArt = "com/mongol/encode/font/MongolianArt.ttf";
			url = loader.getResource(mnglArt);
			font = makeFont(url);
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
			
			// Mongolian Dark Font Registration
			String mnglDark = "com/mongol/encode/font/MongolianDark.ttf";
			url = loader.getResource(mnglDark);
			font = makeFont(url);
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
			
			// Mongolian Hand Writing Font Registration
			String mnglHandwriting = "com/mongol/encode/font/MongolianHandWriting.ttf";
			url = loader.getResource(mnglHandwriting);
			font = makeFont(url);
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
			
			// Mongolian Sonin Font Registration
			String mnglSonin = "com/mongol/encode/font/MongolianSonin.ttf";
			url = loader.getResource(mnglSonin);
			font = makeFont(url);
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
			
			// Mongolian Title Font Registration
			String mnglTitle = "com/mongol/encode/font/MongolianTitle.ttf";
			url = loader.getResource(mnglTitle);
			font = makeFont(url);
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);

			// Mongolian Utasu Font Registration
			String mnglUtasu = "com/mongol/encode/font/MongolianUtasu.ttf";
			url = loader.getResource(mnglUtasu);
			font = makeFont(url);
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
			
			String mnglWhite = "com/mongol/encode/font/MongolianWhite.ttf";
			url = loader.getResource(mnglWhite);
			font = makeFont(url);
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
			
			// Mongolian White Tester Font Registration
			String mnglTester = "com/mongol/encode/font/MongolianWhiteTester.ttf";
			url = loader.getResource(mnglTester);
			font = makeFont(url);
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
						
			// Mongolian Writing Font Registration
			String mnglWriting = "com/mongol/encode/font/MongolianWriting.ttf";
			url = loader.getResource(mnglWriting);
			font = makeFont(url);
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
			
			// Mongolian ZS Font Registration
			String mnglZS = "com/mongol/encode/font/MongolianZS.ttf";
			url = loader.getResource(mnglZS);
			font = makeFont(url);
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
			
			// Mongolian ZX Font Registration
			String mnglZX = "com/mongol/encode/font/MongolianZX.ttf";
			url = loader.getResource(mnglZX);
			font = makeFont(url);
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
			
			String resource = "com.mongol.encode.Font";
			registerFont(resource);
			initialized = true;
		}
	}
	
    public static Font getFont(String family) {
    	if( initialized != true )
    		init();
        Font font = logicalFont.get(family.toUpperCase());
        return font;
    }
    
	public static void registerFont(String resource ){
		if( resource != null ){
			ResourceBundle rb = ResourceBundle.getBundle(resource);

			for (Enumeration e = rb.getKeys(); e.hasMoreElements();) {
				String family = (String) e.nextElement();
				String fontname = rb.getString(family);
				Font font = new Font(fontname, Font.PLAIN, 16);
				if( family!=null && font != null ){
					logicalFont.put(family.toUpperCase(), font);
				}
			}
		}
	}
	
	public static void registerFont(String family, Font font){
		if( family != null ){
			logicalFont.put(family.toUpperCase(), font);
		}
	}
	
    private static Font makeFont(URL url) {
        Font font = null;
        InputStream is = null;
        try{
            is = url.openStream();
            font = (Font.createFont(Font.TRUETYPE_FONT, is)).deriveFont(16.0f);
            is.close();            
        }catch(IOException ioe) {
            ioe.printStackTrace();
        }catch(FontFormatException ffe) {
            ffe.printStackTrace();
        }finally{
            if(is!=null) {
                try{
                    is.close();
                }catch(IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return font;
    }
}
