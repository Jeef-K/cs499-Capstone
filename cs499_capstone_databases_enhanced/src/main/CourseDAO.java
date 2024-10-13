package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) class for managing course-related operations in the SQLite database.
 * This class handles inserting, retrieving, updating, and deleting courses from the database,
 * as well as managing prerequisites for each course.
 */
public class CourseDAO {

    /**
     * Inserts a new course into the Course table in the SQLite database.
     * If the course has prerequisites, they are also inserted into the Prerequisite table.
     * 
     * Time complexity: O(1) for inserting the course and O(m) for inserting the prerequisites, where m is the number of prerequisites.
     */
    public void insertCourse(Course course) throws SQLException {
        String courseSql = "INSERT INTO Course(courseId, courseName, credits, department) VALUES(?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(courseSql)) {
            // Insert course details into the Course table
            pstmt.setString(1, course.getCourseId());
            pstmt.setString(2, course.getCourseName());
            pstmt.setInt(3, course.getCredits());
            pstmt.setString(4, course.getDepartment());
            pstmt.executeUpdate();

            // Insert prerequisites into the Prerequisite table
            if (course.getPrerequisites() != null && !course.getPrerequisites().isEmpty()) {
                String prereqSql = "INSERT INTO Prerequisite(courseId, prerequisiteId) VALUES(?, ?)";
                try (PreparedStatement prereqStmt = conn.prepareStatement(prereqSql)) {
                    for (String prereq : course.getPrerequisites()) {
                        prereqStmt.setString(1, course.getCourseId());
                        prereqStmt.setString(2, prereq);
                        prereqStmt.executeUpdate();
                    }
                }
            }
        }
    }

    
    
    
    /**
     * Retrieves all courses from the Course table in the SQLite database.
     * For each course, the corresponding prerequisites are retrieved from the Prerequisite table.
     * 
     * Time complexity: O(n + m), where n is the number of courses and m is the total number of prerequisites for all courses.
     */
    public List<Course> getAllCourses() throws SQLException {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM Course";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String courseId = rs.getString("courseId");
                String courseName = rs.getString("courseName");
                int credits = rs.getInt("credits");
                String department = rs.getString("department");

                // Retrieve prerequisites for the course
                List<String> prerequisites = getPrerequisitesByCourseId(courseId, conn);

                // Create Course object and add to the list
                Course course = new Course(courseId, courseName, credits, department, prerequisites);
                courses.add(course);
            }
        }
        return courses;
    }

    
    
    
    
    /**
     * Retrieves the list of prerequisites for a specific course from the Prerequisite table.
     * 
     * Time complexity: O(m), where m is the number of prerequisites for the course.
     */
    private List<String> getPrerequisitesByCourseId(String courseId, Connection conn) throws SQLException {
        List<String> prerequisites = new ArrayList<>();
        String prereqSql = "SELECT prerequisiteId FROM Prerequisite WHERE courseId = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(prereqSql)) {
            pstmt.setString(1, courseId);
            ResultSet rs = pstmt.executeQuery();

            // Add each prerequisite to the list
            while (rs.next()) {
                prerequisites.add(rs.getString("prerequisiteId"));
            }
        }
        return prerequisites;
    }

    
    
    
    
    /**
     * Finds and returns a course by its course ID from the Course table.
     * Retrieves course details and prerequisites if they exist.
     * 
     * Time complexity: O(1) for the course lookup and O(m) for retrieving prerequisites, where m is the number of prerequisites.
     * 
     * @param courseId The ID of the course to be found.
     * @return A Course object if found, otherwise null.
     * @throws SQLException if an SQL error occurs during retrieval.
     */
    public Course findCourseById(String courseId) throws SQLException {
        String sql = "SELECT * FROM Course WHERE courseId = ?";
        Course course = null;

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String courseName = rs.getString("courseName");
                int credits = rs.getInt("credits");
                String department = rs.getString("department");

                // Retrieve prerequisites
                List<String> prerequisites = getPrerequisitesByCourseId(courseId, conn);

                course = new Course(courseId, courseName, credits, department, prerequisites);
            }
        }
        return course;
    }

    
    
    
    /**
     * Updates an existing course in the Course table and its prerequisites in the Prerequisite table.
     * Deletes old prerequisites and inserts the new ones.
     * 
     * Time complexity: O(1) for updating the course and O(m) for updating prerequisites, where m is the number of prerequisites.
     */
    public void updateCourse(Course course) throws SQLException {
        String sql = "UPDATE Course SET courseName = ?, credits = ?, department = ? WHERE courseId = ?";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Update course details
            pstmt.setString(1, course.getCourseName());
            pstmt.setInt(2, course.getCredits());
            pstmt.setString(3, course.getDepartment());
            pstmt.setString(4, course.getCourseId());
            pstmt.executeUpdate();

            // Update prerequisites
            deletePrerequisites(course.getCourseId(), conn); // Delete old prerequisites
            insertPrerequisites(course, conn);  // Insert new prerequisites
        }
    }

    
    
    
    /**
     * Deletes all prerequisites for a specific course from the Prerequisite table.
     * 
     * Time complexity: O(m), where m is the number of prerequisites for the course.
     */
    private void deletePrerequisites(String courseId, Connection conn) throws SQLException {
        String sql = "DELETE FROM Prerequisite WHERE courseId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseId);
            pstmt.executeUpdate();
        }
    }

    
    
    
    /**
     * Inserts new prerequisites for a course into the Prerequisite table.
     * 
     * Time complexity: O(m), where m is the number of prerequisites being inserted.
     */
    private void insertPrerequisites(Course course, Connection conn) throws SQLException {
        String prereqSql = "INSERT INTO Prerequisite(courseId, prerequisiteId) VALUES(?, ?)";
        if (course.getPrerequisites() != null && !course.getPrerequisites().isEmpty()) {
            try (PreparedStatement pstmt = conn.prepareStatement(prereqSql)) {
                for (String prereq : course.getPrerequisites()) {
                    pstmt.setString(1, course.getCourseId());
                    pstmt.setString(2, prereq);
                    pstmt.executeUpdate();
                }
            }
        }
    }

    
    
    
    /**
     * Removes a course and its associated prerequisites from the database.
     * 
     * Time complexity: O(1) for removing the course and O(m) for deleting prerequisites, where m is the number of prerequisites.
     */
    public void removeCourse(String courseId) throws SQLException {
        String sqlCourse = "DELETE FROM Course WHERE courseId = ?";
        String sqlPrereq = "DELETE FROM Prerequisite WHERE courseId = ?";

        try (Connection conn = DatabaseManager.connect()) {
            // First, delete associated prerequisites
            try (PreparedStatement pstmt = conn.prepareStatement(sqlPrereq)) {
                pstmt.setString(1, courseId);
                pstmt.executeUpdate();
            }

            // Then, delete the course itself
            try (PreparedStatement pstmt = conn.prepareStatement(sqlCourse)) {
	            pstmt.setString(1, courseId);
	            pstmt.executeUpdate();
	        }
	    }
	}


    
    
    
}