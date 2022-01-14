package com.mongol.swing.plaf;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;

import javax.swing.ComboBoxEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;

import com.mongol.swing.MDefaultComboBoxEditor;
import com.mongol.swing.MDefaultListCellRenderer;
import com.mongol.swing.MSwingRotateUtilities;

public class MComboBoxUI extends BasicComboBoxUI implements MRotation {

    private int rotate_direction = MSwingRotateUtilities.ROTATE_HORIZANTAL;
    private int rotate_hint = MSwingRotateUtilities.ROTATE_DEFAULT;
    
    private static final String IS_TABLE_CELL_EDITOR = "JComboBox.isTableCellEditor";
    
    private boolean squareButton = true;
    
    /**
     * Creates a UI for a JTextField.
     *
     * @param c the text field
     * @return the UI
     */
    public static ComponentUI createUI(JComponent c) {
        return new MComboBoxUI();
    }

    public MComboBoxUI() {
        super();
    }
    
    public void installUI( JComponent c ) {
    	
    	super.installUI( c );
        popup = createMPopup();
        listBox = popup.getList();

        if ( comboBox.getRenderer() == null || comboBox.getRenderer() instanceof UIResource ) {
            comboBox.setRenderer( createRenderer() );
        }

        if ( comboBox.getEditor() == null || comboBox.getEditor() instanceof UIResource ) {
            comboBox.setEditor( createEditor() );
        }

        installListeners();
        installComponents();

        comboBox.setLayout( createLayoutManager() );

        comboBox.setRequestFocusEnabled( true );

        installKeyboardActions();
    
	}
    
    protected ComboBoxEditor createEditor() {
        return new MDefaultComboBoxEditor.UIResource();
    }
    
    protected ListCellRenderer createRenderer() {
        return new MDefaultListCellRenderer();
    }

    
    protected void installDefaults() {
    	super.installDefaults();
    	
        //NOTE: this needs to default to true if not specified
        Boolean b = (Boolean)UIManager.get("ComboBox.squareButton");
        squareButton = b == null ? true : b;
        
        comboBox.setFont(new Font("Mongolian White", Font.PLAIN, 18));
    }

    protected MComboPopup createMPopup() {
        return new MComboPopup( comboBox );
    }

    
    protected LayoutManager createLayoutManager() {
    	
    		return new ComboBoxLayoutManager();
    		
    }

    protected JButton createArrowButton() {
    	
        if( rotate_direction == MSwingRotateUtilities.ROTATE_HORIZANTAL ) {
	
	        JButton button = new BasicArrowButton(BasicArrowButton.SOUTH,
					    UIManager.getColor("ComboBox.buttonBackground"),
					    UIManager.getColor("ComboBox.buttonShadow"),
					    UIManager.getColor("ComboBox.buttonDarkShadow"),
					    UIManager.getColor("ComboBox.buttonHighlight"));
	        button.setName("ComboBox.arrowButton");
	        return button;
	        
        } else {
        	
	        JButton button = new BasicArrowButton(BasicArrowButton.EAST,
					    UIManager.getColor("ComboBox.buttonBackground"),
					    UIManager.getColor("ComboBox.buttonShadow"),
					    UIManager.getColor("ComboBox.buttonDarkShadow"),
					    UIManager.getColor("ComboBox.buttonHighlight"));
	        button.setName("ComboBox.arrowButton");
	        return button;
	        
        }
    }


    

    public void setRotateHint(int hint) {
        this.rotate_hint = hint;
        if( listBox instanceof MRotation ) {
        	((MRotation)listBox).setRotateHint(hint);
        }
        	
    }

    public int getRotateHint(){
        return this.rotate_hint;
    }

    public void setRotateDirection(int direction){
    	boolean changed = rotate_direction != direction;
        this.rotate_direction = direction;
        if( listBox instanceof MRotation ) {
        	((MRotation)listBox).setRotateDirection(direction);
        }
        if( changed ) {
        	uninstallComponents() ;
        	installComponents() ;
        }
        
    }

    public int getRotateDirection(){
        return this.rotate_direction;
    }
    
    
    public class ComboBoxLayoutManager implements LayoutManager {
    	
