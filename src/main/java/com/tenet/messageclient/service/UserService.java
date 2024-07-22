package com.tenet.messageclient.service;

import com.tenet.messageclient.config.RequestConfiguration;
import com.tenet.messageclient.request.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Service
public class UserService {
    private final RestTemplate restTemplate;

    public UserService() {
        this.restTemplate = new RestTemplate();
    }

    public List<String> getUserLogins(RequestConfiguration configuration) {
        String url = configuration.getUrl() + "/api/v1/user";
        List<String> userLogins = restTemplate.getForObject(url, List.class);
        return Objects.requireNonNull(userLogins, "The list of user logins is null");
    }

    public boolean isUserDataIncorrect(RequestConfiguration configuration) {
        String login = configuration.getLogin();
        String password = configuration.getPassword();
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(configuration.getUrl() + "/api/v1/user", new LoginRequest(login, password), String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                if (Objects.equals(response.getBody(), "Login success!")) {
                    System.out.println("Welcome back " + login + "!");
                } else {
                    System.out.println("Welcome " + login + ". We are happy you join us!");
                }
                return false;
            }
        } catch (Exception e) {
            System.out.println("Wrong credentials!");
        }
        return true;
    }
}
