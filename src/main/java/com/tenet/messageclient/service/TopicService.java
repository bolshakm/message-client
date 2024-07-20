package com.tenet.messageclient.service;

import com.tenet.messageclient.config.RequestConfiguration;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Service
public class TopicService {

    private final RestTemplate restTemplate;

    public TopicService() {
        this.restTemplate = new RestTemplate();
    }

    public List<String> getAllTopics(RequestConfiguration configuration) {
        String url = configuration.getUrl() + "/api/v1/topic";
        List<String> topics = restTemplate.getForObject(url, List.class);
        return Objects.requireNonNull(topics, "The list of topics is null");
    }
}
