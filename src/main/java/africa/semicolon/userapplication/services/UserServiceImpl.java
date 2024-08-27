package africa.semicolon.userapplication.services;

import africa.semicolon.userapplication.dtos.request.SignInRequest;
import africa.semicolon.userapplication.dtos.request.SignUpRequest;
import africa.semicolon.userapplication.dtos.response.RegisterResponse;
import africa.semicolon.userapplication.dtos.response.SignInResponse;
import africa.semicolon.userapplication.entity.User;
import africa.semicolon.userapplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository repository ;
    @Override
    public RegisterResponse signUp(SignUpRequest signUpRequest) {
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(signUpRequest.getPassword());
        repository.save(user);
        return new RegisterResponse(user.getUsername(),user.getPassword());
    }

    @Override
    public SignInResponse signIn(SignInRequest signIn) {
        Optional<User> user = repository
                .findByUsername(signIn.getUsername());

        if (user.isEmpty() || !user.get()
                .getPassword().equals(signIn.getPassword())) {
            return new SignInResponse(null,"invalid credential");
        }
        User userIn = user.get();
        return new SignInResponse(userIn.getUsername(),"Singed in successful");
    }
}


