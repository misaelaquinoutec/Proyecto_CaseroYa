-- ==============================================================================
-- CASEROYA - SCRIPT COMPLETO DE BASE DE DATOS PARA SUPABASE (POSTGRESQL)
-- Proyecto: CaseroYa - Desarrollo Basado en Plataformas
-- Compatibilidad: Supabase SQL Editor / PostgreSQL 14+
-- Incluye: 13 Tablas, Vistas de Compatibilidad, 10 Funciones PL/pgSQL, Data Semilla
-- ==============================================================================

-- ------------------------------------------------------------------------------
-- 1. LIMPIEZA PREVIA (DROPS EN CASCADA PARA EJECUCIÓN LIMPIA)
-- ------------------------------------------------------------------------------
DO $$ 
BEGIN
    IF EXISTS (SELECT 1 FROM pg_views WHERE schemaname = 'public' AND viewname = 'perfil_opcion_menu') THEN
        DROP VIEW public.perfil_opcion_menu CASCADE;
    ELSIF EXISTS (SELECT 1 FROM pg_tables WHERE schemaname = 'public' AND tablename = 'perfil_opcion_menu') THEN
        DROP TABLE public.perfil_opcion_menu CASCADE;
    END IF;

    IF EXISTS (SELECT 1 FROM pg_views WHERE schemaname = 'public' AND viewname = 'opciones_menu') THEN
        DROP VIEW public.opciones_menu CASCADE;
    ELSIF EXISTS (SELECT 1 FROM pg_tables WHERE schemaname = 'public' AND tablename = 'opciones_menu') THEN
        DROP TABLE public.opciones_menu CASCADE;
    END IF;

    IF EXISTS (SELECT 1 FROM pg_views WHERE schemaname = 'public' AND viewname = 'tablageneral') THEN
        DROP VIEW public.tablageneral CASCADE;
    ELSIF EXISTS (SELECT 1 FROM pg_tables WHERE schemaname = 'public' AND tablename = 'tablageneral') THEN
        DROP TABLE public.tablageneral CASCADE;
    END IF;
END $$;

DROP TABLE IF EXISTS public.detalle_compra CASCADE;
DROP TABLE IF EXISTS public.documentos_compra CASCADE;
DROP TABLE IF EXISTS public.detalle_venta CASCADE;
DROP TABLE IF EXISTS public.documentos_venta CASCADE;
DROP TABLE IF EXISTS public.control_stock CASCADE;
DROP TABLE IF EXISTS public.productos CASCADE;
DROP TABLE IF EXISTS public.proveedores CASCADE;
DROP TABLE IF EXISTS public.clientes CASCADE;
DROP TABLE IF EXISTS public.perfilopcionmenu CASCADE;
DROP TABLE IF EXISTS public.opcionmenu CASCADE;
DROP TABLE IF EXISTS public.tablas_generales CASCADE;
DROP TABLE IF EXISTS public.usuarios CASCADE;
DROP TABLE IF EXISTS public.perfiles CASCADE;

-- ------------------------------------------------------------------------------
-- 2. CREACIÓN DE TABLAS
-- ------------------------------------------------------------------------------

-- 2.1 Perfiles / Roles
CREATE TABLE public.perfiles (
    id_perfil CHAR(3) NOT NULL PRIMARY KEY,
    nombre_perfil VARCHAR(50) NOT NULL
);

-- 2.2 Usuarios
CREATE TABLE public.usuarios (
    codigo_usuario VARCHAR(20) NOT NULL PRIMARY KEY,
    clave VARCHAR(100) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    perfil_id CHAR(3) REFERENCES public.perfiles(id_perfil),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    usuariocreacion VARCHAR(15),
    fechacreacion TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    usuariomodificacion VARCHAR(15),
    fechamodificacion TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW()
);

-- 2.3 Tablas Generales (Tipos de documento, Monedas, etc.)
CREATE TABLE public.tablas_generales (
    id SERIAL PRIMARY KEY,
    tipo VARCHAR(10) NOT NULL,
    codigo VARCHAR(20) NOT NULL,
    descripcion VARCHAR(100) NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    usuariocreacion VARCHAR(15),
    fechacreacion TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    usuariomodificacion VARCHAR(15),
    fechamodificacion TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW()
);

-- 2.4 Menú de Opciones
CREATE TABLE public.opcionmenu (
    opcionid VARCHAR(10) NOT NULL PRIMARY KEY,
    descripcion VARCHAR(100) NOT NULL,
    url VARCHAR(150),
    icono VARCHAR(50),
    orden INTEGER,
    activo BOOLEAN DEFAULT TRUE
);

