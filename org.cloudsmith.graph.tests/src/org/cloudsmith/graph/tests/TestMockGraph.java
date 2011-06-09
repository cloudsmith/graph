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
package org.cloudsmith.graph.tests;

import java.io.ByteArrayOutputStream;

import org.cloudsmith.graph.IGraph;
import org.cloudsmith.graph.IGraphProvider;
import org.cloudsmith.graph.dot.DotRenderer;
import org.cloudsmith.graph.graphcss.GraphCSS;
import org.cloudsmith.graph.graphviz.IGraphviz;
import org.cloudsmith.graph.style.themes.IStyleTheme;
import org.cloudsmith.graph.testgraphs.SimpleGraph1;
import org.cloudsmith.graph.testgraphs.TestGraph;

/**
 * 
 *
 */
public class TestMockGraph extends AbstractGraphTests {

	/**
	 * Output produced by a very simple graph with no styling.
	 */
	public static final String testSimpleGraph1_dotOutput_text = "digraph root {\n" + //
			"\"root-v1\" [\n" + //
			"label=\"a\"];\n" + //
			"\"root-v2\" [\n" + //
			"label=\"b\"];\n" + //
			"\"root-v3\" [\n" + //
			"label=\"c\"];\n" + //
			"\"root-v1\" -> \"root-v2\";\n" + //
			"\"root-v2\" -> \"root-v3\";\n" + //
			"label=\"a graph\";\n" + //
			"}\n";

	public void testSimpleGraph1_dotOutput() {
		GraphCSS themeSheet = get(GraphCSS.class);

		IGraphProvider graphProvider = get(SimpleGraph1.class);
		IGraph testGraph = graphProvider.computeGraph();
		themeSheet.addAll(graphProvider.getRules());

		DotRenderer dotRenderer = get(DotRenderer.class);
		ByteArrayOutputStream tmp = new ByteArrayOutputStream();

		// Render without the default styles. Use styles from SimpleGraph1
		dotRenderer.write(tmp, testGraph, get(GraphCSS.class), themeSheet);
		assertEquals("Expected result differs", testSimpleGraph1_dotOutput_text, tmp.toString());
	}

	public void testSmokeTest() {
		IGraphviz graphviz = get(IGraphviz.class);

		IStyleTheme theme = get(IStyleTheme.class);
		GraphCSS themeSheet = get(GraphCSS.class);
		themeSheet.addAll(theme.getInstanceRules());

		IGraphProvider graphProvider = get(TestGraph.class);
		IGraph testGraph = graphProvider.computeGraph();
		themeSheet.addAll(graphProvider.getRules());

		DotRenderer dotRenderer = get(DotRenderer.class);
		dotRenderer.write(System.err, testGraph, theme.getDefaultRules(), themeSheet);
		// String dotText = graphviz.getDotText(testGraph, theme.getDefaultRules(), themeSheet);
		// System.err.println(dotText);
	}
}
