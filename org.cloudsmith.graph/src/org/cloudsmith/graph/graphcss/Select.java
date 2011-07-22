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
package org.cloudsmith.graph.graphcss;

import java.util.EnumSet;
import java.util.Set;

import org.cloudsmith.graph.ElementType;
import org.cloudsmith.graph.IEdge;
import org.cloudsmith.graph.IGraphElement;
import org.cloudsmith.graph.style.IStyle;

import com.google.common.collect.Iterators;

/**
 * Style Select is used to produce a configured Selector.
 * 
 */
public class Select {

	/**
	 * A compound rule, where all rules must be satisfied.
	 * 
	 */
	public static class And extends Selector {
		private int specificity = 0;

		private Selector[] selectors;

		public And(Selector... selectors) {
			this.selectors = selectors;
		}

		/**
		 * Important - two And selectors are considered equal only if they have the rules in the same order.
		 * The correctness of this can be discussed.
		 */
		@Override
		public boolean equalMatch(Selector selector) {
			if(!(selector instanceof And))
				return false;
			And a = (And) selector;
			if(selectors.length != a.selectors.length)
				return false;
			for(int i = selectors.length; i >= 0; i--)
				if(!selectors[i].equalMatch(a.selectors[i]))
					return false;
			return true;
		}

		@Override
		public int getSpecificity() {
			if(specificity != 0)
				return specificity;
			for(int i = 0; i < selectors.length; i++)
				specificity += selectors[i].getSpecificity();
			return specificity;
		}

		@Override
		public boolean matches(IGraphElement element) {
			for(int i = 0; i < selectors.length; i++)
				if(!selectors[i].matches(element))
					return false;
			return true;
		}
	}

	/**
	 * Selector for edges that connect a source and a target element.
	 * Source and target selection is based on the context of the source/target elements and
	 * their contexts. To match context for the edge, an additional rule is needed.
	 * 
	 */
	public static class Between extends Selector {
		private Selector sourceSelector;

		private Selector targetSelector;

		public Between(Selector sourceSelector, Selector targetSelector) {
			this.sourceSelector = sourceSelector;
			this.targetSelector = targetSelector;
		}

		@Override
		public boolean equalMatch(Selector selector) {
			if(!(selector instanceof Between))
				return false;
			Between b = (Between) selector;

			if(!(sourceSelector.equalMatch(b.sourceSelector) && targetSelector.equalMatch(b.targetSelector)))
				return false;
			return true;
		}

		@Override
		public int getSpecificity() {
			// no need to cache this result
			return sourceSelector.getSpecificity() + targetSelector.getSpecificity();
		}

		@Override
		public boolean matches(IGraphElement element) {
			if(!(element instanceof IEdge))
				return false;
			IEdge edge = (IEdge) element;
			IGraphElement source = edge.getFrom();
			IGraphElement target = edge.getTo();
			// prototype edges may have no source and target
			// they can't possibly be "between" two selected things
			if(source == null || target == null)
				return false;
			if(!sourceSelector.matches(source))
				return false;
			if(!targetSelector.matches(target))
				return false;
			return true;
		}

	}

	/**
	 * Matches containment where each selector must match an containing element.
	 * The interpretation allows for "holes" - i.e. the rule (==A ==C) matches the containment
	 * in the context (X Y A B C element) since element is contained in a C, that in turn is contained
	 * in an A). This is similar to how the CSS containment rule works.
	 */
	public static class Containment extends Selector {
		private int specificity = 0;

		private Selector[] selectors;

		/**
		 * Selectors for containers - the nearest container first
		 * 
		 * @param selectors
		 */
		public Containment(Selector... selectors) {
			if(selectors == null || selectors.length < 1)
				throw new IllegalArgumentException("no selectors specified");
			this.selectors = selectors;
		}

		@Override
		public boolean equalMatch(Selector selector) {
			if(!(selector instanceof Containment))
				return false;
			Containment c = (Containment) selector;
			if(selectors.length != c.selectors.length)
				return false;
			for(int i = selectors.length; i >= 0; i--)
				if(!selectors[i].equalMatch(c.selectors[i]))
					return false;
			return true;
		}

		@Override
		public int getSpecificity() {
			if(specificity != 0)
				return specificity;
			for(int i = 0; i < selectors.length; i++)
				specificity += selectors[i].getSpecificity();
			return specificity;
		}

