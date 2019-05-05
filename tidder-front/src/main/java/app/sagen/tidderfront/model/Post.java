package app.sagen.tidderfront.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;

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

    public Post(String owner, String topic, LocalDateTime date, String title, String body) {
        this.id = 0;
        this.owner = owner;
        this.topic = topic;
        this.date = date;
        this.title = title;
        this.body = body;
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
}
