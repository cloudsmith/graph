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
package com.puppetlabs.graph.style.themes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.puppetlabs.graph.ElementType;
import com.puppetlabs.graph.graphcss.GraphCSS;
import com.puppetlabs.graph.graphcss.IFunctionFactory;
import com.puppetlabs.graph.graphcss.Rule;
import com.puppetlabs.graph.graphcss.Select;
import com.puppetlabs.graph.graphcss.StyleSet;
import com.puppetlabs.graph.style.Alignment;
import com.puppetlabs.graph.style.Arrow;
import com.puppetlabs.graph.style.EdgeDirection;
import com.puppetlabs.graph.style.IStyleFactory;
import com.puppetlabs.graph.style.LineType;
import com.puppetlabs.graph.style.NodeShape;
import com.puppetlabs.graph.style.VerticalAlignment;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Definition of useful default styles and rules.
 * 
 */
@Singleton
public class DefaultStyleTheme implements IStyleTheme {
	@Inject
	private IStyleFactory styles;

	@Inject
	IFunctionFactory functionFactory;

	private GraphCSS defaultRuleSet;

	private Collection<Rule> defaultInstanceRuleSet;

	public static final String THEME_EDGE_CONTAINMENT = "Containment";

	public static final String THEME_EDGE_REFERENCE = "Reference";

	public static final String COLOR__LIGHT_GREY = "#cccccc";

	public static final String COLOR__MID_GREY = "#b3b3b3";

	public static final String COLOR__DARK_GREY = "#929292";

	public static final String COLOR__DARKEST_GREY = "#646464";

	public static final String COLOR__ALMOST_BLACK = "#444444";

	public static final String COLOR__MID_BLUE = "#2180c7";

	public static final String COLOR__LIGHT_GREY_BLUE = "#77a7c2";

