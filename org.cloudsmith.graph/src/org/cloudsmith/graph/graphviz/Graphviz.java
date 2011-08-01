/**
 * Copyright (c) 2006-2011 Cloudsmith Inc. and other contributors, as listed below.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Cloudsmith
 * 
 */
package org.cloudsmith.graph.graphviz;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.log4j.Logger;
import org.cloudsmith.graph.ICancel;
import org.cloudsmith.graph.IRootGraph;
import org.cloudsmith.graph.dot.DotRenderer;
import org.cloudsmith.graph.graphcss.GraphCSS;

import com.google.inject.Inject;

/**
 * A Graphviz runner.
 * 
 * The Graphviz runner executes one of the graphviz executables to produce
 * one of: a PNG, JPG, SVG, or a HTML USEMAP.
 * TODO: possibly extend output to also include PS (this is
 * just a different "-Ttype", but may require attention to handling of fonts).
 * 
 * This graphviz runner assumes that the layout algorithms are available on the
 * execution path of the jvm (i.e. the executables with the same name as the values of
 * the {@link GraphvizLayout}).
 * 
 * TODO: ideally, the set of available types:renderers should be discovered at runtime from
 * the environment, and then bound in the runtime guice module.
 * 
 */
public class Graphviz implements IGraphviz {
	private IGraphvizConfig config;

	private DotRenderer dotRenderer;

