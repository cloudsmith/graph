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

import java.util.Iterator;
import java.util.Map;

import org.cloudsmith.graph.ElementType;
import org.cloudsmith.graph.IGraphElement;
import org.cloudsmith.graph.graphcss.StyleSet;
import org.cloudsmith.graph.style.IStyle;

import com.google.common.collect.Maps;

/**
 * Base implementation of an IGraphElement.
 * 
 */
public abstract class GraphElement implements IGraphElement {
	private static class ParentIterator implements Iterator<IGraphElement> {

		private IGraphElement current;

		public ParentIterator(IGraphElement ge) {
			this.current = ge;
		}

		@Override
		public boolean hasNext() {
			return current.getParentElement() != null;
		}

		@Override
		public IGraphElement next() {
			current = current.getParentElement();
			return current;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("Can not remove from this iterator.");
		}

	}

	private String id;

	private String styleClass;

	private String urlString;

	private IGraphElement parentElement;

	private StyleSet instanceStyleMap;

	private Map<String, String> data;

	/**
	 * If a graph element is created without an id, it must either be set explicitly
	 * by user code - or set by the graph where it is added. (The default implementation
	 * of Graph does this).
	 */
	public GraphElement() {
		this("", null);
	}

	/**
	 * Copy constructor.
	 * 
	 * @param that
	 */
	protected GraphElement(IGraphElement that) {
		this.id = that.getId();
		this.styleClass = that.getStyleClass();
		this.data = Maps.newHashMap();
		this.data.putAll(that.getUserData());
		if(that.getStyles() != null) {
			this.instanceStyleMap = new StyleSet();
			this.instanceStyleMap.add(that.getStyles());
		}
	}

	public GraphElement(String styleClass) {
		this(styleClass, null);
	}

	public GraphElement(String styleClass, String id) {
		this.id = id;
		this.styleClass = styleClass;
		this.data = Maps.newHashMap();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.IGraphElement#getContext()
	 */
	@Override
	public Iterator<IGraphElement> getContext() {
		return new ParentIterator(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.IGraphElement#getElementType()
	 */
	@Override
	public abstract ElementType getElementType();

	public String getId() {
		return id;
	}

	@Override
	public IGraphElement getParentElement() {
		return parentElement;
	}

	@Override
	public String getStyleClass() {
		return styleClass;
	}

	@Override
	public StyleSet getStyles() {
		return instanceStyleMap;
	}

	public String getUrlString() {
		return urlString;
	}

	public Map<String, String> getUserData() {
		return data;
	}

	@Override
	public String getUserData(String key) {
		return data.get(key);
	}

	@Override
	public void putUserData(String key, String value) {
		data.put(key, value);
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setParentElement(IGraphElement parent) {
		this.parentElement = parent;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public void setStyles(IStyle<?>... styles) {
		if(styles.length > 0) {
			if(instanceStyleMap != null)
				instanceStyleMap.add(StyleSet.withStyles(styles));
			else
				instanceStyleMap = StyleSet.withStyles(styles);
		}
	}

	/**
	 * Sets a copy of the given style map as this element's map. It is illegal to modify the given
	 * StyleSet once it has been set.
	 * 
	 * @param styleMap
	 */
	public void setStyles(StyleSet styleMap) {

		this.instanceStyleMap = styleMap;
	}

	public void setUrlString(String urlString) {
		this.urlString = urlString;
	}
}
