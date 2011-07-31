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
package org.cloudsmith.graph.style;

import org.cloudsmith.graph.style.labels.ILabelTemplate;

public enum StyleType {
	/** Sets the text color for labels. Value is a {@link String}. */
	color,

	/** Sets the fill color for vertices. Value is a {@link String}. */
	backgroundColor,

	/** Sets the border color for vertices, and line color for edges. Value is a {@link String}. */
	lineColor,

	/** Sets the font size in points for labels. Value is an {@link Integer}. */
	fontSize,

	/** Sets the font family for labels. Value is a {@link String}. */
	fontFamily,

	/** Sets the shape for vertices. Value is a {@link NodeShape}. */
	shape,

	/** Sets both head and tail arrow to the same type. Value is an {@link Arrow}. */
	arrows,

	/** Sets the arrow type for the head (target) end. Value is an {@link Arrow}. */
	arrowHead,

	/** Sets the arrow type for the tail (source) end. Value is an {@link Arrow}. */
	arrowTail,

	/** Sets the direction - of the edge. Value is an {@link EdgeDirection}. */
	direction,

	/** Sets the brush/style values for vertices . Value is a {@link ShapeBrush}. */
	shapeBrush,

	/** Sets the brush/style values for edges. Value is a {@link LineBrush}. */
	lineBrush,

	/** Sets the alignment of a table or table cell. Value is an {@link Alignment}. */
	align,

	/** Sets the vertical alignment of a table or table cell. Value is an {@link VerticalAlignment}. */
	valign,

	/** defines a logical port name for a table or cell. Value is a {@link String}. */
	port,

	/**
	 * directs the head to a defined logical port of the node's label
	 * (table or cell) - can specify connector point. Value is a {@link String}.
	 */
	headPort,

	/**
	 * directs the tail to a defined logical port on the node's label
	 * (table or cell) - can specify connector point. Value is a {@link String}.
	 */
	tailPort,

	/** number of rows to span for a cell. Value is an {@link Integer}. */
	rowSpan,

	/**
	 * sets the spacing of all cells on a table, and individually for a cell (?).
	 * Value is an {@link Integer}.
	 */
	cellSpacing,

	/**
	 * sets the padding of all cells on a table, and individually for a cell.
	 * Value is an {@link Integer}.
	 */
	cellPadding,

	/**
	 * sets the border width of all cells on a table. Value is an {@link Integer}.
	 */
	cellBorderWidth,

	/**
	 * sets the border width of a table, and individually for cells - for other elements,
	 * the border width is set in the element's brush (lineBrush, shapeBrush). Value is an {@link Integer}.
	 */
	borderWidth,

	/** defines a target (as in HTML A element) for use with url. Value is a {@link String}. */
	target,

	/** sets the tooltip of an element with a URL - i.e. HTML TITLE. Value is a {@link String}. */
	tooltip,

	/** sets the tooltip of an edge's label. Value is a {@link String}. */
	tooltipForLabel,

	/** sets the tooltip of an edge's Head. Value is a {@link String}. */
	tooltipForHead,

	/** sets the tooltip of an edge's Tail. Value is a {@link String}. */
	tooltipForTail,

	/**
	 * sets the width (of a table, cell, node). Node's width is in points 0.02 minimum.
	 * Value is a {@link Double}.
	 */
	width,

	/**
	 * sets the height (of a table, cell, node) Node's height is in points 0.02 is minimum.
	 * Value is a {@link Double}.
	 */
	height,

	/**
	 * sets the element to have fixed size. Value is a {@link Boolean}.
	 */
	fixedSize,

	/**
	 * decorates an edge with a line from the edge to the label that can be positioned elsewhere.
	 * Value is a {@link Boolean}.
	 */
	decorate,

	/**
	 * specifies a clickable url for node, edge, table, cell.
	 * Value is a {@link String}, but should be a valid URL to work.
	 */
	href,

	/**
	 * defines the scaling of arrow heads - default is 1.0.
	 * Value is a {@link Double}.
	 */
	arrowScale,

	/**
	 * Defines the formatting of a label - the default takes the simple label from a labeled element, but a complex
	 * table style with multiple rows and individually styled cells can be used.
	 * Value is an {@link ILabelTemplate}.
	 */
	labelFormat,

	/**
	 * A string EL that if it evaluates to "false" (case independent) indicates that the element should not
	 * be rendered. Value is a {@link String}, but should be an EL or "false" or "true".
	 */
	rendered,

	/**
	 * controls if edges are splines, straight lines, or poly-straight lines. Value is an {@link EdgeRouting}.
	 */
	edgeRouting,

	/**
	 * concentrates edges (they join along the route). Value is a {@link Boolean}.
	 */
	concentrate,

	/**
	 * The direction TP (top to bottom) or LR (left to right). Value is a {@link RankDirection}.
	 */
	rankDirection,

	/**
	 * The (minimum) separation distance in inches between ranks. value is a {@link Double}.
	 */
	rankSeparation,

	/**
	 * The number of columns to span for a cell. Value is an {@link Integer}.
	 */
	colSpan,

	/**
	 * The background/fill color of nodes and clusters. Also see {@link #backgroundColor}.
	 */
	fillColor,

	/**
	 * True if it should be possible to clip edges at cluster border.
	 */
	compound,

	/**
	 * Clips an edge at the to/head end to a given (containing) cluster.
	 */
	toCluster,

	/**
	 * Clips an edge at the from/tail end to a given (containing) cluster.
	 */
	fromCluster,

	/**
	 * Sets the weight of an edge (default 1.0). In dot, the heavier, the shorter, straighter and more vertical
	 * the edge will be.
	 */
	weight,

	/**
	 * Multiplicative factor (default 1.0) used to alter the graphviz parameters MinQuit (8) and MaxIter (24)
	 * used during crossing minimization. The higher the number the more effort is used to avoid crossings.
	 */
	mclimit,

	/**
	 * Run crossing elimination a second time if set to true. (Dot only).
	 */
	remincross,

	;
}
