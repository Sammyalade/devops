package africa.semicolon.userapplication.security.utils;

import java.util.List;

public class SecurityUtils {

    private SecurityUtils() {}

    public static final List<String> PUBLIC_ENDPOINTS = List.of("/auth/signIn", "/auth/signUp");

    public static final String JWT_PREFIX = "Bearer ";
}
