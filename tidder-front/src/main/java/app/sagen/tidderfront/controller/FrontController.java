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
import java.util.List;
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

    @GetMapping("/{topic}/newPost")
    public String createPost(Model model, @PathVariable String topic) {
        if(!topicService.fetchTopic(topic.toLowerCase().trim()).isPresent()) {
            return "redirect:/t/" + topic;
        }
        Optional<User> user = userService.getAuthenticatedUser();
        if(!user.isPresent()) {
            return "redirect:/login";
        }
        model.addAttribute("currentUser", user.orElseGet(null));
        model.addAttribute("allTopics", topicService.fetchAllTopics());
        model.addAttribute("topicName", topic);
        return "newPost";
    }

    @PostMapping("/createTopic/{topicName}")
    public String createTopic(@PathVariable String topicName, @RequestParam String topicTitle) {
        Topic topic = new Topic();
        topic.setName(topicName.toLowerCase().trim());
        topic.setTitle(topicTitle);

        if(topic.getName().equalsIgnoreCase("all")) return "redirect:/all/"; // never claim "all"

        Optional<User> user = userService.getAuthenticatedUser(); // require login
        if(!user.isPresent()) return "redirect:/login";

        topic.setOwner(user.get().getUsername()); // set owner
        topicService.createTopic(topic); // save if not exists
        return "redirect:/t/" + topic.getName();
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

        if(topicName.equalsIgnoreCase("all")) {
            model.addAttribute("posts", postService.fetchAll());
            Topic topic = new Topic();
            topic.setTitle("A collection of every post on tidder");
            topic.setName("all");
            topic.setOwner("server");
            model.addAttribute("topic", topic);
        } else if(topicName.equalsIgnoreCase("feed")) {
            model.addAttribute("posts", postService.fetchPostsByUsersOrTopics(user.getUsers(), user.getTopics()));
            Topic topic = new Topic();
            topic.setTitle("A collection of posts from poeple and topics you follow");
            topic.setName("feed");
            topic.setOwner("server");
            model.addAttribute("topic", topic);
        } else if(topicOptional.isPresent()) {
            model.addAttribute("posts", postService.fetchPostsByTopic(topicName));
            model.addAttribute("topic", topicOptional.orElse(null));
        }

        model.addAttribute("allTopics", topicService.fetchAllTopics());
        model.addAttribute("currentUser", user);
        return "topic";
    }

}
