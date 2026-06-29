USE CanchaLibre;

-- ═══════════════════════════════════════════════════════════════════════════════
-- SEED DATA — DATOS MAESTROS PERSISTENTES (EQUIVALENTE A Program.java L60–L260)
-- Crea un propietario de prueba y 4 canchas con sus bloques horarios,
-- deportes y etiquetas.
-- ═══════════════════════════════════════════════════════════════════════════════

-- ── PROPIETARIO SEMILLA ──────────────────────────────────────────────────────
INSERT INTO CUENTA_USUARIO(userName, password, rol, intentosFallidos, activo)
VALUES('seed_propietario', 'seed_pass_2026', 'PROPIETARIO', 0, TRUE);

SET @idCuentaProp = LAST_INSERT_ID();

INSERT INTO PROPIETARIO(idCuentaUsuario, nombres, correo, telefono, calificacion, ruc, saldo, activo)
VALUES(@idCuentaProp, 'Complejos Deportivos Lima SAC', 'contacto.seed@complejoslima.com', '999000111', 4.5, '20123456789', 0.00, TRUE);

SET @idProp = LAST_INSERT_ID();

-- ═══════════════════════════════════════════════════════════════════════════════
-- CANCHA 1: Fútbol
-- ═══════════════════════════════════════════════════════════════════════════════
INSERT INTO CANCHA(nombre, descripcion, direccion, imagenUrl, idPropietario, precioBase, promedioCalificacion, activo)
VALUES('Estadio Municipal de Fútbol',
       'Cancha de césped sintético profesional con iluminación LED y graderías.',
       'Av. del Deporte 400, San Miguel, Lima',
       'https://images.unsplash.com/photo-1459865264687-595d652de67e?w=600',
       @idProp, 100.00, 4.7, TRUE);

SET @idCancha1 = LAST_INSERT_ID();

INSERT INTO CANCHA_DEPORTE(idCancha, deporte) VALUES(@idCancha1, 'FUTBOL');

INSERT INTO CANCHA_ETIQUETA(idCancha, etiqueta) VALUES
(@idCancha1, 'ILUMINACION'),
(@idCancha1, 'PARKING'),
(@idCancha1, 'VESTIDORES'),
(@idCancha1, 'DUCHAS');

-- Bloques LUNES–VIERNES (día 1–5) 18:00–22:00
INSERT INTO BLOQUE_HORARIO(idCancha, dia, horaInicio, horaFin, precio, estado, activo) VALUES
(@idCancha1, 1, '18:00', '19:00',  80.00, 'DISPONIBLE', TRUE),
(@idCancha1, 1, '19:00', '20:00',  80.00, 'DISPONIBLE', TRUE),
(@idCancha1, 1, '20:00', '21:00', 100.00, 'DISPONIBLE', TRUE),
(@idCancha1, 1, '21:00', '22:00', 100.00, 'DISPONIBLE', TRUE),
(@idCancha1, 2, '18:00', '19:00',  80.00, 'DISPONIBLE', TRUE),
(@idCancha1, 2, '19:00', '20:00',  80.00, 'DISPONIBLE', TRUE),
(@idCancha1, 2, '20:00', '21:00', 100.00, 'DISPONIBLE', TRUE),
(@idCancha1, 2, '21:00', '22:00', 100.00, 'DISPONIBLE', TRUE),
(@idCancha1, 3, '18:00', '19:00',  80.00, 'DISPONIBLE', TRUE),
(@idCancha1, 3, '19:00', '20:00',  80.00, 'DISPONIBLE', TRUE),
(@idCancha1, 3, '20:00', '21:00', 100.00, 'DISPONIBLE', TRUE),
(@idCancha1, 3, '21:00', '22:00', 100.00, 'DISPONIBLE', TRUE),
(@idCancha1, 4, '18:00', '19:00',  80.00, 'DISPONIBLE', TRUE),
(@idCancha1, 4, '19:00', '20:00',  80.00, 'DISPONIBLE', TRUE),
(@idCancha1, 4, '20:00', '21:00', 100.00, 'DISPONIBLE', TRUE),
(@idCancha1, 4, '21:00', '22:00', 100.00, 'DISPONIBLE', TRUE),
(@idCancha1, 5, '18:00', '19:00',  80.00, 'DISPONIBLE', TRUE),
(@idCancha1, 5, '19:00', '20:00',  80.00, 'DISPONIBLE', TRUE),
(@idCancha1, 5, '20:00', '21:00', 100.00, 'DISPONIBLE', TRUE),
(@idCancha1, 5, '21:00', '22:00', 100.00, 'DISPONIBLE', TRUE);

