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
package com.puppetlabs.graph.utils;

import java.io.OutputStream;
import java.io.PrintStream;

import com.puppetlabs.graph.IEdge;
import com.puppetlabs.graph.IGraph;
import com.puppetlabs.graph.IVertex;
import com.puppetlabs.graph.style.StyleType;

/**
 * Produces GraphML output from a Graph instance.
 * Uses yFiles extensions for labels and formatting as this allows the file to be opened
 * directly in yEd.
 * 
 * Does not take a GCSS into account.
 * 
 * TODO:This implementation is not production quality.
 * TODO: This implementation uses old style m_ and s_ field names...
 */
public class GraphToGraphML {
	private static String s_labelColor = "#2180c7";

	private static String s_borderColor = "#b3b3b2";

	private static String s_backgroundColor = "#ffffff";

	private static String s_lineColor = "#b3b3b2";

	private static String s_edgeLabelColor = "#b3b3b2";

	private PrintStream m_out;

	public GraphToGraphML(OutputStream stream) {
		if(!(stream instanceof PrintStream))
			m_out = new PrintStream(stream, true);
		else
			m_out = (PrintStream) stream;
	}

	private void printBeginEdgeData() {
		m_out.print("<data key=\"d1\"><y:PolyLineEdge>\n");
	}

	private void printBeginNodeData() {
		m_out.print("<data key=\"d0\"><y:ShapeNode>\n");
	}

	/**
	 * If a label is set, the edge will use it, otherwise edges are without labels.
	 * 
	 * @param edge
	 */
	private void printEdge(IEdge edge) {
		String label = edge.getLabel();
		m_out.printf("<edge source=\"%s\" target=\"%s\">\n", edge.getFrom().getId(), edge.getTo().getId());
		printBeginEdgeData();
		// <y:Path sx="-6.875" sy="15.0" tx="0.0" ty="-15.0">
		// <y:Point x="214.375" y="60.0"/>
		// <y:Point x="100.0" y="60.0"/>
		// </y:Path>
		m_out.printf("<y:LineStyle type=\"line\" width=\"1.0\" color=\"%s\" />\n", s_lineColor);
		m_out.print("<y:Arrows source=\"none\" target=\"short\"/>\n");
		m_out.print("<y:BendStyle smoothed=\"true\"/>\n");
		printEdgeLabel(label);
		printEndEdgeData();
		m_out.print("</edge>\n");
	}

	private void printEdgeLabel(String label) {
		if(label == null || label.length() < 1)
			return;
		m_out.printf(
			"<y:EdgeLabel fontSize=\"9\" textColor=\"%s\">%s</y:EdgeLabel>\n", s_edgeLabelColor,
			translateNewLine(label));
	}

	private void printEndEdgeData() {
		m_out.print("</y:PolyLineEdge></data>\n");
	}

	private void printEndNameSpace() {
		m_out.print("</graphml>\n");
	}

	private void printEndNodeData() {
		m_out.print("</y:ShapeNode></data>\n");
	}

	private void printGraph(IGraph graph) {
		m_out.printf("<graph id=\"%s\" edgedefault=\"directed\">\n", graph.getId());
		printVerticesAndEdges(graph);
		m_out.print("</graph>\n");
	}

	private void printHeading() {
		m_out.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\"\n"
				+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
				+ "xmlns:y=\"http://www.yworks.com/xml/graphml\"\n"
				+ "xsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns\n"
				+ "http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd\">\n");

		// declare the data types for the yFiles extensions
		m_out.print("<key id=\"d0\" for=\"node\" yfiles.type=\"nodegraphics\"/>\n");
		m_out.print("<key id=\"d1\" for=\"edge\" yfiles.type=\"edgegraphics\"/>\n");
		m_out.print("<key id=\"d2\" for=\"node\" attr.name=\"url\" attr.type=\"string\"/>\n");
	}

	private void printNodeLabel(String label) {
		// blue node label
		m_out.printf("<y:NodeLabel textColor=\"%s\">%s</y:NodeLabel>", s_labelColor, translateNewLine(label));
	}

	private void printNodeStyling() {
		// filled with white - non transparent
		m_out.printf("<y:Fill color=\"%s\"  transparent=\"false\"/>\n", s_backgroundColor);
		// grey outline
		m_out.printf("<y:BorderStyle type=\"line\" width=\"1.0\" color=\"%s\" />\n", s_borderColor);
	}

	private void printNodeUrl(IVertex v) {
		String url = (String) v.getStyles().getStyleValue(StyleType.href, v);

		if(url == null || url.length() < 1)
			return;
		m_out.printf("<data key=\"d2\">%s</data>\n", url);

	}

	private void printVertex(IVertex vertex) {
		String label = vertex.getLabel();
		String id = vertex.getId();
		label = (label == null || label.length() < 1)
				? vertex.getId()
				: label;
		m_out.printf("<node id=\"%s\">\n", id);
		printBeginNodeData();
		printNodeStyling();
		printNodeLabel(label);
		printEndNodeData();
		// output a URL if there is one in the data
		printNodeUrl(vertex);
		m_out.print("</node>\n");
	}

	private void printVerticesAndEdges(IGraph graph) {
		// Print all the vertices
		for(IVertex v : graph.getVertices())
			printVertex(v);
		for(IEdge e : graph.getEdges())
			printEdge(e);
	}

	private String translateNewLine(String s) {
		// yfiles has &#xA; as new line in labels
		return s.replace("\n", "&#xA;");
	}

	public void write(IGraph graph) {
		printHeading();

		printGraph(graph);

		printEndNameSpace();
	}

}
