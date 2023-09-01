package com.sunkyuj.tarotdream.user;

import com.sunkyuj.tarotdream.user.model.User;
import com.sunkyuj.tarotdream.user.model.UserLoginRequest;
import com.sunkyuj.tarotdream.user.model.UserRegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.sunkyuj.tarotdream.utils.ApiResult;
import com.sunkyuj.tarotdream.utils.ApiUtils;

import java.sql.Timestamp;

@RestController
@RequestMapping("/users")
@Transactional
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;

    @Operation(summary = "로그인", description = "로그인을 하여 회원 인증을 한다", tags = { "User" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Long.class)))
    })
    @PostMapping("/login")
    public ApiResult<Long> login(@RequestBody UserLoginRequest userLoginRequest ){
        User user = userRepository.findByName(userLoginRequest.getName());
        return ApiUtils.success(user.getId());
    }

    @Operation(summary = "회원가입", description = "회원을 등록한다", tags = { "User" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Long.class)))
    })
    @PostMapping("/register")
    public ApiResult<Long> register(@RequestBody UserRegisterRequest userRegisterRequest ){
        User user = User.builder()
                .name(userRegisterRequest.getName())
                .password(userRegisterRequest.getPassword())
                .created(new Timestamp(System.currentTimeMillis()))
                .build();
        Long userId = userRepository.save(user);
        return ApiUtils.success(userId);
    }
}
