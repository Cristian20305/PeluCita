# 📱 PeluCita – App de Gestión de Citas para Peluquerías

## Índice

1. **Especificación de la aplicación**
   - 1.1. Definición de la aplicación
   - 1.2. Catálogo de requisitos
     - 1.2.1. Requisitos Funcionales
     - 1.2.2. Requisitos No Funcionales
2. **Desarrollo de la aplicación**
   - 2.1. Mapa de navegación
   - 2.2. Interfaz gráfica
   - 2.3. Almacenamiento de la información
3. **Construcción de la aplicación**
   - 3.1. Tecnologías seleccionadas
   - 3.2. Herramientas utilizadas
4. **Implantación o publicación**
   - 4.1. Recursos físicos y lógicos
   - 4.2. Procedimiento de implantación

---

## 1. Especificación de la aplicación

### 1.1. Definición de la aplicación

**PeluCita** es una aplicación móvil desarrollada en **Kotlin** utilizando **Jetpack Compose** para la interfaz de usuario. Su propósito es ofrecer una solución eficiente y moderna para la gestión de citas en peluquerías, permitiendo automatizar procesos como la reserva, modificación o cancelación de turnos, y el manejo de agendas tanto para clientes como para personal.

La aplicación está dirigida a pequeños y medianos salones que buscan digitalizar su sistema de reservas con una interfaz optimizada para Android.

**Perfiles principales:**

- **Cliente:**  
  Consultar disponibilidad, reservar, modificar/cancelar citas, y acceder a su historial.
  
- **Administrador (peluquero/a):**  
  Gestionar toda la agenda: añadir, editar, eliminar turnos y consultar datos de clientes.

### 1.2. Catálogo de requisitos

#### 1.2.1. Requisitos Funcionales

- **RF01:** Registro e inicio de sesión con validación local.
- **RF02:** Visualización de disponibilidad horaria gestionada por el administrador.
- **RF03:** Reserva de citas por parte del cliente.
- **RF04:** Cancelación de citas activas.
- **RF05:** Consulta de historial de citas por parte del cliente.
- **RF06:** Panel exclusivo para la gestión completa de la agenda (admin).
- **RF07:** Configuración de horarios de atención.
- **RF08:** Visualización global de citas programadas.

#### 1.2.2. Requisitos No Funcionales

- Interfaz moderna y accesible con Jetpack Compose.
- Persistencia local de datos mediante **SQLite**.
- Posible integración futura de notificaciones push.
- Rendimiento óptimo incluso en dispositivos Android de gama baja.

---

## 2. Desarrollo de la aplicación

### 2.1. Mapa de navegación

La aplicación se divide en dos grandes flujos:
- **Cliente:** Login → Home → Nueva Cita / Historial / Detalle de Cita.
- **Administrador:** Login → Home → Calendario de Citas → Detalle de Cita.

### 2.2. Interfaz gráfica

Se ha priorizado la experiencia de usuario (UX) y la accesibilidad.

**Pantallas principales:**

- **Login y Registro:** Formulario con validaciones claras y navegación fluida.
- **Calendario (Admin):** Vista mensual con citas marcadas por día; permite filtrar y consultar detalles.
- **Reserva de Cita:** DatePicker + selector de horario en bloques de 30 min (mañana/tarde).
- **Historial (Cliente):** Lista cronológica de citas, diferenciando próximas y pasadas.
- **Panel de Administrador:** Listado dinámico de citas, búsqueda rápida y edición desde diálogos modales.

> *Prototipos realizados con Balsamiq, Figma o Canva.*

### 2.3. Almacenamiento de la información

**Base de datos:** SQLite local gestionada manualmente (sin Room).

**Estructura:**

| Usuario             | Tipo      | Descripción                                |
|---------------------|-----------|--------------------------------------------|
| id                  | INTEGER   | PK AUTOINCREMENT                           |
| nombre              | TEXT      | Nombre del usuario                         |
| email               | TEXT      | Email único                                |
| contraseña          | TEXT      | Contraseña                                 |
| tipo                | TEXT      | 'cliente' o 'admin'                        |

| Cita                | Tipo      | Descripción                                |
|---------------------|-----------|--------------------------------------------|
| id                  | INTEGER   | PK AUTOINCREMENT                           |
| clienteId           | INTEGER   | FK → Usuario(id)                           |
| fecha               | TEXT      | YYYY-MM-DD                                 |
| hora                | TEXT      | HH:MM                                      |
| motivo              | TEXT      | Descripción del servicio                   |

**Gestión técnica:**

- `onCreate()`: Crea las tablas.
- `onUpgrade()`: Gestiona las migraciones.
- Consultas manuales: `INSERT`, `UPDATE`, `SELECT`, `DELETE`.

**Disponibilidad horaria:**

No almacenada en la base de datos. Se define en la lógica:
- Mañana: 10:00–14:00
- Tarde: 16:30–20:00

---

## 3. Construcción de la aplicación

### 3.1. Tecnologías seleccionadas

- **Sistema operativo:** Android
- **Lenguaje:** Kotlin
- **Framework UI:** Jetpack Compose
- **Base de datos local:** SQLite manual (via `SQLiteOpenHelper`)
- **Control de versiones:** Git + GitHub
- **Prototipado:** Balsamiq / Figma / Canva
- **Notificaciones (futuro):** Firebase Cloud Messaging

### 3.2. Herramientas utilizadas

- **IDE:** Android Studio
- **Editor:** Android Studio con soporte para Compose
- **Control de versiones:** Git + GitHub
- **Modelado DB:** dbdiagram.io
- **Diseño de navegación:** draw.io
- **Testeo:** Emulador + dispositivos físicos

---

## 4. Implantación o publicación

### 4.1. Recursos físicos y lógicos

**Físicos:**
- Dispositivos Android (smartphones/tablets) para pruebas.
- PC/portátil con Android Studio y emuladores instalados.

**Lógicos:**
- SQLite local (sin necesidad de hosting externo).
- Cuenta Google Play (opcional para publicación).
- Firebase (previsto para futuras integraciones).

### 4.2. Procedimiento de implantación

1. **Compilación y pruebas:** Validación en emuladores y dispositivos reales.
2. **Generación APK/AAB:** Firma digital para distribución.
3. **Publicación (opcional):**
   - Configuración de ficha en Google Play.
   - Subida y revisión.
4. **Distribución interna:** Enlace directo o código QR para descarga.
5. **Guía de instalación:** Instrucciones detalladas para usuarios finales.
6. **Pruebas finales:** Verificación de la instalación y funcionalidades en entorno real.

---

💬 *PeluCita es una app ágil, simple y eficaz para la gestión de citas en peluquerías, construida con tecnologías modernas y pensada para seguir creciendo en futuras versiones.*
