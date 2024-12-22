package gym.management.Sessions;

import gym.customers.*;


import java.time.LocalDateTime;
import java.util.ArrayList;

public class Session {
    private String sessionType;
    private String dateAndTime;
    private String forumType;
    private Instructor instructor;
    private ArrayList<Client> registeredClients;


    public Session(String sessionType, String dateAndTime, String forumType, Instructor instructor){
        this.sessionType = sessionType;
        this.dateAndTime = dateAndTime;
        this.forumType = forumType;
        this.instructor = instructor;
        this.registeredClients = new ArrayList<>();
    }

    public String getDateAndTime(){
        return dateAndTime;
    }
    public String getSessionType(){
        return sessionType;
    }

    public String getForumType(){
        return forumType;
    }

    public int getPrice(){
        switch(this.sessionType){
            case SessionTypes.Pilates:
                return 60;
            case SessionTypes.MachinePilates:
                return 80;
            case SessionTypes.ThaiBoxing:
                return 100;
            case SessionTypes.Ninja:
                return 150;
            default:
                return 0;
        }
    }

    public int getMaxParticipants(String sessionType){
        switch(sessionType){
            case SessionTypes.Pilates:
                return 30;
            case SessionTypes.MachinePilates:
                return 10;
            case SessionTypes.ThaiBoxing:
                return 20;
            case SessionTypes.Ninja:
                return 5;
            default:
                return 0;
        }

    }

    public Instructor getInstructor(){
        return instructor;
    }

    public ArrayList<Client> getRegisteredClients(){
        return registeredClients;
    }

    @Override
    public String toString() {
        return "Session Type: " + sessionType
                + " | Date: " + dateAndTime
                + " | Forum: " + forumType
                + " | Instructor: " + instructor.getName()
                + " | Participants: " + registeredClients.size() + "/" + getMaxParticipants(sessionType);
    }

}
