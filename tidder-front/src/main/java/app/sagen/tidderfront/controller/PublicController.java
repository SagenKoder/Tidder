package app.sagen.tidderfront.controller;

import app.sagen.tidderfront.model.User;
import app.sagen.tidderfront.service.TopicService;
import app.sagen.tidderfront.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class PublicController {

    private UserService userService;
    private TopicService topicService;

    @Autowired
    PublicController(UserService userService, TopicService topicService) {
        this.topicService = topicService;
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(Model model, @RequestParam(value = "error", required = false) String err){
        System.out.println("FRONT :::::: PublicController :::::: GET login");
        Optional<User> user = userService.getAuthenticatedUser();
        user.ifPresent(user1 -> model.addAttribute("currentUser", user1));
        model.addAttribute("error", err != null);
        return "login";
    }

    @GetMapping("/signup")
    public String signup(Model model){
        System.out.println("FRONT :::::: PublicController :::::: GET signup");
        Optional<User> user = userService.getAuthenticatedUser();
        user.ifPresent(user1 -> model.addAttribute("currentUser", user1));
        model.addAttribute("createAdmin", userService.count() == 0);
        return "signup";
    }

    @PostMapping("/signup")
    public String register(@ModelAttribute("user") User user){
        if(user.getUsername().equalsIgnoreCase("anonymoususer")) return "redirect:/signup";
        System.out.println("FRONT :::::: PublicController :::::: POST signup");
        userService.registerNewUser(user);
        return "redirect:/login";
    }

    @GetMapping("/")
    public String homePage(Model model ){
        System.out.println("FRONT :::::: PublicController :::::: GET homepage");
        Optional<User> user = userService.getAuthenticatedUser();
        user.ifPresent(user1 -> model.addAttribute("currentUser", user1));
        model.addAttribute("allTopics", topicService.fetchAllTopics());
        return "index";
    }

}
