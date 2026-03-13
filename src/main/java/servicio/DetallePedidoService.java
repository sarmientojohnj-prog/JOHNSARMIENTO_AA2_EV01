package servicio;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import conexion.ConexionDB;
import modelo.DetallePedido;

// * Servicio encargado de gestionar los productos dentro de un pedido. Maneja cálculos de subtotal y control de inventario (Stock).

public class DetallePedidoService {

    // CREAR DETALLE
    public boolean agregarDetalle(int pedidoId, int productoId, int cantidad) {

        if(cantidad <= 0){
            System.out.println("❌ Cantidad inválida");
            return false;
        }

        try(Connection con = ConexionDB.getConnection()) {

            // verificar si el producto existe y obtener su precio y stock actual
            PreparedStatement psProd = con.prepareStatement(
                "SELECT Disponibilidad, Precio FROM productos WHERE Id_Producto=?");
            psProd.setInt(1, productoId);
            ResultSet rs = psProd.executeQuery();

            if(!rs.next()){
                System.out.println("❌ Producto no existe");
                return false;
            }

            // Validar que hayan suficientes productos en bodega
            int stock = rs.getInt("Disponibilidad");
            double precio = rs.getDouble("Precio");

            if(cantidad > stock){
                System.out.println("❌ Stock insuficiente");
                return false;
            }

            // Calcular el subtotal (Cantidad x Precio)
            double subtotal = cantidad * precio;

            //Guardar el producto en la tabla de detalles del pedido
            PreparedStatement psInsert = con.prepareStatement(
                "INSERT INTO detalle_pedido (Id_Pedido_FK, Id_Producto_FK, Cantidad, Precio_Unitario) VALUES (?,?,?,?)");

            psInsert.setInt(1, pedidoId);
            psInsert.setInt(2, productoId);
            psInsert.setInt(3, cantidad);
            psInsert.setDouble(4, subtotal);
            psInsert.executeUpdate();

            // Actualizar el stock en la tabla productos (Restar lo que se vendió)
            PreparedStatement psUpdate = con.prepareStatement(
                "UPDATE productos SET Disponibilidad = Disponibilidad - ? WHERE Id_Producto=?");
            psUpdate.setInt(1, cantidad);
            psUpdate.setInt(2, productoId);
            psUpdate.executeUpdate();

            System.out.println("✅ Detalle agregado");//mensaje de confirmacion de la modificacion realizada
            return true;

        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }


    // LISTAR DETALLES (MÉTODO PARA VER TODOS LOS PRODUCTOS COMPRADOS (CON NOMBRES))
    public List<DetallePedido> listarDetalles(){

        ArrayList<DetallePedido> lista = new ArrayList<>();

        try(Connection con = ConexionDB.getConnection()){

            // Uso de JOIN para traer el nombre del producto desde la tabla 'productos'
            PreparedStatement ps = con.prepareStatement(
                "SELECT d.*, p.Nombre \r\n" + // 
                "FROM detalle_pedido d \r\n" + //
                "JOIN productos p ON d.Id_Producto_FK = p.Id_Producto");
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                DetallePedido d = new DetallePedido();
                d.setId(rs.getInt("Id_Pedido_FK"));
                d.setPedidoId(rs.getInt("Id_Pedido_FK"));
                d.setProductoId(rs.getInt("Id_Producto_FK"));
                d.setnombreProducto(rs.getString("Nombre")); // Nombre proveniente del JOIN
                d.setCantidad(rs.getInt("Cantidad"));
                d.setSubtotal(rs.getDouble("Precio_Unitario"));
                lista.add(d);
            }

        }catch(SQLException e){
            e.printStackTrace();
        }

        return lista;
    }


    // ACTUALIZAR DETALLE (MÉTODO PARA CAMBIAR LA CANTIDAD DE UN PRODUCTO YA ANOTADO)
    public boolean actualizarCantidad(int id, int nuevaCantidad){

        if(nuevaCantidad <= 0){
            System.out.println("❌ Cantidad inválida");
            return false;
        }

        try(Connection con = ConexionDB.getConnection()){

            PreparedStatement ps = con.prepareStatement(
                "UPDATE detalle_pedido SET cantidad=? WHERE id=?");
            ps.setInt(1, nuevaCantidad);
            ps.setInt(2, id);

            if(ps.executeUpdate() > 0){
                System.out.println("✏ Detalle actualizado");
                return true;
            }

        }catch(SQLException e){
            e.printStackTrace();
        }

        return false;
    }


    // ELIMINAR DETALLE usando Pedido y Producto
public boolean eliminarDetalle(int pedidoId, int productoId) {
    try (Connection con = ConexionDB.getConnection()) {
        // Borramos donde coincidan AMBOS datos
        String sql = "DELETE FROM detalle_pedido WHERE Id_Pedido_FK=? AND Id_Producto_FK=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, pedidoId);
        ps.setInt(2, productoId);

        int filas = ps.executeUpdate();
        if (filas > 0) {
            System.out.println("🗑️ Producto eliminado del pedido con éxito.");
            return true;
        }
        return false;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
}