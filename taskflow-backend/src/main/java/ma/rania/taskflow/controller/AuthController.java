package ma.rania.taskflow.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.rania.taskflow.dto.LoginRequest;
import ma.rania.taskflow.dto.LoginResponse;
import ma.rania.taskflow.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
