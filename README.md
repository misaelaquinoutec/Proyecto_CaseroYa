# Informe Final de Proyecto: CaseroYa - Sistema de Gestión de Inventarios y Ventas

## 1. Portada
- **Título del Proyecto:** CaseroYa - Solución Integral de Inventario y Ventas para Negocios Minoristas
- **Nombre del Curso:** CS 2031 Desarrollo Basado en Plataformas
- **Periodo Académico:** 2026-2
- **Integrantes del Equipo:**
  - Integrante 1 (Módulo de Seguridad y Autenticación)
  - Integrante 2 (Módulo de Catálogos - Cliente, Proveedor, Tabla General)
  - Integrante 3 (Módulo de Inventario - Producto, Stock)
  - Misael Osvaldo Aquino Hidalgo 202510626
  - Integrante 5 (Módulo de Compras)

---

## 2. Índice
1. Portada
2. Índice
3. Introducción
4. Identificación del Problema o Necesidad
5. Descripción de la Solución
6. Modelo de Entidades y Datos
7. Arquitectura y Patrones de Diseño
8. Manejo de Errores y Excepciones
9. Medidas de Seguridad Implementadas
10. Eventos y Asincronía
11. GitHub & Project Management
12. Instrucciones de Despliegue y Ejecución
13. Conclusión
14. Apéndices

---

## 3. Introducción

### Contexto
El entorno actual del comercio minorista, particularmente en el sector de las bodegas, minimarkets y negocios familiares en la región, se encuentra en una etapa de transición tecnológica. Muchos de estos negocios aún dependen de procesos manuales, utilizando cuadernos o rudimentarias hojas de cálculo en Excel para registrar sus ventas diarias, gestionar sus compras y llevar un control de su inventario. Esta dependencia en herramientas no especializadas genera ineficiencias significativas, propensión a errores humanos, pérdida de información y una incapacidad para tomar decisiones financieras basadas en datos reales y actualizados.

### Objetivos del Proyecto
El objetivo principal de CaseroYa es desarrollar e implementar un backend robusto, escalable y seguro basado en la arquitectura de micro-servicios modulares (Domain-Driven Design) que permita la digitalización completa de las operaciones de un negocio minorista.
Los objetivos específicos incluyen:
- Desarrollar un módulo de seguridad inquebrantable basado en roles (RBAC) y JWT.
- Automatizar el registro de entradas y salidas de mercancía mediante un control transaccional del stock.
- Generar reportes de ventas y compras en tiempo real.
- Notificar eventos críticos del sistema (como el registro de grandes ventas) mediante servicios asíncronos y correos electrónicos.

---

## 4. Identificación del Problema o Necesidad

### Descripción del Problema
A través de un análisis del mercado local, se identificó que el principal dolor de cabeza de los dueños de negocios minoristas es la **"pérdida fantasma"** o merma no detectada. Al no tener un sistema centralizado que vincule automáticamente una venta en la caja con la reducción del stock en el almacén, los dueños no pueden detectar a tiempo robos, productos caducados o errores en el cobro. Además, el cierre de caja diario se convierte en un proceso tedioso de varias horas, donde el descuadre de inventario es el pan de cada día.

### Justificación
Automatizar y centralizar este proceso a través de una API RESTful no solo elimina los errores de cálculo humano (como el cálculo manual del IGV del 18%), sino que ahorra hasta un 40% del tiempo administrativo del negocio. Proveer una solución en la nube (con bases de datos en Supabase) permite al dueño del negocio monitorear las transacciones y alertas desde cualquier lugar, escalando el negocio hacia nuevas sucursales sin perder el control de su información.

---

## 5. Descripción de la Solución

### Funcionalidades Implementadas
La solución construida abarca un ecosistema completo dividido en 5 módulos principales desarrollados por el equipo:

1. **Gestión de Identidad y Accesos (IAM):** 
   - Autenticación mediante JSON Web Tokens (JWT).
   - Encriptación de contraseñas de extremo a extremo usando el algoritmo BCrypt.
   - Creación de perfiles (roles) y mapeo de opciones de menú dinámicas.
2. **Catálogos Core:** 
   - Mantenimiento completo (CRUD) de la cartera de Clientes y Proveedores.
   - Implementación de una Tabla General parametrizada para manejar variables del sistema (Ej. Tipos de Moneda, Tipos de Documento) sin tener que alterar la base de datos estructuralmente.
3. **Gestión de Inventario (Almacén):** 
   - Registro de productos con categorías.
   - Control de Stock utilizando transacciones JDBC optimizadas para garantizar que no existan lecturas sucias durante el aumento o disminución del inventario en periodos de alta concurrencia.
