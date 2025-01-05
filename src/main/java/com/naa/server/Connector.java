package com.naa.server;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Connector {
    public static void main(String[] args) {
        // Создаем HTTP клиент с поддержкой cookie
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // 1. Выполняем POST-запрос для авторизации
            HttpPost loginPost = new HttpPost("http://localhost:8080/login");

            // Добавляем логин и пароль в запрос
            List<NameValuePair> loginParams = new ArrayList<>();
            loginParams.add(new BasicNameValuePair("username", "admin_user")); // Укажите логин
            loginParams.add(new BasicNameValuePair("password", "1234")); // Укажите пароль
            loginPost.setEntity(new UrlEncodedFormEntity(loginParams));

            try (CloseableHttpResponse loginResponse = httpClient.execute(loginPost)) {
                int loginStatus = loginResponse.getStatusLine().getStatusCode();
                if (loginStatus == 302 || loginStatus == 200) { // Успешный логин
                    System.out.println("Login completed. Status: " + loginStatus);

                    // 2. Выполняем GET-запрос для получения данных пользователя
                    HttpGet userGet = new HttpGet("http://localhost:8080/api/v1/me");
                    try (CloseableHttpResponse userResponse = httpClient.execute(userGet)) {
                        int userStatus = userResponse.getStatusLine().getStatusCode();
                        if (userStatus == 200) {
                            String userData = EntityUtils.toString(userResponse.getEntity());
                            System.out.println("User data received: " + userData);
                        } else {
                            System.out.println("Failed to fetch user data. Status: " + userStatus);
                        }
                    }
                } else {
                    System.out.println("Login failed. Status: " + loginStatus);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



