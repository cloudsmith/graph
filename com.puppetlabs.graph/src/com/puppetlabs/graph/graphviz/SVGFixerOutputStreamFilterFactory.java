/**
 * Copyright (c) 2011 Cloudsmith Inc. and other contributors, as listed below.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Cloudsmith
 * 
 */
package com.puppetlabs.graph.graphviz;

import java.io.OutputStream;

import com.puppetlabs.graph.utils.IOutputStreamFilterFactory;

/**
 * Factory that wraps the given output stream with a SVGFixerOutputStream.
 * 
 */
public class SVGFixerOutputStreamFilterFactory implements IOutputStreamFilterFactory {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.puppetlabs.graph.utils.IOutputStreamFilterProvider#configureFilterFor(java.io.OutputStream)
	 */
	@Override
	public OutputStream configureFilterFor(OutputStream out) {
		return new SVGFixerOutputStream(out);
	}

}
