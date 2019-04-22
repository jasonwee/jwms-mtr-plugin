package io.jenkins.plugins.sample;

/*
 * Node
id
lable
image
shape
title
 */
public class Node {
	
	private int id;
	private String label;
	private String image = "http://api.opentracker.net/api/img/Hardware-My-Computer-3-icon.png";
	private String shape = "image";
	private int size = 25;
	private String title;

	public Node(String label) {
		this.label = label;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	//{id: 1, label: '${it.sourceNode}', image: 'http://api.opentracker.net/api/img/Hardware-My-Computer-3-icon.png', shape: 'image', size: 25, title: '78.46.64.14'},
	public String toString() {
		return "{"+ 
	                "id: "        + id + ", " +
	                "label: '"     + label + "', " +
				    "image: '"   + image + "', " +
	                "shape: '"   + shape +  "', " +
				    "size: "       + size + ", " +
				    "title: '"      + title + "'" + 
				     "},";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (id != other.id)
			return false;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		return true;
	}
}
