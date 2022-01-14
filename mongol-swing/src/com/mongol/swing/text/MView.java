/*
 * Copyright (c) 1997, 2015, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package com.mongol.swing.text;

import java.awt.*;
import javax.swing.SwingConstants;
import javax.swing.event.*;
import javax.swing.text.Element;
import javax.swing.text.View;

import com.mongol.swing.MSwingRotateUtilities;
import com.mongol.swing.plaf.MRotation;

/**
 * <p>
 * A very important part of the text package is the <code>View</code> class.
 * As the name suggests it represents a view of the text model,
 * or a piece of the text model.
 * It is this class that is responsible for the look of the text component.
 * The view is not intended to be some completely new thing that one must
 * learn, but rather is much like a lightweight component.
 * <p>
By default, a view is very light.  It contains a reference to the parent
view from which it can fetch many things without holding state, and it
contains a reference to a portion of the model (<code>Element</code>).
A view does not
have to exactly represent an element in the model, that is simply a typical
and therefore convenient mapping.  A view can alternatively maintain a couple
of Position objects to maintain its location in the model (i.e. represent
a fragment of an element).  This is typically the result of formatting where
views have been broken down into pieces.  The convenience of a substantial
relationship to the element makes it easier to build factories to produce the
views, and makes it easier  to keep track of the view pieces as the model is
changed and the view must be changed to reflect the model.  Simple views
therefore represent an Element directly and complex views do not.
<p>
A view has the following responsibilities:
  <dl>

    <dt><b>Participate in layout.</b>
    <dd>
    <p>The view has a <code>setSize</code> method which is like
    <code>doLayout</code> and <code>setSize</code> in <code>Component</code> combined.
    The view has a <code>preferenceChanged</code> method which is
    like <code>invalidate</code> in <code>Component</code> except that one can
    invalidate just one axis
    and the child requesting the change is identified.
    <p>A View expresses the size that it would like to be in terms of three
    values, a minimum, a preferred, and a maximum span.  Layout in a view is
    can be done independently upon each axis.  For a properly functioning View
    implementation, the minimum span will be &lt;= the preferred span which in turn
    will be &lt;= the maximum span.
    </p>
    <p style="text-align:center"><img src="doc-files/View-flexibility.jpg"
                     alt="The above text describes this graphic.">
    <p>The minimum set of methods for layout are:
    <ul>
    <li>{@link #getMinimumSpan(int) getMinimumSpan}
    <li>{@link #getPreferredSpan(int) getPreferredSpan}
    <li>{@link #getMaximumSpan(int) getMaximumSpan}
    <li>{@link #getAlignment(int) getAlignment}
    <li>{@link #preferenceChanged(javax.swing.text.View, boolean, boolean) preferenceChanged}
    <li>{@link #setSize(float, float) setSize}
    </ul>

  <p>The <code>setSize</code> method should be prepared to be called a number of times
    (i.e. It may be called even if the size didn't change).
    The <code>setSize</code> method
    is generally called to make sure the View layout is complete prior to trying
    to perform an operation on it that requires an up-to-date layout.  A view's
    size should <em>always</em> be set to a value within the minimum and maximum
    span specified by that view.  Additionally, the view must always call the
    <code>preferenceChanged</code> method on the parent if it has changed the
    values for the
    layout it would like, and expects the parent to honor.  The parent View is
    not required to recognize a change until the <code>preferenceChanged</code>
    has been sent.
    This allows parent View implementations to cache the child requirements if
    desired.  The calling sequence looks something like the following:
    </p>
    <p style="text-align:center">
      <img src="doc-files/View-layout.jpg"
       alt="Sample calling sequence between parent view and child view:
       setSize, getMinimum, getPreferred, getMaximum, getAlignment, setSize">
    <p>The exact calling sequence is up to the layout functionality of
    the parent view (if the view has any children).  The view may collect
    the preferences of the children prior to determining what it will give
    each child, or it might iteratively update the children one at a time.
    </p>

    <dt><b>Render a portion of the model.</b>
    <dd>
    <p>This is done in the paint method, which is pretty much like a component
    paint method.  Views are expected to potentially populate a fairly large
    tree.  A <code>View</code> has the following semantics for rendering:
    </p>
    <ul>
    <li>The view gets its allocation from the parent at paint time, so it
    must be prepared to redo layout if the allocated area is different from
    what it is prepared to deal with.
    <li>The coordinate system is the same as the hosting <code>Component</code>
    (i.e. the <code>Component</code> returned by the
    {@link #getContainer getContainer} method).
    This means a child view lives in the same coordinate system as the parent
    view unless the parent has explicitly changed the coordinate system.
    To schedule itself to be repainted a view can call repaint on the hosting
    <code>Component</code>.
    <li>The default is to <em>not clip</em> the children.  It is more efficient
    to allow a view to clip only if it really feels it needs clipping.
    <li>The <code>Graphics</code> object given is not initialized in any way.
    A view should set any settings needed.
    <li>A <code>View</code> is inherently transparent.  While a view may render into its
    entire allocation, typically a view does not.  Rendering is performed by
    traversing down the tree of <code>View</code> implementations.
    Each <code>View</code> is responsible
    for rendering its children.  This behavior is depended upon for thread
    safety.  While view implementations do not necessarily have to be implemented
    with thread safety in mind, other view implementations that do make use of
    concurrency can depend upon a tree traversal to guarantee thread safety.
    <li>The order of views relative to the model is up to the implementation.
    Although child views will typically be arranged in the same order that they
    occur in the model, they may be visually arranged in an entirely different
    order.  View implementations may have Z-Order associated with them if the
    children are overlapping.
    </ul>
    <p>The methods for rendering are:
    <ul>
    <li>{@link #paint(java.awt.Graphics, java.awt.Shape) paint}
    </ul>

    <dt><b>Translate between the model and view coordinate systems.</b>
    <dd>
    <p>Because the view objects are produced from a factory and therefore cannot
    necessarily be counted upon to be in a particular pattern, one must be able
    to perform translation to properly locate spatial representation of the model.
    The methods for doing this are:
    <ul>
    <li>{@link #modelToView(int, javax.swing.text.Position.Bias, int, javax.swing.text.Position.Bias, java.awt.Shape) modelToView}
    <li>{@link #viewToModel(float, float, java.awt.Shape, javax.swing.text.Position.Bias[]) viewToModel}
    <li>{@link #getDocument() getDocument}
    <li>{@link #getElement() getElement}
    <li>{@link #getStartOffset() getStartOffset}
    <li>{@link #getEndOffset() getEndOffset}
    </ul>
    <p>The layout must be valid prior to attempting to make the translation.
    The translation is not valid, and must not be attempted while changes
    are being broadcasted from the model via a <code>DocumentEvent</code>.
    </p>

    <dt><b>Respond to changes from the model.</b>
    <dd>
    <p>If the overall view is represented by many pieces (which is the best situation
    if one want to be able to change the view and write the least amount of new code),
    it would be impractical to have a huge number of <code>DocumentListener</code>s.
    If each
    view listened to the model, only a few would actually be interested in the
    changes broadcasted at any given time.   Since the model has no knowledge of
    views, it has no way to filter the broadcast of change information.  The view
    hierarchy itself is instead responsible for propagating the change information.
    At any level in the view hierarchy, that view knows enough about its children to
    best distribute the change information further.   Changes are therefore broadcasted
    starting from the root of the view hierarchy.
    The methods for doing this are:
    <ul>
    <li>{@link #insertUpdate insertUpdate}
    <li>{@link #removeUpdate removeUpdate}
    <li>{@link #changedUpdate changedUpdate}
    </ul>
</dl>
 *
 * @author  Timothy Prinzing
 */
public abstract class MView extends View implements SwingConstants, MRotation {

    private int rotate_direction = MSwingRotateUtilities.ROTATE_HORIZANTAL;
    private int rotate_hint = MSwingRotateUtilities.ROTATE_DEFAULT;
    
    /**
     * Creates a new <code>View</code> object.
     *
     * @param elem the <code>Element</code> to represent
     */
    public MView(Element elem) {
        super(elem);
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

};
