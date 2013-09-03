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
package com.puppetlabs.graph.tests;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.puppetlabs.geppetto.pp.AssignmentExpression;
import com.puppetlabs.geppetto.pp.Expression;
import com.puppetlabs.geppetto.pp.PPFactory;
import com.puppetlabs.geppetto.pp.PuppetManifest;
import com.puppetlabs.geppetto.pp.SingleQuotedString;
import com.puppetlabs.geppetto.pp.VariableExpression;
import com.puppetlabs.graph.ICancel;
import com.puppetlabs.graph.IGraphProvider;
import com.puppetlabs.graph.IRootGraph;
import com.puppetlabs.graph.dot.DotRenderer;
import com.puppetlabs.graph.emf.DefaultEGraphModule;
import com.puppetlabs.graph.emf.HorizontalArrayListEGraphProvider;
import com.puppetlabs.graph.graphcss.GraphCSS;
import com.puppetlabs.graph.graphviz.GraphvizLayout;
import com.puppetlabs.graph.graphviz.IGraphviz;
import com.puppetlabs.graph.style.themes.IStyleTheme;
import org.eclipse.emf.common.util.EList;
import org.junit.Test;

import com.google.inject.Module;

/**
 * Tests EGraph
 * 
 */
public class TestEGraph extends AbstractGraphTests {

	private AssignmentExpression getAssignmentExpression(String varName, String value) {
		final PPFactory pf = PPFactory.eINSTANCE;

		AssignmentExpression assign = pf.createAssignmentExpression();
		VariableExpression varX = pf.createVariableExpression();
		varX.setVarName(varName);
		assign.setLeftExpr(varX);
		SingleQuotedString aString = pf.createSingleQuotedString();
		aString.setText(value);
		assign.setRightExpr(aString);
		return assign;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.puppetlabs.graph.tests.AbstractGraphTests#getModule()
	 */
	@Override
	protected Module getModule() {
		return new DefaultEGraphModule();
	}

	@Test
	public void test_smokeTest() throws IOException {
		// Need a model
		// $x = "Hello Graph World"
		final PPFactory pf = PPFactory.eINSTANCE;
		PuppetManifest manifest = pf.createPuppetManifest();
		EList<Expression> statements = manifest.getStatements();
		AssignmentExpression assign = getAssignmentExpression("$x", "Hello EGraph World!");
		statements.add(assign);
		AssignmentExpression assign2 = getAssignmentExpression("$y", "Goodbye EGraph World!");
		statements.add(assign2);

		// Render it
		IGraphviz graphviz = get(IGraphviz.class);
		GraphCSS themeSheet = get(GraphCSS.class);

		IGraphProvider graphProvider = get(IGraphProvider.class);
		IRootGraph testGraph = graphProvider.computeGraph(manifest);

		IStyleTheme theme = get(IStyleTheme.class);
		// append the theme's styles with those from the provider
		themeSheet.addAll(theme.getInstanceRules());
		themeSheet.addAll(graphProvider.getRules());

		// IStyleFactory styles = get(IStyleFactory.class);
		// themeSheet.addRule(Select.graph("RootGraph").withStyle(styles.backgroundColor("#cccccc")));

		File output = getTestOutputFolder("output", true);
		FileOutputStream tmp = new FileOutputStream(new File(output, "e_smokeTest.png"));

		// Render without the default styles. Use styles from SimpleGraph1
		FileOutputStream dot = new FileOutputStream(new File(output, "e_smokeTest.dot"));
		DotRenderer dotRenderer = get(DotRenderer.class);
		dotRenderer.write(ICancel.NullIndicator, dot, testGraph, theme.getDefaultRules(), themeSheet);

		// Render without the default styles. Use styles from SimpleGraph1
		assertTrue("Writing PNG", graphviz.writePNG(
			ICancel.NullIndicator, tmp, GraphvizLayout.dot, testGraph, theme.getDefaultRules(), themeSheet));

	}

	@Test
	public void test_verticalIndex() throws IOException {
		// Need a model
		// $x = "Hello Graph World"
		final PPFactory pf = PPFactory.eINSTANCE;
		PuppetManifest manifest = pf.createPuppetManifest();
		EList<Expression> statements = manifest.getStatements();
		AssignmentExpression assign = getAssignmentExpression("$x", "Hello EGraph World!");
		statements.add(assign);
		AssignmentExpression assign2 = getAssignmentExpression("$y", "Goodbye EGraph World!");
		statements.add(assign2);

		// Render it
		IGraphviz graphviz = get(IGraphviz.class);
		GraphCSS themeSheet = get(GraphCSS.class);

		IGraphProvider graphProvider = get(HorizontalArrayListEGraphProvider.class);
		IRootGraph testGraph = graphProvider.computeGraph(manifest);

		IStyleTheme theme = get(IStyleTheme.class);
		// append the theme's styles with those from the provider
		themeSheet.addAll(theme.getInstanceRules());
		themeSheet.addAll(graphProvider.getRules());

		// IStyleFactory styles = get(IStyleFactory.class);
		// themeSheet.addRule(Select.graph("RootGraph").withStyle(styles.backgroundColor("#cccccc")));

		File output = getTestOutputFolder("output", true);
		FileOutputStream tmp = new FileOutputStream(new File(output, "e_horizontalArrayIndex.png"));

		// Render without the default styles. Use styles from SimpleGraph1
		FileOutputStream dot = new FileOutputStream(new File(output, "e_horizontalArrayIndex.png.dot"));
		DotRenderer dotRenderer = get(DotRenderer.class);
		dotRenderer.write(ICancel.NullIndicator, dot, testGraph, theme.getDefaultRules(), themeSheet);

		// Render without the default styles. Use styles from SimpleGraph1
		assertTrue("Writing PNG", graphviz.writePNG(
			ICancel.NullIndicator, tmp, GraphvizLayout.dot, testGraph, theme.getDefaultRules(), themeSheet));

	}
}
