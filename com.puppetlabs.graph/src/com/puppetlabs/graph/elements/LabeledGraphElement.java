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
package com.puppetlabs.graph.elements;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import com.puppetlabs.graph.IGraphElement;
import com.puppetlabs.graph.ILabeledGraphElement;

/**
 * 
 *
 */
public abstract class LabeledGraphElement extends GraphElement implements IGraphElement, ILabeledGraphElement {
	private String label;

	public LabeledGraphElement() {
		this(null, "", "", null);
	}

	public LabeledGraphElement(Collection<String> styleClasses) {
		this(null, "", styleClasses, null);
	}

	public LabeledGraphElement(Collection<String> styleClasses, String id) {
		this(null, "", styleClasses, id);
	}

	protected LabeledGraphElement(LabeledGraphElement that) {
		super(that);
		this.label = that.label;
	}

	public LabeledGraphElement(Map<String, String> data) {
		this(data, "", "", null);
	}

	public LabeledGraphElement(Map<String, String> data, String styleClass) {
		this(null, "", styleClass, null);
	}

	public LabeledGraphElement(Map<String, String> data, String label, Collection<String> styleClasses, String id) {
		super(styleClasses, id);
		this.label = label;
		if(data != null)
			getUserData().putAll(data);
	}

	public LabeledGraphElement(Map<String, String> data, String styleClass, String id) {
		this(null, "", styleClass, id);
	}

	public LabeledGraphElement(Map<String, String> data, String label, String styleClass, String id) {
		this(data, label, Collections.singleton(styleClass), id);
	}

	public LabeledGraphElement(String styleClass) {
		this(null, "", styleClass, null);
	}

	public LabeledGraphElement(String label, Collection<String> styleClasses, String id) {
		this(null, label, styleClasses, id);
	}

	public LabeledGraphElement(String styleClass, String id) {
		this(null, "", styleClass, id);
	}

	public LabeledGraphElement(String label, String styleClass, String id) {
		this(null, label, styleClass, id);
	}

	/**
	 * @deprecated use {@link #getUserData()}
	 */
	@Deprecated
	public Map<String, String> getData() {
		return getUserData();
	}

	public String getLabel() {
		return label;
	}

	public void setData(Map<String, String> dataMap) {
		getUserData().putAll(dataMap);
	}
}
