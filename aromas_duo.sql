-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

DROP DATABASE IF EXISTS `aromas_duo`;
CREATE DATABASE IF NOT EXISTS `aromas_duo` DEFAULT CHARACTER SET utf8mb4 ;
USE `aromas_duo` ;
-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema aromas_duo
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema aromas_duo
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `aromas_duo` DEFAULT CHARACTER SET utf8mb4 ;
USE `aromas_duo` ;

-- -----------------------------------------------------
-- Table `aromas_duo`.`persona`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `aromas_duo`.`persona` (
  `IdPersona` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `Nombre` VARCHAR(50) NOT NULL,
  `Apellidos` VARCHAR(75) NOT NULL,
  `Identificacion` VARCHAR(20) NOT NULL,
  `Direccion` VARCHAR(200) NOT NULL,
  `Telefono` VARCHAR(15) NOT NULL,
  `Correo_Electronico` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`IdPersona`),
  UNIQUE (`Identificacion`),
  UNIQUE (`Correo_Electronico`))
  ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;


-- -----------------------------------------------------
-- Table `aromas_duo`.`cliente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `aromas_duo`.`cliente` (
  `Id_Cliente` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `Id_Persona` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`Id_Cliente`),
    CONSTRAINT `fk_cliente_Persona`
    FOREIGN KEY (`Id_Persona`)
    REFERENCES `aromas_duo`.`persona` (`IdPersona`)
    ON delete CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;


-- -----------------------------------------------------
-- Table `aromas_duo`.`ingredientes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `aromas_duo`.`ingredientes` (
  `Id_Ingrediente` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `Nombre_Ingrediente` VARCHAR(80) NOT NULL,
  `Unidad_Medida` VARCHAR(45) NOT NULL,
  `Cantidad` DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (`Id_Ingrediente`)
  )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;


