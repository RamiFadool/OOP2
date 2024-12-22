package gym.management;

import gym.customers.Client;
import gym.customers.Instructor;
import gym.customers.Person;
import gym.management.Sessions.Session;

public class Gym {
    private static Gym instance = new Gym();
    private String gymName;
    private Secretary secretary;
    private int gymBalance = -8410;

    private Gym(){}

    public static Gym getInstance(){
        return instance;
    }

    public void setName(String gymName){
        this.gymName = gymName;
    }

    public void setSecretary(Person p, int salary){
        if (this.secretary == null) {
            this.secretary = new Secretary(p);
            this.secretary.addAction("A new secretary has started working at the gym: " + p.getName());
        } else {
            Secretary newSecretary = new Secretary(p);
            newSecretary.setSalary(salary);
            newSecretary.getClients().addAll(this.secretary.getClients());
            newSecretary.getInstructors().addAll(this.secretary.getInstructors());
            newSecretary.getSessions().addAll(this.secretary.getSessions());
            newSecretary.getActionsHistory().addAll(this.secretary.getActionsHistory());
            newSecretary.addAction("A new secretary has started working at the gym: " + p.getName());
            this.secretary.setSecretary(false);
            this.secretary = newSecretary;
        }
    }

    public Secretary getSecretary() {
        return secretary;
    }

    @Override
    public String toString(){
        StringBuilder information = new StringBuilder();

        information.append("\nGym name: " + gymName + "\n");
        information.append("Gym Secretary: " + secretary + "\n");
        information.append("Gym Balance: " + gymBalance + "\n");

        information.append("\nClients Data:\n");
        for(Client client : secretary.getClients()){
            information.append(client +  "\n");
        }

        information.append("\nEmployees Data:\n");
        for(Instructor instructor : secretary.getInstructors()){
            information.append(instructor + "\n");
        }
        information.append(secretary + "\n");

        information.append("\nSessions Data:\n");
        for(Session session : secretary.getSessions()){
            information.append(session + "\n");
        }

        return information.toString();
    }
}
