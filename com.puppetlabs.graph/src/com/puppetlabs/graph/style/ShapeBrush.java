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
package com.puppetlabs.graph.style;

/**
 * Data type describing <i>filled</i> and <i>rounded</i> for figures that have "an area" in addition to what is
 * described by a {@link LineBrush}.
 * 
 */
public class ShapeBrush extends LineBrush {
	private boolean filled;

	private boolean rounded;

	public ShapeBrush(LineType lineType, Double lineWidth, boolean filled, boolean rounded) {
		super(lineType, lineWidth);
		this.filled = filled;
		this.rounded = rounded;
	}

	public boolean isFilled() {
		return filled;
	}

	public boolean isRounded() {
		return rounded;
	}
}
