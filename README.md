# CaseroYa - Backend (Spring Boot)

Este es el repositorio central del backend de **CaseroYa**. El proyecto está estructurado usando una arquitectura en capas (Controller -> Service -> Repository -> Entity) y se conecta a una base de datos PostgreSQL alojada en Supabase.

---

## 🚀 Cómo empezar (Para todos los integrantes)

1. **Clonar el proyecto:**
   ```bash
   git clone <URL_DEL_REPOSITORIO>
   cd caseroya
   ```

2. **Configurar las credenciales (¡Muy importante!):**
   Nunca subas contraseñas a GitHub. Debes crear un archivo llamado `.env` en la raíz del proyecto (a la misma altura que el `pom.xml`) y pedirle al líder técnico que te pase el contenido. Debería verse así:
   ```env
   DB_URL=jdbc:postgresql://aws-0-us-west-2.pooler.supabase.com:5432/postgres
   DB_USER=postgres.poyaixyvrmfqkcubrrxj
   DB_PASSWORD=LA_CONTRASEÑA_REAL
   ```

3. **Ejecutar el proyecto localmente:**
   Dependiendo de tu IDE, asegúrate de inyectar las variables de entorno. 
   - Desde consola (Linux/Mac): `set -a; source .env; set +a; ./mvnw spring-boot:run`
   - En IntelliJ: Instalar plugin *EnvFile* o poner las variables en "Environment variables".

---

## 🛠 Arquitectura y Plantilla a seguir (Por Módulos / DDD)

Hemos cambiado a una **arquitectura agrupada por módulos (Feature-based / Hexagonal ligera)**. Esto significa que los archivos no se separan en grandes carpetas genéricas (todos los controllers juntos), sino que cada "módulo" (ej. `perfil`, `auth`, `ventas`) tiene su propio mundo por dentro.

Si tienes a cargo la tabla `Perfil`, tu estructura debe verse exactamente así:
`src/main/java/com/proyect/caseroya/perfil/`
1. `domain/Perfil.java` (Tu entidad / modelo)
2. `domain/PerfilService.java` (Lógica de negocio)
3. `infrastructure/PerfilRepository.java` (Comunicación con BD)
4. `application/PerfilController.java` (Endpoints de la API)
5. `dto/` (Para tus objetos de petición/respuesta, ej. `PerfilRequestDto`)

> **💡 TIP:** Ya hemos dejado el CRUD completo de la tabla `Perfil` bajo esta nueva estructura. Úsalo como plantilla (copia su estructura) para crear tus propios módulos (`ventas`, `clientes`, `productos`, etc).

---

## 👥 División del Trabajo (5 Integrantes)

### 👨‍💻 Integrante 1: Módulo de Seguridad y Accesos
* **Tablas / Entidades:** `Usuario`, `OpcionMenu`, `PerfilOpcionMenu` *(Nota: `Perfil` ya se hizo como plantilla)*.
* **Archivos a crear:** Controllers, Services, y Repositories de cada uno. Además, un `SecurityConfig.java`.
* **Endpoints:** 
  - `POST /api/login` (Recibe usuario/password, devuelve JWT).
  - `CRUD /api/usuarios`
  - `CRUD /api/opciones-menu`
* **Excepciones clave a manejar:** `UsuarioNoEncontradoException`, `CredencialesInvalidasException`.
* **Reto Principal:** Configurar **Spring Security** y JWT para que el resto de los módulos exijan que el usuario esté autenticado.

### 👩‍💻 Integrante 2: Módulo de Catálogos (Bases de datos generales)
* **Tablas / Entidades:** `Cliente`, `Proveedor`, `TablaGeneral`.
* **Archivos a crear:** Controllers, Services y Repositories de estas 3 entidades.
* **Endpoints:**
  - `CRUD /api/clientes`
  - `CRUD /api/proveedores`
  - `CRUD /api/tablas-generales`
* **Excepciones clave a manejar:** `DocumentoDuplicadoException` (Si alguien intenta registrar el mismo RUC/DNI), `RecursoNoEncontradoException`.
* **Reto Principal:** Tus servicios serán consumidos por Compras y Ventas (ej. verificar que un cliente existe antes de venderle).

### 👨‍💻 Integrante 3: Módulo de Inventario (Almacén)
* **Tablas / Entidades:** `Producto`, `ControlStock`.
* **Archivos a crear:** Controllers, Services y Repositories correspondientes.
* **Endpoints:**
  - `CRUD /api/productos`
  - `GET /api/stock` y `GET /api/stock/{idProducto}`
* **Excepciones clave a manejar:** `StockInsuficienteException` (muy importante para el Integrante 4).
* **Reto Principal:** Debes crear un método público en tu `StockService` (ej: `public void actualizarStock(Integer idProducto, int cantidad, boolean esIngreso)`) que los integrantes 4 y 5 llamarán desde su código.

### 👩‍💻 Integrante 4: Módulo de Ventas (Salida de dinero y mercadería)
* **Tablas / Entidades:** `DocumentoVenta`, `DetalleVenta`.
* **Archivos a crear:** Service, Repository de ambos. Controller solo de `DocumentoVenta`. DTOs para recibir la cabecera y el detalle en un solo JSON.
* **Endpoints:**
  - `POST /api/ventas` (Guarda cabecera y lista de detalles al mismo tiempo).
  - `GET /api/ventas/{id}`
  - `PUT /api/ventas/{id}/anular`
  - `GET /api/reportes/ventas?desde=X&hasta=Y`
* **Excepciones clave a manejar:** `DocumentoAnuladoException` (Si se intenta editar una venta anulada).
* **Reto Principal:** Asegurar el cálculo matemático correcto (Subtotal, IGV, Total) e inyectar el `StockService` del Integrante 3 para **restar stock** al guardar una venta.

### 👨‍💻 Integrante 5: Módulo de Compras (Ingreso de dinero y mercadería)
* **Tablas / Entidades:** `DocumentoCompra`, `DetalleCompra`.
* **Archivos a crear:** Service, Repository de ambos. Controller solo de `DocumentoCompra`. DTOs para la petición combinada.
* **Endpoints:**
  - `POST /api/compras` (Cabecera + Detalle juntos).
  - `GET /api/compras/{id}`
  - `PUT /api/compras/{id}/anular`
* **Excepciones clave a manejar:** Las mismas lógicas de anulación y validación de matemáticas.
* **Reto Principal:** Similar a ventas, pero inyectando el `StockService` del Integrante 3 para **sumar stock** al guardar una compra.

---

## 🔀 Flujo de Trabajo en Git (Git Flow Básico)

**¡Regla de oro: NADIE hace commits directamente a la rama `main`!**

1. **Antes de empezar tu día, actualiza tu código:**
   ```bash
   git checkout main
   git pull origin main
   ```

2. **Crea una rama para lo que vas a programar:**
   *(Usa tu nombre o el nombre de tu módulo)*
   ```bash
   git checkout -b feature/modulo-ventas
   ```

3. **Programa tu código, haz tus pruebas locales y cuando funcione, haz commit:**
   ```bash
   git add .
   git commit -m "Agregada la entidad y controlador de Cliente"
   ```

4. **Sube tu rama a GitHub:**
   ```bash
   git push origin feature/modulo-ventas
   ```

5. **Revisión y Unión (Pull Request):**
   - Entra a GitHub.com, busca el botón **"Compare & pull request"**.
   - Crea el Pull Request (PR).
   - Avísale a tu líder técnico o compañeros para que lo revisen y le den "Merge" hacia `main`.
   - Una vez unido a `main`, todos los demás deben hacer `git pull origin main` para tener tu código.
