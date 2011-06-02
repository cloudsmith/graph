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

import org.cloudsmith.graph.ElementType;
import org.cloudsmith.graph.IGraphElement;
import org.cloudsmith.graph.graphcss.StyleSet;

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

	// private IGraphElement[] context;

	private StyleSet instanceStyleMap;

	/**
	 * If a graph element is created without an id, it must either be set explicitly
	 * by user code - or set by the graph where it is added. (The default implementation
	 * of Graph does this).
	 */
	public GraphElement() {
		this("", null);
	}

	public GraphElement(String styleClass) {
		this(styleClass, null);
	}

	public GraphElement(String styleClass, String id) {
		this.id = id;
		this.styleClass = styleClass;
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

	public void setId(String id) {
		this.id = id;
	}

	public void setParentElement(IGraphElement parent) {
		this.parentElement = parent;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
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
