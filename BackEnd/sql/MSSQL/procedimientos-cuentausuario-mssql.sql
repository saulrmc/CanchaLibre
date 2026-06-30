USE [CanchaLibre];
GO

DROP PROCEDURE IF EXISTS dbo.insertarCuentaUsuario;
DROP PROCEDURE IF EXISTS dbo.modificarCuentaUsuario;
DROP PROCEDURE IF EXISTS dbo.eliminarCuentaUsuario;
DROP PROCEDURE IF EXISTS dbo.buscarCuentaUsuarioPorId;
DROP PROCEDURE IF EXISTS dbo.listarCuentaUsuarios;
DROP PROCEDURE IF EXISTS dbo.loginUsuario;
DROP PROCEDURE IF EXISTS dbo.actualizarSeguridad;
GO

CREATE PROCEDURE dbo.insertarCuentaUsuario
    @p_userName VARCHAR(50),
    @p_password VARCHAR(50),
    @p_rol VARCHAR(15),
    @p_fechaBloqueo DATETIME2,
    @p_activo BIT,
    @p_id INT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;

    INSERT INTO dbo.CUENTA_USUARIO (
        userName,
        [password],
        rol,
        fechaBloqueo,
        activo
    )
    VALUES (
        @p_userName,
        @p_password,
        @p_rol,
        @p_fechaBloqueo,
        @p_activo
    );

    SET @p_id = SCOPE_IDENTITY();
END;
GO

CREATE PROCEDURE dbo.modificarCuentaUsuario
    @p_userName VARCHAR(50),
    @p_password VARCHAR(50),
    @p_intentosFallidos INT,
    @p_ultimaSesion DATETIME2,
    @p_fechaBloqueo DATETIME2,
    @p_activo BIT,
    @p_id INT
AS
BEGIN
    SET NOCOUNT ON;

    UPDATE dbo.CUENTA_USUARIO
    SET
        userName = @p_userName,
        [password] = @p_password,
        intentosFallidos = @p_intentosFallidos,
        ultimaSesion = @p_ultimaSesion,
        fechaBloqueo = @p_fechaBloqueo,
        activo = @p_activo
    WHERE id = @p_id;
END;
GO

CREATE PROCEDURE dbo.eliminarCuentaUsuario
    @p_id INT
AS
BEGIN
    SET NOCOUNT ON;

    UPDATE dbo.CUENTA_USUARIO
    SET activo = 0
    WHERE id = @p_id;
END;
GO

CREATE PROCEDURE dbo.buscarCuentaUsuarioPorId
    @p_id INT
AS
BEGIN
    SET NOCOUNT ON;

    SELECT
        id,
        userName,
        [password],
        rol,
        intentosFallidos,
        ultimaSesion,
        fechaBloqueo,
        activo
    FROM dbo.CUENTA_USUARIO
    WHERE id = @p_id;
END;
GO

CREATE PROCEDURE dbo.listarCuentaUsuarios
AS
BEGIN
    SET NOCOUNT ON;

    SELECT
        id,
        userName,
        [password],
        rol,
        intentosFallidos,
        ultimaSesion,
        fechaBloqueo,
        activo
    FROM dbo.CUENTA_USUARIO
    WHERE activo = 1;
END;
GO

CREATE PROCEDURE dbo.loginUsuario
    @p_username VARCHAR(50),
    @p_password VARCHAR(50),
    @p_valido BIT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @v_count INT = 0;

    SELECT @v_count = COUNT(*)
    FROM dbo.CUENTA_USUARIO
    WHERE userName = @p_username
      AND [password] = @p_password;

    IF @v_count > 0
        SET @p_valido = 1;
    ELSE
        SET @p_valido = 0;
END;
GO

CREATE PROCEDURE dbo.actualizarSeguridad
    @p_intentosFallidos INT,
    @p_ultimaSesion DATETIME2,
    @p_fechaBloqueo DATETIME2,
    @p_id INT
AS
BEGIN
    SET NOCOUNT ON;

    UPDATE dbo.CUENTA_USUARIO
    SET
        intentosFallidos = @p_intentosFallidos,
        ultimaSesion = @p_ultimaSesion,
        fechaBloqueo = @p_fechaBloqueo
    WHERE id = @p_id;
END;
GO