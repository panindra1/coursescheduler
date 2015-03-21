/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coursescheduler1;

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
import java.util.PriorityQueue;
import java.util.Set;

/**
 *
 * @author panindra
 */

public class CourseScheduler1 {

    /**
     * @param args the command line arguments
     */
    static HashMap<Integer, Boolean> mCompletedCourseMap = new HashMap<Integer, Boolean>();
    static HashMap<Integer, Integer> mPrerequisitesMap = new HashMap<Integer, Integer>();
    static ArrayList<Integer> mInterestingCourseList = new ArrayList<>();
    static Set<Course> mAllCourses = new HashSet<Course>();
    static int mInterestingCourseCounter = 0;
    
    static PriorityQueue<Course> mFallPriorityQueue;
    static PriorityQueue<Course> mSpringPriorityQueue;
   
    static int mTotalCostOfCOurses = 0;    
    static int mCmax = 50;
    static int mCmin = 10;
    
    static boolean mInterestingCourseCompleted = false;
      
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
        
        Course[] courseArray = new Course[numOfCourses + 1];
        for(int i = 1 ; i <= numOfCourses ; i ++)
        {
            Course course = new Course();
            String courseLine = br.readLine();
            course.setFallCost(Integer.parseInt(courseLine.split(" ")[0]));
            course.setSpringCost(Integer.parseInt(courseLine.split(" ")[1]));
            course.setCreditHours(Integer.parseInt(courseLine.split(" ")[2]));
            course.setCourseNumber(i);
            course.setInteresting(false);
            course.setImpPreRequisite(false);
            courseArray[i] = course;
            mAllCourses.add(course);
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
                prereqSet.add(prereq);
                if(courseArray[i].isInteresting()){
                    prereq.setImpPreRequisite(true);
                }
            }
            courseArray[i].setPrerequistes(prereqSet);
        }
        
        student.allCourses = mAllCourses;
        String[] interstingCoursesStrArr = br.readLine().split(" ");
        student.interestingCourses = new HashSet<Course>();
        for(String interestingCourseStr : interstingCoursesStrArr)
        {
            if(interestingCourseStr == interstingCoursesStrArr[0])
                continue;
            Course course = courseArray[Integer.parseInt(interestingCourseStr)];
            student.interestingCourses.add(course);
            course.setInteresting(true);
            mInterestingCourseList.add(course.getCourseNumber());
        }
        student.setMaxCost(Integer.parseInt(br.readLine()));
        student.setCurrSemester(semesterType.FALL);
        //System.out.println(student);
        
        mFallPriorityQueue = new PriorityQueue<Course>(10, new Comparator<Course>(){
                public int compare(Course o1, Course o2) {
                    if(o1.getFallCost() > o2.getFallCost()) {
                        return 1;
                    }
                    else {
                        return -1;
                    }
                }
            });
        
        mSpringPriorityQueue = new PriorityQueue<Course>(10, new Comparator<Course>(){
                public int compare(Course o1, Course o2) {                
                    if(o1.getSpringCost()< o2.getSpringCost()) {
                        return 1;
                    }
                    else {
                        return -1;
                    }
                }
            });
        
        
        for(Course course : mAllCourses) {
            Set<Course> prereqSet = course.getPrerequistes();
            if(prereqSet != null) {
                for(Course prereq: prereqSet) {
                   // if(course.isInteresting()) {
                    if(mPrerequisitesMap.containsKey(prereq.getCourseNumber())) {
                        mPrerequisitesMap.put(prereq.getCourseNumber(), mPrerequisitesMap.get(prereq.getCourseNumber()) + 1);
                    }
                    else {
                        mPrerequisitesMap.put(prereq.getCourseNumber(), 1);
                    }
                    //}
                }
            }
        }
        
        mPrerequisitesMap = sortByValue(mPrerequisitesMap);
        CourseTreeNode root = new CourseTreeNode();
        
        int semesterCounter = 0;
       // while(mInterestingCourseCounter < student.interestingCourses.size()) {
            if(semesterCounter % 2 == 0) {
                student.setCurrSemester(semesterType.FALL);
            }
            else {
                student.setCurrSemester(semesterType.SPRING);
            }
            semesterCounter++;
            
            
            for (Course course : mAllCourses){
                if(course.isInteresting() && course.getPrerequistes() == null){
                    root.course = course;
                    root.parent = null;
                    root.child = new ArrayList<>();
                    root.setSemesterNumber(1);
                    break;
                }
            }
            recursiveBacktracking(root);            
       //}
    }
    
    static void recursiveBacktracking(CourseTreeNode root) {
        if(mInterestingCourseCompleted) {
            return;
        }
        root.finishedCourses.add(root.course.getCourseNumber());
        ArrayList<Course> childCourses = new ArrayList<>();
        childCourses = giveInterestingCourses(root, mAllCourses);
        System.out.println("CUrrent root :" + root.course.getCourseNumber() + " : Current semester : " +root.getSemesterNumber());
        
       /* for(int i = 0; i < childCourses.size(); i++) {
            System.out.println("CHildren of root :" + childCourses.get(i).getCourseNumber());
        }*/
        
        
        for(int i=0; i<childCourses.size(); i++){
            CourseTreeNode ctNode = new CourseTreeNode();            
            boolean checkAddCourse = checkToAddNode(root);
            
            if(!checkAddCourse) {
                ctNode.setSemesterNumber(root.getSemesterNumber());
                
                Set<Course> prereqSet = childCourses.get(i).getPrerequistes();
                CourseTreeNode checkRoot = new CourseTreeNode();
                checkRoot = root;
                boolean flag = false;
                int rootSemester = root.getSemesterNumber();
                
                while (checkRoot != null && checkRoot.getSemesterNumber() == rootSemester) {
                   if(prereqSet !=null && prereqSet.contains(checkRoot.course)) {
                       flag = true;
                       break;                       
                   }                   
                   checkRoot = checkRoot.parent;
                }   
                
                if(flag) {
                    continue;
                }
            }
            else {
                ctNode.setSemesterNumber(root.getSemesterNumber() + 1);
            }

            ctNode.course = childCourses.get(i);
            ctNode.child = new  ArrayList<>();
            ctNode.parent = root;
            ctNode.finishedCourses.addAll(root.finishedCourses);
            
            root.child.add(ctNode);            
            
        }
        
        
        for(int i = 0; i < root.child.size(); i++) {
            root = root.child.get(i);            
            if(checkIfInterestedCompelted(root.finishedCourses)) 
                mInterestingCourseCompleted = true;
            
            recursiveBacktracking(root);            
        }
    }
    
    static boolean checkToAddNode(CourseTreeNode root) {
        int parentSemester = root.getSemesterNumber();
        int creditHours = 0;//
                
        while(root.parent != null && root.parent.getSemesterNumber() == parentSemester) {
            creditHours += root.course.getCreditHours();
            root = root.parent;
        }
        
        if(creditHours > mCmin)
            return true;
        
        return false;
    }
    
    static boolean checkIfInterestedCompelted(ArrayList<Integer> finishedCourses) {
        int count = 0;
        for(int j = 0; j < mInterestingCourseList.size(); j++) {
            if(finishedCourses.contains(mInterestingCourseList.get(j))) {
                count++;
            }
        }
        if(count == mInterestingCourseList.size()) 
            return true;
        
        return false;
    }
    
    static ArrayList<Course> giveInterestingCourses(CourseTreeNode ctNode, Set <Course> allCourses ) {
        
        ArrayList<Course> nextCourseList = new ArrayList<>();
        for(Course course : allCourses){
            if(course.isInteresting() && !ctNode.finishedCourses.contains(course.getCourseNumber()) && checkIfPrerequisitesCompleted(course, ctNode.finishedCourses)){
                if(ctNode.totalCoursesCreditHrs + course.getCreditHours() <= mCmax){
                    nextCourseList.add(course);
                }
                else {
                    return nextCourseList;
                }
            }
        
        }
        
        Integer key, value;
        for (Map.Entry<Integer, Integer> entry : mPrerequisitesMap.entrySet()) {
            key = entry.getKey();
            value = entry.getValue();
          
            for(Course c : allCourses){
               // System.out.println(c.getCourseNumber());
                if(c.getCourseNumber() == key && !nextCourseList.contains(c) && !ctNode.finishedCourses.contains(c.getCourseNumber()) && checkIfPrerequisitesCompleted(c, ctNode.finishedCourses) ){
                    if(ctNode.totalCoursesCreditHrs + c.getCreditHours() <= mCmax){
                        nextCourseList.add(c);
                    }                           
                    else {
                    return nextCourseList;
                    }
                }
            }
        }
            
               
        for(Course course: allCourses){
            //System.out.println(course.getCourseNumber());
            if(checkIfPrerequisitesCompleted(course, ctNode.finishedCourses) &&  !ctNode.finishedCourses.contains(course.getCourseNumber()) && !nextCourseList.contains(course)){
                if(ctNode.totalCoursesCreditHrs + course.getCreditHours() <= mCmax){
                    nextCourseList.add(course);
                }
                else {
                    return nextCourseList;
                }
            }            
        }
        
        return nextCourseList;
    }
    
    static private boolean checkIfPrerequisitesCompleted(Course course, ArrayList<Integer> finishedList) {
        //System.out.println("inside Check prereq : ");
        //System.out.println    (course.getCourseNumber());
        Set<Course> prereqSet = course.getPrerequistes();
        if(prereqSet != null){
            for(Course prerequisite : prereqSet) {

                if(!finishedList.contains(prerequisite.getCourseNumber()) ) {
                    return false;
                }
            }
        }
        return true;
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
    
    public void addCourseNode(CourseTreeNode ctNodeParent, Course childCourse){
            CourseTreeNode ctNodeChild = new CourseTreeNode();
            ctNodeChild.course = childCourse;
            ctNodeChild.parent = ctNodeParent;
            ctNodeParent.child.add(ctNodeChild) ;
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
