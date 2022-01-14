package com.mongol.swing.text;

/**
 * This exception is to report the failure of state invarient
 * assertion that was made.  This indicates an internal error
 * has occurred.
 *
 * @author  Jirumutu
 */
class MStateInvariantError extends Error
{
    /**
     * Creates a new StateInvariantFailure object.
     *
     * @param s         a string indicating the assertion that failed
     */
    public MStateInvariantError(String s) {
        super(s);
    }

}
