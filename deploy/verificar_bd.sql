-- Ejecutar esto en el SQL Editor de la base del equipo para comprobar existencia de objetos.

WITH tablas(nombre) AS (
    VALUES ('public.perfiles'), ('public.usuarios'),
           ('public.opcionmenu'), ('public.perfilopcionmenu'),
           ('public.clientes'), ('public.proveedores'), ('public.tablas_generales'),
           ('public.productos'), ('public.control_stock'),
           ('public.documentos_venta'), ('public.detalle_venta'),
           ('public.documentos_compra'), ('public.detalle_compra')
)
SELECT nombre, to_regclass(nombre) IS NOT NULL AS existe
FROM tablas ORDER BY nombre;

WITH funciones(firma) AS (
    VALUES
      ('public.fn_productos_listar()'),
      ('public.fn_productos_obtener_por_id(integer)'),
      ('public.fn_productos_crear(character varying,character varying,character varying,character varying,numeric,numeric,character varying)'),
      ('public.fn_productos_actualizar(integer,character varying,character varying,character varying,character varying,numeric,numeric,character varying)'),
      ('public.fn_productos_eliminar_logico(integer,character varying)'),
      ('public.fn_control_stock_listar()'),
      ('public.fn_control_stock_obtener_por_producto(integer)'),
      ('public.fn_control_stock_disminuir(integer,numeric)'),
      ('public.fn_control_stock_aumentar(integer,numeric)')
)
SELECT firma, to_regprocedure(firma) IS NOT NULL AS existe
FROM funciones ORDER BY firma;

-- Ejecutar las siguientes consultas si las tablas anteriores existen.
-- El id del perfil administrador se usa en ADMIN_ROLE.
SELECT id_perfil, nombre_perfil FROM public.perfiles ORDER BY id_perfil;

-- Debe existir al menos una cuenta activa con el perfil administrador.
SELECT perfil_id, COUNT(*) AS cuentas_activas
FROM public.usuarios
WHERE activo = true
GROUP BY perfil_id ORDER BY perfil_id;

