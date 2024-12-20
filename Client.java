package gym.customers;

import java.util.ArrayList;
import java.util.Objects;

public class Client extends Person implements Observer{
    private Person client;
    private ArrayList<String> notifications = new ArrayList<>();

    public Client(Person p){
        super(p.getName(), p.getBalance(), p.getGender(),p.getBirthDate());
        this.client = p;
    }

    public ArrayList<String> getNotifications() {
        return notifications;
    }

    @Override
    public void update(String message) {
        notifications.add(message);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(getName(), client.getName()) && Objects.equals(getBirthDate(), client.getBirthDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getBirthDate());
    }
    @Override
    public String toString() {
        return "ID: " + client.getID() + " | Name: " + client.getName() + " | Gender: " + client.getGender()
                + " | Birthday: " + client.getBirthDate() + " | Age: " + client.age()
                + " | Balance: " + client.getBalance();
    }

}

