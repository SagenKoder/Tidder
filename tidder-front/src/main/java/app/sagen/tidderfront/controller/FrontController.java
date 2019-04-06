package app.sagen.tidderfront.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/t")
public class FrontController {

    @GetMapping()
    public String topic() {
        return "redirect:/t/all";
    }

    @PostMapping()
    public String topic(@RequestParam String topic) {
        return "redirect:/t/" + topic;
    }

    @GetMapping("{topic}")
    public @ResponseBody String topic(Model model, @PathVariable("topic") String topic) {
        return "This is the topic site for " + topic;
    }

}
