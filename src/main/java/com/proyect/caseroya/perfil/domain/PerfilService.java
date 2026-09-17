package com.proyect.caseroya.perfil.domain;

import com.proyect.caseroya.perfil.infrastructure.PerfilRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerfilService {

    private final PerfilRepository perfilRepository;

    public PerfilService(PerfilRepository perfilRepository) {
        this.perfilRepository = perfilRepository;
    }

    public List<Perfil> obtenerTodos() {
        return perfilRepository.findAll();
    }

    public Perfil obtenerPorId(String id) {
        return perfilRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Perfil no encontrado con ID: " + id));
    }

    public Perfil guardar(Perfil perfil) {
        return perfilRepository.save(perfil);
    }

    public void eliminar(String id) {
        perfilRepository.deleteById(id);
    }
}
