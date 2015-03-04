package coursescheduler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author panindra
 */
public class CourseScheduler {
    public static void main(String args[])
    	throws IOException
    {
		File file = new File(args[0]);
		BufferedReader br =
			new BufferedReader(new FileReader(file));
		Student student = new Student();
		String firstLine = br.readLine();
		student.setMinCreditPerSem(Integer.parseInt(firstLine.split(" ")[1]));
		student.setMaxCreditPerSem(Integer.parseInt(firstLine.split(" ")[2]));
		int numOfCourses = Integer.parseInt(firstLine.split(" ")[0]);
		Set<Course> allCourses = new HashSet<Course>();
		Course[] courseArray = new Course[numOfCourses];
		for(int i = 0 ; i < numOfCourses ; i ++)
		{
			Course course = new Course();
			String courseLine = br.readLine();
			course.setFallCost(Integer.parseInt(courseLine.split(" ")[0]));
			course.setSpringCost(Integer.parseInt(courseLine.split(" ")[1]));
			course.setCreditHours(Integer.parseInt(courseLine.split(" ")[2]));
			courseArray[i] = course;
			allCourses.add(course);
		}
		//student.setAllCourses(allCourses);
		for(int i = 0 ; i < numOfCourses ; i ++)
		{
			String prereqLine = br.readLine();
			if("0".equals(prereqLine)) continue;
			String[] prereqs = prereqLine.split(" ");
			Set<Course> prereqSet = courseArray[i].getPrerequistes();
			if(prereqSet == null) prereqSet = new HashSet<Course>();
			for(int j = 1 ; j < prereqs.length ; j ++)
			{
				Course prereq = courseArray[Integer.parseInt(prereqs[j]) - 1];
				prereqSet.add(prereq);
			}
			courseArray[i].setPrerequistes(prereqSet);
		}
		String[] interstingCoursesStrArr = br.readLine().split(" ");
		student.interestingCourses = new HashSet<Course>();
		for(String interestingCourseStr : interstingCoursesStrArr)
		{
			student.interestingCourses.add(courseArray[Integer.parseInt(interestingCourseStr)]);			
		}
		student.setMaxCost(Integer.parseInt(br.readLine()));
		
		System.out.println(student);
    }
    
    private Course chooseInterestingCourse(ArrayList<Course> courses) {
        Course course = null;
        for(int i = 0; i < courses.size(); i++) {
           if(course.isInteresting() && (course.getPrerequistes().size() == 0 /*|| course.isPrerequisitesFullfilled()*/ ) ) {
               if(course.getFallCost() > course.getSpringCost()) {
                   
               }
           }
        }
        return course;
    }
    
    
    
}
