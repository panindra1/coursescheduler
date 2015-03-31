/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coursescheduler1;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Set;

/**
 *
 * @author panindra
 */
public class CourseSchedulerSemesteronepointtwo {

    private ArrayList<Integer> mInterestingCourseList = new ArrayList<>();
    private ArrayList<Course> mAllCourses = new ArrayList<>();

    private int mCompleteAssignment = 0;
    private int mAttemptedAssignment = 0;
    private boolean mFoundpath = false;

    private PriorityQueue<TreeNode> mPathPriorityQueue;
    private int mBudget = 0;
    
    public void computeCourseSchedule(ArrayList<Course> allCourses, ArrayList<Set<Course>> possibleCombs, PriorityQueue<TreeNode> pathPriorityQueue, ArrayList<Integer> interestingList, int budget) {
        mAllCourses = allCourses;
        mPathPriorityQueue = pathPriorityQueue;
        mInterestingCourseList = interestingList;
        mBudget = budget;
        mFoundpath = false;

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
        if (mFoundpath) {
            return;
        }
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

            if (childNode.totalCost > mBudget) {
                continue;
            }

            if (Util.checkIfInterestedCompelted(mInterestingCourseList, combination) && !mFoundpath) {
                mPathPriorityQueue.add(childNode);
                mFoundpath = true;
                return;
            }
            recursivebacktracking(childNode);
        }
    }
}
