/*******************************************************************
 * Copyright (c) 2006-2011, Cloudsmith Inc.
 * The code, documentation and other materials contained herein
 * are the sole and exclusive property of Cloudsmith Inc. and may
 * not be disclosed, used, modified, copied or distributed without
 * prior written consent or license from Cloudsmith Inc.
 ******************************************************************/

package org.cloudsmith.graph.emf;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import org.cloudsmith.graph.ElementType;
import org.cloudsmith.graph.IGraphProvider;
import org.cloudsmith.graph.IRootGraph;
import org.cloudsmith.graph.graphcss.GraphCSS;
import org.cloudsmith.graph.graphcss.Rule;
import org.cloudsmith.graph.graphcss.Select;
import org.cloudsmith.graph.graphcss.StyleSet;
import org.cloudsmith.graph.graphviz.GraphvizLayout;
import org.cloudsmith.graph.graphviz.GraphvizRenderer;
import org.cloudsmith.graph.graphviz.IGraphviz;
import org.cloudsmith.graph.style.Arrow;
import org.cloudsmith.graph.style.Compass;
import org.cloudsmith.graph.style.EdgeRouting;
import org.cloudsmith.graph.style.LineType;
import org.cloudsmith.graph.style.NodeShape;
import org.cloudsmith.graph.style.RankDirection;
import org.cloudsmith.graph.style.StyleFactory;
import org.cloudsmith.graph.utils.ImageUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;

import com.google.inject.Inject;

/**
 * TODO: UNFINISHED !!
 */
public class Grapher {

	private GraphvizLayout m_layout = org.cloudsmith.graph.graphviz.GraphvizLayout.dot;

	private Arrow m_headArrow = Arrow.vee;

	private Arrow m_tailArrow = Arrow.none;

	private Compass m_tailPort = Compass.NONE;

	private Compass m_headPort = Compass.NONE;

	private Double m_arrowScale = new Double(0.5);

	private boolean m_concentrate = false;

	private EdgeRouting m_routing = EdgeRouting.spline;

	private NodeShape m_nodeShape = NodeShape.rectangle;

	private LineType m_nodeLineType = LineType.solid;

	private boolean m_nodeFilled = true;

	private boolean m_nodeRounded = true;

	private LineType m_edgeLineType = LineType.solid;

	private boolean m_edgeDecorate = false;

	private GraphvizRenderer m_renderer = org.cloudsmith.graph.graphviz.GraphvizRenderer.standard;

	private String m_oid;

	private ResourceSet m_component;

	private String title;

	private IRootGraph m_graph;

	@Inject
	private IGraphProvider graphProvider;

	@Inject
	private IGraphviz graphviz;

	public Grapher(EObject model, String title) {
		if(model == null)
			throw new IllegalArgumentException("model can not be null");
		if(model.eResource() == null)
			throw new IllegalArgumentException("model must be contained in a Resource");
		if(model.eResource().getResourceSet() == null)
			throw new IllegalArgumentException("model's resource must be in a resource set");

		this.m_component = model.eResource().getResourceSet();
		this.title = title;

	}

	/**
	 * Produces a CGSS (Cascading Graph Style Sheet) based on the user's input.
	 * This is also a good demonstration of how to use the Styling capabilities of
	 * the Graphviz support.
	 * 
	 * @return the rule set to use as CGSS
	 */
	private GraphCSS getCGSS() {
		// the rule set that contains all rules and styling
		GraphCSS result = new GraphCSS();

		// a StyleMap corresponds to the { } section in a CSS rule
		StyleSet graphStyle = new StyleSet();
		graphStyle.put(new StyleFactory.Concentrate(Boolean.valueOf(m_concentrate)));
		graphStyle.put(new StyleFactory.Routing(m_routing));
		graphStyle.put(new StyleFactory.RankDirectionStyle(RankDirection.LR));
		graphStyle.put(new StyleFactory.RankSeparation(new Double(2.0)));

		// a rule corresponds to the 'rules { }' in a CSS rule (example the 'A.Foo#x'is a CSS rule)
		result.addRule(new Rule(new Select.Element(ElementType.graph), graphStyle));

		return result;
	}

	public List<Compass> getCompass() {
		return Arrays.asList(Compass.values());
	}

	public IRootGraph getComponentGraph() {
		return null;
	}

	public LineType getEdgeLineType() {
		return m_edgeLineType;
	}

	public List<EdgeRouting> getEdgeRoutings() {
		return Arrays.asList(EdgeRouting.values());
	}

