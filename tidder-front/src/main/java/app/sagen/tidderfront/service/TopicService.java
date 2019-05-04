package app.sagen.tidderfront.service;

import app.sagen.tidderfront.model.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

public class TopicService {

    private RestTemplate restTemplate = new RestTemplate();
    private LoadBalancerClient loadBalancer;

    private URI getTopicURI() {
        ServiceInstance instance = loadBalancer.choose("topic");
        return URI.create(String.format("http://%s:%s", instance.getHost(), instance.getPort()));
    }

    @Autowired
    private TopicService(LoadBalancerClient loadBalancer) {
        this.loadBalancer = loadBalancer;
    }

    public Optional<Topic> fetchTopic(String name) {
        URI uri = getTopicURI().resolve("/topic/" + name);
        System.out.println("FIND BY TOPIC NAME URI: " + uri.toString());
        try {
            return Optional.ofNullable(restTemplate.getForObject(uri, Topic.class));
        } catch (Exception e){
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public List<Topic> fetchTopicsByUser(String user) {
        URI uri = getTopicURI().resolve("/user/" + user);
        try {
            return Arrays.stream(Objects.requireNonNull(restTemplate.getForObject(uri, Topic[].class)))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<Topic> fetchAllTopics() {
        URI uri = getTopicURI();
        try {
            return Arrays.stream(Objects.requireNonNull(restTemplate.getForObject(uri, Topic[].class)))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public Optional<Topic> createTopic(Topic topic) {
        try {
            return Optional.ofNullable(restTemplate.postForObject(getTopicURI(), topic, Topic.class));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<Topic> updateTopic(Topic topic, String name) {
        try {
            return Optional.ofNullable(restTemplate.postForObject(getTopicURI().resolve("/topic/" + name), topic, Topic.class));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

}
