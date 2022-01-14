package com.mongol.swing.text;

import javax.swing.text.*;

import com.mongol.swing.MSwingRotateUtilities;
import com.mongol.swing.plaf.MRotation;

public class MStyledEditorKit extends StyledEditorKit implements MRotation {

    private int rotate_direction = MSwingRotateUtilities.ROTATE_HORIZANTAL;
    private int rotate_hint = MSwingRotateUtilities.ROTATE_DEFAULT;
    
	public MStyledEditorKit() {
		super();
	}

	public MStyledEditorKit(int direction, int hint) {
		super();
		rotate_direction = direction;
		rotate_hint = hint;
	}

    public ViewFactory getViewFactory() {
        return new StyledViewFactory();
    }

    class StyledViewFactory implements ViewFactory {

        public View create(Element elem) {
            String kind = elem.getName();
            if (kind != null) {
                if (kind.equals(AbstractDocument.ContentElementName)) {
                    return new MLabelView(elem, rotate_direction, rotate_hint);
                }
                else if (kind.equals(AbstractDocument.ParagraphElementName)) {
                    return new MParagraphView(elem, rotate_direction, rotate_hint);
                }
                else if (kind.equals(AbstractDocument.SectionElementName)) {
                    return new MEditorBoxView(elem, rotate_direction, rotate_hint);
                }
                else if (kind.equals(StyleConstants.ComponentElementName)) {
                    return new ComponentView(elem);
                }
                else if (kind.equals(StyleConstants.IconElementName)) {
                    return new IconView(elem);
                }
            }

            // default to text display
            return new MLabelView(elem);
        }

    }
    
    public void setRotateHint(int hint) {
        this.rotate_hint = hint;
    }

    public int getRotateHint(){
        return this.rotate_hint;
    }

    public void setRotateDirection(int direction){
        this.rotate_direction = direction;
    }

    public int getRotateDirection(){
        return this.rotate_direction;
    }
    
}
