package com.proyect.caseroya.cliente.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(name = "numero_documento", nullable = false, unique = true, length = 15)
    private String numeroDocumento;

    @Column(name = "tipo_documento_id", nullable = false)
    private Integer tipoDocumentoId;

    @Column(length = 20)
    private String telefono;

    @Column(length = 200)
    private String direccion;

    @Column(nullable = false)
    private Boolean activo = true;
}