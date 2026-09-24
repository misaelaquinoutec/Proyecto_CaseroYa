package com.proyect.caseroya.proveedor.infrastructure;

import com.proyect.caseroya.proveedor.domain.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Integer> {

    Optional<Proveedor> findByRuc(String ruc);

    boolean existsByRuc(String ruc);
}
