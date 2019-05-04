package app.sagen.tiddertopic.service;

import app.sagen.tiddertopic.model.Topic;
import app.sagen.tiddertopic.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Service
public class TopicService {

    private RestTemplate restTemplate = new RestTemplate();
    private LoadBalancerClient loadBalancer;
    private TopicRepository topicRepository;

    @Autowired
    public TopicService(LoadBalancerClient loadBalancer, TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
        this.loadBalancer = loadBalancer;
    }

    private URI getUserService() {
        ServiceInstance instance = loadBalancer.choose("user");
        return URI.create(String.format("http://%s:%s", instance.getHost(), instance.getPort()));
    }

    public Optional<Topic> fetchTopic(String name) {
        return topicRepository.findById(name);
    }

    public List<Topic> fetchTopicsByUser(String user) {
        return topicRepository.findAllByOwner(user);
    }

    public List<Topic> fetchAllTopics() {
        return topicRepository.findAll();
    }

    public Topic createTopic(Topic topic) {
        Optional<Topic> oldTopic = topicRepository.findById(topic.getName());
        // disallow accidental overwrite
        return oldTopic.orElseGet(() -> topicRepository.save(topic));
    }

    public Optional<Topic> updateTopic(Topic topic, String name) {
        Optional<Topic> oldTopic = topicRepository.findById(name);
        if(!oldTopic.isPresent()) return Optional.empty();
        oldTopic.get().setName(name);
        oldTopic.get().setOwner(topic.getOwner());
        oldTopic.get().setTitle(topic.getTitle());
        return Optional.of(topicRepository.save(oldTopic.get()));
    }
}
