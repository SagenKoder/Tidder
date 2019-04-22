package app.sagen.tidderfront.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Post {

    private long id;
    private User owner;
    private Topic topic;
    private LocalDateTime date;
    private String title;
    private String body;

    public Post(User owner, Topic topic, LocalDateTime date, String title, String body) {
        this.id = 0;
        this.owner = owner;
        this.topic = topic;
        this.date = date;
        this.title = title;
        this.body = body;
    }
}
