package app.sagen.tidderuser.service;

import app.sagen.tidderuser.Role;
import app.sagen.tidderuser.model.User;
import app.sagen.tidderuser.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    private UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        if(count() == 0) {
            User user = new User("sagen", "Alexander Meisdalen", "Sagen", "alexmsagen@gmail.com", "{noop}alex123");
            user.setRoles(Stream.of(Role.ROLE_USER, Role.ROLE_ADMIN).collect(Collectors.toSet()));
            userRepository.save(user);
        }
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public long count() {
        return userRepository.count();
    }

    public List<User> findAllByEmail(String email) {
        return userRepository.findAllByEmail(email);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    public User save(User user) {
        Optional<User> oldUserOpt = userRepository.findUserByUsername(user.getUsername());
        return oldUserOpt.map(user1 -> update(user1, user1.getUsername()).get()).orElseGet(() -> userRepository.save(user));
    }

    public Optional<User> update(User user, String username) {
        Optional<User> oldUserOpt = userRepository.findUserByUsername(username);
        if(oldUserOpt.isPresent()) {
            User oldUser = oldUserOpt.get();
            if(user.getEmail() != null)
                oldUser.setEmail(user.getEmail());
            if(user.getFirstName() != null)
                oldUser.setFirstName(user.getFirstName());
            if(user.getLastName() != null)
                oldUser.setLastName(user.getLastName());
            if(user.getPassword() != null)
                oldUser.setPassword(user.getPassword());
            if(user.getRoles() != null && !user.getRoles().isEmpty())
                oldUser.setRoles(user.getRoles());
            save(oldUser);
            return Optional.of(oldUser);
        }
        return Optional.empty();
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    public void delete(String username) {
        userRepository.delete(new User(username));
    }

}
