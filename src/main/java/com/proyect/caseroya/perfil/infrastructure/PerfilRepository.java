package com.proyect.caseroya.perfil.infrastructure;

import com.proyect.caseroya.perfil.domain.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Integer> {
}
