package com.naa.server.service;

import com.naa.server.database.repository.UserRepository;
import com.naa.server.dto.UserReadDto;
import com.naa.server.mapper.UserReadMapper;
import org.slf4j.Logger;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserReadMapper userReadMapper;
    private final UserRepository userRepository;


    private static final Logger log = org.slf4j.LoggerFactory.getLogger(UserService.class);

    public UserService(UserReadMapper userReadMapper, UserRepository userRepository) {
        this.userReadMapper = userReadMapper;
        this.userRepository = userRepository;
    }


    public Optional<UserReadDto> findById(Long id) {
        return userRepository.findById(id)
                .map(userReadMapper::map);
    }

    public List<UserReadDto> findAll() {
        return userRepository.findAll().stream()
                .map(userReadMapper::map).toList();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername " + username);
        return userRepository.findByUsername(username)
                .map(user -> new User(
                        user.getUsername(),
                        user.getPassword(),
                        Collections.singleton(user.getRole())
                ))
                .orElseThrow(() -> new UsernameNotFoundException("Failed search " + username));
    }
}
