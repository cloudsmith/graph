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

import java.util.Iterator;

import org.cloudsmith.graph.graphcss.StyleSet;

/**
 * The base interface for graph elements (graph, vertex, and edge).
 * 
 */
public interface IGraphElement {
	/**
	 * Returns all containing graph elements, the immediate parent first.
	 * The returned iterator does not support the {@link Iterator#remove()} operation.
	 * 
	 * @return an iterator over ancestor containers
	 */
	public Iterator<IGraphElement> getContext();

	/**
	 * Returns the graph element type. This is needed as the inheritance of graph elements reflects
	 * behavior (a graph is a vertex) when it represents a subgraph/cluster and functionality that only
	 * applies to a true vertex have nothing to check against as both are instances of IVertex (and the implementation
	 * details may not be known).
	 * 
	 * @return the ElementType
	 */
	public ElementType getElementType();

	/**
	 * The id of the element. The id must be unique within its parent element.
	 * 
	 * @return a id that is unique within parent
	 */
	public String getId();

	/**
	 * Returns the immediate containing graph element.
	 * 
	 * @return parent graph element
	 */
	public IGraphElement getParentElement();

	/**
	 * The style class of the element.
	 * Examples; for a graph it can be the name of the depicted type (e.g. "Dependency graph", a file etc.),
	 * for an vertex it can be the vertex type (i.e. what the vertex represents), and for an edge
	 * it can be the type name of the edge (e.g. "dependency"). If style class is omitted a default style
	 * is used.
	 * 
	 * @return the style class name
	 */
	public String getStyleClass();

	/**
	 * A StyleMap for this instance, or null if no instance specific style are set.
	 * 
	 * @return a style set for the instance.
	 */
	public StyleSet getStyles();

}
