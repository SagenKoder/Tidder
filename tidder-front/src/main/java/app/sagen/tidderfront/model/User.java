package app.sagen.tidderfront.model;

import app.sagen.tidderfront.Role;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class User implements UserDetails {

    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Set<Role> roles = new HashSet<>();
    private Date passwordSetDate;
    private Date passwordExpires;
    private String passwordSetBy;

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

    /* ROLE_USER-DETAILS IMPLEMENTATION */

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(Role::getGrantedAuthority).collect(Collectors.toSet());
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
