package app.sagen.tidderpost.model;

import lombok.Data;

import java.util.List;

@Data
public class TopicUserList {

    List<String> users;
    List<String> topics;

}
