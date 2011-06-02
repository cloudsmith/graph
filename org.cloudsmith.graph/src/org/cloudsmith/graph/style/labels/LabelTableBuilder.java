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
package org.cloudsmith.graph.style.labels;

import org.cloudsmith.graph.IGraphElement;
import org.cloudsmith.graph.graphcss.IFunctionFactory;
import org.cloudsmith.graph.style.IStyleFactory;

import com.google.common.base.Function;

/**
 * A helper class that makes it easier/more readable to build a label table.
 * Simply implement the {@link #build()} method as described in this method's documentation.
 * This class also implements {@link IFunctionFactory} to make it convenient to use
 * functions as values in the calls to {@link #row(Function, LabelCell...)}, {@link #cell(Function, Function)},
 * (and related methods).
 * 
 */
public abstract class LabelTableBuilder implements IFunctionFactory {

	private IStyleFactory styleFactory;

	private IFunctionFactory functions;

	public LabelTableBuilder(IStyleFactory styleFactory, IFunctionFactory functions) {
		this.styleFactory = styleFactory;
		this.functions = functions;
	}

	/**
	 * <p>
	 * This method should perform something like:
	 * </p>
	 * <code>
	 * return table(
	 *     "MyTableClass", //
	 *     row("MyRowClass", cell("LabelCell", "theLabel"), cell("ValueCell", "theValue")),
	 *     row("MyRowClass", cell("LabelCell", "theLabel"), cell("ValueCell", "theValue")));
	 * </code>
	 * 
	 * @return
	 */
	public abstract LabelTable build();

	final protected LabelCell cell(Function<IGraphElement, String> styleClass, Function<IGraphElement, String> f) {
		return styleFactory.labelCell(styleClass, f);
	}

	final protected LabelCell cell(String styleClass, Function<IGraphElement, String> f) {
		return styleFactory.labelCell(styleClass, f);
	}

	final protected LabelCell cell(String styleClass, String s) {
		return styleFactory.labelCell(styleClass, s);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.graphcss.IFunctionFactory#emptyLabel()
	 */
	@Override
	public Function<IGraphElement, Boolean> emptyLabel() {
		return functions.emptyLabel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.graphcss.IFunctionFactory#emptyLabelData(java.lang.Object)
	 */
	@Override
	public Function<IGraphElement, Boolean> emptyLabelData(Object key) {
		return functions.emptyLabelData(key);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.graphcss.IFunctionFactory#label()
	 */
	@Override
	public Function<IGraphElement, String> label() {
		return functions.label();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.graphcss.IFunctionFactory#labelData(java.lang.Object)
	 */
	@Override
	public Function<IGraphElement, String> labelData(Object key) {
		return functions.labelData(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.graphcss.IFunctionFactory#literalString(java.lang.String)
	 */
	@Override
	public Function<IGraphElement, String> literalString(String s) {
		return functions.literalString(s);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.graphcss.IFunctionFactory#notEmptyLabel()
	 */
	@Override
	public Function<IGraphElement, Boolean> notEmptyLabel() {
		return functions.notEmptyLabel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.graphcss.IFunctionFactory#notEmptyLabelData(java.lang.Object)
	 */
	@Override
	public Function<IGraphElement, Boolean> notEmptyLabelData(Object key) {
		return functions.notEmptyLabelData(key);
	}

	final protected LabelRow row(Function<IGraphElement, String> styleClass, LabelCell... cells) {
		return new LabelRow(styleClass, cells);
	}

	final protected LabelRow row(String styleClass, LabelCell... cells) {
		return new LabelRow(styleClass, cells);
	}

	final protected LabelTable table(Function<IGraphElement, String> styleClass, LabelRow... rows) {
		return new LabelTable(styleClass, rows);
	}

	final protected LabelTable table(String styleClass, LabelRow... rows) {
		return new LabelTable(styleClass, rows);
	}

}
