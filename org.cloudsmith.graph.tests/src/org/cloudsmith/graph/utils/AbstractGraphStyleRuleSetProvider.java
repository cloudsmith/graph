package org.cloudsmith.graph.utils;

import java.util.Collections;

import org.cloudsmith.graph.ElementType;
import org.cloudsmith.graph.graphcss.GraphCSS;
import org.cloudsmith.graph.graphcss.Rule;
import org.cloudsmith.graph.graphcss.Select;
import org.cloudsmith.graph.graphcss.StyleSet;
import org.cloudsmith.graph.graphviz.GraphvizLayout;
import org.cloudsmith.graph.style.Alignment;
import org.cloudsmith.graph.style.Arrow;
import org.cloudsmith.graph.style.Compass;
import org.cloudsmith.graph.style.EdgeRouting;
import org.cloudsmith.graph.style.LineType;
import org.cloudsmith.graph.style.NodeShape;
import org.cloudsmith.graph.style.RankDirection;
import org.cloudsmith.graph.style.StyleFactory;

/**
 * TODO: UNFINISHED, USES OLD STYLE FIELD NAMES m_
 * 
 * @author henrik
 * 
 */
public class AbstractGraphStyleRuleSetProvider implements IGraphStyleRuleSetProvider {

	protected GraphvizLayout m_layout = org.cloudsmith.graph.graphviz.GraphvizLayout.dot;

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

	private GraphCSS getCGSSx() {
		// the rule set that contains all rules and styling
		GraphCSS result = new GraphCSS();

		// and so on...

		// Create a label format - a table with 3 rows, and a cell in each. The cells pick up
		// data called 'name', 'type' and 'version'
		//
		StyleSet withDataStyle = new StyleSet();
		// withDataStyle.put(new StyleFactory.LabelFormat(new LabelTable("DataTable", new LabelRow(
		// "FirstRow", new LabelCell("TypeCell", "#{element.data['type']}")), new LabelRow(
		// "SecondRow", new LabelCell("NameCell", "#{element.data['name']}")), new LabelRow(
		// "ThirdRow", new LabelCell("VersionCell", "#{element.data['version']}")))));
		result.addRule(new Rule(Select.element(ElementType.vertex, "CSpec"), withDataStyle));
		// Make the cells in a "DataTable" left aligned
		StyleSet leftAligned = new StyleSet();
		leftAligned.put(new StyleFactory.Align(Alignment.left));
		result.addRule(new Rule(new Select.And(new Select.Element(ElementType.cell), // a cell
			new Select.Containment( // contained
				new Select.Element(ElementType.table, Collections.singleton("DataTable"), null))),// in a data table
			leftAligned)); // left aligned

		return result;
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
