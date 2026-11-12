package com.proyect.caseroya.config.security;

import com.proyect.caseroya.usuario.domain.Usuario;
import com.proyect.caseroya.usuario.infrastructure.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (!usuarioRepository.existsByCodigoUsuario("ADMIN01")) {
            Usuario admin = new Usuario();
            admin.setCodigoUsuario("ADMIN01");
            admin.setClave(passwordEncoder.encode("admin123"));
            admin.setNombre("Administrador CaseroYa");
            admin.setPerfilId("ADM");
            admin.setActivo(true);
            admin.setUsuarioCreacion("SYSTEM_INIT");
            admin.setFechaCreacion(LocalDateTime.now());
            usuarioRepository.save(admin);
        }
    }
}
