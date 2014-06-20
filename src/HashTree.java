import java.util.Arrays;

import de.medieninf.ads.ADSTool;

public class HashTree {

	private abstract class Node {
		public byte[] hash; // 20 byte hash
	}

	private class NodeInner extends Node {
		public Node left, right; // Unterknoten
	}

	private class NodeLeaf extends Node {
		public byte[] data; // Daten
	}

	private static byte[] nullHash = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };
	private Node root;

	public HashTree(byte[] data, int blocksize) {

		int blocks = (int) Math.ceil((double) data.length / blocksize);
		NodeLeaf[] nodes = new NodeLeaf[blocks];

		for (int i = 0; i < blocks; i++) {
			byte[] d = new byte[blocksize];
			System.arraycopy(data, i * blocksize, d, 0, blocksize);
			NodeLeaf n = new NodeLeaf();
			n.data = d;
			n.hash = ADSTool.sha1(d);
			nodes[i] = n;
		}

		this.root = getRootNode(nodes);

	}

	private Node getRootNode(Node[] nodes) {
		return compactNodes(nodes)[0];
	}

	private Node[] compactNodes(Node[] nodes) {

		// Abbruchbedingung
		if (nodes.length <= 1) return nodes;

		Node[] newNodes = new Node[(int) Math.ceil(nodes.length / 2d)];
		for (int i = 0; i < newNodes.length; i++) {
			NodeInner node = new NodeInner();
			int index2 = 2 * i + 1;
			node.left = nodes[2 * i];
			node.right = (index2 >= nodes.length) ? null : nodes[2 * i + 1];
			byte[] concat = new byte[node.left.hash.length * 2];
			System.arraycopy(node.left.hash, 0, concat, 0, node.left.hash.length);
			System.arraycopy((index2 >= nodes.length) ? nullHash : node.right.hash, 0, concat, node.left.hash.length, node.left.hash.length);
			node.hash = ADSTool.sha1(concat);
			newNodes[i] = node;
		}

		return compactNodes(newNodes);

	}

	public void display() {
		display(root);
	}

	public void display(Node node) {
		if (node instanceof NodeLeaf) {
			NodeLeaf nodeLeaf = (NodeLeaf) node;
			//System.out.print(bytesToHex(nodeLeaf.hash) + ": ");
			System.out.println(Arrays.toString(nodeLeaf.data));
		} else {
			NodeInner nodeInner = (NodeInner) node;
			//System.out.println(bytesToHex(nodeInner.hash) + ": inner node");
			if (nodeInner.left != null) display(nodeInner.left);
			if (nodeInner.right != null) display(nodeInner.right);
		}
	}

}
