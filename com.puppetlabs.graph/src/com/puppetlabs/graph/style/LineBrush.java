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
package com.puppetlabs.graph.style;

/**
 * Data type describing {@link LineType} and lineWidth for edges.
 * 
 */
public class LineBrush {
	private final LineType lineType;

	private final Double lineWidth;

	public LineBrush(LineType lineType, Double lineWidth) {
		this.lineType = lineType;
		this.lineWidth = lineWidth;
	}

	public LineType getLineType() {
		return lineType;
	}

	public Double getLineWidth() {
		return lineWidth;
	}
}
