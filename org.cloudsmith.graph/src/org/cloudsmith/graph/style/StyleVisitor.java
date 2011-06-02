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

/**
 * A default implementation of IStyleVisitor.
 * All methods do nothing, and does not have to be called.
 * 
 */
public class StyleVisitor implements IStyleVisitor {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.style.IStyleVisitor#align(org.cloudsmith.graph.style.Alignment)
	 */
	@Override
	public void align(Alignment x) {
		unsupported(StyleType.align);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.style.IStyleVisitor#arrowHead(org.cloudsmith.graph.style.Arrow)
	 */
	@Override
	public void arrowHead(Arrow x) {
		unsupported(StyleType.arrowHead);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.style.IStyleVisitor#arrows(org.cloudsmith.graph.style.Arrow)
	 */
	@Override
	public void arrows(Arrow x) {
		unsupported(StyleType.arrows);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.style.IStyleVisitor#arrowScale(java.lang.Double)
	 */
	@Override
	public void arrowScale(double x) {
		unsupported(StyleType.arrowScale);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.style.IStyleVisitor#arrowTail(org.cloudsmith.graph.style.Arrow)
	 */
	@Override
	public void arrowTail(Arrow x) {
		unsupported(StyleType.arrowTail);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.style.IStyleVisitor#backgroundColor(java.lang.String)
	 */
	@Override
	public void backgroundColor(String x) {
		unsupported(StyleType.backgroundColor);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.style.IStyleVisitor#borderWidth(int)
	 */
	@Override
	public void borderWidth(int x) {
		unsupported(StyleType.borderWidth);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.style.IStyleVisitor#cellBorderWidth(int)
	 */
	@Override
	public void cellBorderWidth(int x) {
		unsupported(StyleType.cellBorderWidth);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.style.IStyleVisitor#cellPadding(int)
	 */
	@Override
	public void cellPadding(int x) {
		unsupported(StyleType.cellPadding);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.style.IStyleVisitor#cellSpacing(int)
	 */
	@Override
	public void cellSpacing(int x) {
		unsupported(StyleType.cellSpacing);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.style.IStyleVisitor#color(java.lang.String)
	 */
	@Override
	public void color(String x) {
		unsupported(StyleType.color);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.style.IStyleVisitor#colSpan(int)
	 */
	@Override
	public void colSpan(int x) {
		unsupported(StyleType.colSpan);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.style.IStyleVisitor#concentrate(boolean)
	 */
	@Override
	public void concentrate(boolean x) {
		unsupported(StyleType.concentrate);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.style.IStyleVisitor#decorate(boolean)
	 */
	@Override
	public void decorate(boolean x) {
		unsupported(StyleType.decorate);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.style.IStyleVisitor#direction(org.cloudsmith.graph.style.EdgeDirection)
	 */
	@Override
	public void direction(EdgeDirection x) {
		unsupported(StyleType.direction);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.style.IStyleVisitor#backgroundColor(java.lang.String)
	 */
	@Override
	public void fillColor(String x) {
		unsupported(StyleType.fillColor);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.style.IStyleVisitor#fixedSize(boolean)
	 */
	@Override
	public void fixedSize(boolean x) {
		unsupported(StyleType.fixedSize);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.style.IStyleVisitor#fontFamily(java.lang.String)
	 */
	@Override
	public void fontFamily(String x) {
		unsupported(StyleType.fontFamily);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.style.IStyleVisitor#fontSize(int)
	 */
	@Override
	public void fontSize(int x) {
		unsupported(StyleType.fontSize);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.style.IStyleVisitor#headPort(java.lang.String)
	 */
	@Override
	public void headPort(String x) {
		unsupported(StyleType.headPort);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.style.IStyleVisitor#height(double)
	 */
	@Override
	public void height(double d) {
		unsupported(StyleType.height);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.style.IStyleVisitor#href(java.lang.String)
	 */
	@Override
	public void href(String x) {
		unsupported(StyleType.href);

	}

	// /*
	// * (non-Javadoc)
	// *
	// * @see org.cloudsmith.graph.style.IStyleVisitor#labelCell(org.cloudsmith.graph.style.labels.LabelCell)
	// */
	// @Override
	// public void labelCell(LabelCell cell) {
	// unsupported(StyleType.labelFormatCell);
	//
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.style.IStyleVisitor#labelFormat(org.cloudsmith.graph.style.labels.ILabelTemplate)
	 */
	@Override
	public void labelFormat(ILabelTemplate x) {
		unsupported(StyleType.labelFormat);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.style.IStyleVisitor#lineBrush(org.cloudsmith.graph.style.LineType, double)
	 */
	@Override
	public void lineBrush(LineBrush brush) {
		unsupported(StyleType.lineBrush);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.style.IStyleVisitor#lineColor(java.lang.String)
	 */
	@Override
	public void lineColor(String x) {
		unsupported(StyleType.lineColor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.style.IStyleVisitor#port(java.lang.String)
	 */
	@Override
	public void port(String x) {
		unsupported(StyleType.port);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.style.IStyleVisitor#rankDirectionStyle(org.cloudsmith.graph.style.RankDirection)
	 */
	@Override
	public void rankDirection(RankDirection x) {
		unsupported(StyleType.rankDirection);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.style.IStyleVisitor#rankSeparation(double)
	 */
	@Override
	public void rankSeparation(double x) {
		unsupported(StyleType.rankSeparation);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.style.IStyleVisitor#rendered(java.lang.String)
	 */
	@Override
	public void rendered(boolean x) {
		unsupported(StyleType.rendered);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.style.IStyleVisitor#routing(org.cloudsmith.graph.style.EdgeRouting)
	 */
	@Override
	public void routing(EdgeRouting x) {
		unsupported(StyleType.edgeRouting);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.style.IStyleVisitor#rowSpan(int)
	 */
	@Override
	public void rowSpan(int x) {
		unsupported(StyleType.rowSpan);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.style.IStyleVisitor#shape(org.cloudsmith.graph.style.NodeShape)
	 */
	@Override
	public void shape(NodeShape x) {
		unsupported(StyleType.shape);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.style.IStyleVisitor#shapeBrush(org.cloudsmith.graph.style.ShapeBrush)
	 */
	@Override
	public void shapeBrush(ShapeBrush brush) {
		unsupported(StyleType.shapeBrush);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.style.IStyleVisitor#tailPort(java.lang.String)
	 */
	@Override
	public void tailPort(String x) {
		unsupported(StyleType.tailPort);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.style.IStyleVisitor#target(java.lang.String)
	 */
	@Override
	public void target(String x) {
		unsupported(StyleType.target);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.style.IStyleVisitor#tooltip(java.lang.String)
	 */
	@Override
	public void tooltip(String x) {
		unsupported(StyleType.tooltip);

	}

	/**
	 * This implementation does nothing. A derived class can implement this method as a "catch all" for
	 * unimplemented methods.
	 * 
	 * @see org.cloudsmith.graph.style.IStyleVisitor#unsupported(org.cloudsmith.graph.IStyle)
	 */
	protected void unsupported(StyleType style) {
		// does nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.style.IStyleVisitor#verticalAlign(org.cloudsmith.graph.style.VerticalAlignment)
	 */
	@Override
	public void verticalAlign(VerticalAlignment x) {
		// do nothing

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.style.IStyleVisitor#width(double)
	 */
	@Override
	public void width(double x) {
		// do nothing

	}

}
