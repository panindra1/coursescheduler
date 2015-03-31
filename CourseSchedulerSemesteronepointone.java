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
public class CourseSchedulerSemesteronepointone {   
    static ArrayList<Integer> mInterestingCourseList = new ArrayList<>();
    static ArrayList<Course> mAllCourses = new ArrayList<Course>();
    static int mCompleteAssignment = 0;
    static int mAttemptedAssignment = 0;
    
    static PriorityQueue<TreeNode> mPathPriorityQueue;
    static boolean mFoundpath = false;
    static int mTotalMinCostOfCOurses = 0;    
    static int mBudget = 0;    
    static int mCmax = 50;
    static int mCmin = 10;
    static boolean mInterestingCourseCompleted = false;
    static int minTillNow = Integer.MAX_VALUE;
    
    public static void main(String args[])
    	throws IOException
    {
        File file = new File("fourthScenario.txt");
        BufferedReader br =
                new BufferedReader(new FileReader(file));
        int numOfCourses = Integer.parseInt(br.readLine().split(" ")[0]);
                
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
                 
        for(int i = 1; i <= numOfCourses ; i ++)
        {
            String prereqLine = br.readLine();
            if("0".equals(prereqLine)) continue;
            String[] prereqs = prereqLine.split(" ");
            ArrayList<Course> prereqSet = courseArray[i].getPrerequistes();
            if(prereqSet == null) prereqSet = new ArrayList<Course>();
            
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
                
        String[] interstingCoursesStrArr = br.readLine().split(" ");        
        for(String interestingCourseStr : interstingCoursesStrArr)
        {
            if(interestingCourseStr == interstingCoursesStrArr[0])
                continue;
            Course course = courseArray[Integer.parseInt(interestingCourseStr)];            
            course.setInteresting(true);
            mInterestingCourseList.add(course.getCourseNumber());
        }
        mBudget = Integer.parseInt(br.readLine());
                
        mPathPriorityQueue = new PriorityQueue<TreeNode>(10, new Comparator<TreeNode>(){
                public int compare(TreeNode o1, TreeNode o2) {                
                    if(o1.totalCost > o2.totalCost) {
                        return 1;
                    }
                    else {
                        return -1;
                    }
                }
            });            
        
        int semesterCounter = 1;
                     
        ArrayList<Set<Course>> possibleCombs = getCombinations(null, semesterCounter);
                
        for(Set<Course> combination : possibleCombs) {
            TreeNode cTNode = new TreeNode();
            cTNode.parent = null;
            int totalCost = 0;
            cTNode.courseSet = combination;
            cTNode.semesterNumber = semesterCounter;
            //System.out.println("First sem" + combination);
            for(Course C : combination){           
                totalCost += C.getFallCost();
            }
            cTNode.totalCost = totalCost;
            recursivebacktracking(cTNode);
            break;
        }
        System.out.println(mPathPriorityQueue.peek().totalCost + " "+ mPathPriorityQueue.peek().semesterNumber);
        printTree(mPathPriorityQueue.peek());
        System.out.println("\n The number of Incompelte Assignments are : " + Math.abs(mAttemptedAssignment - mCompleteAssignment));
        System.out.println(" The number of complete assignments :" + mCompleteAssignment);
    }
    
    static void recursivebacktracking(TreeNode ctNode) {                
        mAttemptedAssignment++;
        if(mFoundpath) {
            return;
        }
        ArrayList<Set<Course>> allowableCombs = getCombinations(ctNode.courseSet, ctNode.semesterNumber);
        for(Set<Course> combination : allowableCombs) {            
            
            TreeNode childNode = new TreeNode();
            childNode.parent = ctNode;
            int totalCost = 0;
            childNode.courseSet = combination;
            childNode.semesterNumber = ctNode.semesterNumber + 1;
            //System.out.println("First sem" + combination);
            for(Course C : combination){
                if(childNode.semesterNumber %2 == 1)
                    totalCost += C.getFallCost();
                else 
                    totalCost += C.getSpringCost();
            }
            
            childNode.totalCost = totalCost + ctNode.totalCost;
            combination.addAll(ctNode.courseSet);                
            
            TreeNode existingNode = mPathPriorityQueue.peek();            
            
            if(checkIfInterestedCompelted(combination) && !mFoundpath) {                                 
                mPathPriorityQueue.add(childNode); 
                //continue;
                mCompleteAssignment++;                
                mFoundpath = true;
                return;
            }
            
            recursivebacktracking(childNode);
        }
        //recursivebacktracking();        
    }
    
     static boolean checkIfInterestedCompelted(Set<Course> finishedCourses) {
        int count = 0;
        for(int j = 0; j < mInterestingCourseList.size(); j++) {
            for(Course course : finishedCourses) {
                if(course.getCourseNumber() == mInterestingCourseList.get(j)) {
                    count++;
                }
            }
        }
       // System.out.println("Interesting course count" + count);
        if(count == mInterestingCourseList.size()) 
            return true;
        
        return false;
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
    
     static public void printTree(TreeNode node) {
        TreeNode root = node;
        ArrayList<ArrayList<Integer>> resultSet = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> outptList = new ArrayList<>();
        ArrayList<Integer> costSet = new ArrayList<Integer>();
        while(root != null) {
            ArrayList<Integer> resultSubSet = new ArrayList<Integer>();
            for(Course course: root.courseSet) {
                resultSubSet.add(course.getCourseNumber());
            }
            costSet.add(root.totalCost);
            resultSet.add(resultSubSet);
            root = root.parent;
        } 
        
        ArrayList<Integer> lastSet = resultSet.get(resultSet.size() - 1);
        System.out.print(lastSet.size() + " ");
        for(int k = 0; k < lastSet.size(); k++) {
            System.out.print(lastSet.get(k) + " ");
                
        }
        System.out.println("");
        for(int i = resultSet.size() - 2; i >= 0 ; i--) {
            ArrayList<Integer> resultSubSet = resultSet.get(i);
           
            ArrayList<Integer> answerSet = new ArrayList<>();
            for(int j = 0; j < resultSubSet.size() ; j++) {
                if(!lastSet.contains(resultSubSet.get(j))) {
                    answerSet.add(resultSubSet.get(j));
                }
            }
            lastSet.addAll(answerSet );
            System.out.print(answerSet.size() + " ");
            for(int k = 0; k < answerSet.size(); k++) {
                System.out.print(answerSet.get(k) + " ");
                
            }
            System.out.println("");
        }
                
        for(int k = 0; k < costSet.size(); k++) {
            if(k < costSet.size() - 1)
                outptList.add(costSet.get(k) - costSet.get(k + 1));              
            else 
                outptList.add(costSet.get(k));                
        }
        
        for(int k = outptList.size() - 1; k >= 0; k--) {
            System.out.print(outptList.get(k) + " ");
        }
        
    }   
    
     static ArrayList<Set<Course>> getCombinations(Set<Course> currentComb, int semesterNumber){
         ArrayList<Set<Course>> result = new ArrayList<Set<Course>>();
         Set<Course> possibleCourses = new HashSet<>();
         
         for(Course course : mAllCourses){
             if(currentComb == null || !currentComb.contains(course)){
                if(checkIfPrereqCompleted(currentComb, course)){
                    
                    possibleCourses.add(course);
                }
             }
         }
         
         //permute(possibleCourses);
         Set<Set<Course>> courseSet = powerSet(possibleCourses);
         Map<Integer, Set<Course>> courseMap = new HashMap<Integer, Set<Course>>();
         
         Set<Course> tempSet = new HashSet<>();
         for(Set<Course> courseSubset : courseSet) {
             int totalCredits = 0;
             int totalCost = 0;
             boolean hasInterestingCourse = false;
             
             for(Course course : courseSubset) {
                 if(course.isInteresting()){
                     hasInterestingCourse = true;
                 }
                 totalCredits+=course.getCreditHours();
                 if(semesterNumber % 2 == 0){
                     totalCost+=course.getSpringCost();
                     
                 }
                 else {
                     totalCost+=course.getFallCost();
                 }
             }
             if(hasInterestingCourse &&(totalCredits >= mCmin && totalCredits <= mCmax)){
                 //result.add(courseSubset);
                 courseMap.put( totalCost, courseSubset);
             }
         }
         
         //sortByValue(courseMap);
         ArrayList<Integer> keys = new ArrayList(courseMap.keySet());
         Collections.sort(keys);
         
         for(int i : keys){
             result.add(courseMap.get(i));
         }                        
         return result;
     }
     
     public static <T> Set<Set<T>> powerSet(Set<T> originalSet) {
    Set<Set<T>> sets = new HashSet<Set<T>>();
    
    if (originalSet.isEmpty()) {
    	sets.add(new HashSet<T>());
    	return sets;
    }
    List<T> list = new ArrayList<T>(originalSet);
    T head = list.get(0);
    Set<T> rest = new HashSet<T>(list.subList(1, list.size())); 
    for (Set<T> set : powerSet(rest)) {
    	Set<T> newSet = new HashSet<T>();
    	newSet.add(head);
    	newSet.addAll(set);        
    	sets.add(newSet);
    	sets.add(set);
    }		
    return sets;
}
     
     static boolean checkIfPrereqCompleted(Set<Course> completedCourses, Course currentCourse){
         ArrayList<Course> preReqs = new ArrayList<>();
         preReqs = currentCourse.getPrerequistes();
         if(preReqs == null || preReqs.isEmpty()){
             return true;
         }
         else {
             for(Course c : preReqs){
                 if(completedCourses==null || !completedCourses.contains(c)){
                     return false;
                 }
             }
         }
         return true;
         
     }          
}
