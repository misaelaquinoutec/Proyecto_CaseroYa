package com.proyect.caseroya.perfil.domain;

import com.proyect.caseroya.perfil.infrastructure.PerfilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PerfilService {

    @Autowired
    private PerfilRepository perfilRepository;

    public List<Perfil> obtenerTodos() {
        return perfilRepository.findAll();
    }

    public Optional<Perfil> obtenerPorId(Integer id) {
        return perfilRepository.findById(id);
    }

    public Perfil guardar(Perfil perfil) {
        return perfilRepository.save(perfil);
    }

    public void eliminar(Integer id) {
        perfilRepository.deleteById(id);
    }
}
