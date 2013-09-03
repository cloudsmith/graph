/**
 * Copyright (c) 2006-2011 Cloudsmith Inc. and other contributors, as listed below.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   AT&T - initial API
 *   Cloudsmith
 * 
 */
package org.cloudsmith.graph.style;

/**
 * An Arrow consists of either a single ArrowType (there are 36), or is a sequence of 2-4 ArrowTypes.
 * When using a sequence, the first given arrow type is rendered closest to the shape it is attached to.
 * 
 */
public class Arrow {

	/**
	 * The 36 basic arrow types
	 */
	public enum ArrowType {
		box, lbox, rbox, obox, olbox, orbox, crow, lcrow, rcrow, diamond, ldiamond, rdiamond, odiamond, oldiamond, ordiamond, dot, odot, inv, linv, rinv, oinv, olinv, orinv, none, normal, lnormal, rnormal, onormal, olnormal, ornormal, tee, ltee, rtee, vee, lvee, rvee,
	}

	public static final Arrow box = new Arrow(ArrowType.box);

	public static final Arrow lbox = new Arrow(ArrowType.lbox);

	public static final Arrow rbox = new Arrow(ArrowType.rbox);

	public static final Arrow obox = new Arrow(ArrowType.obox);

	public static final Arrow olbox = new Arrow(ArrowType.olbox);

	public static final Arrow orbox = new Arrow(ArrowType.orbox);

	public static final Arrow crow = new Arrow(ArrowType.crow);

	public static final Arrow lcrow = new Arrow(ArrowType.lcrow);

	public static final Arrow rcrow = new Arrow(ArrowType.rcrow);

	public static final Arrow diamond = new Arrow(ArrowType.diamond);

	public static final Arrow ldiamond = new Arrow(ArrowType.ldiamond);

	public static final Arrow rdiamond = new Arrow(ArrowType.rdiamond);

	public static final Arrow odiamond = new Arrow(ArrowType.odiamond);

	public static final Arrow oldiamond = new Arrow(ArrowType.oldiamond);

	public static final Arrow ordiamond = new Arrow(ArrowType.ordiamond);

	public static final Arrow dot = new Arrow(ArrowType.dot);

	public static final Arrow odot = new Arrow(ArrowType.odot);

	public static final Arrow inv = new Arrow(ArrowType.inv);

	public static final Arrow linv = new Arrow(ArrowType.linv);

	public static final Arrow rinv = new Arrow(ArrowType.rinv);

	public static final Arrow oinv = new Arrow(ArrowType.oinv);

	public static final Arrow olinv = new Arrow(ArrowType.olinv);

	public static final Arrow orinv = new Arrow(ArrowType.orinv);

	public static final Arrow none = new Arrow(ArrowType.none);

	public static final Arrow normal = new Arrow(ArrowType.normal);

	public static final Arrow lnormal = new Arrow(ArrowType.lnormal);

	public static final Arrow rnormal = new Arrow(ArrowType.rnormal);

	public static final Arrow onormal = new Arrow(ArrowType.onormal);

	public static final Arrow olnormal = new Arrow(ArrowType.olnormal);

	public static final Arrow ornormal = new Arrow(ArrowType.ornormal);

	public static final Arrow tee = new Arrow(ArrowType.tee);

	public static final Arrow ltee = new Arrow(ArrowType.ltee);

	public static final Arrow rtee = new Arrow(ArrowType.rtee);

	public static final Arrow vee = new Arrow(ArrowType.vee);

	public static final Arrow lvee = new Arrow(ArrowType.lvee);

	public static final Arrow rvee = new Arrow(ArrowType.rvee);

	/** dual arrowhead - inv and filled dot (i.e. a 0< symbol) */
	public static final Arrow invdot = new Arrow(ArrowType.inv, ArrowType.dot);

	/** dual arrowhead - inv and hollow dot (i.e. a O< symbol) */
	public static final Arrow invodot = new Arrow(ArrowType.inv, ArrowType.odot);

	/** dual arrowhead - tee and filled dot (i.e. a 0| symbol) */
	public static final Arrow teedot = new Arrow(ArrowType.tee, ArrowType.dot);

	/** dual arrowhead - tee and hollow dot (i.e. a 0| symbol) */
	public static final Arrow teeodot = new Arrow(ArrowType.tee, ArrowType.odot);

	/** dual arrowhead - tee and tee (i.e. a || symbol) */
	public static final Arrow teetee = new Arrow(ArrowType.tee, ArrowType.tee);

	/** dual arrow head crow and filled dot (i.e. 0M type symbol) */
	public static final Arrow crowdot = new Arrow(ArrowType.crow, ArrowType.dot);

	/** dual arrow head crow and hollow dot (i.e. 0M type symbol) */
	public static final Arrow crowodot = new Arrow(ArrowType.crow, ArrowType.odot);

	/** dual arrow head crow and tee (i.e. |M type symbol) */
	public static final Arrow crowtee = new Arrow(ArrowType.crow, ArrowType.tee);

	/** dual arrow head with two normal arrows (i.e. a "fast forward" type symbol) */
	public static final Arrow normalnormal = new Arrow(ArrowType.normal, ArrowType.normal);

	/** dual arrow head with two stick arrows */
	public static final Arrow veevee = new Arrow(ArrowType.vee, ArrowType.vee);

	private ArrowType arrows[];

	private String stringValue;

	public Arrow(ArrowType type) {
		arrows = new ArrowType[] { type };
	}

	public Arrow(ArrowType... type) {
		if(type.length == 0 || type.length > 4)
			throw new IllegalArgumentException("Number of types should be 1-4. Was: " + type.length);
		arrows = type;
	}

	public String getValue() {
		if(stringValue != null)
			return stringValue;
		StringBuffer buf = new StringBuffer();
		for(ArrowType a : arrows)
			buf.append(a);
		stringValue = buf.toString();
		return stringValue;
	}

	@Override
	public String toString() {
		return getValue();
	}
}
