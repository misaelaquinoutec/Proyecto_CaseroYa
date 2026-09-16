package com.proyect.caseroya.service;

import com.proyect.caseroya.entity.Perfil;
import com.proyect.caseroya.repository.PerfilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PerfilService {

    @Autowired
    private PerfilRepository perfilRepository;

    // Obtener todos
    public List<Perfil> obtenerTodos() {
        return perfilRepository.findAll();
    }

    // Obtener por ID
    public Optional<Perfil> obtenerPorId(Integer id) {
        return perfilRepository.findById(id);
    }

    // Guardar (Crear o Actualizar)
    public Perfil guardar(Perfil perfil) {
        return perfilRepository.save(perfil);
    }

    // Eliminar
    public void eliminar(Integer id) {
        perfilRepository.deleteById(id);
    }
}
