package io.jenkins.plugins.sample;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MTRReport {
	
	Set<Node> nodes;
	List<Edge> edges;
	
	public MTRReport() {
		nodes = new HashSet<Node>();
		edges = new ArrayList<Edge>();
	}


	public boolean addNode(Node node) {
		return nodes.add(node);
	}
	
	public void addEdge(Edge edge) {
		edges.add(edge);
	}
	
	
	
	
	
	public void setNodes(Set<Node> nodes) {
		this.nodes = nodes;
	}
	
	public Set<Node> getNodes() {
		return nodes;
	}

	public void setEdges(List<Edge> edges) {
		this.edges = edges;
	}
	
	public List<Edge> getEdges() {
		return edges;
	}
	
}
