/**
 * Copyright (c) 2013 Puppet Labs, Inc. and other contributors, as listed below.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Puppet Labs
 */
package com.puppetlabs.graph.graphcss;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import com.google.common.collect.Iterators;
import com.google.common.collect.Sets;
import com.puppetlabs.graph.ElementType;
import com.puppetlabs.graph.IEdge;
import com.puppetlabs.graph.IGraphElement;
import com.puppetlabs.graph.style.IStyle;

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

		private Set<String> matchClasses;

		private String matchId;

		private int specificity = 0;

		public Element(Collection<String> styleClasses, String id) {
			this(EnumSet.allOf(ElementType.class), styleClasses, id);
		}

		public Element(ElementType t) {
			this(EnumSet.of(t));
		}

		// public Element(ElementType elementType, String styleClass, String id) {
		// this(EnumSet.of(elementType), Sets.newHashSet(styleClass), id);
		// }

		public Element(ElementType elementType, Collection<String> styleClasses, String id) {
			this(EnumSet.of(elementType), styleClasses, id);
		}

		public Element(Set<ElementType> elementType) {
			this(elementType, null, null);
		}

		public Element(Set<ElementType> elementType, Collection<String> styleClass, String id) {
			if(elementType == null) {
				elementType = EnumSet.allOf(ElementType.class);
			}
			this.matchElement = elementType;
			this.matchClasses = Sets.newHashSet();
			if(styleClass != null)
				this.matchClasses.addAll(styleClass);
			// this.matchClass = styleClass != null
			// ? styleClass
			// : "";
			this.matchId = id != null
					? id
					: "";
		}

		@Override
		public boolean equalMatch(Selector selector) {
			if(!(selector instanceof Element))
				return false;
			Element e = (Element) selector;
			if(!matchElement.equals(e.matchElement))
				return false;
			if(!(matchClasses.size() == e.matchClasses.size() && e.matchClasses.containsAll(matchClasses)))
				return false;
			// if(!matchClass.equals(e.matchClass))
			// return false;
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

			specificity = matchClasses.size() * 10;
			// if(matchClass != null && matchClass.length() > 0)
			// specificity += 10;

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

			// i.e styleClass && styleClass && ...
			if(matchClasses.size() > 0 && !element.getStyleClasses().containsAll(matchClasses))
				return false;
			// if(matchClass != null //
			// &&
			// matchClass.length() > 0 //
			// // && !matchClass.equals(element.getStyleClass()) //
			// && !element.hasStyleClass(matchClass) //
			// )
			// return false;
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

	public static class Not extends Selector {
		private Selector selector;

		public Not(Selector selector) {
			this.selector = selector;
		}

		@Override
		public boolean equalMatch(Selector selector) {
			if(!(selector instanceof Not))
				return false;
			Not notSelector = (Not) selector;
			return this.selector.equalMatch(notSelector.selector);
		}

		@Override
		public int getSpecificity() {
			return this.selector.getSpecificity() + 1;
		}

		@Override
		public boolean matches(IGraphElement element) {
			return !this.selector.matches(element);
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
		return new Select.Element(ElementType.ANY, Sets.newHashSet(styleClass), null);
	}

	public static Select.Element any(String styleClass, String id) {
		return new Select.Element(ElementType.ANY, Sets.newHashSet(styleClass), id);
	}

	public static Select.Between between(Select.Selector source, Select.Selector target) {
		return new Select.Between(source, target);
	}

	public static Select.Element cell() {
		return new Select.Element(ElementType.cell);
	}

	public static Select.Element cell(Collection<String> styleClasses) {
		return new Select.Element(ElementType.cell, styleClasses, null);
	}

	public static Select.Element cell(Collection<String> styleClasses, String id) {
		return new Select.Element(ElementType.cell, styleClasses, id);
	}

	public static Select.Element cell(String styleClass) {
		return new Select.Element(ElementType.cell, Collections.singleton(styleClass), null);
	}

	public static Select.Element cell(String styleClass, String id) {
		return new Select.Element(ElementType.cell, Collections.singleton(styleClass), id);
	}

	public static Select.Containment containment(Select.Selector... selectors) {
		return new Select.Containment(selectors);
	}

	public static Select.Element edge() {
		return new Select.Element(ElementType.edge);
	}

	public static Select.Element edge(Collection<String> styleClasses) {
		return new Select.Element(ElementType.edge, styleClasses, null);
	}

	public static Select.Element edge(Collection<String> styleClasses, String id) {
		return new Select.Element(ElementType.edge, styleClasses, id);
	}

	public static Select.Element edge(String styleClass) {
		return new Select.Element(ElementType.edge, Collections.singleton(styleClass), null);
	}

	public static Select.Element edge(String styleClass, String id) {
		return new Select.Element(ElementType.edge, Collections.singleton(styleClass), id);
	}

	public static Select.Element element(Collection<String> styleClasses) {
		return new Select.Element(styleClasses, null);
	}

	public static Select.Element element(Collection<String> styleClasses, String id) {
		return new Select.Element(styleClasses, id);
	}

	public static Select.Element element(ElementType type) {
		return new Select.Element(type);
	}

	public static Select.Element element(ElementType type, Collection<String> styleClasses, String id) {
		return new Select.Element(type, styleClasses, id);
	}

	public static Select.Element element(ElementType type, String styleClass) {
		return new Select.Element(type, Collections.singleton(styleClass), null);
	}

	public static Select.Element element(ElementType type, String styleClass, String id) {
		return new Select.Element(type, Collections.singleton(styleClass), id);
	}

	public static Select.Element element(String styleClass) {
		return new Select.Element(Collections.singleton(styleClass), null);
	}

	public static Select.Element element(String styleClass, String id) {
		return new Select.Element(Collections.singleton(styleClass), id);
	}

	public static Select.Element graph() {
		return new Select.Element(ElementType.graph);
	}

	public static Select.Element graph(Collection<String> styleClasses) {
		return new Select.Element(ElementType.graph, styleClasses, null);
	}

	public static Select.Element graph(Collection<String> styleClasses, String id) {
		return new Select.Element(ElementType.graph, styleClasses, id);
	}

	public static Select.Element graph(String styleClass) {
		return new Select.Element(ElementType.graph, Collections.singleton(styleClass), null);
	}

	public static Select.Element graph(String styleClass, String id) {
		return new Select.Element(ElementType.graph, Collections.singleton(styleClass), id);
	}

	public static Select.Instance instance(IGraphElement x) {
		return new Select.Instance(x);
	}

	public static Select.Not not(Select.Selector selector) {
		return new Select.Not(selector);
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

	public static Select.Element table(Collection<String> styleClasses) {
		return new Select.Element(ElementType.table, styleClasses, null);
	}

	public static Select.Element table(Collection<String> styleClasses, String id) {
		return new Select.Element(ElementType.table, styleClasses, id);
	}

	public static Select.Element table(String styleClass) {
		return new Select.Element(ElementType.table, Collections.singleton(styleClass), null);
	}

	public static Select.Element table(String styleClass, String id) {
		return new Select.Element(ElementType.table, Collections.singleton(styleClass), id);
	}

	public static Select.Element vertex() {
		return new Select.Element(ElementType.vertex);
	}

	public static Select.Element vertex(Collection<String> styleClasses) {
		return new Select.Element(ElementType.vertex, styleClasses, null);
	}

	public static Select.Element vertex(Collection<String> styleClasses, String id) {
		return new Select.Element(ElementType.vertex, styleClasses, id);
	}

	public static Select.Element vertex(String styleClass) {
		return new Select.Element(ElementType.vertex, Collections.singleton(styleClass), null);
	}

	public static Select.Element vertex(String styleClass, String id) {
		return new Select.Element(ElementType.vertex, Collections.singleton(styleClass), id);
	}

}
