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

import junit.framework.TestCase;

import org.cloudsmith.graph.DefaultGraphModule;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 *
 */
public class AbstractGraphTests extends TestCase {
	private Injector injector;

	protected <T> T get(Class<T> clazz) {
		return getInjector().getInstance(clazz);
	}

	protected Injector getInjector() {
		return injector;
	}

	/**
	 * This implementation returns a DefaultGraphModule.
	 * 
	 * @return
	 */
	protected Module getModule() {
		return new DefaultGraphModule();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		injector = Guice.createInjector(getModule());
	}
}
