package com.proyect.caseroya.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "perfiles")
public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_perfil")
    private Integer idPerfil;

    @Column(name = "nombre_perfil", length = 50, nullable = false)
    private String nombrePerfil;

}
