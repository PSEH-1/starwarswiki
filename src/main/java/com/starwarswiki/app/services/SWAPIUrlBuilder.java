package com.starwarswiki.app.services;

import com.starwarswiki.app.secretsprovider.SWAPISecretsProvider;
import org.springframework.stereotype.Component;

@Component
public class SWAPIUrlBuilder {

    public String buildApiUrl(String type, String name) {
        return new StringBuffer(SWAPISecretsProvider.API_BASE_URL)
                .append("/")
                .append(type)
                .append("/?search=")
                .append(name)
                .toString();
    }

}
