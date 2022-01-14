package com.mongol.swing.text.html;

import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;
import java.beans.*;
import java.lang.reflect.*;

/**
 * Modified ObjectView
 * 
 * @author jrmt@Almas
 */
public class MObjectView extends ComponentView {

	/**
	 * Creates a new ObjectView object.
	 * 
	 * @param elem
	 *            the element to decorate
	 */
	public MObjectView(Element elem) {
		super(elem);
	}

	/**
	 * Create the component. The classid is used as a specification of the
	 * classname, which we try to load.
	 */
	protected Component createComponent() {
		AttributeSet attr = getElement().getAttributes();
		String classname = (String) attr.getAttribute(MHTML.Attribute.CLASSID);
		try {
			Class c = Class.forName(classname, true, Thread.currentThread()
					.getContextClassLoader());
			Object o = c.newInstance();
			if (o instanceof Component) {
				Component comp = (Component) o;
				setParameters(comp, attr);
				return comp;
			}
		} catch (Throwable e) {
			// couldn't create a component... fall through to the
			// couldn't load representation.
		}

		return getUnloadableRepresentation();
	}

	/**
	 * Fetch a component that can be used to represent the object if it can't be
	 * created.
	 */
	Component getUnloadableRepresentation() {
		// PENDING(prinz) get some artwork and return something
		// interesting here.
		Component comp = new JLabel("??");
		comp.setForeground(Color.red);
		return comp;
	}

	/**
	 * Get a Class object to use for loading the classid. If possible, the
	 * Classloader used to load the associated Document is used. This would
	 * typically be the same as the ClassLoader used to load the EditorKit. If
	 * the documents ClassLoader is null, <code>Class.forName</code> is used.
	 */
	private Class getClass(String classname) throws ClassNotFoundException {
		Class klass;

		Class docClass = getDocument().getClass();
		ClassLoader loader = docClass.getClassLoader();
		if (loader != null) {
			klass = loader.loadClass(classname);
		} else {
			klass = Class.forName(classname);
		}
		return klass;
	}

	/**
	 * Initialize this component according the KEY/VALUEs passed in via the
	 * &lt;param&gt; elements in the corresponding &lt;object&gt; element.
	 */
	private void setParameters(Component comp, AttributeSet attr) {
		Class k = comp.getClass();
		BeanInfo bi;
		try {
			bi = Introspector.getBeanInfo(k);
		} catch (IntrospectionException ex) {
			System.err.println("introspector failed, ex: " + ex);
			return; // quit for now
		}
		PropertyDescriptor props[] = bi.getPropertyDescriptors();
		for (int i = 0; i < props.length; i++) {
			// System.err.println("checking on props[i]: "+props[i].getName());
			Object v = attr.getAttribute(props[i].getName());
			if (v instanceof String) {
				// found a property parameter
				String value = (String) v;
				Method writer = props[i].getWriteMethod();
				if (writer == null) {
					// read-only property. ignore
					return; // for now
				}
				Class[] params = writer.getParameterTypes();
				if (params.length != 1) {
					// zero or more than one argument, ignore
					return; // for now
				}
				Object[] args = { value };
				try {
					writer.invoke(comp, args);
				} catch (Exception ex) {
					System.err.println("Invocation failed");
					// invocation code
				}
			}
		}
	}

}
