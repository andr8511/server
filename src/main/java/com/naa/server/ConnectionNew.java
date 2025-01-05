package com.naa.server;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ConnectionNew {
    private static String jwtToken;

    public static void main(String[] args) {
        if (authenticate("admin_user", "1234")) {
            // Используем токен для последующих запросов
            performGetRequest();
        }
    }

    private static boolean authenticate(String username, String password) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            // Создаем тело запроса
            Map<String, String> requestBodyMap = Map.of(
                    "username", username,
                    "password", password
            );
            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(requestBodyMap);

            // Создаем POST-запрос
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/auth/login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            // Отправляем запрос и получаем ответ
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // Получаем JWT токен из ответа
                Map<String, String> responseBodyMap = objectMapper.readValue(response.body(), Map.class);
                jwtToken = responseBodyMap.get("token");
                System.out.println("JWT Token: " + jwtToken);
                return true;
            } else {
                System.out.println("Неверный логин или пароль.");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Произошла ошибка при подключении к серверу.");
            return false;
        }
    }

    private static void performGetRequest() {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/protected-resource"))
                    .header("Authorization", "Bearer " + jwtToken)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("Ответ от сервера: " + response.body());
            } else {
                System.out.println("Ошибка доступа к ресурсу. Статус: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Произошла ошибка при выполнении запроса.");
        }
    }
}


