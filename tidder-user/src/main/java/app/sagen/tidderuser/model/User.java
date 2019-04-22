package app.sagen.tidderuser.model;

import app.sagen.tidderuser.Role;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class User {

    @Id
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Date passwordSetDate;
    private Date passwordExpires;
    private String passwordSetBy;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    public User(String username) {
        this.username = username;
    }

    public User(String username, String firstName, String lastName, String email, String password) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.passwordSetDate = new Date();
        this.passwordExpires = new Date(System.currentTimeMillis() + 30*6*(long)(8.64e+7));
        this.passwordSetBy = "System";
    }

    public void setAndEncryptPassword(String password) {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        setPassword(encoder.encode(password));
    }

    public String setAndReturnRandomPassword() {
        int passwordLength = 16;
        Random random = new SecureRandom();
        String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder pass = new StringBuilder(passwordLength);
        for (int i = 0; i < passwordLength; i++) {
            pass.append(alphabet.charAt(random.nextInt(alphabet.length())));
        }
        String password = pass.toString();
        setAndEncryptPassword(password);
        return password;
    }
}
