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
package org.cloudsmith.graph.elements;

import org.cloudsmith.graph.ElementType;
import org.cloudsmith.graph.IGraph;
import org.cloudsmith.graph.IRootGraph;

/**
 * A graph is a container of other graph elements (which include other graphs as subgraphs, vertexes and edges).
 * This implementation of {@link IGraph} works with instances of other graph elements in the same package
 * as it manages their containment and identity (if not set).
 * 
 */
public class RootGraph extends LabeledGraph implements IRootGraph {

	public RootGraph(String label, String styleClass) {
		this(label, styleClass, null);
	}

	public RootGraph(String label, String styleClass, String id) {
		super(label, styleClass, id);

	}

	@Override
	public ElementType getElementType() {
		return ElementType.graph;
	}

}
