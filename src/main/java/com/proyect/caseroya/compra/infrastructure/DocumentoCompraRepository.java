package com.proyect.caseroya.compra.infrastructure;

import com.proyect.caseroya.compra.domain.DocumentoCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentoCompraRepository extends JpaRepository<DocumentoCompra, Integer> {
}
