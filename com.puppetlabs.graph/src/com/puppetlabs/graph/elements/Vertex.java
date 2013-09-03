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
import com.puppetlabs.graph.IVertex;

/**
 * An implementation of IVertex.
 * 
 */
public class Vertex extends LabeledGraphElement implements IVertex {
	public Vertex(String label, Collection<String> styleClasses) {
		super(label, styleClasses, null);
	}

	public Vertex(String label, Collection<String> styleClasses, String id) {
		super(label, styleClasses, id);
	}

	public Vertex(String label, String styleClass) {
		super(label, styleClass, null);
	}

	public Vertex(String label, String styleClass, String id) {
		super(label, styleClass, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.puppetlabs.graph.impl.GraphElement#getElementType()
	 */
	@Override
	public ElementType getElementType() {
		return ElementType.vertex;
	}
}
