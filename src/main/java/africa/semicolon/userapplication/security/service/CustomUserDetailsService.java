package africa.semicolon.userapplication.security.service;

import africa.semicolon.userapplication.entity.User;
import africa.semicolon.userapplication.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = userService.getUserByUsername(username);
            List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.name()))
                    .toList();
            return new org.springframework.security.core.userdetails.User
                    (user.getUsername(), user.getPassword(), authorities);
        } catch (UsernameNotFoundException e){
            throw new UsernameNotFoundException(e.getMessage());
        }
    }
}
