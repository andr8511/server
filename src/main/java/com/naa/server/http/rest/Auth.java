//package com.naa.server.http.rest;
//
//import com.naa.server.dto.JwtDto;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//
//import java.security.Key;
//import java.util.Date;
//
//@RestController
//@RequestMapping("api/auth")
//public class Auth {
//
//    private final PasswordEncoder passwordEncoder;
//    private final AuthenticationManager authenticationManager;
//
//    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512); // Сохрани этот ключ в безопасном месте
//    private final long jwtExpirationInMs = 3600000; // 1 час
//
//    public Auth(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
//        this.authenticationManager = authenticationManager;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    @GetMapping
//    public ResponseEntity<?> authenticateUser() {
//        try {
//
//            // 1. Создание токена аутентификации с предоставленными данными
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken("admin", "1234")
//            );
//
//            // 2. Установка аутентификации в контекст безопасности
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//
//            // 3. Генерация JWT токена
//            String jwt = generateJwtToken(authentication);
//
//            // 4. Возврат JWT в ответе
//            return ResponseEntity.ok( new JwtDto(jwt));
//        } catch (BadCredentialsException e) {
//            // Обработка некорректных учетных данных
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неверный логин или пароль");
//        } catch (Exception e) {
//            // Обработка других возможных ошибок
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Произошла ошибка при аутентификации");
//        }
//    }
//
//    private String generateJwtToken(Authentication authentication) {
//
//        String username = authentication.getName();
//        Date now = new Date();
//        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
//
//        return Jwts.builder()
//                .setSubject(username)
//                .setIssuedAt(now)
//                .setExpiration(expiryDate)
//                .signWith(key)
//                .compact();
//    }
//
//}

