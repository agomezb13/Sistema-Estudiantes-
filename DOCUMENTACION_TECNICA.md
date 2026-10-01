# DOCUMENTACIÓN TÉCNICA DEL SISTEMA DE GESTIÓN DE VENTAS

## 1. Resumen General del Proyecto

El proyecto **Sistema de Gestión de Ventas** es una aplicación de escritorio desarrollada en Java utilizando la biblioteca gráfica Swing. Su arquitectura y diseño de interfaz gráfica se basan en el proyecto de referencia `sistema_ventas`, adoptando una interfaz de múltiples documentos (MDI con `JDesktopPane`) y una separación ordenada entre pantallas de consulta/búsqueda (`FrmFiltro...`) y pantallas de captura/edición de datos (`FrmTecleo...`).

El diseño del modelo de dominio responde directamente al **Diagrama de Clases UML** y al **Diagrama Entidad-Relación** del sistema, implementando una arquitectura multicapa desacoplada (Modelo - DAO - Controlador - Vista - Configuración). La aplicación incluye un flujo de navegación completo con **Menú de Inicio** (`FrmInicio`) y **Menú Final** (`FrmFinal`), además de estar preparada para persistencia en base de datos MySQL con respaldo en memoria.

---

## 2. Arquitectura de Software

La aplicación sigue una arquitectura en capas fundamentada en buenas prácticas de programación orientada a objetos:

1. **Capa de Modelo (`modelo`)**: Define los objetos de negocio, sus atributos, métodos operativos, encapsulamiento (getters y setters) y constructores.
2. **Capa de Acceso a Datos (`dao`)**: Define interfaces que declaran las operaciones CRUD y clases concretas de implementación que gestionan el acceso y las consultas.
3. **Capa de Datos y Conexión (`conexion` y `datos`)**:
   - `ConexionMySQL`: Administrador de la conexión JDBC contra MySQL.
   - `BaseDatosMemoria`: Repositorio local con colecciones y datos pregrabados que garantiza la operación continua del sistema incluso cuando el motor MySQL no esté activo.
4. **Capa de Controladores (`controlador`)**: Contiene las reglas de negocio, validaciones y orquesta la comunicación entre la vista y la capa de acceso a datos.
5. **Capa de Configuración (`config`)**: `ContenedorAplicacion` centraliza la instanciación de controladores y DAOs facilitando la inyección de dependencias simple.
6. **Capa de Presentación / Vista (`vista`)**: Interfaz gráfica en Swing basada en un escritorio MDI (`JDesktopPane`), con menús de inicio, menús finales y formularios desacoplados de filtro y tecleo.

---

## 3. Modelo de Datos y Entidades (UML & ERD)

A continuación se describen las entidades que conforman el núcleo de ventas e inventario:

| Entidad | Tabla BD | Descripción |
| :--- | :--- | :--- |
| **Cliente** | `cliente` | Persona o entidad que adquiere productos. Incluye DPI, NIT, teléfono, dirección y estado. |
| **DireccionEnvio** | `direccion_envio` | Ubicación geográfica asociada a un cliente para entregas. |
| **Vendedor** | `vendedor` | Personal de ventas con código de empleado, departamento y contacto. |
| **Categoria** | `categoria` | Clasificación organizativa de productos del catálogo. |
| **Producto** | `producto` | Bien comercializable con precio de venta, SKU y existencias. |
| **Inventario** | `inventario` | Registro físico del stock por producto y ubicación física en almacén. |
| **Carrito** | `carrito` | Estructura temporal que agrupa selecciones de compra por cliente. |
| **ElementoCarrito** | `elemento_carrito` | Ítem individual agregado al carrito con cantidad y precio. |
| **Pedido** | `pedido` | Orden de compra confirmada con cliente, dirección, fecha y total. |
| **DetallePedido** | `detalle_pedido` | Líneas que componen el pedido (producto, cantidad, precio, subtotal). |
| **Pago** | `pago` | Transacción financiera ligada a un pedido (efectivo, tarjeta, etc.). |
| **Factura** | `factura` | Documento legal contable con numeración, cliente, vendedor, IVA (12%) y total. |
| **DetalleFactura** | `detalle_factura` | Desglose individual de productos facturados. |

---

## 4. Catálogo Detallado de Archivos del Proyecto

### 4.1. Paquete Raíz (`gt.edu.umg.sistema.estudiantes`)

