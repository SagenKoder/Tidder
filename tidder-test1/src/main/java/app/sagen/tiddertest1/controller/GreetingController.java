package app.sagen.tiddertest1.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    @RequestMapping("/")
    public String root() {
        return "Root av server 1";
    }

    @RequestMapping("/greeting")
    public String greeting() {
        return "Greeting from server 1";
    }

}