-- 2.5 Relación Perfil - Menú
CREATE TABLE public.perfilopcionmenu (
    id SERIAL PRIMARY KEY,
    perfil_id VARCHAR(10) NOT NULL REFERENCES public.perfiles(id_perfil) ON DELETE CASCADE,
    opcionid VARCHAR(10) NOT NULL REFERENCES public.opcionmenu(opcionid) ON DELETE CASCADE
);

-- 2.6 Clientes
CREATE TABLE public.clientes (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    numero_documento VARCHAR(15) NOT NULL UNIQUE,
    tipo_documento_id INTEGER NOT NULL,
    telefono VARCHAR(20),
    direccion VARCHAR(200),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    usuariocreacion VARCHAR(15),
    fechacreacion TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    usuariomodificacion VARCHAR(15),
    fechamodificacion TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW()
);

-- 2.7 Proveedores
CREATE TABLE public.proveedores (
    id SERIAL PRIMARY KEY,
    razon_social VARCHAR(150) NOT NULL,
    ruc VARCHAR(11) NOT NULL UNIQUE,
    tipo_documento_id INTEGER NOT NULL,
    telefono VARCHAR(20),
    direccion VARCHAR(200),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    usuariocreacion VARCHAR(15),
    fechacreacion TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    usuariomodificacion VARCHAR(15),
    fechamodificacion TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW()
);

-- 2.8 Productos
CREATE TABLE public.productos (
    id SERIAL PRIMARY KEY,
    codigo VARCHAR(30) NOT NULL UNIQUE,
    nombre VARCHAR(150) NOT NULL,
    categoria VARCHAR(50) NOT NULL,
    unidad_medida VARCHAR(20) NOT NULL,
    precio_venta NUMERIC(12,2) NOT NULL,
    precio_compra NUMERIC(12,2) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    usuariocreacion VARCHAR(15),
    fechacreacion TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    usuariomodificacion VARCHAR(15),
    fechamodificacion TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW()
);

-- 2.9 Control de Stock
CREATE TABLE public.control_stock (
    producto_id INTEGER NOT NULL PRIMARY KEY REFERENCES public.productos(id) ON DELETE CASCADE,
    cantidad_actual NUMERIC(10,2) NOT NULL DEFAULT 0.00,
    actualizado_en TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    usuariocreacion VARCHAR(15),
    fechacreacion TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    usuariomodificacion VARCHAR(15),
    fechamodificacion TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW()
);

-- 2.10 Documentos de Venta (Cabecera)
CREATE TABLE public.documentos_venta (
    id SERIAL PRIMARY KEY,
    numero_documento VARCHAR(20),
    tipo_comprobante VARCHAR(10) DEFAULT '02',
    cliente_id INTEGER REFERENCES public.clientes(id),
    usuario_id VARCHAR(20) REFERENCES public.usuarios(codigo_usuario),
    codigo_usuario VARCHAR(20),
    moneda_id INTEGER REFERENCES public.tablas_generales(id),
    moneda VARCHAR(10) DEFAULT 'PEN',
    fecha_emision DATE NOT NULL DEFAULT CURRENT_DATE,
    subtotal NUMERIC(12,2) NOT NULL DEFAULT 0.00,
    igv NUMERIC(12,2) NOT NULL DEFAULT 0.00,
    total NUMERIC(12,2) NOT NULL DEFAULT 0.00,
    anulado BOOLEAN NOT NULL DEFAULT FALSE,
    estado_sunat VARCHAR(20) DEFAULT 'ACEPTADO',
    respuesta_sunat TEXT,
    usuariocreacion VARCHAR(15),
    fechacreacion TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    usuariomodificacion VARCHAR(15),
    fechamodificacion TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW()
);

-- 2.11 Detalle de Venta
CREATE TABLE public.detalle_venta (
    id SERIAL PRIMARY KEY,
    documento_venta_id INTEGER NOT NULL REFERENCES public.documentos_venta(id) ON DELETE CASCADE,
    producto_id INTEGER NOT NULL REFERENCES public.productos(id),
    cantidad NUMERIC(10,2) NOT NULL DEFAULT 1.00,
    precio_unitario NUMERIC(12,2) NOT NULL DEFAULT 0.00,
    subtotal NUMERIC(12,2) NOT NULL DEFAULT 0.00,
    usuariocreacion VARCHAR(15),
    fechacreacion TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    usuariomodificacion VARCHAR(15),
    fechamodificacion TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW()
);

