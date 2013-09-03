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
package org.cloudsmith.graph.style;

/**
 * Describes the direction of a edge.
 */
public enum EdgeDirection {
	/** from tail to head - i.e. arrow at head (this is the standard) */
	forward,
	/** from head to tail - i.e. arrow at tail */
	back,
	/** bidirectional - i.e. arrows at both ends */
	both;

}
