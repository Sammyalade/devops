package africa.semicolon.userapplication.services;
import africa.semicolon.userapplication.dtos.request.SignUpRequest;

import africa.semicolon.userapplication.dtos.response.RegisterResponse;
import africa.semicolon.userapplication.entity.User;

public interface UserService {
     RegisterResponse signUp(SignUpRequest signUpRequest);

     User getUserByUsername(String username);
}
