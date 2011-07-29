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
package org.cloudsmith.graph.graphviz;

import java.io.IOException;
import java.io.OutputStream;

import org.cloudsmith.graph.utils.RuleFilteredOutputStream;

/**
 * An SVG stream that fixes broken and bad output from graphviz.
 * <ul>
 * <li>Replaces {@link #EMPTY_STRING_BUG} with ""</li>
 * <li>Replaces content between &lt;title&gt; and &lt;/title&gt; with a single space.</li>
 * <li>Replaces faulty tooltips contining "\L" with ""</li>
 * </ul>
 */
public class SVGFixerOutputStream extends RuleFilteredOutputStream {
	private class TitleEndRule extends RuleFilteredOutputStream.PatternRule {

		public TitleEndRule() {
			super(titleEndPattern);
		}

		@Override
		protected void performAction() throws IOException {
			// a single space replaces what was deleted
			// out.write(' ');
			// write the matched, but not flushed pattern
			buffer.writeFlush(titleEndPattern.length, out);
		}

	}

	private class TitleStartRule extends RuleFilteredOutputStream.PatternRule {

		public TitleStartRule() {
			super(titleStartPattern);
		}

		@Override
		protected void performAction() throws IOException {
			// output "<title>"
			buffer.writeFlush(titleStartPattern.length, out);
			// delete until "</title>"
			setDeleteModeOn(titleEndRule, false);
		}

	}

	public static final String EMPTY_STRING_BUG = "$_$bug1491$_$";

	private final TitleStartRule titleStartRule;

	private final TitleEndRule titleEndRule;

	private final RemoveRule removeEmptyString;

	private final ReplaceRule replaceEmptyLabelTooltip;

	static final byte[] titleStartPattern = "<title>".getBytes();

	static final byte[] titleEndPattern = "</title>".getBytes();

	static final byte[] emptyStringPattern = EMPTY_STRING_BUG.getBytes();

	/**
	 * @param out
	 */
	public SVGFixerOutputStream(OutputStream out) {
		super(out);

		// Replace magic empty string with ""
		removeEmptyString = new RemoveRule(emptyStringPattern);

		// Replace the string "\L" with ""
		replaceEmptyLabelTooltip = new ReplaceRule("\"\\L\"".getBytes(), "\"\"".getBytes());

		// trigger delete of everything after <title>
		titleStartRule = new TitleStartRule();

		// stop deleting when seeing </title> and output "<title> </title>"
		titleEndRule = new TitleEndRule();

		setRules(new PatternRule[] { removeEmptyString, replaceEmptyLabelTooltip, titleStartRule });
	}

}
