package africa.semicolon.userapplication.security.filter;

import africa.semicolon.userapplication.security.service.TokenService;
import africa.semicolon.userapplication.security.roles.Role;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static africa.semicolon.userapplication.security.utils.SecurityUtils.JWT_PREFIX;
import static africa.semicolon.userapplication.security.utils.SecurityUtils.PUBLIC_ENDPOINTS;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    public CustomAuthorizationFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String requestPath = request.getServletPath();
        boolean isRequestPathPublic = PUBLIC_ENDPOINTS.contains(requestPath);
        if (isRequestPathPublic) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith(JWT_PREFIX)) {
            try {
                String token = authorizationHeader.substring(JWT_PREFIX.length()).strip();
                String username = tokenService.getUsernameFromToken(token);
                List<Role> roles = tokenService.getRolesFromToken(token);

                List<SimpleGrantedAuthority> authorities = Role.toAuthorities(roles);

                Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (RuntimeException e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