- **`SistemaEstudiantes.java`**: Punto de entrada de la aplicación (`main`). Aplica el Look & Feel de Swing ("Nimbus"), instancia el `ContenedorAplicacion` y lanza el menú de inicio (`FrmInicio`).

### 4.2. Paquete de Conexión (`gt.edu.umg.sistema.estudiantes.conexion`)

- **`ConexionMySQL.java`**: Gestiona la conexión JDBC contra la base de datos `sistema_ventas`. Contiene métodos para probar conexión (`probarConexion()`), reconfigurar parámetros en tiempo de ejecución (`configurar()`) y obtener la conexión activa (`getConnection()`).

### 4.3. Paquete de Configuración (`gt.edu.umg.sistema.estudiantes.config`)

- **`ContenedorAplicacion.java`**: Contenedor de servicios e inyección de dependencias. Instancia los DAOs y los controladores, exponiéndolos mediante métodos get para que las vistas puedan consumirlos de forma ordenada.

### 4.4. Paquete de Almacenamiento Local (`gt.edu.umg.sistema.estudiantes.datos`)

- **`BaseDatosMemoria.java`**: Repositorio en memoria concurrente y sincronizado que contiene listas y secuencias auto-incrementables para cada entidad. Incluye datos de prueba iniciales (clientes, vendedores, categorías, productos, pedidos, facturas) para permitir el uso y pruebas de la interfaz sin requerir la base de datos MySQL iniciada.

### 4.5. Paquete de Modelos (`gt.edu.umg.sistema.estudiantes.modelo`)

- **`Cliente.java`**: Entidad de clientes con atributos `id`, `nombre`, `correo`, `telefono`, `direccion`, `numeroDPI`, `NIT` y `estado`.
- **`DireccionEnvio.java`**: Entidad de direcciones de entrega con `id`, `clienteId`, `calle`, `ciudad`, `codigoPostal` y `pais`.
- **`Vendedor.java`**: Entidad de vendedores con `id`, `nombre`, `codigoEmpleado`, `departamento`, `telefono`, `correo` y `estado`.
- **`Categoria.java`**: Entidad de categorías de producto con `id`, `nombre` y `descripcion`.
- **`Producto.java`**: Entidad de productos con `id`, `categoriaId`, `nombre`, `descripcion`, `precio`, `stock` y `sku`.
- **`Inventario.java`**: Entidad de inventario con `id`, `productoId`, `cantidadDisponible` y `ubicacion`. Incluye métodos operativos `actualizarStock()` y `consultarDisponibilidad()`.
- **`Carrito.java`**: Entidad del carrito de compra con `id`, `clienteId`, `fechaCreacion` y lista de `ElementoCarrito`.
- **`ElementoCarrito.java`**: Detalle del carrito con `id`, `carritoId`, `productoId`, `cantidad` y `precioUnitario`.
- **`Pedido.java`**: Entidad de orden de compra con `id`, `clienteId`, `direccionEnvioId`, `fecha`, `estado`, `total` y lista de `DetallePedido`. Incluye métodos `confirmar()`, `cancelar()` y `calcularTotal()`.
- **`DetallePedido.java`**: Línea de producto del pedido con `id`, `pedidoId`, `productoId`, `cantidad`, `precio` y `subtotal`.
- **`Pago.java`**: Registro de pago con `id`, `pedidoId`, `monto`, `metodo` y `estado`. Incluye métodos `procesar()` y `reembolsar()`.
- **`Factura.java`**: Documento de facturación con `id`, `pedidoId`, `clienteId`, `vendedorId`, `numero`, `fechaEmision`, `subtotal`, `impuesto` (IVA 12%), `total`, `estado` y detalles.
- **`DetalleFactura.java`**: Línea de producto facturado con cálculo de subtotal.
- **`Persona.java`**: Clase base para entidades con información personal (compatibilidad).
- **`Empleado.java`**: Subclase de persona para empleados generales (compatibilidad).
- **`Estudiante.java`**: Entidad de estudiantes (compatibilidad con la versión original del proyecto).

### 4.6. Paquete de Acceso a Datos (`gt.edu.umg.sistema.estudiantes.dao`)

