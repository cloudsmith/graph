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
import org.cloudsmith.graph.IGraphElement;
import org.cloudsmith.graph.style.labels.ILabelTemplate;
import org.cloudsmith.graph.style.labels.LabelCell;
import org.cloudsmith.graph.style.labels.LabelRow;
import org.cloudsmith.graph.style.labels.LabelStringTemplate;
import org.cloudsmith.graph.style.labels.LabelTable;

import com.google.common.base.Function;

/**
 * A Factory for style values - see {@link IStyle}.
 * 
 */
public interface IStyleFactory {

	public IStyle<Alignment> align(Alignment x);

	public IStyle<Arrow> arrowHead(Arrow x);

	public IStyle<Arrow> arrows(Arrow x);

	public IStyle<Double> arrowScale(Double x);

	public IStyle<Arrow> arrowTail(Arrow x);

	/**
	 * Sets the background color. Note that it may be set to the name "transparent" which is useful
	 * when rendering a PNG.
	 * 
	 * @param x
	 * @return
	 */
	public IStyle<String> backgroundColor(String x);

	public IStyle<Integer> borderWidth(int x);

	public IStyle<Integer> cellBorderWidth(int x);

	public IStyle<Integer> cellPadding(int x);

	public IStyle<Integer> cellSpacing(int x);

	public IStyle<String> color(String x);

	public IStyle<Integer> colSpan(int x);

	public IStyle<Boolean> compound(boolean x);

	public IStyle<Boolean> concentrate(boolean x);

	public IStyle<Boolean> decorate(boolean x);

	public IStyle<EdgeDirection> direction(EdgeDirection x);

	public IStyle<String> fillColor(String x);

	public IStyle<Boolean> fixedSize(boolean x);

	public IStyle<String> fontFamily(String x);

	public IStyle<Integer> fontSize(int x);

	public IStyle<IClusterGraph> headCluster(IClusterGraph x);

	public IStyle<String> headPort(Compass x);

	public IStyle<String> headPort(String x);

	public IStyle<String> headPort(String x, Compass c);

	public IStyle<Double> height(double d);

	public IStyle<String> href(String x);

	public LabelCell labelCell(Function<IGraphElement, String> styleClass, Function<IGraphElement, String> f);

	public LabelCell labelCell(String styleClass, Function<IGraphElement, String> f);

	public LabelCell labelCell(String styleClass, String value);

	public LabelCell labelCell(String styleClass, String value, Span span);

	public IStyle<ILabelTemplate> labelFormat(ILabelTemplate x);

	public LabelRow labelRow(String styleClass, LabelCell... cells);

	public LabelStringTemplate labelStringTemplate(Function<IGraphElement, String> f);

	public LabelStringTemplate labelStringTemplate(String x);

	public LabelTable labelTable(String styleClass, LabelRow... rows);

	public IStyle<LineBrush> lineBrush(LineType lineType, double lineWidth);

	public IStyle<String> lineColor(String x);

	public IStyle<String> port(String x);

	public IStyle<RankDirection> rankDirectionStyle(RankDirection x);

	public IStyle<Double> rankSeparation(double x);

	public IStyle<Boolean> rendered(boolean x);

	public IStyle<Boolean> rendered(Function<IGraphElement, Boolean> f);

	public IStyle<EdgeRouting> routing(EdgeRouting x);

	public IStyle<Integer> rowSpan(int x);

	public IStyle<NodeShape> shape(NodeShape x);

	public IStyle<ShapeBrush> shapeBrush(LineType lineType, double lineWidth, boolean filled, boolean rounded);

	public IStyle<IClusterGraph> tailCluster(IClusterGraph x);

	public IStyle<String> tailPort(Compass x);

	public IStyle<String> tailPort(String x);

	public IStyle<String> tailPort(String x, Compass c);

	public IStyle<String> target(String x);

	public IStyle<String> tooltip(Function<IGraphElement, String> f);

	public IStyle<String> tooltip(String x);

	public IStyle<VerticalAlignment> verticalAlign(VerticalAlignment x);

	public IStyle<Double> width(double x);

}
