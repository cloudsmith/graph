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

import static org.cloudsmith.graph.ElementType.ANY;
import static org.cloudsmith.graph.ElementType.CELL;
import static org.cloudsmith.graph.ElementType.CELL_AND_TABLE_AND_VERTEX;
import static org.cloudsmith.graph.ElementType.CLUSTER_AND_GRAPH_AND_CELL_AND_TABLE;
import static org.cloudsmith.graph.ElementType.CLUSTER_AND_VERTEX;
import static org.cloudsmith.graph.ElementType.EDGE;
import static org.cloudsmith.graph.ElementType.EDGE_AND_GRAPH_AND_VERTEX;
import static org.cloudsmith.graph.ElementType.GRAPH;
import static org.cloudsmith.graph.ElementType.GRAPH_AND_VERTEX;
import static org.cloudsmith.graph.ElementType.NOT_GRAPH;
import static org.cloudsmith.graph.ElementType.NOT_SUBGRAPH;
import static org.cloudsmith.graph.ElementType.TABLE;
import static org.cloudsmith.graph.ElementType.TABLE_AND_CELL;
import static org.cloudsmith.graph.ElementType.VERTEX;

import java.util.Set;

import org.cloudsmith.graph.ElementType;
import org.cloudsmith.graph.IGraphElement;
import org.cloudsmith.graph.graphcss.IFunctionFactory;
import org.cloudsmith.graph.style.labels.ILabelTemplate;
import org.cloudsmith.graph.style.labels.LabelCell;
import org.cloudsmith.graph.style.labels.LabelRow;
import org.cloudsmith.graph.style.labels.LabelStringTemplate;
import org.cloudsmith.graph.style.labels.LabelTable;

import com.google.common.base.Function;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * An IStyleFacory implementation for graphviz dot language.
 * 
 */
@Singleton
public class StyleFactory implements IStyleFactory {
	protected static abstract class AbstractStyle<T> implements IStyle<T> {
		private StyleType style;

		private T value;

		private Function<IGraphElement, T> function;

		private Set<ElementType> types;

		public AbstractStyle(StyleType style, Function<IGraphElement, T> function) {
			this.style = style;
			this.function = function;
		}

