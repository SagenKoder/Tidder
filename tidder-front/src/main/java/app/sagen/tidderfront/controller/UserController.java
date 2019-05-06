package app.sagen.tidderfront.controller;

import app.sagen.tidderfront.model.User;
import app.sagen.tidderfront.service.PostService;
import app.sagen.tidderfront.service.TopicService;
import app.sagen.tidderfront.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("{username}")
    public String userSite(Model model, @PathVariable String username) {
        Optional<User> userOptional = userService.getAuthenticatedUser();
        model.addAttribute("currentUser", userOptional.orElse(null));
        Optional<User> selectedUserOpt = userService.findByUsername(username);
        if(!selectedUserOpt.isPresent()) return "redirect:/t/all";
        model.addAttribute("user", selectedUserOpt.get());
        model.addAttribute("posts", postService.fetchPostsByUser(username));
        return "user";
    }

    @GetMapping("{username}/follow")
    public String followUser(@PathVariable String username) {
        Optional<User> userOptional = userService.getAuthenticatedUser();
        if(!userOptional.isPresent()) return "redirect:/login";
        Optional<User> selectedUserOpt = userService.findByUsername(username);
        if(!selectedUserOpt.isPresent()) return "redirect:/t/all";

        userOptional.get().getUsers().add(selectedUserOpt.get().getUsername());
        userService.update(userOptional.get(), userOptional.get().getUsername());

        return "redirect:/u/" + username;
    }

    @GetMapping("{username}/unfollow")
    public String unfollowUser(@PathVariable String username) {
        Optional<User> userOptional = userService.getAuthenticatedUser();
        if(!userOptional.isPresent()) return "redirect:/login";

        userOptional.get().getUsers().remove(username);
        userService.update(userOptional.get(), userOptional.get().getUsername());

        return "redirect:/u/" + username;
    }

    @PostMapping("{username}/changePassword")
    public String changePassword(@PathVariable String username, @RequestParam String password) {
        Optional<User> authenticatedUser = userService.getAuthenticatedUser();
        if(!authenticatedUser.isPresent()) return "redirect:/u/" + username;

        authenticatedUser.get().setAndEncryptPassword(password);
        userService.update(authenticatedUser.get(), authenticatedUser.get().getUsername());
        return "redirect:/u/" + username;
    }

}
