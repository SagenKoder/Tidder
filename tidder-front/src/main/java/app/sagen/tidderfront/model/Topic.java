package app.sagen.tidderfront.model;

import lombok.Data;

@Data
public class Topic {

    private User owner;
    private String name;
    private String title;

    public Topic(User owner, String name, String title) {
        this.owner = owner;
        this.name = name;
        this.title = title;
    }

}
