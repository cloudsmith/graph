package com.puppetlabs.graph.utils;

import com.puppetlabs.graph.ElementType;
import com.puppetlabs.graph.graphcss.GraphCSS;
import com.puppetlabs.graph.graphcss.Rule;
import com.puppetlabs.graph.graphcss.Select;
import com.puppetlabs.graph.graphcss.StyleSet;
import com.puppetlabs.graph.graphviz.GraphvizLayout;
import com.puppetlabs.graph.style.Arrow;
import com.puppetlabs.graph.style.Compass;
import com.puppetlabs.graph.style.EdgeRouting;
import com.puppetlabs.graph.style.LineType;
import com.puppetlabs.graph.style.NodeShape;
import com.puppetlabs.graph.style.RankDirection;
import com.puppetlabs.graph.style.StyleFactory;

/**
 * TODO: UNFINISHED, USES OLD STYLE FIELD NAMES m_
 * 
 * @author henrik
 * 
 */
public class AbstractGraphStyleRuleSetProvider implements IGraphStyleRuleSetProvider {

	protected GraphvizLayout m_layout = com.puppetlabs.graph.graphviz.GraphvizLayout.dot;

	protected Arrow m_headArrow = Arrow.vee;

	protected Arrow m_tailArrow = Arrow.none;

	protected Compass m_tailPort = Compass.NONE;

	protected Compass m_headPort = Compass.NONE;

	protected Double m_arrowScale = new Double(0.5);

	protected boolean m_concentrate = false;

	protected EdgeRouting m_routing = EdgeRouting.spline;

	protected NodeShape m_nodeShape = NodeShape.rectangle;

	protected LineType m_nodeLineType = LineType.solid;

	protected boolean m_nodeFilled = true;

	protected boolean m_nodeRounded = true;

	protected LineType m_edgeLineType = LineType.solid;

	protected boolean m_edgeDecorate = false;

	protected GraphCSS CGSS = new GraphCSS();

	protected void configureDefaults() {
		CGSS.addRule(getDefaultGraphStyle());
		CGSS.addRule(getDefaultVertexStyle());
		CGSS.addRule(getDefaultEdgeStyle());
	}

	public GraphCSS getCGSS() {
		return CGSS;
	}

	/**
	 * Returns the default rule for ElementType.edge
	 * This implementation returns NULL_RULE as the default is to use the default for the rendering technology.
	 * 
	 * @return GraphStyleRule.NULL_RULE
	 */
	protected Rule getDefaultEdgeStyle() {

		// There is no need to set the value below as they are default when rendering with
		// graphviz. Setting these values will just cause bloat in the generated graph.

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
		// return edgeRule;
		return Rule.NULL_RULE;

	}

	/**
	 * Returns the default rule for ElementType.graph
	 * This implementation returns a graph style that concentrates edges, uses spline routing, LR rank
	 * direction, and with a 2.0 rank separation.
	 * 
	 * @return a configured rule for a graph
	 */
	protected Rule getDefaultGraphStyle() {
		// a StyleMap corresponds to the { } section in a CSS rule
		StyleSet graphStyle = new StyleSet();
		graphStyle.put(new StyleFactory.Concentrate(Boolean.valueOf(m_concentrate)));
		graphStyle.put(new StyleFactory.Routing(m_routing));
		graphStyle.put(new StyleFactory.RankDirectionStyle(RankDirection.LR));
		graphStyle.put(new StyleFactory.RankSeparation(new Double(2.0)));

		// a rule corresponds to the 'rules { }' in a CSS rule (example the 'A.Foo#x'is a CSS rule)
		return new Rule(new Select.Element(ElementType.graph), graphStyle);
	}

	/**
	 * Returns the default rule for ElementType.vertex.
	 * This implementation returns NULL_RULE as the default is to use the default for the rendering technology.
	 * 
	 * @return GraphStyleRule.NULL_RULE
	 */
	protected Rule getDefaultVertexStyle() {
		// There is no need to set the value below as they are default when rendering with
		// graphviz. Setting these values will just cause bloat in the generated graph.

		// StyleMap nodeStyle = new StyleMap();
		// nodeStyle.put(new GraphStyle.Shape(m_nodeShape));
		// nodeStyle.put(new GraphStyle.NodeBrush(m_nodeLineType, new Double(0.5), m_nodeFilled, m_nodeRounded));
		// result.addRule(new GraphStyleRule(
		// new GraphStyleRule.Element(ElementType.vertex), nodeStyle));
		return Rule.NULL_RULE;
	}
}
