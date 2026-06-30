USE [CanchaLibre];
GO

DROP PROCEDURE IF EXISTS dbo.insertarPropietario;
DROP PROCEDURE IF EXISTS dbo.modificarPropietario;
DROP PROCEDURE IF EXISTS dbo.eliminarPropietario;
DROP PROCEDURE IF EXISTS dbo.buscarPropietarioPorId;
DROP PROCEDURE IF EXISTS dbo.listarPropietarios;
DROP PROCEDURE IF EXISTS dbo.buscarPropietarioPorNombre;
DROP PROCEDURE IF EXISTS dbo.buscarPropietarioPorCuenta;
DROP PROCEDURE IF EXISTS dbo.actualizarSaldo;
GO

CREATE PROCEDURE dbo.insertarPropietario
    @p_idCuentaUsuario INT,
    @p_nombres VARCHAR(150),
    @p_correo VARCHAR(50),
    @p_telefono VARCHAR(15),
    @p_ruc CHAR(11),
    @p_activo BIT,
    @p_id INT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;

    INSERT INTO dbo.PROPIETARIO (
        idCuentaUsuario,
        nombres,
        correo,
        telefono,
        ruc,
        activo
    )
    VALUES (
        @p_idCuentaUsuario,
        @p_nombres,
        @p_correo,
        @p_telefono,
        @p_ruc,
        @p_activo
    );

    SET @p_id = SCOPE_IDENTITY();
END;
GO

CREATE PROCEDURE dbo.modificarPropietario
    @p_idCuentaUsuario INT,
    @p_nombres VARCHAR(150),
    @p_correo VARCHAR(50),
    @p_telefono VARCHAR(15),
    @p_calificacion DECIMAL(2,1),
    @p_ruc CHAR(11),
    @p_saldo DECIMAL(10,2),
    @p_activo BIT,
    @p_id INT
AS
BEGIN
    SET NOCOUNT ON;

    UPDATE dbo.PROPIETARIO
    SET
        idCuentaUsuario = @p_idCuentaUsuario,
        nombres = @p_nombres,
        correo = @p_correo,
        telefono = @p_telefono,
        calificacion = @p_calificacion,
        ruc = @p_ruc,
        saldo = @p_saldo,
        activo = @p_activo
    WHERE id = @p_id;
END;
GO

CREATE PROCEDURE dbo.eliminarPropietario
    @p_id INT
AS
BEGIN
    SET NOCOUNT ON;

    UPDATE dbo.PROPIETARIO
    SET activo = 0
    WHERE id = @p_id;
END;
GO

CREATE PROCEDURE dbo.buscarPropietarioPorId
    @p_id INT
AS
BEGIN
    SET NOCOUNT ON;

    SELECT
        p.id AS id,
        p.activo AS activo,
        p.nombres AS nombres,
        p.correo AS correo,
        p.telefono AS telefono,
        p.calificacion AS calificacion,
        p.ruc AS ruc,
        p.saldo AS saldo,
        p.idCuentaUsuario AS idCuentaUsuario,
        cu.userName AS userName,
        cu.[password] AS [password],
        cu.rol AS rol,
        cu.intentosFallidos AS intentosFallidos,
        cu.ultimaSesion AS ultimaSesion,
        cu.fechaBloqueo AS fechaBloqueo
    FROM dbo.PROPIETARIO p
    INNER JOIN dbo.CUENTA_USUARIO cu 
        ON p.idCuentaUsuario = cu.id
    WHERE p.id = @p_id;
END;
GO

CREATE PROCEDURE dbo.listarPropietarios
AS
BEGIN
    SET NOCOUNT ON;

    SELECT
        p.id AS id,
        p.activo AS activo,
        p.nombres AS nombres,
        p.correo AS correo,
        p.telefono AS telefono,
        p.calificacion AS calificacion,
        p.ruc AS ruc,
        p.saldo AS saldo,
        p.idCuentaUsuario AS idCuentaUsuario,
        cu.userName AS userName,
        cu.[password] AS [password],
        cu.rol AS rol,
        cu.intentosFallidos AS intentosFallidos,
        cu.ultimaSesion AS ultimaSesion,
        cu.fechaBloqueo AS fechaBloqueo
    FROM dbo.PROPIETARIO p
    INNER JOIN dbo.CUENTA_USUARIO cu 
        ON p.idCuentaUsuario = cu.id
    WHERE p.activo = 1;
END;
GO

CREATE PROCEDURE dbo.buscarPropietarioPorNombre
    @p_nombres VARCHAR(150)
AS
BEGIN
    SET NOCOUNT ON;

    SELECT
        p.id AS id,
        p.activo AS activo,
        p.nombres AS nombres,
        p.correo AS correo,
        p.telefono AS telefono,
        p.calificacion AS calificacion,
        p.ruc AS ruc,
        p.saldo AS saldo,
        p.idCuentaUsuario AS idCuentaUsuario,
        cu.userName AS userName,
        cu.[password] AS [password],
        cu.rol AS rol,
        cu.intentosFallidos AS intentosFallidos,
        cu.ultimaSesion AS ultimaSesion,
        cu.fechaBloqueo AS fechaBloqueo
    FROM dbo.PROPIETARIO p
    INNER JOIN dbo.CUENTA_USUARIO cu 
        ON p.idCuentaUsuario = cu.id
    WHERE p.nombres = @p_nombres;
END;
GO

CREATE PROCEDURE dbo.buscarPropietarioPorCuenta
    @p_userName VARCHAR(50)
AS
BEGIN
    SET NOCOUNT ON;

    SELECT
        p.id AS id,
        p.activo AS activo,
        p.nombres AS nombres,
        p.correo AS correo,
        p.telefono AS telefono,
        p.calificacion AS calificacion,
        p.ruc AS ruc,
        p.saldo AS saldo,
        p.idCuentaUsuario AS idCuentaUsuario,
        cu.userName AS userName,
        cu.[password] AS [password],
        cu.rol AS rol,
        cu.intentosFallidos AS intentosFallidos,
        cu.ultimaSesion AS ultimaSesion,
        cu.fechaBloqueo AS fechaBloqueo
    FROM dbo.PROPIETARIO p
    INNER JOIN dbo.CUENTA_USUARIO cu 
        ON p.idCuentaUsuario = cu.id
    WHERE cu.userName = @p_userName;
END;
GO

CREATE PROCEDURE dbo.actualizarSaldo
    @p_idPropietario INT,
    @p_monto DECIMAL(10,2)
AS
BEGIN
    SET NOCOUNT ON;

    UPDATE dbo.PROPIETARIO
    SET saldo = saldo + @p_monto
    WHERE id = @p_idPropietario;
END;
GO