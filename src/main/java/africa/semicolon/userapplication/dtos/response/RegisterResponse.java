package africa.semicolon.userapplication.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class RegisterResponse {
    private String username;
    private String password;
}
