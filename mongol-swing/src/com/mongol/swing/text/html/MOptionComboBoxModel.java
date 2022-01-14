package com.mongol.swing.text.html;

import javax.swing.*;
import java.io.Serializable;

/**
 * Mofdified OptionComboBoxModel
 *
  @author jrmt@Almas
 */

class MOptionComboBoxModel extends DefaultComboBoxModel implements Serializable {

    private MOption selectedOption = null;

    /**
     * Stores the Option that has been marked its
     * selected attribute set.
     */
    public void setInitialSelection(MOption option) {
	selectedOption = option;
    }

    /**
     * Fetches the Option item that represents that was
     * initially set to a selected state.
     */
    public MOption getInitialSelection() {
	return selectedOption;
    }
}