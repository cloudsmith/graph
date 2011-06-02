/**
 * Copyright (c) 2011 Cloudsmith Inc. and other contributors, as listed below.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Cloudsmith
 * 
 */
package org.cloudsmith.graph.graphcss;

import org.cloudsmith.graph.IGraphElement;

import com.google.common.base.Function;

/**
 * @author henrik
 *
 */
public interface IFunctionFactory {

	/**
	 * Returns a function returning true if the graph element has an empty label.
	 * 
	 * @return
	 */
	public Function<IGraphElement, Boolean> emptyLabel();

	/**
	 * Returns a function returning true if the graph element has an empty label data for the given key.
	 * 
	 * @return
	 */
	public Function<IGraphElement, Boolean> emptyLabelData(Object key);

	/**
	 * Returns the label string of the graph element.
	 * 
	 * @return
	 */
	public Function<IGraphElement, String> label();

	/**
	 * Returns the label data of the given key from the graph element.
	 * 
	 * @param key
	 * @return
	 */
	public Function<IGraphElement, String> labelData(Object key);

	public Function<IGraphElement, String> literalString(String s);

	/**
	 * Returns true if the graph element's label is not empty.
	 * 
	 * @return
	 */
	public Function<IGraphElement, Boolean> notEmptyLabel();

	/**
	 * Returns true if the graph element's label data with the given key is not empty.
	 * 
	 * @param key
	 * @return
	 */
	public Function<IGraphElement, Boolean> notEmptyLabelData(Object key);

}
