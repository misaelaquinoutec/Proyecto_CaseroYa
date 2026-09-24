package com.proyect.caseroya.tablageneral.domain;

import com.proyect.caseroya.config.exception.DocumentoDuplicadoException;
import com.proyect.caseroya.config.exception.RecursoNoEncontradoException;
import com.proyect.caseroya.tablageneral.dto.TablaGeneralRequestDto;
import com.proyect.caseroya.tablageneral.dto.TablaGeneralResponseDto;
import com.proyect.caseroya.tablageneral.infrastructure.TablaGeneralRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TablaGeneralService {

    @Autowired
    private TablaGeneralRepository tablaGeneralRepository;

    public List<TablaGeneralResponseDto> listarTodos(String tipo) {
        List<TablaGeneral> lista;
        if (tipo != null && !tipo.trim().isEmpty()) {
            lista = tablaGeneralRepository.findByTipo(tipo.toUpperCase());
        } else {
            lista = tablaGeneralRepository.findAll();
        }
        return lista.stream()
                .map(this::convertirAResponseDto)
                .collect(Collectors.toList());
    }

    public TablaGeneralResponseDto obtenerPorId(Integer id) {
        TablaGeneral registro = tablaGeneralRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Registro no encontrado con id: " + id));
        return convertirAResponseDto(registro);
    }

    public TablaGeneralResponseDto crear(TablaGeneralRequestDto request) {
        if (tablaGeneralRepository.existsByTipoAndCodigo(request.getTipo(), request.getCodigo())) {
            throw new DocumentoDuplicadoException("Ya existe un registro con el tipo '" + request.getTipo() + "' y código '" + request.getCodigo() + "'");
        }

        TablaGeneral tablaGeneral = new TablaGeneral();
        tablaGeneral.setTipo(request.getTipo().toUpperCase());
        tablaGeneral.setCodigo(request.getCodigo());
        tablaGeneral.setDescripcion(request.getDescripcion());

        TablaGeneral guardado = tablaGeneralRepository.save(tablaGeneral);
        return convertirAResponseDto(guardado);
    }

    public TablaGeneralResponseDto actualizar(Integer id, TablaGeneralRequestDto request) {
        TablaGeneral tablaGeneral = tablaGeneralRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Registro no encontrado con id: " + id));

        tablaGeneral.setTipo(request.getTipo().toUpperCase());
        tablaGeneral.setCodigo(request.getCodigo());
        tablaGeneral.setDescripcion(request.getDescripcion());

        TablaGeneral actualizado = tablaGeneralRepository.save(tablaGeneral);
        return convertirAResponseDto(actualizado);
    }

    public void eliminar(Integer id) {
        TablaGeneral tablaGeneral = tablaGeneralRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Registro no encontrado con id: " + id));

        tablaGeneralRepository.delete(tablaGeneral);
    }

    private TablaGeneralResponseDto convertirAResponseDto(TablaGeneral tablaGeneral) {
        TablaGeneralResponseDto dto = new TablaGeneralResponseDto();
        dto.setId(tablaGeneral.getId());
        dto.setTipo(tablaGeneral.getTipo());
        dto.setCodigo(tablaGeneral.getCodigo());
        dto.setDescripcion(tablaGeneral.getDescripcion());
        return dto;
    }
}