package com.tenet.messageclient.service;

import com.tenet.messageclient.config.RequestConfiguration;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class GetMessageRunner implements Runnable {
    private RequestConfiguration configuration;
    private final RestTemplate restTemplate;

    public GetMessageRunner(RequestConfiguration configuration) {
        this.configuration = configuration;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public void run() {
        String url = configuration.getUrl() + "/api/v1/message?topic=" + configuration.getTopic() + "&lastRead=" + configuration.getLastRead();
        List<?> items = restTemplate.getForObject(url, List.class);
        if (items != null) {
            items.stream()
                    .filter(Objects::nonNull)
                    .filter(item -> !StringUtils.startsWithIgnoreCase(item.toString(), configuration.getLogin()))
                    .forEach(System.out::println);
        }
        configuration.setLastRead(LocalDateTime.now().minusHours(2));
    }
}
