/**
 * Copyright (c) 2011 Cloudsmith Inc. and other contributors, as listed below.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Cloudsmith
 * 
 */
package org.cloudsmith.graph.style;

/**
 * Data type defining span.
 * 
 */
public class Span {
	public static final Span SPAN_1x1 = new Span(1, 1);

	public static Span colSpan(int colspan) {
		return new Span(1, colspan);
	}

	public static Span rowSpan(int rowspan) {
		return new Span(rowspan, 1);
	}

	int rowspan = 1;

	int colspan = 1;

	public Span() {
	}

	public Span(int rowspan, int colspan) {
		this.rowspan = rowspan;
		this.colspan = colspan;
	}

	public int getColspan() {
		return colspan;
	}

	public int getRowspan() {
		return rowspan;
	}
}
