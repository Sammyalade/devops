package africa.semicolon.userapplication.services;

import africa.semicolon.userapplication.dtos.request.SignInRequest;
import africa.semicolon.userapplication.dtos.request.SignUpRequest;
import africa.semicolon.userapplication.dtos.response.RegisterResponse;
import africa.semicolon.userapplication.dtos.response.SignInResponse;
import africa.semicolon.userapplication.entity.User;
import africa.semicolon.userapplication.repository.UserRepository;
import africa.semicolon.userapplication.security.roles.Role;
import africa.semicolon.userapplication.security.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Autowired
    public UserServiceImpl(UserRepository repository, BCryptPasswordEncoder passwordEncoder, TokenService tokenService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @Override
    public RegisterResponse signUp(SignUpRequest signUpRequest) {
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        repository.save(user);
        return new RegisterResponse(user.getUsername(), "Registration successful");
    }

    @Override
    public SignInResponse signIn(SignInRequest signInRequest) {
        Optional<User> userOptional = repository.findByUsername(signInRequest.getUsername());

        if (userOptional.isEmpty() || !passwordEncoder.matches(signInRequest.getPassword(), userOptional.get().getPassword())) {
            return new SignInResponse(null, "Invalid credentials", null);
        }

        User user = userOptional.get();
        List<Role> roles = getUserRoles(user);
        String token = tokenService.generateToken(user.getUsername(), roles);
        return new SignInResponse(user.getUsername(), "Sign-in successful", token);
    }

    private List<Role> getUserRoles(User user) {
        return user.getRoles();
    }
}
