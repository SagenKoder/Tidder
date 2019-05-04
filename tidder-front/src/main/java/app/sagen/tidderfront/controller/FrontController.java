package app.sagen.tidderfront.controller;

import app.sagen.tidderfront.model.Topic;
import app.sagen.tidderfront.model.User;
import app.sagen.tidderfront.service.PostService;
import app.sagen.tidderfront.service.TopicService;
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
    private PostService postService;
    private TopicService topicService;

    @Autowired
    FrontController(UserService userService, PostService postService, TopicService topicService) {
        this.userService = userService;
        this.postService = postService;
        this.topicService = topicService;
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

    @GetMapping("{topicName}")
    public String topic(HttpServletResponse response, Model model, @PathVariable("topicName") String topicName) {
        String filteredTopic = topicName.toLowerCase();
        filteredTopic = filteredTopic.replaceAll("[^a-z0-9]", "");
        if(!filteredTopic.equals(topicName)) return "redirect:/t/" + filteredTopic;
        topicName = filteredTopic;
        Optional<User> optuser = userService.getAuthenticatedUser();
        User user = null;
        if(optuser.isPresent()) user = optuser.get();

        Optional<Topic> topicOptional = topicService.fetchTopic(topicName);

        model.addAttribute("topicName", topicName);
        model.addAttribute("topic", topicOptional.orElse(null));
        if(topicOptional.isPresent()) model.addAttribute("posts", postService.fetchAll(topicName));

        model.addAttribute("currentUser", user);
        return "topic";
    }

}
