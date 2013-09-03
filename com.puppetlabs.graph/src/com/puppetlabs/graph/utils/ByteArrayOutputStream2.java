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
package com.puppetlabs.graph.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * A ByteArrayOutputStream that can efficiently changed to a ByteArrayInputStream
 * 
 */
public class ByteArrayOutputStream2 extends ByteArrayOutputStream {

	/**
	 * Steals the buffer in this output stream and gives it to the returned
	 * InputStream. This output stream will be reset after this operation.
	 * 
	 * This operation is not thread safe.
	 * 
	 * @param allowFurtherOutput
	 *            set to true if the output stream should allow further output. Passing false, and then writing
	 *            to the output stream will have unpredictable result.
	 * @return
	 */
	public ByteArrayInputStream toInputStream(boolean allowFurtherOutput) {
		ByteArrayInputStream result = new ByteArrayInputStream(this.buf, 0, this.count);
		if(allowFurtherOutput) {
			buf = new byte[32];
			reset();
		}
		return result;
	}
}