-- 2.12 Documentos de Compra (Cabecera)
CREATE TABLE public.documentos_compra (
    id SERIAL PRIMARY KEY,
    numero_documento VARCHAR(20),
    proveedor_id INTEGER REFERENCES public.proveedores(id),
    usuario_id VARCHAR(20) REFERENCES public.usuarios(codigo_usuario),
    moneda_id INTEGER REFERENCES public.tablas_generales(id),
    fecha_emision DATE NOT NULL DEFAULT CURRENT_DATE,
    subtotal NUMERIC(12,2) NOT NULL DEFAULT 0.00,
    igv NUMERIC(12,2) NOT NULL DEFAULT 0.00,
    total NUMERIC(12,2) NOT NULL DEFAULT 0.00,
    anulado BOOLEAN NOT NULL DEFAULT FALSE,
    usuariocreacion VARCHAR(15),
    fechacreacion TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    usuariomodificacion VARCHAR(15),
    fechamodificacion TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW()
);

-- 2.13 Detalle de Compra
CREATE TABLE public.detalle_compra (
    id SERIAL PRIMARY KEY,
    documento_compra_id INTEGER NOT NULL REFERENCES public.documentos_compra(id) ON DELETE CASCADE,
    producto_id INTEGER NOT NULL REFERENCES public.productos(id),
    cantidad NUMERIC(10,2) NOT NULL DEFAULT 1.00,
    precio_unitario NUMERIC(12,2) NOT NULL DEFAULT 0.00,
    subtotal NUMERIC(12,2) NOT NULL DEFAULT 0.00
);

-- ------------------------------------------------------------------------------
-- 3. VISTAS DE COMPATIBILIDAD CON CODIGO LEGADO
-- ------------------------------------------------------------------------------
CREATE OR REPLACE VIEW public.tablageneral AS
SELECT id, tipo AS tipotabla, codigo AS codigotabla, descripcion, usuariocreacion, fechacreacion, usuariomodificacion, fechamodificacion
FROM public.tablas_generales;

CREATE OR REPLACE VIEW public.opciones_menu AS
SELECT opcionid AS id_opcion_menu, descripcion AS nombre_opcion, 'ADMIN'::varchar AS usuariocreacion, NOW() AS fechacreacion, 'ADMIN'::varchar AS usuariomodificacion, NOW() AS fechamodificacion
FROM public.opcionmenu;

CREATE OR REPLACE VIEW public.perfil_opcion_menu AS
SELECT perfil_id, opcionid AS opcion_menu_id, 'ADMIN'::varchar AS usuariocreacion, NOW() AS fechacreacion, 'ADMIN'::varchar AS usuariomodificacion, NOW() AS fechamodificacion
FROM public.perfilopcionmenu;


-- ------------------------------------------------------------------------------
-- 4. FUNCIONES PL/pgSQL
-- ------------------------------------------------------------------------------
DROP FUNCTION IF EXISTS public.fn_productos_listar() CASCADE;
DROP FUNCTION IF EXISTS public.fn_productos_obtener_por_id(integer) CASCADE;
DROP FUNCTION IF EXISTS public.fn_productos_crear(character varying, character varying, character varying, character varying, numeric, numeric, character varying) CASCADE;
DROP FUNCTION IF EXISTS public.fn_productos_actualizar(integer, character varying, character varying, character varying, character varying, numeric, numeric, character varying) CASCADE;
DROP FUNCTION IF EXISTS public.fn_productos_eliminar_logico(integer, character varying) CASCADE;
DROP FUNCTION IF EXISTS public.fn_control_stock_listar() CASCADE;
DROP FUNCTION IF EXISTS public.fn_control_stock_obtener_por_producto(integer) CASCADE;
DROP FUNCTION IF EXISTS public.fn_control_stock_disminuir(integer, numeric) CASCADE;
DROP FUNCTION IF EXISTS public.fn_control_stock_aumentar(integer, numeric) CASCADE;
DROP FUNCTION IF EXISTS public.fn_productos_mas_vendidos(date, date, integer) CASCADE;

-- 4.1 fn_productos_listar
CREATE OR REPLACE FUNCTION public.fn_productos_listar()
RETURNS TABLE(
    id integer, 
    codigo character varying, 
    nombre character varying, 
    categoria character varying, 
    unidad_medida character varying, 
    precio_venta numeric, 
    precio_compra numeric, 
    activo boolean
)
LANGUAGE plpgsql AS $$
BEGIN
    RETURN QUERY
    SELECT p.id, p.codigo, p.nombre, p.categoria, p.unidad_medida, p.precio_venta, p.precio_compra, p.activo
    FROM public.productos p
    WHERE p.activo = TRUE
    ORDER BY p.id DESC;
END;
$$;

-- 4.2 fn_productos_obtener_por_id
CREATE OR REPLACE FUNCTION public.fn_productos_obtener_por_id(p_id integer)
RETURNS TABLE(
    id integer, 
    codigo character varying, 
    nombre character varying, 
    categoria character varying, 
    unidad_medida character varying, 
    precio_venta numeric, 
    precio_compra numeric, 
    activo boolean
)
LANGUAGE plpgsql AS $$
BEGIN
    RETURN QUERY
    SELECT p.id, p.codigo, p.nombre, p.categoria, p.unidad_medida, p.precio_venta, p.precio_compra, p.activo
    FROM public.productos p
    WHERE p.id = p_id;
