package com.proyect.caseroya.producto.domain;

import com.proyect.caseroya.producto.dto.ProductoRequest;
import com.proyect.caseroya.producto.infrastructure.ProductoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductoService {

    private static final BigDecimal PRECIO_MAXIMO = new BigDecimal("9999999999.99");
    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> listar() {
        return productoRepository.listar();
    }

    public Producto obtenerPorId(Integer id) {
        validarId(id);
        return productoRepository.obtenerPorId(id)
                .orElseThrow(() -> noEncontrado(id));
    }

    @Transactional
    public Producto crear(ProductoRequest datos) {
        ProductoRequest valido = validarDatos(datos);
        Integer id = productoRepository.crear(valido);
        return obtenerPorId(id);
    }

    @Transactional
    public Producto actualizar(Integer id, ProductoRequest datos) {
        validarId(id);
        ProductoRequest valido = validarDatos(datos);
        if (!productoRepository.actualizar(id, valido)) {
            throw noEncontrado(id);
        }
        return obtenerPorId(id);
    }

    @Transactional
    public void eliminar(Integer id, String usuario) {
        validarId(id);
        String usuarioValido = validarTexto(usuario, "usuario", 15);
        if (!productoRepository.eliminarLogico(id, usuarioValido)) {
            throw noEncontrado(id);
        }
    }

    private ProductoRequest validarDatos(ProductoRequest datos) {
        if (datos == null) {
            throw errorValidacion("Debes enviar los datos del producto.");
        }
        return new ProductoRequest(
                validarTexto(datos.codigo(), "codigo", 30),
                validarTexto(datos.nombre(), "nombre", 150),
                validarTexto(datos.categoria(), "categoria", 50),
                validarTexto(datos.unidadMedida(), "unidadMedida", 20),
                validarPrecio(datos.precioVenta(), "precioVenta"),
                validarPrecio(datos.precioCompra(), "precioCompra"),
                validarTexto(datos.usuario(), "usuario", 15)
        );
    }

    private String validarTexto(String valor, String campo, int longitudMaxima) {
        if (valor == null || valor.isBlank()) {
            throw errorValidacion("El campo " + campo + " es obligatorio.");
        }
        String texto = valor.strip();
        if (texto.codePointCount(0, texto.length()) > longitudMaxima) {
            throw errorValidacion("El campo " + campo + " admite hasta " + longitudMaxima + " caracteres.");
        }
        return texto;
    }

    private BigDecimal validarPrecio(BigDecimal valor, String campo) {
        if (valor == null || valor.signum() < 0 || valor.compareTo(PRECIO_MAXIMO) > 0) {
            throw errorValidacion("El campo " + campo + " debe estar entre 0 y 9999999999.99.");
        }
        if (valor.scale() > 2) {
            throw errorValidacion("El campo " + campo + " admite hasta 2 decimales.");
        }
        return valor;
    }

    private void validarId(Integer id) {
        if (id == null || id <= 0) {
            throw errorValidacion("El id del producto debe ser un entero positivo.");
        }
    }

    private ResponseStatusException errorValidacion(String mensaje) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, mensaje);
    }

    private ResponseStatusException noEncontrado(Integer id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado con id: " + id);
    }
}
