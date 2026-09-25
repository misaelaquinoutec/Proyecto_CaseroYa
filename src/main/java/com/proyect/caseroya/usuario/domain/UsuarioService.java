package com.proyect.caseroya.usuario.domain;

import com.proyect.caseroya.usuario.dto.UsuarioRequestDto;
import com.proyect.caseroya.usuario.dto.UsuarioResponseDto;
import com.proyect.caseroya.usuario.infrastructure.UsuarioRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, ApplicationEventPublisher eventPublisher) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = eventPublisher;
    }

    public List<UsuarioResponseDto> obtenerTodos() {
        return usuarioRepository.findAll().stream()
                .map(u -> new UsuarioResponseDto(u.getCodigoUsuario(), u.getNombre(), u.getPerfilId(), u.getActivo()))
                .collect(Collectors.toList());
    }

    public UsuarioResponseDto crearUsuario(UsuarioRequestDto dto) {
        Usuario usuario = new Usuario();
        usuario.setCodigoUsuario(dto.getCodigoUsuario());
        usuario.setClave(passwordEncoder.encode(dto.getClave()));
        usuario.setNombre(dto.getNombre());
        usuario.setPerfilId(dto.getPerfilId());
        usuario.setActivo(true);
        usuario.setUsuarioCreacion("SYSTEM");
        usuario.setFechaCreacion(LocalDateTime.now());

        Usuario guardado = usuarioRepository.save(usuario);
        
        eventPublisher.publishEvent(new UsuarioCreadoEvent(this, guardado.getCodigoUsuario(), guardado.getNombre()));
        
        return new UsuarioResponseDto(guardado.getCodigoUsuario(), guardado.getNombre(), guardado.getPerfilId(), guardado.getActivo());
    }
}