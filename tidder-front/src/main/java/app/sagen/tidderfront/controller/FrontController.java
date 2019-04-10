package app.sagen.tidderfront.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/t")
public class FrontController {

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
    public @ResponseBody String topic(HttpServletResponse response, Model model, @PathVariable("topic") String topic) {
        String filteredTopic = topic.toLowerCase();
        filteredTopic = filteredTopic.replaceAll("[^a-z0-9]", "");
        if(!filteredTopic.equals(topic)) {
            try {
                response.sendRedirect("/t/" + filteredTopic);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }
        return "This is the topic site for " + topic;
    }

}
