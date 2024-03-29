package local.example.hail.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vaadin.fusion.Nonnull;
import java.util.Set;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import local.example.hail.data.AbstractEntity;
import local.example.hail.data.Role;

@Entity
public class User
        extends AbstractEntity {

    @Nonnull
    private String username;

    @Nonnull
    private String name;

    @JsonIgnore
    private String hashedPassword;

    @ElementCollection(fetch = FetchType.EAGER)
    @Nonnull
    private Set<Role> roles;

    @Nonnull
    @Lob
    private String profilePictureUrl;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }
}