END;
$$;

-- 4.3 fn_productos_crear
CREATE OR REPLACE FUNCTION public.fn_productos_crear(
    p_codigo character varying, 
    p_nombre character varying, 
    p_categoria character varying, 
    p_unidad_medida character varying, 
    p_precio_venta numeric, 
    p_precio_compra numeric, 
    p_usuario character varying
)
RETURNS integer
LANGUAGE plpgsql AS $$
DECLARE
    v_id INT;
BEGIN
    INSERT INTO public.productos (
        codigo, nombre, categoria, unidad_medida, precio_venta, precio_compra,
        activo, usuariocreacion, fechacreacion, usuariomodificacion, fechamodificacion
    ) VALUES (
        p_codigo, p_nombre, p_categoria, p_unidad_medida, p_precio_venta, p_precio_compra,
        TRUE, p_usuario, NOW(), p_usuario, NOW()
    )
    RETURNING id INTO v_id;

    -- Inicializar control de stock automáticamente para el nuevo producto
    INSERT INTO public.control_stock (
        producto_id, cantidad_actual, actualizado_en,
        usuariocreacion, fechacreacion, usuariomodificacion, fechamodificacion
    ) VALUES (
        v_id, 0.00, NOW(),
        p_usuario, NOW(), p_usuario, NOW()
    ) ON CONFLICT (producto_id) DO NOTHING;

    RETURN v_id;
END;
$$;

-- 4.4 fn_productos_actualizar
CREATE OR REPLACE FUNCTION public.fn_productos_actualizar(
    p_id integer, 
    p_codigo character varying, 
    p_nombre character varying, 
    p_categoria character varying, 
    p_unidad_medida character varying, 
    p_precio_venta numeric, 
    p_precio_compra numeric, 
    p_usuario character varying
)
RETURNS boolean
LANGUAGE plpgsql AS $$
BEGIN
    UPDATE public.productos
    SET 
        codigo = p_codigo,
        nombre = p_nombre,
        categoria = p_categoria,
        unidad_medida = p_unidad_medida,
        precio_venta = p_precio_venta,
        precio_compra = p_precio_compra,
        usuariomodificacion = p_usuario,
        fechamodificacion = NOW()
    WHERE id = p_id;

    RETURN FOUND;
END;
$$;

-- 4.5 fn_productos_eliminar_logico
CREATE OR REPLACE FUNCTION public.fn_productos_eliminar_logico(
    p_id integer, 
    p_usuario character varying
)
RETURNS boolean
LANGUAGE plpgsql AS $$
BEGIN
    UPDATE public.productos
    SET 
        activo = FALSE,
        usuariomodificacion = p_usuario,
        fechamodificacion = NOW()
    WHERE id = p_id;

    RETURN FOUND;
END;
$$;

-- 4.6 fn_control_stock_listar
CREATE OR REPLACE FUNCTION public.fn_control_stock_listar()
RETURNS TABLE(
    producto_id integer, 
    cantidad_actual numeric, 
    actualizado_en timestamp without time zone
)
LANGUAGE plpgsql AS $$
BEGIN
    RETURN QUERY
    SELECT s.producto_id, s.cantidad_actual, s.actualizado_en
    FROM public.control_stock s
    ORDER BY s.producto_id ASC;
END;
$$;

-- 4.7 fn_control_stock_obtener_por_producto
CREATE OR REPLACE FUNCTION public.fn_control_stock_obtener_por_producto(p_producto_id integer)
RETURNS TABLE(
    producto_id integer, 
    cantidad_actual numeric, 
    actualizado_en timestamp without time zone
)
LANGUAGE plpgsql AS $$
BEGIN
    RETURN QUERY
    SELECT s.producto_id, s.cantidad_actual, s.actualizado_en
    FROM public.control_stock s
    WHERE s.producto_id = p_producto_id;
END;
$$;

-- 4.8 fn_control_stock_disminuir
CREATE OR REPLACE FUNCTION public.fn_control_stock_disminuir(
    p_producto_id integer, 
    p_cantidad numeric
)
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

-- 4.9 fn_control_stock_aumentar
CREATE OR REPLACE FUNCTION public.fn_control_stock_aumentar(
    p_producto_id integer, 
    p_cantidad numeric
)
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

