package app.sagen.tidderfront.controller;

import app.sagen.tidderfront.Role;
import app.sagen.tidderfront.model.ResponseSendtEmail;
import app.sagen.tidderfront.model.User;
import app.sagen.tidderfront.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @GetMapping("resetPassword/{id}")
    public String resetPassowrd(Model model, @PathVariable("id") String id) {
        Optional<User> user = userService.getAuthenticatedUser();
        if(!user.isPresent()) return "redirect:/login";
        Optional<User> editUser = userService.findByUsername(id);
        if(!editUser.isPresent()) return "redirect:/admin/user";

        String password = editUser.get().setAndReturnRandomPassword();

        Optional<ResponseSendtEmail> response = userService.sendEmail(user.get(), "Tidder.no - Your new password...", "Here is your new password: \"" + password + "\"");
        if(response.isPresent() && response.get().getStatus().equalsIgnoreCase("ok")) {
            userService.update(user.get(), user.get().getUsername());
        }
        return "redirect:/admin/user";
    }

    @PostMapping("save/{user}")
    public String updateUser(@PathVariable String user, @RequestParam String username, @RequestParam String firstName, @RequestParam String lastName, @RequestParam String email, @RequestParam List<String> roles) {
        User u = new User();
        u.setUsername(username);
        u.setFirstName(firstName);
        u.setLastName(lastName);
        u.setEmail(email);
        u.setRoles(roles.stream().map(Role::valueOf).collect(Collectors.toSet()));
        userService.update(u, username);
        return "redirect:/admin/user";
    }

}
