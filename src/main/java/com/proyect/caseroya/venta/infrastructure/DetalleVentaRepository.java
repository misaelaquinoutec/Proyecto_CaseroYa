package com.proyect.caseroya.venta.infrastructure;

import com.proyect.caseroya.venta.domain.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Integer> {
}