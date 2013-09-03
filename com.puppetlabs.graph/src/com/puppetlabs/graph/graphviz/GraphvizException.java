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

/**
 * An exception indicating that the external (graphviz) executable reported problems indicating
 * failure.
 */
public class GraphvizException extends IllegalStateException {

	private static final long serialVersionUID = 1L;

	public GraphvizException(String message) {
		super(message);
	}

	public GraphvizException(String message, Throwable cause) {
		super(message, cause);
	}
}
