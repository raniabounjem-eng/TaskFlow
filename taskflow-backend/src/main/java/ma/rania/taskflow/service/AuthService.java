package ma.rania.taskflow.service;

import lombok.RequiredArgsConstructor;
import ma.rania.taskflow.dto.LoginRequest;
import ma.rania.taskflow.dto.LoginResponse;
import ma.rania.taskflow.model.User;
import ma.rania.taskflow.repository.UserRepository;
import ma.rania.taskflow.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository        userRepository;
    private final JwtUtil               jwtUtil;

    public LoginResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (AuthenticationException e) {
            throw new RuntimeException("Email ou mot de passe incorrect");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        String token = jwtUtil.generateToken(user.getEmail());

        return new LoginResponse(token, user.getEmail(), user.getFullName(), user.getRole().name());
    }
}
