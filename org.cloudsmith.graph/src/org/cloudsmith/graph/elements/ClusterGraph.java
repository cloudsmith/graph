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
package org.cloudsmith.graph.elements;

import org.cloudsmith.graph.ElementType;
import org.cloudsmith.graph.IClusterGraph;
import org.cloudsmith.graph.ILabeledGraphElement;

/**
 * A concrete implementation of ISubGraph. A SubGraph acts as a container with no visual bounds or appearance of
 * its own. It is useful for setting defaults, and for style rules that act on the style class/id of a container.
 * 
 */
public class ClusterGraph extends LabeledGraph implements IClusterGraph, ILabeledGraphElement {

	public ClusterGraph(String styleClass) {
		super(styleClass);
	}

	public ClusterGraph(String styleClass, String id) {
		super(styleClass, id);
	}

	public ClusterGraph(String label, String styleClass, String id) {
		super(label, styleClass, id);
	}

	@Override
	public ElementType getElementType() {
		return ElementType.cluster;
	}

}
