package com.tenet.messageclient.service;

import com.tenet.messageclient.config.RequestConfiguration;
import com.tenet.messageclient.responce.TopicResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class TopicService {

    private final RestTemplate restTemplate;

    public TopicService() {
        this.restTemplate = new RestTemplate();
    }

    public List<String> getAllTopics(RequestConfiguration configuration){
        return Objects.requireNonNull(restTemplate.getForEntity(configuration.getUrl() + "/api/v1/topic", List.class)
                .getBody());
    }


}
