package app.sagen.tidderfront.service;

import app.sagen.tidderfront.Role;
import app.sagen.tidderfront.model.User;
import app.sagen.tidderfront.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    private UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByEmail(email);
        if(!user.isPresent()) throw new UsernameNotFoundException("Not found user with email: " + email);
        return user.get();
    }

    public Optional<User> loadUserById(long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findUserByEmail(auth.getName());
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public long count() {
        return userRepository.count();
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void registerNewUser(User user) {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        Set<Role> newRoles = new HashSet<>();
        newRoles.add(Role.ROLE_USER);
        if(count() == 0) newRoles.add(Role.ROLE_ADMIN); // make the first user an administrator.
        user.setRoles(newRoles);
        save(user);
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    public void deleteById(long id) {
        userRepository.deleteById(id);
    }

}
