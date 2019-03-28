package app.sagen.tiddertest2.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    @RequestMapping("/")
    public String root() {
        return "Root av server 2";
    }

    @RequestMapping("/greeting")
    public String greeting() {
        return "Greeting from server 2";
    }

}
