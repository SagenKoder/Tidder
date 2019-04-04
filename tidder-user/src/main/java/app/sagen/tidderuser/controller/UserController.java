package app.sagen.tidderuser.controller;

import app.sagen.tidderuser.exceptions.ResourceNotFoundException;
import app.sagen.tidderuser.model.RequestEmail;
import app.sagen.tidderuser.model.ResponseSendtEmail;
import app.sagen.tidderuser.model.User;
import app.sagen.tidderuser.service.EmailService;
import app.sagen.tidderuser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Value("${server.port}")
    private String serverPort;
    private EmailService emailService;
    private UserService userService;

    @Autowired
    public UserController(EmailService emailService, UserService userService) {
        this.emailService = emailService;
        this.userService = userService;
    }

    @GetMapping("/")
    public List<User> getAll() {
        return userService.findAll();
    }

    @GetMapping("/email/{email}")
    public List<User> getByEmail(@PathVariable String email) {
        return userService.findAllByEmail(email);
    }

    @GetMapping("/username/{username}")
    public User getByUsername(@PathVariable String username) {
        Optional<User> optUser = userService.findByUsername(username);
        return optUser.orElseGet(() -> {throw new ResourceNotFoundException("Could not find a user with the username: " + username);});
    }

    @RequestMapping("/")
    public String home() {
        StringBuilder response = new StringBuilder("<a>Welcome to the User Service!</a>\n");
        response.append("<a>I am assigned the following network interfaces:</a>\n");
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while(networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                StringBuilder inetData = new StringBuilder("<a>&nbsp&nbsp- " + networkInterface.getDisplayName() + "\n");
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                int num = 0;
                while(inetAddresses.hasMoreElements()) {
                    num++;
                    InetAddress inetAddress = inetAddresses.nextElement();
                    inetData.append("<a>&nbsp&nbsp&nbsp&nbsp- ").append(inetAddress.getHostAddress()).append("</a>\n");
                }
                if(num > 0) response.append(inetData);
            }
        } catch (Exception e) {
            response.append("<a>Exception while getting interface information! Error: ").append(e.getMessage());
            e.printStackTrace();
        }
        response.append("<a>Listening to port: ").append(serverPort).append("</a>\n");
        response.append("<a>Server time: ").append(new Date().toString()).append("</a>\n");

        return response.toString().replace("\n", "<br/>\n");
    }

    @PostMapping("/username/{username}/sendEmail")
    public ResponseSendtEmail sendMailUser(@PathVariable String username, @RequestBody RequestEmail requestEmail) {
        Optional<User> optUser = userService.findByUsername(username);
        if(!optUser.isPresent()) {
            new ResponseSendtEmail(username, requestEmail.getHeader(), requestEmail.getBody(), "ERROR", "Could not find the requested user!");
        }
        emailService.sendSimpleEmail(username, requestEmail.getHeader(), requestEmail.getBody());
        return new ResponseSendtEmail(username, requestEmail.getHeader(), requestEmail.getBody(), "OK", "Email successfully sent!");
    }

    @PostMapping("/sendMail/{email}")
    public ResponseSendtEmail sendMailEmail(@PathVariable() String email, @RequestBody RequestEmail requestEmail) {
        emailService.sendSimpleEmail(email, requestEmail.getHeader(), requestEmail.getBody());
        return new ResponseSendtEmail(email, requestEmail.getHeader(), requestEmail.getBody(), "OK", "Email successfully sent!");
    }

}