	@Inject
	public Graphviz(IGraphvizConfig config, DotRenderer dotRenderer) {
		this.config = config;
		this.dotRenderer = dotRenderer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.impl.dot.IGraphviz#getDotText(org.cloudsmith.graph.IGraph, org.cloudsmith.graph.impl.style.RuleSet)
	 */
	@Override
	public String getDotText(final ICancel cancel, IRootGraph graph, GraphCSS defaultCSS, GraphCSS... gCSS) {
		ByteArrayOutputStream bufferStream = new ByteArrayOutputStream();
		dotRenderer.write(cancel, bufferStream, graph, defaultCSS, gCSS);
		return bufferStream.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.impl.dot.IGraphviz#getUsemap(org.cloudsmith.graph.IGraph, org.cloudsmith.graph.impl.style.RuleSet,
	 * org.cloudsmith.graph.impl.dot.Graphviz.Layout)
	 */
	@Override
	public String getUsemap(final ICancel cancel, GraphvizLayout layout, IRootGraph graph, GraphCSS defaultStyle,
			GraphCSS... styleSheets) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		if(writeGraphvizOutput(cancel, stream, GraphvizFormat.cmapx, null, layout, graph, defaultStyle, styleSheets) == null)
			return "";
		return stream.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.impl.dot.IGraphviz#getJpgImage(org.cloudsmith.graph.IGraph, org.cloudsmith.graph.impl.style.RuleSet,
	 * org.cloudsmith.graph.impl.dot.Graphviz.Layout)
	 */
	@Override
	public byte[] toJPG(final ICancel cancel, GraphvizLayout layout, IRootGraph graph, GraphCSS defaultStyleSheet,
			GraphCSS... styleSheets) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		if(writeGraphvizOutput(
			cancel, stream, GraphvizFormat.jpg, config.getRenderer(), layout, graph, defaultStyleSheet, styleSheets) == null)
			return null;
		byte[] ret = stream.toByteArray();
		return (ret == null || ret.length < 1)
				? null
				: ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.impl.dot.IGraphviz#getPngImage(org.cloudsmith.graph.IGraph, org.cloudsmith.graph.impl.style.RuleSet,
	 * org.cloudsmith.graph.impl.dot.Graphviz.Layout)
	 */
	@Override
	public byte[] toPNG(final ICancel cancel, GraphvizLayout layout, IRootGraph graph, GraphCSS defaultStyle,
			GraphCSS... styleSheets) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		if(writeGraphvizOutput(
			cancel, stream, GraphvizFormat.png, config.getRenderer(), layout, graph, defaultStyle, styleSheets) == null)
			return null;
		byte[] ret = stream.toByteArray();
		return (ret == null || ret.length < 1)
				? null
				: ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.impl.dot.IGraphviz#getSvgImage(org.cloudsmith.graph.IGraph, org.cloudsmith.graph.impl.style.RuleSet,
	 * org.cloudsmith.graph.impl.dot.Graphviz.Layout)
	 */
	@Override
	public String toSVG(final ICancel cancel, GraphvizLayout layout, IRootGraph graph, GraphCSS defaultStyle,
			GraphCSS... styleSheets) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		if(writeGraphvizOutput(
			cancel, stream, GraphvizFormat.svg, config.getRenderer(), layout, graph, defaultStyle, styleSheets) == null)
			return null;
		return stream.toString();
	}

	@Override
	public OutputStream writeGraphvizOutput(final ICancel cancel, final OutputStream output, GraphvizFormat format,
			GraphvizRenderer renderer, //
			GraphvizLayout layout, //
			final byte[] dotData //
	) {
		Process p;
		// graphviz -T format:renderer is something like -T png:cairo
		// Construct renderer string (':renderer' after the format) if renderer is specified - generally
		// a bad idea as you need to know what renderers are available.
		//
		String r = (renderer == null || renderer == GraphvizRenderer.standard)
				? ""
				: ":" + config.getRenderer().toString();
		try {

			p = Runtime.getRuntime().exec(layout.toString() + " -T" + format.toString() + r);
		}
		catch(IOException e1) {
			Logger log = Logger.getLogger(Graphviz.class);
			log.error("Could not execute " + layout.toString() + " -T" + format.toString() + r);
			// TODO: need to close the process streams or they may remain open
			return null;
		}
		final OutputStream out = new BufferedOutputStream(p.getOutputStream());
		final InputStream in = new BufferedInputStream(p.getInputStream());
		final InputStream err = new BufferedInputStream(p.getErrorStream());

		// use the stream connected to the command's stdin
		/*
		 * A thread is needed when producing output to graphviz. If there is something
		 * wrong graphviz will not read the input and the writer will block.
		 */
		Thread writer = new Thread() {
			@Override
			public void run() {
				// print the dot output on the stream
				try {
					out.write(dotData); // dotOutput.toByteArray());
					// close the stream, or graphviz will read for ever
					out.close();
				}
				catch(IOException e) {
					Logger log = Logger.getLogger(Graphviz.class);
					log.error("error closing output stream to graphviz ", e);
				}
			}
		};

		writer.start();

		class ReaderThread extends Thread {
			public boolean done = false;

			@Override
			public void run() {
				byte[] buffer = new byte[512];

				try {
					int read = in.read(buffer);
					while(read != -1) {
						try {
							output.write(buffer, 0, read);
						}
						catch(Throwable e) {
							Logger log = Logger.getLogger(Graphviz.class);
							log.error("Exception while writing result read from graphviz", e);
						}
						cancel.assertContinue();
						read = in.read(buffer);
					}
					// close the input - we are finished
					in.close();
					// flag that we are done reading in a normal way.
					done = true;
				}
				catch(IOException e) {
					Logger log = Logger.getLogger(Graphviz.class);
					log.error("error reading output from graphviz ", e);
				}
			}
		}
		;
		ReaderThread reader = new ReaderThread();
		// start reading the output from graphviz
		reader.start();

		final ByteArrayOutputStream eout = new ByteArrayOutputStream();
		Thread errorHandler = new Thread() {
			@Override
			public void run() {
				byte[] buffer = new byte[512];
				try {
					int read = err.read(buffer);
					while(read != -1) {
						eout.write(buffer, 0, read);
						read = err.read(buffer);
					}

					Logger log = Logger.getLogger(Graphviz.class);
					// if there was no output this could be because EOF was reached due to normal end.
					String tmp = eout.toString();
					if(tmp != null && tmp.length() > 0)
						log.error("Graphviz error: " + tmp);
				}
				catch(IOException e) {
					// an IO Exception here is the expected outcome
					// we are reading stderr, and there should be no output to read
					// so this thread should (in the normal case) just hang on the read,
					// and then be interrupted when the stderr is closed as part of normal
					// processing.
				}
			}
		};
		errorHandler.start();
		try {
			cancel.assertContinue();
			// wait for process to finish
			p.waitFor();
			// wait until everything has been read from process
			cancel.assertContinue();
			reader.join();

			cancel.assertContinue();

			// TODO: it may be needed to check the error output, if it is an error or a warning
			// warnings could be ignored - now they also terminate the output if the warning occurs before
			// the writer is done.
			if(p.exitValue() != 0) {
				throw new GraphvizException(eout.toString());
			}

		}
		catch(InterruptedException e) {
			Logger log = Logger.getLogger(Graphviz.class);
			log.error("Graphviz reading interupted");
			return null;
		}
		finally {
			// close ALL input
			// This terminates the reader and writer as the pipe is forcefully
			// closed on them - errors may occur after input has already been
			// closed or after output has been closed - make sure all three
			// are closed.
			try {
				in.close();
			}
			catch(IOException ioe) {
			}
			try {
				out.close();
			}
			catch(IOException ioe) {
			}
			try {
				err.close();
			}
			catch(IOException ioe) {
			}

		}
		if(!reader.done)
			return null;
		return output;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.impl.dot.IGraphviz#getGraphvizOutput(java.io.OutputStream, org.cloudsmith.graph.impl.dot.Graphviz.Format,
	 * org.cloudsmith.graph.impl.dot.Graphviz.Renderer, org.cloudsmith.graph.IGraph, org.cloudsmith.graph.impl.style.RuleSet,
	 * org.cloudsmith.graph.impl.dot.Graphviz.Layout)
	 */
	@Override
	public OutputStream writeGraphvizOutput(ICancel cancel, OutputStream output, GraphvizFormat format,
			GraphvizRenderer renderer, GraphvizLayout layout, IRootGraph graph, GraphCSS defaultStyleSheet,
			GraphCSS... styleSheets) {
		// Produce the dot output to a buffer (at one point we could not run this in a thread because JBoss Seam
		// got confused over context - maybe possible to revisit
		//
		final ByteArrayOutputStream dotOutput = new ByteArrayOutputStream();
		dotRenderer.write(cancel, dotOutput, graph, defaultStyleSheet, styleSheets);
		return writeGraphvizOutput(cancel, output, format, renderer, layout, dotOutput.toByteArray());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.graphviz.IGraphviz#writePNG(java.io.OutputStream, org.cloudsmith.graph.graphviz.GraphvizLayout,
	 * org.cloudsmith.graph.IRootGraph, org.cloudsmith.graph.graphcss.GraphCSS, org.cloudsmith.graph.graphcss.GraphCSS[])
	 */
	@Override
	public boolean writePNG(ICancel cancel, OutputStream output, GraphvizLayout layout, IRootGraph graph,
			GraphCSS defaultStyle, GraphCSS... styleSheets) {
		if(writeGraphvizOutput(
			cancel, output, GraphvizFormat.png, config.getRenderer(), layout, graph, defaultStyle, styleSheets) == null)
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.impl.dot.IGraphviz#getSvgImage(java.io.OutputStream, org.cloudsmith.graph.IGraph,
	 * org.cloudsmith.graph.impl.style.RuleSet, org.cloudsmith.graph.impl.dot.Graphviz.Layout)
	 */
	@Override
	public boolean writeSVG(ICancel cancel, OutputStream output, GraphvizLayout layout, IRootGraph graph,
			GraphCSS defaultStyle, GraphCSS... styleSheets) {
		if(writeGraphvizOutput(
			cancel, output, GraphvizFormat.svg, config.getRenderer(), layout, graph, defaultStyle, styleSheets) == null)
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.impl.dot.IGraphviz#getSvgzImage(java.io.OutputStream, org.cloudsmith.graph.IGraph,
	 * org.cloudsmith.graph.impl.style.RuleSet, org.cloudsmith.graph.impl.dot.Graphviz.Layout)
	 */
	@Override
	public boolean writeSVGZ(ICancel cancel, OutputStream output, GraphvizLayout layout, IRootGraph graph,
			GraphCSS defaultStyle, GraphCSS... styleSheets) {
		try {
			GZIPOutputStream stream = new GZIPOutputStream(output);
			if(writeGraphvizOutput(
				cancel, stream, GraphvizFormat.svg, config.getRenderer(), layout, graph, defaultStyle, styleSheets) == null)
				return false;
			stream.finish();
			stream.flush();
		}
		catch(IOException e) {
			return false;
		}
		return true;
	}
}
