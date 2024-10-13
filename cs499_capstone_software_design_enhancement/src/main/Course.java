package main;

import java.util.ArrayList;
import java.util.List;

//Class to represent a Course
public class Course {
	String courseId;
    String title;
    List<String> prerequisite;

    // Constructor
    public Course(String courseId, String title) {
        this.courseId = courseId;
        this.title = title;
        this.prerequisite = new ArrayList<>();
    }
}
