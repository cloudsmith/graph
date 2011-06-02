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

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.cloudsmith.graph.IGraph;
import org.cloudsmith.graph.IGraphProvider;
import org.cloudsmith.graph.elements.Edge;
import org.cloudsmith.graph.elements.Graph;
import org.cloudsmith.graph.graphcss.Rule;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;

/**
 * Provides an instance graph for a model.
 * 
 */
public class EGraphProvider implements IGraphProvider {

	@Inject
	private IELabelStyleProvider labelStyleProvider;

	private Collection<Rule> rules;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.IGraphProvider#computeGraph()
	 */
	@Override
	public IGraph computeGraph() {
		throw new UnsupportedOperationException("An EGraphProvider must have a model to transform.");
	}

	private void computeGraph(Graph g, EVertex v, EObject model) {
		// Process everything the root references (contained, and referenced)
		for(EReference reference : Iterables.concat(
			model.eClass().getEAllContainments(), model.eClass().getEAllReferences())) {
			if(reference.isMany()) {
				List<EObject> allReferenced = safeListCast(model.eGet(reference), EObject.class);
				int counter = 0;
				EVertex previousVertex = v;
				for(EObject referenced : allReferenced) {
					EVertex v2 = new EVertex(referenced);
					GraphElementAdapterFactory.eINSTANCE.adapt(referenced).setAssociatedInfo(g, v2);
					g.addVertex(v2);

					// set the index key so that it can be displayed in the resulting
					// label/table.
					//
					v2.getData().put(EVertex.DATA_KEY_INDEX, Integer.toString(counter));

					// The main edge is an EEdge configured from the reference
					if(counter == 0) {
						EEdge edge_1 = new EEdge(reference, previousVertex, v2);
						g.addEdge(edge_1);

					}
					else {
						// link the subsequent element to the previous using and index-to-index edge
						// (it will be connected to the index ports and rendered in a special style)
						// It has no label.
						//
						Edge edge_n = new Edge(null, //
							EAttributeBasedLabelStyleProvider.STYLE__INDEX_TO_INDEX_EDGE, //
							previousVertex, v2);
						g.addEdge(edge_n);
					}
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
	 * @see org.cloudsmith.graph.IGraphProvider#computeGraph(java.lang.Object)
	 */
	@Override
	public IGraph computeGraph(Object model) {
		return computeGraph(model, "an EMF instance graph", "root");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.emf.IEGraphProvider#computeGraph(org.eclipse.emf.ecore.EObject, java.lang.String, java.lang.String)
	 */
	@Override
	public IGraph computeGraph(Object modelObj, String label, String id) {
		if(!(modelObj instanceof EObject))
			throw new IllegalArgumentException("EGraphProvider can only compute a graph for an EObject");
		EObject model = (EObject) modelObj;
		// Create the main graph
		Graph g = new Graph(label, id);

		// Compute the label style for all classes in the model
		rules = Collections.unmodifiableCollection(labelStyleProvider.configureLabelStyles(model));

		// Add a root vertex for the model itself.
		EVertex v = new EVertex(model);
		GraphElementAdapterFactory.eINSTANCE.adapt(model).setAssociatedInfo(g, v);
		g.addVertex(v);
		computeGraph(g, v, model);
		return g;
	}

	@Override
	public Collection<Rule> getRules() {
		if(rules == null)
			rules = Collections.emptyList();
		return rules;
	}

	@SuppressWarnings("unchecked")
	private <T> List<T> safeListCast(Object o, Class<T> clazz) {
		if(!(o instanceof List))
			throw new ClassCastException("Can not cast object to a List. Class: " + o.getClass().getName());
		List<?> x = (List<?>) o;
		if(x.size() > 0) {
			clazz.cast(x.get(0)); // cast for side effect only
		}
		return (List<T>) x;
	}
}
