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
package com.puppetlabs.graph.emf;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.puppetlabs.graph.IGraphProvider;
import com.puppetlabs.graph.IRootGraph;
import com.puppetlabs.graph.elements.RootGraph;
import com.puppetlabs.graph.graphcss.Rule;
import org.eclipse.emf.ecore.EObject;

import com.google.inject.Inject;

/**
 * Provides an instance graph for a model.
 * 
 */
public abstract class AbstractEGraphProvider implements IGraphProvider {

	@Inject
	private IELabelStyleProvider labelStyleProvider;

	private Collection<Rule> rules;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.puppetlabs.graph.IGraphProvider#computeGraph()
	 */
	@Override
	public IRootGraph computeGraph() {
		throw new UnsupportedOperationException("An EGraphProvider must have a model to transform.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.puppetlabs.graph.IGraphProvider#computeGraph(java.lang.Object)
	 */
	@Override
	public IRootGraph computeGraph(Object model) {
		return computeGraph(model, "an EMF instance graph", "root");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.puppetlabs.graph.emf.IEGraphProvider#computeGraph(org.eclipse.emf.ecore.EObject, java.lang.String, java.lang.String)
	 */
	@Override
	public IRootGraph computeGraph(Object modelObj, String label, String id) {
		if(!(modelObj instanceof EObject))
			throw new IllegalArgumentException("EGraphProvider can only compute a graph for an EObject");
		EObject model = (EObject) modelObj;
		// Create the main graph
		RootGraph g = new RootGraph(label, "RootGraph", id);

		// Compute the label style for all classes in the model
		rules = Collections.unmodifiableCollection(labelStyleProvider.configureLabelStyles(model));

		// Add a root vertex for the model itself.
		EVertex v = new EVertex(model);
		GraphElementAdapterFactory.eINSTANCE.adapt(model).setAssociatedInfo(g, v);
		g.addVertex(v);
		computeGraph(g, v, model);
		return g;
	}

	/**
	 * Compute the content of graph g, for the given vertex v representing the given model.
	 * 
	 * @param g
	 * @param v
	 * @param model
	 */

	abstract protected void computeGraph(RootGraph g, EVertex v, EObject model);

	@Override
	public Iterable<Rule> getRules() {
		if(rules == null)
			rules = Collections.emptyList();
		return rules;
	}

	@SuppressWarnings("unchecked")
	protected <T> List<T> safeListCast(Object o, Class<T> clazz) {
		if(!(o instanceof List))
			throw new ClassCastException("Can not cast object to a List. Class: " + o.getClass().getName());
		List<?> x = (List<?>) o;
		if(x.size() > 0) {
			clazz.cast(x.get(0)); // cast for side effect only
		}
		return (List<T>) x;
	}
}
