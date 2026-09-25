package com.proyect.caseroya.auth.domain;

import com.proyect.caseroya.auth.dto.AuthResponseDto;
import com.proyect.caseroya.auth.dto.LoginRequestDto;
import com.proyect.caseroya.config.security.JwtService; // <-- Este import faltaba
import com.proyect.caseroya.usuario.domain.Usuario;
import com.proyect.caseroya.usuario.infrastructure.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UsuarioRepository usuarioRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponseDto login(LoginRequestDto request) {
        Usuario usuario = usuarioRepository.findByCodigoUsuarioAndActivoTrue(request.getCodigoUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado o inactivo"));

        if (!passwordEncoder.matches(request.getClave(), usuario.getClave())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        String token = jwtService.generarToken(usuario.getCodigoUsuario());
        return new AuthResponseDto(token, "Login exitoso");
    }
}