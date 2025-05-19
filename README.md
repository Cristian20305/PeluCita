# 📱 PeluCita – App de Gestión de Citas para Peluquerías

## Índice
1. [Especificación de la aplicación](#especificación-de-la-aplicación)
   - [Definición de la aplicación](#definición-de-la-aplicación)
   - [Catálogo de requisitos](#catálogo-de-requisitos)
2. [Desarrollo de la aplicación](#desarrollo-de-la-aplicación)
   - [Mapa de navegación](#mapa-de-navegación)
   - [Interfaz gráfica](#interfaz-gráfica)
   - [Almacenamiento de la información](#almacenamiento-de-la-información)
3. [Construcción de la aplicación](#construcción-de-la-aplicación)
   - [Tecnologías seleccionadas](#tecnologías-seleccionadas)
   - [Herramientas utilizadas](#herramientas-utilizadas)
4. [Implantación o publicación](#implantación-o-publicación)
   - [Recursos físicos y lógicos](#recursos-físicos-y-lógicos)
   - [Procedimiento de implantación](#procedimiento-de-implantación)

---

## 1. Especificación de la aplicación

### Definición de la aplicación
**PeluCita** es una app móvil Android desarrollada en **Kotlin** con **Jetpack Compose** que permite la gestión integral de citas en peluquerías. Está dirigida a pequeños y medianos salones que buscan digitalizar sus procesos de reserva.

### Perfiles de usuario:
- **Cliente**: Puede consultar disponibilidad, reservar, modificar o cancelar citas y acceder a su historial.
- **Administrador (peluquero/a)**: Gestiona la agenda, añade/modifica/elimina turnos y consulta información de los clientes.

---

### Catálogo de requisitos

#### Requisitos Funcionales
- RF01: Registro e inicio de sesión con validación de credenciales locales.
- RF02: Visualización de disponibilidad horaria gestionada por el admin.
- RF03: Reserva de citas con selección de día, hora, servicio y peluquero.
- RF04: Cancelación de citas activas y liberación del horario.
- RF05: Consulta del historial de citas por parte del cliente.
- RF06: Panel exclusivo para el administrador con gestión total de citas.
- RF07: Configuración de horarios y bloqueo de días.
- RF08: Visualización de calendario y detalles de citas programadas.
- RF09: Recordatorios automáticos mediante notificaciones locales y posibilidad de añadir cita al calendario.

#### Requisitos No Funcionales
- Interfaz moderna y accesible con Jetpack Compose.
- Funcionamiento 100% offline con SQLite.
- Notificaciones locales mediante `WorkManager` y `NotificationManager`.
- Buen rendimiento en dispositivos Android de gama baja.

---

## 2. Desarrollo de la aplicación

### Mapa de navegación
- **Cliente**: Login → HomeCliente → Nueva Cita / Historial / Detalle de Cita.
- **Administrador**: Login → HomeAdmin → Calendario → Detalle de Cita → Crear / Editar / Filtrar citas.

---

### Interfaz gráfica

#### Pantallas principales:
- **Login/Registro**: Formularios con validación, mensajes de error y navegación fluida.
- **Calendario (Admin)**: Vista mensual con días destacados por cita. Al seleccionar un día, se muestran los detalles.
- **Nueva Cita**: DatePicker + horarios en bloques de 30 minutos (mañana/tarde), selector de servicio y peluquero, alert dialog para añadir al calendario.
- **Historial (Cliente)**: Vista en `LazyVerticalGrid` con tarjetas diferenciando citas futuras y pasadas.
- **AdminHomeScreen**: Gestión completa de citas, filtrado por día, búsqueda rápida, edición mediante diálogos modales, y selección de clientes existentes.

---

### Almacenamiento de la información

La app PeluCita gestiona localmente todos los datos mediante una base de datos **SQLite** sin utilizar Room, lo que permite mayor control sobre las operaciones SQL y un rendimiento más optimizado.

#### Base de datos: SQLite local gestionada manualmente (con `SQLiteOpenHelper`)

---

#### Estructura de las tablas

##### Tabla: `Usuario`
| Campo     | Tipo     | Descripción                         |
|-----------|----------|-------------------------------------|
| id        | INTEGER  | PRIMARY KEY AUTOINCREMENT           |
| nombre    | TEXT     | Nombre del usuario                  |
| email     | TEXT     | Email único (clave de inicio)       |
| contraseña| TEXT     | Contraseña del usuario              |
| tipo      | TEXT     | Rol del usuario: `cliente` o `admin`|

##### Tabla: `Cita`
| Campo       | Tipo     | Descripción                                      |
|-------------|----------|--------------------------------------------------|
| id          | INTEGER  | PRIMARY KEY AUTOINCREMENT                        |
| clienteId   | INTEGER  | FOREIGN KEY → Usuario(id)                        |
| fecha       | TEXT     | Fecha de la cita (formato `YYYY-MM-DD`)          |
| hora        | TEXT     | Hora de la cita (formato `HH:MM`)                |
| servicio    | TEXT     | Nombre del servicio reservado                    |
| peluquero   | TEXT     | Nombre del peluquero asignado (opcional)         |

##### Tabla: `Peluquero`
| Campo     | Tipo     | Descripción                         |
|-----------|----------|-------------------------------------|
| id        | INTEGER  | PRIMARY KEY AUTOINCREMENT           |
| nombre    | TEXT     | Nombre del peluquero/a              |

##### Tabla: `Servicio`
| Campo     | Tipo     | Descripción                         |
|-----------|----------|-------------------------------------|
| id        | INTEGER  | PRIMARY KEY AUTOINCREMENT           |
| nombre    | TEXT     | Nombre del servicio ofrecido        |

---

#### 🛠️ Gestión técnica
- `onCreate()`: Crea todas las tablas y carga datos por defecto (admin, servicios, peluqueros).
- `onUpgrade()`: Permite modificar la estructura manteniendo datos al actualizar versión.
- Consultas: Se usan sentencias manuales SQL (`INSERT`, `UPDATE`, `SELECT`, `DELETE`).

---

#### 🕒 Disponibilidad horaria

La disponibilidad **no se almacena en la base de datos**. Se define directamente en la lógica de la app:

- **Turno de mañana**: 10:00 – 14:00
- **Turno de tarde**: 16:30 – 20:00

Estas franjas se dividen en bloques de 30 minutos. Las ya reservadas se deshabilitan automáticamente.

---


---

## 3. Construcción de la aplicación

### Tecnologías seleccionadas
- **Sistema Operativo**: Android
- **Lenguaje**: Kotlin
- **Framework UI**: Jetpack Compose
- **Base de Datos**: SQLite manual (SQLiteOpenHelper)
- **Notificaciones locales**: WorkManager + NotificationManager
- **Control de versiones**: Git + GitHub

### Herramientas utilizadas
- IDE: Android Studio
- Modelado DB: dbdiagram.io
- Diseño UI/UX: Draw.io / Figma / Canva
- Prototipado y pruebas: Emulador Android + dispositivos reales

---

## 4. Implantación o publicación

### Recursos físicos y lógicos

#### Físicos:
- Dispositivos Android para pruebas.
- PC con Android Studio, SDK y emuladores.

#### Lógicos:
- SQLite local sin hosting externo.
- Firebase (planificado a futuro).
- Cuenta Google Play Developer (opcional para publicación).

---

### Procedimiento de implantación

1. **Compilación y prueba en emulador y móviles reales.**
2. **Generación de APK firmado digitalmente.**
3. **Publicación en Google Play (opcional):**
   - Ficha de app, capturas, política de privacidad.
4. **Distribución interna:**
   - Compartir por enlace o QR.
5. **Guía de instalación y requisitos mínimos.**
6. **Pruebas finales de funcionamiento.**

---

> 💬 PeluCita es una app ágil, intuitiva y funcional para la gestión de citas, pensada para seguir creciendo con futuras integraciones como bases de datos online o notificaciones push avanzadas.
