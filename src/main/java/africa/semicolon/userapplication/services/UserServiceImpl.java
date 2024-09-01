package africa.semicolon.userapplication.services;

import africa.semicolon.userapplication.dtos.request.SignInRequest;
import africa.semicolon.userapplication.dtos.request.SignUpRequest;
import africa.semicolon.userapplication.dtos.response.RegisterResponse;
import africa.semicolon.userapplication.dtos.response.SignInResponse;
import africa.semicolon.userapplication.entity.User;
import africa.semicolon.userapplication.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository repository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public RegisterResponse signUp(SignUpRequest signUpRequest) {
        User user = modelMapper.map(signUpRequest, User.class);
        repository.save(user);
        return modelMapper.map(user, RegisterResponse.class);
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