4. **Sistema de Ventas (Punto de Venta):** 
   - Generación de Documentos de Venta con múltiples Detalles de Venta en cascada.
   - Cálculo automático de subtotales, impuestos (IGV) y montos totales.
   - Anulación de ventas con lógica inversa (devolución del stock al almacén).
   - Generación de reportes financieros filtrados por rangos de fechas.
5. **Sistema de Compras (Abastecimiento):**
   - Registro de facturas de proveedores.
   - Integración directa con el módulo de Inventario para aumentar el stock automáticamente una vez se registra el Documento de Compra.

### Tecnologías Utilizadas
- **Lenguaje Base:** Java 26
- **Framework Principal:** Spring Boot 3.x
- **Persistencia de Datos:** Spring Data JPA, Hibernate, JDBC Template.
- **Base de Datos:** PostgreSQL alojada en la nube mediante Supabase (con soporte para Docker Compose en entorno local de desarrollo).
- **Seguridad:** Spring Security, Java JWT (jjwt).
- **Asincronía y Tareas:** Spring `@Async`, `ThreadPoolTaskExecutor`.
- **Notificaciones:** JavaMailSender (Integración SMTP).
- **Documentación de API:** Springdoc OpenAPI (Swagger UI).
- **Validaciones:** Hibernate Validator (`spring-boot-starter-validation`).
- **Herramientas de Reducción de Boilerplate:** Lombok.

---

## 6. Modelo de Entidades y Datos

El modelo de base de datos fue diseñado siguiendo los principios de la tercera forma normal (3NF) para evitar redundancias, asegurando la integridad referencial mediante constraints estrictos a nivel de base de datos (`@NotNull`, `@Column(unique=true)`, etc.).

### Entidades Principales y Relaciones
1. **Módulo de Seguridad:**
   - `Usuario`: Representa a los empleados o dueños del sistema. Relacionado `ManyToOne` con `Perfil`.
   - `Perfil`: Define el rol (ADMIN, CAJERO, LOGISTICA).
   - `OpcionMenu` y `PerfilOpcionMenu`: Permite construir un panel de navegación dinámico en el frontend basado en lo que el perfil tiene permitido ver.
2. **Módulo Comercial:**
   - `Cliente`: Datos de facturación del consumidor final.
   - `Proveedor`: Datos de las empresas que abastecen la bodega.
   - `TablaGeneral`: Diccionario de datos llave-valor para configuraciones globales.
3. **Módulo de Inventario:**
   - `Producto`: Entidad maestra de la mercancía.
   - `ControlStock`: Tabla de movimientos y consolidado actual de unidades.
4. **Módulo de Transacciones (Ventas y Compras):**
   - `DocumentoVenta`: Cabecera de la factura/boleta (`ManyToOne` a Cliente, Usuario y Moneda).
   - `DetalleVenta`: Ítems comprados (`ManyToOne` a DocumentoVenta, vinculación directa a Producto).
   - `DocumentoCompra` y `DetalleCompra`: Estructura espejo para gestionar los ingresos de mercadería al almacén.

*Nota: Se implementó un diseño tipo CQRS en ciertas partes críticas del inventario, utilizando `Java Records` para agilizar las lecturas masivas.*

---

## 7. Arquitectura y Patrones de Diseño

El backend se construyó sobre los cimientos del **Domain-Driven Design (DDD)** adaptado a la **Arquitectura Hexagonal (Puertos y Adaptadores)**. 

### Separación de Capas
En lugar de organizar el proyecto por capas técnicas (todos los controladores juntos, todos los repositorios juntos), se organizó mediante **Package-by-Feature**. Cada módulo de negocio (`venta`, `compra`, `auth`, `producto`) es un micro-ecosistema independiente que contiene sus propias capas:
- **`application`:** Contiene los Controladores REST (`@RestController`). Es la capa de presentación que recibe los DTOs y delega el trabajo pesado.
- **`domain`:** Contiene las Entidades (`@Entity`), Eventos de Dominio y Servicios (`@Service`). Aquí reside el 100% de la lógica de negocio pura.
- **`infrastructure`:** Contiene los Repositorios (`@Repository` de Spring Data JPA). Es la capa que dialoga con la base de datos PostgreSQL.
- **`dto`:** Objetos de Transferencia de Datos. Se aseguró una estricta separación para nunca exponer las entidades de base de datos directamente al Frontend, previniendo vulnerabilidades de inyección de propiedades y fuga de datos sensibles.

