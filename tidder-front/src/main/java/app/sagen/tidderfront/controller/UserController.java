package app.sagen.tidderfront.controller;

import app.sagen.tidderfront.model.User;
import app.sagen.tidderfront.service.PostService;
import app.sagen.tidderfront.service.TopicService;
import app.sagen.tidderfront.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller()
@RequestMapping("/u")
public class UserController {

    private UserService userService;
    private PostService postService;
    private TopicService topicService;

    @Autowired
    UserController(UserService userService, PostService postService, TopicService topicService) {
        this.userService = userService;
        this.postService = postService;
        this.topicService = topicService;
    }

    @GetMapping("{user}")
    public String userSite(Model model, @PathVariable String username) {
        Optional<User> userOptional = userService.getAuthenticatedUser();
        model.addAttribute("currentUser", userOptional.orElseGet(null));
        Optional<User> selectedUserOpt = userService.findByUsername(username);
        if(!selectedUserOpt.isPresent()) return "redirect:/t/all";
        model.addAttribute("user", selectedUserOpt.get());
        model.addAttribute("posts", postService.fetchPostsByUser(username));
        return "user";
    }

}
