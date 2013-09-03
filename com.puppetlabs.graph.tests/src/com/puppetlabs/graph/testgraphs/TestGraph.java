/*******************************************************************
 * Copyright (c) 2006-2011, Cloudsmith Inc.
 * The code, documentation and other materials contained herein
 * are the sole and exclusive property of Cloudsmith Inc. and may
 * not be disclosed, used, modified, copied or distributed without
 * prior written consent or license from Cloudsmith Inc.
 ******************************************************************/

package org.cloudsmith.graph.testgraphs;

import java.util.Collection;
import java.util.List;

import org.cloudsmith.graph.IGraphProvider;
import org.cloudsmith.graph.IRootGraph;
import org.cloudsmith.graph.elements.ClusterGraph;
import org.cloudsmith.graph.elements.Edge;
import org.cloudsmith.graph.elements.RootGraph;
import org.cloudsmith.graph.elements.Vertex;
import org.cloudsmith.graph.graphcss.IFunctionFactory;
import org.cloudsmith.graph.graphcss.Rule;
import org.cloudsmith.graph.graphcss.Select;
import org.cloudsmith.graph.graphcss.StyleSet;
import org.cloudsmith.graph.style.Alignment;
import org.cloudsmith.graph.style.Arrow;
import org.cloudsmith.graph.style.Compass;
import org.cloudsmith.graph.style.EdgeRouting;
import org.cloudsmith.graph.style.IStyleFactory;
import org.cloudsmith.graph.style.LineType;
import org.cloudsmith.graph.style.NodeShape;
import org.cloudsmith.graph.style.labels.LabelTable;
import org.cloudsmith.graph.style.labels.LabelTableBuilder;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

/**
 * A test and demo of the graphviz graph layout and rendering support.
 * TODO: UNFINISHED, AND HAS ISSUES...
 * 
 */
public class TestGraph implements IGraphProvider {

	private Arrow m_headArrow = Arrow.vee;

	// none is the default, and does not have to be set...
	// private Arrow m_tailArrow = Arrow.none;

	private Compass m_tailPort = Compass.NONE;

	private Compass m_headPort = Compass.n;

	private Double m_arrowScale = new Double(1.0);

	private boolean m_concentrate = false;

	private EdgeRouting m_routing = EdgeRouting.spline;

	private NodeShape m_nodeShape = NodeShape.rectangle;

	private LineType m_nodeLineType = LineType.solid;

	private boolean m_nodeFilled = true;

	private boolean m_nodeRounded = true;

	private LineType m_edgeLineType = LineType.solid;

	private boolean m_edgeDecorate = false;

	// private GraphvizRenderer m_renderer = GraphvizRenderer.gd;
	//
	// private DotRenderer dotRenderer;

	private IStyleFactory styleFactory;

	private IFunctionFactory functions;

	@Inject
	public TestGraph(IStyleFactory styleFactory, IFunctionFactory functions) {
		this.styleFactory = styleFactory;
		this.functions = functions;
	}

