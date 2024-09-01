package africa.semicolon.userapplication.services;

import africa.semicolon.userapplication.dtos.request.SignInRequest;
import africa.semicolon.userapplication.dtos.request.SignUpRequest;
import africa.semicolon.userapplication.dtos.response.RegisterResponse;
import africa.semicolon.userapplication.dtos.response.SignInResponse;
import africa.semicolon.userapplication.entity.User;
import africa.semicolon.userapplication.repository.UserRepository;
import africa.semicolon.userapplication.security.roles.Role;
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
        user.getRoles().add(Role.USER);
        repository.save(user);
        return modelMapper.map(user, RegisterResponse.class);
    }

}