# CaseroYa - Sistema de Gestión de Inventarios y Ventas

## 1. Portada
- **Título del Proyecto:** CaseroYa - Solución Integral de Inventario y Ventas
- **Nombre del Curso:** CS 2031 Desarrollo Basado en Plataforma
- **Integrantes:** (Llenar Nombres Aquí)

## 2. Índice
1. Portada
2. Índice
3. Introducción
4. Identificación del Problema o Necesidad
5. Descripción de la Solución
6. Modelo de Entidades
7. Manejo de Errores
8. Medidas de Seguridad Implementadas
9. Eventos y Asincronía
10. GitHub & Management
11. Conclusión
12. Apéndices

## 3. Introducción
**Contexto:** El comercio minorista en la región requiere herramientas tecnológicas modernas que dejen de lado el registro manual de ventas en papel o excel.
**Objetivos:** Desarrollar un backend robusto que permita gestionar catálogo, inventario, usuarios, ventas y compras de forma eficiente, escalable y segura.

## 4. Identificación del Problema o Necesidad
**Descripción del Problema:** Muchas bodegas y minimarkets pierden dinero por la falta de control exacto de su stock y la imposibilidad de auditar las ventas diarias.
**Justificación:** Automatizar este proceso ahorra hasta un 40% del tiempo administrativo del negocio y reduce las mermas.

## 5. Descripción de la Solución
**Funcionalidades:**
- **Seguridad:** Login JWT con encriptación BCrypt y control de accesos basado en roles (RBAC).
- **Catálogo:** Gestión de productos, clientes y proveedores.
- **Inventario:** Control transaccional de aumento y disminución de stock.
- **Ventas y Compras:** Registro de comprobantes con cálculo automático de IGV y actualización inmediata de inventario.

**Tecnologías:**
- Java 26, Spring Boot 3.x (Web, Data JPA, Security, Mail, Validation)
- PostgreSQL (Supabase)
- Docker
- Swagger (Springdoc OpenAPI) para documentación

## 6. Modelo de Entidades
**Diagrama:** (Incluir imagen aquí)
**Descripción:** 
- `Usuario`, `Perfil`, `OpcionMenu`: Control de accesos.
- `Producto`, `ControlStock`: Manejo de inventario (Stock usando CQRS/Record pattern).
- `Cliente`, `Proveedor`: Entidades comerciales.
- `DocumentoVenta` y `DocumentoCompra`: Entidades transaccionales conectadas a sus respectivos Detalles.

## 7. Manejo de Errores
Implementamos un `@RestControllerAdvice` (`GlobalExceptionHandler`) que centraliza el manejo de excepciones. Captura excepciones personalizadas como `RecursoNoEncontradoException`, `InsufficientStockException`, `UnauthorizedException` y `MethodArgumentNotValidException`, transformándolas en un `ErrorResponseDto` estandarizado que incluye timestamp, HTTP status, error y el path de la petición.

## 8. Medidas de Seguridad Implementadas
- **Seguridad de Datos:** Las contraseñas no se guardan en texto plano; usamos `BCryptPasswordEncoder`. La autenticación se maneja vía tokens JWT firmados criptográficamente.
- **Prevención:** Deshabilitamos CSRF (al ser una API REST sin estado) y configuramos el `CorsConfig` estrictamente. Las rutas sensibles requieren `@PreAuthorize("hasRole('ADMIN')")`.

## 9. Eventos y Asincronía
**Eventos:** Implementamos el patrón Observer con Spring Events (`ApplicationEventPublisher`). Cuando se registra una venta, lanzamos un `VentaCreadaEvent`.
**Asincronía:** El listener de la venta (`VentaEventListener`) está marcado con `@Async` y `@TransactionalEventListener`, lo que significa que después de que se hace commit en BD, un hilo en segundo plano (vía `ThreadPoolTaskExecutor`) procesa el envío de un correo HTML (`JavaMailSender`) al administrador sin detener la respuesta HTTP al cliente, garantizando una latencia mínima.

## 10. GitHub & Management
- **Gestión:** Utilizamos GitHub Projects y asignación de Issues por integrante.
- **Ramas:** Implementamos un flujo basado en GitFlow (`feature/modulo-X`).

## 11. Conclusión
- **Logros:** Se consolidó una arquitectura Hexagonal/DDD completamente funcional con más de 10 entidades integradas perfectamente.
- **Aprendizajes:** Dominio de JPA avanzado (Cascade, Lazy Fetching), seguridad con JWT y procesamiento asíncrono.
- **Trabajo Futuro:** Integración con pasarelas de pago y despliegue automatizado con GitHub Actions hacia AWS.

## 12. Apéndices
- **Licencia:** MIT License
- **Referencias:** Documentación oficial de Spring Boot, Baeldung.
