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
package com.puppetlabs.graph.graphviz;

import com.puppetlabs.graph.ICancel;
import com.puppetlabs.graph.IGraph;
import com.puppetlabs.graph.graphcss.GraphCSS;

/**
 * The renderer an IGraphviz should use. Graphviz can use different rendering packages. The default windows installation of
 * Graphviz supports only the gd library. The newer (and superior) "cairo" (the drawing package used by Gimp) is an add on
 * on linux systems. It is unclear if it is possible to get this library also working with Graphviz on windows.
 * To check which renderers are supported run "dot -Tpng:" on a command line for a list of choices.
 * 
 * A Renderer is needed for output formats based on some sort of image (e.g.
 * {@link Graphviz#toJPG(ICancel, GraphvizLayout, IGraph, GraphCSS, GraphCSS...)},
 * and {@link Graphviz#toPNG(ICancel, GraphvizLayout, IGraph, GraphCSS, GraphCSS...)}
 */
public enum GraphvizRenderer {
	/**
	 * Use default renderer bound in the runtime module. Should <b>NOT</b> be used as the default bound
	 * in the runtime module. This renderer should be specified when format does not require a renderer (such as
	 * the xdot format).
	 */
	standard,
	/**
	 * Use :gd renderer
	 */
	gd,
	/**
	 * Use :cairo renderer
	 */
	cairo,

	/**
	 * Use :quartz renderer on OSx
	 */
	quartz, ;
}
