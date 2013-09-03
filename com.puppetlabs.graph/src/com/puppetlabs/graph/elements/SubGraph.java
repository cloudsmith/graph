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
package com.puppetlabs.graph.elements;

import com.puppetlabs.graph.ElementType;
import com.puppetlabs.graph.IClusterGraph;
import com.puppetlabs.graph.IRootGraph;
import com.puppetlabs.graph.ISubGraph;

/**
 * A concrete implementation of ISubGraph. A SubGraph acts as a container with no visual bounds or appearance of
 * its own. It is useful for setting defaults, and for style rules that act on the style class/id of a container.
 * 
 */
public class SubGraph extends Graph implements ISubGraph {

	protected SubGraph(Graph g) {
		super(g);
	}

	public SubGraph(String styleClass) {
		super(styleClass);
	}

	public SubGraph(String styleClass, String id) {
		super(styleClass, id);
	}

	@Override
	public IClusterGraph asClusterGraph(String label) {
		return new ClusterGraph(label, this);
	}

	@Override
	public IRootGraph asRootGraph(String label) {
		return new RootGraph(label, this);
	}

	@Override
	public ElementType getElementType() {
		return ElementType.subgraph;
	}

}