-- Bloques SÁBADO–DOMINGO (día 6–7) 10:00–22:00
INSERT INTO BLOQUE_HORARIO(idCancha, dia, horaInicio, horaFin, precio, estado, activo) VALUES
(@idCancha1, 6, '10:00', '11:00', 100.00, 'DISPONIBLE', TRUE),
(@idCancha1, 6, '11:00', '12:00', 100.00, 'DISPONIBLE', TRUE),
(@idCancha1, 6, '12:00', '13:00', 100.00, 'DISPONIBLE', TRUE),
(@idCancha1, 6, '13:00', '14:00', 100.00, 'DISPONIBLE', TRUE),
(@idCancha1, 6, '14:00', '15:00', 100.00, 'DISPONIBLE', TRUE),
(@idCancha1, 6, '15:00', '16:00', 100.00, 'DISPONIBLE', TRUE),
(@idCancha1, 6, '16:00', '17:00', 100.00, 'DISPONIBLE', TRUE),
(@idCancha1, 6, '17:00', '18:00', 100.00, 'DISPONIBLE', TRUE),
(@idCancha1, 6, '18:00', '19:00', 100.00, 'DISPONIBLE', TRUE),
(@idCancha1, 6, '19:00', '20:00', 100.00, 'DISPONIBLE', TRUE),
(@idCancha1, 6, '20:00', '21:00', 120.00, 'DISPONIBLE', TRUE),
(@idCancha1, 6, '21:00', '22:00', 120.00, 'DISPONIBLE', TRUE),
(@idCancha1, 7, '10:00', '11:00', 100.00, 'DISPONIBLE', TRUE),
(@idCancha1, 7, '11:00', '12:00', 100.00, 'DISPONIBLE', TRUE),
(@idCancha1, 7, '12:00', '13:00', 100.00, 'DISPONIBLE', TRUE),
(@idCancha1, 7, '13:00', '14:00', 100.00, 'DISPONIBLE', TRUE),
(@idCancha1, 7, '14:00', '15:00', 100.00, 'DISPONIBLE', TRUE),
(@idCancha1, 7, '15:00', '16:00', 100.00, 'DISPONIBLE', TRUE),
(@idCancha1, 7, '16:00', '17:00', 100.00, 'DISPONIBLE', TRUE),
(@idCancha1, 7, '17:00', '18:00', 100.00, 'DISPONIBLE', TRUE),
(@idCancha1, 7, '18:00', '19:00', 100.00, 'DISPONIBLE', TRUE),
(@idCancha1, 7, '19:00', '20:00', 100.00, 'DISPONIBLE', TRUE),
(@idCancha1, 7, '20:00', '21:00', 120.00, 'DISPONIBLE', TRUE),
(@idCancha1, 7, '21:00', '22:00', 120.00, 'DISPONIBLE', TRUE);

-- ═══════════════════════════════════════════════════════════════════════════════
-- CANCHA 2: Básquet
-- ═══════════════════════════════════════════════════════════════════════════════
INSERT INTO CANCHA(nombre, descripcion, direccion, imagenUrl, idPropietario, precioBase, promedioCalificacion, activo)
VALUES('Cancha de Básquet San Miguel',
       'Losa múltiple techada con tableros reglamentarios y marcador electrónico.',
       'Jr. Deportes 250, San Miguel, Lima',
       'https://images.unsplash.com/photo-1546519638-68e109498ffc?w=600',
       @idProp, 70.00, 4.3, TRUE);

