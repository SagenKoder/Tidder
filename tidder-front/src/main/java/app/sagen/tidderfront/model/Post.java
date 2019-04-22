package app.sagen.tidderfront.model;

import lombok.Data;

@Data
public class Post {

    private long id;
    private User owner;
    private Topic topic;
    private String title;
    private String body;

}
