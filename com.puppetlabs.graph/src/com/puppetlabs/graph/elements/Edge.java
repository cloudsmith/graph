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
import java.util.Collections;

import com.puppetlabs.graph.ElementType;
import com.puppetlabs.graph.IEdge;
import com.puppetlabs.graph.IVertex;

/**
 * Edges can go between any two vertices including those nested in subgraphs (as well as subgraphs).
 * Edges are typically created without an identity as in most cases, there is little need to
 * refer to them individually. When added to the default Graph implementation an id
 * is automatically assigned.
 * 
 * An explicit id can be set in the constructors, or in a separate call to setId.
 */
public class Edge extends LabeledGraphElement implements IEdge {
	private final IVertex to;

	private final IVertex from;

	public Edge(IVertex from, IVertex to) {
		this("", "", from, to, null);
	}

	public Edge(IVertex from, IVertex to, String id) {
		this("", "", from, to, id);
	}

	public Edge(String label, Collection<String> styleClasses, IVertex from, IVertex to) {
		this(label, styleClasses, from, to, null);
	}

	public Edge(String label, Collection<String> styleClass, IVertex from, IVertex to, String id) {
		super(label, styleClass, id);
		if(from == null)
			throw new IllegalArgumentException("from can not be null");
		if(to == null)
			throw new IllegalArgumentException("to can not be null");
		this.from = from;
		this.to = to;
	}

	public Edge(String label, IVertex from, IVertex to) {
		this(label, "", from, to, null);
	}

	public Edge(String label, IVertex from, IVertex to, String id) {
		this(label, "", from, to, id);
	}

	public Edge(String label, String styleClass, IVertex from, IVertex to) {
		this(label, styleClass, from, to, null);
	}

	public Edge(String label, String styleClass, IVertex from, IVertex to, String id) {
		this(label, Collections.singleton(styleClass), from, to, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.puppetlabs.graph.impl.GraphElement#getElementType()
	 */
	@Override
	public ElementType getElementType() {
		return ElementType.edge;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.puppetlabs.graph.impl.GraphElement#getFrom()
	 */
	@Override
	public IVertex getFrom() {
		return from;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.puppetlabs.graph.impl.GraphElement#getTo()
	 */
	@Override
	public IVertex getTo() {
		return to;
	}

}
