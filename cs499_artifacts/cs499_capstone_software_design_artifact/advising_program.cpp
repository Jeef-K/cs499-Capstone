#include <iostream>
#include <vector>
#include <fstream>
#include <string>
#include <sstream>
#include <stdexcept>
#include <cctype>


using namespace std;



// define a structure to hold course information
struct Course {
    string courseId; // unique identifier
    string title;
    vector<string> prerequisite;
};

// Internal structure for tree node
struct Node {
    Course course;
    Node* left;
    Node* right;

    // default constructor
    Node() {
        left = nullptr;
        right = nullptr;
    }

    // initialize with a course
    Node(Course aCourse) :
        Node() {
        course = aCourse;
    }
};



class BinarySearchTree {

private:
    Node* root;

    void addNode(Node* node, Course course);
    void inOrder(Node* node);
    //Node* removeNode(Node* node, string courseId);

public:
    BinarySearchTree();
    void InOrder();
    void Insert(Course course);
    //void Remove(string courseId);
    Course Search(string courseId);
};

/**
 * Default constructor
 */
BinarySearchTree::BinarySearchTree() {
    //root is equal to nullptr
    this->root = nullptr;
}



/**
 * Traverse the tree in order
 */
void BinarySearchTree::InOrder() {
    // call inOrder fuction and pass root 
    inOrder(this->root);
}


  /**
   * Insert a course
   */
void BinarySearchTree::Insert(Course course) {
    // if root equarl to null ptr
    if (root == nullptr) {
        // root is equal to new node course
        root = new Node(course);
    }
    // else
    else {
        // add Node root and course
        this->addNode(root, course);
    }
}

/**
 * Remove a course
 */
//void BinarySearchTree::Remove(string courseId) {
//    // remove node root courseID
//    this->removeNode(root, courseId);
//}

/**
 * Search for a course
 */
Course BinarySearchTree::Search(string courseId) {
    // set current node equal to root
    Node* currentNode = root;

    // keep looping downwards until bottom reached or matching courseId found
    while (currentNode != nullptr) {
        // if match found, return current course
        if (currentNode->course.courseId == courseId) {
            return currentNode->course;
        }
        // if course is smaller than current node then traverse left
        if (currentNode->course.courseId > courseId) {
            currentNode = currentNode->left;
        }
        // else larger so traverse right
        else {
            currentNode = currentNode->right;
        }
    }


    Course course;
    return course;
}

/**
 * Add a course to some node (recursive)
 *
 * @param node Current node in tree
 * @param course Course to be added
 */
void BinarySearchTree::addNode(Node* node, Course course) {
    // FIXME (8) Implement inserting a course into the tree
    // if node is larger then add to left
    if (node->course.courseId > course.courseId) {

        // if no left node
        if (node->left == nullptr) {
            // this node becomes left
            node->left = new Node(course);
        }
        // else recurse down the left node
        else {
            this->addNode(node->left, course);
        }
    }
    // else
    else {
        // if no right node
        if (node->right == nullptr) {
            // this node becomes right
            node->right = new Node(course);
        }
        //else
        else {
            // recurse down the right node
            this->addNode(node->right, course);

        }
    }
}
void BinarySearchTree::inOrder(Node* node) {
    // FixMe (9): Pre order root
    //if node is not equal to null ptr
    if (node != nullptr) {
        //InOrder not left
        inOrder(node->left);
        //output courseID, title, amount, fund
        cout << node->course.courseId << ", " << node->course.title << endl;
        //InOder right
        inOrder(node->right);
    }
}







/**
 * Display the course information to the console (std::out)
 *
 * @param course struct containing the course info
 */
void displayCourse(Course course) {
    cout << course.courseId << ", " << course.title << endl;

    cout << "prerequisites: ";
    for (int i = 0; i < course.prerequisite.size(); i++) {
        cout << course.prerequisite.at(i);
        if ( (i + 1) < course.prerequisite.size()) {
            cout << ", ";
        }
    }
    cout << endl;
    return;
}

/**
 * Load file containing courses into a container
 *
 */
void loadCourses(const std::string& filePath, BinarySearchTree* bst) {
    std::ifstream file(filePath);
    if (!file) {
        throw std::runtime_error("Failed to open file");
    }

    std::string line;
    while (getline(file, line)) {
        std::vector<std::string> row;
        std::stringstream ss(line);
        std::string item;

        while (getline(ss, item, ',')) {
            row.push_back(item);
        }

        Course course;
        course.courseId = row[0];
        course.title = row[1];
        for (size_t i = 2; i < row.size(); i++) {
            course.prerequisite.push_back(row[i]);
        }

        try {
            bst->Insert(course);
        }
        catch (const std::exception& e) {
            std::cerr << e.what() << std::endl;
        }
    }
}


void to_upper(std::string& s) {
    for (char& c : s) {
        c = std::toupper(c);
    }
}






int main(int argc, char* argv[]) {

    // process command line arguments
    string filePath, courseKey;
    switch (argc) {
    case 2:
        filePath = argv[1];
        courseKey = "MATH201";
        break;
    case 3:
        filePath = argv[1];
        courseKey = argv[2];
        break;
    default:
        filePath = "advising_program_input.csv";
        courseKey = "MATH201";
    }


    // Define a binary search tree to hold all courses
    BinarySearchTree* bst;
    bst = new BinarySearchTree();
    Course course;

    int choice = 0;
    while (choice != 9) {
        cout << "Menu:" << endl;
        cout << "  1. Load Courses" << endl;
        cout << "  2. Display All Courses" << endl;
        cout << "  3. Find Course" << endl;
        cout << "  9. Exit" << endl;
        cout << "Enter choice: ";
        cin >> choice;

        switch (choice) {

        case 1:
            // Complete the method call to load the courses
            loadCourses(filePath, bst);
            cout << "Courses loaded successfully." << endl;

            break;

        case 2:
            cout << "Here is a sample schedule: " << endl;
            cout << endl;
            bst->InOrder();
            break;

        case 3:
            cout << endl;
            cout << "Please Enter The Course ID: ";
            cin >> courseKey;
            cout << endl;

            to_upper(courseKey);
            course = bst->Search(courseKey);
            if (!course.courseId.empty()) {
                displayCourse(course);
            }
            else {
                cout << "Course Id " << courseKey << " not found." << endl;
            }

            break;
        case 4:
            cout << "Thank you for using the course planner!" << endl;
        case 9:
            break;
        default:
            cout << choice << " is not a valid option." << endl;
        }
        cout << endl;
    }

        cout << "Good bye." << endl;

        return 0;
    
}
