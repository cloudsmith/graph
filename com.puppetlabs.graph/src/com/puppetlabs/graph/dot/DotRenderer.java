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
package com.puppetlabs.graph.dot;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.google.common.collect.Iterators;
import com.google.inject.BindingAnnotation;
import com.google.inject.Inject;
import com.puppetlabs.graph.ICancel;
import com.puppetlabs.graph.IClusterGraph;
import com.puppetlabs.graph.IEdge;
import com.puppetlabs.graph.IGraph;
import com.puppetlabs.graph.IGraphElement;
import com.puppetlabs.graph.IRootGraph;
import com.puppetlabs.graph.IVertex;
import com.puppetlabs.graph.elements.Edge;
import com.puppetlabs.graph.elements.RootGraph;
import com.puppetlabs.graph.elements.Vertex;
import com.puppetlabs.graph.graphcss.GraphCSS;
import com.puppetlabs.graph.graphcss.StyleSet;
import com.puppetlabs.graph.style.StyleFactory;
import com.puppetlabs.graph.style.StyleType;

/**
 * Produces dot output from a Graph instance.
 */
public class DotRenderer {

	/**
	 * Annotation to use for the Dot Output Empty string.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.FIELD, ElementType.PARAMETER })
	@BindingAnnotation
	public @interface EmptyString {
	}

	private PrintStream out;

	private GraphCSS theGCSS;

	StyleFactory.LabelFormat defaultNodeLabelFormat;

	StyleFactory.LabelFormat defaultEdgeLabelFormat;

	StyleFactory.LabelFormat defaultGraphLabelFormat;

	private StyleSet defaultGraphStyles;

	private StyleSet defaultNodeStyles;

	private StyleSet defaultEdgeStyles;

	private IRootGraph graphPrototype;

	private Vertex vertexPrototype;

	private Edge edgePrototype;

	private DotGraphElementRenderer elementRenderer;

	private GraphCSS defaultGCSS;

	@Inject
	public DotRenderer(DotGraphElementRenderer elementRenderer) {
		this.elementRenderer = elementRenderer;
	}

	private String formatReference(IGraphElement element) {
		IGraphElement[] parents = Iterators.toArray(element.getContext(), IGraphElement.class);
		int plength = (parents == null)
				? 0
				: parents.length;
		StringBuffer buf = new StringBuffer(10 + 5 * plength);

		// the vertex can be an instance of IGraph in which case it should be handled as
		// a subgraph, and the name is different if the subgraph is a cluster
		if(element instanceof IGraph) {
			// all references to subgraphs must start with the keyword subgraph
			buf.append("subgraph ");
		}
		// enclose the constructed names in quotes
		buf.append("\"");

		if(element instanceof IClusterGraph) {
			// if the graph is a cluster it's name must be prefixed with 'cluster'
			// ad a '_' to make it easier to read.
			buf.append("cluster_");
		}
		// add each parents id separated by - start with root (last in array)
		if(parents != null)
			for(int i = parents.length - 1; i >= 0; i--)
				buf.append((i == (plength - 1)
						? ""
						: "-") + parents[i].getId());
		buf.append("-");
		buf.append(element.getId());
		// close the "
		buf.append("\"");
		return buf.toString();
	}

	private void printDefaultEdgeStyling(ICancel cancel) {
		ByteArrayOutputStream tmp = new ByteArrayOutputStream();
		PrintStream tmpOut = new PrintStream(tmp);

		tmpOut.print("edge ");
		int numStyles = elementRenderer.printStyles(cancel, tmpOut, edgePrototype, defaultEdgeStyles, defaultGCSS);
		tmpOut.print(";\n");
		if(numStyles > 0)
			out.print(tmp.toString());
	}

	private void printDefaultGraphStyling(ICancel cancel) {
		ByteArrayOutputStream tmp = new ByteArrayOutputStream();
		PrintStream tmpOut = new PrintStream(tmp);

		tmpOut.print("graph ");
		int numStyles = elementRenderer.printStyles(cancel, tmpOut, graphPrototype, defaultGraphStyles, defaultGCSS);
		tmpOut.print(";\n");

		if(numStyles > 0)
			out.print(tmp.toString());
	}

	private void printDefaultNodeStyling(ICancel cancel) {
		ByteArrayOutputStream tmp = new ByteArrayOutputStream();
		PrintStream tmpOut = new PrintStream(tmp);

		tmpOut.print("node ");
		int numStyles = elementRenderer.printStyles(cancel, tmpOut, vertexPrototype, defaultNodeStyles, defaultGCSS);
		tmpOut.print(";\n");

		if(numStyles > 0)
			out.print(tmp.toString());
	}

	/**
	 * If a label is set, the edge will use it, otherwise edges are without labels.
	 * 
	 * @param edge
	 */
	private void printEdge(IEdge edge, ICancel cancel) {
		// produce edges that link to the "north" port, but use default (from center) linking
		// from the source node (this looks best).
		//
		out.printf("%s -> %s", formatReference(edge.getFrom()), formatReference(edge.getTo()));

		ByteArrayOutputStream tmp = new ByteArrayOutputStream();
		PrintStream tmpOut = new PrintStream(tmp);

		int numStyles = elementRenderer.printStyles(cancel, tmpOut, edge, theGCSS);
		if(numStyles > 0)
			out.print(tmp.toString());
		out.print(";\n");
	}

