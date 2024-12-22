package gym.management;
import gym.Exception.*;
import gym.customers.*;
import gym.management.Sessions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Iterator;

public class Secretary implements Subject{

    private Person secretary;
    private int salaryPerMonth;
    private ArrayList<Client> clients = new ArrayList<>();
    private ArrayList<Instructor> instructors = new ArrayList<>();
    private ArrayList<Session> sessions = new ArrayList<>();
    private ArrayList<String> actionsHistory = new ArrayList<>();
    private boolean isSecretary;

    public Secretary(Person p){
        this.secretary = p;
        this.isSecretary = true;
    }

    public void setSalary(int salary){
        this.salaryPerMonth = salary;
    }
    public void setSecretary(boolean isSecretary){
        this.isSecretary = isSecretary;
    }

    public Client registerClient(Person p) throws InvalidAgeException, DuplicateClientException{
        if(!this.isSecretary){
            return null;
        }

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
        if(!this.isSecretary){
            return;
        }

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
        if(!this.isSecretary){
            return null;
        }

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

        actionsHistory.add("Hired new instructor: " + p.getName() + " with salary per hour: " + hourSalary);
        return newInstructor;
    }

    public Session addSession(String sessionType, String dateAndTime, String forumType, Instructor instructor) throws InstructorNotQualifiedException{
        if(!this.isSecretary){
            return null;
        }

        if(!instructor.getQualifiedSessions().contains(sessionType)){
            throw new InstructorNotQualifiedException("Error: Instructor is not qualified to conduct this session type.");
        }

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(dateAndTime, dateFormat);

        Session newSession = new Session(sessionType, dateAndTime, forumType, instructor);
        sessions.add(newSession);
        actionsHistory.add("Created new session: " + sessionType + " on " + dateTime + " with instructor: " + instructor.getName());

        return newSession;
    }

    public void registerClientToLesson(Client client, Session session) throws DuplicateClientException, ClientNotRegisteredException, NullPointerException {
        if(!this.isSecretary){
            throw new NullPointerException();
        }
        boolean is = true;
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        LocalDateTime sessionDateTime = LocalDateTime.parse(session.getDateAndTime(), dateTimeFormatter);
        LocalDate dateAndTime = sessionDateTime.toLocalDate();

        if (!clients.contains(client)) {
            throw new ClientNotRegisteredException("Error: The client is not registered with the gym and cannot enroll in lessons");
        }

        if (session.getRegisteredClients().contains(client) && clients.contains(client)) {
            throw new DuplicateClientException("Error: The client is already registered for this lesson");
        }

        if (!dateAndTime.isAfter(currentDate)) {
            actionsHistory.add("Failed registration: Session is not in the future");
            is = false;
        }

        if (session.getForumType().equals(ForumType.Seniors) && client.age() < 65) {
            actionsHistory.add("Failed registration: Client doesn't meet the age requirements for this session (Seniors)");
            is = false;
        }

        if (!(session.getForumType().equals(ForumType.All)) && client.getGender() != session.getForumType() ){
            actionsHistory.add("Failed registration: Client's gender doesn't match the session's gender requirements");
            is = false;
        }
        if (session.getRegisteredClients().size() >= session.getMaxParticipants(session.getSessionType())) {
            actionsHistory.add("Failed registration: No available spots for session");
            is = false;

        }

        if (client.getBalance() < session.getPrice()) {
            actionsHistory.add("Failed registration: Client doesn't have enough balance");
            is = false;
        }

        if(is){
            chargeClientForSession(client, session);
            session.getRegisteredClients().add(client);
            actionsHistory.add("Registered client: " + client.getName() + " to session: " + session.getSessionType() + " on " + session.getDateAndTime() + " for price: " + session.getPrice());

        }
    }

    public void chargeClientForSession(Client client, Session session) {
        int sessionCost = session.getPrice(); // Get the session cost
        int currentBalance = client.getBalance(); // Get the client's current balance

        // Deduct the session cost from the balance
        int newBalance = currentBalance - sessionCost;
        client.setBalance(newBalance); // Update the client's balance
    }

    private String formatDate(String date) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDate = LocalDate.parse(date, inputFormatter);

        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return localDate.format(outputFormatter);
    }



    @Override
    public void notify(Session session, String message) {
        if(!this.isSecretary){
            return;
        }

        for(Client client : session.getRegisteredClients()){
            client.update(message);
        }
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(session.getDateAndTime(), dateFormat);
        actionsHistory.add("A message was sent to everyone registered for session " + session.getSessionType() + " on " + dateTime + " : " + message);
    }

    @Override
    public void notify(String date, String message) {
        if(!this.isSecretary){
            return;
        }
        boolean is = false;
        for(Session session : sessions){
            if(session.getDateAndTime().startsWith(date)){
                for(Client client : session.getRegisteredClients()){
                    client.update(message);
                    is = true;
                }
            }
        }
        if(is){
            String formattedDate = formatDate(date);
            actionsHistory.add("A message was sent to everyone registered for a session on " + formattedDate + " : " + message);
        }
    }

    @Override
    public void notify(String message) {
        if(!this.isSecretary){
            return;
        }

        actionsHistory.add("A message was sent to all gym clients: " + message);
        for(Client client: clients){
            client.update(message);
        }
    }

    public void paySalaries(){
        if(!this.isSecretary){
            return;
        }

        for(Instructor instructor : instructors){
            int totalHours = 0;
            for(Session session : sessions){
                if(session.getInstructor() == instructor){
                    totalHours += 1;
                }
            }
            int amount = instructor.getSalaryPerHour() * totalHours;
            instructor.setBalance(instructor.getBalance() + amount);
        }
        this.secretary.setBalance(secretary.getBalance() + salaryPerMonth);
        actionsHistory.add("Salaries have been paid to all employees");
    }

    public void printActions() {
        if(!this.isSecretary){
            return;
        }

        for(String action : actionsHistory){
            System.out.println(action);
        }
    }

    public ArrayList<Client> getClients() {
        if(!this.isSecretary){
            return null;
        }
        return clients;
    }

    public ArrayList<Instructor> getInstructors() {
        if(!this.isSecretary){
            return null;
        }
        return instructors;
    }

    public ArrayList<Session> getSessions() {
        if(!this.isSecretary){
            return null;
        }
        return sessions;
    }

    public void addAction(String action){
        if(!this.isSecretary){
            return;
        }
        actionsHistory.add(action);
    }

    public ArrayList<String> getActionsHistory(){
        if(!this.isSecretary){
            return null;
        }

        return actionsHistory;
    }

    @Override
    public String toString() {
        if(!this.isSecretary){
            return null;
        }

        return "ID: " + secretary.getID() + " | Name: " + secretary.getName() + " | Gender: " + secretary.getGender()
                + " | Birthday: " + secretary.getBirthDate() + " | Age: " + secretary.age()
                + " | Balance: " + secretary.getBalance() + " | Role: Secretary"
                + " | Salary per Month: " + salaryPerMonth;
    }
}

