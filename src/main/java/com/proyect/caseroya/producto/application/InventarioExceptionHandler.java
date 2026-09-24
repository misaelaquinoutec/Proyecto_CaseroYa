package com.proyect.caseroya.producto.application;

import com.proyect.caseroya.stock.application.StockController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

// Este manejador se aplica únicamente a los dos controladores del módulo.
@RestControllerAdvice(assignableTypes = {ProductoController.class, StockController.class})
public class InventarioExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(InventarioExceptionHandler.class);

    public record ErrorRespuesta(int estado, String mensaje) {
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorRespuesta> manejarEstado(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode())
                .body(new ErrorRespuesta(ex.getStatusCode().value(), ex.getReason()));
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ErrorRespuesta> manejarDuplicado(DuplicateKeyException ex) {
        return respuesta(HttpStatus.CONFLICT, "Ya existe un registro con un valor único repetido; revisa el código del producto.");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorRespuesta> manejarJson(HttpMessageNotReadableException ex) {
        return respuesta(HttpStatus.BAD_REQUEST, "El cuerpo debe ser un JSON válido con los tipos de datos indicados.");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorRespuesta> manejarTipo(MethodArgumentTypeMismatchException ex) {
        return respuesta(HttpStatus.BAD_REQUEST, "El parámetro " + ex.getName() + " debe ser un entero válido.");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorRespuesta> manejarParametro(MissingServletRequestParameterException ex) {
        return respuesta(HttpStatus.BAD_REQUEST, "Falta el parámetro obligatorio: " + ex.getParameterName());
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorRespuesta> manejarBaseDeDatos(DataAccessException ex) {
        LOG.error("Error al ejecutar las funciones de productos o stock", ex);
        return respuesta(HttpStatus.INTERNAL_SERVER_ERROR,
                "No se pudo completar la operación en la base de datos.");
    }

    private ResponseEntity<ErrorRespuesta> respuesta(HttpStatus estado, String mensaje) {
        return ResponseEntity.status(estado).body(new ErrorRespuesta(estado.value(), mensaje));
    }
}
