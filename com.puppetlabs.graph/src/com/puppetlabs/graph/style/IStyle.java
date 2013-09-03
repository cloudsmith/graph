/**
 * Copyright (c) 2013 Puppet Labs, Inc. and other contributors, as listed below.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Puppet Labs
 */
package com.puppetlabs.graph.style;

import java.util.Set;

import com.puppetlabs.graph.ElementType;
import com.puppetlabs.graph.IGraphElement;

/**
 * Interface for a style.
 * A style has a {@link StyleType} and a value of type <T>.
 * 
 * @param <T>
 */
public interface IStyle<T> {

	public StyleType getStyleType();

	/**
	 * Gets the value of the style. A graph element must be provided as the style may be dynamic
	 * and produce its value as a function of the given graph element. If a style returns false
	 * from {@link #isFunction()}, the parameter ge may be null. At all other times must ge be a valid
	 * graph element.
	 * 
	 * @param ge
	 * @return
	 */
	public T getValue(IGraphElement ge);

	/**
	 * A style with a value determined as a function of a graphic element returns true. A style that
	 * returns false allows null being passed as the graph element in a call to {@link #getValue(IGraphElement)}.
	 * 
	 * @return
	 */
	public boolean isFunction();

	/**
	 * Returns true if the style is supported for a particular type of element.
	 * 
	 * @param type
	 * @return
	 */
	public boolean supports(ElementType type);

	/**
	 * Returns true if the style supports all elements in the given set.
	 * 
	 * @param types
	 * @return
	 */
	public boolean supports(Set<ElementType> types);

	/**
	 * Visiting this IStyle means it will call back to the given visitor method named after the style type.
	 * 
	 * @param ge
	 *            The visited graph element
	 * @param visitor
	 *            The visitor that will be called
	 */
	public void visit(IGraphElement ge, IStyleVisitor visitor);

}
