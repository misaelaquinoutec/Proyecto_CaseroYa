package com.proyect.caseroya.stock.infrastructure;

import com.proyect.caseroya.stock.domain.ControlStock;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class StockRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<ControlStock> MAPPER = (rs, rowNum) -> new ControlStock(
            rs.getInt("producto_id"),
            rs.getBigDecimal("cantidad_actual"),
            rs.getObject("actualizado_en", LocalDateTime.class)
    );

    public StockRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ControlStock> listar() {
        return jdbcTemplate.query("SELECT * FROM public.fn_control_stock_listar()", MAPPER);
    }

    public Optional<ControlStock> obtenerPorProducto(Integer productoId) {
        return jdbcTemplate.query(
                "SELECT * FROM public.fn_control_stock_obtener_por_producto(?)", MAPPER, productoId
        ).stream().findFirst();
    }
    public boolean disminuirStock(Integer productoId, java.math.BigDecimal cantidad) {
        Boolean actualizado = jdbcTemplate.queryForObject(
                "SELECT public.fn_control_stock_disminuir(?, ?)",
                Boolean.class, productoId, cantidad
        );
        return Boolean.TRUE.equals(actualizado);
    }
}