	public byte[] getGraph() {
		if(m_graph == null)
			m_graph = getComponentGraph();
		byte[] img = graphviz.toPNG(m_layout, m_graph, getCGSS());
		return img == null
				? ImageUtils.loadImage("/static/img/no_image.png")
				: img;

	}

	public String getGraphSvg() {
		if(m_graph == null)
			m_graph = getComponentGraph();
		return graphviz.toSVG(m_layout, m_graph, getCGSS());
	}

	public boolean getGraphSvg(OutputStream stream) {
		if(m_graph == null)
			m_graph = getComponentGraph();
		return graphviz.writeSVG(stream, m_layout, m_graph, getCGSS());
	}

	/**
	 * First implementation - test direct writing to stream using just plain svg
	 * 
	 * @param stream
	 * @return
	 * @throws OidNotFoundException
	 * @throws OidAccessDeniedException
	 */
	public boolean getGraphSvgz(OutputStream stream) {
		if(m_graph == null)
			m_graph = getComponentGraph();
		try {
			stream = new GZIPOutputStream(stream);
		}
		catch(IOException e) {
			return false;
		}
		return graphviz.writeSVGZ(stream, m_layout, m_graph, getCGSS());
	}

	public String getGraphText() {
		if(m_graph == null)
			m_graph = getComponentGraph();
		return graphviz.getDotText(m_graph, getCGSS());
	}

	public Arrow getHeadArrow() {
		return m_headArrow;
	}

	public Compass getHeadPort() {
		return m_headPort;
	}

	public GraphvizLayout getLayout() {
		return m_layout;
	}

	public List<LineType> getLineTypes() {
		return Arrays.asList(LineType.values());
	}

	public LineType getNodeLineType() {
		return m_nodeLineType;
	}

	public NodeShape getNodeShape() {
		return m_nodeShape;
	}

	public List<NodeShape> getNodeShapes() {
		return Arrays.asList(NodeShape.values());
	}

	public String getOid() {
		return m_oid;
	}

	public GraphvizRenderer getRenderer() {
		return m_renderer;
	}

	// public List<GraphvizRenderer> getRenderers() {
	// return Arrays.asList(IGraphviz.Renderer.values());
	// }

	public EdgeRouting getRouting() {
		return m_routing;
	}

	public Arrow getTailArrow() {
		return m_tailArrow;
	}

	public Compass getTailPort() {
		return m_tailPort;
	}

	public String getUsemap() {
		if(m_graph == null)
			m_graph = getComponentGraph();
		return graphviz.getUsemap(m_layout, m_graph, getCGSS());
	}

	public boolean isConcentrate() {
		return m_concentrate;
	}

	public boolean isEdgeDecorate() {
		return m_edgeDecorate;
	}

	public boolean isNodeFilled() {
		return m_nodeFilled;
	}

	public boolean isNodeRounded() {
		return m_nodeRounded;
	}

	public void setArrowScale(Double arrowScale) {
		m_arrowScale = arrowScale;
	}

	public void setConcentrate(boolean concentrate) {
		m_concentrate = concentrate;
	}

	public void setEdgeDecorate(boolean edgeDecorate) {
		m_edgeDecorate = edgeDecorate;
	}

	public void setEdgeLineType(LineType edgeLineType) {
		m_edgeLineType = edgeLineType;
	}

	public void setHeadArrow(Arrow headArrow) {
		m_headArrow = headArrow;
	}

	public void setHeadPort(Compass headPort) {
		m_headPort = headPort;
	}

	public void setLayout(GraphvizLayout layout) {
		m_layout = layout;
	}

	public void setNodeFilled(boolean nodeFilled) {
		m_nodeFilled = nodeFilled;
	}

	public void setNodeLineType(LineType nodeLineType) {
		m_nodeLineType = nodeLineType;
	}

	public void setNodeRounded(boolean nodeRounded) {
		m_nodeRounded = nodeRounded;
	}

	public void setNodeShape(NodeShape nodeShape) {
		m_nodeShape = nodeShape;
	}

	public void setOid(String oid) {
		m_oid = oid;
	}

	public void setRenderer(GraphvizRenderer renderer) {
		m_renderer = renderer;
	}

	public void setRouting(EdgeRouting routing) {
		m_routing = routing;
	}

	public void setTailArrow(Arrow tailArrow) {
		m_tailArrow = tailArrow;
	}

	public void setTailPort(Compass tailPort) {
		m_tailPort = tailPort;
	}
}
