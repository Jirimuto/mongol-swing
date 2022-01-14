package com.mongol.swing.plaf;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicComboPopup;

import com.mongol.swing.MList;
import com.mongol.swing.MSwingRotateUtilities;


public class MComboPopup extends BasicComboPopup {
	
    //===================================================================
    // begin Initialization routines
    //
    public MComboPopup( JComboBox combo ) {
        super(combo);
    }


    protected MList createList() {
	return new MList( comboBox.getModel() ) {
            public void processMouseEvent(MouseEvent e)  {
                if (isMenuShortcutKeyDown(e))  {
                    // Fix for 4234053. Filter out the Control Key from the list. 
                    // ie., don't allow CTRL key deselection.
                    Toolkit toolkit = Toolkit.getDefaultToolkit();
                    e = new MouseEvent((Component)e.getSource(), e.getID(), e.getWhen(), 
                                       e.getModifiers() ^ toolkit.getMenuShortcutKeyMask(),
                                       e.getX(), e.getY(),
                                       e.getXOnScreen(), e.getYOnScreen(),
                                       e.getClickCount(),
                                       e.isPopupTrigger(),
                                       MouseEvent.NOBUTTON);
                }
                super.processMouseEvent(e);
            }
        };
    }

    private boolean isMenuShortcutKeyDown(InputEvent event) {
        return (event.getModifiers() & 
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()) != 0;
    }
	
    protected void configureList() {
    	Font font = comboBox.getFont();
        list.setFont( font );
        list.setForeground( comboBox.getForeground() ); 
        list.setBackground( comboBox.getBackground() );
        list.setSelectionForeground( UIManager.getColor( "ComboBox.selectionForeground" ) );
        list.setSelectionBackground( UIManager.getColor( "ComboBox.selectionBackground" ) );
        list.setBorder( null );
        list.setCellRenderer( comboBox.getRenderer() );
        list.setFocusable( false );
        list.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        setListSelection( comboBox.getSelectedIndex() );
        installListListeners();
    }


    //========================================
    // begin ComboPopup method implementations
    //

    /**
     * Implementation of ComboPopup.show().
     */
    public void show() {
		setListSelection(comboBox.getSelectedIndex());
	
		Point location = getPopupLocation();
        show( comboBox, location.x, location.y );
    }

    /**
     * Sets the list selection index to the selectedIndex. This 
     * method is used to synchronize the list selection with the 
     * combo box selection.
     * 
     * @param selectedIndex the index to set the list
     */
    private void setListSelection(int selectedIndex) {
        if ( selectedIndex == -1 ) {
            list.clearSelection();
        }
        else {
            list.setSelectedIndex( selectedIndex );
	    list.ensureIndexIsVisible( selectedIndex );
        }
    }


    /**
     * Calculates the upper left location of the Popup.
     */
    private Point getPopupLocation() {
		Dimension popupSize = comboBox.getSize();
		Insets insets = getInsets();

		Rectangle popupBounds = new Rectangle();
		if( list instanceof MRotation ) {
			if( ((MRotation)list).getRotateDirection()!=MSwingRotateUtilities.ROTATE_HORIZANTAL ){
				// Vertical 
				popupSize.setSize(getPopupWidthForRowCount( comboBox.getMaximumRowCount()), 
									popupSize.height - (insets.right + insets.left));
				
				popupBounds = computePopupBounds( comboBox.getBounds().width, 0,
			                                                    popupSize.width, popupSize.height);
			} else {
				
				popupSize.setSize(popupSize.width - (insets.right + insets.left), 
						  getPopupHeightForRowCount( comboBox.getMaximumRowCount()));
				popupBounds = computePopupBounds( 0, comboBox.getBounds().height,
			                                                    popupSize.width, popupSize.height);
			}
			
		} else {
			// reduce the width of the scrollpane by the insets so that the popup
			// is the same width as the combo box.
			popupSize.setSize(popupSize.width - (insets.right + insets.left), 
					  getPopupHeightForRowCount( comboBox.getMaximumRowCount()));
			popupBounds = computePopupBounds( 0, comboBox.getBounds().height,
		                                                    popupSize.width, popupSize.height);
		}
		Dimension scrollSize = popupBounds.getSize();
		Point popupLocation = popupBounds.getLocation();
		    
		scroller.setMaximumSize( scrollSize );
		scroller.setPreferredSize( scrollSize );
		scroller.setMinimumSize( scrollSize );
		
		list.revalidate();
	
		return popupLocation;
    }

    /**
     * Retrieves the height of the popup based on the current
     * ListCellRenderer and the maximum row count.
     */
    protected int getPopupWidthForRowCount(int maxRowCount) {
	// Set the cached value of the minimum row count
        int minRowCount = Math.min( maxRowCount, comboBox.getItemCount() );
        int width = 0;
        ListCellRenderer renderer = list.getCellRenderer();
        Object value = null;

        for ( int i = 0; i < minRowCount; ++i ) {
            value = list.getModel().getElementAt( i );
            Component c = renderer.getListCellRendererComponent( list, value, i, false, false );
            width += c.getPreferredSize().width;
        }
        
        if (width == 0) {
            width = comboBox.getHeight();
        }
        
        Border border = scroller.getViewportBorder();
        if (border != null) {
            Insets insets = border.getBorderInsets(null);
            width += insets.top + insets.bottom;
        }
        
        border = scroller.getBorder();
        if (border != null) {
            Insets insets = border.getBorderInsets(null);
            width += insets.top + insets.bottom;
        }
        
        return width;
    }


    /**
     * Implementation of ComboPopup.hide().
     */
    public void hide() {
        MenuSelectionManager manager = MenuSelectionManager.defaultManager();
        MenuElement [] selection = manager.getSelectedPath();
        for ( int i = 0 ; i < selection.length ; i++ ) {
            if ( selection[i] == this ) {
                manager.clearSelectedPath();
                break;
            }
        }
        if (selection.length > 0) {
            comboBox.repaint();
        }
    }
    
    protected JScrollPane createScroller() {
        JScrollPane sp = new JScrollPane( list, 
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED );
        return sp;
    }

}
