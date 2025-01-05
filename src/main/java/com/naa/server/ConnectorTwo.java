package com.naa.server;


import org.springframework.http.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class ConnectorTwo {
    public static void main(String[] args) {
        String loginUrl = "http://localhost:8080/login";
        String apiUrl = "http://localhost:8080/api/v1/me";
        String username = "admin_user";  // Укажите логин
        String password = "1234";  // Укажите пароль

        // Создаем WebClient
        WebClient webClient = WebClient.create();

        // Шаг 1: Аутентификация
        // Отправляем POST запрос для логина
        Mono<ResponseEntity<String>> loginResponse = webClient.post()
                .uri(loginUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue("username=" + username + "&password=" + password)
                .retrieve()
                .toEntity(String.class);

        // Получаем ответ от сервера
        loginResponse.subscribe(response -> {
            if (response.getStatusCode() == HttpStatus.OK) {
                // Шаг 2: Получаем данные пользователя
                // Отправляем GET запрос с куками для получения данных
                String cookie = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);

                Mono<ResponseEntity<String>> userResponse = webClient.get()
                        .uri(apiUrl)
                        .header(HttpHeaders.COOKIE, cookie)
                        .retrieve()
                        .toEntity(String.class);

                userResponse.subscribe(userResponseEntity -> {
                    if (userResponseEntity.getStatusCode() == HttpStatus.OK) {
                        System.out.println("Ответ от сервера: " + userResponseEntity.getBody());
                    } else {
                        System.out.println("Ошибка при получении данных: " + userResponseEntity.getStatusCode());
                    }
                });
            } else {
                System.out.println("Ошибка при аутентификации: " + response.getStatusCode());
            }
        });
    }
}