		/**
		 * context vector has nearest container first
		 */
		@Override
		public boolean matches(IGraphElement element) {
			IGraphElement[] context = Iterators.toArray(element.getContext(), IGraphElement.class);
			int startIdx = 0;
			int matchCount = 0;
			for(int si = 0; si < selectors.length; si++) {
				for(int ci = startIdx; ci < context.length; ci++)
					if(selectors[si].matches(context[ci])) {
						// match
						startIdx = ci + 1; // next rule must match context further away
						matchCount++; // one match ok
						break;
					}
			}
			// match if all containment rules where satisfied
			return (matchCount == selectors.length)
					? true
					: false;
		}

	}

	/**
	 * Matches on Element type, style class and id.
	 * 
	 */
	public static class Element extends Selector {
		private Set<ElementType> matchElement;

		private String matchClass;

		private String matchId;

		private int specificity = 0;

		public Element(ElementType t) {
			this(EnumSet.of(t));
		}

		public Element(ElementType elementType, String styleClass, String id) {
			this(EnumSet.of(elementType), styleClass, id);
		}

		public Element(Set<ElementType> elementType) {
			this(elementType, null, null);
		}

		public Element(Set<ElementType> elementType, String styleClass, String id) {
			if(elementType == null) {
				elementType = EnumSet.allOf(ElementType.class);
			}
			this.matchElement = elementType;
			this.matchClass = styleClass != null
					? styleClass
					: "";
			this.matchId = id != null
					? id
					: "";
		}

		public Element(String styleClass, String id) {
			this(EnumSet.allOf(ElementType.class), styleClass, id);
		}

		@Override
		public boolean equalMatch(Selector selector) {
			if(!(selector instanceof Element))
				return false;
			Element e = (Element) selector;
			if(!matchElement.equals(e.matchElement))
				return false;
			if(!matchClass.equals(e.matchClass))
				return false;
			if(!matchId.equals(e.matchId))
				return false;
			return true;
		}

		@Override
		public int getSpecificity() {
			if(specificity > 0)
				return specificity;
			specificity = 0;

			if(matchId != null && matchId.length() > 0)
				specificity += 100;

			if(matchClass != null && matchClass.length() > 0)
				specificity += 10;

			switch(matchElement.size()) {
				case ElementType.NUM_TYPES:
					// if matching any == 0
					break;
				case 1:
					// if matching single element type == 2
					specificity += 2;
					break;
				default:
					// if matching multiple types but not all == 1
					specificity++;
			}
			return specificity;
		}

		@Override
		public boolean matches(IGraphElement element) {
			if(!matchElement.contains(element.getElementType()))
				return false;

			// switch(matchElement) {
			// case any:
			// break;
			// case graph:
			// if(!(element instanceof IGraph))
			// return false;
			// break;
			// case vertex:
			// if(!(element instanceof IVertex))
			// return false;
			// break;
			// case edge:
			// if(!(element instanceof IEdge))
			// return false;
			// break;
			// case table:
			// if(!(element instanceof IGraphTable))
			// return false;
			// break;
			// case cell:
			// if(!(element instanceof IGraphCell))
			// return false;
			// break;
			// }
			if(matchClass != null && matchClass.length() > 0 && !matchClass.equals(element.getStyleClass()))
				return false;
			if(matchId != null && matchId.length() > 0 && !matchId.equals(element.getId()))
				return false;
			return true;
		}
	}

	/**
	 * The most specific selector that matches on a instance using ==.
	 * 
	 */
	public static class Instance extends Selector {
		private final IGraphElement element;

		public Instance(IGraphElement element) {
			this.element = element;
		}

		@Override
		public boolean equalMatch(Selector selector) {
			if(!(selector instanceof Instance))
				return false;
			return this.element == ((Instance) selector).element;
		}

		@Override
		public int getSpecificity() {
			return MAX_SPECIFICITY;
		}

		@Override
		public boolean matches(IGraphElement element) {
			return this.element == element;
		}
	}

	/**
	 * A NullSelector is only useful in a NullRule. It matches nothing.
	 * 
	 */
	public static class NullSelector extends Selector {

		@Override
		public boolean equalMatch(Selector selector) {
			return false;
		}

		@Override
		public int getSpecificity() {
			return 0;
		}

		@Override
		public boolean matches(IGraphElement element) {
			return false;
		}

	}

	public static class ParentSelector extends Selector {
		private Selector parentSelector;

