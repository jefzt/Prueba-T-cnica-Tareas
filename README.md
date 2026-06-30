# Gestión de Tareas - API REST

API REST para la gestión de tareas diarias, desarrollada con Spring Boot 3, Spring Data JPA y MySQL.

## Stack utilizado

- Java 21
- Spring Boot 3.2.0
- Spring Data JPA + Hibernate
- MySQL 8.x
- Maven
- Lombok

## Funcionalidad adicional implementada (Nivel 2)

**Opción B - Estadísticas**: endpoint `GET /api/tareas/estadisticas` que retorna el conteo de tareas agrupadas por estado y el total general.

## Estructura del proyecto

```
src/main/java/com/tareas/gestion/
├── controller/      → endpoints REST
├── service/         → lógica de negocio
├── repository/      → acceso a datos (Spring Data JPA)
├── model/           → entidad Tarea y enums
├── dto/             → objetos de transferencia de datos
└── exception/       → manejo centralizado de errores
```

## Configuración de la base de datos

### 1. Crear la base de datos (opcional)

La aplicación crea automáticamente la base de datos gracias a `createDatabaseIfNotExist=true`. Si prefieres crearla manualmente:

```sql
CREATE DATABASE gestion_tareas_db
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;
```

### 2. Configurar credenciales

Edita `src/main/resources/application.properties` y reemplaza `TU_PASSWORD` con tu contraseña real de MySQL:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/gestion_tareas_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=TU_PASSWORD
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.sql.init.mode=always
spring.jpa.defer-datasource-initialization=true

server.port=8080
```

> ⚠️ Las credenciales de ejemplo aquí son `root` / `TU_PASSWORD`. Ajusta según tu instalación local de MySQL.

### 3. Datos de prueba

El archivo `src/main/resources/data.sql` inserta automáticamente 7 tareas de prueba al iniciar la aplicación (gracias a `spring.sql.init.mode=always`). El script también está disponible de forma independiente en `script-datos-prueba.sql` en la raíz del proyecto.

## Cómo ejecutar el proyecto

```bash
mvn clean spring-boot:run
```

La aplicación quedará disponible en `http://localhost:8080`.

## Endpoints disponibles

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/api/tareas` | Listar todas las tareas |
| GET | `/api/tareas/{id}` | Obtener una tarea por id |
| POST | `/api/tareas` | Crear una nueva tarea |
| PUT | `/api/tareas/{id}` | Actualizar una tarea completa |
| PATCH | `/api/tareas/{id}/estado` | Cambiar solo el estado de una tarea |
| DELETE | `/api/tareas/{id}` | Eliminar una tarea |
| GET | `/api/tareas/filtrar/estado?estado=PENDIENTE` | Filtrar tareas por estado |
| GET | `/api/tareas/filtrar/prioridad?prioridad=ALTA` | Filtrar tareas por prioridad |
| GET | `/api/tareas/buscar?q=texto` | Buscar tareas por título |
| GET | `/api/tareas/estadisticas` | Obtener estadísticas (Nivel 2) |

## Formato de respuesta

Todos los endpoints retornan una respuesta envuelta en:

```json
{
  "success": true,
  "mensaje": "Tarea creada exitosamente",
  "data": { ... }
}
```

## Reglas de negocio implementadas

- `fechaCreacion` se asigna una única vez al persistir la entidad (`@PrePersist`).
- `fechaActualizacion` se actualiza en cada modificación (`@PreUpdate`).
- Una tarea en estado `CANCELADA` no puede cambiar de estado.
- Una tarea en estado `COMPLETADA` no puede volver a `PENDIENTE` ni a `EN_PROGRESO`.
- El título no puede estar vacío ni tener menos de 3 caracteres.

## Ejemplo de creación de tarea (POST)

```json
POST /api/tareas
Content-Type: application/json

{
  "titulo": "Preparar presentación",
  "descripcion": "Presentación para el cliente del viernes",
  "prioridad": "ALTA"
}
```

## Ejemplo de cambio de estado (PATCH)

```json
PATCH /api/tareas/1/estado
Content-Type: application/json

{
  "estado": "EN_PROGRESO"
}
```

## Documento RESPUESTAS.md

Las respuestas a las 5 preguntas teóricas del Nivel 3 se encuentran en el archivo `RESPUESTAS.md` 
