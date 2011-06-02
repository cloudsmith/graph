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
package org.cloudsmith.graph.style.labels;

import org.cloudsmith.graph.IGraphElement;

import com.google.common.base.Function;

/**
 * Data for label format
 * 
 */
public class LabelTable implements ILabelTemplate {
	private LabelRow[] rows;

	private Function<IGraphElement, String> styleClass;

	public LabelTable(final Function<IGraphElement, String> styleClass, LabelRow... rows) {
		this.rows = rows;
		this.styleClass = styleClass;
	}

	public LabelTable(final String styleClass, LabelRow... rows) {
		this.rows = rows;
		this.styleClass = new Function<IGraphElement, String>() {
			public String apply(IGraphElement ge) {
				return styleClass;
			}
		};
	}

	public LabelRow[] getRows() {
		return rows;
	}

	public String getStyleClass(IGraphElement ge) {
		return styleClass.apply(ge);
	}
}
