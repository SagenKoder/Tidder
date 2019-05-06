package app.sagen.tidderfront.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class Post {

    private long id;
    private String owner;
    private String topic;
    private LocalDateTime date;
    private String title;
    private String body;
    private String image;
    private Set<String> votes = new HashSet<>();
    private int upvotes = 0;
    private int downvotes = 0;

    public Post(String owner, String topic, LocalDateTime date, String title, String body) {
        this.id = 0;
        this.owner = owner;
        this.topic = topic;
        this.date = date;
        this.title = title;
        this.body = body;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setImage(MultipartFile file) {
        if(file == null) return;
        try {
            byte[] encoded = Base64.getEncoder().encode(file.getBytes());
            this.image = new String(encoded, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Duration getTimeSince() {
        return Duration.between(date, LocalDateTime.now());
    }

    public String humanTimeSince() {
        return getTimeSince().toString()
                .substring(2)
                .replaceAll("(\\d[HMS])(?!$)", "$1 ")
                .toLowerCase();
    }
}
