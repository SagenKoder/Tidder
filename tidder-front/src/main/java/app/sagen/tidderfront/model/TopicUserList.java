package app.sagen.tidderfront.model;

import lombok.Data;

import java.util.List;

@Data
public class TopicUserList {

    List<String> users;
    List<String> topics;

}
