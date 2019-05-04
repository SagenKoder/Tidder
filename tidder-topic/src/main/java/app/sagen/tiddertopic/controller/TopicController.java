package app.sagen.tiddertopic.controller;

import app.sagen.tiddertopic.model.Topic;
import app.sagen.tiddertopic.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
public class TopicController {

    TopicService topicService;

    @Autowired
    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @GetMapping
    public List<Topic> allTopics() {
        return topicService.fetchAllTopics();
    }

    @GetMapping("/topic/{topic}")
    public Topic topic(@PathVariable String topic) {
        Optional<Topic> topicOptional = topicService.fetchTopic(topic);
        topicOptional.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Topic not found!"));
        return topicOptional.get();
    }

    @GetMapping("/user/{userName}")
    public List<Topic> topicByUser(@PathVariable String userName) {
        return topicService.fetchTopicsByUser(userName);
    }

    @PostMapping("")
    public Topic createTopic(@RequestBody Topic topic) {
        return topicService.createTopic(topic);
    }

    @PostMapping("/topic/{topicName}")
    public Topic createTopic(@PathVariable String topicName, @RequestBody Topic topic) {
        Optional<Topic> topicOptional = topicService.updateTopic(topic, topicName);
        topicOptional.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Topic not found!"));
        return topicOptional.get();
    }

}