		public ParentSelector(Selector parentSelector) {
			this.parentSelector = parentSelector;
		}

		@Override
		public boolean equalMatch(Selector s) {
			if(!(s instanceof ParentSelector))
				return false;
			return parentSelector.equalMatch(((ParentSelector) s).parentSelector);
		}

		@Override
		public int getSpecificity() {
			return parentSelector.getSpecificity();
		}

		@Override
		public boolean matches(IGraphElement element) {
			return parentSelector.matches(element.getParentElement());
		}
	}

	public static abstract class Selector {
		public static final int MAX_SPECIFICITY = Integer.MAX_VALUE;

		public And and(Selector selector) {
			return new And(this, selector);
		}

		public abstract boolean equalMatch(Selector selector);

		/**
		 * Returns the specificity in the same style as used in CSS:
		 * 100 * id count + 10 * class count + 1 * other count
		 * 
		 * @return
		 */
		public abstract int getSpecificity();

		public abstract boolean matches(IGraphElement element);

		public Rule withStyle(IStyle<? extends Object> styles) {
			return new Rule(this, StyleSet.withStyles(styles));
		}

		public Rule withStyle(StyleSet styleMap) {
			return new Rule(this, styleMap);
		}

		public Rule withStyles(IStyle<?>... styles) {
			return new Rule(this, StyleSet.withStyles(styles));
		}
	}

	public static Select.And and(Select.Selector... selectors) {
		return new Select.And(selectors);
	}

	public static Select.Element any() {
		return new Select.Element(ElementType.ANY);
	}

	public static Select.Element any(String styleClass) {
		return new Select.Element(ElementType.ANY, styleClass, null);
	}

	public static Select.Element any(String styleClass, String id) {
		return new Select.Element(ElementType.ANY, styleClass, id);
	}

	public static Select.Between between(Select.Selector source, Select.Selector target) {
		return new Select.Between(source, target);
	}

	public static Select.Element cell() {
		return new Select.Element(ElementType.cell);
	}

	public static Select.Element cell(String styleClass) {
		return new Select.Element(ElementType.cell, styleClass, null);
	}

	public static Select.Element cell(String styleClass, String id) {
		return new Select.Element(ElementType.cell, styleClass, id);
	}

	public static Select.Containment containment(Select.Selector... selectors) {
		return new Select.Containment(selectors);
	}

	public static Select.Element edge() {
		return new Select.Element(ElementType.edge);
	}

	public static Select.Element edge(String styleClass) {
		return new Select.Element(ElementType.edge, styleClass, null);
	}

	public static Select.Element edge(String styleClass, String id) {
		return new Select.Element(ElementType.edge, styleClass, id);
	}

	public static Select.Element element(ElementType type) {
		return new Select.Element(type);
	}

	public static Select.Element element(ElementType type, String styleClass, String id) {
		return new Select.Element(type, styleClass, id);
	}

	public static Select.Element element(String styleClass) {
		return new Select.Element(styleClass, null);
	}

	public static Select.Element element(String styleClass, String id) {
		return new Select.Element(styleClass, id);
	}

	public static Select.Element graph() {
		return new Select.Element(ElementType.graph);
	}

	public static Select.Element graph(String styleClass) {
		return new Select.Element(ElementType.graph, styleClass, null);
	}

	public static Select.Element graph(String styleClass, String id) {
		return new Select.Element(ElementType.graph, styleClass, id);
	}

	public static Select.Instance instance(IGraphElement x) {
		return new Select.Instance(x);
	}

	public static Select.NullSelector nullSelector() {
		return new Select.NullSelector();
	}

	public static Select.ParentSelector parent(Selector selector) {
		return new Select.ParentSelector(selector);
	}

	public static Select.Element table() {
		return new Select.Element(ElementType.table);
	}

	public static Select.Element table(String styleClass) {
		return new Select.Element(ElementType.table, styleClass, null);
	}

	public static Select.Element table(String styleClass, String id) {
		return new Select.Element(ElementType.table, styleClass, id);
	}

	public static Select.Element vertex() {
		return new Select.Element(ElementType.vertex);
	}

	public static Select.Element vertex(String styleClass) {
		return new Select.Element(ElementType.vertex, styleClass, null);
	}

	public static Select.Element vertex(String styleClass, String id) {
		return new Select.Element(ElementType.vertex, styleClass, id);
	}

}
