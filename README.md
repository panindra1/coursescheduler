# AI-assgn2
This a constraint satisfaction problem and we have schedule courses to the semesters optimally.

The input files are :
1) firstScenario.txt (assisgn 10 subjects/courses optimally)
2) secondScenario.txt (20 subjects)
3) thirddScenario.txt (30 subjects)
4) fourthScenario.txt (40 subjects)

The classes are 
1) Courses.java (This file contains a data structure to contain each coourse). (Each object is a course)
2) Util.java (This file contains all helper methods for all the remaining files)
3) CourseScheduler.java (This is the main program to execute the project. Here you should provide input)
4) CourseSchedulerSemesteronepointone.java (This program contains a program to execute 1.1 (No constraints on Cost))
5) CourseSchedulerSemesteronepointtwo (This program contains a program to execute 1.2 with few more constraints.
6) CourseSchedulerSemesteronepointthree.java (This program contains a program to execute 1.3 with optimal path and pruning.

Here are the steps to execute the project.
1) Open the CourseScheduler.java file and change the input files.
2) Comment/ Uncomment the calling methods. (For ex, uncoomment CourseSchedulerSemesteronepointone class to call for 1.1)

Output
2665 4  //Total cost and No of semesters took to take all interesting courses
2 9 2  //First semester. 2 courses taken. they are 9 and 2.
2 8 4 
2 5 3 
2 7 1 
1040 272 907 446 //Cost per semester
The number of Incompelte Assignments are : 96
The number of complete assignments :56

