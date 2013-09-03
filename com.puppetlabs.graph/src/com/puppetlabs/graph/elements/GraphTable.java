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
import java.util.Set;

import org.cloudsmith.graph.ElementType;
import org.cloudsmith.graph.ITable;
import org.cloudsmith.graph.ITableRow;

/**
 * An implementation of ITable.
 * 
 */
public class GraphTable extends GraphElement implements ITable {
	private ArrayList<ITableRow> rows;

	/**
	 * Creates a default table with the id "label".
	 * (Which is a bit superflous as a styling rule for "#label" will find all
	 * tables just as the element type 'table' will).
	 * When creating a graph table for a graph element - the styleClass of the graph
	 * element should be applied to the table as well.
	 * 
	 * @param styleClass
	 */
	public GraphTable(Set<String> styleClass) {
		super(styleClass, "label");
		rows = new ArrayList<ITableRow>(1);
	}

	/**
	 * Creates a default table with the id "label".
	 * (Which is a bit superflous as a styling rule for "#label" will find all
	 * tables just as the element type 'table' will).
	 * When creating a graph table for a graph element - the styleClass of the graph
	 * element should be applied to the table as well.
	 * 
	 * @param styleClass
	 */
	public GraphTable(String styleClass) {
		super(styleClass, "label");
		rows = new ArrayList<ITableRow>(1);
	}

	public void addRow(GraphRow row) {
		rows.add(row);
		String id = row.getId();
		if(id == null || id.length() < 1)
			row.setId("r" + rows.size());
		row.setParentElement(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.impl.GraphElement#getElementType()
	 */
	@Override
	public ElementType getElementType() {
		return ElementType.table;
	}

	public List<ITableRow> getRows() {
		return rows;
	}
}
