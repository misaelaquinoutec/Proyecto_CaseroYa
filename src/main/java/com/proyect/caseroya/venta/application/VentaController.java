package com.proyect.caseroya.venta.application;

import com.proyect.caseroya.cliente.domain.Cliente;
import com.proyect.caseroya.tablageneral.domain.TablaGeneral;
import com.proyect.caseroya.usuario.domain.Usuario;
import com.proyect.caseroya.venta.domain.DetalleVenta;
import com.proyect.caseroya.venta.domain.DocumentoVenta;
import com.proyect.caseroya.venta.domain.VentaService;
import com.proyect.caseroya.venta.dto.VentaRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @PostMapping
    public ResponseEntity<DocumentoVenta> registrarVenta(@RequestBody VentaRequestDto dto) {
        DocumentoVenta venta = new DocumentoVenta();
        venta.setNumeroDocumento(dto.getNumeroDocumento());

        Cliente cliente = new Cliente();
        cliente.setId(dto.getClienteId());
        venta.setCliente(cliente);

        Usuario usuario = new Usuario();
        usuario.setCodigoUsuario(dto.getUsuarioId());
        venta.setUsuario(usuario);

        TablaGeneral moneda = new TablaGeneral();
        moneda.setId(dto.getMonedaId());
        venta.setMoneda(moneda);

        for (VentaRequestDto.DetalleVentaDto detDto : dto.getDetalles()) {
            DetalleVenta detalle = new DetalleVenta();
            detalle.setProductoId(detDto.getProductoId());
            detalle.setCantidad(detDto.getCantidad());
            detalle.setPrecioUnitario(detDto.getPrecioUnitario());
            venta.addDetalle(detalle);
        }

        DocumentoVenta ventaGuardada = ventaService.registrarVenta(venta);
        return ResponseEntity.ok(ventaGuardada);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentoVenta> obtenerVenta(@PathVariable Integer id) {
        return ResponseEntity.ok(ventaService.obtenerPorId(id));
    }

    @PutMapping("/{id}/anular")
    public ResponseEntity<Void> anularVenta(@PathVariable Integer id) {
        ventaService.anularVenta(id);
        return ResponseEntity.noContent().build();
    }
}
