package com.proyect.caseroya.config.security;

import com.proyect.caseroya.usuario.domain.Usuario;
import com.proyect.caseroya.usuario.infrastructure.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JdbcTemplate jdbcTemplate;

    public DataInitializer(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JdbcTemplate jdbcTemplate) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) {
        initStoredFunctions();

        if (!usuarioRepository.existsByCodigoUsuario("ADMIN01")) {
            Usuario admin = new Usuario();
            admin.setCodigoUsuario("ADMIN01");
            admin.setClave(passwordEncoder.encode("admin123"));
            admin.setNombre("Administrador CaseroYa");
            admin.setPerfilId("ADM");
            admin.setActivo(true);
            admin.setUsuarioCreacion("SYSTEM_INIT");
            admin.setFechaCreacion(LocalDateTime.now());
            usuarioRepository.save(admin);
        }
    }

    private void initStoredFunctions() {
        try {
            jdbcTemplate.execute("""
                CREATE OR REPLACE FUNCTION public.fn_productos_listar()
                RETURNS TABLE(id integer, codigo character varying, nombre character varying, categoria character varying, unidad_medida character varying, precio_venta numeric, precio_compra numeric, activo boolean)
                LANGUAGE plpgsql AS $$
                BEGIN
                    RETURN QUERY
                    SELECT p.id, p.codigo, p.nombre, p.categoria, p.unidad_medida, p.precio_venta, p.precio_compra, p.activo
                    FROM public.productos p
                    WHERE p.activo = TRUE
                    ORDER BY p.id DESC;
                END;
                $$;
            """);

            jdbcTemplate.execute("""
                CREATE OR REPLACE FUNCTION public.fn_productos_obtener_por_id(p_id integer)
                RETURNS TABLE(id integer, codigo character varying, nombre character varying, categoria character varying, unidad_medida character varying, precio_venta numeric, precio_compra numeric, activo boolean)
                LANGUAGE plpgsql AS $$
                BEGIN
                    RETURN QUERY
                    SELECT p.id, p.codigo, p.nombre, p.categoria, p.unidad_medida, p.precio_venta, p.precio_compra, p.activo
                    FROM public.productos p
                    WHERE p.id = p_id;
                END;
                $$;
            """);

            jdbcTemplate.execute("""
                CREATE OR REPLACE FUNCTION public.fn_control_stock_disminuir(p_producto_id integer, p_cantidad numeric)
                RETURNS boolean
                LANGUAGE plpgsql AS $$
                BEGIN
                    UPDATE public.control_stock
                    SET 
                        cantidad_actual = cantidad_actual - p_cantidad,
                        actualizado_en = NOW()
                    WHERE producto_id = p_producto_id;

                    RETURN FOUND;
                END;
                $$;
            """);

            jdbcTemplate.execute("""
                CREATE OR REPLACE FUNCTION public.fn_control_stock_aumentar(p_producto_id integer, p_cantidad numeric)
                RETURNS boolean
                LANGUAGE plpgsql AS $$
                BEGIN
                    UPDATE public.control_stock
                    SET 
                        cantidad_actual = cantidad_actual + p_cantidad,
                        actualizado_en = NOW()
                    WHERE producto_id = p_producto_id;

                    RETURN FOUND;
                END;
                $$;
            """);
        } catch (Exception e) {
            System.err.println("Aviso al inicializar funciones PL/pgSQL: " + e.getMessage());
        }
    }
}
