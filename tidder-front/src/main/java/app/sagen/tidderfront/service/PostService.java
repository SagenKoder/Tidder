package app.sagen.tidderfront.service;

import app.sagen.tidderfront.model.Post;
import app.sagen.tidderfront.model.Topic;
import app.sagen.tidderfront.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class PostService {

    private RestTemplate restTemplate = new RestTemplate();
    private LoadBalancerClient loadBalancer;
    private UserService userService;

    @Autowired
    private PostService(LoadBalancerClient loadBalancer, UserService userService) {
        this.loadBalancer = loadBalancer;
        this.userService = userService;
    }

    private URI getPostService() {
        ServiceInstance instance = loadBalancer.choose("post");
        return URI.create(String.format("http://%s:%s", instance.getHost(), instance.getPort()));
    }

    public List<Post> fetchAll(String topic) {
        return Collections.emptyList();
    }

}
