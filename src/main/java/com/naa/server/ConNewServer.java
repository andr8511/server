package com.naa.server;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ConNewServer {

    private static String jwtToken;

    public static void main(String[] args) {
        if (authenticate("admin", "1234")) {
            // Теперь вы можете выполнять защищённые запросы
            performGetRequest();
            // Или вызвать другие методы запросов
        }
    }

    // Метод для аутентификации и получения JWT токена
    private static boolean authenticate(String username, String password) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            // Создаём тело запроса в формате JSON
            Map<String, String> requestBodyMap = Map.of(
                    "username", username,
                    "password", password
            );

            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(requestBodyMap);

            // Создаём POST-запрос на эндпоинт /api/auth/login
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/auth/login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            // Отправляем запрос и получаем ответ
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Проверяем статус ответа
            if (response.statusCode() == 200) {
                // Извлекаем JWT токен из ответа
                Map<String, String> responseBodyMap = objectMapper.readValue(response.body(), Map.class);
                jwtToken = responseBodyMap.get("token");
                System.out.println("JWT Token: " + jwtToken);
                return true;
            } else {
                // Обработка ошибок аутентификации
                System.out.println("Ошибка аутентификации. Статус: " + response.statusCode());
                System.out.println("Ответ сервера: " + response.body());
                return false;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Произошла ошибка при подключении к серверу.");
            return false;
        }
    }

    // Метод для выполнения GET-запроса с авторизацией
    private static void performGetRequest() {
        try {
            HttpClient client = HttpClient.newHttpClient();

            // Создаём GET-запрос с заголовком Authorization
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/protected-resource"))
                    .header("Authorization", "Bearer " + jwtToken)
                    .GET()
                    .build();

            // Отправляем запрос и получаем ответ
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Проверяем статус ответа
            if (response.statusCode() == 200) {
                System.out.println("Ответ от сервера: " + response.body());
            } else {
                System.out.println("Ошибка доступа к ресурсу. Статус: " + response.statusCode());
                System.out.println("Ответ сервера: " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Произошла ошибка при выполнении запроса.");
        }
    }

    private static void performPostRequest(String jsonData) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/protected-resource"))
                    .header("Authorization", "Bearer " + jwtToken)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonData))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("Ответ от сервера: " + response.body());
            } else {
                System.out.println("Ошибка доступа к ресурсу. Статус: " + response.statusCode());
                System.out.println("Ответ сервера: " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Произошла ошибка при выполнении запроса.");
        }
    }


    // Вы можете добавить методы для других типов запросов (POST, PUT, DELETE) по аналогии
}


