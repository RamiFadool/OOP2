package gym.customers;

import gym.management.Sessions.SessionTypes;

import java.util.ArrayList;

public class Instructor extends Person{
    private int salaryPerHour;
    private ArrayList<String> qualifiedSessions;

    public Instructor(Person p, int salaryPerHour, ArrayList<String> qualifiedSessions) {
        super(p.getName(), p.getBalance(), p.getGender(), p.getBirthDate());
        this.salaryPerHour = salaryPerHour;
        this.qualifiedSessions = qualifiedSessions;
    }

    public int getSalaryPerHour(){
        return salaryPerHour;
    }

    public ArrayList<String> getQualifiedSessions(){
        return qualifiedSessions;
    }

    @Override
    public String toString() {
        String certifiedClasses = "";
        for (int i = 0; i < qualifiedSessions.size(); i++) {
            certifiedClasses += qualifiedSessions.get(i);

            if (i < qualifiedSessions.size() - 1) {
                certifiedClasses += ", ";
            }
        }

        return "ID: " + getID() + " | Name: " + getName() + " | Gender: " + getGender()
                + " | Birthday: " + getBirthDate() + " | Age: " + age()
                + " | Balance: " + getBalance() + " | Role: Instructor"
                + " | Salary per Hour: " + salaryPerHour
                + " | Certified Classes: " + certifiedClasses;
    }


}
