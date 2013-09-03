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

import java.io.ByteArrayOutputStream;

import com.puppetlabs.graph.graphviz.SVGFixerOutputStream;
import com.puppetlabs.graph.utils.Base64;
import org.junit.Test;

/**
 * Tests the SVGFixerOutputStream
 * 
 */
public class TestSVGFixer {

	@Test
	public void test_keepClassIfNotBase64Id() throws Exception {
		ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
		SVGFixerOutputStream out = new SVGFixerOutputStream(dataStream);

		StringBuilder text = new StringBuilder();
		text.append("xid=\"theid\" class=\"keep\"y");
		out.write(text.toString().getBytes("UTF8"));
		out.close();
		assertEquals("should have kept the id and class", "xid=\"theid\" class=\"keep\"y", dataStream.toString("UTF8"));
	}

	@Test
	public void test_replaceEmptyLabel() throws Exception {
		ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
		SVGFixerOutputStream out = new SVGFixerOutputStream(dataStream);

		StringBuilder text = new StringBuilder();
		text.append("x\"\\L\"y");
		out.write(text.toString().getBytes("UTF8"));
		out.close();
		assertEquals("should have replaced \"\\L\" with \"\"", "x\"\"y", dataStream.toString("UTF8"));
	}

	@Test
	public void test_replaceIdAndClass() throws Exception {
		ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
		SVGFixerOutputStream out = new SVGFixerOutputStream(dataStream);

		String replacement = "base64:" + Base64.byteArrayToBase64("id=\"new\" class=\"newclass\"".getBytes("UTF8"));
		StringBuilder text = new StringBuilder();
		text.append("xid=\"");
		text.append(replacement);
		text.append("\"");
		text.append(" class=\"keep\"y");
		out.write(text.toString().getBytes("UTF8"));
		out.close();
		assertEquals(
			"should have replaced the id and class", "xid=\"new\" class=\"newclass\"y", dataStream.toString("UTF8"));
	}

	@Test
	public void test_titleFilter() throws Exception {
		ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
		SVGFixerOutputStream out = new SVGFixerOutputStream(dataStream);

		StringBuilder text = new StringBuilder();
		text.append("x<title>y</title>z");
		out.write(text.toString().getBytes("UTF8"));
		out.close();
		assertEquals("title should have been set to  empty string", "x<title></title>z", dataStream.toString("UTF8"));
	}
}
