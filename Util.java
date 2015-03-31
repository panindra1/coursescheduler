/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coursescheduler1;

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

/**
 *
 * @author panindra
*/


public class Util {
    static int mCmax = 50;
    static int mCmin = 10;    
    
     static boolean checkIfInterestedCompelted(ArrayList<Integer> inerestingCoursesList, Set<Course> finishedCourses) {
        int count = 0;
        for(int j = 0; j < inerestingCoursesList.size(); j++) {
            for(Course course : finishedCourses) {
                if(course.getCourseNumber() == inerestingCoursesList.get(j)) {
                    count++;
                }
            }
        }
       // System.out.println("Interesting course count" + count);
        if(count == inerestingCoursesList.size()) 
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
    
     static ArrayList<Set<Course>> getCombinations(ArrayList<Course> allCourses, Set<Course> currentComb, int semesterNumber){
         ArrayList<Set<Course>> result = new ArrayList<Set<Course>>();
         Set<Course> possibleCourses = new HashSet<>();
         
         for(Course course : allCourses){
             if(currentComb == null || !currentComb.contains(course)){
                if(checkIfPrereqCompleted(currentComb, course)){
                    
                    possibleCourses.add(course);
                }
             }
         }
         
         //permute(possibleCourses);
         Set<Set<Course>> courseSet = powerSet(possibleCourses);
         Map<Integer, Set<Course>> courseMap = new HashMap<Integer, Set<Course>>();
                  
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
