package app.sagen.tidderpost.service;

import app.sagen.tidderpost.model.Post;
import app.sagen.tidderpost.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> fetchAll(String searchterm) {
        if(searchterm != null && !searchterm.trim().isEmpty())
            return postRepository.findAllByTopicContainsOrBodyContainsOrderByDate(searchterm, searchterm);
        return postRepository.findAllByOrderByDateDesc();
    }

    public Optional<Post> fetchPostById(long id) {
        return postRepository.findById(id);
    }

    public List<Post> fetchPostsByUser(String user) {
        return postRepository.findAllByOwnerOrderByDateDesc(user);
    }

    public List<Post> fetchPostsByTopic(String topic) {
        List<Post> allByTopicOrderByDate = postRepository.findAllByTopicOrderByDateDesc(topic);
        return allByTopicOrderByDate;
    }

    public List<Post> fetchPostsByUsersOrTopics(List<String> users, List<String> topics) {
        List<Post> allByTopicInOrOwnerInOrderByDateDesc = postRepository.findAllByTopicInOrOwnerInOrderByDateDesc(topics, users);

        System.out.println("********************************");
        System.out.println("FETCH POSTS BY USER OR TOPIC INPUT -> \n\t" + users + "\n\t" + topics);
        System.out.println("53 RETURNING POSTS FROM DATABASE -> " + allByTopicInOrOwnerInOrderByDateDesc);
        System.out.println("54 EVERY POST IN DB -> " + postRepository.findAll());
        System.out.println("********************************");

        return allByTopicInOrOwnerInOrderByDateDesc;
    }

    public void deleteByUser(String user) {
        postRepository.deleteAllByOwner(user);
    }

    public void deleteByTopic(String topic) {
        postRepository.deleteAllByTopic(topic);
    }

    public void deletePost(long id) {
        postRepository.deleteById(id);
    }

    public Post createPost(Post post) {
        Optional<Post> oldPost = postRepository.findById(post.getId());
        // disallow accidental overwrite
        return oldPost.orElseGet(() -> postRepository.save(post));
    }

    public Optional<Post> updatePost(Post post, long id) {
        Optional<Post> oldPost = postRepository.findById(id);
        if(!oldPost.isPresent()) return Optional.empty();
        oldPost.get().setBody(post.getBody());
        oldPost.get().setDate(post.getDate());
        oldPost.get().setOwner(post.getOwner());
        oldPost.get().setTitle(post.getTitle());
        oldPost.get().setTopic(post.getTopic());
        oldPost.get().setImage(post.getImage());
        oldPost.get().setUpvotes(post.getUpvotes());
        oldPost.get().setDownvotes(post.getDownvotes());
        oldPost.get().setVotes(post.getVotes());
        return Optional.of(postRepository.save(oldPost.get()));
    }

}
