package com.tenet.messageclient.service;

import com.tenet.messageclient.config.RequestConfiguration;
import com.tenet.messageclient.request.SendMessageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MessageService {

    private final RestTemplate restTemplate;

    public MessageService() {
        this.restTemplate = new RestTemplate();
    }

    public boolean isHealthcheckInvalid(RequestConfiguration requestConfiguration) {
        String url = requestConfiguration.getUrl();
        try {
            ResponseEntity<Object> response = restTemplate.getForEntity(url + "/api/v1/healthcheck", Object.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Success connected to: " + url);
                return false;
            }
        } catch (Exception e) {
            System.out.println("Something went wrong! Please enter valid IP or URL. Example: http://localhost:8080");
        }
        return true;
    }

    public void sendMessage(RequestConfiguration configuration, String text) {
        SendMessageRequest messageRequest = new SendMessageRequest();
        messageRequest.setLogin(configuration.getLogin());
        messageRequest.setPassword(configuration.getPassword());
        messageRequest.setTopicName(configuration.getTopic());
        messageRequest.setText(text);
        restTemplate.postForEntity(configuration.getUrl() + "/api/v1/message", messageRequest, String.class);
    }
}
