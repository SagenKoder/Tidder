package app.sagen.tidderfront.service;

import app.sagen.tidderfront.model.Post;
import app.sagen.tidderfront.model.TopicUserList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService {

    private RestTemplate restTemplate = new RestTemplate();
    private LoadBalancerClient loadBalancer;

    @Autowired
    private PostService(LoadBalancerClient loadBalancer) {
        this.loadBalancer = loadBalancer;
    }

    private URI getPostService() {
        ServiceInstance instance = loadBalancer.choose("post");
        return URI.create(String.format("http://%s:%s", instance.getHost(), instance.getPort()));
    }

    // GET /
    public List<Post> fetchAll(Optional<String> searchterm) {
        String search = searchterm.orElse("");
        URI uri = getPostService().resolve("/");
        try {
            return Arrays.stream(Objects.requireNonNull(restTemplate.postForObject(uri, search, Post[].class)))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // GET /p/{id}
    public Optional<Post> fetchPostById(long id) {
        URI uri = getPostService().resolve("/p/" + id);
        System.out.println("FIND BY POST Id URI: " + uri.toString());
        try {
            return Optional.ofNullable(restTemplate.getForObject(uri, Post.class));
        } catch (Exception e){
            e.printStackTrace();
            return Optional.empty();
        }
    }

    // GET /u/{username}
    public List<Post> fetchPostsByUser(String user) {
        URI uri = getPostService().resolve("/u/" + user);
        try {
            return Arrays.stream(Objects.requireNonNull(restTemplate.getForObject(uri, Post[].class)))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // GET /t/{topic}
    public List<Post> fetchPostsByTopic(String topic) {
        URI uri = getPostService().resolve("/t/" + topic);
        try {
            return Arrays.stream(Objects.requireNonNull(restTemplate.getForObject(uri, Post[].class)))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // POST /feed -> TopicUserList
    public List<Post> fetchPostsByUsersOrTopics(List<String> users, List<String> topics) {
        URI uri = getPostService().resolve("/feed");
        TopicUserList topicUserList = new TopicUserList();
        topicUserList.setTopics(topics);
        topicUserList.setUsers(users);
        try {
            return Arrays.stream(Objects.requireNonNull(restTemplate.postForObject(uri, topicUserList, Post[].class)))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // DELETE /u/{username}
    public void deleteByUser(String user) {
        try {
            URI uri = getPostService().resolve("/u/" + user);
            restTemplate.delete(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // DELETE /t/{topic}
    public void deleteByTopic(String topic) {
        try {
            URI uri = getPostService().resolve("/t/" + topic);
            restTemplate.delete(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // DELETE /p/{id}
    public void deletePost(long id) {
        try {
            URI uri = getPostService().resolve("/p/" + id);
            restTemplate.delete(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // POST / -> Post
    public Optional<Post> createPost(Post post) {
        URI uri = getPostService();
        System.out.println("********************************");
        System.out.println("SENDING POST TO POST SERVER -> " + post);
        System.out.println("********************************");
        try {
            return Optional.ofNullable(restTemplate.postForObject(uri, post, Post.class));
        } catch (Exception e){
            e.printStackTrace();
            return Optional.empty();
        }
    }

    // POST /p/{postid}
    public Optional<Post> updatePost(Post post, long id) {
        URI uri = getPostService().resolve("/p/" + id);
        try {
            return Optional.ofNullable(restTemplate.postForObject(uri, post, Post.class));
        } catch (Exception e){
            e.printStackTrace();
            return Optional.empty();
        }
    }

}
