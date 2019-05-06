package app.sagen.tidderpost.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
public class Post {

    @Id
    @GeneratedValue
    private long id;
    private String owner;
    private String topic;
    private LocalDateTime date;
    private String title;
    private String body;
    @ElementCollection
    private Set<String> votes = new HashSet<>();
    private int upvotes = 0;
    private int downvotes = 0;

    @Lob()
    @Column(length = 52428800)
    private String image;

    public Post(String owner, String topic, LocalDateTime date, String title, String body, String image, Set<String> votes) {
        this.id = 0;
        this.owner = owner;
        this.topic = topic;
        this.date = date;
        this.title = title;
        this.body = body;
        this.image = image;
        this.votes = votes;
    }
}
