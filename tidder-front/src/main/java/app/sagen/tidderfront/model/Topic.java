package app.sagen.tidderfront.model;

import lombok.Data;

@Data
public class Topic {

    String name;
    User owner;
    String title;

}
