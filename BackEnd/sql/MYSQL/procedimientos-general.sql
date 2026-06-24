USE CanchaLibre;
DROP PROCEDURE IF EXISTS insertarCuentaUsuario;

DELIMITER $$

DROP PROCEDURE IF EXISTS insertarCuentaUsuario$$

CREATE PROCEDURE insertarCuentaUsuario(
    IN p_userName VARCHAR(50),
    IN p_password VARCHAR(255),
    IN p_rol VARCHAR(20),
    IN p_fechaBloqueo TIMESTAMP,
    IN p_activo TINYINT(1),
    OUT p_id INT
        )
BEGIN
    -- Insertamos los datos en la tabla cuenta_usuario
    -- (Asegúrate de cambiar los nombres de las columnas según tu tabla exacta)
INSERT INTO cuenta_usuario (
    userName,
    password,
    rol,
    intentosFallidos, -- Se inicializa por defecto en el flujo de creación
    fechaBloqueo,
    activo
)
VALUES (
           p_userName,
           p_password,
           p_rol,
           0,
           p_fechaBloqueo,
           p_activo
       );

-- Recuperamos el ID autogenerado por el INT AUTO_INCREMENT
-- y lo asignamos al parámetro de salida para que tu Java lo lea con registerOutParameter
SET p_id = LAST_INSERT_ID();

END$$

DELIMITER ;

DROP TABLE IF EXISTS cuenta_usuario;
CREATE TABLE cuenta_usuario (
                                id_cuenta_usuario INT AUTO_INCREMENT PRIMARY KEY,
                                userName VARCHAR(50) NOT NULL UNIQUE,
                                password VARCHAR(255) NOT NULL,
                                rol VARCHAR(20) NOT NULL,
                                intentosFallidos INT DEFAULT 0 NOT NULL,
                                ultimaSesion TIMESTAMP NULL,
                                fechaBloqueo TIMESTAMP NULL,
                                activo TINYINT(1) DEFAULT 1 NOT NULL,

                                INDEX idx_username (userName)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;