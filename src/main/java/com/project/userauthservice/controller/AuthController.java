package com.project.userauthservice.controller;

import com.project.userauthservice.dto.AuthRequest;
import com.project.userauthservice.dto.LoginResponse;
import com.project.userauthservice.dto.UserDto;
import com.project.userauthservice.model.Role;
import com.project.userauthservice.model.User;
import com.project.userauthservice.service.AuthService;
import com.project.userauthservice.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@Valid @RequestBody AuthRequest authRequest){
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

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody AuthRequest authRequest){
        String token = authService.login(authRequest.getEmail(), authRequest.getPassword());
        if (token == null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        LoginResponse response = new LoginResponse();
        response.setToken(token);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/validateToken")
    public ResponseEntity<UserDto> validateToken(@RequestHeader("Authorization") String header) {
        if (!header.startsWith("Bearer ")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            Claims claims = jwtService.parseToken(header.substring("Bearer ".length()));
            UserDto dto = new UserDto();
            dto.setEmail(claims.getSubject());
            dto.setRole(Role.valueOf(claims.get("role", String.class)));
            return ResponseEntity.ok(dto);
        } catch (JwtException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("hello")
    public String hello(){
        return "Hello World";
    }

    @GetMapping("/healthcheck")
    public ResponseEntity<String> healthCheck(){
        return ResponseEntity.ok("Up and running");
    }

}
