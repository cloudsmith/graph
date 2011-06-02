/*******************************************************************
 * Copyright (c) 2006-2007, Cloudsmith Inc.
 * The code, documentation and other materials contained herein
 * are the sole and exclusive property of Cloudsmith Inc. and may
 * not be disclosed, used, modified, copied or distributed without
 * prior written consent or license from Cloudsmith Inc.
 ******************************************************************/

package org.cloudsmith.graph.servlets;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.AccessControlException;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This is a sample servlet that delivers graphs using SVGZ (compressed SVG).
 * The interface to the graph production factory is just an illustration as it depends on the
 * application.
 *
 */
public class SvgzServlet extends HttpServlet
{

	private static final long serialVersionUID = 8759810144923079498L;

	/**
	 * Extra path information after the servlet path. This string needs to be changed if
	 * the servlet is mapped differently. 
	 */
	private static final String SVGZ_GRAPH = "/graph";
	private static final String SVGZ_SVGZ_GRAPH = "/svgz/graph";

	private enum SvgzKind { GRAPH, TBD1 };
	
	/**
	 * Looks up the no_image.png and reads it into a buffer that is kept in the servlet.
	 */
	public SvgzServlet()
	{
		System.err.print("SVGZServlet started");
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		SvgzKind kind = null;
		String pathInfo = request.getPathInfo();
		// check for both /types of paths in the string depending on servlet mapping
		if(SVGZ_GRAPH.equals(pathInfo) || SVGZ_SVGZ_GRAPH.equals(pathInfo))
			kind = SvgzKind.GRAPH;

		if(kind != null )
		{
			IGraphFactorySample graphFactory = null; // Naturally it needs to be obtained from somewhere

			String oid = request.getParameter("oid");
			response.setContentType("image/svg+xml");
			response.setHeader("Content-Encoding", "gzip");
			try
			{
				ByteArrayOutputStream result = new ByteArrayOutputStream();
				if(!graphFactory.getGraphSvg(oid, result))
				{
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					return;
				}
				ByteArrayOutputStream buf = new ByteArrayOutputStream();
				GZIPOutputStream compressed = new GZIPOutputStream(buf);
				byte[] data = result.toString("UTF-8").getBytes("UTF-8");
				compressed.write(data);
				compressed.finish();
				compressed.flush();
				data = buf.toByteArray();
				response.setContentLength(data.length);
				response.getOutputStream().write(data);
			}
			catch(AccessControlException e)
			{
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return;
			}
			catch(FileNotFoundException e)
			{
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
			catch(IllegalArgumentException e)
			{
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				return;				
			}
			response.setStatus(HttpServletResponse.SC_OK);
			response.getOutputStream().flush();
		}
	}
}
