USE [CanchaLibre];
GO

DROP PROCEDURE IF EXISTS dbo.insertarAdministrador;
DROP PROCEDURE IF EXISTS dbo.modificarAdministrador;
DROP PROCEDURE IF EXISTS dbo.eliminarAdministrador;
DROP PROCEDURE IF EXISTS dbo.buscarAdministradorPorId;
DROP PROCEDURE IF EXISTS dbo.listarAdministradores;
DROP PROCEDURE IF EXISTS dbo.buscarAdministradorPorNombre;
DROP PROCEDURE IF EXISTS dbo.buscarAdministradorPorCuenta;
GO

CREATE PROCEDURE dbo.insertarAdministrador
    @p_idCuentaUsuario INT,
    @p_nombres VARCHAR(150),
    @p_correo VARCHAR(50),
    @p_telefono VARCHAR(15),
    @p_activo BIT,
    @p_id INT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;

    INSERT INTO dbo.ADMINISTRADOR (
        idCuentaUsuario,
        nombres,
        correo,
        telefono,
        activo
    )
    VALUES (
        @p_idCuentaUsuario,
        @p_nombres,
        @p_correo,
        @p_telefono,
        @p_activo
    );

    SET @p_id = SCOPE_IDENTITY();
END;
GO

CREATE PROCEDURE dbo.modificarAdministrador
    @p_idCuentaUsuario INT,
    @p_nombres VARCHAR(150),
    @p_correo VARCHAR(50),
    @p_telefono VARCHAR(15),
    @p_activo BIT,
    @p_id INT
AS
BEGIN
    SET NOCOUNT ON;

    UPDATE dbo.ADMINISTRADOR
    SET
        idCuentaUsuario = @p_idCuentaUsuario,
        nombres = @p_nombres,
        correo = @p_correo,
        telefono = @p_telefono,
        activo = @p_activo
    WHERE id = @p_id;
END;
GO

CREATE PROCEDURE dbo.eliminarAdministrador
    @p_id INT
AS
BEGIN
    SET NOCOUNT ON;

    UPDATE dbo.ADMINISTRADOR
    SET activo = 0
    WHERE id = @p_id;
END;
GO

CREATE PROCEDURE dbo.buscarAdministradorPorId
    @p_id INT
AS
BEGIN
    SET NOCOUNT ON;

    SELECT
        a.id AS id,
        a.activo AS activo,
        a.nombres AS nombres,
        a.correo AS correo,
        a.telefono AS telefono,
        a.idCuentaUsuario AS idCuentaUsuario,
        cu.userName AS userName,
        cu.[password] AS [password],
        cu.rol AS rol,
        cu.intentosFallidos AS intentosFallidos,
        cu.ultimaSesion AS ultimaSesion,
        cu.fechaBloqueo AS fechaBloqueo
    FROM dbo.ADMINISTRADOR a
    INNER JOIN dbo.CUENTA_USUARIO cu 
        ON a.idCuentaUsuario = cu.id
    WHERE a.id = @p_id;
END;
GO

CREATE PROCEDURE dbo.listarAdministradores
AS
BEGIN
    SET NOCOUNT ON;

    SELECT
        a.id AS id,
        a.activo AS activo,
        a.nombres AS nombres,
        a.correo AS correo,
        a.telefono AS telefono,
        a.idCuentaUsuario AS idCuentaUsuario,
        cu.userName AS userName,
        cu.[password] AS [password],
        cu.rol AS rol,
        cu.intentosFallidos AS intentosFallidos,
        cu.ultimaSesion AS ultimaSesion,
        cu.fechaBloqueo AS fechaBloqueo
    FROM dbo.ADMINISTRADOR a
    INNER JOIN dbo.CUENTA_USUARIO cu 
        ON a.idCuentaUsuario = cu.id
    WHERE a.activo = 1;
END;
GO

CREATE PROCEDURE dbo.buscarAdministradorPorNombre
    @p_nombres VARCHAR(150)
AS
BEGIN
    SET NOCOUNT ON;

    SELECT
        a.id AS id,
        a.activo AS activo,
        a.nombres AS nombres,
        a.correo AS correo,
        a.telefono AS telefono,
        a.idCuentaUsuario AS idCuentaUsuario,
        cu.userName AS userName,
        cu.[password] AS [password],
        cu.rol AS rol,
        cu.intentosFallidos AS intentosFallidos,
        cu.ultimaSesion AS ultimaSesion,
        cu.fechaBloqueo AS fechaBloqueo
    FROM dbo.ADMINISTRADOR a
    INNER JOIN dbo.CUENTA_USUARIO cu 
        ON a.idCuentaUsuario = cu.id
    WHERE a.nombres = @p_nombres;
END;
GO

CREATE PROCEDURE dbo.buscarAdministradorPorCuenta
    @p_userName VARCHAR(50)
AS
BEGIN
    SET NOCOUNT ON;

    SELECT
        a.id AS id,
        a.activo AS activo,
        a.nombres AS nombres,
        a.correo AS correo,
        a.telefono AS telefono,
        a.idCuentaUsuario AS idCuentaUsuario,
        cu.userName AS userName,
        cu.[password] AS [password],
        cu.rol AS rol,
        cu.intentosFallidos AS intentosFallidos,
        cu.ultimaSesion AS ultimaSesion,
        cu.fechaBloqueo AS fechaBloqueo
    FROM dbo.ADMINISTRADOR a
    INNER JOIN dbo.CUENTA_USUARIO cu 
        ON a.idCuentaUsuario = cu.id
    WHERE cu.userName = @p_userName;
END;
GO