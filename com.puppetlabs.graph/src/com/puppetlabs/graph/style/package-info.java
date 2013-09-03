/*
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
/**
 * This package contains an implementation that describes the style of {@link com.puppetlabs.graph.IGraph} graphs.
 * 
 * The main interface is the {@link com.puppetlabs.graph.style.IStyleFactory} which should be used to
 * obtain styles. The {@link com.puppetlabs.graph.style.StyleFactory} is a default implementation of 
 * this interface. 
 * 
 * The classes in the package may be injected with Google Guice, but may also be instantiated manually.
 */
package com.puppetlabs.graph.style;