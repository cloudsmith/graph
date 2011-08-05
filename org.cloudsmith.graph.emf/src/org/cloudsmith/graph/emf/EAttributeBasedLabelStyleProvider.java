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
package org.cloudsmith.graph.emf;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.cloudsmith.graph.IGraphElement;
import org.cloudsmith.graph.graphcss.IFunctionFactory;
import org.cloudsmith.graph.graphcss.Rule;
import org.cloudsmith.graph.graphcss.Select;
import org.cloudsmith.graph.graphcss.StyleSet;
import org.cloudsmith.graph.style.Alignment;
import org.cloudsmith.graph.style.Compass;
import org.cloudsmith.graph.style.IStyleFactory;
import org.cloudsmith.graph.style.Span;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;

/**
 * Provides label definitions based on all defined EAttributes of an EObject's EClass.
 * Requires that vertexes have a method with the signature public EObject getEObject(), and that
 * Vertex style corresponds to getEObject().eClass().getName().
 * See {@link EVertex#getEObject()} for one such implementation.
 * 
 */
public class EAttributeBasedLabelStyleProvider implements IELabelStyleProvider {

	private static class AttributeFunction implements Function<IGraphElement, String> {
		private EAttribute attribute;

		public AttributeFunction(EAttribute attribute) {
			this.attribute = attribute;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.google.common.base.Function#apply(java.lang.Object)
		 */
		@Override
		public String apply(IGraphElement from) {
			if(!(from instanceof EVertex))
				return "ERROR: not an EVertex";
			EObject obj = ((EVertex) from).getEObject();
			Object x = obj.eGet(attribute);
			if(x == null)
				return "null"; // TODO: should probably be ""
			return x.toString(); // TODO: should use a to string provider
		}
	}

	/**
	 * The name of the style containing an attribute value.
	 */
	public static final String STYLE__ATTRIBUTE_VALUE_CELL = "AttributeValueCell";

	/**
	 * The style of the Table Cell containing an attribute name.
	 */
	public static final String STYLE__ATTRIBUTE_NAME_CELL = "AttributeNameCell";

	/**
	 * The style of a Table Row containing one attribute name/value.
	 */
	public static final String STYLE__ATTRIBUTE_ROW = "AttributeRow";

	/**
	 * The style of the Table Cell containing the name of the EObject class.
	 */
	public static final String STYLE__CLASS_NAME_CELL = "ClassNameCell";

	/**
	 * The style of the Table Row containing the EObject class name.
	 */
	public static final String STYLE__CLASS_ROW = "ClassRow";

	/**
	 * The style of the table containing all attributes of an EObject.
	 */
	public static final String STYLE__E_OBJECT_TABLE = "EObjectTable";

	public static final String STYLE__INDEX_CELL = "IndexCell";

	public static final String INDEX_PORT_NAME = "index";

	/**
	 * Style for edges between two index ports.
	 */
	public static final String STYLE__INDEX_TO_INDEX_EDGE = "IndexEdge";

	private Map<EClass, StyleSet> classToStyle;

	private IStyleFactory styles;

	@Inject
	IFunctionFactory functions;

	@Inject
	public EAttributeBasedLabelStyleProvider(IStyleFactory styleFactory) {
		styles = styleFactory;
		classToStyle = Maps.newHashMap();
	}

	protected Rule computeLabelRule(EClass classifier) {
		return new Rule(Select.vertex(classifier.getName()), computeLabelStyle(classifier));

	}

	protected Rule computeLabelRule(EObject o) {
		return computeLabelRule(o.eClass());
	}

