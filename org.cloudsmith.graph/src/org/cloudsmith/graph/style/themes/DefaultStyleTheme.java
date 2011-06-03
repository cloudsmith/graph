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
package org.cloudsmith.graph.style.themes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.cloudsmith.graph.ElementType;
import org.cloudsmith.graph.graphcss.GraphCSS;
import org.cloudsmith.graph.graphcss.IFunctionFactory;
import org.cloudsmith.graph.graphcss.Rule;
import org.cloudsmith.graph.graphcss.Select;
import org.cloudsmith.graph.graphcss.StyleSet;
import org.cloudsmith.graph.style.Arrow;
import org.cloudsmith.graph.style.EdgeDirection;
import org.cloudsmith.graph.style.IStyleFactory;
import org.cloudsmith.graph.style.LineType;
import org.cloudsmith.graph.style.NodeShape;
import org.cloudsmith.graph.style.VerticalAlignment;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.style.themes.IStyleTheme#getDefaultRules()
	 */
	@Override
	public GraphCSS getDefaultRules() {
		if(defaultRuleSet != null)
			return defaultRuleSet;

		defaultRuleSet = new GraphCSS();

		// SUBGRAPH
		defaultRuleSet.addRule(Select.element(ElementType.graph).withStyles( //
			styles.shapeBrush(LineType.dotted, 0.5, false, false), //
			styles.lineColor("#77a7c2") // "grey/blue" color
		));

		// NODE
		defaultRuleSet.addRule(Select.element(ElementType.vertex).withStyles( //
			styles.color("#2180c7"), //
			styles.fillColor("#ffffff"), //
			styles.lineColor("#b3b3b3"), //
			styles.fontFamily("Verdana"), //
			styles.fontSize(8), //
			styles.shape(NodeShape.rectangle), //
			styles.shapeBrush(LineType.solid, 0.5, true, true) //
		));

		// EDGE
		defaultRuleSet.addRule(Select.element(ElementType.edge).withStyles( //
			styles.color("#929292"), //
			styles.lineColor("#b3b3b3"), //
			styles.fontFamily("Verdana"), //
			styles.fontSize(7), //
			styles.lineBrush(LineType.solid, 0.5), //

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
	 * @see org.cloudsmith.graph.style.themes.IStyleTheme#getInstanceRules()
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
			styles.labelRow("LabelRow", styles.labelCell("LabelCell", functionFactory.label())))));

		StyleSet simpleLabelFormat = new StyleSet();
		simpleLabelFormat.put(styles.labelFormat(styles.labelStringTemplate(functionFactory.label())));

		// use a simple label format for edges, as this reduces dot data a lot
		//
		Collections.addAll(rules, //
			Select.element(ElementType.vertex).withStyle(labelFormatStyle),//
			Select.element(ElementType.edge).withStyle(simpleLabelFormat), //
			Select.element(ElementType.graph).withStyle(labelFormatStyle));

		// The label is only rendered if there is some text to render
		// (this avoids empty bordered or filled background areas)
		//
		Collections.addAll(rules, Select.element(ElementType.table).withStyles(//
			styles.rendered(functionFactory.notEmptyLabel()), // "#{not empty element.label}"
			styles.cellPadding(0), //
			styles.cellBorderWidth(0), //
			styles.cellSpacing(0), //
			styles.borderWidth(0)));

		// set TD valign to "bottom" (to make labels centered vertically in nodes)
		//
		Collections.addAll(rules, //
			Select.element(ElementType.cell).withStyle(//
				styles.verticalAlign(VerticalAlignment.bottom)));

		// // Default rules for HREF URL's
		// StyleSet urlFormatStyle = StyleSet.withImmutableStyles(styles.href("#{element.urlString}"));
		// Collections.addAll(rules, //
		// Select.element(ElementType.vertex).withStyle(urlFormatStyle), //
		// Select.element(ElementType.edge).withStyle(urlFormatStyle));

		// Root graph should be compound by default (allow head/tail clip on cluster border).
		Collections.addAll(rules, //
			Select.element(ElementType.graph).withStyle(styles.compound(true)));

		defaultInstanceRuleSet = Collections.unmodifiableList(rules);
		return defaultInstanceRuleSet;
	}
}
