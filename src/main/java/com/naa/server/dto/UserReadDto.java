package com.naa.server.dto;

public record UserReadDto(Long id,
                          String username,
                          String email,
                          String password) {
}
