package com.proyect.caseroya.usuario.infrastructure;

import com.proyect.caseroya.usuario.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    List<Usuario> findByActivoTrue();
    Optional<Usuario> findByCodigoUsuarioAndActivoTrue(String codigoUsuario);
}