	public IRootGraph computeGraph() {
		return computeGraph(null, "a graph with subgraphs", "root");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudsmith.graph.IGraphProvider#computeGraph()
	 */
	@Override
	public IRootGraph computeGraph(Object modelObj) {
		return computeGraph();
	}

	// public byte[] getGraph() {
	// IGraph graph = getMockGraph();
	// IGraphviz g = new Graphviz(new DefaultGraphvizConfig(), dotRenderer);
	// byte[] img = g.getPngImage(m_layout, graph, getCGSS(), null);
	// // TODO: get the bytes of the no_image.png under images folder
	// return img == null
	// ? ImageUtils.loadImage("/static/img/no_image.png")
	// : img;
	//
	// }

	/**
	 * @modelObj - ignored, returns same graph at all times.
	 */
	public IRootGraph computeGraph(Object modelObj, String title, String id) {
		RootGraph g = new RootGraph(title, "RootGraph", id);
		ClusterGraph sub1 = getMockGraph("x1");
		g.addSubgraph(sub1);
		ClusterGraph sub2 = getMockGraph("x2");
		sub2.addSubgraph(getNestedMockGraph());
		g.addSubgraph(sub2);
		g.addEdge(new Edge("between", Iterators.get(sub1.getVertices().iterator(), 0), Iterators.get(
			sub2.getVertices().iterator(), 0)));
		g.addEdge(new Edge("between2", Iterators.get(sub1.getVertices().iterator(), 3), Iterators.get(
			sub2.getVertices().iterator(), 3)));
		return g;
	}

	public ClusterGraph getMockGraph(String id) {
		ClusterGraph g = new ClusterGraph(id, "", id);
		Vertex[] vv = new Vertex[5];
		for(int i = 0; i < vv.length; i++) {
			vv[i] = new Vertex("label_" + Integer.toString(i) + "\nsome text", "");
			vv[i].setStyles(StyleSet.withImmutableStyles(styleFactory.href("http://www.google.com")));
			g.addVertex(vv[i]);
		}
		vv[1].addStyleClass("MakeMePink");
		Edge[] ev = new Edge[4];
		for(int i = 0; i < ev.length; i++) {
			ev[i] = new Edge("label_" + Integer.toString(i), vv[0], vv[i + 1]);
			ev[i].setStyles(StyleSet.withImmutableStyles(styleFactory.href("http://www.google.com")));
			g.addEdge(ev[i]);
		}
		g.addEdge(new Edge(vv[1], vv[2]));
		g.addEdge(new Edge(vv[2], vv[3]));

		// add some data to a node
		vv[4].getUserData().put("name", "John Doe");
		vv[4].getUserData().put("version", "1.2.3");
		vv[4].getUserData().put("type", "demo");
		vv[4].addStyleClass("WithData");

		ev[1].addStyleClass("SpecialArrows");
		ev[2].addStyleClass("Testing");
		ev[3].addStyleClass("MakeMePink");

		return g;
	}

	private ClusterGraph getNestedMockGraph() {
		ClusterGraph g = new ClusterGraph("x11", "a graph with subgraphs", "RootGraph");
		ClusterGraph sub1 = getMockGraph("x12");
		g.addSubgraph(sub1);
		ClusterGraph sub2 = getMockGraph("x13");
		g.addSubgraph(sub2);
		g.addEdge(new Edge(Iterators.get(sub1.getVertices().iterator(), 0), Iterators.get(
			sub2.getVertices().iterator(), 0)));
		g.addEdge(new Edge(Iterators.get(sub1.getVertices().iterator(), 3), Iterators.get(
			sub2.getVertices().iterator(), 3)));
		return g;
	}

	/**
	 * Produces a CGSS (Cascading Graph Style Sheet) based on the user's input.
	 * This is also a good demonstration of how to use the Styling capabilities of
	 * the Graphviz support.
	 * 
	 * @return the rule set to use as CGSS
	 */
	public Collection<Rule> getRules() {

		// the rule set that contains all rules and styling
		List<Rule> result = Lists.newArrayList();

		// Styles for the graph
		result.add(Select.graph().withStyles( //
			styleFactory.concentrate(m_concentrate), //
			styleFactory.routing(m_routing)));

		// styles for vertexes
		result.add(Select.vertex().withStyles( //
			styleFactory.shape(m_nodeShape), //
			styleFactory.shapeBrush(m_nodeLineType, 0.5, m_nodeFilled, m_nodeRounded)));

		result.add(Select.edge().withStyles( //
			styleFactory.lineBrush(m_edgeLineType, 0.5), //
			styleFactory.headPort(m_headPort), //
			styleFactory.tailPort(m_tailPort), //
			styleFactory.arrowHead(m_headArrow), //
			styleFactory.arrowScale(m_arrowScale), //
			styleFactory.decorate(m_edgeDecorate)));

		// Create a label format - a table with 3 rows, and a cell in each. The cells pick up
		// data called 'name', 'type' and 'version'
		//
		LabelTableBuilder tableBuilder = new LabelTableBuilder(styleFactory, functions) {

			@Override
			public LabelTable build() {
				return table("DataTable", //
					row("FirstRow", cell("NameCell", "name")), //
					row("SecondRow", cell("TypeCell", "type")), //
					row("ThirdRow", cell("VersionCell", "version")) //
				);
			}

		};

		result.add(Select.vertex("WithData").withStyle(styleFactory.labelFormat(tableBuilder.build())));

		// Make the cells in a "DataTable" left aligned, and in orange color
		result.add(Select.and(Select.cell(), Select.containment(Select.table("DataTable"))).withStyles( //
			styleFactory.align(Alignment.left), //
			styleFactory.color("#FFA144")));

		// Style all "NameCell" cells to define a port called "name"
		//
		result.add(Select.element("VersionCell", null).withStyle(styleFactory.port("name")));

		// this demonstrates the Between rule for edges - i.e. between "any" and "WithData"
		// - Make all nodes that point to a vertex with style class "WithData" point to the
		// port "name" - and make the line orange
		// Note that ports can be a combination of a named port, and a compass - here we always use
		// "name" and the headport that the use can change
		//
		result.add(Select.between(Select.any(), Select.element("WithData")).withStyles( //
			styleFactory.lineColor("#FFA144"), //
			styleFactory.headPort("name", m_headPort)));

		return result;
	}

	// public String getUsemap() {
	// IGraph graph = getMockGraph();
	// // usemap does not support different renderers (?)
	// // TODO: check if usemap is ok for cairo as well as gd
	// // right now the Graphviz runner sets the renderer to null for usemap rendering.
	// //
	// IGraphviz g = new Graphviz(new DefaultGraphvizConfig(), dotRenderer);
	// return g.getUsemap(m_layout, graph, getCGSS(), null);
	// }
}
