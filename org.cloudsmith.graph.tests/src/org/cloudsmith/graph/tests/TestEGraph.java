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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.cloudsmith.geppetto.pp.AssignmentExpression;
import org.cloudsmith.geppetto.pp.Expression;
import org.cloudsmith.geppetto.pp.PPFactory;
import org.cloudsmith.geppetto.pp.PuppetManifest;
import org.cloudsmith.geppetto.pp.SingleQuotedString;
import org.cloudsmith.geppetto.pp.VariableExpression;
import org.cloudsmith.graph.ICancel;
import org.cloudsmith.graph.IGraphProvider;
import org.cloudsmith.graph.IRootGraph;
import org.cloudsmith.graph.dot.DotRenderer;
import org.cloudsmith.graph.emf.DefaultEGraphModule;
import org.cloudsmith.graph.emf.HorizontalArrayListEGraphProvider;
import org.cloudsmith.graph.graphcss.GraphCSS;
import org.cloudsmith.graph.graphviz.GraphvizLayout;
import org.cloudsmith.graph.graphviz.IGraphviz;
import org.cloudsmith.graph.style.themes.IStyleTheme;
import org.eclipse.emf.common.util.EList;

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
	 * @see org.cloudsmith.graph.tests.AbstractGraphTests#getModule()
	 */
	@Override
	protected Module getModule() {
		return new DefaultEGraphModule();
	}

	public void test_smokeTest() throws FileNotFoundException {
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

		FileOutputStream tmp = new FileOutputStream(new File("./output/e_smokeTest.png"));

		// Render without the default styles. Use styles from SimpleGraph1
		FileOutputStream dot = new FileOutputStream(new File("./output/e_smokeTest.dot"));
		DotRenderer dotRenderer = get(DotRenderer.class);
		dotRenderer.write(ICancel.NullIndicator, dot, testGraph, theme.getDefaultRules(), themeSheet);

		// Render without the default styles. Use styles from SimpleGraph1
		assertTrue("Writing PNG", graphviz.writePNG(
			ICancel.NullIndicator, tmp, GraphvizLayout.dot, testGraph, theme.getDefaultRules(), themeSheet));

	}

	public void test_verticalIndex() throws FileNotFoundException {
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

		FileOutputStream tmp = new FileOutputStream(new File("./output/e_horizontalArrayIndex.png"));

		// Render without the default styles. Use styles from SimpleGraph1
		FileOutputStream dot = new FileOutputStream(new File("./output/e_horizontalArrayIndex.png.dot"));
		DotRenderer dotRenderer = get(DotRenderer.class);
		dotRenderer.write(ICancel.NullIndicator, dot, testGraph, theme.getDefaultRules(), themeSheet);

		// Render without the default styles. Use styles from SimpleGraph1
		assertTrue("Writing PNG", graphviz.writePNG(
			ICancel.NullIndicator, tmp, GraphvizLayout.dot, testGraph, theme.getDefaultRules(), themeSheet));

	}
}
