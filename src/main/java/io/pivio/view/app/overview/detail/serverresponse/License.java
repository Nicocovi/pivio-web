package io.pivio.view.app.overview.detail.serverresponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class License {
    public String name;
    public String url;

    public License(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public License() {
    }
}