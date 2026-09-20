package com.proyect.caseroya.opcionmenu.infrastructure;

import com.proyect.caseroya.opcionmenu.domain.PerfilOpcionMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerfilOpcionMenuRepository extends JpaRepository<PerfilOpcionMenu, Long> {
    List<PerfilOpcionMenu> findByPerfilId(String perfilId);
}