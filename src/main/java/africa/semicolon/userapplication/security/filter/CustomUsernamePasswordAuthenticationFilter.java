package africa.semicolon.userapplication.security.filter;

import africa.semicolon.userapplication.dtos.request.SignInRequest;
import africa.semicolon.userapplication.dtos.response.BaseResponse;
import africa.semicolon.userapplication.dtos.response.SignInResponse;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.Collection;


@AllArgsConstructor
public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            return authenticate(retrieveAuthCredentialsFrom(request));
        } catch (IOException e) {
            throw new BadCredentialsException(e.getMessage());
        }
    }

    private Authentication authenticate(SignInRequest signInRequest) {
        Authentication authentication = new
                UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword());
        Authentication authenticationResult =  authenticationManager.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(authenticationResult);
        return authenticationResult;
    }

    private SignInRequest retrieveAuthCredentialsFrom(HttpServletRequest request) throws IOException {
        InputStream requestBodyStream = request.getInputStream();
        return mapper.readValue(requestBodyStream, SignInRequest.class);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult)
            throws IOException, ServletException {

        SignInResponse signInResponse = new SignInResponse();
        signInResponse.setToken(generateAccessToken(authResult));
        signInResponse.setMessage("Successful Authentication");
        BaseResponse<SignInResponse> baseResponse = new BaseResponse<>();
        baseResponse.setData(signInResponse);
        baseResponse.setCode(HttpStatus.OK.value());
        baseResponse.setStatus(true);

        response.getOutputStream().write(mapper.writeValueAsBytes(baseResponse));
        response.flushBuffer();
        chain.doFilter(request, response);
    }

    private static String generateAccessToken(Authentication authResult) {
        return JWT.create()
                .withIssuer("MavericksHub")
                .withArrayClaim("roles",
                        getClaimsFrom(authResult.getAuthorities()))
                .withExpiresAt(Instant.now()
                        .plusSeconds(24*60*60))
                .sign(Algorithm.HMAC512("secret"));
    }

    private static String[] getClaimsFrom(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .toArray(String[]::new);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException exception) throws IOException, ServletException {
        SignInResponse signInResponse = new SignInResponse();
        signInResponse.setMessage(exception.getMessage());
        BaseResponse<SignInResponse> baseResponse = new BaseResponse<>();
        baseResponse.setData(signInResponse);
        baseResponse.setStatus(false);
        baseResponse.setCode(HttpStatus.UNAUTHORIZED.value());

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getOutputStream().write(mapper.writeValueAsBytes(baseResponse));
        response.flushBuffer();
    }
}
