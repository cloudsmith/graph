/**
 * Copyright (c) 2006-2011 Cloudsmith Inc. and other contributors, as listed below.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   AT&T - initial API
 *   Cloudsmith
 * 
 */

package com.puppetlabs.graph.style;

public enum NodeShape {
	/** rectangle */
	rectangle,
	/** rectangle with 1:1 aspect ration */
	square,
	/** oval */
	ellipse,
	/** circle */
	circle,
	/** double bordered circle */
	doublecircle,
	/** filled small circle */
	point,
	/** triangle with rounded corners, tip up */
	egg, //
	/** triangle tip up */
	triangle,
	/** triangle, tip down */
	invtriangle, //
	/** label only */
	none,
	/** diamond */
	diamond,
	/** box narrower at top */
	trapezium,
	/** box narrower at bottom */
	invtrapezium,
	/** tilted rectangle */
	parallelogram,
	/** house silhouette seen from side, tip up */
	house,
	/** house with tip down */
	invhouse, //
	/** 5 sided shape */
	pentagon,
	/** 6 sided shape */
	hexagon,
	/** 7 sided shape */
	septagon, //
	/** 8 sided shape */
	octagon,
	/** double bordered 8 sided shape */
	doubleoctagon,
	/** triple bordered 8 sided shape */
	tripeloctagon,
	/** diamond with diagonal lines cutting corners */
	Mdiamond,
	/** square with diagonal lines cutting corners */
	Msquare,
	/** circle with bottom and top segments */
	Mcircle,
	/**
	 * treats the label as a table specification. Each | starts a new cell - and a list of cells
	 * enclosed in { } reverses the direction horizontal (default) and vertical.
	 * The label "a |{b | c | d}|e " produces the layout:
	 * | |b| |
	 * |a|c|d|
	 * | |d| |
	 * Can be combined with node style "rounded" to produce a rounded rect.
	 */
	record,
	/** resembles paper folder */
	folder,
	/** rectangle with folded corner */
	note,
	/** resembles tabbed window */
	tab,
	/** A rectangle in 3d */
	box3d,
	/** a rectangle with two "hinges" on the left side */
	component, ;
}