- **`ClienteDAO.java`**: Interfaz de operaciones de persistencia para clientes.
- **`ClienteDAOImpl.java`**: Implementación híbrida de acceso a datos para clientes (MySQL con soporte en memoria).
- **`CategoriaDAO.java`**: Interfaz de operaciones para categorías.
- **`CategoriaDAOImpl.java`**: Implementación de acceso a datos para categorías.
- **`ProductoDAO.java`**: Interfaz de operaciones para productos y control de stock.
- **`ProductoDAOImpl.java`**: Implementación de acceso a datos para productos.
- **`VendedorDAO.java`**: Interfaz de operaciones para vendedores.
- **`VendedorDAOImpl.java`**: Implementación de persistencia para vendedores.
- **`DireccionEnvioDAO.java`**: Interfaz para administración de direcciones de entrega.
- **`DireccionEnvioDAOImpl.java`**: Implementación de persistencia para direcciones de entrega.
- **`PedidoDAO.java`**: Interfaz para administración de pedidos y detalles de pedido.
- **`PedidoDAOImpl.java`**: Implementación de persistencia para pedidos.
- **`PagoDAO.java`**: Interfaz para registro y consulta de pagos.
- **`PagoDAOImpl.java`**: Implementación de persistencia para pagos.
- **`FacturaDAO.java`**: Interfaz para emisión, consulta y anulación de facturas.
- **`FacturaDAOImpl.java`**: Implementación de persistencia para facturas (soporta tanto el nuevo modelo como métodos legacy).
- **`EstudianteDAO.java`**: Interfaz DAO para estudiantes.
- **`EstudianteDAOImpl.java`**: Implementación DAO para estudiantes.

### 4.7. Paquete de Controladores (`gt.edu.umg.sistema.estudiantes.controlador`)

- **`ClienteController.java`**: Lógica de validación y control para clientes.
- **`CategoriaController.java`**: Gestión y validaciones de categorías de productos.
- **`ProductoController.java`**: Control de catálogo de productos y precios.
- **`VendedorController.java`**: Control de vendedores y validación de código de empleado.
- **`DireccionEnvioController.java`**: Gestión de direcciones asociadas a clientes.
- **`PedidoController.java`**: Reglas de negocio para creación, cálculo y consulta de pedidos.
- **`PagoController.java`**: Gestión de pagos y validación de montos.
- **`FacturaController.java`**: Emisión, cálculo de IVA (12%) y anulación de facturas.
- **`InventarioController.java`**: Lógica de consulta de existencias y actualización de stock.
- **`EstudianteController.java`**: Controlador para la pantalla de estudiantes existente.

### 4.8. Paquete de Vistas (`gt.edu.umg.sistema.estudiantes.vista`)

- **`FrmInicio.java`**: **Menú de inicio** de la aplicación. Muestra el título del sistema, estado del servicio de base de datos MySQL con botón para probar o reconfigurar la conexión, botón de acceso al sistema MDI y acceso directo al menú final.
- **`FrmPrincipal.java`**: Ventana principal MDI (`JFrame` con `JDesktopPane`). Organiza todas las opciones del sistema en menús superiores:
  - *Catálogos*: Clientes, Categorías, Productos, Vendedores, Gestión Estudiantes.
  - *Ventas*: Pedidos, Registro de Pagos.
  - *Inventario*: Control de Inventario y Stock.
  - *Facturación*: Facturas.
  - *Sistema*: Menú de Inicio, Menú Final / Cierre de Sesión, Salir.
