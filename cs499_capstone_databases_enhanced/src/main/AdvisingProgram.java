/**
 * AdvisingProgram.java
 * 
 * Programmer: Jeffrey Karl
 * Contact: jkarl2484@gmail.com
 * Date: 10/06/2024
 * Version: version 1.1
 * 
 * Purpose: 
 * This program is designed to assist students with academic course planning.
 * It uses an SQLite database to store and manage course information, including
 * course details and prerequisites. The program allows users to perform CRUD 
 * (Create, Read, Update, Delete) operations on the courses, search for specific 
 * courses by their course ID, and import courses from a CSV file.
 * 
 * Intent: 
 * The decision to transition from an in-memory data structure to 
 * an SQLite database was made to allow for persistent storage and improved 
 * scalability. The SQLite database ensures that courses and prerequisites 
 * are stored across sessions, supporting more complex queries and operations. 
 * This also makes the program more suitable for larger datasets as opposed to 
 * the previous ArrayList implementation.
 * 
 * Time Complexity: 
 * - The CRUD operations (Create, Read, Update, Delete) generally operate in O(n)
 *   where n is the number of courses. This is typical for database operations when 
 *   the database size increases. Optimizing query indexes in SQLite could improve 
 *   performance.
 * - Searching for a course by ID is efficient due to the primary key constraints 
 *   in the database (O(1) in the best case).
 * - Importing courses from a CSV involves reading each line and splitting the values, 
 *   which operates in O(n), where n is the number of lines in the CSV file.
 * 
 * New Features:
 * - Version 1.1:
 *   - Integrated SQLite database for persistent storage of course information.
 *   - Added functionality to create, read, update, and delete courses from the database.
 *   - Added support for multiple prerequisites using a separate table in the database.
 *   - Implemented CSV import functionality, allowing users to load course data 
 *     (with prerequisites) from external files.
 * 
 * Known Issues:
 * - The program assumes that the CSV file format is correct; input validation 
 *   for the file could be further improved.
 * - Error handling around database operations could be expanded.
 * 
 * Modifications:
 * - Version 1.0: Initial implementation using ArrayList for in-memory storage of courses.
 * - Version 1.1: Transitioned to SQLite database for persistent storage and implemented 
 *   additional course management functionality (CRUD operations).
 * 
 */




