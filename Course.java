package coursescheduler1;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author panindra
 */
public class Course {
    private int creditHours;
    private int courseNumber;        
    public int getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(int courseNumber) {
        this.courseNumber = courseNumber;
    }
    private int springCost;
    private int fallCost;
    
    
    private boolean interesting;
    private boolean impPreRequisite;

    public boolean isImpPreRequisite() {
        return impPreRequisite;
    }

    public void setImpPreRequisite(boolean impPreRequisite) {
        this.impPreRequisite = impPreRequisite;
    }
    private ArrayList<Course> prerequistes;

    Course() {
    }
    
    public boolean isInteresting() {
        return interesting;
    }

    public void setInteresting(boolean interesting) {
        this.interesting = interesting;
    }
    
    public int getCreditHours() {
        return creditHours;
    }

    public void setCreditHours(int creditHours) {
        this.creditHours = creditHours;
    }

    public int getSpringCost() {
        return springCost;
    }

    public void setSpringCost(int springCost) {
        this.springCost = springCost;
    }

    public int getFallCost() {
        return fallCost;
    }

    public void setFallCost(int fallCost) {
        this.fallCost = fallCost;
    }

    public ArrayList<Course> getPrerequistes() {
        return prerequistes;
    }

    public void setPrerequistes(ArrayList<Course> prerequistes) {
        this.prerequistes = prerequistes;
    }

	@Override
	public String toString() {
		return "Course [creditHours=" + creditHours + ", springCost="
				+ springCost + ", fallCost=" + fallCost + ", prerequistes="
				+ prerequistes + "]";
	}
}
