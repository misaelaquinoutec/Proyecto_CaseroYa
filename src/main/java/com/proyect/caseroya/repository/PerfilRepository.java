package com.proyect.caseroya.repository;

import com.proyect.caseroya.entity.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Integer> {
    // Aquí pueden ir consultas personalizadas si las necesitan luego,
    // ej: Optional<Perfil> findByNombrePerfil(String nombrePerfil);
}