package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class AdvisingProgram {
	
	/**
     * Main method that serves as the entry point of the program.
     * Provides a menu for users to select actions like adding, listing,
     * importing, updating, finding, or removing courses.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CourseDAO courseDAO = new CourseDAO();

        // Create the necessary tables in the SQLite database
        DatabaseManager.createCourseTable();

        boolean running = true;

        // Main loop for interacting with the program
        while (running) {
            System.out.println("\nAdvising Program");
            System.out.println("1. Add Course");
            System.out.println("2. List Courses");
            System.out.println("3. Import Courses from CSV");
            System.out.println("4. Find Course");
            System.out.println("5. Update Course");
            System.out.println("6. Remove Course");
            System.out.println("7. Exit");

            System.out.print("Choose an option: ");
            
            try {
                int option = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                
                switch (option) {
                    case 1:
                        addCourse(scanner, courseDAO); // Add a new course to the database
                        break;
                    case 2:
                        listCourses(courseDAO); // List all courses in the database
                        break;
                    case 3:
                        System.out.print("Enter CSV file path: ");
                        String csvFilePath = scanner.nextLine();
                        try {
                            importCoursesFromCSV(csvFilePath, courseDAO); // Import courses from CSV
                            System.out.println("Courses imported successfully.");
                        } catch (IOException | SQLException e) {
                            System.out.println("Error importing courses: " + e.getMessage());
                        }
                        break;
                    case 4:
                        findCourse(scanner, courseDAO); // Find and display course details
                        break;
                    case 5:
                        updateCourse(scanner, courseDAO); // Update a course's details
                        break;
                    case 6:
                        removeCourse(scanner, courseDAO); // Remove a course from the database
                        break;
                    case 7:
                        running = false;
                        System.out.println("Goodbye.");
                        break;
                    default:
                        System.out.println("Invalid option. Please choose again.");
                }
            } catch (InputMismatchException e) {
                // Handle invalid input (non-integer input for the menu)
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();  // Clear the invalid input from the scanner
            }
        }
        scanner.close();
    }

    
    
    /**
     * Adds a new course to the database based on user input.
     * Time complexity: O(1) for inserting a single course in SQLite.
     */
    private static void addCourse(Scanner scanner, CourseDAO courseDAO) {
        System.out.print("Enter Course ID: ");
        String courseId = scanner.nextLine();
        System.out.print("Enter Course Name: ");
        String courseName = scanner.nextLine();
        System.out.print("Enter Course Credits: ");
        int credits = scanner.nextInt();
        scanner.nextLine();  // Consume newline
        System.out.print("Enter Department: ");
        String department = scanner.nextLine();
        System.out.print("Enter Prerequisites (separated by semicolons or None): ");
        String prerequisitesInput = scanner.nextLine();
        
        List<String> prerequisites = new ArrayList<>();
        if (!prerequisitesInput.equalsIgnoreCase("None")) {
            String[] prereqArray = prerequisitesInput.split(";");
            prerequisites = Arrays.asList(prereqArray); // Splitting prerequisites
        }

        Course course = new Course(courseId, courseName, credits, department, prerequisites);

        try {
            // Insert the course into the database
            courseDAO.insertCourse(course);
            System.out.println("Course added successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding course: " + e.getMessage());
        }
    }
	
	
	
    
    /**
     * Finds a course in the database based on the course ID provided by the user.
     * Time complexity: O(1) as the query leverages the primary key (courseId) in SQLite.
     */
    private static void findCourse(Scanner scanner, CourseDAO courseDAO) {
        System.out.print("Enter Course ID: ");
        String courseId = scanner.nextLine();

        try {
            Course course = courseDAO.findCourseById(courseId);
            if (course != null) {
                System.out.println(course);  // Print the course details
            } else {
                System.out.println("Course not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error finding course: " + e.getMessage());
        }
    }
	
	
	
	
    /**
     * Updates an existing course in the database based on user input.
     * Time complexity: O(1) for updating the course, and O(m) for updating prerequisites, where m is the number of prerequisites.
     */
    private static void updateCourse(Scanner scanner, CourseDAO courseDAO) {
        System.out.print("Enter Course ID: ");
        String courseId = scanner.nextLine();

        try {
            Course existingCourse = courseDAO.findCourseById(courseId);  // Find the course first
            if (existingCourse != null) {
                System.out.println("Updating course: " + existingCourse);
                System.out.print("Enter New Course Name: ");
                String courseName = scanner.nextLine();
                System.out.print("Enter New Course Credits: ");
                int credits = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                System.out.print("Enter New Department: ");
                String department = scanner.nextLine();
                System.out.print("Enter New Prerequisites (separated by semicolons or None): ");
                String prerequisitesInput = scanner.nextLine();
                
                List<String> prerequisites = new ArrayList<>();
                if (!prerequisitesInput.equalsIgnoreCase("None")) {
                    String[] prereqArray = prerequisitesInput.split(";");
                    prerequisites = Arrays.asList(prereqArray);
                }

                Course updatedCourse = new Course(courseId, courseName, credits, department, prerequisites);
                courseDAO.updateCourse(updatedCourse);  // Update the course in the database
                System.out.println("Course updated successfully.");
            } else {
                System.out.println("Course not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating course: " + e.getMessage());
        }
    }
	
	
	
	
    /**
     * Removes a course from the database based on the course ID provided by the user.
     * Time complexity: O(1) for removing the course, as the course ID is a primary key.
     */
    private static void removeCourse(Scanner scanner, CourseDAO courseDAO) {
        System.out.print("Enter Course ID to remove: ");
        String courseId = scanner.nextLine();

        try {
            Course course = courseDAO.findCourseById(courseId);  // Find the course to confirm existence
            if (course != null) {
                courseDAO.removeCourse(courseId);  // Remove the course from the database
                System.out.println("Course removed successfully.");
            } else {
                System.out.println("Course not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error removing course: " + e.getMessage());
        }
    }


	
	
	
	
	
	
    /**
     * Displays all courses stored in the SQLite database.
     * This method retrieves all courses using the CourseDAO class 
     * and prints them to the console.
     * 
     * Time complexity: O(n), where n is the number of courses in the database.
     * The database query retrieves all rows from the Course table, and 
     * each course is printed in the console using a for-each loop.
     */
    private static void listCourses(CourseDAO courseDAO) {
        try {
            System.out.println("Course List:");
            // Fetch all courses and iterate over the list
            for (Course c : courseDAO.getAllCourses()) {
                System.out.println(c);  // Display each course
            }
        } catch (SQLException e) {
            // Catch and print any SQL errors that occur while retrieving courses
            System.out.println("Error retrieving courses: " + e.getMessage());
        }
    }

    
    
    
    
    /**
     * Imports courses from a CSV file and adds them to the SQLite database.
     * Each line in the CSV represents a course and its related information.
     * The CSV is expected to have 5 values (course ID, course name, credits, department, prerequisites).
     * 
     * Time complexity: O(n), where n is the number of lines in the CSV file. 
     * For each line, the CSV data is parsed and processed before being inserted into the database.
     */
    private static void importCoursesFromCSV(String csvFilePath, CourseDAO courseDAO) throws IOException, SQLException {
        // Load the CSV file from the resources folder
        InputStream inputStream = AdvisingProgram.class.getClassLoader().getResourceAsStream(csvFilePath);

        // Check if the resource was found in the classpath
        if (inputStream == null) {
            System.err.println("Error: File not found in resources folder!");
            return;
        }

        // Use BufferedReader to read the CSV file line by line
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;

            // Process each line of the CSV file
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");  // Split the line into fields by commas

                if (values.length == 5) {  // Expect 5 values: courseId, courseName, credits, department, prerequisites
                    String courseId = values[0];
                    String courseName = values[1];
                    try {
                        // Parse the credits field as an integer
                        int credits = Integer.parseInt(values[2]);
                        String department = values[3];

                        // Process prerequisites
                        List<String> prerequisites = new ArrayList<>();
                        if (!values[4].equalsIgnoreCase("None")) {
                            String[] prereqsArray = values[4].split(";");
                            prerequisites = Arrays.asList(prereqsArray);  // Split prerequisites by semicolons
                        }

                        // Create a new Course object with the parsed data
                        Course course = new Course(courseId, courseName, credits, department, prerequisites);

                        // Insert the course into the database
                        courseDAO.insertCourse(course);
                    } catch (NumberFormatException e) {
                        // Catch and handle the error if the credits field cannot be parsed as an integer
                        System.out.println("Error parsing credits for course: " + courseId + ". Skipping entry.");
                    } catch (SQLException e) {
                        // Handle any SQL errors during course insertion
                        System.out.println("Error inserting course: " + courseId + " into database: " + e.getMessage());
                    }
                } else {
                    // Print an error message for improperly formatted lines
                    System.out.println("Invalid line format: " + line);
                }
            }
            // Inform the user that the courses have been loaded successfully
            System.out.println();
            System.out.println("Courses loaded successfully.");

        } catch (IOException e) {
            // Handle any file reading errors
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }



}
