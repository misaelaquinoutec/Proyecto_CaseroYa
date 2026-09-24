package com.proyect.caseroya.venta.infrastructure;

import com.proyect.caseroya.venta.domain.DocumentoVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DocumentoVentaRepository extends JpaRepository<DocumentoVenta, Integer> {
    List<DocumentoVenta> findByFechaEmisionBetween(LocalDate desde, LocalDate hasta);
}
