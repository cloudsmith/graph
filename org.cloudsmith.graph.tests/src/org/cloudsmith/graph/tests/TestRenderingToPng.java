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

import org.cloudsmith.graph.ICancel;
import org.cloudsmith.graph.IGraphProvider;
import org.cloudsmith.graph.IRootGraph;
import org.cloudsmith.graph.dot.DotRenderer;
import org.cloudsmith.graph.graphcss.GraphCSS;
import org.cloudsmith.graph.graphcss.Select;
import org.cloudsmith.graph.graphviz.GraphvizLayout;
import org.cloudsmith.graph.graphviz.IGraphviz;
import org.cloudsmith.graph.style.IStyleFactory;
import org.cloudsmith.graph.style.RankDirection;
import org.cloudsmith.graph.style.themes.IStyleTheme;
import org.cloudsmith.graph.testgraphs.SimpleGraph1;
import org.cloudsmith.graph.testgraphs.SimpleGraph2;

/**
 * Tests rendering to PNG. Manual inspection of result is required.
 * 
 */
public class TestRenderingToPng extends AbstractGraphTests {

	public void testPNG_abc_abc_vertical_default() throws FileNotFoundException {
		IGraphviz graphviz = get(IGraphviz.class);
		GraphCSS themeSheet = get(GraphCSS.class);

		IGraphProvider graphProvider = get(SimpleGraph2.class);
		IRootGraph testGraph = graphProvider.computeGraph();

		IStyleTheme theme = get(IStyleTheme.class);
		// append the theme's styles with those from the provider
		themeSheet.addAll(theme.getInstanceRules());
		themeSheet.addAll(graphProvider.getRules());

		// IStyleFactory styles = get(IStyleFactory.class);
		// themeSheet.addRule(Select.graph("RootGraph").withStyle(styles.backgroundColor("#cccccc")));

		FileOutputStream tmp = new FileOutputStream(new File("./output/abc_abc_vertical_default.png"));

		// Render without the default styles. Use styles from SimpleGraph1
		FileOutputStream dot = new FileOutputStream(new File("./output/abc_abc_vertical_default.dot"));
		DotRenderer dotRenderer = get(DotRenderer.class);
		dotRenderer.write(ICancel.NullIndicator, dot, testGraph, theme.getDefaultRules(), themeSheet);

		// Render without the default styles. Use styles from SimpleGraph1
		assertTrue("Writing PNG", graphviz.writePNG(
			ICancel.NullIndicator, tmp, GraphvizLayout.dot, testGraph, theme.getDefaultRules(), themeSheet));

	}

	public void testPNG_abc_horizontal_default() throws FileNotFoundException {
		IGraphviz graphviz = get(IGraphviz.class);
		GraphCSS themeSheet = get(GraphCSS.class);

		IGraphProvider graphProvider = get(SimpleGraph1.class);
		IRootGraph testGraph = graphProvider.computeGraph();

		IStyleTheme theme = get(IStyleTheme.class);
		IStyleFactory styles = get(IStyleFactory.class);
		// append the theme's styles with those from the provider
		themeSheet.addAll(theme.getInstanceRules());
		themeSheet.addAll(graphProvider.getRules());
		themeSheet.addRule(Select.graph().withStyle(styles.rankDirectionStyle(RankDirection.LR)));

		FileOutputStream png = new FileOutputStream(new File("./output/abc_horizontal_default.png"));

		// Render without the default styles. Use styles from SimpleGraph1
		FileOutputStream dot = new FileOutputStream(new File("./output/abc_horizontal_default.dot"));
		DotRenderer dotRenderer = get(DotRenderer.class);
		dotRenderer.write(ICancel.NullIndicator, dot, testGraph, theme.getDefaultRules(), themeSheet);

		// Render without the default styles. Use styles from SimpleGraph1
		assertTrue("Wriging PNG", graphviz.writePNG(
			ICancel.NullIndicator, png, GraphvizLayout.dot, testGraph, theme.getDefaultRules(), themeSheet));

	}

	public void testPNG_abc_vertical_default() throws FileNotFoundException {
		IGraphviz graphviz = get(IGraphviz.class);
		GraphCSS themeSheet = get(GraphCSS.class);

		IGraphProvider graphProvider = get(SimpleGraph1.class);
		IRootGraph testGraph = graphProvider.computeGraph();

		IStyleTheme theme = get(IStyleTheme.class);
		// append the theme's styles with those from the provider
		themeSheet.addAll(theme.getInstanceRules());
		themeSheet.addAll(graphProvider.getRules());

		FileOutputStream tmp = new FileOutputStream(new File("./output/abc_vertical_default.png"));

		// Render without the default styles. Use styles from SimpleGraph1
		FileOutputStream dot = new FileOutputStream(new File("./output/abc_vertical_default.dot"));
		DotRenderer dotRenderer = get(DotRenderer.class);
		dotRenderer.write(ICancel.NullIndicator, dot, testGraph, theme.getDefaultRules(), themeSheet);

		// Render without the default styles. Use styles from SimpleGraph1
		assertTrue("Writing PNG", graphviz.writePNG(
			ICancel.NullIndicator, tmp, GraphvizLayout.dot, testGraph, theme.getDefaultRules(), themeSheet));
	}

	public void testPNG_abc_vertical_unstyled() throws FileNotFoundException {
		IGraphviz graphviz = get(IGraphviz.class);
		GraphCSS themeSheet = get(GraphCSS.class);

		IGraphProvider graphProvider = get(SimpleGraph1.class);
		IRootGraph testGraph = graphProvider.computeGraph();
		themeSheet.addAll(graphProvider.getRules());

		FileOutputStream tmp = new FileOutputStream(new File("./output/abc_vertical_unstyled.png"));

		// Render without the default styles. Use styles from SimpleGraph1
		assertTrue("Writing PNG", graphviz.writePNG(
			ICancel.NullIndicator, tmp, GraphvizLayout.dot, testGraph, get(GraphCSS.class), themeSheet));
	}

}
