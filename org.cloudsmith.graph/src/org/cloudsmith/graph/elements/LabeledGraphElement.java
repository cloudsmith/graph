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

import java.util.HashMap;
import java.util.Map;

import org.cloudsmith.graph.IGraphElement;
import org.cloudsmith.graph.ILabeledGraphElement;

import com.google.common.collect.Maps;

/**
 * 
 *
 */
public abstract class LabeledGraphElement extends GraphElement implements IGraphElement, ILabeledGraphElement {
	private String label;

	private Map<String, String> data;

	public LabeledGraphElement() {
		this(null, "", "", null);
	}

	protected LabeledGraphElement(LabeledGraphElement that) {
		super(that);
		this.label = that.label;
		if(that.data != null) {
			this.data = Maps.newHashMap();
			this.data.putAll(that.data);
		}
	}

	public LabeledGraphElement(Map<String, String> data) {
		this(data, "", "", null);
	}

	public LabeledGraphElement(Map<String, String> data, String styleClass) {
		this(null, "", styleClass, null);
	}

	public LabeledGraphElement(Map<String, String> data, String styleClass, String id) {
		this(null, "", styleClass, id);
	}

	public LabeledGraphElement(Map<String, String> data, String label, String styleClass, String id) {
		super(styleClass, id);
		this.label = label;
		this.data = data;
	}

	public LabeledGraphElement(String styleClass) {
		this(null, "", styleClass, null);
	}

	public LabeledGraphElement(String styleClass, String id) {
		this(null, "", styleClass, id);
	}

	public LabeledGraphElement(String label, String styleClass, String id) {
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
}
