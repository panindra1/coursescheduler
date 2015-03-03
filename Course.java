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
public class Course {
    private int creditHours;
    private int springCost;
    private int fallCost;
    private Set<Course> prerequistes;

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

    public Set<Course> getPrerequistes() {
        return prerequistes;
    }

    public void setPrerequistes(Set<Course> prerequistes) {
        this.prerequistes = prerequistes;
    }

	@Override
	public String toString() {
		return "Course [creditHours=" + creditHours + ", springCost="
				+ springCost + ", fallCost=" + fallCost + ", prerequistes="
				+ prerequistes + "]";
	}
}