        public void addLayoutComponent(String name, Component comp) {}

        public void removeLayoutComponent(Component comp) {}

        public Dimension preferredLayoutSize(Container parent) {
            return parent.getPreferredSize();
        }

        public Dimension minimumLayoutSize(Container parent) {
            return parent.getMinimumSize();
        }

        public void layoutContainer(Container parent) {
            JComboBox cb = (JComboBox)parent;
            if( rotate_direction == MSwingRotateUtilities.ROTATE_HORIZANTAL ) {
            	
                int width = cb.getWidth();
                int height = cb.getHeight();

	            Insets insets = getInsets();
	            int buttonHeight = height - (insets.top + insets.bottom);
	            int buttonWidth = buttonHeight;
	            if (arrowButton != null) {
	                Insets arrowInsets = arrowButton.getInsets();
	                buttonWidth = squareButton ? 
	                    buttonHeight : 
	                    arrowButton.getPreferredSize().width + arrowInsets.left + arrowInsets.right;
	            }
	            Rectangle cvb;
	
	            if ( arrowButton != null ) {
			        if(isLeftToRight(cb)) {
				    arrowButton.setBounds( width - (insets.right + buttonWidth),
							   insets.top,
							   buttonWidth, buttonHeight);
					}
					else {
					    arrowButton.setBounds( insets.left, insets.top,
								   buttonWidth, buttonHeight);
					}
	            }
	            if ( editor != null ) {
	                cvb = rectangleForCurrentValue();
	                editor.setBounds(cvb);
	            }
	            
            } else {
            	
                int width = cb.getWidth();
                int height = cb.getHeight();

	            Insets insets = getInsets();
	            int  buttonWidth = width - (insets.top + insets.bottom);
	            int  buttonHeight = buttonWidth;
	            if (arrowButton != null) {
	                Insets arrowInsets = arrowButton.getInsets();
	                buttonHeight = squareButton ? 
	                		buttonWidth : 
	                    arrowButton.getPreferredSize().height + arrowInsets.top + arrowInsets.bottom;
	            }
	            Rectangle cvb;
	
	            if ( arrowButton != null ) {
			        if(isLeftToRight(cb)) {
					    arrowButton.setBounds( insets.left, height - (insets.bottom + buttonWidth),
								   buttonWidth, buttonHeight);
					}
					else {
					    arrowButton.setBounds( insets.left, insets.top,
								   buttonWidth, buttonHeight);
					}
	            }
	            if ( editor != null ) {
	                cvb = rectangleForCurrentValue();
	                editor.setBounds(cvb);
	            }
	            
            }
        }

    }

    protected Rectangle rectangleForCurrentValue() {
    	
        int width = comboBox.getWidth();
        int height = comboBox.getHeight();
    	
        if( rotate_direction == MSwingRotateUtilities.ROTATE_HORIZANTAL ) {
            
	        Insets insets = getInsets();
	        int buttonSize = height - (insets.top + insets.bottom);
			if ( arrowButton != null ) {
		            buttonSize = arrowButton.getWidth();
			}
			if(isLeftToRight(comboBox)) {
			    return new Rectangle(insets.left, insets.top,
					     width - (insets.left + insets.right + buttonSize),
		                             height - (insets.top + insets.bottom));
			}
			else {
			    return new Rectangle(insets.left + buttonSize, insets.top,
					     width - (insets.left + insets.right + buttonSize),
		                             height - (insets.top + insets.bottom));
			}
			
        } else {
        	
	        Insets insets = getInsets();
	        int buttonSize = width - (insets.left + insets.right);
			if ( arrowButton != null ) {
		            buttonSize = arrowButton.getHeight();
			}
			if(isLeftToRight(comboBox)) {
			    return new Rectangle(insets.left, insets.top,
					     width - (insets.left + insets.right),
		                             height - (insets.top + insets.bottom + buttonSize));
			}
			else {
			    return new Rectangle(insets.left, insets.top + buttonSize,
					     width - (insets.left + insets.right),
		                             height - (insets.top + insets.bottom  + buttonSize));
			}
        	
        }
    }

    private boolean isLeftToRight( Component c ) {
        return c.getComponentOrientation().isLeftToRight();
    }
    

}
