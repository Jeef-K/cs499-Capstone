/**
 * AdvisingProgram.java
 * 
 * Programmer: Jeffrey Karl
 * Contact: jkarl2484@gmail.com
 * Date: 9/22/2024
 * Version: 1.0
 * 
 * Purpose:
 * This program assists students with academic course planning by reading a list of courses 
 * from a CSV file, storing them in a BinarySearchTree, and allowing users to search for specific 
 * courses by their course ID. The program also provides a user menu to load and display all courses.
 * 
 * Intent:
 * The decision to convert this program from C++ to Java was made to leverage Java's portability and 
 * ease of integration with modern development environments. Java provides built-in libraries for 
 * tasks such as file handling and input/output, making the program more scalable and maintainable. 
 * Additionally, Java's object-oriented nature helped streamline the code structure, while its memory 
 * management (automatic garbage collection) reduced the need for manual memory handling compared to C++.
 * 
 * The conversion process provided valuable learning on language-specific differences such as memory 
 * management, exception handling, and using Java’s standard libraries to achieve the same functionality 
 * that was originally implemented in C++. By leveraging Java’s class libraries for handling input streams 
 * and collections, the program now benefits from error handling and portability across platforms.
 * 
 * Known Issues:
 * - The current implementation is not secure against invalid input or file formats; further input validation 
 *   should be added to ensure stability.
 * - The load method assumes the CSV format is consistent; future versions should account for potential inconsistencies.
 * 
 * Modifications:
 * - Version 1.0: Initial implementation with course search and display functionality converted from C++ to Java.
 * - Future versions may include improvements for input validation, error handling, and scalability for larger datasets.
 * 
 */

package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

        // Initialize the Binary Search Tree
        BinarySearchTree bst = new BinarySearchTree();

        // Load courses from a file and insert them into the tree
        //loadCourses(filePath, bst);

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
	                    loadCourses(filePath, bst);
	                    //System.out.println("Courses loaded successfully.");
	                    break;
	                case 2:
	                	System.out.println();
	                    System.out.println("Here is a sample schedule:");
	                    bst.inOrder();
	                    break;
	                case 3:
	                	System.out.println();
	                    System.out.println("Please Enter The Course ID: ");
	                    courseKey = sc.next();
	                    Course course = bst.search(courseKey.toUpperCase());
	                    
	                    if (course != null) {
	                        displayCourse(course);
	                    } else {
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
            } catch (InputMismatchException e){
            	// Handle invalid input
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine();  // Clear the invalid input from the scanner
            }
        }
        
        sc.close();
	}
	
	
	// Method to load courses from a file
	private static void loadCourses(String filePath, BinarySearchTree bst) {
	    // Use class loader to access the file from the resources folder
	    InputStream inputStream = AdvisingProgram.class.getClassLoader().getResourceAsStream(filePath);

	    // Check if the resource was found
	    if (inputStream == null) {
	        System.err.println("Error: File not found in resources folder!");
	        return;
	    }

	    try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
	        String line;
	        
	        while ((line = br.readLine()) != null) {
	            String[] fields = line.split(",");
	            Course course = new Course(fields[0], fields[1]);
	            
	            for (int i = 2; i < fields.length; i++) {
	                course.prerequisite.add(fields[i]);
	            }
	            
	            bst.insert(course);
	        }
	        System.out.println();
	        System.out.println("Courses loaded successfully.");
	        
	    } catch (IOException e) {
	        System.err.println("Error reading the file: " + e.getMessage());
	    }
	}
	
	
	// Display the course information to the console (System.out)
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
