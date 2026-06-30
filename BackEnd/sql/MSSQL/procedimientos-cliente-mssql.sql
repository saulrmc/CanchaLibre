USE [CanchaLibre];
GO

DROP PROCEDURE IF EXISTS dbo.insertarCliente;
DROP PROCEDURE IF EXISTS dbo.modificarCliente;
DROP PROCEDURE IF EXISTS dbo.eliminarCliente;
DROP PROCEDURE IF EXISTS dbo.buscarClientePorId;
DROP PROCEDURE IF EXISTS dbo.listarClientes;
DROP PROCEDURE IF EXISTS dbo.buscarClientePorNombre;
DROP PROCEDURE IF EXISTS dbo.buscarClientePorCuenta;
GO

CREATE PROCEDURE dbo.insertarCliente
    @p_idCuentaUsuario INT,
    @p_nombres VARCHAR(150),
    @p_correo VARCHAR(50),
    @p_telefono VARCHAR(15),
    @p_activo BIT,
    @p_id INT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;

    INSERT INTO dbo.CLIENTE (
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

CREATE PROCEDURE dbo.modificarCliente
    @p_idCuentaUsuario INT,
    @p_nombres VARCHAR(150),
    @p_correo VARCHAR(50),
    @p_telefono VARCHAR(15),
    @p_calificacion DECIMAL(2,1),
    @p_activo BIT,
    @p_id INT
AS
BEGIN
    SET NOCOUNT ON;

    UPDATE dbo.CLIENTE
    SET
        idCuentaUsuario = @p_idCuentaUsuario,
        nombres = @p_nombres,
        correo = @p_correo,
        telefono = @p_telefono,
        calificacion = @p_calificacion,
        activo = @p_activo
    WHERE id = @p_id;
END;
GO

CREATE PROCEDURE dbo.eliminarCliente
    @p_id INT
AS
BEGIN
    SET NOCOUNT ON;

    UPDATE dbo.CLIENTE
    SET activo = 0
    WHERE id = @p_id;
END;
GO

CREATE PROCEDURE dbo.buscarClientePorId
    @p_id INT
AS
BEGIN
    SET NOCOUNT ON;

    SELECT
        c.id AS id,
        c.activo AS activo,
        c.nombres AS nombres,
        c.correo AS correo,
        c.telefono AS telefono,
        c.calificacion AS calificacion,
        c.idCuentaUsuario AS idCuentaUsuario,
        cu.userName AS userName,
        cu.[password] AS [password],
        cu.rol AS rol,
        cu.intentosFallidos AS intentosFallidos,
        cu.ultimaSesion AS ultimaSesion,
        cu.fechaBloqueo AS fechaBloqueo
    FROM dbo.CLIENTE c
    INNER JOIN dbo.CUENTA_USUARIO cu
        ON c.idCuentaUsuario = cu.id
    WHERE c.id = @p_id;
END;
GO

CREATE PROCEDURE dbo.listarClientes
AS
BEGIN
    SET NOCOUNT ON;

    SELECT
        c.id AS id,
        c.activo AS activo,
        c.nombres AS nombres,
        c.correo AS correo,
        c.telefono AS telefono,
        c.calificacion AS calificacion,
        c.idCuentaUsuario AS idCuentaUsuario,
        cu.userName AS userName,
        cu.[password] AS [password],
        cu.rol AS rol,
        cu.intentosFallidos AS intentosFallidos,
        cu.ultimaSesion AS ultimaSesion,
        cu.fechaBloqueo AS fechaBloqueo
    FROM dbo.CLIENTE c
    INNER JOIN dbo.CUENTA_USUARIO cu
        ON c.idCuentaUsuario = cu.id
    WHERE c.activo = 1;
END;
GO

CREATE PROCEDURE dbo.buscarClientePorNombre
    @p_nombres VARCHAR(150)
AS
BEGIN
    SET NOCOUNT ON;

    SELECT
        c.id AS id,
        c.activo AS activo,
        c.nombres AS nombres,
        c.correo AS correo,
        c.telefono AS telefono,
        c.calificacion AS calificacion,
        c.idCuentaUsuario AS idCuentaUsuario,
        cu.userName AS userName,
        cu.[password] AS [password],
        cu.rol AS rol,
        cu.intentosFallidos AS intentosFallidos,
        cu.ultimaSesion AS ultimaSesion,
        cu.fechaBloqueo AS fechaBloqueo
    FROM dbo.CLIENTE c
    INNER JOIN dbo.CUENTA_USUARIO cu
        ON c.idCuentaUsuario = cu.id
    WHERE c.nombres = @p_nombres;
END;
GO

CREATE PROCEDURE dbo.buscarClientePorCuenta
    @p_userName VARCHAR(50)
AS
BEGIN
    SET NOCOUNT ON;

    SELECT
        c.id AS id,
        c.activo AS activo,
        c.nombres AS nombres,
        c.correo AS correo,
        c.telefono AS telefono,
        c.calificacion AS calificacion,
        c.idCuentaUsuario AS idCuentaUsuario,
        cu.userName AS userName,
        cu.[password] AS [password],
        cu.rol AS rol,
        cu.intentosFallidos AS intentosFallidos,
        cu.ultimaSesion AS ultimaSesion,
        cu.fechaBloqueo AS fechaBloqueo
    FROM dbo.CLIENTE c
    INNER JOIN dbo.CUENTA_USUARIO cu
        ON c.idCuentaUsuario = cu.id
    WHERE cu.userName = @p_userName;
END;
GO