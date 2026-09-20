package com.proyect.caseroya.opcionmenu.infrastructure;

import com.proyect.caseroya.opcionmenu.domain.OpcionMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpcionMenuRepository extends JpaRepository<OpcionMenu, String> {
}