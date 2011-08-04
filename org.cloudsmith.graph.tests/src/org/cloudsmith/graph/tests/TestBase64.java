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

import org.cloudsmith.graph.utils.Base64;

/**
 * Test base64 conversion.
 * 
 */
public class TestBase64 extends TestCase {

	public void testBase64() {
		StringBuilder b = new StringBuilder();
		byte buf[] = new byte[0xff + 1];
		for(int i = 0; i < 0xff; i++)
			buf[i] = (byte) i;
		String data = Base64.byteArrayToBase64(buf);
		byte[] buf2 = Base64.base64ToByteArray(data);
		for(int i = 0; i < 0xff; i++)
			assertEquals("before and after conversion", buf[i], buf2[i]);
	}
}
