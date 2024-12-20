package gym.customers;

import gym.management.Sessions.SessionTypes;

import java.util.ArrayList;

public class Instructor extends Person{
    private Person instructor;
    private int salaryPerHour;
    private ArrayList<String> qualifiedSessions;

    public Instructor(Person p, int salaryPerHour, ArrayList<String> qualifiedSessions) {
        super(p.getName(), p.getBalance(), p.getGender(), p.getBirthDate());
        this.instructor = p;
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

        return "ID: " + instructor.getID() + " | Name: " + instructor.getName() + " | Gender: " + instructor.getGender()
                + " | Birthday: " + instructor.getBirthDate() + " | Age: " + instructor.age()
                + " | Balance: " + instructor.getBalance() + " | Role: Instructor"
                + " | Salary per Hour: " + salaryPerHour
                + " | Certified Classes: " + certifiedClasses;
    }


}
