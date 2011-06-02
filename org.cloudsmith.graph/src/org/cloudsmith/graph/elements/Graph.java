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
package org.cloudsmith.graph.elements;

import java.util.ArrayList;

import org.cloudsmith.graph.ElementType;
import org.cloudsmith.graph.IEdge;
import org.cloudsmith.graph.IGraph;
import org.cloudsmith.graph.IVertex;

/**
 * A graph is a container of other graph elements (which include other graphs as subgraphs, vertexes and edges).
 * This implementation of {@link IGraph} works with instances of other graph elements in the same package
 * as it manages their containment and identity (if not set).
 * 
 */
public class Graph extends Vertex implements IGraph {
	private ArrayList<IEdge> edges;

	private ArrayList<IGraph> subgraphs;

	private ArrayList<IVertex> vertices;

	private boolean cluster = false;

	public Graph(String label, String styleClass) {
		this(label, styleClass, null);
	}

	public Graph(String label, String styleClass, String id) {
		super(label, styleClass, id);

		edges = new ArrayList<IEdge>();
		vertices = new ArrayList<IVertex>();
		subgraphs = new ArrayList<IGraph>();
	}

	/**
	 * Not part of the API - how edges are created is up to the implementation.
	 * 
	 * @param edge
	 */
	public void _addEdge(Edge edge) {
		edges.add(edge);
		if(edge.getId() == null)
			edge.setId("e" + edges.size());
		edge.setParentElement(this);
	}

	private void _addVertex(Vertex vertex) {
		vertices.add(vertex);
		if(vertex.getId() == null)
			vertex.setId("v" + vertices.size());
		vertex.setParentElement(this);
	}

	/**
	 * Not part of the API - how edges are created is up to the implementation.
	 * 
	 * @param edge
	 */
	public void addEdge(Edge edge, Edge... edges) {
		_addEdge(edge);
		for(Edge e : edges)
			_addEdge(e);
	}

	/**
	 * Not part of the API - how subgraphs are created and added is up to the implementation.
	 * 
	 * @param graph
	 */
	public void addSubgraph(Graph graph) {
		subgraphs.add(graph);
		if(graph.getId() == null)
			graph.setId("g" + subgraphs.size());
		graph.setParentElement(this);
	}

	/**
	 * Not part of the API - how vertexes are created and added is up to the implementation.
	 * 
	 * @param vertex
	 */
	public void addVertex(Vertex v0, Vertex... vn) {
		_addVertex(v0);
		for(Vertex v : vn)
			_addVertex(v);
	}

	@Override
	public Iterable<IEdge> getEdges() {
		return edges;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.elements.Vertex#getElementType()
	 */
	@Override
	public ElementType getElementType() {
		return ElementType.graph;
	}

	@Override
	public Iterable<IGraph> getSubgraphs() {
		return subgraphs;
	}

	@Override
	public Iterable<IVertex> getVertices() {
		return vertices;
	}

	@Override
	public boolean isCluster() {
		return cluster;
	}

	/**
	 * Not part of API.
	 * 
	 * @param flag
	 */
	public void setCluster(boolean flag) {
		cluster = flag;
	}
}