-- 4.10 fn_productos_mas_vendidos (Reportes)
CREATE OR REPLACE FUNCTION public.fn_productos_mas_vendidos(
    p_fecha_inicio date, 
    p_fecha_fin date, 
    p_limite integer DEFAULT 10
)
RETURNS TABLE(
    producto_id integer, 
    codigo_producto character varying, 
    nombre_producto character varying, 
    categoria character varying, 
    total_unidades_vendidas numeric, 
    total_recaudado numeric
)
LANGUAGE plpgsql AS $$
BEGIN
    RETURN QUERY
    SELECT 
        p.id AS producto_id,
        p.codigo AS codigo_producto,
        p.nombre AS nombre_producto,
        p.categoria,
        COALESCE(SUM(dv.cantidad), 0) AS total_unidades_vendidas,
        COALESCE(SUM(dv.subtotal), 0) AS total_recaudado
    FROM public.detalle_venta dv
    INNER JOIN public.documentos_venta doc ON dv.documento_venta_id = doc.id
    INNER JOIN public.productos p ON dv.producto_id = p.id
    WHERE doc.fecha_emision BETWEEN p_fecha_inicio AND p_fecha_fin
      AND doc.anulado = FALSE
    GROUP BY p.id, p.codigo, p.nombre, p.categoria
    ORDER BY total_unidades_vendidas DESC
    LIMIT p_limite;
END;
$$;


-- ------------------------------------------------------------------------------
-- 5. DATOS SEMILLA (ROLES, USUARIOS BCRYPT, GENERALES, PRODUCTOS, STOCK, DEMO)
-- ------------------------------------------------------------------------------

-- 5.1 Perfiles
INSERT INTO public.perfiles (id_perfil, nombre_perfil) VALUES
('ADM', 'Administrador'),
('CAJ', 'Cajero'),
('GER', 'Gerente'),
('DUE', 'Dueño')
ON CONFLICT (id_perfil) DO UPDATE SET nombre_perfil = EXCLUDED.nombre_perfil;

-- 5.2 Tablas Generales (Tipos de Documento, Monedas, Comprobantes)
INSERT INTO public.tablas_generales (id, tipo, codigo, descripcion, activo, usuariocreacion) VALUES
(1, 'TD', '1', 'DNI', true, 'SYSTEM'),
(2, 'TD', '4', 'CARNET DE EXTRANJERIA', true, 'SYSTEM'),
(3, 'TD', '6', 'RUC', true, 'SYSTEM'),
(4, 'TD', '7', 'PASAPORTE', true, 'SYSTEM'),
(5, 'MO', 'PEN', 'SOLES', true, 'SYSTEM'),
(6, 'MO', 'USD', 'DOLARES', true, 'SYSTEM'),
(7, 'DOC', '01', 'FACTURA', true, 'SYSTEM'),
(8, 'DOC', '02', 'BOLETA DE VENTA', true, 'SYSTEM'),
(9, 'DOC', '03', 'RECIBO POR HONORARIOS', true, 'SYSTEM')
ON CONFLICT (id) DO UPDATE SET descripcion = EXCLUDED.descripcion;

-- 5.3 Menú de Opciones
INSERT INTO public.opcionmenu (opcionid, descripcion, url, icono, orden, activo) VALUES
('POS',  'Punto de Venta',       '/ventas/nueva', 'shopping-cart', 1, true),
('HVEN', 'Historial de Ventas',  '/ventas',       'receipt',       2, true),
('PROD', 'Gestión de Productos', '/inventario',   'box',           3, true),
('STCK', 'Control de Stock',     '/inventario',   'archive',       4, true),
('CLIE', 'Directorio Clientes',  '/clientes',     'users',         5, true),
('PROV', 'Directorio Proveedores','/proveedores', 'truck',         6, true),
('REPO', 'Reportes de Ventas',   '/reportes',     'bar-chart',     7, true),
('USUA', 'Gestión de Usuarios',  '/usuarios',     'user-check',    8, true),
('PERM', 'Permisos de Roles',    '/permisos',     'shield',        9, true)
ON CONFLICT (opcionid) DO NOTHING;

-- 5.4 Asignación de Opciones a Perfiles
INSERT INTO public.perfilopcionmenu (perfil_id, opcionid) VALUES
('ADM', 'POS'), ('ADM', 'HVEN'), ('ADM', 'PROD'), ('ADM', 'STCK'), ('ADM', 'CLIE'), ('ADM', 'PROV'), ('ADM', 'REPO'), ('ADM', 'USUA'), ('ADM', 'PERM'),
('CAJ', 'POS'), ('CAJ', 'HVEN'), ('CAJ', 'PROD'), ('CAJ', 'STCK'), ('CAJ', 'CLIE'),
('GER', 'HVEN'), ('GER', 'PROD'), ('GER', 'STCK'), ('GER', 'CLIE'), ('GER', 'PROV'), ('GER', 'REPO'),
('DUE', 'HVEN'), ('DUE', 'PROD'), ('DUE', 'STCK'), ('DUE', 'CLIE'), ('DUE', 'PROV'), ('DUE', 'REPO')
ON CONFLICT DO NOTHING;

