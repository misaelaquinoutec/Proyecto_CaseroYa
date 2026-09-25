package com.proyect.caseroya.auth.domain;

import com.proyect.caseroya.auth.dto.AuthResponseDto;
import com.proyect.caseroya.auth.dto.LoginRequestDto;
import com.proyect.caseroya.usuario.domain.Usuario;
import com.proyect.caseroya.usuario.infrastructure.UsuarioRepository;
import com.proyect.caseroya.config.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UsuarioRepository usuarioRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponseDto login(LoginRequestDto request) {
        Usuario usuario = usuarioRepository.findByCodigoUsuarioAndActivoTrue(request.getCodigoUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado o inactivo"));

        if (!passwordEncoder.matches(request.getClave(), usuario.getClave())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        String jwtToken = jwtService.generateToken(usuario.getCodigoUsuario());
        return new AuthResponseDto(jwtToken, usuario.getCodigoUsuario(), usuario.getNombre(), usuario.getPerfilId());
    }
}
