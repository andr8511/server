package com.naa.server.http.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class LoginController {

    private final Key key;
    private final long jwtExpirationInMs = 3600000; // 1 час

    public LoginController(Key key, AuthenticationManager authenticationManager) {
        this.key = key;
        this.authenticationManager = authenticationManager;
    }

    private final AuthenticationManager authenticationManager;

    @GetMapping("/login")
    public String loginPage(){
        return "user/login";
    }

    @PostMapping("/login")
    public String authenticateUser(@RequestParam String username,
                                   @RequestParam String password,
                                   HttpServletResponse response) {
        // Логика аутентификации пользователя
        // Если аутентификация успешна, создайте JWT-токен
        String jwtToken = generateJwtToken(username, password);

        // Сохраните токен в cookie
        Cookie jwtCookie = new Cookie("JWT_TOKEN", jwtToken);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(3600); // Время жизни токена в секундах
        response.addCookie(jwtCookie);

        // Перенаправление на главную страницу или необходимый ресурс
        return "redirect:/";
    }
    private String generateJwtToken(String username, String password) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        password
                )
        );

        String userName = authentication.getName();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        // Получаем роли пользователя
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(userName)
                .claim("roles", roles)
                // Добавьте другие необходимые данные
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }
}
