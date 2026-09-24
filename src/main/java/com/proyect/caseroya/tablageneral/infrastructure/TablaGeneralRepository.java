package com.proyect.caseroya.tablageneral.infrastructure;

import com.proyect.caseroya.tablageneral.domain.TablaGeneral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TablaGeneralRepository extends JpaRepository<TablaGeneral, Integer> {

    // Permite filtrar catálogos por tipo (ej: ?tipo=MO, ?tipo=TD, ?tipo=CAT, ?tipo=UM)
    List<TablaGeneral> findByTipo(String tipo);

    boolean existsByTipoAndCodigo(String tipo, String codigo);
}

