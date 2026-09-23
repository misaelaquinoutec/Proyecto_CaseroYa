package com.proyect.caseroya.opcionmenu.domain;

import com.proyect.caseroya.opcionmenu.dto.OpcionMenuDto;
import com.proyect.caseroya.opcionmenu.infrastructure.OpcionMenuRepository;
import com.proyect.caseroya.opcionmenu.infrastructure.PerfilOpcionMenuRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OpcionMenuService {

    private final OpcionMenuRepository opcionMenuRepository;
    private final PerfilOpcionMenuRepository perfilOpcionMenuRepository;

    public OpcionMenuService(OpcionMenuRepository opcionMenuRepository,
                             PerfilOpcionMenuRepository perfilOpcionMenuRepository) {
        this.opcionMenuRepository = opcionMenuRepository;
        this.perfilOpcionMenuRepository = perfilOpcionMenuRepository;
    }

    // Método que ya tenías
    public List<OpcionMenuDto> obtenerMenuPorPerfil(String perfilId) {
        List<String> opcionesIds = perfilOpcionMenuRepository.findByPerfilId(perfilId)
                .stream()
                .map(PerfilOpcionMenu::getOpcionId)
                .collect(Collectors.toList());

        return opcionMenuRepository.findAllById(opcionesIds).stream()
                .filter(o -> Boolean.TRUE.equals(o.getActivo()))
                .sorted(Comparator.comparing(OpcionMenu::getOrden, Comparator.nullsLast(Comparator.naturalOrder())))
                .map(o -> new OpcionMenuDto(o.getOpcionId(), o.getDescripcion(), o.getUrl(), o.getIcono(), o.getOrden()))
                .collect(Collectors.toList());
    }

    // --- MÉTODOS CRUD AÑADIDOS ---

    public List<OpcionMenu> listarTodos() {
        return opcionMenuRepository.findAll().stream()
                .filter(o -> Boolean.TRUE.equals(o.getActivo()))
                .collect(Collectors.toList());
    }

    public OpcionMenu obtenerPorId(String id) {
        return opcionMenuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Opción de menú no encontrada con ID: " + id));
    }

    public OpcionMenu crear(OpcionMenu opcion) {
        return opcionMenuRepository.save(opcion);
    }

    public OpcionMenu actualizar(String id, OpcionMenu detalles) {
        OpcionMenu opcion = obtenerPorId(id);
        opcion.setDescripcion(detalles.getDescripcion());
        opcion.setUrl(detalles.getUrl());
        opcion.setIcono(detalles.getIcono());
        opcion.setOrden(detalles.getOrden());
        if (detalles.getActivo() != null) {
            opcion.setActivo(detalles.getActivo());
        }
        return opcionMenuRepository.save(opcion);
    }

    public void eliminarLogicamente(String id) {
        OpcionMenu opcion = obtenerPorId(id);
        opcion.setActivo(false);
        opcionMenuRepository.save(opcion);
    }
}