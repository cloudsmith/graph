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
import com.puppetlabs.graph.ITableCell;

/**
 * Implementation of ITableCell
 * 
 */
public class GraphCell extends GraphElement implements ITableCell {

	public static class SeparatorCell extends GraphCell {
		public SeparatorCell() {
			super("", SeparatorCell.class.getName());
		}

		@Override
		public boolean isSeparator() {
			return true;
		}
	}

	private String value;

	private GraphTable table;

	// private GraphCell(String value, int rowspan, int colspan, String styleClass, String id) {
	// super(styleClass, id);
	// this.value = value;
	// }
	//
	// private GraphCell(String value, int rowspan, int colspan, Collection<String> styleClass, String id) {
	// super(styleClass, id);
	// this.value = value;
	// }

	public GraphCell(String value, Collection<String> styleClasses) {
		this(value, styleClasses, null);
	}

	public GraphCell(String value, Collection<String> styleClasses, String id) {
		super(styleClasses, id);
		this.value = value;
	}

	/**
	 * Creates a default cell with null id.
	 * 
	 * @param styleClass
	 */
	public GraphCell(String value, String styleClass) {
		this(value, styleClass, null);
	}

	public GraphCell(String value, String styleClass, String id) {
		super(styleClass, id);
		this.value = value;
	}

	@Override
	public ElementType getElementType() {
		return ElementType.cell;
	}

	@Override
	public GraphTable getTableContents() {
		return this.table;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public boolean isSeparator() {
		return false;
	}

	public void setTableContent(GraphTable gt) {
		this.table = gt;
		gt.setParentElement(this);
		String id = gt.getId();
		if(id == null || id.length() == 0)
			gt.setId("t");

	}
}