	public static final String COLOR__WHITE = "#ffffff";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.puppetlabs.graph.style.themes.IStyleTheme#defaultFontFamily()
	 */
	@Override
	public String defaultFontFamily() {
		return "Verdana";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.puppetlabs.graph.style.themes.IStyleTheme#defaultPointSize()
	 */
	@Override
	public int defaultPointSize() {
		return 8;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.puppetlabs.graph.style.themes.IStyleTheme#getDefaultRules()
	 */
	@Override
	public GraphCSS getDefaultRules() {
		if(defaultRuleSet != null)
			return defaultRuleSet;

		defaultRuleSet = new GraphCSS();

		// GRAPH and SUBGRAPHS
		defaultRuleSet.addRule(Select.element(ElementType.graph).withStyles( //
			styles.shapeBrush(LineType.dotted, 0.5, false, false), //
			styles.lineColor(COLOR__LIGHT_GREY_BLUE), // "grey/blue" color
			styles.tooltip(""), // or the graph name is displayed over the entire surface
			styles.mclimit(4.0), // try harder to eliminate cross-overs
			styles.remincross(true) // run mincross a second time
		));

		// NODE
		defaultRuleSet.addRule(Select.element(ElementType.vertex).withStyles( //
			styles.color(COLOR__MID_BLUE), //
			styles.fillColor(COLOR__WHITE), //
			styles.lineColor(COLOR__MID_GREY), //
			styles.fontFamily(defaultFontFamily()), //
			styles.fontSize(defaultPointSize()), //
			styles.shape(NodeShape.rectangle), //
			styles.shapeBrush(LineType.solid, 0.5, true, true), //

			// Tooltip handling in graphviz is not good, if empty, the label is used, and if it is a <TABLE>
			// then the text "<TABLE>" is displayed !!, setting it to "" means it is "unset" and the default
			// is used. Nodes with table based labels, must set something meaningful as tooltip.
			// There is support for translation of empty strings to distinct text that can be postprocessed
			// for textual output. (Tooltips are only relevant for SVG anyway).
			styles.tooltip("") //
		));

		// EDGE
		defaultRuleSet.addRule(Select.element(ElementType.edge).withStyles( //
			styles.color(COLOR__DARK_GREY), //
			styles.lineColor(COLOR__MID_GREY), //
			styles.fontFamily(defaultFontFamily()), //
			styles.fontSize(7), //
			styles.lineBrush(LineType.solid, 0.5), //

			// the automatic tooltip handling in graphviz is wacky, and does not work as stated in documentation
			styles.tooltip("\\L"), // use the label (if it exists) as tooltip for the edge's "line"
			styles.tooltipForLabel("\\L"), // use the label of the edge as the tooltip for the edge label !!

			styles.arrowHead(Arrow.vee), //
			styles.arrowTail(Arrow.none), //
			styles.direction(EdgeDirection.forward), //
			styles.arrowScale(0.5) //
		// styles.headPort(Compass.n); use default - point to center as the default
		));

		return defaultRuleSet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.puppetlabs.graph.style.themes.IStyleTheme#getInstanceRules()
	 */
	@Override
	public Collection<Rule> getInstanceRules() {
		if(defaultInstanceRuleSet != null)
			return defaultInstanceRuleSet;
		final ArrayList<Rule> rules = Lists.newArrayList();
		// Default rules for label formatting for vertex, edge, and graph
		//
		StyleSet labelFormatStyle = new StyleSet();
		// a table with one row and one cell that gets the label of the element
		labelFormatStyle.put(styles.labelFormat(//
		styles.labelTable("Label", //
			styles.labelRow("LabelRow", //
				styles.labelCell("LabelCell", functionFactory.label() //
				))) //
		));

		StyleSet simpleLabelFormat = new StyleSet();
		simpleLabelFormat.put(styles.labelFormat(styles.labelTemplate(functionFactory.label())));

		// use a simple label format for edges, as this reduces dot data a lot
		//
		Collections.addAll(rules, //
			Select.element(ElementType.vertex).withStyle(labelFormatStyle),//
			Select.element(ElementType.edge).withStyle(simpleLabelFormat), //
			Select.element(ElementType.graph).withStyle(labelFormatStyle) //
		);

		// The label of a graph
		Collections.addAll(rules, //
			Select.and(Select.table("Label"), Select.parent(Select.graph())).withStyles( //
				styles.cellPadding(5), // or it is too close to the graph nodes
				styles.fontFamily(defaultFontFamily()), //
				styles.fontSize(12), //
				styles.color(COLOR__DARK_GREY)));

		// The label is only rendered if there is some text to render
		// (this avoids empty bordered or filled background areas)
		//
		Collections.addAll(rules, Select.element(ElementType.table).withStyles(//
			styles.rendered(functionFactory.notEmptyLabel()), // "#{not empty element.label}"
			styles.cellPadding(0), //
			styles.cellBorderWidth(0), //
			styles.cellSpacing(0), //
			styles.borderWidth(0) //
		));

		// set TD valign to "bottom" (to make labels centered vertically in nodes)
		//
		Collections.addAll(rules, //
			Select.element(ElementType.cell).withStyle(//
				styles.verticalAlign(VerticalAlignment.bottom) //
			));

		// // Default rules for HREF URL's
		// StyleSet urlFormatStyle = StyleSet.withImmutableStyles(styles.href("#{element.urlString}"));
		// Collections.addAll(rules, //
		// Select.element(ElementType.vertex).withStyle(urlFormatStyle), //
		// Select.element(ElementType.edge).withStyle(urlFormatStyle));

		// Root graph should be compound by default (allow head/tail clip on cluster border).
		Collections.addAll(rules, //
			Select.element(ElementType.graph).withStyle(//
				styles.compound(true) //
			), //
				// place root graph label at top left position (the default for subgraphs).
			Select.graph().withStyles(//
				styles.align(Alignment.left), styles.verticalAlign(VerticalAlignment.top)));

		// Containment class edges should have a diamond by default
		Collections.addAll(rules, Select.edge(THEME_EDGE_CONTAINMENT).withStyles( // styles
			styles.direction(EdgeDirection.both), //
			styles.arrowTail(Arrow.diamond) //
		));

		// // SVG output adds tooltips for every element and the tooltip is the internal ID
		// // unfortunately, it is not possible to set a tooltip at graph level
		// Collections.addAll(rules, //
		// new Select.Element(ElementType.NOT_GRAPH).withStyle(//
		// styles.tooltip("-")//
		// ));

		defaultInstanceRuleSet = Collections.unmodifiableList(rules);
		return defaultInstanceRuleSet;
	}
}
