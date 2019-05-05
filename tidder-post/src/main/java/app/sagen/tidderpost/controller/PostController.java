package app.sagen.tidderpost.controller;

import app.sagen.tidderpost.model.Post;
import app.sagen.tidderpost.model.TopicUserList;
import app.sagen.tidderpost.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
public class PostController {

    private PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/u/{user}")
    public List<Post> allPostsByUser(@PathVariable String user) {
        return postService.fetchPostsByUser(user);
    }

    @GetMapping("/t/{topic}")
    public List<Post> allPostsByTopic(@PathVariable String topic) {
        List<Post> posts = postService.fetchPostsByTopic(topic);
        System.out.println("********************************");
        System.out.println("RETUGNING POSTS FROM SERVICE -> " + posts);
        System.out.println("********************************");
        return posts;
    }

    @PostMapping("/feed")
    public List<Post> allPostsByUsersOrTopics(@RequestBody TopicUserList topicUserList) {
        return postService.fetchPostsByUsersOrTopics(topicUserList.getUsers(), topicUserList.getTopics());
    }

    @PostMapping("/")
    public Post createPost(@RequestBody Post post) {
        System.out.println("********************************");
        System.out.println("SENDING POST TO SERVICE FOR SAVING -> " + post);
        System.out.println("********************************");
        return postService.createPost(post);
    }

    @GetMapping("/")
    public List<Post> allPosts() {
        return postService.fetchAll();
    }

    @GetMapping("/p/{id}")
    public Post postById(@PathVariable long id) {
        Optional<Post> postOptional = postService.fetchPostById(id);
        postOptional.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found!"));
        return postOptional.get();
    }

    @PostMapping("/p/{postid}")
    public Post updatePost(@RequestBody Post post, @PathVariable long postid) {
        Optional<Post> postOptional = postService.updatePost(post, postid);
        postOptional.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found!"));
        return postOptional.get();
    }

    @DeleteMapping("/p/{postid}")
    public void deletePost(@PathVariable long postid) {
        postService.deletePost(postid);
    }

    @DeleteMapping("/u/{username}")
    public void deletePostsByUser(@PathVariable String username) {
        postService.deleteByUser(username);
    }

    @DeleteMapping("/t/{topic}")
    public void deletePostsByTopic(@PathVariable String topic) {
        postService.deleteByTopic(topic);
    }

}
