package main;

//Class to represent a Node in the Binary Search Tree
public class Node {
	Course course;
    Node left, right;

    // Constructor
    public Node(Course course) {
        this.course = course;
        this.left = this.right = null;
    }
}
