package com.naa.server;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class Connection {

    private static String jwtToken;

    public static void main(String[] args) {
        if (authenticate("admin", "1234")) {
            // Теперь вы можете выполнять защищённые запросы
            performGetRequest();
            performGetRequestById(1L);
            uploadFile(Path.of("C:\\Users\\37533\\OneDrive\\Рабочий стол\\список для Андрея для расчета.xlsx"));
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

            System.out.println("Статус ответа: " + response.statusCode());
            System.out.println("Ответ сервера: " + response.body());

            if (response.statusCode() == 200) {
                // Извлекаем JWT токен из ответа
                Map<String, String> responseBodyMap = objectMapper.readValue(response.body(), Map.class);
                jwtToken = responseBodyMap.get("token");
                System.out.println("JWT Token: " + jwtToken);
                return true;
            } else {
                System.out.println("Ошибка аутентификации.");
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
                    .uri(URI.create("http://localhost:8080/api/v1/users"))
                    .header("Authorization", "Bearer " + jwtToken)
                    .GET()
                    .build();

            // Отправляем запрос и получаем ответ
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Статус ответа: " + response.statusCode());
            System.out.println("Ответ сервера: " + response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Произошла ошибка при выполнении запроса.");
        }
    }

    // Пример метода для получения пользователя по ID

    private static void performGetRequestById(Long id) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/v1/users/" + id))
                .header("Authorization", "Bearer " + jwtToken)
                .GET()
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Статус ответа: " + response.statusCode());
            System.out.println("Ответ сервера: " + response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Произошла ошибка при выполнении запроса.");
        }
    }

    private static void uploadFile(Path filePath) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            // Уникальный разделитель для multipart/form-data
            String boundary = "Boundary-" + System.currentTimeMillis();

            // Создаем тело запроса в формате multipart/form-data
            String CRLF = "\r\n"; // Стандартный перевод строки в HTTP
            StringBuilder sb = new StringBuilder();

            // Заголовок для поля файла
            sb.append("--").append(boundary).append(CRLF);
            sb.append("Content-Disposition: form-data; name=\"file\"; filename=\"")
                    .append(filePath.getFileName().toString()).append("\"").append(CRLF);
            sb.append("Content-Type: application/vnd.openxmlformats-officedocument.spreadsheetml.sheet").append(CRLF);
            sb.append(CRLF);

            byte[] fileBytes = Files.readAllBytes(filePath);
            byte[] headerBytes = sb.toString().getBytes(StandardCharsets.UTF_8);
            byte[] footerBytes = (CRLF + "--" + boundary + "--" + CRLF).getBytes(StandardCharsets.UTF_8);

            // Создаем BodyPublisher из частей
            HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofByteArrays(
                    Arrays.asList(
                            headerBytes,
                            fileBytes,
                            footerBytes
                    )
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/v1/files/upload"))
                    .header("Authorization", "Bearer " + jwtToken)
                    .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                    .POST(bodyPublisher)
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Статус ответа: " + response.statusCode());
            System.out.println("Ответ сервера: " + response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Произошла ошибка при выполнении запроса.");
        }
    }

}
