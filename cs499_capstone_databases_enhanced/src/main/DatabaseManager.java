package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DatabaseManager class for handling database connections and table creation in SQLite.
 * This class provides methods to connect to the SQLite database and create the necessary
 * tables for storing course information and prerequisites.
 */
public class DatabaseManager {

    // URL for the SQLite database file. The database will be created if it doesn't exist.
    private static final String URL = "jdbc:sqlite:advisingProgram.db";

    /**
     * Establishes a connection to the SQLite database.
     * 
     * Time complexity: O(1) since establishing a connection is constant time.
     * 
     * @return A Connection object if the connection is successful, or null if an error occurs.
     */
    public static Connection connect() {
        Connection conn = null;
        try {
            // Establish a connection to the SQLite database
            conn = DriverManager.getConnection(URL);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            // Print error message if connection fails
            System.out.println(e.getMessage());
        }
        return conn;
    }

    /**
     * Creates the necessary tables in the SQLite database if they don't already exist.
     * The Course table stores course details, and the Prerequisite table stores prerequisite relationships.
     * 
     * Time complexity: O(1) as table creation is constant time, provided the tables don't already exist.
     * 
     * The Course table:
     * - courseId (TEXT): The primary key that uniquely identifies each course.
     * - courseName (TEXT): The name of the course.
     * - credits (INTEGER): The number of credits the course is worth.
     * - department (TEXT): The department offering the course.
     * 
     * The Prerequisite table:
     * - courseId (TEXT): A foreign key that refers to the course in the Course table.
     * - prerequisiteId (TEXT): The course ID of the prerequisite course.
     * 
     * Both tables are created with foreign key constraints to ensure data integrity.
     */
    public static void createCourseTable() {
        // SQL statement to create the Course table
        String courseTable = "CREATE TABLE IF NOT EXISTS Course ("
                + " courseId TEXT PRIMARY KEY,"  // Primary key for unique course identification
                + " courseName TEXT NOT NULL,"   // Course name, required field
                + " credits INTEGER NOT NULL,"   // Number of credits for the course
                + " department TEXT NOT NULL"    // Department offering the course
                + ");";

        // SQL statement to create the Prerequisite table
        String prerequisiteTable = "CREATE TABLE IF NOT EXISTS Prerequisite ("
                + " courseId TEXT NOT NULL,"  // Foreign key referencing courseId in Course table
                + " prerequisiteId TEXT NOT NULL,"  // Prerequisite course ID
                + " FOREIGN KEY(courseId) REFERENCES Course(courseId)"  // Foreign key constraint
                + ");";

        // Execute the SQL statements to create the tables
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(courseTable);  // Create the Course table
            stmt.execute(prerequisiteTable);  // Create the Prerequisite table
            System.out.println("Course and Prerequisite tables created.");
        } catch (SQLException e) {
            // Handle SQL exceptions (such as syntax errors or connection issues)
            System.out.println(e.getMessage());
        }
    }

}