-- 5.5 Usuarios con Hash BCrypt oficial de Spring Security:
-- admin123 -> $2a$10$0Wf2KCFijidGeip2xQymMO.Zjtr4j01.9Cz.6Oo6Rj90zLS1fzAL6
-- 1234     -> $2a$10$gqw561D9toPFwmAaZ1F.YeYUBQqhKDivFAe2YsSk0s75sJv3C7FMi
INSERT INTO public.usuarios (codigo_usuario, clave, nombre, perfil_id, activo, usuariocreacion, usuariomodificacion) VALUES
('ADMIN01',   '$2a$10$0Wf2KCFijidGeip2xQymMO.Zjtr4j01.9Cz.6Oo6Rj90zLS1fzAL6', 'Administrador CaseroYa', 'ADM', true, 'SYSTEM', 'SYSTEM'),
('ACESPEDES', '$2a$10$gqw561D9toPFwmAaZ1F.YeYUBQqhKDivFAe2YsSk0s75sJv3C7FMi', 'Adrian Cespedes',        'ADM', true, 'SYSTEM', 'SYSTEM'),
('PSOTO',     '$2a$10$gqw561D9toPFwmAaZ1F.YeYUBQqhKDivFAe2YsSk0s75sJv3C7FMi', 'Paolo Soto',              'ADM', true, 'SYSTEM', 'SYSTEM'),
('ECALDERON', '$2a$10$gqw561D9toPFwmAaZ1F.YeYUBQqhKDivFAe2YsSk0s75sJv3C7FMi', 'Enzo Calderon',           'ADM', true, 'SYSTEM', 'SYSTEM'),
('JLAMA',     '$2a$10$gqw561D9toPFwmAaZ1F.YeYUBQqhKDivFAe2YsSk0s75sJv3C7FMi', 'Juan Lama',               'ADM', true, 'SYSTEM', 'SYSTEM')
ON CONFLICT (codigo_usuario) DO UPDATE
SET clave = EXCLUDED.clave, perfil_id = EXCLUDED.perfil_id, activo = EXCLUDED.activo;

-- 5.6 Clientes
INSERT INTO public.clientes (id, nombre, tipo_documento_id, numero_documento, telefono, direccion, activo, usuariocreacion) VALUES
(1, 'Carlos Ramos', 1, '45896321', '987654321', 'Av. Lima 123', true, 'ADMIN01'),
(2, 'Ronald Barranco', 1, '61679025', '916504785', 'Barranco', true, 'ADMIN01'),
(3, 'Adval Miraflores', 3, '74587534', '916502486', 'Miraflores', true, 'ADMIN01'),
(4, 'Rodrigo Olivos', 2, '21456378', '916504476', 'Los Olivos', true, 'ADMIN01'),
(5, 'Distribuidora San Jorge SAC', 3, '20123456789', '916504486', 'Av. Colonial 456', true, 'ADMIN01')
ON CONFLICT (numero_documento) DO NOTHING;

-- 5.7 Proveedores
INSERT INTO public.proveedores (id, razon_social, ruc, tipo_documento_id, telefono, direccion, activo, usuariocreacion) VALUES
(1, 'Alicorp S.A.A.', '20100055237', 3, '013150000', 'Av. Argentina 4793, Callao', true, 'ADMIN01'),
(2, 'Gloria S.A.', '20100190797', 3, '014707170', 'Av. Republica de Panama 2461, Lima', true, 'ADMIN01'),
(3, 'Molitalia S.A.', '20100035121', 3, '015135700', 'Av. Venezuela 2850, Lima', true, 'ADMIN01'),
(4, 'Compania Nacional de Chocolates de Peru S.A.', '20100030595', 3, '015188000', 'Av. Maquinarias 2190, Lima', true, 'ADMIN01')
ON CONFLICT (ruc) DO NOTHING;

