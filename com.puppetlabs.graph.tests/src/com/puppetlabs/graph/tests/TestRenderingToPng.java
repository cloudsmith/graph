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

import com.puppetlabs.graph.ICancel;
import com.puppetlabs.graph.IGraphProvider;
import com.puppetlabs.graph.IRootGraph;
import com.puppetlabs.graph.dot.DotRenderer;
import com.puppetlabs.graph.graphcss.GraphCSS;
import com.puppetlabs.graph.graphcss.Select;
import com.puppetlabs.graph.graphviz.GraphvizLayout;
import com.puppetlabs.graph.graphviz.IGraphviz;
import com.puppetlabs.graph.style.IStyleFactory;
import com.puppetlabs.graph.style.RankDirection;
import com.puppetlabs.graph.style.themes.IStyleTheme;
import com.puppetlabs.graph.testgraphs.SimpleGraph1;
import com.puppetlabs.graph.testgraphs.SimpleGraph2;
import org.junit.Test;

/**
 * Tests rendering to PNG. Manual inspection of result is required.
 * 
 */
public class TestRenderingToPng extends AbstractGraphTests {

	@Test
	public void testPNG_abc_abc_vertical_default() throws IOException {
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

		File output = getTestOutputFolder("output", true);
		FileOutputStream tmp = new FileOutputStream(new File(output, "abc_abc_vertical_default.png"));

		// Render without the default styles. Use styles from SimpleGraph1
		FileOutputStream dot = new FileOutputStream(new File(output, "abc_abc_vertical_default.dot"));
		DotRenderer dotRenderer = get(DotRenderer.class);
		dotRenderer.write(ICancel.NullIndicator, dot, testGraph, theme.getDefaultRules(), themeSheet);

		// Render without the default styles. Use styles from SimpleGraph1
		assertTrue("Writing PNG", graphviz.writePNG(
			ICancel.NullIndicator, tmp, GraphvizLayout.dot, testGraph, theme.getDefaultRules(), themeSheet));

	}

	@Test
	public void testPNG_abc_horizontal_default() throws IOException {
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

		File output = getTestOutputFolder("output", true);
		FileOutputStream png = new FileOutputStream(new File(output, "abc_horizontal_default.png"));

		// Render without the default styles. Use styles from SimpleGraph1
		FileOutputStream dot = new FileOutputStream(new File(output, "abc_horizontal_default.dot"));
		DotRenderer dotRenderer = get(DotRenderer.class);
		dotRenderer.write(ICancel.NullIndicator, dot, testGraph, theme.getDefaultRules(), themeSheet);

		// Render without the default styles. Use styles from SimpleGraph1
		assertTrue("Wriging PNG", graphviz.writePNG(
			ICancel.NullIndicator, png, GraphvizLayout.dot, testGraph, theme.getDefaultRules(), themeSheet));

	}

	@Test
	public void testPNG_abc_vertical_default() throws IOException {
		IGraphviz graphviz = get(IGraphviz.class);
		GraphCSS themeSheet = get(GraphCSS.class);

		IGraphProvider graphProvider = get(SimpleGraph1.class);
		IRootGraph testGraph = graphProvider.computeGraph();

		IStyleTheme theme = get(IStyleTheme.class);
		// append the theme's styles with those from the provider
		themeSheet.addAll(theme.getInstanceRules());
		themeSheet.addAll(graphProvider.getRules());

		File output = getTestOutputFolder("output", true);
		FileOutputStream tmp = new FileOutputStream(new File(output, "abc_vertical_default.png"));

		// Render without the default styles. Use styles from SimpleGraph1
		FileOutputStream dot = new FileOutputStream(new File(output, "abc_vertical_default.dot"));
		DotRenderer dotRenderer = get(DotRenderer.class);
		dotRenderer.write(ICancel.NullIndicator, dot, testGraph, theme.getDefaultRules(), themeSheet);

		// Render without the default styles. Use styles from SimpleGraph1
		assertTrue("Writing PNG", graphviz.writePNG(
			ICancel.NullIndicator, tmp, GraphvizLayout.dot, testGraph, theme.getDefaultRules(), themeSheet));
	}

	@Test
	public void testPNG_abc_vertical_unstyled() throws IOException {
		IGraphviz graphviz = get(IGraphviz.class);
		GraphCSS themeSheet = get(GraphCSS.class);

		IGraphProvider graphProvider = get(SimpleGraph1.class);
		IRootGraph testGraph = graphProvider.computeGraph();
		themeSheet.addAll(graphProvider.getRules());

		File output = getTestOutputFolder("output", true);
		FileOutputStream tmp = new FileOutputStream(new File(output, "abc_vertical_unstyled.png"));

		// Render without the default styles. Use styles from SimpleGraph1
		assertTrue("Writing PNG", graphviz.writePNG(
			ICancel.NullIndicator, tmp, GraphvizLayout.dot, testGraph, get(GraphCSS.class), themeSheet));
	}

}
