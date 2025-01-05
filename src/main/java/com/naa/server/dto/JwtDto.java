package com.naa.server.dto;

import org.springframework.stereotype.Component;

public class JwtDto {
    private String accessToken;
    private String tokenType = "Bearer";

    public JwtDto(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
}
