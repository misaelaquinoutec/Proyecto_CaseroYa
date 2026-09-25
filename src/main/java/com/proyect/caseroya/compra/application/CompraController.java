package com.proyect.caseroya.compra.application;

import com.proyect.caseroya.compra.domain.CompraService;
import com.proyect.caseroya.compra.domain.DetalleCompra;
import com.proyect.caseroya.compra.domain.DocumentoCompra;
import com.proyect.caseroya.compra.dto.CompraRequestDto;
import com.proyect.caseroya.proveedor.domain.Proveedor;
import com.proyect.caseroya.tablageneral.domain.TablaGeneral;
import com.proyect.caseroya.usuario.domain.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/compras")
public class CompraController {

    private final CompraService compraService;

    public CompraController(CompraService compraService) {
        this.compraService = compraService;
    }

    @PostMapping
    public ResponseEntity<DocumentoCompra> registrarCompra(@RequestBody CompraRequestDto dto) {
        DocumentoCompra compra = new DocumentoCompra();
        compra.setNumeroDocumento(dto.getNumeroDocumento());

        Proveedor proveedor = new Proveedor();
        proveedor.setId(dto.getProveedorId());
        compra.setProveedor(proveedor);

        Usuario usuario = new Usuario();
        usuario.setCodigoUsuario(dto.getUsuarioId());
        compra.setUsuario(usuario);

        TablaGeneral moneda = new TablaGeneral();
        moneda.setId(dto.getMonedaId());
        compra.setMoneda(moneda);

        if (dto.getDetalles() != null) {
            for (CompraRequestDto.DetalleCompraDto detalleDto : dto.getDetalles()) {
                DetalleCompra detalle = new DetalleCompra();
                detalle.setProductoId(detalleDto.getProductoId());
                detalle.setCantidad(detalleDto.getCantidad());
                detalle.setPrecioUnitario(detalleDto.getPrecioUnitario());
                compra.addDetalle(detalle);
            }
        }

        return ResponseEntity.ok(compraService.registrarCompra(compra));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentoCompra> obtenerCompra(@PathVariable Integer id) {
        return ResponseEntity.ok(compraService.obtenerPorId(id));
    }

    @PutMapping("/{id}/anular")
    public ResponseEntity<Void> anularCompra(@PathVariable Integer id) {
        compraService.anularCompra(id);
        return ResponseEntity.noContent().build();
    }
}
