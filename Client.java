package gym.customers;

import java.util.ArrayList;
import java.util.Objects;

public class Client extends Person implements Observer{
    private ArrayList<String> notifications = new ArrayList<>();
    private int balance;

    public Client(Person p){
        super(p.getName(), p.getBalance(), p.getGender(),p.getBirthDate());
        this.balance = p.getBalance();
    }

    @Override
    public int getBalance(){
        return balance;
    }

    @Override
    public void setBalance(int balance){
        this.balance = balance;
    }

    public ArrayList<String> getNotifications() {
        return notifications;
    }



    @Override
    public void update(String message) {
        notifications.add(message);
    }

    @Override
    public String toString() {
        return "ID: " + getID() + " | Name: " + getName() + " | Gender: " + getGender()
                + " | Birthday: " + getBirthDate() + " | Age: " + age()
                + " | Balance: " + getBalance();
    }

}

