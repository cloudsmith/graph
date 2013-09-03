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
 * Interface for an edge between two vertexes.
 * 
 */
public interface IEdge extends ILabeledGraphElement {

	/**
	 * The "source" or "tail" end of the edge.
	 * 
	 * @return the from vertex
	 */
	public IVertex getFrom();

	/**
	 * The "target" or "head" end of the edge.
	 * 
	 * @return the to vertex
	 */
	public IVertex getTo();

}
