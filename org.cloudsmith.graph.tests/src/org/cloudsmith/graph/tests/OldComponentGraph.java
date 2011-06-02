/*******************************************************************
 * Copyright (c) 2006-2007, Cloudsmith Inc.
 * The code, documentation and other materials contained herein
 * are the sole and exclusive property of Cloudsmith Inc. and may
 * not be disclosed, used, modified, copied or distributed without
 * prior written consent or license from Cloudsmith Inc.
 ******************************************************************/

package org.cloudsmith.graph.tests;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import org.cloudsmith.graph.ElementType;
import org.cloudsmith.graph.IGraph;
import org.cloudsmith.graph.elements.Edge;
import org.cloudsmith.graph.elements.Graph;
import org.cloudsmith.graph.elements.Vertex;
import org.cloudsmith.graph.graphcss.GraphCSS;
import org.cloudsmith.graph.graphcss.Rule;
import org.cloudsmith.graph.graphcss.Select;
import org.cloudsmith.graph.graphcss.StyleSet;
import org.cloudsmith.graph.graphviz.GraphvizLayout;
import org.cloudsmith.graph.graphviz.GraphvizRenderer;
import org.cloudsmith.graph.graphviz.IGraphviz;
import org.cloudsmith.graph.style.Alignment;
import org.cloudsmith.graph.style.Arrow;
import org.cloudsmith.graph.style.Compass;
import org.cloudsmith.graph.style.EdgeRouting;
import org.cloudsmith.graph.style.LineType;
import org.cloudsmith.graph.style.NodeShape;
import org.cloudsmith.graph.style.RankDirection;
import org.cloudsmith.graph.style.StyleFactory;
import org.cloudsmith.graph.utils.ImageUtils;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

import com.google.inject.Inject;

/**
 * A test and demo of the graphviz graph layout and rendering support.
 * This is a copy of old code that builds a model consisting of:
 * Component - 0* -> Version -> Component -> Dependency (on Component with a range).
 * The graph is quite complex.
 * Code is semi hacked to deal with EMF ResourceSet, Resource and EObjects...
 */
public class OldComponentGraph {
	public static class EObjectIterator implements Iterable<EObject> {

		private Iterator<EObject> iterator;

		public EObjectIterator(Iterator<EObject> iterator) {
			this.iterator = iterator;
		}

		public Iterator<EObject> iterator() {
			return iterator;
		}

	}

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

	private IGraph m_graph;

	@Inject
	IGraphviz graphviz;

	public OldComponentGraph(EObject model, String title) {
		if(model == null)
			throw new IllegalArgumentException("model can not be null");
		if(model.eResource() == null)
			throw new IllegalArgumentException("model must be contained in a Resource");
		if(model.eResource().getResourceSet() == null)
			throw new IllegalArgumentException("model's resource must be in a resource set");

		this.m_component = model.eResource().getResourceSet();
		this.title = title;

	}

	public Double getArrowScale() {
		return m_arrowScale;
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

		// and so on...
		// StyleMap nodeStyle = new StyleMap();
		// nodeStyle.put(new GraphStyle.Shape(m_nodeShape));
		// nodeStyle.put(new GraphStyle.NodeBrush(m_nodeLineType, new Double(0.5), m_nodeFilled, m_nodeRounded));
		// result.addRule(new GraphStyleRule(
		// new GraphStyleRule.Element(ElementType.vertex), nodeStyle));

		// StyleMap edgeStyle = new StyleMap();
		// edgeStyle.put(new GraphStyle.EdgeBrush(m_edgeLineType, new Double(0.5))); // DEFAULT
		// edgeStyle.put(new GraphStyle.HeadPort(m_headPort)); // DEFAULT
		// edgeStyle.put(new GraphStyle.TailPort(m_tailPort)); // DEFAULT
		// edgeStyle.put(new GraphStyle.ArrowHead(m_headArrow)); //DEFAULT
		// edgeStyle.put(new GraphStyle.ArrowTail(m_tailArrow)); // DEFAULT
		// edgeStyle.put(new GraphStyle.ArrowScale(m_arrowScale)); //DEFAULT
		// edgeStyle.put(new GraphStyle.Decorate(Boolean.valueOf(m_edgeDecorate)));
		// GraphStyleRule edgeRule = new GraphStyleRule(
		// new GraphStyleRule.Element(ElementType.edge), edgeStyle);
		// result.addRule(edgeRule);

		// Create a label format - a table with 3 rows, and a cell in each. The cells pick up
		// data called 'name', 'type' and 'version'
		//
		StyleSet withDataStyle = new StyleSet();
		// withDataStyle.put(new StyleFactory.LabelFormat(new LabelTable("DataTable", new LabelRow("FirstRow", new LabelCell(
		// "TypeCell", "#{element.data['type']}")), new LabelRow("SecondRow", new LabelCell(
		// "NameCell", "#{element.data['name']}")), new LabelRow("ThirdRow", new LabelCell(
		// "VersionCell", "#{element.data['version']}")))));
		result.addRule(new Rule(new Select.Element(ElementType.vertex, "CSpec", null), withDataStyle));
		// Make the cells in a "DataTable" left aligned
		StyleSet leftAligned = new StyleSet();
		leftAligned.put(new StyleFactory.Align(Alignment.left));
		result.addRule(new Rule(new Select.And(new Select.Element(ElementType.cell), // a cell
			new Select.Containment( // contained
				new Select.Element(ElementType.table, "DataTable", null))),// in a data table
			leftAligned)); // left aligned

		return result;
	}