		public AbstractStyle(StyleType style, T value) {
			this.style = style;
			this.value = value;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.cloudsmith.graph.impl.style.IStyle#getStyle()
		 */
		@Override
		public StyleType getStyleType() {
			return style;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.cloudsmith.graph.impl.style.IStyle#getValue(ge)
		 */
		@Override
		public T getValue(IGraphElement ge) {
			return function != null
					? function.apply(ge)
					: value;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.cloudsmith.graph.style.IStyle#isFunction()
		 */
		@Override
		public boolean isFunction() {
			return function != null;
		}

		protected void setTypes(Set<ElementType> types) {
			this.types = types;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.cloudsmith.graph.impl.style.IStyle#supports(org.cloudsmith.graph.ElementType)
		 */
		@Override
		public boolean supports(ElementType type) {
			return types.contains(type) || types.contains(ElementType.ANY);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.cloudsmith.graph.IStyle#supports(java.util.Set)
		 */
		@Override
		public boolean supports(Set<ElementType> types) {
			return this.types.containsAll(types);
		}
	}

	public static class Align extends AbstractStyle<Alignment> {
		public Align(Alignment align) {
			super(StyleType.align, align);
			setTypes(TABLE_AND_CELL);
		}

		@Override
		public void visit(IGraphElement ge, IStyleVisitor visitor) {
			visitor.align(getValue(ge));
		}
	}

	public static class ArrowHead extends AbstractStyle<Arrow> {
		public ArrowHead(Arrow arrow) {
			super(StyleType.arrowHead, arrow);
			setTypes(EDGE);
		}

		@Override
		public void visit(IGraphElement ge, IStyleVisitor visitor) {
			visitor.arrowHead(getValue(ge));
		}
	}

	public static class Arrows extends AbstractStyle<Arrow> {
		public Arrows(Arrow arrow) {
			super(StyleType.arrows, arrow);
			setTypes(EDGE);
		}

		@Override
		public void visit(IGraphElement ge, IStyleVisitor visitor) {
			visitor.arrows(getValue(ge));
		}

	}

	public static class ArrowScale extends DoubleStyle {
		public ArrowScale(Double factor) {
			super(StyleType.arrowScale, factor);
			setTypes(EDGE);
		}

		@Override
		public void visit(IGraphElement ge, IStyleVisitor visitor) {
			visitor.arrowScale(getValue(ge));
		}

	}

	public static class ArrowTail extends AbstractStyle<Arrow> {
		public ArrowTail(Arrow arrow) {
			super(StyleType.arrowTail, arrow);
			setTypes(EDGE);
		}

		@Override
		public void visit(IGraphElement ge, IStyleVisitor visitor) {
			visitor.arrowTail(getValue(ge));
		}

	}

	public static class BackgroundColor extends StringStyle {
		public BackgroundColor(String colorString) {
			super(StyleType.backgroundColor, colorString);
			setTypes(CLUSTER_AND_GRAPH_AND_CELL_AND_TABLE);
		}

		@Override
		public void visit(IGraphElement ge, IStyleVisitor visitor) {
			visitor.backgroundColor(getValue(ge));
		}

	}

	public abstract static class BooleanStyle extends AbstractStyle<Boolean> {
		public BooleanStyle(StyleType style, Boolean value) {
			super(style, value);
		}

		public BooleanStyle(StyleType style, Function<IGraphElement, Boolean> value) {
			super(style, value);
		}
	}

	public static class BorderWidth extends IntegerStyle {
		public BorderWidth(Integer value) {
			super(StyleType.borderWidth, value);
			setTypes(TABLE_AND_CELL);
		}

		@Override
		public void visit(IGraphElement ge, IStyleVisitor visitor) {
			visitor.borderWidth(getValue(ge));
		}

	}

	public static class CellBorderWidth extends IntegerStyle {
		public CellBorderWidth(Integer value) {
			super(StyleType.cellBorderWidth, value);
			setTypes(TABLE);
		}

		@Override
		public void visit(IGraphElement ge, IStyleVisitor visitor) {
			visitor.cellBorderWidth(getValue(ge));
		}

	}

	public static class CellPadding extends IntegerStyle {
		public CellPadding(Integer value) {
			super(StyleType.cellPadding, value);
			setTypes(TABLE_AND_CELL);
		}

		@Override
		public void visit(IGraphElement ge, IStyleVisitor visitor) {
			visitor.cellPadding(getValue(ge));
		}

	}

	public static class CellSpacing extends IntegerStyle {
		public CellSpacing(Integer value) {
			super(StyleType.cellSpacing, value);
			setTypes(TABLE_AND_CELL);
		}

		@Override
		public void visit(IGraphElement ge, IStyleVisitor visitor) {
			visitor.cellSpacing(getValue(ge));
		}

	}

	public static class Color extends StringStyle {
		public Color(String colorString) {
			super(StyleType.color, colorString);
			setTypes(ANY);
		}

		@Override
		public void visit(IGraphElement ge, IStyleVisitor visitor) {
			visitor.color(getValue(ge));
		}

	}

	public static class ColSpan extends IntegerStyle {
		public ColSpan(Integer value) {
			super(StyleType.colSpan, value);
			setTypes(CELL);
		}

		@Override
		public void visit(IGraphElement ge, IStyleVisitor visitor) {
			visitor.colSpan(getValue(ge));
		}

	}

	public static class Compound extends BooleanStyle {
		public Compound(Boolean flag) {
			super(StyleType.compound, flag);
			setTypes(GRAPH);
		}

		@Override
		public void visit(IGraphElement ge, IStyleVisitor visitor) {
			visitor.compound(getValue(ge));
		}

	}

	public static class Concentrate extends BooleanStyle {
		public Concentrate(Boolean flag) {
			super(StyleType.concentrate, flag);
			setTypes(GRAPH);
		}

		@Override
		public void visit(IGraphElement ge, IStyleVisitor visitor) {
			visitor.concentrate(getValue(ge));
		}

	}

	public static class Decorate extends BooleanStyle {
		public Decorate(Boolean flag) {
			super(StyleType.decorate, flag);
			setTypes(EDGE);
		}

		@Override
		public void visit(IGraphElement ge, IStyleVisitor visitor) {
			visitor.decorate(getValue(ge));
		}

	}

	public static class Direction extends AbstractStyle<EdgeDirection> {
		public Direction(EdgeDirection dir) {
			super(StyleType.direction, dir);
			setTypes(EDGE);
		}

		@Override
		public void visit(IGraphElement ge, IStyleVisitor visitor) {
			visitor.direction(getValue(ge));
		}

	}

	protected abstract static class DoubleStyle extends AbstractStyle<Double> {
		public DoubleStyle(StyleType style, Double value) {
			super(style, value);
		}

	}

	public static class EdgeBrush extends AbstractStyle<LineBrush> {
		public EdgeBrush(LineType lineType, Double lineWidth) {
			super(StyleType.lineBrush, new LineBrush(lineType, lineWidth));
			setTypes(EDGE);
		}

		@Override
		public void visit(IGraphElement ge, IStyleVisitor visitor) {
			visitor.lineBrush(getValue(ge));
		}

	}

	public abstract static class EdgePortStyle extends StringStyle {
		public EdgePortStyle(StyleType style, Compass portName) {
			super(style, portName == Compass.NONE
					? ""
					: portName.toString());
			setTypes(EDGE);
		}

		public EdgePortStyle(StyleType style, String portName) {
			super(style, portName);
			setTypes(EDGE);
		}

		public EdgePortStyle(StyleType style, String portName, Compass compass) {
			this(style, portName + ((compass == Compass.NONE)
					? ""
					: ":" + compass.toString()));
		}

	}

	public static class FillColor extends StringStyle {
		public FillColor(String colorString) {
			super(StyleType.backgroundColor, colorString);
			setTypes(CLUSTER_AND_VERTEX);
		}

		@Override
		public void visit(IGraphElement ge, IStyleVisitor visitor) {
			visitor.fillColor(getValue(ge));
		}

	}

	public static class FixedSize extends BooleanStyle {
		public FixedSize(Boolean flag) {
			super(StyleType.fixedSize, flag);
			setTypes(CELL_AND_TABLE_AND_VERTEX);
		}

		@Override
		public void visit(IGraphElement ge, IStyleVisitor visitor) {
			visitor.fixedSize(getValue(ge));
		}

	}

	public static class FontFamily extends StringStyle {
		public FontFamily(String name) {
			super(StyleType.fontFamily, name);
			setTypes(ANY);
		}

		@Override
		public void visit(IGraphElement ge, IStyleVisitor visitor) {
			visitor.fontFamily(getValue(ge));
		}

	}

	public static class FontSize extends IntegerStyle {
		public FontSize(Integer size) {
			super(StyleType.fontSize, size);
			setTypes(ANY);
		}

		@Override
		public void visit(IGraphElement ge, IStyleVisitor visitor) {
			visitor.fontSize(getValue(ge));
		}

	}

	/**
	 * Specifies a port where the head of an edge should connect. This is either a portname, or a
	 * combination of port name and a compass "direction". Everything an edge can connect to has the
	 * compass directions as default ports.
	 */
	public static class HeadPort extends EdgePortStyle {
		public HeadPort(Compass portName) {
			super(StyleType.headPort, portName);
		}

		public HeadPort(String portName) {
			super(StyleType.headPort, portName);
		}

		public HeadPort(String portName, Compass compass) {
			super(StyleType.headPort, portName, compass);
		}

		@Override
		public void visit(IGraphElement ge, IStyleVisitor visitor) {
			visitor.headPort(getValue(ge));
		}

	}

	public static class Height extends DoubleStyle {
		public Height(Double value) {
			super(StyleType.height, value);
			setTypes(CELL_AND_TABLE_AND_VERTEX);
		}

		@Override
		public void visit(IGraphElement ge, IStyleVisitor visitor) {
			visitor.height(getValue(ge));
		}

	}

	public static class Href extends StringStyle {
		public Href(String urlString) {
			super(StyleType.href, urlString);
			setTypes(NOT_SUBGRAPH);
		}

		@Override
		public void visit(IGraphElement ge, IStyleVisitor visitor) {
			visitor.href(getValue(ge));
		}

	}

	protected abstract static class IntegerStyle extends AbstractStyle<Integer> {
		public IntegerStyle(StyleType style, Integer value) {
			super(style, value);
		}
	}

	public static class LabelFormat extends AbstractStyle<ILabelTemplate> {
		/**
		 * Set the table template, or if you don't want a label at all, set the table to null.
		 * 
		 * @param t
		 */
		public LabelFormat(ILabelTemplate t) {
			super(StyleType.labelFormat, t);
			setTypes(EDGE_AND_GRAPH_AND_VERTEX);
		}

		@Override
		public void visit(IGraphElement ge, IStyleVisitor visitor) {
			visitor.labelFormat(getValue(ge));
		}

	}

	public static class LineColor extends StringStyle {
		public LineColor(String colorString) {
			super(StyleType.lineColor, colorString);
			setTypes(EDGE_AND_GRAPH_AND_VERTEX);
		}

		@Override
		public void visit(IGraphElement ge, IStyleVisitor visitor) {
			visitor.lineColor(getValue(ge));
		}

	}

	public static class NodeBrush extends AbstractStyle<ShapeBrush> {
		public NodeBrush(LineType lineType, Double lineWidth, boolean filled, boolean rounded) {
			super(StyleType.shapeBrush, new ShapeBrush(lineType, lineWidth, filled, rounded));
			// does not apply to root graph - this must be checked when rendering
			setTypes(GRAPH_AND_VERTEX);
		}

		@Override
		public void visit(IGraphElement ge, IStyleVisitor visitor) {
			visitor.shapeBrush(getValue(ge));
		}

	}

	public static class Port extends StringStyle {
		public Port(String portName) {
			super(StyleType.port, portName);
		}

		@Override
		public void visit(IGraphElement ge, IStyleVisitor visitor) {
			visitor.port(getValue(ge));
		}

	}

	public static class RankDirectionStyle extends AbstractStyle<RankDirection> {
		public RankDirectionStyle(RankDirection direction) {
			super(StyleType.rankDirection, direction);
			setTypes(GRAPH);
		}

		@Override
		public void visit(IGraphElement ge, IStyleVisitor visitor) {
			visitor.rankDirection(getValue(ge));
		}

	}

	public static class RankSeparation extends DoubleStyle {
		public RankSeparation(Double inches) {
			super(StyleType.rankSeparation, inches);
			setTypes(GRAPH);
		}

		@Override
		public void visit(IGraphElement ge, IStyleVisitor visitor) {
			visitor.rankSeparation(getValue(ge));
		}

	}

	/**
	 * Takes an EL expression. If it evaluates to anything but "FALSE" or "false" - the meaning
	 * is to render.
	 * 
	 */
	public static class Rendered extends BooleanStyle {
		public Rendered(Boolean value) {
			super(StyleType.rendered, value);
			setTypes(TABLE_AND_CELL);
		}

		public Rendered(Function<IGraphElement, Boolean> value) {
			super(StyleType.rendered, value);
			setTypes(TABLE_AND_CELL);
		}

		@Override
		public void visit(IGraphElement ge, IStyleVisitor visitor) {
			visitor.rendered(getValue(ge));
		}

	}

	public static class Routing extends AbstractStyle<EdgeRouting> {
		public Routing(EdgeRouting routing) {
			super(StyleType.edgeRouting, routing);
			setTypes(GRAPH);
		}

		@Override
		public void visit(IGraphElement ge, IStyleVisitor visitor) {
			visitor.routing(getValue(ge));
		}

	}

	public static class RowSpan extends IntegerStyle {
		public RowSpan(Integer value) {
			super(StyleType.rowSpan, value);
			setTypes(CELL);
		}

		@Override
		public void visit(IGraphElement ge, IStyleVisitor visitor) {
			visitor.rowSpan(getValue(ge));
		}

	}

	public static class Shape extends AbstractStyle<NodeShape> {
		public Shape(NodeShape shape) {
			super(StyleType.shape, shape);
			setTypes(VERTEX);
		}

		@Override
		public void visit(IGraphElement ge, IStyleVisitor visitor) {
			visitor.shape(getValue(ge));
		}

	}

	protected abstract static class StringStyle extends AbstractStyle<String> {
		public StringStyle(StyleType style, Function<IGraphElement, String> value) {
			super(style, value);
		}

		public StringStyle(StyleType style, String value) {
			super(style, value);
		}

	}

	/**
	 * Specifies a port where the tail of an edge should connect. This is either a portname, or a
	 * combination of port name and a compass "direction". Everything an edge can connect to has the
	 * compass directions as default ports.
	 * 
	 */
	public static class TailPort extends EdgePortStyle {
		public TailPort(Compass portName) {
			super(StyleType.tailPort, portName);
		}

		public TailPort(String portName) {
			super(StyleType.tailPort, portName);
		}

		public TailPort(String portName, Compass compass) {
			super(StyleType.tailPort, portName, compass);
		}

		@Override
		public void visit(IGraphElement ge, IStyleVisitor visitor) {
			visitor.tailPort(getValue(ge));
		}

	}

	public static class Target extends StringStyle {
		public Target(String target) {
			super(StyleType.target, target);
			setTypes(ANY);
		}

		@Override
		public void visit(IGraphElement ge, IStyleVisitor visitor) {
			visitor.target(getValue(ge));
		}

	}

	public static class Tooltip extends StringStyle {
		public Tooltip(Function<IGraphElement, String> text) {
			super(StyleType.tooltip, text);
			setTypes(NOT_GRAPH);
		}

		@Override
		public void visit(IGraphElement ge, IStyleVisitor visitor) {
			visitor.tooltip(getValue(ge));
		}

	}

	public static class VerticalAlign extends AbstractStyle<VerticalAlignment> {
		public VerticalAlign(VerticalAlignment valign) {
			super(StyleType.valign, valign);
			setTypes(TABLE_AND_CELL);
		}

		@Override
		public void visit(IGraphElement ge, IStyleVisitor visitor) {
			visitor.verticalAlign(getValue(ge));
		}

	}

	public static class Width extends DoubleStyle {
		public Width(Double value) {
			super(StyleType.width, value);
			setTypes(CELL_AND_TABLE_AND_VERTEX);
		}

		@Override
		public void visit(IGraphElement ge, IStyleVisitor visitor) {
			visitor.width(getValue(ge));
		}

	}

	private IFunctionFactory functions;

	@Inject
	public StyleFactory(IFunctionFactory factory) {
		this.functions = factory;
	}

	@Override
	public Align align(Alignment x) {
		return new Align(x);
	}

	@Override
	public ArrowHead arrowHead(Arrow x) {
		return new ArrowHead(x);
	}

	@Override
	public Arrows arrows(Arrow x) {
		return new Arrows(x);
	}

	@Override
	public ArrowScale arrowScale(Double x) {
		return new ArrowScale(x);
	}

	@Override
	public ArrowTail arrowTail(Arrow x) {
		return new ArrowTail(x);
	}

	@Override
	public BackgroundColor backgroundColor(String x) {
		return new BackgroundColor(x);
	}

	@Override
	public BorderWidth borderWidth(int x) {
		return new BorderWidth(x);
	}

	@Override
	public CellBorderWidth cellBorderWidth(int x) {
		return new CellBorderWidth(x);
	}

	@Override
	public CellPadding cellPadding(int x) {
		return new CellPadding(x);
	}

	@Override
	public CellSpacing cellSpacing(int x) {
		return new CellSpacing(x);
	}

	@Override
	public Color color(String x) {
		return new Color(x);
	}

	@Override
	public ColSpan colSpan(int x) {
		return new ColSpan(x);
	}

	@Override
	public IStyle<Boolean> compound(boolean x) {
		return new Compound(x);
	}

	@Override
	public Concentrate concentrate(boolean x) {
		return new Concentrate(x);
	}

	@Override
	public Decorate decorate(boolean x) {
		return new Decorate(x);
	}

	@Override
	public Direction direction(EdgeDirection x) {
		return new Direction(x);
	}

	@Override
	public FillColor fillColor(String x) {
		return new FillColor(x);
	}

	@Override
	public FixedSize fixedSize(boolean x) {
		return new FixedSize(x);
	}

	@Override
	public FontFamily fontFamily(String x) {
		return new FontFamily(x);
	}

	@Override
	public FontSize fontSize(int x) {
		return new FontSize(x);
	}

	@Override
	public HeadPort headPort(Compass x) {
		return new HeadPort(x);
	}

	@Override
	public HeadPort headPort(String x) {
		return new HeadPort(x);
	}

	@Override
	public HeadPort headPort(String x, Compass c) {
		return new HeadPort(x, c);
	}

	@Override
	public Height height(double d) {
		return new Height(d);
	}

	@Override
	public Href href(String x) {
		return new Href(x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.style.IStyleFactory#labelCell(com.google.common.base.Function, com.google.common.base.Function)
	 */
	@Override
	public LabelCell labelCell(Function<IGraphElement, String> styleClass, Function<IGraphElement, String> f) {
		return new LabelCell(styleClass, f);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.style.IStyleFactory#labelCell(java.lang.String, com.google.common.base.Function)
	 */
	@Override
	public LabelCell labelCell(String styleClass, Function<IGraphElement, String> f) {
		return new LabelCell(functions.literalString(styleClass), f);
	}

	@Override
	public LabelCell labelCell(String styleClass, String value) {
		return new LabelCell(functions.literalString(styleClass), functions.literalString(value));
	}

	@Override
	public LabelCell labelCell(String styleClass, String value, Span span) {
		return new LabelCell(functions.literalString(styleClass), functions.literalString(value), span);
	}

	@Override
	public LabelFormat labelFormat(ILabelTemplate x) {
		return new LabelFormat(x);
	}

	@Override
	public LabelRow labelRow(String styleClass, LabelCell... cells) {
		return new LabelRow(styleClass, cells);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.style.IStyleFactory#labelStringTemplate(com.google.common.base.Function)
	 */
	@Override
	public LabelStringTemplate labelStringTemplate(Function<IGraphElement, String> f) {
		return new LabelStringTemplate(f);
	}

	@Override
	public LabelStringTemplate labelStringTemplate(String x) {
		return new LabelStringTemplate(x);
	}

	@Override
	public LabelTable labelTable(String styleClass, LabelRow... rows) {
		return new LabelTable(styleClass, rows);
	}

	@Override
	public EdgeBrush lineBrush(LineType lineType, double lineWidth) {
		return new EdgeBrush(lineType, lineWidth);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.style.IStyleFactory#lineColor(java.lang.String)
	 */
	@Override
	public LineColor lineColor(String x) {
		return new LineColor(x);
	}

	@Override
	public Port port(String x) {
		return new Port(x);
	}

	@Override
	public RankDirectionStyle rankDirectionStyle(RankDirection x) {
		return new RankDirectionStyle(x);
	}

	@Override
	public RankSeparation rankSeparation(double x) {
		return new RankSeparation(x);
	}

	@Override
	public Rendered rendered(boolean x) {
		return new Rendered(x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.style.IStyleFactory#rendered(com.google.common.base.Function)
	 */
	@Override
	public IStyle<Boolean> rendered(Function<IGraphElement, Boolean> f) {
		return new Rendered(f);
	}

	@Override
	public Routing routing(EdgeRouting x) {
		return new Routing(x);
	}

	@Override
	public RowSpan rowSpan(int x) {
		return new RowSpan(x);
	}

	@Override
	public Shape shape(NodeShape x) {
		return new Shape(x);
	}

	@Override
	public NodeBrush shapeBrush(LineType lineType, double lineWidth, boolean filled, boolean rounded) {
		return new NodeBrush(lineType, lineWidth, filled, rounded);
	}

	@Override
	public TailPort tailPort(Compass x) {
		return new TailPort(x);
	}

	@Override
	public TailPort tailPort(String x) {
		return new TailPort(x);
	}

	@Override
	public TailPort tailPort(String x, Compass c) {
		return new TailPort(x, c);
	}

	@Override
	public Target target(String x) {
		return new Target(x);
	}

	@Override
	public Tooltip tooltip(Function<IGraphElement, String> f) {
		return new Tooltip(f);
	}

	@Override
	public Tooltip tooltip(String x) {
		return new Tooltip(functions.literalString(x));
	}

	@Override
	public VerticalAlign verticalAlign(VerticalAlignment x) {
		return new VerticalAlign(x);
	}

	@Override
	public Width width(double x) {
		return new Width(x);
	}

}
