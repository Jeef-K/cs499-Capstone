package main;

import java.util.List;

/**
 * Class to represent a Course in the academic advising program.
 * This class stores course details including course ID, name, credits,
 * department, and a list of prerequisites. It provides getters and 
 * setters for accessing and modifying the course attributes.
 */
public class Course {
    // Fields representing course attributes
    private String courseId;        // Unique identifier for the course
    private String courseName;      // Name of the course
    private int credits;            // Number of credits for the course
    private String department;      // Department offering the course
    private List<String> prerequisites;  // List to handle multiple prerequisites

    /**
     * Constructor for the Course class.
     * Initializes the course with a unique course ID, name, credits, department, 
     * and a list of prerequisites.
     * 
     * @param courseId       The unique course identifier.
     * @param courseName     The name of the course.
     * @param credits        The number of credits the course offers.
     * @param department     The department under which the course is offered.
     * @param prerequisites  A list of prerequisites for the course, which may be empty.
     */
    public Course(String courseId, String courseName, int credits, String department, List<String> prerequisites) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.credits = credits;
        this.department = department;
        this.prerequisites = prerequisites;  // Initialize prerequisites list
    }

    // Getters and setters for accessing course properties
    public String getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getCredits() {
        return credits;
    }

    public String getDepartment() {
        return department;
    }

    /**
     * Returns the list of prerequisites for the course.
     * This list may be empty if no prerequisites are required.
     * 
     * @return A List of prerequisite course IDs.
     */
    public List<String> getPrerequisites() {
        return prerequisites;
    }

    /**
     * Overrides the default toString method to provide a string 
     * representation of the course, including its ID, name, credits, 
     * department, and a list of prerequisites (if any).
     * 
     * @return A formatted string with course details.
     */
    @Override
    public String toString() {
        // Display "None" if there are no prerequisites; otherwise, show the list
        return "Course ID: " + courseId + ", Name: " + courseName + ", Credits: " + credits + 
               ", Department: " + department + ", Prerequisites: " + (prerequisites.isEmpty() ? "None" : prerequisites);
    }
}