/**
 * Copyright (c) 2013 Puppet Labs, Inc. and other contributors, as listed below.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Puppet Labs
 */
package com.puppetlabs.graph.elements;

import java.util.Collection;

import com.puppetlabs.graph.ElementType;
import com.puppetlabs.graph.IClusterGraph;
import com.puppetlabs.graph.IGraph;
import com.puppetlabs.graph.IRootGraph;
import com.puppetlabs.graph.ISubGraph;

/**
 * A graph is a container of other graph elements (which include other graphs as subgraphs, vertexes and edges).
 * This implementation of {@link IGraph} works with instances of other graph elements in the same package
 * as it manages their containment and identity (if not set).
 * 
 */
public class RootGraph extends LabeledGraph implements IRootGraph {

	/**
	 * @param clusterGraph
	 */
	public RootGraph(IClusterGraph that) {
		super(that);
	}

	public RootGraph(IRootGraph that) {
		super(that);
	}

	public RootGraph(String label, Collection<String> styleClasses) {
		this(label, styleClasses, null);
	}

	public RootGraph(String label, Collection<String> styleClasses, String id) {
		super(label, styleClasses, id);

	}

	public RootGraph(String label, IGraph that) {
		super(label, that);
	}

	public RootGraph(String label, String styleClass) {
		this(label, styleClass, null);
	}

	public RootGraph(String label, String styleClass, String id) {
		super(label, styleClass, id);

	}

	@Override
	public IClusterGraph asCluster() {
		return new ClusterGraph(this);
	}

	@Override
	public ISubGraph asSubGraph() {
		return new SubGraph(this);
	}

	@Override
	public ElementType getElementType() {
		return ElementType.graph;
	}

}
