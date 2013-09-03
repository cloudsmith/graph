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
package org.cloudsmith.graph.emf;

import java.util.List;

import org.cloudsmith.graph.elements.Edge;
import org.cloudsmith.graph.elements.RootGraph;
import org.cloudsmith.graph.elements.Vertex;
import org.cloudsmith.graph.graphcss.Rule;
import org.cloudsmith.graph.graphcss.Select;
import org.cloudsmith.graph.style.Compass;
import org.cloudsmith.graph.style.IStyleFactory;
import org.cloudsmith.graph.style.LineType;
import org.cloudsmith.graph.style.NodeShape;
import org.cloudsmith.graph.style.labels.LabelMatrix;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

/**
 * Creates a graph with lists being represented as a vertical box 0..n with links to each
 * element.
 * 
 */
public class HorizontalArrayListEGraphProvider extends AbstractEGraphProvider {

	@Inject
	IStyleFactory styles;

	/**
	 * Compute the content of graph g, for the given vertex v representing the given model.
	 * 
	 * @param g
	 * @param v
	 * @param model
	 */
	@Override
	protected void computeGraph(RootGraph g, EVertex v, EObject model) {
		// Process everything the root references (contained, and referenced)
		// Generated Edges have the capability to treat containment and reference differently.
		for(EReference reference : model.eClass().getEAllReferences()) {

			if(reference.isMany()) {
				List<EObject> allReferenced = safeListCast(model.eGet(reference), EObject.class);
				// Create the index node
				int dim = Math.max(allReferenced.size(), 1);
				// the label is not used, but default rule does not render a vertex without a label.
				Vertex indexVertex = new Vertex(Integer.toString(dim), "Index");
				indexVertex.setStyles( //
					styles.labelFormat(new LabelMatrix("Ix", 1, dim)), //
					styles.shape(NodeShape.none), //
					styles.shapeBrush(LineType.solid, 0, false, false));
				g.addVertex(indexVertex);
				EEdge edge1 = new EEdge(reference, v, indexVertex);
				edge1.setStyles(styles.headPort("pt", Compass.n));
				g.addEdge(edge1);

				int counter = 0;
				for(EObject referenced : allReferenced) {
					EVertex v2 = new EVertex(referenced);
					GraphElementAdapterFactory.eINSTANCE.adapt(referenced).setAssociatedInfo(g, v2);
					g.addVertex(v2);

					// link the subsequent element to the previous using and index-to-index edge
					// (it will be connected to the index ports and rendered in a special style)
					// It has no label.
					//
					Edge edge_n = new Edge(null, //
						"IxToElement", //
						indexVertex, v2);
					g.addEdge(edge_n);
					edge_n.setStyles(styles.tailPort("p" + Integer.toString(counter), Compass.s));

					counter++;
					computeGraph(g, v2, referenced);
				}
			}
			else {
				// reference is not a list
				EObject referenced = EObject.class.cast(model.eGet(reference));
				EVertex v2 = new EVertex(referenced);
				GraphElementAdapterFactory.eINSTANCE.adapt(referenced).setAssociatedInfo(g, v2);
				g.addVertex(v2);
				EEdge edge = new EEdge(reference, v, v2);
				g.addEdge(edge);
				computeGraph(g, v2, referenced);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.emf.AbstractEGraphProvider#getRules()
	 */
	@Override
	public Iterable<Rule> getRules() {
		List<Rule> extraRules = Lists.newArrayList();
		extraRules.add(Select.table("Ix").withStyle(styles.cellBorderWidth(1)));
		return Iterables.concat(super.getRules(), extraRules);
	}
}
