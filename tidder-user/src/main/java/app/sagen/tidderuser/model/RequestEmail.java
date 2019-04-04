package app.sagen.tidderuser.model;

import lombok.Data;

@Data
public class RequestEmail {
    private String header;
    private String body;
}
