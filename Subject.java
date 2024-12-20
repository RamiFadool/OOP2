package gym.management;

import gym.customers.Observer;
import gym.management.Sessions.Session;

public interface Subject {
    void notify(Session session, String message); // Notify based on session
    void notify(String date, String message);      // Notify based on date
    void notify(String message);                  // Notify all clients
}