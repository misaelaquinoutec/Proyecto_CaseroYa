package com.proyect.caseroya.usuario.domain;

import com.proyect.caseroya.config.exception.CredencialesInvalidasException;
import com.proyect.caseroya.config.exception.UsuarioNoEncontradoException;
import com.proyect.caseroya.usuario.dto.UsuarioRequestDto;
import com.proyect.caseroya.usuario.dto.UsuarioResponseDto;
import com.proyect.caseroya.usuario.infrastructure.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UsuarioResponseDto> obtenerTodos() {
        return usuarioRepository.findByActivoTrue().stream()
                .map(u -> new UsuarioResponseDto(u.getCodigoUsuario(), u.getNombre(), u.getPerfilId(), u.getActivo()))
                .collect(Collectors.toList());
    }

    public UsuarioResponseDto obtenerPorId(String id) {
        Usuario u = usuarioRepository.findByCodigoUsuarioAndActivoTrue(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado con ID: " + id));
        return new UsuarioResponseDto(u.getCodigoUsuario(), u.getNombre(), u.getPerfilId(), u.getActivo());
    }

    public UsuarioResponseDto crearUsuario(UsuarioRequestDto dto) {
        if (usuarioRepository.existsByCodigoUsuario(dto.getCodigoUsuario())) {
            throw new CredencialesInvalidasException("El usuario '" + dto.getCodigoUsuario() + "' ya se encuentra registrado.");
        }
        Usuario usuario = new Usuario();
        usuario.setCodigoUsuario(dto.getCodigoUsuario());
        usuario.setClave(passwordEncoder.encode(dto.getClave()));
        usuario.setNombre(dto.getNombre());
        usuario.setPerfilId(dto.getPerfilId());
        usuario.setActivo(true);
        usuario.setUsuarioCreacion("SYSTEM");
        usuario.setFechaCreacion(LocalDateTime.now());

        Usuario guardado = usuarioRepository.save(usuario);
        return new UsuarioResponseDto(guardado.getCodigoUsuario(), guardado.getNombre(), guardado.getPerfilId(), guardado.getActivo());
    }

    public UsuarioResponseDto actualizarUsuario(String id, UsuarioRequestDto dto) {
        Usuario usuario = usuarioRepository.findByCodigoUsuarioAndActivoTrue(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado con ID: " + id));

        usuario.setNombre(dto.getNombre());
        if (dto.getClave() != null && !dto.getClave().isEmpty()) {
            usuario.setClave(dto.getClave());
        }
        usuario.setPerfilId(dto.getPerfilId());
        usuario.setUsuarioModificacion("SYSTEM");
        usuario.setFechaModificacion(LocalDateTime.now());

        Usuario actualizado = usuarioRepository.save(usuario);
        return new UsuarioResponseDto(actualizado.getCodigoUsuario(), actualizado.getNombre(), actualizado.getPerfilId(), actualizado.getActivo());
    }

    public void eliminarUsuario(String id) {
        Usuario usuario = usuarioRepository.findByCodigoUsuarioAndActivoTrue(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado con ID: " + id));

        usuario.setActivo(false);
        usuario.setUsuarioModificacion("SYSTEM");
        usuario.setFechaModificacion(LocalDateTime.now());
        usuarioRepository.save(usuario);
    }
}