-- 5.8 Productos
INSERT INTO public.productos (id, codigo, nombre, categoria, unidad_medida, precio_venta, precio_compra, activo, usuariocreacion, usuariomodificacion) VALUES
(1,  '38087829', 'Galleta Soda San Jorge',               'Galletas y Dulces', 'KG',  1.50, 1.20, true, 'ADMIN01', 'ADMIN01'),
(2,  '77501820', 'Galleta Casino Chocolate 43g',         'Galletas y Dulces', 'UND', 1.20, 0.90, true, 'ADMIN01', 'ADMIN01'),
(3,  '77501821', 'Galleta Tentacion Vainilla 40g',        'Galletas y Dulces', 'UND', 1.20, 0.90, true, 'ADMIN01', 'ADMIN01'),
(4,  '77501822', 'Galleta Choco Soda Field 36g',         'Galletas y Dulces', 'UND', 1.50, 1.15, true, 'ADMIN01', 'ADMIN01'),
(5,  '77501823', 'Galleta Dona Pepa Field 23g',          'Galletas y Dulces', 'UND', 1.80, 1.40, true, 'ADMIN01', 'ADMIN01'),
(6,  '77501824', 'Caramelos Lemonhead 50g',              'Galletas y Dulces', 'UND', 1.00, 0.70, true, 'ADMIN01', 'ADMIN01'),
(7,  '77501825', 'Chocolate Sublime Clasico 30g',        'Galletas y Dulces', 'UND', 2.50, 1.90, true, 'ADMIN01', 'ADMIN01'),
(8,  '77501826', 'Chocolate Triangulo DOnofrio 30g',     'Galletas y Dulces', 'UND', 2.50, 1.95, true, 'ADMIN01', 'ADMIN01'),
(9,  '77501827', 'Wafer Costa Vainilla 70g',             'Galletas y Dulces', 'UND', 2.00, 1.50, true, 'ADMIN01', 'ADMIN01'),
(10, '77501828', 'Marshmallows Millows 100g',            'Galletas y Dulces', 'UND', 3.50, 2.60, true, 'ADMIN01', 'ADMIN01'),
(11, '77500010', 'Arroz Superior Costeno 1kg',           'Abarrotes',         'KG',  4.80, 4.00, true, 'ADMIN01', 'ADMIN01'),
(12, '77500011', 'Arroz Extra Paisana 1kg',              'Abarrotes',         'KG',  4.50, 3.80, true, 'ADMIN01', 'ADMIN01'),
(13, '77500012', 'Azucar Rubio Faraon 1kg',              'Abarrotes',         'KG',  3.80, 3.10, true, 'ADMIN01', 'ADMIN01'),
(14, '77500013', 'Aceite Vegetal Primor 1L',             'Abarrotes',         'UND', 8.90, 7.30, true, 'ADMIN01', 'ADMIN01'),
(15, '77500014', 'Aceite Soya Cocinero 900ml',           'Abarrotes',         'UND', 7.50, 6.00, true, 'ADMIN01', 'ADMIN01'),
(16, '77500015', 'Fideos Tallarin Don Vittorio 500g',    'Abarrotes',         'UND', 3.20, 2.40, true, 'ADMIN01', 'ADMIN01'),
(17, '77500016', 'Fideos Canuto Molitalia 500g',         'Abarrotes',         'UND', 3.40, 2.55, true, 'ADMIN01', 'ADMIN01'),
(18, '77500017', 'Avena Grano Entero 3 Ositos 400g',     'Abarrotes',         'UND', 3.80, 2.90, true, 'ADMIN01', 'ADMIN01'),
(19, '77500018', 'Sal Yodada Emsal 1kg',                 'Abarrotes',         'KG',  1.80, 1.20, true, 'ADMIN01', 'ADMIN01'),
(20, '77500019', 'Lenteja Bebe Costeno 500g',            'Abarrotes',         'UND', 4.50, 3.60, true, 'ADMIN01', 'ADMIN01'),
(21, '77500201', 'Leche Evaporada Gloria Azul 395g',     'Lacteos y Huevos',  'UND', 4.20, 3.60, true, 'ADMIN01', 'ADMIN01'),
(22, '77500202', 'Leche Evaporada Ideal Light 395g',     'Lacteos y Huevos',  'UND', 4.00, 3.40, true, 'ADMIN01', 'ADMIN01'),
(23, '77500203', 'Yogurt Batido Gloria Fresa 1kg',       'Lacteos y Huevos',  'UND', 6.50, 5.10, true, 'ADMIN01', 'ADMIN01'),
(24, '77500204', 'Yogurt Griego Tottus Vainilla 150g',   'Lacteos y Huevos',  'UND', 3.80, 2.80, true, 'ADMIN01', 'ADMIN01'),
(25, '77500205', 'Mantequilla La Preferida con Sal 200g','Lacteos y Huevos',  'UND', 8.50, 6.80, true, 'ADMIN01', 'ADMIN01'),
(26, '77500206', 'Margarina Sello de Oro 200g',          'Lacteos y Huevos',  'UND', 4.20, 3.30, true, 'ADMIN01', 'ADMIN01'),
(27, '77500207', 'Queso Edam Bonle Molde 200g',          'Lacteos y Huevos',  'UND', 9.90, 8.00, true, 'ADMIN01', 'ADMIN01')
ON CONFLICT (codigo) DO UPDATE
SET nombre = EXCLUDED.nombre, precio_venta = EXCLUDED.precio_venta, precio_compra = EXCLUDED.precio_compra;

