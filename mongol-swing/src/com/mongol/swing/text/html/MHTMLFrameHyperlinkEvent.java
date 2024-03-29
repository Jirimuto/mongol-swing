package com.mongol.swing.text.html;

import javax.swing.text.*;
import javax.swing.event.HyperlinkEvent;
import java.net.URL;

/**
 * Modified HTMLFrameHyperlinkEvent
 *
 * @author jrmt@Almas
 */

public class MHTMLFrameHyperlinkEvent extends HyperlinkEvent {

    /**
     * Creates a new object representing a html frame 
     * hypertext link event.
     *
     * @param source the object responsible for the event
     * @param type the event type
     * @param targetURL the affected URL
     * @param targetFrame the Frame to display the document in
     */
    public MHTMLFrameHyperlinkEvent(Object source, EventType type, URL targetURL, 
				   String targetFrame) {
        super(source, type, targetURL);
	this.targetFrame = targetFrame;
    }


    /**
     * Creates a new object representing a hypertext link event.
     *
     * @param source the object responsible for the event
     * @param type the event type
     * @param targetURL the affected URL
     * @param desc a description
     * @param targetFrame the Frame to display the document in
     */
    public MHTMLFrameHyperlinkEvent(Object source, EventType type, URL targetURL, String desc,  
				   String targetFrame) {
        super(source, type, targetURL, desc);
	this.targetFrame = targetFrame;
    }

    /**
     * Creates a new object representing a hypertext link event.
     *
     * @param source the object responsible for the event
     * @param type the event type
     * @param targetURL the affected URL
     * @param sourceElement the element that corresponds to the source
     *                      of the event
     * @param targetFrame the Frame to display the document in
     */
    public MHTMLFrameHyperlinkEvent(Object source, EventType type, URL targetURL, 
				   Element sourceElement, String targetFrame) {
        super(source, type, targetURL, null, sourceElement);
	this.targetFrame = targetFrame;
    }


    /**
     * Creates a new object representing a hypertext link event.
     *
     * @param source the object responsible for the event
     * @param type the event type
     * @param targetURL the affected URL
     * @param desc a desription
     * @param sourceElement the element that corresponds to the source
     *                      of the event
     * @param targetFrame the Frame to display the document in
     */
    public MHTMLFrameHyperlinkEvent(Object source, EventType type, URL targetURL, String desc,  
				   Element sourceElement, String targetFrame) {
        super(source, type, targetURL, desc, sourceElement);
	this.targetFrame = targetFrame;
    }

    /**
     * returns the target for the link.
     */
    public String getTarget() {
	return targetFrame;
    }

    private String targetFrame;
}