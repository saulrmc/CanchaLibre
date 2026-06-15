USE CanchaLibre;

DROP PROCEDURE IF EXISTS loginUsuario;

CREATE PROCEDURE loginUsuario(
    IN p_username VARCHAR(50),
    IN p_password VARCHAR(50),
    OUT p_valido BOOLEAN
)
BEGIN
    DECLARE v_count INT DEFAULT 0;

    SELECT COUNT(*) INTO v_count
    FROM CUENTA_USUARIO
    WHERE userName = p_username
      AND password = p_password;

    IF v_count > 0 THEN
        SET p_valido = TRUE;
    ELSE
        SET p_valido = FALSE;
    END IF;
END //