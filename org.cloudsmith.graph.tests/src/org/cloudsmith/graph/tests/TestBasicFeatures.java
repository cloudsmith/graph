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
import org.cloudsmith.graph.elements.Edge;
import org.cloudsmith.graph.elements.Graph;
import org.cloudsmith.graph.elements.Vertex;
import org.cloudsmith.graph.graphcss.GraphCSS;
import org.cloudsmith.graph.graphcss.IFunctionFactory;
import org.cloudsmith.graph.graphviz.GraphvizFormat;
import org.cloudsmith.graph.graphviz.GraphvizLayout;
import org.cloudsmith.graph.graphviz.GraphvizRenderer;
import org.cloudsmith.graph.graphviz.IGraphviz;
import org.cloudsmith.graph.style.IStyleFactory;
import org.cloudsmith.graph.testgraphs.AbstractTestGraph;

import com.google.inject.Inject;

/**
 * Tests basic production of graphviz output:
 * - that the DotRenderer is producing expected dot output
 * - that the graphviz renderer can be called, and that it produces different types of output
 */
public class TestBasicFeatures extends AbstractGraphTests {

	/**
	 * Tests that vertexes with explicit ids keep these ids in dot and that edges
	 * use the user specified ids.
	 * 
	 */
	public static class IdentityTestGraph extends AbstractTestGraph {
		@Inject
		public IdentityTestGraph(IStyleFactory styleFactory, IFunctionFactory functions) {
			super(styleFactory, functions);
		}

		/**
		 * @modelObj - ignored, returns same graph at all times.
		 */
		@Override
		public IGraph computeGraph(Object modelObj, String title, String id) {
			Graph g = new Graph(title, "RootGraph", id);
			Vertex a = new Vertex("a", "v", "a");
			Vertex b = new Vertex("b", "v", "b");
			Vertex c = new Vertex("c", "v", "c");
			g.addVertex(a, b, c);

			Edge abEdge = new Edge(a, b);
			Edge bcEdge = new Edge(b, c);
			g.addEdge(abEdge, bcEdge);

			return g;
		}
	}

	/**
	 * Tests that vertexes that do not have explicit ids gets generated ids and that edges between
	 * these vertexes work.
	 */
	public static class NoIdentityTestGraph extends AbstractTestGraph {
		@Inject
		public NoIdentityTestGraph(IStyleFactory styleFactory, IFunctionFactory functions) {
			super(styleFactory, functions);
		}

		/**
		 * @modelObj - ignored, returns same graph at all times.
		 */
		@Override
		public IGraph computeGraph(Object modelObj, String title, String id) {
			Graph g = new Graph(title, "RootGraph", id);
			Vertex a = new Vertex("a", "v");
			Vertex b = new Vertex("b", "v");
			Vertex c = new Vertex("c", "v");
			g.addVertex(a, b, c);

			Edge abEdge = new Edge(a, b);
			Edge bcEdge = new Edge(b, c);
			g.addEdge(abEdge, bcEdge);

			return g;
		}
	}

	/**
	 * Output produced by a very simple graph with no identities.
	 */
	public static final String testGraph_noIdentities_expected = "digraph root {\n" + //
			"subgraph \"-root\" {\n" + //
			"label=\"a test graph\";\n" + //
			"\"root-v1\" [\n" + "label=\"a\"];\n" + //
			"\"root-v2\" [\n" + "label=\"b\"];\n" + //
			"\"root-v3\" [\n" + "label=\"c\"];\n" + //
			"\"root-v1\" -> \"root-v2\";\n" + //
			"\"root-v2\" -> \"root-v3\";\n" + //
			"}\n" + //
			"}\n";

	/**
	 * Output produced by a very simple graph with no identities.
	 */
	public static final String testGraph_Identities_expected = "digraph root {\n" + //
			"subgraph \"-root\" {\n" + //
			"label=\"a test graph\";\n" + //
			"\"root-a\" [\n" + "label=\"a\"];\n" + //
			"\"root-b\" [\n" + "label=\"b\"];\n" + //
			"\"root-c\" [\n" + "label=\"c\"];\n" + //
			"\"root-a\" -> \"root-b\";\n" + //
			"\"root-b\" -> \"root-c\";\n" + //
			"}\n" + //
			"}\n";

