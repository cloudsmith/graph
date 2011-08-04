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

import org.cloudsmith.graph.utils.Base64;
import org.cloudsmith.graph.utils.RuleFilteredOutputStream;

/**
 * An SVG stream that fixes broken and bad output from graphviz.
 * <ul>
 * <li>Replaces {@link #EMPTY_STRING_BUG} with ""</li>
 * <li>Replaces content between &lt;title&gt; and &lt;/title&gt; with a single space.</li>
 * <li>Replaces faulty tooltips contining "\L" with ""</li>
 * <li>Replaces a sequence of id="X" class="..." with X</li>
 * </ul>
 */
public class SVGFixerOutputStream extends RuleFilteredOutputStream {

	/**
	 * If a class="..." was preceeded with a rule that processed a base64 encoded id this
	 * rule will delete the class="...", else flush it to output.
	 * 
	 */
	private class RemoveClassRule extends RuleFilteredOutputStream.PatternRule {
		public RemoveClassRule() {
			super("class=\"".getBytes());
		}

		@Override
		protected void performAction() throws IOException {
			// search for closing " after class="
			int endIdx = buffer.indexOf((byte) '"', 7);
			if(endIdx < 0) {
				setPendingRule(this);
				return; // not enough data
			}
			setPendingRule(null);
			if(seenBase64Id)
				buffer.delete(endIdx + 1); // remove class="...."
			else
				buffer.writeFlush(endIdx + 1, out);
			seenBase64Id = false;
		}
	}

	/**
	 * Rule that is triggered by id="base64:BASE64DATA....". When triggered, it will replace
	 * the id="..." sequence with the decoded content of base64 data.
	 * 
	 */
	private class ReplaceIdWithIdContent extends RuleFilteredOutputStream.PatternRule {
		private static final String idPattern = "id=\"base64:";

		private static final int idPatternLength = 11; // LENGHT OF idPattern

		public ReplaceIdWithIdContent() {
			super(idPattern.getBytes());
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.cloudsmith.graph.utils.RuleFilteredOutputStream.PatternRule#performAction()
		 */
		@Override
		protected void performAction() throws IOException {
			// search for closing " after id="
			int endIdx = buffer.indexOf((byte) '"', idPatternLength);
			if(endIdx < 0) {
				setPendingRule(this);
				return; // not enough data
			}
			setPendingRule(null);
			buffer.delete(idPatternLength); // remove id="
			byte[] idData = buffer.remove(endIdx - idPatternLength); // content between ""
			buffer.delete(1); // closing "

			// remove trailing space
			while(buffer.size() > 0 && buffer.peek() == ' ')
				buffer.delete(1);

			// decode the base64 content read, and write the result in plain text.
			out.write(Base64.base64ToByteArray(new String(idData, "UTF8")));
			seenBase64Id = true;
		}
	}

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
			// delete until "</title>" non inclusive
			setDeleteModeOn(titleEndRule, false);
		}

	}

	private boolean seenBase64Id = false;

	public static final String EMPTY_STRING_BUG = "$_$bug1491$_$";

	private final TitleStartRule titleStartRule;

	private final TitleEndRule titleEndRule;

	private final RemoveRule removeEmptyString;

	private final ReplaceRule replaceEmptyLabelTooltip;

	static final byte[] titleStartPattern = "<title>".getBytes();

	static final byte[] titleEndPattern = "</title>".getBytes();

	static final byte[] emptyStringPattern = EMPTY_STRING_BUG.getBytes();

	private final ReplaceIdWithIdContent replaceIdWithIdContentRule;

	private final RemoveClassRule removeClassRule;

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

		replaceIdWithIdContentRule = new ReplaceIdWithIdContent();

		removeClassRule = new RemoveClassRule();

		setRules(new PatternRule[] { //
		removeEmptyString, //
				replaceEmptyLabelTooltip, //
				titleStartRule, //
				replaceIdWithIdContentRule, //
				removeClassRule //
		});

	}

}
