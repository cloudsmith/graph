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
package org.cloudsmith.graph;

import java.util.Map;

/**
 * A graph element that supports a label.
 * A label is either a simple string label, or is built from keyed label parts in a data map.
 * A label may also through the use of EL and reflection get implementation specific properties.
 * 
 */
public interface ILabeledGraphElement extends IGraphElement {
	/**
	 * Labeled elements can have a map of data. The data elements can be interpolated using EL statements.
	 * 
	 * @return
	 */
	public Map<String, String> getData();

	/**
	 * Return a simple string label. The label may contain new lines on the form '\' 'n'.
	 * 
	 * @return
	 */
	public String getLabel();
}
