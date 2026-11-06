package com.proyect.caseroya.auth.domain;

import com.proyect.caseroya.auth.dto.AuthResponseDto;
import com.proyect.caseroya.auth.dto.LoginRequestDto;
import com.proyect.caseroya.usuario.domain.Usuario;
import com.proyect.caseroya.usuario.infrastructure.UsuarioRepository;
import com.proyect.caseroya.config.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

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

        boolean matches = checkPassword(request.getClave(), usuario.getClave());

        if (!matches) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        if (usuario.getClave() != null && !usuario.getClave().startsWith("$2")) {
            usuario.setClave(passwordEncoder.encode(request.getClave()));
            usuarioRepository.save(usuario);
        }

        String jwtToken = jwtService.generateToken(usuario.getCodigoUsuario(), usuario.getPerfilId());
        return new AuthResponseDto(jwtToken, usuario.getCodigoUsuario(), usuario.getNombre(), usuario.getPerfilId());
    }

    private boolean checkPassword(String rawPassword, String storedPassword) {
        if (storedPassword == null || rawPassword == null) {
            return false;
        }

        if (storedPassword.startsWith("$2a$") || storedPassword.startsWith("$2b$") || storedPassword.startsWith("$2y$")) {
            try {
                if (passwordEncoder.matches(rawPassword, storedPassword)) {
                    return true;
                }
            } catch (Exception ignored) {}
        }

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(rawPassword.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            if (sb.toString().equalsIgnoreCase(storedPassword)) {
                return true;
            }
        } catch (Exception ignored) {}

        return rawPassword.equals(storedPassword);
    }
}
