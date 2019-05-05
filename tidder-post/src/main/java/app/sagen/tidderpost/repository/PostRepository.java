package app.sagen.tidderpost.repository;

import app.sagen.tidderpost.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByOwnerOrderByDate(String owner);

    List<Post> findAllByTopicOrderByDate(String topic);

    List<Post> findAllByTopicInOrOwnerInOrderByDate(List<String> topics, List<String> owners);

    void deleteAllByOwner(String owner);

    void deleteAllByTopic(String topic);

}
