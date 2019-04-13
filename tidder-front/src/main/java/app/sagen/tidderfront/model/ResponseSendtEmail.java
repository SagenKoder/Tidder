package app.sagen.tidderfront.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseSendtEmail {
    private String username;
    private String head;
    private String body;
    private String status;
    private String message;
}
