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
package com.puppetlabs.graph.utils;

/**
 * A counter that is useful when a final int is not working...
 * 
 */
public class Counter {

	private int count = 0;

	private String separator = ",\n";

	public Counter() {
	}

	public Counter(char... cs) {
		separator = String.valueOf(cs);
	}

	public Counter(String separator) {
		this.separator = separator;
	}

	public void decrement() {
		count--;
	}

	public void increment() {
		count++;
	}

	public boolean isSeparatorNeeded() {
		return count != 0;
	}

	public String separator() {
		return count != 0
				? separator
				: "";
	}

	public int value() {
		return count;
	}
}
