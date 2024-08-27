package africa.semicolon.userapplication.services;
import africa.semicolon.userapplication.dtos.request.SignInRequest;
import africa.semicolon.userapplication.dtos.request.SignUpRequest;
import africa.semicolon.userapplication.dtos.response.SignInResponse;
import africa.semicolon.userapplication.dtos.response.RegisterResponse;

public interface UserService {
     RegisterResponse signUp(SignUpRequest signUpRequest);
     SignInResponse signIn(SignInRequest signIn);
}
