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
package com.puppetlabs.graph.style.labels;

import com.google.common.base.Function;
import com.puppetlabs.graph.IGraphElement;

public class LabelStringTemplate implements ILabelTemplate {
	private Function<IGraphElement, String> templateString;

	public LabelStringTemplate(Function<IGraphElement, String> templateString) {
		this.templateString = templateString;
	}

	public LabelStringTemplate(final String value) {
		this.templateString = new Function<IGraphElement, String>() {
			public String apply(IGraphElement ge) {
				return value;
			}
		};
	}

	public String getTemplateString(IGraphElement ge) {
		return templateString.apply(ge);
	}
}
