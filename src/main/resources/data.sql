INSERT INTO tareas (titulo, descripcion, estado, prioridad, fecha_creacion, fecha_actualizacion) VALUES
('Configurar servidor de produccion', 'Preparar el ambiente de produccion con todas las dependencias', 'PENDIENTE', 'ALTA', NOW(), NOW()),
('Revisar pull request del equipo', 'Validar el codigo antes de mergear a main', 'EN_PROGRESO', 'MEDIA', NOW(), NOW()),
('Actualizar documentacion del API', 'Agregar los nuevos endpoints a la documentacion', 'PENDIENTE', 'BAJA', NOW(), NOW()),
('Corregir bug en login', 'Los usuarios no pueden iniciar sesion con Google', 'COMPLETADA', 'URGENTE', NOW(), NOW()),
('Migrar base de datos', 'Migracion de PostgreSQL a MySQL', 'CANCELADA', 'ALTA', NOW(), NOW());