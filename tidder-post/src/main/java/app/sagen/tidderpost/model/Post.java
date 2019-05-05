package app.sagen.tidderpost.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
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

    @Lob()
    @Column(length = 52428800)
    private String image;

    public Post(String owner, String topic, LocalDateTime date, String title, String body, String image) {
        this.id = 0;
        this.owner = owner;
        this.topic = topic;
        this.date = date;
        this.title = title;
        this.body = body;
        this.image = image;
    }
}
