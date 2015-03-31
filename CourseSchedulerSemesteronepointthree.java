/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coursescheduler1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Set;

/**
 *
 * @author panindra
 */
public class CourseSchedulerSemesteronepointthree {

    private ArrayList<Integer> mInterestingCourseList = new ArrayList<>();
    private ArrayList<Course> mAllCourses = new ArrayList<Course>();

    private int mCompleteAssignment = 0;
    private int mAttemptedAssignment = 0;

    private PriorityQueue<TreeNode> mPathPriorityQueue;

    private HashMap<ArrayList<Integer>, Integer> mCourseCompletedMapInSem = new HashMap<ArrayList<Integer>, Integer>();
    //static HashMap<ArrayList<Integer>, Integer> mCourseCompletedMapInSem = new HashMap<ArrayList<Integer>, Integer>(); 
    private int mBudget = 0;
    private int mCmax = 50;
    private int mCmin = 10;
    private boolean mInterestingCourseCompleted = false;

    public void computeCourseSchedule(ArrayList<Course> allCourses, ArrayList<Set<Course>> possibleCombs, PriorityQueue<TreeNode> pathPriorityQueue, ArrayList<Integer> interestingList, int budget) {
        mAllCourses = allCourses;
        mPathPriorityQueue = pathPriorityQueue;
        mInterestingCourseList = interestingList;
        mBudget = budget;

        for (Set<Course> combination : possibleCombs) {
            TreeNode cTNode = new TreeNode();
            cTNode.parent = null;
            int totalCost = 0;
            cTNode.courseSet = combination;
            cTNode.semesterNumber = 1;
            //System.out.println("First sem" + combination);
            for (Course C : combination) {
                totalCost += C.getFallCost();
            }
            cTNode.totalCost = totalCost;
            recursivebacktracking(cTNode);
            //break;
        }
        System.out.println(mPathPriorityQueue.peek().totalCost + " " + mPathPriorityQueue.peek().semesterNumber);
        Util.printTree(mPathPriorityQueue.peek());
        System.out.println("\n The number of Incompelte Assignments are : " + Math.abs(mAttemptedAssignment - mCompleteAssignment));
        System.out.println(" The number of complete assignments :" + mCompleteAssignment);
    }

    void recursivebacktracking(TreeNode ctNode) {
        mAttemptedAssignment++;
        ArrayList<Set<Course>> allowableCombs = Util.getCombinations(mAllCourses, ctNode.courseSet, ctNode.semesterNumber);
        for (Set<Course> combination : allowableCombs) {

            TreeNode childNode = new TreeNode();
            childNode.parent = ctNode;
            int totalCost = 0;
            childNode.courseSet = combination;
            childNode.semesterNumber = ctNode.semesterNumber + 1;
            //System.out.println("First sem" + combination);
            for (Course C : combination) {
                if (childNode.semesterNumber % 2 == 1) {
                    totalCost += C.getFallCost();
                } else {
                    totalCost += C.getSpringCost();
                }
            }

            childNode.totalCost = totalCost + ctNode.totalCost;
            combination.addAll(ctNode.courseSet);
            if (Util.checkIfInterestedCompelted(mInterestingCourseList, combination)) {
                mCompleteAssignment++;
            }

            TreeNode existingNode = mPathPriorityQueue.peek();
            if (pruneTree(ctNode)) {
                continue;
            }

            if (existingNode != null && (existingNode.totalCost < childNode.totalCost)) {
                continue;
            }

            if (childNode.totalCost > mBudget) {
                continue;
            }

            if (Util.checkIfInterestedCompelted(mInterestingCourseList, combination)) {
                mPathPriorityQueue.add(childNode);
                continue;
            }

            recursivebacktracking(childNode);
        }
        //recursivebacktracking();        
    }

    boolean pruneTree(TreeNode node) {
        ArrayList<Integer> courseNumArr = new ArrayList<>();
        for (Course course : node.courseSet) {
            courseNumArr.add(course.getCourseNumber());
        }

        if (!courseNumArr.isEmpty()) {
            Collections.sort(courseNumArr);
            if (mCourseCompletedMapInSem.containsKey(courseNumArr)) {
                Integer existingTotalCosts = mCourseCompletedMapInSem.get(courseNumArr);
                ///int val = mCourseCompletedMapInSem.get(courseNumArr);

                if (existingTotalCosts >= node.totalCost) {
                    mCourseCompletedMapInSem.put(courseNumArr, node.totalCost);
                } else if (node.totalCost > existingTotalCosts) {
                    return true;
                }
            } else {
                mCourseCompletedMapInSem.put(courseNumArr, node.totalCost);
            }
        }
        return false;
    }

}
