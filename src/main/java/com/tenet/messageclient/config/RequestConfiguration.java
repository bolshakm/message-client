package com.tenet.messageclient.config;

import java.time.LocalDateTime;

public class RequestConfiguration {
    private String url;
    private String login;
    private String password;
    private String topic;
    private LocalDateTime lastRead;

    public RequestConfiguration() {
        lastRead = LocalDateTime.now();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getLastRead() {
        return lastRead;
    }

    public void setLastRead(LocalDateTime lastRead) {
        this.lastRead = lastRead;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
