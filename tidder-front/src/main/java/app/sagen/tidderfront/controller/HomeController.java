package app.sagen.tidderfront.controller;

import app.sagen.tidderfront.model.Topic;
import app.sagen.tidderfront.model.User;
import app.sagen.tidderfront.service.TopicService;
import app.sagen.tidderfront.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/home")
public class HomeController {

    private TopicService topicService;
    private UserService userService;

    @Autowired
    HomeController(UserService userService, TopicService topicService) {
        this.topicService = topicService;
        this.userService = userService;
    }

    @GetMapping()
    public String homePage(Model model){
        System.out.println("FRONT :::::: HomeController :::::: GET homepage");
        Optional<User> user = userService.getAuthenticatedUser();
        model.addAttribute("currentUser", user.orElseGet(null));
        return "home";
    }

}
