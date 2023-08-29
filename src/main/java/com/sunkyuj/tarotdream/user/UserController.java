package com.sunkyuj.tarotdream.user;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@RestController
@RequestMapping("/users")
@Transactional
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;

    @PostMapping("/login")
    public Long login(@RequestBody UserLoginRequest userLoginRequest ){
        User user = userRepository.findByName(userLoginRequest.getName());
        return user.getId();
    }

    @PostMapping("/register")
    public Long register(@RequestBody UserRegisterRequest userRegisterRequest ){
        User user = User.builder()
                .name(userRegisterRequest.getName())
                .password(userRegisterRequest.getPassword())
                .created(new Timestamp(System.currentTimeMillis()))
                .build();
        return userRepository.save(user);
    }
}
