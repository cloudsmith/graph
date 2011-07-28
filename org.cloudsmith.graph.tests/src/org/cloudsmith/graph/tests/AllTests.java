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
package org.cloudsmith.graph.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * All Puppet Tests.
 * 
 */
public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getName());
		// $JUnit-BEGIN$
		suite.addTestSuite(TestBasicFeatures.class);
		suite.addTestSuite(TestMockGraph.class);
		suite.addTestSuite(TestRenderingToPng.class);
		suite.addTestSuite(TestEGraph.class);
		suite.addTestSuite(TestCircularByteBuffer.class);
		suite.addTestSuite(TestRuleBasedFilterStream.class);
		// suite.addTestSuite(PPTPManagerTests.class);
		// $JUnit-END$
		return suite;
	}

}
