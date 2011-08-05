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
package org.cloudsmith.graph.graphcss;

import java.io.UnsupportedEncodingException;

import org.cloudsmith.graph.IGraphElement;
import org.cloudsmith.graph.ILabeledGraphElement;
import org.cloudsmith.graph.utils.Base64;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;
import com.google.inject.Singleton;

/**
 * A FunctionFactory producing values that are dynamically produced when applying a style to
 * a graph element.
 * 
 */
@Singleton
public class FunctionFactory implements IFunctionFactory {

	private static class EmptyLabel implements Function<IGraphElement, Boolean> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.google.common.base.Function#apply(java.lang.Object)
		 */
		@Override
		public Boolean apply(IGraphElement ge) {
			if(!(ge instanceof ILabeledGraphElement))
				return false;
			String item = ((ILabeledGraphElement) ge).getLabel();
			return item == null || item.length() == 0;
		}

	}

	private static class EmptyLabelData implements Function<IGraphElement, Boolean> {

		private Object key;

		public EmptyLabelData(Object key) {
			this.key = key;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.google.common.base.Function#apply(java.lang.Object)
		 */
		@Override
		public Boolean apply(IGraphElement ge) {
			if(!(ge instanceof ILabeledGraphElement))
				return false;
			String item = ((ILabeledGraphElement) ge).getUserData().get(key);
			return item == null || item.length() == 0;
		}

	}

	/**
	 * A Function that translates a IGraphElement into a base64 encoded string containing
	 * id="ID" class="CLASS" where:
	 * <ul>
	 * <li>ID is the user data {@link #ID_KEY} value from the element, or if missing the fully qualified id of the given element.</li>
	 * <li>CLASS</li> is the type of element and the style class of the given element</li>
	 * </ul>
	 * The intention is to combine the user of this function to set the id style of
	 * an element and use a SVG post processor that replaces the generated id="..." class="..."
	 * sequence with that encoded in what this function produces.
	 * 
	 * @author henrik
	 * 
	 */
	public static class IdClassReplacerFunction implements Function<IGraphElement, String> {
		public final static String ID_KEY = IdClassReplacerFunction.class.getName() + "_Id";

		@Override
		public String apply(IGraphElement from) {

			String idString = from.getUserData(ID_KEY);
			if(idString == null)
				idString = computeID(from);
			String allStyleClasses = from.getAllStyleClasses();
			StringBuilder builder = new StringBuilder(idString.length() + allStyleClasses.length() + 20);
			builder.append("id=\"");
			builder.append(idString);
			builder.append("\" class=\"");
			builder.append(from.getElementType()); // e.g. "vertex", "edge", etc.
			builder.append(" ");
			builder.append(allStyleClasses);
			builder.append("\"");
			try {
				return "base64:" + Base64.byteArrayToBase64(builder.toString().getBytes("UTF8"));
			}
			catch(UnsupportedEncodingException e) {
				throw new IllegalStateException(e);
			}
		}

		private String computeID(IGraphElement element) {
			IGraphElement[] parents = Iterators.toArray(element.getContext(), IGraphElement.class);
			int plength = (parents == null)
					? 0
					: parents.length;
			StringBuilder buf = new StringBuilder(10 + 5 * plength);
			// add each parents id separated by - start with root (last in array)
			if(parents != null)
				for(int i = parents.length - 1; i >= 0; i--)
					buf.append((i == (plength - 1)
							? ""
							: "-") + parents[i].getId());
			buf.append("-");
			buf.append(element.getId());
			return buf.toString();
		}
	}

	private static class Label implements Function<IGraphElement, String> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.google.common.base.Function#apply(java.lang.Object)
		 */
		@Override
		public String apply(IGraphElement ge) {
			if(!(ge instanceof ILabeledGraphElement))
				return "";
			return ((ILabeledGraphElement) ge).getLabel();
		}

	}

	private static class LabelData implements Function<IGraphElement, String> {

		private Object key;

		public LabelData(Object key) {
			this.key = key;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.google.common.base.Function#apply(java.lang.Object)
		 */
		@Override
		public String apply(IGraphElement ge) {
			if(!(ge instanceof ILabeledGraphElement))
				return "";
			return ((ILabeledGraphElement) ge).getUserData().get(key);
		}
	}

	private static class LiteralString implements Function<IGraphElement, String> {

		private String value;

		public LiteralString(String value) {
			this.value = value;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.google.common.base.Function#apply(java.lang.Object)
		 */
		@Override
		public String apply(IGraphElement ge) {
			return value;
		}
	}

	public static class Not implements Function<IGraphElement, Boolean> {

		Function<IGraphElement, Boolean> function;

		public Not(Function<IGraphElement, Boolean> function) {
			this.function = function;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.google.common.base.Function#apply(java.lang.Object)
		 */
		@Override
		public Boolean apply(IGraphElement ge) {
			return !function.apply(ge);
		}

	}

	private static final EmptyLabel theEmptyLabelFunction = new EmptyLabel();

	private static final Not theNotEmptyLabelFunction = new Not(theEmptyLabelFunction);

	private static final Label theLabelFunction = new Label();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.graphcss.IFunctionFactory#emptyLabel()
	 */
	@Override
	public Function<IGraphElement, Boolean> emptyLabel() {
		return theEmptyLabelFunction;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.graphcss.IFunctionFactory#emptyLabelData(java.lang.Object)
	 */
	@Override
	public Function<IGraphElement, Boolean> emptyLabelData(Object key) {
		return new EmptyLabelData(key);
	}

	@Override
	public Function<IGraphElement, String> idClassReplacer() {
		return new IdClassReplacerFunction();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.graphcss.IFunctionFactory#label()
	 */
	@Override
	public Function<IGraphElement, String> label() {
		return theLabelFunction;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.graphcss.IFunctionFactory#labelData(java.lang.Object)
	 */
	@Override
	public Function<IGraphElement, String> labelData(Object key) {
		return new LabelData(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.graphcss.IFunctionFactory#literalString(java.lang.String)
	 */
	@Override
	public Function<IGraphElement, String> literalString(String s) {
		return new LiteralString(s);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.graphcss.IFunctionFactory#notEmptyLabel()
	 */
	@Override
	public Function<IGraphElement, Boolean> notEmptyLabel() {
		return theNotEmptyLabelFunction;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.graphcss.IFunctionFactory#notEmptyLabelData(java.lang.Object)
	 */
	@Override
	public Function<IGraphElement, Boolean> notEmptyLabelData(Object key) {
		return new Not(emptyLabelData(key));
	}
}