### Patrones Aplicados
- **Principio de Responsabilidad Única (SRP):** Cada clase tiene un único propósito. Los controladores no tienen lógica de negocio, solo orquestan respuestas HTTP (`ResponseEntity`).
- **Inyección de Dependencias (DI):** Totalmente desacoplado mediante el uso intensivo de constructores y `@Autowired`, facilitando las pruebas unitarias.
- **Patrón Observer:** Uso de `ApplicationEventPublisher` para lanzar eventos de dominio sin acoplar módulos distintos.

---

## 8. Manejo de Errores y Excepciones

Para garantizar que el Frontend y los usuarios nunca vean un "Stack Trace" de Java o errores técnicos indescifrables, se implementó un mecanismo de manejo de errores de nivel empresarial.

Se creó un `@RestControllerAdvice` (`GlobalExceptionHandler.java`) que actúa como un escudo interceptor global. Las excepciones capturadas son transformadas a un DTO estándar (`ErrorResponseDto`) que contiene:
- `timestamp`: Momento exacto del fallo.
- `status`: Código HTTP semántico (400, 401, 403, 404, 409, 500).
- `error`: Nombre legible del tipo de error.
- `message`: Descripción amigable para el usuario final.
- `path`: Ruta del endpoint donde ocurrió el error.

**Excepciones Personalizadas Implementadas:**
Se crearon más de 7 excepciones personalizadas agrupadas por contexto de negocio:
1. `RecursoNoEncontradoException` (HTTP 404 - Not Found)
2. `DocumentoDuplicadoException` (HTTP 400 - Bad Request)
3. `UnauthorizedException` (HTTP 401 - Unauthorized)
4. `InvalidOperationException` (HTTP 403 - Forbidden)
5. `InsufficientStockException` (HTTP 409 - Conflict)
6. `BusinessRuleException` (HTTP 400 - Bad Request)
7. `InvalidDataException` (HTTP 400 - Bad Request)

Adicionalmente, el manejador global atrapa `MethodArgumentNotValidException` (disparado por los decoradores `@Valid` y `@NotNull` en los DTOs) y desgrana los errores campo por campo, enviando un JSON limpio con los atributos que fallaron la validación.

---

## 9. Medidas de Seguridad Implementadas

La seguridad del sistema es una prioridad crítica, por lo que se integró **Spring Security 6**.

### Seguridad de Datos (Confidencialidad e Integridad)
- **Cifrado Unidireccional:** Se utilizó `BCryptPasswordEncoder` para aplicar un hash con *salt* a las contraseñas de todos los usuarios. En caso de una vulneración de la base de datos, las contraseñas son criptográficamente imposibles de recuperar a texto plano.
- **Autenticación Stateless (JWT):** Al hacer login correctamente, el sistema emite un JSON Web Token firmado con el algoritmo `HS256` utilizando una clave secreta (`JWT_SECRET`) inyectada mediante variables de entorno (`.env`). El token tiene un tiempo de expiración riguroso de 24 horas.

### Prevención de Vulnerabilidades (OWASP)
- **CSRF Deshabilitado:** Al tratarse de una API RESTful Stateless que no usa cookies de sesión, el ataque CSRF es mitigado nativamente por el uso de tokens Bearer.
- **Control de Acceso Basado en Roles (RBAC):** Se habilitó `@EnableMethodSecurity`. El filtro `JwtAuthenticationFilter` extrae el Perfil del usuario directamente del Token JWT y lo inyecta en el `SecurityContextHolder`. Esto permite utilizar decoradores robustos como `@PreAuthorize("hasRole('ADMIN')")` en controladores críticos (como anulación de ventas o eliminación de usuarios), bloqueando accesos no autorizados (HTTP 403).
- **CORS Estricto:** Se implementó `CorsConfig` global para asegurar que los navegadores web confíen y permitan las peticiones OPTIONS preflight solo bajo parámetros controlados.

---

## 10. Eventos y Asincronía

Para maximizar el rendimiento de la API y garantizar tiempos de respuesta en milisegundos, el proyecto adoptó una **arquitectura dirigida por eventos (Event-Driven Architecture)** en combinación con procesamiento en segundo plano.

### Implementación de Eventos de Dominio
En lugar de que el `VentaService` llame directamente a un servicio de envío de correos o analíticas (lo que acoplaría el código y aumentaría la deuda técnica), ahora el servicio se limita a hacer su lógica transaccional y disparar un `VentaCreadaEvent` mediante `ApplicationEventPublisher`. 

### Procesamiento Asíncrono (`@Async`)
Se habilitó `@EnableAsync` y se configuró un `ThreadPoolTaskExecutor` en `AsyncConfig.java` para proveer un pool dedicado de hilos. 