	/**
	 * Prints a subgraph on the form:
	 * subgraph "reference" { graph body }
	 * Where reference is the full "[cluster_]id-id...-id"
	 * 
	 * @param graph
	 */
	private void printGraph(IGraph graph, ICancel cancel) {
		out.printf("%s {\n", formatReference(graph));
		printGraphBody(graph, cancel);
		out.print("}\n");
	}

	private void printGraphBody(IGraph graph, ICancel cancel) {

		// print the root graph's attributes

		// graphs have their styles set as statements instead of as a list after the body.
		// i.e.
		// graph { a; b; a->b; color="blue"; }
		// and not
		// graph { a; b; a->b; }[color="blue"];
		//

		// Print all the vertices
		for(IVertex v : graph.getVertices()) {
			cancel.assertContinue();
			printVertex(v, cancel);
		}
		// and all the edges
		for(IEdge e : graph.getEdges()) {
			cancel.assertContinue();
			printEdge(e, cancel);
		}

		// Print all the subgraphs first so they do not inherit settings intended for the root
		// graph. All inherited styles should have been set as defaults per element type.
		for(IGraph g : graph.getSubgraphs()) {
			cancel.assertContinue();
			printGraph(g, cancel);
		}

		ByteArrayOutputStream tmp = new ByteArrayOutputStream();
		PrintStream tmpOut = new PrintStream(tmp);

		int numStyles = elementRenderer.printStyleStatements(cancel, tmpOut, graph, theGCSS);
		if(numStyles > 0) {
			out.print(tmp.toString());
			out.print("\n");
		}

	}

	private void printVertex(IVertex vertex, ICancel cancel) {
		// get the full name as it is used in references
		String reference = formatReference(vertex);
		if(reference == null || reference.length() == 0)
			throw new IllegalStateException("A vertext produced empty identity");
		out.printf("%s ", formatReference(vertex));

		ByteArrayOutputStream tmp = new ByteArrayOutputStream();
		PrintStream tmpOut = new PrintStream(tmp);

		int numStyles = elementRenderer.printStyles(cancel, tmpOut, vertex, theGCSS);
		if(numStyles > 0) {
			tmpOut.flush();
			out.print(tmp.toString());
		}
		out.print(";\n");
	}

	/**
	 * The defaultRules are the rules that are used to set dot (static) defaults per ElementType.
	 * These rules can not contain EL statements, nor any styles that needs to be set per instance.
	 * 
	 * The styleRules are the rules that are added to an instance rule set. Lowest priority first.
	 * 
	 * @throws IllegalArgumentException
	 *             for invalid input
	 */
	private void processGCSS(ICancel cancel, GraphCSS defaultGCSS, GraphCSS... styleRules) {
		if(defaultGCSS == null)
			throw new IllegalArgumentException("default style rules is null");

		theGCSS = new GraphCSS();
		for(GraphCSS gcss : styleRules)
			theGCSS.addAll(gcss);

		this.defaultGCSS = defaultGCSS;
		graphPrototype = new RootGraph("", "", "prototype");
		vertexPrototype = new Vertex("", "", "prototype");
		edgePrototype = new Edge(vertexPrototype, vertexPrototype, "prototype");

		defaultGraphStyles = defaultGCSS.collectStyles(graphPrototype, cancel);
		defaultNodeStyles = defaultGCSS.collectStyles(vertexPrototype, cancel);
		defaultEdgeStyles = defaultGCSS.collectStyles(edgePrototype, cancel);

		// assert that label formats are available - look up label formats in the instance rules.
		//
		if(theGCSS.collectStyles(graphPrototype, cancel).getStyleValue(StyleType.labelFormat, graphPrototype) == null)
			throw new IllegalArgumentException("Default graph label format is null");
		if(theGCSS.collectStyles(vertexPrototype, cancel).getStyleValue(StyleType.labelFormat, vertexPrototype) == null)
			throw new IllegalArgumentException("Default graph label format is null");
		if(theGCSS.collectStyles(edgePrototype, cancel).getStyleValue(StyleType.labelFormat, edgePrototype) == null)
			throw new IllegalArgumentException("Default graph label format is null");

	}

	/**
	 * Produces output in Dot notation on the given stream. The defaultCSS should contain
	 * static rules per element. No reference to style class, instance id, or use of EL is allowed in
	 * this style set. The styleCheets is one or more instance style sheets in increasing order of
	 * importance. Note that as a minimum the style must contain label format styles for the three
	 * element types Vertex, Graph, and Edge. Failing to supply these will result in an IllegalArgumentException.
	 * 
	 * @param cancel
	 *            A cancel indicator that hould periodically be checked for cancellation.
	 * @param stream
	 *            where the dot output should be written
	 * @param graph
	 *            the graph to render
	 * @param defaultCSS
	 *            the default (static CSS)
	 * @param styleSheets
	 *            - use case specific stylesheets.
	 */
	public void write(ICancel cancel, OutputStream stream, IGraph graph, GraphCSS defaultCSS, GraphCSS... styleSheets) {
		if(stream == null)
			throw new IllegalArgumentException("stream is null");
		if(!(stream instanceof PrintStream))
			out = new PrintStream(stream, true);
		else
			out = (PrintStream) stream;

		processGCSS(cancel, defaultCSS, styleSheets);

		// a directed graph (this is the root graph).
		out.printf("digraph %s {\n", graph.getId());

		// print the default styling for graph, node and edge
		printDefaultGraphStyling(cancel);
		printDefaultNodeStyling(cancel);
		printDefaultEdgeStyling(cancel);

		// print the graph
		printGraphBody(graph, cancel);

		// printGraph(graph);

		// close
		out.printf("}\n");
	}
}
