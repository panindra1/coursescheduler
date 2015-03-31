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
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Set;

/**
 *
 * @author panindra
 */
class TreeNode {
    Set<Course> courseSet;
    TreeNode parent;
    int totalCost;
    int semesterNumber;
    
    public TreeNode() {
        parent = null;
    }    
}

public class CourseScheduler {

    public static void main(String args[]) throws IOException {
        PriorityQueue<TreeNode> pathPriorityQueue = new PriorityQueue<TreeNode>(10, new Comparator<TreeNode>() {
            public int compare(TreeNode o1, TreeNode o2) {
                if (o1.totalCost > o2.totalCost) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });

        ArrayList<Integer> interestingCourseList = new ArrayList<>();
        ArrayList<Course> allCourses = new ArrayList<Course>();
        int budget = 0;
        File file = new File("secondScenario.txt");
        BufferedReader br
                = new BufferedReader(new FileReader(file));
        int numOfCourses = Integer.parseInt(br.readLine().split(" ")[0]);

        Course[] courseArray = new Course[numOfCourses + 1];
        for (int i = 1; i <= numOfCourses; i++) {
            Course course = new Course();
            String courseLine = br.readLine();
            course.setFallCost(Integer.parseInt(courseLine.split(" ")[0]));
            course.setSpringCost(Integer.parseInt(courseLine.split(" ")[1]));
            course.setCreditHours(Integer.parseInt(courseLine.split(" ")[2]));
            course.setCourseNumber(i);
            course.setInteresting(false);
            course.setImpPreRequisite(false);
            courseArray[i] = course;
            allCourses.add(course);
        }

        //student.setAllCourses(allCourses);
        for (int i = 1; i <= numOfCourses; i++) {
            String prereqLine = br.readLine();
            if ("0".equals(prereqLine)) {
                continue;
            }
            String[] prereqs = prereqLine.split(" ");
            ArrayList<Course> prereqSet = courseArray[i].getPrerequistes();
            if (prereqSet == null) {
                prereqSet = new ArrayList<Course>();
            }

            for (int j = 1; j < prereqs.length; j++) {
                Course prereq = courseArray[Integer.parseInt(prereqs[j])];
                prereqSet.add(prereq);
                if (courseArray[i].isInteresting()) {
                    prereq.setImpPreRequisite(true);
                }
            }
            courseArray[i].setPrerequistes(prereqSet);
        }

        String[] interstingCoursesStrArr = br.readLine().split(" ");
        for (String interestingCourseStr : interstingCoursesStrArr) {
            if (interestingCourseStr == interstingCoursesStrArr[0]) {
                continue;
            }
            Course course = courseArray[Integer.parseInt(interestingCourseStr)];
            course.setInteresting(true);
            interestingCourseList.add(course.getCourseNumber());
        }
        budget = Integer.parseInt(br.readLine());

        ArrayList<Set<Course>> possibleCombs = Util.getCombinations(allCourses, null, 1);
        
        CourseSchedulerSemesteronepointthree c = new CourseSchedulerSemesteronepointthree();
        c.computeCourseSchedule(allCourses, possibleCombs, pathPriorityQueue, interestingCourseList, budget);
        
        //CourseSchedulerSemesteronepointone c = new CourseSchedulerSemesteronepointone();
        //c.computeCourseSchedule(allCourses, possibleCombs, pathPriorityQueue, interestingCourseList, budget);
        
        //CourseSchedulerSemesteronepointtwo c1 = new CourseSchedulerSemesteronepointtwo();
        //c1.computeCourseSchedule(allCourses, possibleCombs, pathPriorityQueue, interestingCourseList, budget);
        

    }
}
