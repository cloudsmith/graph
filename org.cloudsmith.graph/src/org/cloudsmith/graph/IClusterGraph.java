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
package org.cloudsmith.graph;

/**
 * Marker interface for a graph having a non root role and being clustered.
 * 
 */
public interface IClusterGraph extends ISubGraph, ILabeledGraphElement {

	/**
	 * Create a new RootGraph with the same content as this subgraph.
	 * 
	 * @return an IRootGraph being a copy of this cluster graph's content.
	 */

	public abstract IRootGraph asRootGraph();

}