-- -----------------------------------------------------
-- Table `aromas_duo`.`productos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `aromas_duo`.`productos` (
  `Id_Producto` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `Nombre` VARCHAR(50) NOT NULL,
  `Descripcion` VARCHAR(200) NULL DEFAULT NULL,
  `Categoria` VARCHAR(80) NOT NULL,
  `Precio` INT UNSIGNED NOT NULL,
  `Disponibilidad` TINYINT NOT NULL,
  PRIMARY KEY (`Id_Producto`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;


-- -----------------------------------------------------
-- Table `aromas_duo`.`ingrediente_producto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `aromas_duo`.`ingrediente_producto` (
  `Id_Producto_FK` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `Id_Ingrediente_FK` INT UNSIGNED NOT NULL,
  `Cantidad` DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (`Id_Producto_FK`, `Id_Ingrediente_FK`),
  CONSTRAINT `fk_ingprod_producto`
    FOREIGN KEY (`Id_Producto_FK`)
    REFERENCES `aromas_duo`.`productos` (`Id_Producto`)
    on delete cascade
    ON UPDATE CASCADE,
   CONSTRAINT `fk_ingprod_ingrediente`
    FOREIGN KEY (`Id_Ingrediente_FK`)
    REFERENCES `aromas_duo`.`ingredientes` (`Id_Ingrediente`)
    on delete cascade
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;


-- -----------------------------------------------------
-- Table `aromas_duo`.`pedidos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `aromas_duo`.`pedidos` (
  `Id_Pedido` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `Id_Cliente_FK` INT UNSIGNED NOT NULL,
  `Fecha_Hora_Pedido` DATETIME NOT NULL,
  `Tipo_Entrega` ENUM('En local', 'Domicilio propio', 'Rappi', 'Uber', 'Domicilios.com') NOT NULL,
  `Notas_Cliente` VARCHAR(200) NULL DEFAULT NULL,
  `Total` INT UNSIGNED NOT NULL,
  `Estado_Pedido` ENUM('En preparacion', 'Listo para enviar', 'En camino', 'Entregado', 'Retrasado', 'Cancelado') NOT NULL,
  PRIMARY KEY (`Id_Pedido`),
  CONSTRAINT `fk_pedido_cliente`
  FOREIGN KEY (`Id_Cliente_FK`)
  REFERENCES `aromas_duo`.`cliente` (`Id_Cliente`) 
  on delete cascade
  ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;


-- -----------------------------------------------------
-- Table `aromas_duo`.`usuario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `aromas_duo`.`usuario` (
  `Id_Usuario` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `Id_Persona_FK` INT UNSIGNED NOT NULL,
  `Nombre_Usuario` VARCHAR(45) NOT NULL,
  `Estado` TINYINT NOT NULL,
  `Rol` ENUM('Administrativo', 'Operativo', 'Cliente') NOT NULL,
  `Area_acceso` ENUM('General', 'Ventas', 'Cocina', 'Pedidos', 'Inventario') NOT NULL,
  `Contrasena` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`Id_Usuario`),
  UNIQUE INDEX `Nombre_Usuario_UNIQUE` (`Nombre_Usuario` ASC) ,
  INDEX `idx_usuario_persona` (`Id_Persona_FK` ASC) ,
  CONSTRAINT `fk_usuario_persona`
    FOREIGN KEY (`Id_Persona_FK`)
    REFERENCES `aromas_duo`.`persona` (`IdPersona`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;


-- -----------------------------------------------------
-- Table `aromas_duo`.`inventario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `aromas_duo`.`inventario` (
  `Id_Ingrediente_FK` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `Cantidad_Actual` INT UNSIGNED NOT NULL,
  `Costo_Unitario` INT UNSIGNED NOT NULL,
  `Fecha_Vencimiento` DATE NOT NULL,
  `Fecha_Ultima_Actualizacion` DATE NOT NULL,
  `Minimo_Stock` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`Id_Ingrediente_FK`),
  CONSTRAINT `fk_inventario_ingrediente`
    FOREIGN KEY (`Id_Ingrediente_FK`)
    REFERENCES `aromas_duo`.`ingredientes` (`Id_Ingrediente`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `aromas_duo`.`detalle_pedido`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `aromas_duo`.`detalle_pedido` (
  `Id_Pedido_FK` INT UNSIGNED NOT NULL,
  `Id_Producto_FK` INT UNSIGNED NOT NULL,
  `Cantidad` INT UNSIGNED NOT NULL,
  `Precio_Unitario` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`Id_Pedido_FK`, `Id_Producto_FK`),
  INDEX `idx_detalle_pedido_producto` (`Id_Producto_FK` ASC) ,
  CONSTRAINT `fk_detalle_pedido_pedidos`
    FOREIGN KEY (`Id_Pedido_FK`)
    REFERENCES `aromas_duo`.`pedidos` (`Id_Pedido`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_detalle_pedido_productos`
    FOREIGN KEY (`Id_Producto_FK`)
    REFERENCES `aromas_duo`.`productos` (`Id_Producto`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `aromas_duo`.`entrega`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `aromas_duo`.`entrega` (
  `Id_Entrega` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `Direccion_Entrega` VARCHAR(250) NOT NULL,
  `Estado_Entrega` ENUM('En proceso', 'Exitosa', 'Fallida', 'Cancelada') NOT NULL,
  `Costo_Entrega` INT UNSIGNED NOT NULL,
  `Responsable` VARCHAR(45) NOT NULL,
  `Id_Pedido_FK` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`Id_Entrega`),
  INDEX `Id_Pedido_FK_idx` (`Id_Pedido_FK` ASC) ,
  CONSTRAINT `fk_entrega_Id_Pedido_FK`
    FOREIGN KEY (`Id_Pedido_FK`)
    REFERENCES `aromas_duo`.`pedidos` (`Id_Pedido`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `aromas_duo`.`PAGO`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `aromas_duo`.`pago` (
  `Id_pago` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `Id_Pedido_FK` INT UNSIGNED NOT NULL,
  `Fecha` DATE NOT NULL,
  `Cantidad_Producto` INT UNSIGNED NOT NULL,
  `Valor_Pagado` INT UNSIGNED NOT NULL,
  `Pago_Finalizado` TINYINT NOT NULL,
  `Numero_Confirmacion` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`Id_Pago`),
  INDEX `idx_pago_Pedido` (`Id_Pedido_FK` ASC) ,
  CONSTRAINT `fk_pago_Id_Pedido`
    FOREIGN KEY (`Id_Pedido_FK`)
    REFERENCES `aromas_duo`.`pedidos` (`Id_Pedido`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `aromas_duo`.`metodo pago`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `aromas_duo`.`metodo_pago` (
  `Id_MetodoPago` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `Id_Pago_FK` INT UNSIGNED NOT NULL,
  `TipoMetodo` ENUM('Efectivo', 'Transferencia', 'T Debito', 'T Credito', 'Aplicación') NOT NULL,
  `DescripcionMetodo` VARCHAR(150) NOT NULL,
  PRIMARY KEY (`Id_MetodoPago`),
  INDEX `idx_metodo_pago_Id_Pago` (`Id_pago_FK` ASC) ,
  CONSTRAINT `fk_metodo_pago_Id_Pago`
    FOREIGN KEY (`Id_pago_FK`)
    REFERENCES `aromas_duo`.`pago` (`Id_Pago`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
