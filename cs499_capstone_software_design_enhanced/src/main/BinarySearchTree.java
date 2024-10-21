package main;

//Binary Search Tree to manage courses
public class BinarySearchTree {
	private Node root;

    // Constructor
    public BinarySearchTree() {
        root = null;
    }

    // Insert method
    public void insert(Course course) {
        root = addNode(root, course);
    }

    // Helper function to add node to the tree
    private Node addNode(Node root, Course course) {
        if (root == null) {
            return new Node(course);
        }

        if (course.courseId.compareTo(root.course.courseId) < 0) {
            root.left = addNode(root.left, course);
        } else if (course.courseId.compareTo(root.course.courseId) > 0) {
            root.right = addNode(root.right, course);
        }
        return root;
    }

    // InOrder traversal to display courses
    public void inOrder() {
        inOrderTraversal(root);
    }

    // Helper function for in-order traversal
    private void inOrderTraversal(Node root) {
        if (root != null) {
            inOrderTraversal(root.left);
            System.out.println("Course ID: " + root.course.courseId + ", Title: " + root.course.title);
            inOrderTraversal(root.right);
        }
    }

    // Search for a course by ID
    public Course search(String courseId) {
        return searchNode(root, courseId);
    }

    // Helper function to search for a node in the tree
    private Course searchNode(Node root, String courseId) {
        if (root == null || root.course.courseId.equals(courseId)) {
            return root != null ? root.course : null;
        }

        if (courseId.compareTo(root.course.courseId) < 0) {
            return searchNode(root.left, courseId);
        } else {
            return searchNode(root.right, courseId);
        }
    }
}
