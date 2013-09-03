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
package com.puppetlabs.graph.tests;

import static org.junit.Assert.assertEquals;

import com.puppetlabs.graph.utils.Base64;
import org.junit.Test;

/**
 * Test base64 conversion.
 * 
 */
public class TestBase64 {

	@Test
	public void testBase64() {
		byte buf[] = new byte[0xff + 1];
		for(int i = 0; i < 0xff; i++)
			buf[i] = (byte) i;
		String data = Base64.byteArrayToBase64(buf);
		byte[] buf2 = Base64.base64ToByteArray(data);
		for(int i = 0; i < 0xff; i++)
			assertEquals("before and after conversion", buf[i], buf2[i]);
	}
}
