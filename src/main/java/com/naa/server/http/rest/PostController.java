package com.naa.server.http.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

    @PostMapping()
    public ResponseEntity<String> get(@PathVariable("file") MultipartFile file){
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            reader.lines().forEach(System.out::println);
        } catch (IOException e) {
            throw new RuntimeException("Error processing file", e);
        }
        return ResponseEntity.ok("Method post");

    }
}
