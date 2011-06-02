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

import java.util.ArrayList;
import java.util.List;

import org.cloudsmith.graph.ElementType;
import org.cloudsmith.graph.ITableCell;
import org.cloudsmith.graph.ITableRow;

/**
 * Implementation of a GraphRow.
 * 
 */
public class GraphRow extends GraphElement implements ITableRow {
	private ArrayList<ITableCell> cells;

	/**
	 * Creates a default row with the id "row".
	 * Note that styles can not be set on the GrapRow, but a style class is useful
	 * in containment rules for cells.
	 * 
	 * @param styleClass
	 */
	public GraphRow(String styleClass) {
		super(styleClass, null);
		cells = new ArrayList<ITableCell>(1);
	}

	public void addCell(GraphCell cell) {
		cells.add(cell);
		String id = cell.getId();
		if(id == null || id.length() < 1)
			cell.setId("r" + cells.size());
		cell.setParentElement(this);
	}

	public List<ITableCell> getCells() {
		return cells;
	}

	@Override
	public ElementType getElementType() {
		return ElementType.row;
	}
}
