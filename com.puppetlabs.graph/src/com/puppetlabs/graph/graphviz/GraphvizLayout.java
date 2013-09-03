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
package com.puppetlabs.graph.graphviz;

/**
 * Layout algorithms supported by the {@link Graphviz} runner
 * If you have a directed graph - you probably want {@link #dot} For undirected graphs you probably want {@link #neato}.
 * 
 */
public enum GraphvizLayout {
	dot, neato, circo, fdp, twopi, ;
}
