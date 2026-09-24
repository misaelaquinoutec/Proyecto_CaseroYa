package com.proyect.caseroya.proveedor.domain;

import com.proyect.caseroya.config.exception.DocumentoDuplicadoException;
import com.proyect.caseroya.config.exception.RecursoNoEncontradoException;
import com.proyect.caseroya.proveedor.dto.ProveedorRequestDto;
import com.proyect.caseroya.proveedor.dto.ProveedorResponseDto;
import com.proyect.caseroya.proveedor.infrastructure.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;

    public List<ProveedorResponseDto> listarTodos() {
        return proveedorRepository.findAll().stream()
                .map(this::convertirAResponseDto)
                .collect(Collectors.toList());
    }

    public ProveedorResponseDto obtenerPorId(Integer id) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Proveedor no encontrado con id: " + id));
        return convertirAResponseDto(proveedor);
    }

    public ProveedorResponseDto crear(ProveedorRequestDto request) {
        if (proveedorRepository.existsByRuc(request.getRuc())) {
            throw new DocumentoDuplicadoException("El RUC ya está registrado: " + request.getRuc());
        }

        Proveedor proveedor = new Proveedor();
        proveedor.setRazonSocial(request.getRazonSocial());
        proveedor.setRuc(request.getRuc());
        proveedor.setTipoDocumentoId(request.getTipoDocumentoId());
        proveedor.setTelefono(request.getTelefono());
        proveedor.setDireccion(request.getDireccion());
        proveedor.setActivo(true);

        Proveedor guardado = proveedorRepository.save(proveedor);
        return convertirAResponseDto(guardado);
    }

    public ProveedorResponseDto actualizar(Integer id, ProveedorRequestDto request) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Proveedor no encontrado con id: " + id));

        // Si cambió el RUC, validar que no le pertenezca a otro proveedor
        if (!proveedor.getRuc().equals(request.getRuc()) &&
                proveedorRepository.existsByRuc(request.getRuc())) {
            throw new DocumentoDuplicadoException("El RUC ya pertenece a otro proveedor: " + request.getRuc());
        }

        proveedor.setRazonSocial(request.getRazonSocial());
        proveedor.setRuc(request.getRuc());
        proveedor.setTipoDocumentoId(request.getTipoDocumentoId());
        proveedor.setTelefono(request.getTelefono());
        proveedor.setDireccion(request.getDireccion());

        Proveedor actualizado = proveedorRepository.save(proveedor);
        return convertirAResponseDto(actualizado);
    }

    public void inactivarLogico(Integer id) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Proveedor no encontrado con id: " + id));

        proveedor.setActivo(false);
        proveedorRepository.save(proveedor);
    }

    private ProveedorResponseDto convertirAResponseDto(Proveedor proveedor) {
        ProveedorResponseDto dto = new ProveedorResponseDto();
        dto.setId(proveedor.getId());
        dto.setRazonSocial(proveedor.getRazonSocial());
        dto.setRuc(proveedor.getRuc());
        dto.setTipoDocumentoId(proveedor.getTipoDocumentoId());
        dto.setTelefono(proveedor.getTelefono());
        dto.setDireccion(proveedor.getDireccion());
        dto.setActivo(proveedor.getActivo());
        return dto;
    }
}