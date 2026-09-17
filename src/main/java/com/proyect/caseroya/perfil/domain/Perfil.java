package com.proyect.caseroya.perfil.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "perfiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Perfil {

    @Id
    @Column(name = "id_perfil", length = 3, nullable = false)
    private String idPerfil; // Cambiado de 'char' a 'String' y sin @GeneratedValue

    @Column(name = "nombre_perfil", nullable = false, length = 50)
    private String nombrePerfil;

}
