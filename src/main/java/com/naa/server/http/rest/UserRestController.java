package com.naa.server.http.rest;

import com.naa.server.database.enums.Role;
import com.naa.server.database.repository.UserRepository;
import com.naa.server.dto.UserReadDto;
import com.naa.server.http.controller.UserController;
import com.naa.server.mapper.UserReadMapper;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1")
public class UserRestController {

    private final UserRepository userRepository;
    private final UserReadMapper userReadMapper;
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UserRestController.class);

    public UserRestController(UserRepository userRepository,UserReadMapper userReadMapper ) {
        this.userRepository = userRepository;
        this.userReadMapper = userReadMapper;
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserReadDto> finById(@PathVariable("id") Long id){
        log.info(" method finById " + id);
        return ResponseEntity.ok(userRepository.findById(id)
                .map(userReadMapper::map)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }


    @GetMapping("/users")
    public ResponseEntity<List<UserReadDto>> findAll() {
        log.info(" method findAll");
        List<UserReadDto> users = userRepository.findAll().stream()
                .map(userReadMapper::map)
                .toList();
        return users.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(users);
    }

    @GetMapping("/me")
    public ResponseEntity<UserReadDto> findUser(Principal principal) {
        log.info(" method findUser /me");
        return ResponseEntity.ok(userRepository.findByUsername(principal.getName())
                .map(userReadMapper::map)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @GetMapping("/role")
    public ResponseEntity<List<Role>> findAllRole() {
        log.info(" method findAllRole");
        return ResponseEntity.ok(Collections.unmodifiableList(Arrays.asList(Role.values())));
    }
}