- **`FrmFinal.java`**: **Menú final** y cierre de sesión. Muestra un resumen cuantitativo de la sesión (total de clientes, productos, pedidos y facturas) y botones para volver al inicio, regresar al sistema de ventas o salir definitivamente.
- **`FormularioHelper.java`**: Clase utilitaria para estandarizar la creación de interfaces Swing (posicionamiento en `GridBagLayout`, modelos de tabla de solo lectura, llenado seguro de comboboxes y apertura centrada en el escritorio MDI).
- **`FrmFiltroBase.java`**: Clase abstracta base para formularios de consulta interna (`JInternalFrame`). Proporciona tabla paginable/ordenable, campos de filtro y botones estándar (Buscar, Limpiar, Nuevo, Editar, Eliminar).
- **`FrmFiltroCliente.java`**: Formulario de búsqueda de clientes por nombre, DPI o NIT.
- **`FrmTecleoCliente.java`**: Formulario de captura y modificación de clientes.
- **`FrmFiltroCategoria.java`**: Formulario de consulta de categorías.
- **`FrmTecleoCategoria.java`**: Formulario de edición y registro de categorías.
- **`FrmFiltroProducto.java`**: Formulario de búsqueda de productos por nombre y categoría.
- **`FrmTecleoProducto.java`**: Formulario de registro y edición de productos.
- **`FrmFiltroVendedor.java`**: Formulario de consulta y filtro de vendedores.
- **`FrmTecleoVendedor.java`**: Formulario de captura de vendedores.
- **`FrmFiltroPedido.java`**: Formulario de consulta de pedidos con filtros por cliente y estado.
- **`FrmTecleoPedido.java`**: Formulario de captura de pedidos con selección de cliente, dirección de entrega, tabla dinámica de productos agregados y cálculo automático del total.
- **`FrmFiltroFactura.java`**: Formulario de consulta y búsqueda de facturas por correlativo y cliente.
- **`FrmTecleoFactura.java`**: Formulario de emisión de facturas con selección de cliente, vendedor, pedido relacionado, detalle de productos, subtotal, IVA (12%) y total, con soporte de anulación.
- **`FrmFiltroPago.java`**: Formulario de consulta de pagos filtrados por pedido y método.
- **`FrmTecleoPago.java`**: Formulario de registro y procesamiento de pagos asociados a pedidos.
- **`FrmInventario.java`**: Formulario especializado de control de existencias, filtro por categoría, búsqueda por texto y actualización directa de stock disponible.
- **`FrmEstudiante.java` / `FrmEstudiante.form`**: Vista original de mantenimiento de estudiantes (mantenida para compatibilidad).
- **`FrmFactura.java` / `FrmFactura.form`**: Formulario simple anterior de facturación (mantenido para compatibilidad).
- **`FrmMenuPrincipal.java`**: Formulario de navegación alternativo de módulos.

### 4.9. Archivos de Configuración y Base de Datos en la Raíz

- **`pom.xml`**: Configuración de dependencias Maven del proyecto (incluye el conector oficial `mysql-connector-j` versión `8.0.33`).
- **`schema_sistema_ventas.sql`**: Script DDL completo de creación de base de datos MySQL `sistema_ventas` con las 13 tablas correspondientes al ERD, claves foráneas, restricciones de integridad y registros iniciales de prueba.
- **`DOCUMENTACION_TECNICA.md`**: Este documento técnico.

---

## 5. Instrucciones de Base de Datos y Configuración

### 5.1. Requisitos
- Servidor MySQL 5.7, 8.0 o compatible (por ejemplo, mediante XAMPP, WampServer o MySQL Server nativo).
- Puerto por defecto: `3306`.
- Usuario por defecto: `root` (sin contraseña o con la configurada en su entorno).

### 5.2. Pasos para Instalar la Base de Datos en MySQL
1. Abra su cliente de base de datos preferido (MySQL Workbench, phpMyAdmin, DBeaver o la consola de comandos de MySQL).
2. Abra el archivo `schema_sistema_ventas.sql` ubicado en la raíz del proyecto.
3. Ejecute el script completo.
4. El script creará la base de datos `sistema_ventas` con todas las tablas y cargará los datos de prueba iniciales.

### 5.3. Configuración en la Aplicación
Al ejecutar la aplicación, el **Menú de Inicio** (`FrmInicio`) informará si la conexión con MySQL se ha establecido correctamente. Si el servidor MySQL utiliza un puerto o contraseña diferente, puede presionar el botón **"Configurar BD"** en la pantalla de inicio e introducir los parámetros correspondientes en tiempo de ejecución.

---

## 6. Instrucciones de Compilación y Ejecución

### 6.1. Desde Consola con Maven
Para compilar el proyecto:
```powershell
mvn compile
```

Para empaquetar el archivo JAR:
```powershell
mvn package
```

Para ejecutar la aplicación:
```powershell
mvn exec:java -Dexec.mainClass="gt.edu.umg.sistema.estudiantes.SistemaEstudiantes"
```

### 6.2. Desde Apache NetBeans
1. Abra NetBeans y seleccione **File -> Open Project**.
2. Seleccione la carpeta del proyecto `Sistema-Estudiantes-`.
3. Haga clic derecho sobre el proyecto y seleccione **Build**.
4. Presione **F6** o haga clic derecho y seleccione **Run** para iniciar el sistema.
