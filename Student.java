package coursescheduler;

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
enum semesterType {FALL, SPRING};

public class Student {
    int numSemesters;
    int numCourses;
    int costIncurred;
    int minCreditPerSem;
    int maxCreditPerSem;
    int maxCost;
    semesterType currSemester;

    public semesterType getCurrSemester() {
        return currSemester;
    }

    public void setCurrSemester(semesterType currSemester) {
        this.currSemester = currSemester;
    }
    Set<Course> allCourses;
    Set<Course> interestingCourses;
    
    
    public int getNumSemesters() {
        return numSemesters;
    }

    public void setNumSemesters(int numSemesters) {
        this.numSemesters = numSemesters;
    }

    public int getNumCourses() {
        return numCourses;
    }

    public void setNumCourses(int numCourses) {
        this.numCourses = numCourses;
    }

    public int getCostIncurred() {
        return costIncurred;
    }

    public void setCostIncurred(int costIncurred) {
        this.costIncurred = costIncurred;
    }

	public int getMinCreditPerSem() {
		return minCreditPerSem;
	}

	public void setMinCreditPerSem(int minCreditPerSem) {
		this.minCreditPerSem = minCreditPerSem;
	}

	public int getMaxCreditPerSem() {
		return maxCreditPerSem;
	}

	public void setMaxCreditPerSem(int maxCreditPerSem) {
		this.maxCreditPerSem = maxCreditPerSem;
	}

	public Set<Course> getAllCourses() {
		return allCourses;
	}

	public void setAllCourses(Set<Course> allCourses) {
		this.allCourses = allCourses;
	}

	public Set<Course> getInterestingCourses() {
		return interestingCourses;
	}

	public void setInterestingCourses(Set<Course> interestingCourses) {
		this.interestingCourses = interestingCourses;
	}

	@Override
	public String toString() {
		return "Student [numSemesters=" + numSemesters + ", numCourses="
				+ numCourses + ", costIncurred=" + costIncurred
				+ ", minCreditPerSem=" + minCreditPerSem + ", maxCreditPerSem="
				+ maxCreditPerSem + ", maxCost=" + maxCost + ", allCourses="
				+ allCourses + ", interestingCourses=" + interestingCourses
				+ "]";
	}

	public int getMaxCost() {
		return maxCost;
	}

	public void setMaxCost(int maxCost) {
		this.maxCost = maxCost;
	}
    
}
