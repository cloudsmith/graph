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

/**
 * 
 *
 */
public abstract class LabeledGraphElement extends GraphElement implements IGraphElement, ILabeledGraphElement {
	private String m_label;

	private Map<String, String> m_data;

	public LabeledGraphElement() {
		this(null, "", "", null);
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
		m_label = label;
		m_data = data;
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
		if(m_data == null)
			m_data = new HashMap<String, String>();
		return m_data;
	}

	public String getLabel() {
		return m_label;
	}

	public void setData(Map<String, String> dataMap) {
		m_data = dataMap;
	}
}
