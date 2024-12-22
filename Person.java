package gym.customers;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class Person {
    private static int idCounter = 1111;
    private int id;
    private String gender;
    private String name;
    private int balance;
    private String birthDate;

    public Person(String name, int balance, String gender, String birthDate){
        this.id = idCounter++;
        this.name = name;
        this.balance = balance;
        this.gender = gender;
        this.birthDate = birthDate;
    }

    public int getID(){
        return id;
    }

    public String getName(){
        return name;
    }

    public int getBalance(){
        return balance;
    }

    public void setBalance(int balance){
        this.balance = balance;
    }

    public String getGender(){
        return gender;
    }
    public String getBirthDate(){
        return birthDate;
    }

    public int age(){
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate dateOfBirth = LocalDate.parse(getBirthDate(), formatter);
        int age = Period.between(dateOfBirth, currentDate).getYears();

        if (currentDate.isBefore(dateOfBirth.withYear(currentDate.getYear()))) {
            age++;
        }

        return age;
    }

}
