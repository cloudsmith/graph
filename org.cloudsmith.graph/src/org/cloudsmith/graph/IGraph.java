/**
 * Copyright (c) 2006-2011 Cloudsmith Inc. and other contributors, as listed below.
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
 * The interface for a graph that is either the root graph or a subgraph.
 * 
 */
public interface IGraph extends IGraphElement {
	/**
	 * Returns a list of all edges in the graph.
	 * 
	 * @return empty list if there are no edges.
	 */
	public Iterable<IEdge> getEdges();

	/**
	 * Returns a list of all subgraphs in the graph.
	 * 
	 * @return
	 */
	public Iterable<ISubGraph> getSubgraphs();

	/**
	 * Returns a list of all vertices in the graph.
	 * 
	 * @return empty list if there are no edges.
	 */
	public Iterable<IVertex> getVertices();

	// /**
	// * Subgraphs that should be handled as separate "clustered" graphs (in a separate
	// * space) should return true on this method. The root graph should not be clustered.
	// * The content of unclustered graphs are seen as additions to the parent graph, and the
	// * content is laid out intermixed with all other content in the parent graph.
	// *
	// * @return true if this graph is a subgraph
	// */
	// public boolean isCluster();
}
