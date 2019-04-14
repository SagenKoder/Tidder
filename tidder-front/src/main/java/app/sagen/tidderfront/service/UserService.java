package app.sagen.tidderfront.service;

import app.sagen.tidderfront.Role;
import app.sagen.tidderfront.model.RequestEmail;
import app.sagen.tidderfront.model.ResponseSendtEmail;
import app.sagen.tidderfront.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private RestTemplate restTemplate = new RestTemplate();
    private LoadBalancerClient loadBalancer;

    private URI getUserService() {
        ServiceInstance instance = loadBalancer.choose("user");
        return URI.create(String.format("http://%s:%s", instance.getHost(), instance.getPort()));
    }

    @Autowired
    private UserService(LoadBalancerClient loadBalancer) {
        this.loadBalancer = loadBalancer;
    }

    public List<User> findAll() {
        return Arrays.stream(Objects.requireNonNull(restTemplate.getForObject(getUserService(), User[].class)))
                .collect(Collectors.toList());
    }

    public long count() {
        return findAll().size();
    }

    public List<User> findAllByEmail(String email) {
        URI uri = getUserService().resolve("/email/" + email);
        return Arrays.stream(Objects.requireNonNull(restTemplate.getForObject(uri, User[].class)))
                .collect(Collectors.toList());
    }

    public Optional<User> findByUsername(String username) {
        URI uri = getUserService().resolve("/u/" + username);
        System.out.println("FIND BY USERNAME URI: " + uri.toString());
        try {
            return Optional.ofNullable(restTemplate.getForObject(uri, User.class));
        } catch (Exception e){
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<User> save(User user) {
        try {
            return Optional.ofNullable(restTemplate.postForObject(getUserService(), user, User.class));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<User> update(User user, String username) {
        try {
            return Optional.ofNullable(restTemplate.postForObject(getUserService().resolve("/u/" + username), user, User.class));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public void delete(User user) {
        try {
            restTemplate.delete(getUserService().resolve("/u/" + user.getUsername()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(String username) {
        try {
            restTemplate.delete(getUserService().resolve("/u/" + username));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Optional<User> getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return findByUsername(auth.getName());
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = findByUsername(username);
        if(!user.isPresent()) throw new UsernameNotFoundException("Not found user with email: " + username);
        return user.get();
    }

    public Optional<ResponseSendtEmail> sendEmail(User user, String header, String body) {
        URI uri = getUserService().resolve("/u/" + user.getUsername() + "/sendEmail");
        try {
            return Optional.ofNullable(restTemplate.postForObject(uri, new RequestEmail(header, body), ResponseSendtEmail.class));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

}
