package com.proyect.caseroya.auth.application;

import com.proyect.caseroya.auth.domain.AuthService;
import com.proyect.caseroya.auth.dto.AuthResponseDto;
import com.proyect.caseroya.auth.dto.LoginRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