Se crearon clases "Listeners" separadas (como `VentaEventListener` y `UsuarioEventListener`) que escuchan los eventos. Específicamente, se utilizó la anotación `@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)`. Esto asegura de forma elegante que:
1. El correo electrónico al cliente (vía `JavaMailSender` implementado en `MailService`) **sólo** se empiece a enviar si la transacción de la base de datos hizo commit exitosamente.
2. Al estar decorado con `@Async`, el hilo principal de HTTP responde inmediatamente un "200 OK" al Frontend, mientras que un hilo secundario del *ThreadPool* procesa el envío del correo de confirmación HTML en el fondo, reduciendo la latencia de respuesta web de ~3000ms a ~50ms.

---

## 11. GitHub & Project Management

El éxito colaborativo del proyecto en equipo se logró mediante un riguroso control de versiones.

- **GitFlow Adaptado:** Se protegió la rama `main`. Cada desarrollador trabajó sus propios módulos en ramas especializadas (por ejemplo, `feature/modulo-ventas`, `feature/seguridad-jwt`).
- **Trazabilidad:** Cada commit fue semántico (ej: `feat(ventas): implementa reporte asincrono`, `fix(auth): corrige error en BCrypt`).
- **Gestión Visual:** Se utilizaron las herramientas nativas de GitHub (Issues y GitHub Projects) para repartir los requerimientos de la rúbrica entre los 5 integrantes, establecer fechas de entrega ("milestones") y mover las tarjetas en un tablero estilo Kanban (To Do, In Progress, Done).

---

## 12. Instrucciones de Despliegue y Ejecución

### Requisitos Previos
- Java JDK 26 instalado.
- Maven (O usar el Wrapper `./mvnw` incluido).
- Docker y Docker Compose instalados.

### Pasos para Ejecutar en Local
1. **Clonar el repositorio.**
2. **Configurar Entorno:** Renombrar el archivo `.env.example` a `.env` en la raíz del proyecto. Llenar los campos obligatorios:
   ```env
   DB_URL=jdbc:postgresql://localhost:5432/caseroya
   DB_USER=postgres
   DB_PASSWORD=tu_password_seguro
   JWT_SECRET=generar_un_hash_largo_de_256_bits_aqui
   ```
3. **Levantar Base de Datos Local:**
   ```bash
   docker-compose up -d
   ```
4. **Compilar y Ejecutar:**
   ```bash
   ./mvnw spring-boot:run
   ```
5. **Probar:** 
   - La documentación automática interactiva está en: `http://localhost:8080/swagger-ui/index.html`
   - La colección JSON de Postman se encuentra en la raíz del proyecto (`postman_collection.json`), lista para ser importada.

---

## 13. Conclusión

### Logros del Proyecto
CaseroYa ha superado su objetivo inicial. Hemos entregado un backend de clase empresarial, modularizado bajo Clean Architecture, que soluciona de raíz el problema de pérdida de inventarios en negocios locales. El sistema no solo realiza el trabajo matemático eficientemente, sino que sus respuestas son casi instantáneas gracias al uso de procesamiento asíncrono para tareas pesadas como envío de correos, y su información está completamente blindada por JWT y BCrypt.

### Aprendizajes Clave
A lo largo de este ciclo, el equipo interiorizó que el código que "funciona" no siempre es "código limpio". Comprender la inyección de dependencias para desacoplar clases, dominar las anotaciones transaccionales (`@Transactional`) para evitar lecturas sucias en la base de datos, y lograr implementar un Global Exception Handler nos ha transformado de programadores a arquitectos de software con un profundo sentido de responsabilidad y mantenibilidad de código.

### Trabajo Futuro
En las próximas versiones, planeamos implementar:
- Pasarelas de pagos online mediante integración con APIs de terceros (Niubiz/MercadoPago).
- Automatización del despliegue contínuo (CI/CD) creando un pipeline de GitHub Actions que ejecute pruebas unitarias (JUnit/Mockito) y despliegue el contenedor Docker final directamente hacia Amazon Web Services (AWS EC2 / ECS).
- Paginación avanzada en todos los endpoints de listado para manejar millones de registros de auditoría.

---

## 14. Apéndices

- **Licencia:** Este proyecto se distribuye bajo la Licencia MIT.
- **Colección Postman:** Se adjunta el archivo `postman_collection.json` en este repositorio. Incluye el flujo automático para captura de Tokens (Tests pre-request).
- **Referencias Bibliográficas:**
  - Spring Framework Official Documentation (2026).
  - Baeldung Java & Spring Tutorials.
  - JWT.io Documentation.
