package app.sagen.tiddertopic.repository;

import app.sagen.tiddertopic.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicRepository extends JpaRepository<Topic, String> {

    List<Topic> findAllByOwner(String owner);

}
