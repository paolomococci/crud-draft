package local.example.hail.data.entity;

import com.vaadin.fusion.Nonnull;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.validation.constraints.Email;

import local.example.hail.data.AbstractEntity;

@Entity
public class Guest
        extends AbstractEntity {

    @Nonnull
    private String name;

    @Nonnull
    private String surname;

    @Email
    @Nonnull
    private String email;

    @Nonnull
    private String phone;

    private LocalDate birthday;

    @Nonnull
    private String occupation;

    @Nonnull
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

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
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
