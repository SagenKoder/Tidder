package app.sagen.tidderfront.controller;

import app.sagen.tidderfront.model.User;
import app.sagen.tidderfront.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Controller
@RequestMapping("/t")
public class FrontController {

    private UserService userService;

    @Autowired
    FrontController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String topic() {
        return "redirect:/t/all";
    }

    @PostMapping()
    public String topic(@RequestParam String topic) {
        topic = topic.toLowerCase();
        topic = topic.replaceAll("[^a-z0-9]", "");
        return "redirect:/t/" + topic;
    }

    @GetMapping("{topic}")
    public String topic(HttpServletResponse response, Model model, @PathVariable("topic") String topic) {
        String filteredTopic = topic.toLowerCase();
        filteredTopic = filteredTopic.replaceAll("[^a-z0-9]", "");
        if(!filteredTopic.equals(topic)) return "redirect:/t/" + filteredTopic;
        Optional<User> optuser = userService.getAuthenticatedUser();
        User user = null;
        if(optuser.isPresent()) user = optuser.get();
        model.addAttribute("currentUser", user);
        model.addAttribute("topicExists", false);
        model.addAttribute("topicName", topic);
        return "topic";
    }

}
