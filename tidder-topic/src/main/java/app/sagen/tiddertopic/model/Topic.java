package app.sagen.tiddertopic.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
public class Topic {

    @Id
    String name;
    String owner;
    String title;

}