	/**
	 * Computes a (complex) table based label for an EClass based on all defined attributes.
	 * 
	 * @param classifier
	 * @return
	 */
	protected StyleSet computeLabelStyle(EClass classifier) {
		if(classifier == null)
			throw new IllegalArgumentException("classifier can not be null");
		List<EAttribute> attributes = Lists.newArrayList();
		attributes.addAll(classifier.getEAllAttributes());
		Collections.sort(attributes, new Comparator<EAttribute>() {

			@Override
			public int compare(EAttribute o1, EAttribute o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});

		org.cloudsmith.graph.style.labels.LabelRow labelRows[] = new org.cloudsmith.graph.style.labels.LabelRow[attributes.size() + 1];

		// First row contains an index cell that spans all rows
		// Class-name cell spans two columns.
		labelRows[0] = styles.labelRow(
			STYLE__CLASS_ROW, //
			styles.labelCell(
				STYLE__INDEX_CELL, functions.labelData(EVertex.DATA_KEY_INDEX), Span.rowSpan(labelRows.length)), //
			styles.labelCell(STYLE__CLASS_NAME_CELL, "<B>" + classifier.getName() + "</B>", Span.colSpan(2)));

		int index = 1;
		for(EAttribute a : attributes) {
			labelRows[index++] = styles.labelRow(STYLE__ATTRIBUTE_ROW, //
				styles.labelCell(STYLE__ATTRIBUTE_NAME_CELL, a.getName()), //
				styles.labelCell(STYLE__ATTRIBUTE_VALUE_CELL, new AttributeFunction(a)));
		}
		return StyleSet.withStyle(styles.labelFormat(styles.labelTable(STYLE__E_OBJECT_TABLE, labelRows)));
	}

	protected StyleSet computeLabelStyle(EObject o) {
		return computeLabelStyle(o.eClass());
	}

	/**
	 * Compute the label style for all types in the model.
	 * Only styles for what is actually in the model are needed.
	 * The returned rules also contains general formatting rules for the generated tables.
	 * 
	 * Each label table is style classified with {@link #STYLE__E_OBJECT_TABLE}, and all cells in such a table
	 * are left aligned. An index cell can be used to link elements in lists.
	 * Edge styles are added with suitable configuration
	 * 
	 * @param model
	 */
	public Collection<Rule> configureLabelStyles(EObject model) {
		List<Rule> styleRules = Lists.newArrayList();
		for(TreeIterator<EObject> itor = model.eAllContents(); itor.hasNext();) {
			EObject x = itor.next();
			// get the style to trigger computation and caching
			getLabelStyle(x);
		}

		// Make cells in a EObjectTable left aligned.
		styleRules.add(Select.cell().and(Select.containment(Select.table(STYLE__E_OBJECT_TABLE))).withStyles(
			styles.align(Alignment.left)));

		// make the index cell have a port called INDEX_PORT_NAME
		// and only rendered if the element depicted in the table has data[index] set.
		styleRules.add(//
		Select.cell(STYLE__INDEX_CELL) //
		.and(Select.containment(Select.table(STYLE__E_OBJECT_TABLE))) //
		.withStyles(styles.port(INDEX_PORT_NAME), //
			styles.rendered(functions.notEmptyLabelData(EVertex.DATA_KEY_INDEX)) //
		));

		// make edges with INDEX_EDGE style go from index:s to index:n
		styleRules.add(Select.edge(STYLE__INDEX_TO_INDEX_EDGE).withStyles(//
			styles.tailPort(INDEX_PORT_NAME, Compass.s), //
			styles.headPort(INDEX_PORT_NAME, Compass.n) //
		));

		for(EClass classifier : classToStyle.keySet()) {
			styleRules.add(getLabelRule(classifier));
		}
		return styleRules;
	}

	protected Rule getLabelRule(EClass classifier) {
		return new Rule(Select.vertex(classifier.getName()), getLabelStyle(classifier));

	}

	protected Rule getLabelRule(EObject o) {
		return getLabelRule(o.eClass());
	}

	protected StyleSet getLabelStyle(EClass classifier) {
		StyleSet style = classToStyle.get(classifier);
		if(style == null)
			classToStyle.put(classifier, style = computeLabelStyle(classifier));
		return style;

	}

	protected StyleSet getLabelStyle(EObject o) {
		return getLabelStyle(o.eClass());

	}
}
