package africa.semicolon.userapplication.services;
import africa.semicolon.userapplication.dtos.request.SignUpRequest;
import africa.semicolon.userapplication.dtos.response.RegisterResponse;

public interface UserService {
    public RegisterResponse signUp(SignUpRequest signUpRequest);
}
