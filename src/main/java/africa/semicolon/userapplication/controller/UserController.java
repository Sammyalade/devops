package africa.semicolon.userapplication.controller;

import africa.semicolon.userapplication.dtos.request.SignInRequest;
import africa.semicolon.userapplication.dtos.request.SignUpRequest;
import africa.semicolon.userapplication.dtos.response.SignInResponse;
import africa.semicolon.userapplication.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest signUpRequest){
        return new ResponseEntity<>(userService.signUp(signUpRequest), HttpStatus.OK);
    }

    @PostMapping("/signIn")
    public ResponseEntity<SignInResponse> loginUser(@RequestBody SignInRequest signIn) {
        SignInResponse response = userService.signIn(signIn);
        if (response.getMessage().equals("Login successful")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }
}

