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
package com.puppetlabs.graph.style.labels;

import java.util.Set;

import com.puppetlabs.graph.IGraphElement;
import com.puppetlabs.graph.graphcss.StyleSet;
import com.puppetlabs.graph.style.IStyle;
import com.puppetlabs.graph.style.Span;

import com.google.common.base.Function;

/**
 * Data for label format.
 */
public class LabelCell {
	public static class Separator extends LabelCell {
		public Separator(Function<IGraphElement, Set<String>> styleClass, Function<IGraphElement, ILabelTemplate> value) {
			super(styleClass, value);
		}

		@Override
		public boolean isSeparator() {
			return true;
		}
	}

	private Function<IGraphElement, ILabelTemplate> valueFunc;

	private Function<IGraphElement, Set<String>> styleClassFunc;

	private final Span span;

	private StyleSet instanceStyles;

	/**
	 * @param styleClass2
	 * @param f
	 */
	public LabelCell(Function<IGraphElement, Set<String>> styleClass, Function<IGraphElement, ILabelTemplate> value) {
		this.valueFunc = value;
		this.styleClassFunc = styleClass;
		this.span = Span.SPAN_1x1;
	}

	public LabelCell(Function<IGraphElement, Set<String>> styleClass, Function<IGraphElement, ILabelTemplate> value,
			Span span) {
		this.valueFunc = value;
		this.styleClassFunc = styleClass;
		this.span = span;
	}

	public Span getSpan() {
		return span;
	}

	public Set<String> getStyleClass(IGraphElement ge) {
		return styleClassFunc.apply(ge);
	}

	public StyleSet getStyles() {
		return instanceStyles;
	}

	public ILabelTemplate getValue(IGraphElement ge) {
		return valueFunc.apply(ge);
	}

	public boolean isSeparator() {
		return false;
	}

	public LabelCell withStyle(IStyle<?> style) {
		return withStyles(style);
	}

	/**
	 * Convenience method to set instance styles on a LabelCell.
	 * It is illegal to call this method more than once.
	 * 
	 * @param styles
	 * @return the LabelCell
	 */
	public LabelCell withStyles(IStyle<?>... styles) {
		if(instanceStyles != null)
			throw new IllegalArgumentException("LabelCell's instance style is immutable once set");
		StyleSet styleMap = new StyleSet();
		for(IStyle<?> s : styles)
			styleMap.put(s);
		instanceStyles = styleMap;

		return this;
	}

}
