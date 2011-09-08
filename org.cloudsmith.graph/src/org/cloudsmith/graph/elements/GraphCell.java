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
import org.cloudsmith.graph.ITableCell;

/**
 * Implementation of IGraphCell
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

	public GraphCell(String value, int rowspan, int colspan, String styleClass, String id) {
		super(styleClass, id);
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
	public String getValue() {
		return value;
	}

	@Override
	public boolean isSeparator() {
		return false;
	}
}
