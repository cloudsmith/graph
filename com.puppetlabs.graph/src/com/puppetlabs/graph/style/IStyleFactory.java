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

import java.util.Collection;
import java.util.Set;

import com.puppetlabs.graph.IClusterGraph;
import com.puppetlabs.graph.IGraphElement;
import com.puppetlabs.graph.style.labels.ILabelTemplate;
import com.puppetlabs.graph.style.labels.LabelCell;
import com.puppetlabs.graph.style.labels.LabelRow;
import com.puppetlabs.graph.style.labels.LabelTable;

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

	public LabelCell cellSeparator();

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

	public IStyle<String> id(Function<IGraphElement, String> idClassReplacer);

	public IStyle<String> id(String s);

	public LabelCell labelCell(Collection<String> styleClasses, Function<IGraphElement, ILabelTemplate> f);

	public LabelCell labelCell(Collection<String> styleClasses, Function<IGraphElement, ILabelTemplate> f, Span span);

	public LabelCell labelCell(Collection<String> styleClasses, LabelTable value);

	public LabelCell labelCell(Collection<String> styleClasses, LabelTable value, Span span);

	public LabelCell labelCell(Collection<String> styleClasses, String value);

	public LabelCell labelCell(Collection<String> styleClasses, String value, Span span);

	public LabelCell labelCell(Function<IGraphElement, Set<String>> styleClass,
			Function<IGraphElement, ILabelTemplate> f, Span span);

	public LabelCell labelCell(String styleClass, Function<IGraphElement, ILabelTemplate> f);

	public LabelCell labelCell(String styleClass, Function<IGraphElement, ILabelTemplate> f, Span span);

	public LabelCell labelCell(String styleClasse, LabelTable value);

	public LabelCell labelCell(String styleClass, LabelTable value, Span span);

	public LabelCell labelCell(String styleClass, String value);

	public LabelCell labelCell(String styleClass, String value, Span span);

	public IStyle<ILabelTemplate> labelFormat(ILabelTemplate x);

	public LabelRow labelRow(String styleClass, LabelCell... cells);

	public ILabelTemplate labelStringTemplate(Function<IGraphElement, String> f);

	public ILabelTemplate labelStringTemplate(String x);

	public LabelTable labelTable(String styleClass, LabelRow... rows);

	public ILabelTemplate labelTemplate(Function<IGraphElement, ILabelTemplate> f);

	public IStyle<LineBrush> lineBrush(LineType lineType, double lineWidth);

	public IStyle<String> lineColor(String x);

	public IStyle<?> mclimit(double d);

	public IStyle<String> port(String x);

	public IStyle<RankDirection> rankDirectionStyle(RankDirection x);

	public IStyle<Double> rankSeparation(double x);

	public IStyle<?> remincross(boolean b);

	public IStyle<Boolean> rendered(boolean x);

	public IStyle<Boolean> rendered(Function<IGraphElement, Boolean> f);

	public IStyle<EdgeRouting> routing(EdgeRouting x);

	public LabelRow rowSeparator();

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

	public IStyle<String> tooltipForHead(String x);

	public IStyle<String> tooltipForLabel(String x);

	public IStyle<String> tooltipForTail(String x);

	public IStyle<VerticalAlignment> verticalAlign(VerticalAlignment x);

	public IStyle<Double> weight(double x);

	public IStyle<Double> width(double x);

}
