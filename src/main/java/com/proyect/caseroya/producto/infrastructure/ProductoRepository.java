package com.proyect.caseroya.producto.infrastructure;

import com.proyect.caseroya.producto.domain.Producto;
import com.proyect.caseroya.producto.dto.ProductoRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductoRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<Producto> MAPPER = (rs, rowNum) -> new Producto(
            rs.getInt("id"),
            rs.getString("codigo"),
            rs.getString("nombre"),
            rs.getString("categoria"),
            rs.getString("unidad_medida"),
            rs.getBigDecimal("precio_venta"),
            rs.getBigDecimal("precio_compra"),
            rs.getBoolean("activo")
    );

    public ProductoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Producto> listar() {
        return jdbcTemplate.query("SELECT * FROM public.fn_productos_listar()", MAPPER);
    }

    public Optional<Producto> obtenerPorId(Integer id) {
        return jdbcTemplate.query(
                "SELECT * FROM public.fn_productos_obtener_por_id(?)", MAPPER, id
        ).stream().findFirst();
    }

    public Integer crear(ProductoRequest datos) {
        return jdbcTemplate.queryForObject(
                "SELECT public.fn_productos_crear(?, ?, ?, ?, ?, ?, ?)",
                Integer.class,
                datos.codigo(), datos.nombre(), datos.categoria(), datos.unidadMedida(),
                datos.precioVenta(), datos.precioCompra(), datos.usuario()
        );
    }

    public boolean actualizar(Integer id, ProductoRequest datos) {
        Boolean actualizado = jdbcTemplate.queryForObject(
                "SELECT public.fn_productos_actualizar(?, ?, ?, ?, ?, ?, ?, ?)",
                Boolean.class,
                id, datos.codigo(), datos.nombre(), datos.categoria(), datos.unidadMedida(),
                datos.precioVenta(), datos.precioCompra(), datos.usuario()
        );
        return Boolean.TRUE.equals(actualizado);
    }

    public boolean eliminarLogico(Integer id, String usuario) {
        Boolean eliminado = jdbcTemplate.queryForObject(
                "SELECT public.fn_productos_eliminar_logico(?, ?)",
                Boolean.class, id, usuario
        );
        return Boolean.TRUE.equals(eliminado);
    }
}
