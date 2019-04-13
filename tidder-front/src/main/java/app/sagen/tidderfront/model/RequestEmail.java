package app.sagen.tidderfront.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestEmail {
    private String header;
    private String body;
}
