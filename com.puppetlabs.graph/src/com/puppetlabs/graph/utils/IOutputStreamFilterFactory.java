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

import java.io.IOException;
import java.io.OutputStream;

import com.google.inject.ImplementedBy;

/**
 * A factory for an output stream filtering configuration.
 * 
 */
@ImplementedBy(TransparentOutputStreamFilterFactory.class)
public interface IOutputStreamFilterFactory {
	/**
	 * Interface that filters can implement to indicate to a user
	 * of the filter that it needs to be finished (as opposed to closed) to
	 * ensure that everything written is passed on downstream).
	 * 
	 */
	public interface IFinishable {
		public void finish() throws IOException;
	}

	public OutputStream configureFilterFor(OutputStream out);

}
