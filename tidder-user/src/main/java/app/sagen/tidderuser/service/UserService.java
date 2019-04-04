package app.sagen.tidderuser.service;

import app.sagen.tidderuser.model.User;
import app.sagen.tidderuser.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    private UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        if(count() == 0) {
            userRepository.save(new User("sagen", "Alexander Meisdalen", "Sagen", "alexmsagen@gmail.com", "SomeRandomPasswordHash"));
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

    public void save(User user) {
        Optional<User> oldUserOpt = userRepository.findUserByUsername(user.getUsername());
        if(oldUserOpt.isPresent()) {
            update(oldUserOpt.get(), oldUserOpt.get().getUsername());
        }
        else {
            userRepository.save(user);
        }
    }

    public void update(User user, String username) {
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
        }
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    public void delete(String username) {
        userRepository.delete(new User(username));
    }

}
