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

import java.util.Collection;
import java.util.Set;

import org.cloudsmith.graph.IGraphElement;
import org.cloudsmith.graph.style.labels.ILabelTemplate;
import org.cloudsmith.graph.style.labels.LabelTable;

import com.google.common.base.Function;

/**
 * @author henrik
 * 
 */
public interface IFunctionFactory {

	public final static String ID_KEY = IFunctionFactory.class.getName() + "_Id";

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
	 * Returns a string with id="ID" class="CLASSES" based on an optional user data key (or the
	 * fully qualified id if missing), and the combination of graph element type and graph element style class.
	 * 
	 * @return
	 */
	Function<IGraphElement, String> idClassReplacer();

	/**
	 * Returns the label string of the graph element.
	 * 
	 * @return
	 */
	public Function<IGraphElement, ILabelTemplate> label();

	/**
	 * Returns the label data of the given key from the graph element.
	 * 
	 * @param key
	 * @return
	 */
	public Function<IGraphElement, String> labelData(Object key);

	public Function<IGraphElement, ILabelTemplate> labelTemplate(Function<IGraphElement, String> stringfunc);

	public Function<IGraphElement, ILabelTemplate> literalLabelTemplate(LabelTable t);

	public Function<IGraphElement, ILabelTemplate> literalLabelTemplate(String s);

	public Function<IGraphElement, String> literalString(String s);

	public Function<IGraphElement, Set<String>> literalStringSet(Collection<String> s);

	public Function<IGraphElement, Set<String>> literalStringSet(String s);

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
