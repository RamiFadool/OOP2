package gym.management;
import gym.Exception.*;
import gym.customers.*;
import gym.management.Sessions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;

public class Secretary implements Subject{

    private Person secretary;
    private int salaryPerMonth;
    private ArrayList<Client> clients = new ArrayList<>();
    private ArrayList<Instructor> instructors = new ArrayList<>();
    private ArrayList<Session> sessions = new ArrayList<>();
    private ArrayList<String> actionsHistory = new ArrayList<>();

    public Secretary(Person p){
        this.secretary = p;
    }

    public void setSalary(int salary){
        this.salaryPerMonth = salary;
    }

    public Client registerClient(Person p) throws InvalidAgeException, DuplicateClientException{
        if(p.age() < 18){
            throw new InvalidAgeException();
        }

        //Check if the Client already registered
        Iterator<Client> iterator = clients.iterator();
        while(iterator.hasNext()){
            Client client = iterator.next();
            if (client.getName().equals(p.getName()) && client.getBirthDate().equals(p.getBirthDate())) {
                throw new DuplicateClientException("Error: The client is already registered");
            }
        }

        Client newClient = new Client(p);
        clients.add(newClient);
        actionsHistory.add("Registered new client: " + p.getName());
        return newClient;
    }

    public void unregisterClient(Client c) throws ClientNotRegisteredException{
        Iterator<Client> iterator = clients.iterator();
        while (iterator.hasNext()) {
            Client client = iterator.next();
            if(client.getName().equals(c.getName()) && client.getBirthDate().equals(c.getBirthDate())){
                iterator.remove();

                actionsHistory.add("Unregistered client: " + c.getName());
                return;
            }
        }
        throw new ClientNotRegisteredException("Error: Registration is required before attempting to unregister");
    }

    public Instructor hireInstructor(Person p, int hourSalary, ArrayList<String> qualifiedSessions){
        Iterator<Instructor> iterator = instructors.iterator();
        while(iterator.hasNext()){
            Instructor instructor = iterator.next();
            if(instructor.getName().equals(p.getName()) && instructor.getBirthDate().equals(p.getBirthDate())
                && instructor.getSalaryPerHour() == hourSalary && instructor.getQualifiedSessions().equals(qualifiedSessions)){

                System.out.println("Error: " + p.getName() + " is already hired as an instructor.");
                return instructor;
            }
        }
        Instructor newInstructor = new Instructor(p, hourSalary, qualifiedSessions);
        instructors.add(newInstructor);

        actionsHistory.add("Hired new instructor: " + p.getName() + "with salary per hour: " + hourSalary);
        return newInstructor;
    }

    public Session addSession(String sessionType, String dateAndTime, String forumType, Instructor instructor) throws InstructorNotQualifiedException{

        if(!instructor.getQualifiedSessions().contains(sessionType)){
            throw new InstructorNotQualifiedException();
        }

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(dateAndTime, dateFormat);

        Session newSession = new Session(sessionType, dateAndTime, forumType, instructor);
        sessions.add(newSession);
        actionsHistory.add("Created new session: " + sessionType + "on " + dateTime + "with instructor: " + instructor);

        return newSession;
    }

    public void registerClientToLesson(Client client, Session session) throws DuplicateClientException, ClientNotRegisteredException {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        LocalDateTime sessionDateTime = LocalDateTime.parse(session.getDateAndTime(), dateTimeFormatter);
        LocalDate dateAndTime = sessionDateTime.toLocalDate();

        if (!clients.contains(client)) {
            throw new ClientNotRegisteredException("Error: The client is not registered with the gym and cannot enroll in lessons");
        }

        if (session.getRegisteredClients().contains(client)) {
            // Instead of throwing the exception directly, check for a duplicate and notify or handle it differently
            actionsHistory.add("Client " + client.getName() + " is already registered for this session.");
            System.out.println("Error: The client is already registered for this session");
            return;  // Prevent further registration for the same client-session
        }

        if (!dateAndTime.isAfter(currentDate)) {
            actionsHistory.add("Failed registration: Session is not in the future");
            return;
        }

        if (session.getForumType() == ForumType.Seniors && client.age() < 65) {
            actionsHistory.add("Failed registration: Client doesn't meet the age requirements for this session (Seniors)");
            return;
        }

        if ((client.getGender() == Gender.Male && session.getForumType() == ForumType.Female)
                || (client.getGender() == Gender.Female && session.getForumType() == ForumType.Male)) {
            actionsHistory.add("Failed registration: Client's gender doesn't match the session's gender requirements");
            return;
        }

        if (client.getBalance() < session.getPrice()) {
            actionsHistory.add("Failed registration: Client doesn't have enough balance");
            return;
        }

        if (session.getRegisteredClients().size() >= session.getMaxParticipants(session.getSessionType())) {
            actionsHistory.add("Failed registration: No available spots for session");
            return;
        }

        // Register client if all checks pass
        client.setBalance(client.getBalance() - session.getPrice());
        session.getRegisteredClients().add(client);
        actionsHistory.add("Registered client: " + client.getName() + " to session: " + session.getSessionType() + " on " + session.getDateAndTime());
    }


    @Override
    public void notify(Session session, String message) {
        for(Client client : session.getRegisteredClients()){
            actionsHistory.add("A message was sent to everyone registered for session " + session.getSessionType() + " on " + session.getDateAndTime() + " : ");
            client.update(message);
        }
    }

    @Override
    public void notify(String date, String message) {
        for(Session session : sessions){
            if(session.getDateAndTime().startsWith(date)){
                for(Client client : session.getRegisteredClients()){
                    actionsHistory.add("A message was sent to everyone registered for a session on " + session.getDateAndTime() + " : ");
                    client.update(message);
                }
            }
        }
    }

    @Override
    public void notify(String message) {
        for(Client client: clients){
            actionsHistory.add("A message was sent to all gym clients: " + message);
            client.update(message);
        }
    }

    public void paySalaries(){
        for(Instructor instructor : instructors){
            int totalHours = 0;
            for(Session session : sessions){
                if(session.getInstructor() == instructor){
                    totalHours += 1;
                }
            }
            instructor.setBalance(instructor.getBalance() + (instructor.getSalaryPerHour() * totalHours));
            this.secretary.setBalance(secretary.getBalance() + salaryPerMonth);
        }
        actionsHistory.add("Salaries have been paid to all employees");
    }

    public void printActions() {
        for(String action : actionsHistory){
            System.out.println(action);
        }
    }

    public ArrayList<Client> getClients() {
        return clients;
    }

    public ArrayList<Instructor> getInstructors() {
        return instructors;
    }

    public ArrayList<Session> getSessions() {
        return sessions;
    }

    public void addAction(String action){
        actionsHistory.add(action);
    }

    public ArrayList<String> getActionsHistory(){
        return actionsHistory;
    }

    @Override
    public String toString() {
        return "ID: " + secretary.getID() + " | Name: " + secretary.getName() + " | Gender: " + secretary.getGender()
                + " | Birthday: " + secretary.getBirthDate() + " | Age: " + secretary.age()
                + " | Balance: " + secretary.getBalance() + " | Role: Secretary"
                + " | Salary per Month: " + salaryPerMonth;
    }
}

