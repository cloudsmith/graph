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

import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import org.cloudsmith.graph.IGraphElement;
import org.cloudsmith.graph.style.IStyle;
import org.cloudsmith.graph.style.StyleType;

import com.google.common.collect.ImmutableMap;

/**
 * A set of styles.
 * This implementation is not thread safe.
 * 
 */
public class StyleSet {
	public static class ImmutableStyleSet extends StyleSet {

		public ImmutableStyleSet(StyleSet s) {
			this.styleMap = ImmutableMap.copyOf(s.styleMap);
		}

	}

	public static ImmutableStyleSet withImmutableStyle(IStyle<?> style) {
		return new ImmutableStyleSet(withStyles(style));
	}

	public static ImmutableStyleSet withImmutableStyles(IStyle<?>... styles) {
		return new ImmutableStyleSet(withStyles(styles));
	}

	public static StyleSet withStyle(IStyle<?> style) {
		return withStyles(style);
	}

	/**
	 * Factory method.
	 * 
	 * @param styles
	 * @return
	 */
	public static StyleSet withStyles(IStyle<?>... styles) {
		StyleSet styleMap = new StyleSet();
		for(IStyle<?> s : styles)
			styleMap.put(s);
		return styleMap;
	}

	// protected Map<Class<?>, StyleBase<? extends Object>> styleMap;
	protected Map<StyleType, IStyle<? extends Object>> styleMap;

	public StyleSet() {
	}

	/**
	 * Add all style settings from map into this map - overwrite existing values.
	 * 
	 * @param map
	 */
	public void add(StyleSet map) {
		if(styleMap == null)
			styleMap = new EnumMap<StyleType, IStyle<? extends Object>>(StyleType.class);
		styleMap.putAll(map.styleMap);
	}

	public Collection<IStyle<? extends Object>> getStyles() {
		if(styleMap == null)
			return Collections.emptyList();
		return styleMap.values();
	}

	/**
	 * Gets the value of a particular style, or null if not set. See the respective style type (doc) for
	 * information about returned type.
	 * 
	 * @param x
	 * @return
	 */
	public Object getStyleValue(StyleType x, IGraphElement ge) {
		if(styleMap == null)
			return null;
		return styleMap.get(x).getValue(ge);
	}

	/**
	 * Put a style in the map - overwrite any existing entry for this style.
	 * 
	 * @param style
	 */
	public void put(IStyle<? extends Object> style) {
		if(styleMap == null)
			styleMap = new EnumMap<StyleType, IStyle<? extends Object>>(StyleType.class);
		styleMap.put(style.getStyleType(), style);
	}
}
