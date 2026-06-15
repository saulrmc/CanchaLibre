USE canchalibre;
GO

IF OBJECT_ID('dbo.insertarCliente', 'P') IS NOT NULL DROP PROCEDURE dbo.insertarCliente;
GO
IF OBJECT_ID('dbo.modificarCliente', 'P') IS NOT NULL DROP PROCEDURE dbo.modificarCliente;
GO
IF OBJECT_ID('dbo.insertarPropietario', 'P') IS NOT NULL DROP PROCEDURE dbo.insertarPropietario;
GO

-- RF01: Registro de Cliente con validación
CREATE PROCEDURE dbo.insertarCliente
    @p_nombres NVARCHAR(150),
    @p_correo NVARCHAR(100),
    @p_telefono NVARCHAR(20),
    @p_contrasena NVARCHAR(255),
    @p_calificacion INT,
    @p_id INT OUTPUT
AS
BEGIN
    IF NOT EXISTS (SELECT 1 FROM Cliente WHERE correo = @p_correo)
    BEGIN
        INSERT INTO Cliente (nombres, correo, telefono, contrasena, calificacion, intentosFallidos)
        VALUES (@p_nombres, @p_correo, @p_telefono, @p_contrasena, @p_calificacion, 0);
        SET @p_id = SCOPE_IDENTITY();
    END
    ELSE
    BEGIN
        SET @p_id = 0;
    END
END
GO

-- RF13: Modificar datos de Cliente
CREATE PROCEDURE dbo.modificarCliente
    @p_id INT,
    @p_nombres NVARCHAR(150),
    @p_correo NVARCHAR(100),
    @p_telefono NVARCHAR(20),
    @p_contrasena NVARCHAR(255)
AS
BEGIN
    UPDATE Cliente
    SET nombres = @p_nombres,
        correo = @p_correo,
        telefono = @p_telefono,
        contrasena = @p_contrasena
    WHERE idCliente = @p_id;
END
GO

-- RF04: Registro de Propietario
CREATE PROCEDURE dbo.insertarPropietario
    @p_nombres NVARCHAR(150),
    @p_correo NVARCHAR(100),
    @p_ruc NVARCHAR(20),
    @p_id INT OUTPUT
AS
BEGIN
    INSERT INTO Propietario (nombres, correo, ruc)
    VALUES (@p_nombres, @p_correo, @p_ruc);
    SET @p_id = SCOPE_IDENTITY();
END
GO