package app.sagen.tidderfront.controller;

import app.sagen.tidderfront.model.User;
import app.sagen.tidderfront.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/admin/user")
public class AdminUserController {

    private UserService userService;

    @Autowired
    AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String user(Model model) {
        Optional<User> user = userService.getAuthenticatedUser();
        if(!user.isPresent()) return "redirect:/login";
        model.addAttribute("currentUser", user.get());
        model.addAttribute("allUsers", userService.findAll());
        model.addAttribute("editUser", new User());
        return "adminUser";
    }

    @GetMapping("delete/{id}")
    public String deleteUser(@PathVariable("id") String id) {
        userService.delete(id);
        return "redirect:/admin/user";
    }

    @GetMapping("edit/{id}")
    public String editUser(Model model, @PathVariable("id") String id) {
        Optional<User> user = userService.getAuthenticatedUser();
        if(!user.isPresent()) return "redirect:/login";
        Optional<User> editUser = userService.findByUsername(id);
        if(!editUser.isPresent()) return "redirect:/admin/user";
        model.addAttribute("currentUser", user.get());
        model.addAttribute("allUsers", userService.findAll());
        model.addAttribute("editUser", editUser.get());
        return "adminUser";
    }

}
