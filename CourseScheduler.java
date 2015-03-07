package coursescheduler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
    static HashMap<Integer, Boolean> mCompletedCourseMap = new HashMap<Integer, Boolean>();
    static HashMap<Integer, Integer> mPrerequisitesMap = new HashMap<Integer, Integer>();
    static ArrayList<Course> interestingCourseList = new ArrayList<Course>();
    static int mInterestingCourseCounter = 0;
    
    
    static int mCmax = 50;
    public static void main(String args[])
    	throws IOException
    {
        File file = new File("firstScenario.txt");
        BufferedReader br =
                new BufferedReader(new FileReader(file));
        Student student = new Student();
        String firstLine = br.readLine();
        student.setMinCreditPerSem(Integer.parseInt(firstLine.split(" ")[1]));
        student.setMaxCreditPerSem(Integer.parseInt(firstLine.split(" ")[2]));
        int numOfCourses = Integer.parseInt(firstLine.split(" ")[0]);
        Set<Course> allCourses = new HashSet<Course>();
        Course[] courseArray = new Course[numOfCourses + 1];
        for(int i = 1 ; i <= numOfCourses ; i ++)
        {
            Course course = new Course();
            String courseLine = br.readLine();
            course.setFallCost(Integer.parseInt(courseLine.split(" ")[0]));
            course.setSpringCost(Integer.parseInt(courseLine.split(" ")[1]));
            course.setCreditHours(Integer.parseInt(courseLine.split(" ")[2]));
            course.setCourseNumber(i);
            courseArray[i] = course;
            allCourses.add(course);
        }
        //student.setAllCourses(allCourses);
        for(int i = 1; i <= numOfCourses ; i ++)
        {
            String prereqLine = br.readLine();
            if("0".equals(prereqLine)) continue;
            String[] prereqs = prereqLine.split(" ");
            Set<Course> prereqSet = courseArray[i].getPrerequistes();
            if(prereqSet == null) prereqSet = new HashSet<Course>();
            
            for(int j = 1 ; j < prereqs.length ; j ++)
            {
                Course prereq = courseArray[Integer.parseInt(prereqs[j])];
                if(mPrerequisitesMap.containsKey(prereq.getCourseNumber())) {
                    mPrerequisitesMap.put(prereq.getCourseNumber(), mPrerequisitesMap.get(prereq.getCourseNumber()) + 1);
                }
                else {
                    mPrerequisitesMap.put(prereq.getCourseNumber(), 1);
                }
                prereqSet.add(prereq);
            }
            courseArray[i].setPrerequistes(prereqSet);
        }
        student.allCourses = allCourses;
        String[] interstingCoursesStrArr = br.readLine().split(" ");
        student.interestingCourses = new HashSet<Course>();
        for(String interestingCourseStr : interstingCoursesStrArr)
        {
            Course course = courseArray[Integer.parseInt(interestingCourseStr)];
            student.interestingCourses.add(course);
            course.setInteresting(true);
        }
        student.setMaxCost(Integer.parseInt(br.readLine()));
        student.setCurrSemester(semesterType.FALL);
        //System.out.println(student);
        int semesterCounter = 0;
        while(mInterestingCourseCounter < interstingCoursesStrArr.length) {
            if(semesterCounter % 2 == 0) {
                student.setCurrSemester(semesterType.FALL);
            }
            else {
                student.setCurrSemester(semesterType.SPRING);
            }
            semesterCounter++;
            Set<Course> CourseSet = chooseInterestingCourse(allCourses);
            for(Course course : CourseSet) {
                mCompletedCourseMap.put(course.getCourseNumber(), true);
                System.out.println("Course Number : " + course.getCourseNumber() + "    Credit hours :"+ course.getCreditHours());
            }
            System.out.println("----------------------------");
        }
    }
    
    static private Set<Course> chooseInterestingCourse(Set<Course> courses) {
        for(Course course: courses){
            if(course.isInteresting()){
                interestingCourseList.add(course);
            }
        }
        
        
         Set<Course> CourseSet = new HashSet<Course>();
        int creditHours = 0;
        Integer key = 0, value = 0;
        mPrerequisitesMap = sortByValue(mPrerequisitesMap);
        for(Course course : courses) {
           if( (course.isInteresting() && course.getPrerequistes() == null ) || (course.isInteresting() && checkIfPrerequisitesCompleted(course)) ){
               if(creditHours >= mCmax) {
                   return CourseSet;
               }
               if(!mCompletedCourseMap.containsKey(course.getCourseNumber())){
                   if( (creditHours + course.getCreditHours()) <= mCmax ) {
                        creditHours += course.getCreditHours();
                        mInterestingCourseCounter++;
                        //mCompletedCourseMap.put(course.getCourseNumber(), true);
                        CourseSet.add(course);
                        int counter = 0;
                        if(mCompletedCourseMap.size() >= interestingCourseList.size()){
                            for (Map.Entry<Integer, Integer> entry : mPrerequisitesMap.entrySet()) {
                                key = entry.getKey();
                                value = entry.getValue();
                                for(Course c : interestingCourseList){
                                    if(c.getCourseNumber() == key){
                                        counter++;
                                        if(counter == interestingCourseList.size()){
                                            return CourseSet;
                                        }
                                    }
                                }
                            }
                        }
                   }
                }
            }
        }
        

        for (Map.Entry<Integer, Integer> entry : mPrerequisitesMap.entrySet()) {
            key = entry.getKey();
            value = entry.getValue();
            if(creditHours < mCmax){
                if(!mCompletedCourseMap.containsKey(key)){
                    for(Course c : courses){
                        if(c.getCourseNumber() == key &&  ((creditHours + c.getCreditHours()) <= mCmax) && checkIfPrerequisitesCompleted(c) ){
                            creditHours+=c.getCreditHours();
                           // mCompletedCourseMap.put(key, Boolean.TRUE);
                            CourseSet.add(c);
                            int counter = 0;
                            if(mCompletedCourseMap.size() >= interestingCourseList.size()){
                                for (Map.Entry<Integer, Integer> prereqEntry : mPrerequisitesMap.entrySet()) {
                                    key = prereqEntry.getKey();
                                    value = prereqEntry.getValue();
                                    for(Course cInt : interestingCourseList){
                                        if(cInt.getCourseNumber() == key){
                                            counter++;
                                            if(counter == interestingCourseList.size()){
                                                return CourseSet;
                                            }
                                        }
                                    }

                                }
                           }
                        }
                    }
                }


            }
            else {
                return CourseSet;
            }

        }
        
        for(Course remainingCourse : courses){
            if(!remainingCourse.isInteresting() &&  !mCompletedCourseMap.containsKey(remainingCourse.getCourseNumber()) && checkIfPrerequisitesCompleted(remainingCourse)){
                if( (creditHours + remainingCourse.getCreditHours()) <= mCmax ) {
                    creditHours+=remainingCourse.getCreditHours();
                    //mCompletedCourseMap.put(remainingCourse.getCourseNumber(), Boolean.TRUE);
                    CourseSet.add(remainingCourse);
                }

            }
//            else {
//                return CourseSet;
//            }
        }
        
        return CourseSet;
    }
    
    static HashMap sortByValue(Map map) {
     List list = new LinkedList(map.entrySet());
     Collections.sort(list, new Comparator() {
          public int compare(Object o1, Object o2) {
               return ((Comparable) ((Map.Entry) (o1)).getValue())
              .compareTo(((Map.Entry) (o2)).getValue());
          }
     });

    HashMap result = new LinkedHashMap();
    for (Iterator it = list.iterator(); it.hasNext();) {
        Map.Entry entry = (Map.Entry)it.next();
        result.put(entry.getKey(), entry.getValue());
    }
    return result;
} 
    
    static private boolean checkIfPrerequisitesCompleted(Course course) {
        //System.out.println("inside Check prereq : ");
        //System.out.println    (course.getCourseNumber());
        if(course.getPrerequistes() != null){
            for(Course prerequisite : course.getPrerequistes()) {

                if(!mCompletedCourseMap.containsKey(prerequisite.getCourseNumber()) ) {
                    return false;
                }
            }
        }
        return true;
    }
    
    static boolean chooseSemester(Course course, semesterType type) {
        int currentPrice = 0;
        int nextSemPrice = 0;
        
        switch(type) {
            case SPRING:
                currentPrice = course.getSpringCost();
                nextSemPrice = course.getFallCost();
            break;
            
            case FALL:
                currentPrice = course.getFallCost();
                nextSemPrice = course.getSpringCost();
            break;
                
        }
        
        return currentPrice < nextSemPrice;
    }
}
