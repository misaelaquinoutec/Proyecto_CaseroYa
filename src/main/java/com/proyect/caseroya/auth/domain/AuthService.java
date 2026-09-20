package com.proyect.caseroya.auth.domain;

import com.proyect.caseroya.auth.dto.AuthResponseDto;
import com.proyect.caseroya.auth.dto.LoginRequestDto;
import com.proyect.caseroya.usuario.domain.Usuario;
import com.proyect.caseroya.usuario.infrastructure.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;

    public AuthService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public AuthResponseDto login(LoginRequestDto request) {
        Usuario usuario = usuarioRepository.findByCodigoUsuarioAndActivoTrue(request.getCodigoUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado o inactivo"));

        if (!usuario.getClave().equals(request.getClave())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        // Aquí se puede generar un JWT si lo usas más adelante; por ahora enviamos datos básicos
        return new AuthResponseDto("DUMMY_TOKEN_SESSION", usuario.getCodigoUsuario(), usuario.getNombre(), usuario.getPerfilId());
    }
}
