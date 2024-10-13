/**
 * AdvisingProgram.java
 * 
 * Programmer: Jeffrey Karl
 * Contact: jkarl2484@gmail.com
 * Date: 9/28/2024
 * Version: version 1.0
 * 
 * Purpose: 
 * This program is designed to assist students with academic course planning.
 * It reads a list of courses from a file, stores them in an ArrayList, 
 * and allows users to search for specific courses by their course ID. 
 * The program also provides a menu for users to load and display all courses.
 * 
 * Intent:
 * The ArrayList was chosen for its efficiency in random access (O(1)) and constant-time insertion
 * at the end (O(1)), while maintaining sorted order to improve search efficiency. By inserting each
 * course in the correct position using `Collections.binarySearch()`, the program ensures that the 
 * ArrayList remains sorted, allowing for faster search times (O(log n)) with binary search. This approach
 * strikes a balance between simplicity, efficient random access, and improved search performance.
 * 
 * Known Issues:
 * Inserting into a sorted ArrayList has a time complexity of O(n) due to shifting elements, but this
 * is acceptable given the project’s requirements. For very large datasets, other structures like
 * balanced trees or hash maps may be considered to further improve efficiency.
 */



package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Scanner;

public class AdvisingProgram {
	
	public static void main(String[] args) {
        // Assuming the input CSV file is passed as an argument
        String filePath = "advising_program_input.csv";
        String courseKey = "MATH201";
        if (args.length > 0) {
            filePath = args[0];
        }
        if (args.length > 1) {
            courseKey = args[1];
        }

        // Initialize the ArrayList to store courses, which will remain sorted for better search performance
        ArrayList<Course> courseList = new ArrayList<>();

        // Menu-driven program
        Scanner sc = new Scanner(System.in);
        int choice = 0;
        while (choice != 9) {
            System.out.println();
            System.out.println("Menu:");
            System.out.println("1. Load Courses");
            System.out.println("2. Display All Courses");
            System.out.println("3. Find Course");
            System.out.println("9. Exit");
            System.out.print("Enter choice: ");
            
            // Try to read an integer input
            try {
                choice = sc.nextInt();
                
                switch (choice) {
                    case 1:
                        // Load courses from a file and insert them into the ArrayList, keeping it sorted
                        loadCourses(filePath, courseList);
                        break;
                    case 2:
                        // Display all the courses (in sorted order due to ArrayList sorting)
                        System.out.println();
                        System.out.println("Here is a sample schedule:");
                        for (Course course : courseList) {
                            System.out.println("Course ID: " + course.courseId + ", Title: " + course.title);
                        }
                        break;
                    case 3:
                        // Search for a course by its course ID using a linear search (O(n)) for simplicity
                        System.out.println();
                        System.out.println("Please Enter The Course ID: ");
                        courseKey = sc.next();
                        boolean foundCourse = false;
                        for (Course course : courseList) {
                            if (course.courseId.equals(courseKey)) {
                                displayCourse(course); // Display the found course
                                foundCourse = true;
                                break;
                            }
                        }
                        
                        // Handle course not found scenario
                        if (!foundCourse) {
                            System.out.println();
                            System.out.println("Course Id " + courseKey + " not found.");
                        }
                        break;
                    case 9:
                        System.out.println("Goodbye!");
                        break;
                    default:
                        System.out.println(choice + " is not a valid option.");
                        break;
                }
            } catch (InputMismatchException e) {
                // Handle invalid input
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine();  // Clear the invalid input from the scanner
            }
        }
        
        sc.close();
    }
	
	
	// Method to load courses from a file and insert them into the ArrayList in sorted order
    // Time Complexity: O(n log n) because of binary search for finding insertion point (O(log n))
    // and shifting elements (O(n)) when inserting
    private static void loadCourses(String filePath, ArrayList<Course> courseList) {
        // Use class loader to access the file from the resources folder
        InputStream inputStream = AdvisingProgram.class.getClassLoader().getResourceAsStream(filePath);

        // Check if the resource was found
        if (inputStream == null) {
            System.err.println("Error: File not found in resources folder!");
            return;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            
            // Read the file line by line
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                Course course = new Course(fields[0], fields[1]);
                
                // Add prerequisites to the course
                for (int i = 2; i < fields.length; i++) {
                    course.prerequisite.add(fields[i]);
                }
                // Insert course in sorted order using binary search
                int index = Collections.binarySearch(courseList, course, (c1, c2) -> c1.courseId.compareTo(c2.courseId));
                if (index < 0) {
                    index = -(index + 1); // Calculate insertion point if course not found
                }
                courseList.add(index, course); // Insert at the correct sorted position
            }
            System.out.println();
            System.out.println("Courses loaded successfully.");
            
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }
	
	
	// Display the course information
    // Time Complexity: O(1) for displaying a single course
    public static void displayCourse(Course course) {
        System.out.println();
        // Print course ID and title
        System.out.println(course.courseId + ", " + course.title);

        // Print prerequisites
        System.out.print("Prerequisites: ");
        for (int i = 0; i < course.prerequisite.size(); i++) {
            System.out.print(course.prerequisite.get(i));
            // Add a comma and space if there are more prerequisites
            if ((i + 1) < course.prerequisite.size()) {
                System.out.print(", ");
            }
        }
        System.out.println();  // Move to a new line after printing prerequisites
    }


}
