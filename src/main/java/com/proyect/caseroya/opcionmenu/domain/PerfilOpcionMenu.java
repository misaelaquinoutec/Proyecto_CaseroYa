package com.proyect.caseroya.opcionmenu.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "perfilopcionmenu", schema = "public")
public class PerfilOpcionMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "perfil_id", length = 10, nullable = false)
    private String perfilId;

    @Column(name = "opcionid", length = 10, nullable = false)
    private String opcionId;

    public PerfilOpcionMenu() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPerfilId() { return perfilId; }
    public void setPerfilId(String perfilId) { this.perfilId = perfilId; }

    public String getOpcionId() { return opcionId; }
    public void setOpcionId(String opcionId) { this.opcionId = opcionId; }
}
