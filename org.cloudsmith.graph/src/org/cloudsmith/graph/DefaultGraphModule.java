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
package org.cloudsmith.graph;

import org.cloudsmith.graph.graphcss.FunctionFactory;
import org.cloudsmith.graph.graphcss.IFunctionFactory;
import org.cloudsmith.graph.graphviz.DefaultGraphvizConfig;
import org.cloudsmith.graph.graphviz.Graphviz;
import org.cloudsmith.graph.graphviz.IGraphviz;
import org.cloudsmith.graph.graphviz.IGraphvizConfig;
import org.cloudsmith.graph.style.IStyleFactory;
import org.cloudsmith.graph.style.StyleFactory;
import org.cloudsmith.graph.style.themes.DefaultStyleTheme;
import org.cloudsmith.graph.style.themes.IStyleTheme;

import com.google.inject.AbstractModule;

/**
 * A default graph runtime module.
 * 
 */
public class DefaultGraphModule extends AbstractModule {

	/**
	 * Binds the main Dot Renderer, and the utility DotLabelRenderer and a DotGraphElementRenderer.
	 * Other renderers may require a different configuration.
	 */
	protected void bindDotRenderer() {
		// bind(DotRenderer.class).to(DotRenderer.class); // default, but make it explicit
		// bind(DotLabelRenderer.class).to(DotLabelRenderer.class); // default made explicit
		// bind(DotGraphElementRenderer.class).to(DotGraphElementRenderer.class); // default made explicit.
	}

	/**
	 * Binds the standard style value function factory.
	 */
	protected void bindIFunctionFactory() {
		bind(IFunctionFactory.class).to(FunctionFactory.class);
	}

	/**
	 * Binds implementation of graphviz runner.
	 */
	protected void bindIGraphviz() {
		bind(IGraphviz.class).to(Graphviz.class);
	}

	/**
	 * Binds the standard graphviz configuration to use. This binding is environment dependent.
	 */
	protected void bindIGraphvizConfig() {
		bind(IGraphvizConfig.class).to(DefaultGraphvizConfig.class);
	}

	/**
	 * Binds the standard style (data) factory.
	 */
	protected void bindIStyleFactory() {
		bind(IStyleFactory.class).to(StyleFactory.class);
	}

	protected void bindIStyleTheme() {
		bind(IStyleTheme.class).to(DefaultStyleTheme.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.inject.AbstractModule#configure()
	 */
	@Override
	protected void configure() {
		bindIGraphviz();
		bindIGraphvizConfig();
		bindIFunctionFactory();
		bindIStyleFactory();
		bindDotRenderer();
		bindIStyleTheme();
	}

}
