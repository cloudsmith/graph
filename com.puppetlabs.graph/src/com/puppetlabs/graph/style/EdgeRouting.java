/**
 * Copyright (c) 2006-2011 Cloudsmith Inc. and other contributors, as listed below.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   AT&T - initial API
 *   Cloudsmith
 * 
 */
package com.puppetlabs.graph.style;

/**
 * Edge routing type.
 * 
 */
public enum EdgeRouting {
	/** curved - produces more compact layouts */
	spline,
	/** straight line */
	line,
	/** rectalinear */
	polyline, ;

}
