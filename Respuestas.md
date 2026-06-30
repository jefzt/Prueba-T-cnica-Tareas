# RESPUESTAS - Nivel 3 (Preguntas teóricas)

## 1. ¿Cuál es la diferencia entre @RestController y @Controller en Spring Boot?

`@Controller` es la anotación base de Spring MVC para marcar una clase como controlador web. Por defecto, los métodos de una clase anotada con `@Controller` retornan el nombre de una vista (por ejemplo, un archivo HTML renderizado con Thymeleaf), no datos directamente. Si se quiere que un método retorne datos en formato JSON o texto plano, hay que agregar manualmente la anotación `@ResponseBody` sobre cada método.

`@RestController` es una anotación de conveniencia que combina `@Controller` y `@ResponseBody`. Esto significa que **todos** los métodos de la clase retornan directamente el objeto serializado (normalmente a JSON), sin necesidad de renderizar ninguna vista. Es la anotación que se usa en el desarrollo de APIs REST, como en este proyecto, donde `TareaController` retorna objetos `ApiResponse<T>` que Spring convierte automáticamente a JSON.

En resumen: `@Controller` se usa para aplicaciones que renderizan vistas (MVC tradicional), mientras que `@RestController` se usa para APIs REST que solo retornan datos.

## 2. ¿Por qué se usan DTOs en lugar de exponer directamente la entidad JPA?

Usar DTOs (Data Transfer Objects) en lugar de exponer las entidades JPA directamente tiene varias ventajas importantes:

- **Desacoplamiento**: la estructura de la base de datos puede cambiar sin afectar el contrato de la API. Si se agrega o renombra una columna en la entidad, el cliente de la API no se ve afectado si el DTO se mantiene estable.
- **Seguridad**: evita exponer campos sensibles o internos de la entidad (como contraseñas, relaciones internas o metadatos de auditoría) que no deberían ser visibles para el cliente.
- **Evitar problemas de serialización**: las entidades JPA suelen tener relaciones (`@OneToMany`, `@ManyToOne`) que, si se serializan directamente, pueden generar bucles infinitos (`LazyInitializationException`) o cargar datos innecesarios.
- **Validaciones específicas por caso de uso**: un DTO de entrada (`TareaRequestDTO`) puede tener validaciones distintas a un DTO de salida (`TareaResponseDTO`), algo que no es práctico de lograr con una sola entidad.
- **Control explícito de lo que se expone**: el DTO actúa como un contrato claro entre el backend y el cliente, mostrando exactamente qué información se envía y se recibe.

En este proyecto, `TareaRequestDTO` valida los datos de entrada (`@NotBlank`, `@Size`) mientras que `TareaResponseDTO` define exactamente qué campos se devuelven al cliente, sin acoplar la API directamente a la entidad `Tarea`.

## 3. ¿Qué ventaja tiene @PrePersist sobre asignar la fecha en el constructor de la entidad?

`@PrePersist` es un callback del ciclo de vida de JPA que se ejecuta automáticamente **justo antes** de que la entidad sea insertada en la base de datos, gestionado directamente por el proveedor de persistencia (Hibernate).

Las ventajas frente a asignar la fecha en el constructor son:

- **Se ejecuta en el momento correcto**: el constructor se invoca al crear el objeto en memoria, que puede no coincidir con el momento real de la persistencia. Si el objeto se crea y se modifica antes de guardarlo, la fecha del constructor podría no reflejar el momento real de creación en la base de datos.
- **Funciona con cualquier mecanismo de creación del objeto**: ya sea que la entidad se cree manualmente con `new`, mediante un framework de mapeo, o por JPA al reconstruir el objeto desde la base de datos, `@PrePersist` siempre se ejecuta en el momento adecuado del ciclo de vida.
- **Centraliza la lógica de auditoría**: mantiene la lógica de "qué pasa antes de guardar" dentro del propio modelo de forma declarativa, sin necesidad de recordar inicializar el campo en cada constructor o método de fábrica.
- **Se complementa naturalmente con @PreUpdate**: en este proyecto, `@PrePersist` asigna `fechaCreacion` y `fechaActualizacion` una sola vez, mientras que `@PreUpdate` actualiza solo `fechaActualizacion` en cada modificación, separando claramente ambas responsabilidades sin duplicar lógica.

## 4. ¿Qué diferencia hay entre spring.jpa.hibernate.ddl-auto=update y ddl-auto=create? ¿Cuál usarías en producción y por qué?

- **`update`**: Hibernate compara el esquema actual de la base de datos con las entidades del proyecto y aplica únicamente los cambios necesarios (agregar columnas o tablas nuevas), sin eliminar datos existentes ni borrar tablas. Es el modo usado en este proyecto durante el desarrollo.
- **`create`**: Hibernate elimina el esquema completo (si existe) y lo vuelve a crear desde cero cada vez que la aplicación arranca. Esto implica que **se pierden todos los datos** almacenados previamente.

**En producción no usaría ninguno de los dos.** La opción recomendada en producción es `spring.jpa.hibernate.ddl-auto=validate` o directamente `none`, combinado con una herramienta de migración de esquema como **Flyway** o **Liquibase**.

La razón es que dejar que Hibernate gestione automáticamente el esquema en producción es riesgoso: un cambio mal interpretado en una entidad podría alterar o eliminar columnas con datos reales sin ningún control de versiones ni posibilidad de rollback. Con Flyway o Liquibase, cada cambio de esquema queda versionado en scripts SQL explícitos, revisables y reversibles, lo cual da control total y trazabilidad sobre la evolución de la base de datos en un ambiente con usuarios reales.

## 5. Si esta API fuera a producción con usuarios reales, menciona al menos 3 cambios que harías en la configuración o arquitectura

1. **Autenticación y autorización con JWT o OAuth2**: actualmente la API no tiene ningún mecanismo de seguridad, cualquiera podría crear, modificar o eliminar tareas. En producción implementaría Spring Security con JWT para autenticar usuarios y asociar las tareas a su propietario, evitando que un usuario pueda ver o modificar las tareas de otro.

2. **Migraciones de base de datos versionadas (Flyway/Liquibase)**: como se mencionó en la pregunta anterior, reemplazaría `ddl-auto=update` por `validate` y gestionaría todos los cambios de esquema mediante scripts de migración versionados, permitiendo control de cambios y rollback seguro.

3. **Manejo seguro de credenciales y configuración por ambiente**: las credenciales de la base de datos no deberían estar en `application.properties` dentro del repositorio. Usaría variables de entorno o un gestor de secretos (como AWS Secrets Manager o Spring Cloud Config) y tendría perfiles separados (`application-dev.properties`, `application-prod.properties`) para diferenciar configuración de desarrollo y producción.

4. **Paginación obligatoria en los listados**: el endpoint `GET /api/tareas` actualmente retorna todos los registros sin límite. Con miles de usuarios y tareas, esto generaría respuestas muy pesadas y lentas. Implementaría paginación obligatoria con `Pageable` en todos los endpoints de listado.

5. **Logging estructurado y monitoreo**: agregaría un sistema de logs estructurado (como Logback con formato JSON) y herramientas de monitoreo (como Actuator + Prometheus/Grafana) para detectar errores, medir tiempos de respuesta y tener visibilidad del estado de la aplicación en tiempo real.
