package com.proyect.caseroya.perfil.infrastructure;

import com.proyect.caseroya.perfil.domain.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

public interface PerfilRepository extends JpaRepository<Perfil, String> {
}