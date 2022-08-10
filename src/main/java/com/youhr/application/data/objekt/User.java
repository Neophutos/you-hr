package com.youhr.application.data.objekt;

import com.youhr.application.security.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Set;
import javax.persistence.*;

/**
 * @desc Das Objekt User setzt die Zugangsberechtigungen eines Mitarbeiters für das System.
 *
 * @attributes username, name, hashedPassword, roles, profilePictureUrl
 *
 * @mappedattributes id, mitarbeiter
 *
 * @category Objekt
 * @version 1.0
 * @since 2022-07-06
 */
@Entity
@Table(name = "application_user")
public class User extends AbstractID {

    private String username;
    private String name;
    @JsonIgnore
    private String hashedPassword;
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles;
    @Lob
    private String profilePictureUrl;

    @OneToOne(mappedBy = "user")
    private Mitarbeiter mitarbeiter;

    public User() {

        this.profilePictureUrl = " ";
        this.roles = roles;
        this.name = name;
    }

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
    public void addRole(Role role) {
        this.roles.add(role);
    }
    public void removeRole(Role role) {
        this.roles.remove(role);
    }
    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }
    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }
    public Mitarbeiter getMitarbeiter() {
        return mitarbeiter;
    }
}
