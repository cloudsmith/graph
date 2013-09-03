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

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import com.puppetlabs.graph.IGraph;
import com.puppetlabs.graph.ILabeledGraphElement;

/**
 * An IGraph that is also ILabeledGraphElement
 * 
 */
public abstract class LabeledGraph extends Graph implements ILabeledGraphElement {

	private String label;

	protected LabeledGraph() {
		this(null, "", "", null);
	}

	protected LabeledGraph(IGraph graph) {
		super(graph);
		if(!(graph instanceof LabeledGraphElement))
			throw new IllegalArgumentException("graph must implement ILabeledGraphElement");
		ILabeledGraphElement that = (ILabeledGraphElement) graph;
		this.label = that.getLabel();
	}

	protected LabeledGraph(Map<String, String> data) {
		this(data, "", "", null);
	}

	protected LabeledGraph(Map<String, String> data, String styleClass) {
		this(null, "", styleClass, null);
	}

	protected LabeledGraph(Map<String, String> data, String label, Collection<String> styleClasses, String id) {
		super(styleClasses, id);
		this.label = label;
		if(data != null)
			getUserData().putAll(data);
	}

	protected LabeledGraph(Map<String, String> data, String styleClass, String id) {
		this(null, "", styleClass, id);
	}

	protected LabeledGraph(Map<String, String> data, String label, String styleClass, String id) {
		this(data, label, Collections.singleton(styleClass), id);
	}

	protected LabeledGraph(String styleClass) {
		this(null, "", styleClass, null);
	}

	protected LabeledGraph(String label, Collection<String> styleClasses, String id) {
		this(null, label, styleClasses, id);
	}

	protected LabeledGraph(String label, IGraph that) {
		super(that);
		this.label = label;
	}

	protected LabeledGraph(String styleClass, String id) {
		this(null, "", styleClass, id);
	}

	protected LabeledGraph(String label, String styleClass, String id) {
		this(null, label, styleClass, id);
	}

	// /**
	// * @deprecated use {@link #getUserData()}.
	// */
	// @Deprecated
	// public Map<String, String> getData() {
	// return getUserData();
	// }

	public String getLabel() {
		return label;
	}

	public void setData(Map<String, String> dataMap) {
		getUserData().putAll(dataMap);
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