	public List<Compass> getCompass() {
		return Arrays.asList(Compass.values());
	}

	public IGraph getComponentGraph() {

		Map<Long, Graph> c2g = new HashMap<Long, Graph>(10); // map of component id to graph
		Map<EObject, Vertex> c2v = new HashMap<EObject, Vertex>(30); // map of EObject to node
		Map<URI, Graph> s2g = new HashMap<URI, Graph>(5); // map of Resource to graph

		// The root graph
		Graph g = new Graph("ResourceSet", "ComponentGraph", "root");
		// c2g.put(m_component.getId(), g);

		// add a vertex for the resource set itself
		Vertex componentVertex = new Vertex(title, "Component");
		g.addVertex(componentVertex);
		// c2v.put(m_component.getId(), componentVertex);

		// add a cluster subgraph per resource
		for(Resource csd : m_component.getResources()) {
			URI uri = csd.getURI();
			Graph spaceGraph = s2g.get(uri);
			if(spaceGraph == null) {
				spaceGraph = new Graph("Resource: " + uri.toString(), "Space");
				spaceGraph.setCluster(true);
				s2g.put(uri, spaceGraph);
				g.addSubgraph(spaceGraph);
			}

			for(EObject cspec : new EObjectIterator(csd.getAllContents())) {
				Vertex cspecNode = new Vertex(cspec.eClass().getName(), "CSpec");
				Map<String, String> data = cspecNode.getData();
				spaceGraph.addVertex(cspecNode);
				c2v.put(cspec, cspecNode);

				data.put("type", "cspec");
				data.put("name", cspec.eClass().getName());
				// data.put("version", cspec.getVersionString());
				// data.put("versionType", cspec.getVersionType());

				// An edge is added from the component to each cspec
				Edge versionOf = new Edge("Version", componentVertex, cspecNode);
				g.addEdge(versionOf);

				// What to do here?
				for(EObject dep : cspec.eCrossReferences()) {
					// Add the component that the root component depends on
					// Add a node for it in the top graph
					// Add an edge with the version designator as label
					//
					// Component c = dep.getComponent();
					// Vertex depComponentNode = c2v.get(c.getId());
					// if(depComponentNode == null)
					// {
					// depComponentNode = new Vertex("Component:" + c.getName(), "DepComponent");
					// c2v.put(c.getId(), depComponentNode);
					// g.addVertex(depComponentNode);
					// }
					// String versionDesignator = dep.getVersionDesignator();
					// Edge depEdge = new Edge(versionDesignator,"Dependency", cspecNode, depComponentNode);
					// spaceGraph.addEdge(depEdge);
					//
					// For the component the root component depends on
					// Add a cluster subgraph per space
					// Output all cspecs for it in each space
					//
					// for(ComponentSpecData csdDep : c.getComponentSpecDatas())
					// {
					// PublishingSpace depSpace = csdDep.getSpace();
					// Graph spaceGraphDep = s2g.get(depSpace.getId());
					// if(spaceGraphDep == null)
					// {
					// spaceGraphDep = new Graph("Space: "+csdDep.getSpaceName(), "Space");
					// spaceGraphDep.setCluster(true);
					// s2g.put(depSpace.getId(), spaceGraphDep);
					// g.addSubgraph(spaceGraphDep);
					// }
					// for(ComponentSpec cspecDep : csdDep.getComponentSpecs())
					// {
					// // Each cspec is added as a node in the dep subgraph
					//
					// Vertex cspecNodeDep = c2v.get(cspecDep.getId());
					// if(cspecNodeDep == null)
					// {
					// cspecNodeDep = new Vertex(cspecDep.getName(),"CSpec");
					// c2v.put(cspecDep.getId(), cspecNodeDep);
					// spaceGraphDep.addVertex(cspecNodeDep);
					//
					// Map<String, String> dataDep = cspecNodeDep.getData();
					// dataDep.put("type", "cspec");
					// dataDep.put("name", cspecDep.getName());
					// dataDep.put("version", cspecDep.getVersionString());
					// dataDep.put("versionType", cspecDep.getVersionType());
					//
					// // An edge is added from the component to each cspec
					// // only do this once per component - cspec
					// Edge versionOfDep = new Edge("Version", depComponentNode, cspecNodeDep);
					// spaceGraphDep.addEdge(versionOfDep);
					// }
					// }
					// }
				}
			}
		}

		return g;
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
		byte[] img = graphviz.toPNG(m_layout, m_graph, getCGSS(), null);
		return img == null
				? ImageUtils.loadImage("/static/img/no_image.png")
				: img;

	}

	public String getGraphSvg() {
		if(m_graph == null)
			m_graph = getComponentGraph();
		return graphviz.toSVG(m_layout, m_graph, getCGSS(), null);
	}

	public boolean getGraphSvg(OutputStream stream) {
		if(m_graph == null)
			m_graph = getComponentGraph();
		return graphviz.writeSVG(stream, m_layout, m_graph, getCGSS(), null);
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
		return graphviz.writeSVGZ(stream, m_layout, m_graph, getCGSS(), null);
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

	// /**
	// * Returns a list of available layouts
	// *
	// * @return
	// */
	// public List<GraphvizLayout> getLayouts() {
	// return Arrays.asList(IGraphviz.Layout.values());
	// }

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

	// public List<GraphvizRenderer> getRenderers() {
	// return Arrays.asList(IGraphviz.Renderer.values());
	// }

	public GraphvizRenderer getRenderer() {
		return m_renderer;
	}

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
		return graphviz.getUsemap(m_layout, m_graph, getCGSS(), null);
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
