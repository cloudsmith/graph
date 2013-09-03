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
package com.puppetlabs.graph.utils;

import java.io.OutputStream;

/**
 * A (null) provider for filtered output stream setup.
 * This implementation simply returns the given stream without any filtering.
 * 
 */
public class TransparentOutputStreamFilterFactory implements IOutputStreamFilterFactory {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.puppetlabs.graph.utils.IOutputStreamFilterProvider#configureFilterFor(java.io.OutputStream)
	 */
	@Override
	public OutputStream configureFilterFor(OutputStream out) {
		return out;
	}

}
