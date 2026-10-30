package com.project.userauthservice.controller;

import com.project.userauthservice.dto.AuthRequest;
import com.project.userauthservice.dto.UserDto;
import com.project.userauthservice.model.User;
import com.project.userauthservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody AuthRequest authRequest){
        User user = authService.signUp(authRequest.getEmail(), authRequest.getPassword());

        if (user == null){
            return ResponseEntity.badRequest().build();
        }

        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole());
        userDto.setId(user.getId());

        return ResponseEntity.ok(userDto);
    }
}