	/**
	 * Test that output is correct when there are no styles and identities have not
	 * been set.
	 */
	public void testGraph_identities() {
		GraphCSS themeSheet = get(GraphCSS.class);

		IGraphProvider graphProvider = get(IdentityTestGraph.class);
		IGraph testGraph = graphProvider.computeGraph();
		themeSheet.addAll(graphProvider.getRules());

		DotRenderer dotRenderer = get(DotRenderer.class);
		ByteArrayOutputStream tmp = new ByteArrayOutputStream();

		// Render without the default styles. Use styles from SimpleGraph1
		dotRenderer.write(tmp, testGraph, get(GraphCSS.class), themeSheet);
		assertEquals("Expected result differs", testGraph_Identities_expected, tmp.toString());
	}

	/**
	 * Test that output is correct when there are no styles and identities have not
	 * been set.
	 */
	public void testGraph_noIdentities() {
		GraphCSS themeSheet = get(GraphCSS.class);

		IGraphProvider graphProvider = get(NoIdentityTestGraph.class);
		IGraph testGraph = graphProvider.computeGraph();
		themeSheet.addAll(graphProvider.getRules());

		DotRenderer dotRenderer = get(DotRenderer.class);
		ByteArrayOutputStream tmp = new ByteArrayOutputStream();

		// Render without the default styles. Use styles from SimpleGraph1
		dotRenderer.write(tmp, testGraph, get(GraphCSS.class), themeSheet);
		assertEquals("Expected result differs", testGraph_noIdentities_expected, tmp.toString());
	}

	/**
	 * Tests running graphviz to produce output. The test is performed by producing output in svg format
	 * (this format is textual) and checks that text is in svg tags, that there are calls to path and polygon
	 * (to ensure there is some drawing information, and not just empty svg), and that the end tag is present
	 * (i.e. that output is not truncated).
	 */
	public void testGraph_runGraphviz_svg() {
		IGraphviz graphviz = get(IGraphviz.class);
		GraphCSS themeSheet = get(GraphCSS.class);

		IGraphProvider graphProvider = get(IdentityTestGraph.class);
		IGraph testGraph = graphProvider.computeGraph();
		themeSheet.addAll(graphProvider.getRules());

		ByteArrayOutputStream tmp = new ByteArrayOutputStream();
		graphviz.writeGraphvizOutput(
			tmp, GraphvizFormat.svg, GraphvizRenderer.standard, GraphvizLayout.dot, testGraph, get(GraphCSS.class),
			themeSheet);
		String output = tmp.toString();
		assertTrue("Should contain <svg", output.contains("<svg"));
		assertTrue("Should contain <polygon", output.contains("<polygon"));
		assertTrue("Should contain <path", output.contains("<path"));
		assertTrue("Should end with </svg>", output.endsWith("</svg>\n"));
	}

	/**
	 * Tests running graphviz to produce output. The test is performed by producing output in xdot format
	 * (as this format is textual) and checks that text contains calls to "_draw_".
	 */
	public void testGraph_runGraphviz_xdot() {
		IGraphviz graphviz = get(IGraphviz.class);
		GraphCSS themeSheet = get(GraphCSS.class);

		IGraphProvider graphProvider = get(IdentityTestGraph.class);
		IGraph testGraph = graphProvider.computeGraph();
		themeSheet.addAll(graphProvider.getRules());

		ByteArrayOutputStream tmp = new ByteArrayOutputStream();
		graphviz.writeGraphvizOutput(
			tmp, GraphvizFormat.xdot, GraphvizRenderer.standard, GraphvizLayout.dot, testGraph, get(GraphCSS.class),
			themeSheet);
		String output = tmp.toString();
		assertTrue("Should contain _draw_ calls", output.contains("_draw_"));
	}
}
