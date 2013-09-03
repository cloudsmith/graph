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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;

import org.cloudsmith.graph.graphviz.SVGFixerOutputStream;
import org.cloudsmith.graph.utils.RuleFilteredOutputStream;
import org.cloudsmith.graph.utils.RuleFilteredOutputStream.ReplaceRule;
import org.junit.Test;

/**
 * tests the RuleFilteredOutputStream
 * 
 */
public class TestRuleBasedFilterStream {

	@Test
	public void test_replace() throws Exception {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		@SuppressWarnings("resource")
		RuleFilteredOutputStream out = new RuleFilteredOutputStream(stream);
		ReplaceRule r = out.new ReplaceRule("1".getBytes(), "a".getBytes());
		out.setRules(new RuleFilteredOutputStream.PatternRule[] { r });

		out.write("12345678901234567890".getBytes());
		out.flush();
		assertEquals("read what was replaced", "a234567890a234567890", stream.toString());

	}

	@Test
	public void test_svgFixerStream() throws Exception {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		@SuppressWarnings("resource")
		SVGFixerOutputStream out = new SVGFixerOutputStream(stream);

		out.write(("aaaaa<title>bbbbb</title>aaaaa<xref:xxx=\"\\L\">aaaa<xref:title=\"" +
				SVGFixerOutputStream.EMPTY_STRING_BUG + "\">aaa").getBytes());
		out.finish();

		String content = stream.toString();
		assertFalse("no \\L in the text", content.contains("\\L"));
		assertFalse("no bbbbb in the text", content.contains("bbbbb"));
		assertFalse("no magic empty string in the text", content.contains(SVGFixerOutputStream.EMPTY_STRING_BUG));
		assertTrue("contins <title></title>", content.contains("<title></title>"));
		System.err.println(content);
		assertTrue("contains xref:title=\"\"", content.contains("xref:title=\"\""));

	}

	@Test
	public void test_transparent() throws Exception {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		@SuppressWarnings("resource")
		RuleFilteredOutputStream out = new RuleFilteredOutputStream(stream);

		out.write("1234567890".getBytes());
		out.flush();
		assertEquals("read what was written", "1234567890", stream.toString());
	}

}
