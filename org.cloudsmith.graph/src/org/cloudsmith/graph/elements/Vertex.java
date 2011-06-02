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
import org.cloudsmith.graph.IVertex;

/**
 * An implementation of IVertex.
 * 
 */
public class Vertex extends LabeledGraphElement implements IVertex {
	public Vertex(String label, String styleClass) {
		super(label, styleClass, null);
	}

	public Vertex(String label, String styleClass, String id) {
		super(label, styleClass, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.impl.GraphElement#getElementType()
	 */
	@Override
	public ElementType getElementType() {
		return ElementType.vertex;
	}
}
