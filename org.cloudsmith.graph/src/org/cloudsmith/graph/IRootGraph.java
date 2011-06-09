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
 * Marker interface for a graph having a root role.
 * 
 */
public interface IRootGraph extends IGraph, ILabeledGraphElement {

	/**
	 * Create a new ClusterGraph with the same content as this root graph.
	 * 
	 * @return an IClusterGraph being a copy of this RootGraph's content.
	 */
	public abstract IClusterGraph asCluster();

	/**
	 * Create a new subgraph with the same content as this root graph (looses label and label data
	 * information).
	 * 
	 * @return an ISubGraph being a copy of this RootGraph's content.
	 */
	public abstract ISubGraph asSubGraph();

}
