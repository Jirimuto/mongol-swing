package com.mongol.swing.text.html;

import java.io.InputStream;

/**
 * Modified Resource Loader
 *
 * @author  jrmt@Almas
 */
class MResourceLoader implements java.security.PrivilegedAction {

    MResourceLoader(String name) {
	this.name = name;
    }

    public Object run() {
	Object o = MHTMLEditorKit.class.getResourceAsStream(name);
	return o;
    }

    public static InputStream getResourceAsStream(String name) {
	java.security.PrivilegedAction a = new MResourceLoader(name);
        return (InputStream) java.security.AccessController.doPrivileged(a);
    }

    private String name;
}
