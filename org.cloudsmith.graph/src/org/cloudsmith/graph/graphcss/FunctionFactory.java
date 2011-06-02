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

import org.cloudsmith.graph.IGraphElement;
import org.cloudsmith.graph.ILabeledGraphElement;

import com.google.common.base.Function;
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
			String item = ((ILabeledGraphElement) ge).getData().get(key);
			return item == null || item.length() == 0;
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
			return ((ILabeledGraphElement) ge).getData().get(key);
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

	/* (non-Javadoc)
	 * @see org.cloudsmith.graph.graphcss.IFunctionFactory#emptyLabel()
	 */
	@Override
	public Function<IGraphElement, Boolean> emptyLabel() {
		return theEmptyLabelFunction;
	}

	/* (non-Javadoc)
	 * @see org.cloudsmith.graph.graphcss.IFunctionFactory#emptyLabelData(java.lang.Object)
	 */
	@Override
	public Function<IGraphElement, Boolean> emptyLabelData(Object key) {
		return new EmptyLabelData(key);
	}

	/* (non-Javadoc)
	 * @see org.cloudsmith.graph.graphcss.IFunctionFactory#label()
	 */
	@Override
	public Function<IGraphElement, String> label() {
		return theLabelFunction;
	}

	/* (non-Javadoc)
	 * @see org.cloudsmith.graph.graphcss.IFunctionFactory#labelData(java.lang.Object)
	 */
	@Override
	public Function<IGraphElement, String> labelData(Object key) {
		return new LabelData(key);
	}

	/* (non-Javadoc)
	 * @see org.cloudsmith.graph.graphcss.IFunctionFactory#literalString(java.lang.String)
	 */
	@Override
	public Function<IGraphElement, String> literalString(String s) {
		return new LiteralString(s);
	}

	/* (non-Javadoc)
	 * @see org.cloudsmith.graph.graphcss.IFunctionFactory#notEmptyLabel()
	 */
	@Override
	public Function<IGraphElement, Boolean> notEmptyLabel() {
		return theNotEmptyLabelFunction;
	}

	/* (non-Javadoc)
	 * @see org.cloudsmith.graph.graphcss.IFunctionFactory#notEmptyLabelData(java.lang.Object)
	 */
	@Override
	public Function<IGraphElement, Boolean> notEmptyLabelData(Object key) {
		return new Not(emptyLabelData(key));
	}

}
