package app.sagen.tidderpost.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
public class Post {

    @Id
    private long id;
    private String owner;
    private String topic;
    private LocalDateTime date;
    private String title;
    private String body;

    public Post(String owner, String topic, LocalDateTime date, String title, String body) {
        this.id = 0;
        this.owner = owner;
        this.topic = topic;
        this.date = date;
        this.title = title;
        this.body = body;
    }
}
