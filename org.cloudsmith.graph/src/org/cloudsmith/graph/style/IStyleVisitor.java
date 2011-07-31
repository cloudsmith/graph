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

import org.cloudsmith.graph.IClusterGraph;
import org.cloudsmith.graph.style.labels.ILabelTemplate;

/**
 * A visitor for IStyle
 * 
 */
public interface IStyleVisitor {

	public void align(Alignment x);

	public void arrowHead(Arrow x);

	public void arrows(Arrow x);

	public void arrowScale(double x);

	public void arrowTail(Arrow x);

	public void backgroundColor(String x);

	public void borderWidth(int x);

	public void cellBorderWidth(int x);

	public void cellPadding(int x);

	public void cellSpacing(int x);

	public void color(String x);

	public void colSpan(int x);

	public void compound(boolean value);

	public void concentrate(boolean x);

	public void decorate(boolean x);

	public void direction(EdgeDirection x);

	public void fillColor(String value);

	public void fixedSize(boolean x);

	public void fontFamily(String x);

	public void fontSize(int x);

	public void fromCluster(IClusterGraph value);

	public void headPort(String x);

	public void height(double d);

	public void href(String x);

	public void labelFormat(ILabelTemplate x);

	public void lineBrush(LineBrush brush);

	public void lineColor(String x);

	public void port(String x);

	public void rankDirection(RankDirection x);

	public void rankSeparation(double x);

	public void rendered(boolean x);

	public void routing(EdgeRouting x);

	public void rowSpan(int x);

	public void shape(NodeShape x);

	public void shapeBrush(ShapeBrush brush);

	public void tailPort(String x);

	public void target(String x);

	public void toCluster(IClusterGraph value);

	public void tooltip(String x);

	public void tooltipForHead(String value);

	public void tooltipForLabel(String value);

	public void tooltipForTail(String value);

	public void verticalAlign(VerticalAlignment x);

	public void weight(double value);

	public void width(double x);

}
