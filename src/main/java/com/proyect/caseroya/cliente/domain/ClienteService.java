package com.proyect.caseroya.cliente.domain;

import com.proyect.caseroya.cliente.dto.ClienteRequestDto;
import com.proyect.caseroya.cliente.dto.ClienteResponseDto;
import com.proyect.caseroya.cliente.infrastructure.ClienteRepository;
import com.proyect.caseroya.exception.DocumentoDuplicadoException;
import com.proyect.caseroya.exception.RecursoNoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<ClienteResponseDto> listarTodos() {
        return clienteRepository.findAll().stream()
                .map(this::convertirAResponseDto)
                .collect(Collectors.toList());
    }

    public ClienteResponseDto obtenerPorId(Integer id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado con id: " + id));
        return convertirAResponseDto(cliente);
    }

    public ClienteResponseDto crear(ClienteRequestDto request) {
        if (clienteRepository.existsByNumeroDocumento(request.getNumeroDocumento())) {
            throw new DocumentoDuplicadoException("El número de documento ya está registrado: " + request.getNumeroDocumento());
        }

        Cliente cliente = new Cliente();
        cliente.setNombre(request.getNombre());
        cliente.setNumeroDocumento(request.getNumeroDocumento());
        cliente.setTipoDocumentoId(request.getTipoDocumentoId());
        cliente.setTelefono(request.getTelefono());
        cliente.setDireccion(request.getDireccion());
        cliente.setActivo(true);

        Cliente guardado = clienteRepository.save(cliente);
        return convertirAResponseDto(guardado);
    }

    public ClienteResponseDto actualizar(Integer id, ClienteRequestDto request) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado con id: " + id));

        // Si cambió el documento, verificar que no esté repetido en otro registro
        if (!cliente.getNumeroDocumento().equals(request.getNumeroDocumento()) &&
                clienteRepository.existsByNumeroDocumento(request.getNumeroDocumento())) {
            throw new DocumentoDuplicadoException("El número de documento ya pertenece a otro cliente: " + request.getNumeroDocumento());
        }

        cliente.setNombre(request.getNombre());
        cliente.setNumeroDocumento(request.getNumeroDocumento());
        cliente.setTipoDocumentoId(request.getTipoDocumentoId());
        cliente.setTelefono(request.getTelefono());
        cliente.setDireccion(request.getDireccion());

        Cliente actualizado = clienteRepository.save(cliente);
        return convertirAResponseDto(actualizado);
    }

    public void inactivarLógico(Integer id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado con id: " + id));

        cliente.setActivo(false);
        clienteRepository.save(cliente);
    }

    private ClienteResponseDto convertirAResponseDto(Cliente cliente) {
        ClienteResponseDto dto = new ClienteResponseDto();
        dto.setId(cliente.getId());
        dto.setNombre(cliente.getNombre());
        dto.setNumeroDocumento(cliente.getNumeroDocumento());
        dto.setTipoDocumentoId(cliente.getTipoDocumentoId());
        dto.setTelefono(cliente.getTelefono());
        dto.setDireccion(cliente.getDireccion());
        dto.setActivo(cliente.getActivo());
        return dto;
    }
}