SET @idCancha2 = LAST_INSERT_ID();

INSERT INTO CANCHA_DEPORTE(idCancha, deporte) VALUES(@idCancha2, 'BASQUET');

INSERT INTO CANCHA_ETIQUETA(idCancha, etiqueta) VALUES
(@idCancha2, 'ILUMINACION'),
(@idCancha2, 'WIFI'),
(@idCancha2, 'VESTIDORES');

-- Bloques LUNES–VIERNES (día 1–5) 16:00–22:00
INSERT INTO BLOQUE_HORARIO(idCancha, dia, horaInicio, horaFin, precio, estado, activo) VALUES
(@idCancha2, 1, '16:00', '17:00', 60.00, 'DISPONIBLE', TRUE),
(@idCancha2, 1, '17:00', '18:00', 60.00, 'DISPONIBLE', TRUE),
(@idCancha2, 1, '18:00', '19:00', 60.00, 'DISPONIBLE', TRUE),
(@idCancha2, 1, '19:00', '20:00', 60.00, 'DISPONIBLE', TRUE),
(@idCancha2, 1, '20:00', '21:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha2, 1, '21:00', '22:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha2, 2, '16:00', '17:00', 60.00, 'DISPONIBLE', TRUE),
(@idCancha2, 2, '17:00', '18:00', 60.00, 'DISPONIBLE', TRUE),
(@idCancha2, 2, '18:00', '19:00', 60.00, 'DISPONIBLE', TRUE),
(@idCancha2, 2, '19:00', '20:00', 60.00, 'DISPONIBLE', TRUE),
(@idCancha2, 2, '20:00', '21:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha2, 2, '21:00', '22:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha2, 3, '16:00', '17:00', 60.00, 'DISPONIBLE', TRUE),
(@idCancha2, 3, '17:00', '18:00', 60.00, 'DISPONIBLE', TRUE),
(@idCancha2, 3, '18:00', '19:00', 60.00, 'DISPONIBLE', TRUE),
(@idCancha2, 3, '19:00', '20:00', 60.00, 'DISPONIBLE', TRUE),
(@idCancha2, 3, '20:00', '21:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha2, 3, '21:00', '22:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha2, 4, '16:00', '17:00', 60.00, 'DISPONIBLE', TRUE),
(@idCancha2, 4, '17:00', '18:00', 60.00, 'DISPONIBLE', TRUE),
(@idCancha2, 4, '18:00', '19:00', 60.00, 'DISPONIBLE', TRUE),
(@idCancha2, 4, '19:00', '20:00', 60.00, 'DISPONIBLE', TRUE),
(@idCancha2, 4, '20:00', '21:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha2, 4, '21:00', '22:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha2, 5, '16:00', '17:00', 60.00, 'DISPONIBLE', TRUE),
(@idCancha2, 5, '17:00', '18:00', 60.00, 'DISPONIBLE', TRUE),
(@idCancha2, 5, '18:00', '19:00', 60.00, 'DISPONIBLE', TRUE),
(@idCancha2, 5, '19:00', '20:00', 60.00, 'DISPONIBLE', TRUE),
(@idCancha2, 5, '20:00', '21:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha2, 5, '21:00', '22:00', 70.00, 'DISPONIBLE', TRUE);

-- Bloques SÁBADO (día 6) 14:00–22:00
INSERT INTO BLOQUE_HORARIO(idCancha, dia, horaInicio, horaFin, precio, estado, activo) VALUES
(@idCancha2, 6, '14:00', '15:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha2, 6, '15:00', '16:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha2, 6, '16:00', '17:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha2, 6, '17:00', '18:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha2, 6, '18:00', '19:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha2, 6, '19:00', '20:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha2, 6, '20:00', '21:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha2, 6, '21:00', '22:00', 70.00, 'DISPONIBLE', TRUE);

-- ═══════════════════════════════════════════════════════════════════════════════
-- CANCHA 3: Vóley
-- ═══════════════════════════════════════════════════════════════════════════════
INSERT INTO CANCHA(nombre, descripcion, direccion, imagenUrl, idPropietario, precioBase, promedioCalificacion, activo)
VALUES('Complejo de Vóley La Molina',
       'Cancha de arena sintética con redes profesionales y zona de sombra.',
       'Av. La Molina 1800, La Molina, Lima',
       'https://images.unsplash.com/photo-1612872087720-bb876e2e67d1?w=600',
       @idProp, 60.00, 4.5, TRUE);

SET @idCancha3 = LAST_INSERT_ID();

INSERT INTO CANCHA_DEPORTE(idCancha, deporte) VALUES(@idCancha3, 'VOLEY');

INSERT INTO CANCHA_ETIQUETA(idCancha, etiqueta) VALUES
(@idCancha3, 'PARKING'),
(@idCancha3, 'VESTIDORES'),
(@idCancha3, 'BANOS');

-- Bloques LUNES–VIERNES (día 1–5) 14:00–20:00
INSERT INTO BLOQUE_HORARIO(idCancha, dia, horaInicio, horaFin, precio, estado, activo) VALUES
(@idCancha3, 1, '14:00', '15:00', 50.00, 'DISPONIBLE', TRUE),
(@idCancha3, 1, '15:00', '16:00', 50.00, 'DISPONIBLE', TRUE),
(@idCancha3, 1, '16:00', '17:00', 50.00, 'DISPONIBLE', TRUE),
(@idCancha3, 1, '17:00', '18:00', 50.00, 'DISPONIBLE', TRUE),
(@idCancha3, 1, '18:00', '19:00', 50.00, 'DISPONIBLE', TRUE),
(@idCancha3, 1, '19:00', '20:00', 50.00, 'DISPONIBLE', TRUE),
(@idCancha3, 2, '14:00', '15:00', 50.00, 'DISPONIBLE', TRUE),
(@idCancha3, 2, '15:00', '16:00', 50.00, 'DISPONIBLE', TRUE),
(@idCancha3, 2, '16:00', '17:00', 50.00, 'DISPONIBLE', TRUE),
(@idCancha3, 2, '17:00', '18:00', 50.00, 'DISPONIBLE', TRUE),
(@idCancha3, 2, '18:00', '19:00', 50.00, 'DISPONIBLE', TRUE),
(@idCancha3, 2, '19:00', '20:00', 50.00, 'DISPONIBLE', TRUE),
(@idCancha3, 3, '14:00', '15:00', 50.00, 'DISPONIBLE', TRUE),
(@idCancha3, 3, '15:00', '16:00', 50.00, 'DISPONIBLE', TRUE),
(@idCancha3, 3, '16:00', '17:00', 50.00, 'DISPONIBLE', TRUE),
(@idCancha3, 3, '17:00', '18:00', 50.00, 'DISPONIBLE', TRUE),
(@idCancha3, 3, '18:00', '19:00', 50.00, 'DISPONIBLE', TRUE),
(@idCancha3, 3, '19:00', '20:00', 50.00, 'DISPONIBLE', TRUE),
(@idCancha3, 4, '14:00', '15:00', 50.00, 'DISPONIBLE', TRUE),
(@idCancha3, 4, '15:00', '16:00', 50.00, 'DISPONIBLE', TRUE),
(@idCancha3, 4, '16:00', '17:00', 50.00, 'DISPONIBLE', TRUE),
(@idCancha3, 4, '17:00', '18:00', 50.00, 'DISPONIBLE', TRUE),
(@idCancha3, 4, '18:00', '19:00', 50.00, 'DISPONIBLE', TRUE),
(@idCancha3, 4, '19:00', '20:00', 50.00, 'DISPONIBLE', TRUE),
(@idCancha3, 5, '14:00', '15:00', 50.00, 'DISPONIBLE', TRUE),
(@idCancha3, 5, '15:00', '16:00', 50.00, 'DISPONIBLE', TRUE),
(@idCancha3, 5, '16:00', '17:00', 50.00, 'DISPONIBLE', TRUE),
(@idCancha3, 5, '17:00', '18:00', 50.00, 'DISPONIBLE', TRUE),
(@idCancha3, 5, '18:00', '19:00', 50.00, 'DISPONIBLE', TRUE),
(@idCancha3, 5, '19:00', '20:00', 50.00, 'DISPONIBLE', TRUE);

-- Bloques SÁBADO (día 6) 10:00–19:00
INSERT INTO BLOQUE_HORARIO(idCancha, dia, horaInicio, horaFin, precio, estado, activo) VALUES
(@idCancha3, 6, '10:00', '11:00', 60.00, 'DISPONIBLE', TRUE),
(@idCancha3, 6, '11:00', '12:00', 60.00, 'DISPONIBLE', TRUE),
(@idCancha3, 6, '12:00', '13:00', 60.00, 'DISPONIBLE', TRUE),
(@idCancha3, 6, '13:00', '14:00', 60.00, 'DISPONIBLE', TRUE),
(@idCancha3, 6, '14:00', '15:00', 60.00, 'DISPONIBLE', TRUE),
(@idCancha3, 6, '15:00', '16:00', 60.00, 'DISPONIBLE', TRUE),
(@idCancha3, 6, '16:00', '17:00', 60.00, 'DISPONIBLE', TRUE),
(@idCancha3, 6, '17:00', '18:00', 60.00, 'DISPONIBLE', TRUE),
(@idCancha3, 6, '18:00', '19:00', 60.00, 'DISPONIBLE', TRUE);

-- ═══════════════════════════════════════════════════════════════════════════════
-- CANCHA 4: Tenis
-- ═══════════════════════════════════════════════════════════════════════════════
INSERT INTO CANCHA(nombre, descripcion, direccion, imagenUrl, idPropietario, precioBase, promedioCalificacion, activo)
VALUES('Cancha de Tenis El Golf',
       'Cancha de polvo de ladrillo con iluminación, muros de práctica y zona de descanso.',
       'Calle Los Olivos 550, San Isidro, Lima',
       'https://images.unsplash.com/photo-1595435934249-5df7ed86e1c0?w=600',
       @idProp, 90.00, 4.8, TRUE);

SET @idCancha4 = LAST_INSERT_ID();

INSERT INTO CANCHA_DEPORTE(idCancha, deporte) VALUES(@idCancha4, 'TENIS');

INSERT INTO CANCHA_ETIQUETA(idCancha, etiqueta) VALUES
(@idCancha4, 'ILUMINACION'),
(@idCancha4, 'WIFI'),
(@idCancha4, 'BANOS'),
(@idCancha4, 'DUCHAS');

-- Bloques LUNES–VIERNES (día 1–5) 08:00–18:00
INSERT INTO BLOQUE_HORARIO(idCancha, dia, horaInicio, horaFin, precio, estado, activo) VALUES
(@idCancha4, 1, '08:00', '09:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha4, 1, '09:00', '10:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha4, 1, '10:00', '11:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha4, 1, '11:00', '12:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha4, 1, '12:00', '13:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha4, 1, '13:00', '14:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha4, 1, '14:00', '15:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha4, 1, '15:00', '16:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha4, 1, '16:00', '17:00', 90.00, 'DISPONIBLE', TRUE),
(@idCancha4, 1, '17:00', '18:00', 90.00, 'DISPONIBLE', TRUE),
(@idCancha4, 2, '08:00', '09:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha4, 2, '09:00', '10:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha4, 2, '10:00', '11:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha4, 2, '11:00', '12:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha4, 2, '12:00', '13:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha4, 2, '13:00', '14:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha4, 2, '14:00', '15:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha4, 2, '15:00', '16:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha4, 2, '16:00', '17:00', 90.00, 'DISPONIBLE', TRUE),
(@idCancha4, 2, '17:00', '18:00', 90.00, 'DISPONIBLE', TRUE),
(@idCancha4, 3, '08:00', '09:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha4, 3, '09:00', '10:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha4, 3, '10:00', '11:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha4, 3, '11:00', '12:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha4, 3, '12:00', '13:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha4, 3, '13:00', '14:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha4, 3, '14:00', '15:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha4, 3, '15:00', '16:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha4, 3, '16:00', '17:00', 90.00, 'DISPONIBLE', TRUE),
(@idCancha4, 3, '17:00', '18:00', 90.00, 'DISPONIBLE', TRUE),
(@idCancha4, 4, '08:00', '09:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha4, 4, '09:00', '10:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha4, 4, '10:00', '11:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha4, 4, '11:00', '12:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha4, 4, '12:00', '13:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha4, 4, '13:00', '14:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha4, 4, '14:00', '15:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha4, 4, '15:00', '16:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha4, 4, '16:00', '17:00', 90.00, 'DISPONIBLE', TRUE),
(@idCancha4, 4, '17:00', '18:00', 90.00, 'DISPONIBLE', TRUE),
(@idCancha4, 5, '08:00', '09:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha4, 5, '09:00', '10:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha4, 5, '10:00', '11:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha4, 5, '11:00', '12:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha4, 5, '12:00', '13:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha4, 5, '13:00', '14:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha4, 5, '14:00', '15:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha4, 5, '15:00', '16:00', 70.00, 'DISPONIBLE', TRUE),
(@idCancha4, 5, '16:00', '17:00', 90.00, 'DISPONIBLE', TRUE),
(@idCancha4, 5, '17:00', '18:00', 90.00, 'DISPONIBLE', TRUE);

-- Bloques SÁBADO–DOMINGO (día 6–7) 08:00–15:00
INSERT INTO BLOQUE_HORARIO(idCancha, dia, horaInicio, horaFin, precio, estado, activo) VALUES
(@idCancha4, 6, '08:00', '09:00', 90.00, 'DISPONIBLE', TRUE),
(@idCancha4, 6, '09:00', '10:00', 90.00, 'DISPONIBLE', TRUE),
(@idCancha4, 6, '10:00', '11:00', 90.00, 'DISPONIBLE', TRUE),
(@idCancha4, 6, '11:00', '12:00', 90.00, 'DISPONIBLE', TRUE),
(@idCancha4, 6, '12:00', '13:00', 90.00, 'DISPONIBLE', TRUE),
(@idCancha4, 6, '13:00', '14:00', 90.00, 'DISPONIBLE', TRUE),
(@idCancha4, 6, '14:00', '15:00', 90.00, 'DISPONIBLE', TRUE),
(@idCancha4, 7, '08:00', '09:00', 90.00, 'DISPONIBLE', TRUE),
(@idCancha4, 7, '09:00', '10:00', 90.00, 'DISPONIBLE', TRUE),
(@idCancha4, 7, '10:00', '11:00', 90.00, 'DISPONIBLE', TRUE),
(@idCancha4, 7, '11:00', '12:00', 90.00, 'DISPONIBLE', TRUE),
(@idCancha4, 7, '12:00', '13:00', 90.00, 'DISPONIBLE', TRUE),
(@idCancha4, 7, '13:00', '14:00', 90.00, 'DISPONIBLE', TRUE),
(@idCancha4, 7, '14:00', '15:00', 90.00, 'DISPONIBLE', TRUE);
