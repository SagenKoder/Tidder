package app.sagen.tidderpost.repository;

import app.sagen.tidderpost.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByOrderByDateDesc();

    List<Post> findAllByOwnerOrderByDateDesc(String owner);

    List<Post> findAllByTopicOrderByDateDesc(String topic);

    List<Post> findAllByTopicInOrOwnerInOrderByDateDesc(List<String> topic, List<String> owner);

    void deleteAllByOwner(String owner);

    void deleteAllByTopic(String topic);

}