-- 5.9 Control de Stock Inicial
INSERT INTO public.control_stock (producto_id, cantidad_actual, usuariocreacion, usuariomodificacion) VALUES
(1,  50.00,  'ADMIN01', 'ADMIN01'),
(2,  100.00, 'ADMIN01', 'ADMIN01'),
(3,  80.00,  'ADMIN01', 'ADMIN01'),
(4,  90.00,  'ADMIN01', 'ADMIN01'),
(5,  120.00, 'ADMIN01', 'ADMIN01'),
(6,  60.00,  'ADMIN01', 'ADMIN01'),
(7,  150.00, 'ADMIN01', 'ADMIN01'),
(8,  110.00, 'ADMIN01', 'ADMIN01'),
(9,  75.00,  'ADMIN01', 'ADMIN01'),
(10, 40.00,  'ADMIN01', 'ADMIN01'),
(11, 200.00, 'ADMIN01', 'ADMIN01'),
(12, 180.00, 'ADMIN01', 'ADMIN01'),
(13, 150.00, 'ADMIN01', 'ADMIN01'),
(14, 90.00,  'ADMIN01', 'ADMIN01'),
(15, 85.00,  'ADMIN01', 'ADMIN01'),
(16, 120.00, 'ADMIN01', 'ADMIN01'),
(17, 100.00, 'ADMIN01', 'ADMIN01'),
(18, 70.00,  'ADMIN01', 'ADMIN01'),
(19, 130.00, 'ADMIN01', 'ADMIN01'),
(20, 60.00,  'ADMIN01', 'ADMIN01'),
(21, 250.00, 'ADMIN01', 'ADMIN01'),
(22, 140.00, 'ADMIN01', 'ADMIN01'),
(23, 80.00,  'ADMIN01', 'ADMIN01'),
(24, 65.00,  'ADMIN01', 'ADMIN01'),
(25, 45.00,  'ADMIN01', 'ADMIN01'),
(26, 70.00,  'ADMIN01', 'ADMIN01'),
(27, 35.00,  'ADMIN01', 'ADMIN01')
ON CONFLICT (producto_id) DO UPDATE
SET cantidad_actual = EXCLUDED.cantidad_actual, actualizado_en = NOW();

-- 5.10 Venta Inicial de Demostración
INSERT INTO public.documentos_venta (id, numero_documento, cliente_id, usuario_id, codigo_usuario, moneda_id, fecha_emision, subtotal, igv, total, anulado, usuariocreacion, usuariomodificacion) VALUES
(1, 'B001-00000001', 1, 'ADMIN01', 'ADMIN01', 5, CURRENT_DATE, 20.00, 3.60, 23.60, false, 'ADMIN01', 'ADMIN01')
ON CONFLICT (id) DO NOTHING;

INSERT INTO public.detalle_venta (id, documento_venta_id, producto_id, cantidad, precio_unitario, subtotal, usuariocreacion, usuariomodificacion) VALUES
(1, 1, 14, 2.00, 8.90, 17.80, 'ADMIN01', 'ADMIN01'),
(2, 1, 7,  2.00, 2.50, 5.00,  'ADMIN01', 'ADMIN01')
ON CONFLICT (id) DO NOTHING;

-- ------------------------------------------------------------------------------
-- 6. ACTUALIZACIÓN DE SECUENCIAS (EVITAR CONFLICTOS DE IDS)
-- ------------------------------------------------------------------------------
SELECT setval('public.tablas_generales_id_seq', COALESCE((SELECT MAX(id) FROM public.tablas_generales), 1));
SELECT setval('public.clientes_id_seq', COALESCE((SELECT MAX(id) FROM public.clientes), 1));
SELECT setval('public.proveedores_id_seq', COALESCE((SELECT MAX(id) FROM public.proveedores), 1));
SELECT setval('public.productos_id_seq', COALESCE((SELECT MAX(id) FROM public.productos), 1));
SELECT setval('public.documentos_venta_id_seq', COALESCE((SELECT MAX(id) FROM public.documentos_venta), 1));
SELECT setval('public.detalle_venta_id_seq', COALESCE((SELECT MAX(id) FROM public.detalle_venta), 1));
SELECT setval('public.documentos_compra_id_seq', COALESCE((SELECT MAX(id) FROM public.documentos_compra), 1));
SELECT setval('public.detalle_compra_id_seq', COALESCE((SELECT MAX(id) FROM public.detalle_compra), 1));
SELECT setval('public.perfilopcionmenu_id_seq', COALESCE((SELECT MAX(id) FROM public.perfilopcionmenu), 1));

-- ==============================================================================
-- FIN DEL SCRIPT SUPABASE
-- ==============================================================================
