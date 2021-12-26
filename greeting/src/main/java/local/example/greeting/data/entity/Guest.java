package local.example.greeting.data.entity;

import java.time.LocalDate;
import javax.persistence.Entity;
import local.example.greeting.data.AbstractEntity;

@Entity
public class Guest extends AbstractEntity {

    private String name;
    private String surname;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private String occupation;
    private boolean acknowledged;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    public String getOccupation() {
        return occupation;
    }
    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }
    public boolean isAcknowledged() {
        return acknowledged;
    }
    public void setAcknowledged(boolean acknowledged) {
        this.acknowledged = acknowledged;
    }

}
