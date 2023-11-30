package com.carShowroom.WebCarShowroom.helpers;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class ApiClient {
    RestTemplate restTemplate = new RestTemplate();

    public String getTemplate(String uri) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uri);
        return restTemplate.getForObject(builder.toUriString(), String.class);
    }

    public String getTemplateWithPathVariable(String uri, int var) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(uri, String.class, var);
    }
}
