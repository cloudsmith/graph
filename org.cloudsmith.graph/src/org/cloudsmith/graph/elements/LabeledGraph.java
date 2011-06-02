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

import java.util.HashMap;
import java.util.Map;

import org.cloudsmith.graph.ILabeledGraphElement;

/**
 * An IGraph that is also ILabeledGraphElement
 * 
 */
public abstract class LabeledGraph extends Graph implements ILabeledGraphElement {

	private String label;

	private Map<String, String> data;

	protected LabeledGraph() {
		this(null, "", "", null);
	}

	protected LabeledGraph(Map<String, String> data) {
		this(data, "", "", null);
	}

	protected LabeledGraph(Map<String, String> data, String styleClass) {
		this(null, "", styleClass, null);
	}

	protected LabeledGraph(Map<String, String> data, String styleClass, String id) {
		this(null, "", styleClass, id);
	}

	protected LabeledGraph(Map<String, String> data, String label, String styleClass, String id) {
		super(styleClass, id);
		this.label = label;
		this.data = data;
	}

	protected LabeledGraph(String styleClass) {
		this(null, "", styleClass, null);
	}

	protected LabeledGraph(String styleClass, String id) {
		this(null, "", styleClass, id);
	}

	protected LabeledGraph(String label, String styleClass, String id) {
		this(null, label, styleClass, id);
	}

	public Map<String, String> getData() {
		if(data == null)
			data = new HashMap<String, String>();
		return data;
	}

	public String getLabel() {
		return label;
	}

	public void setData(Map<String, String> dataMap) {
		data = dataMap;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
