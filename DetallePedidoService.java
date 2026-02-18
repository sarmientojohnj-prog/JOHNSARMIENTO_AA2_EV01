import java.sql.*;
import java.util.ArrayList;

public class DetallePedidoService {

    // CREAR DETALLE
    public boolean agregarDetalle(int pedidoId, int productoId, int cantidad) {

        if(cantidad <= 0){
            System.out.println("❌ Cantidad inválida");
            return false;
        }

        try(Connection con = ConexionDB.getConnection()) {

            // verificar producto
            PreparedStatement psProd = con.prepareStatement(
                "SELECT stock, precio FROM productos WHERE id=?");
            psProd.setInt(1, productoId);
            ResultSet rs = psProd.executeQuery();

            if(!rs.next()){
                System.out.println("❌ Producto no existe");
                return false;
            }

            int stock = rs.getInt("stock");
            double precio = rs.getDouble("precio");

            if(cantidad > stock){
                System.out.println("❌ Stock insuficiente");
                return false;
            }

            double subtotal = cantidad * precio;

            PreparedStatement psInsert = con.prepareStatement(
                "INSERT INTO detalle_pedido (pedido_id, producto_id, cantidad, subtotal) VALUES (?,?,?,?)");

            psInsert.setInt(1, pedidoId);
            psInsert.setInt(2, productoId);
            psInsert.setInt(3, cantidad);
            psInsert.setDouble(4, subtotal);
            psInsert.executeUpdate();

            // actualizar stock
            PreparedStatement psUpdate = con.prepareStatement(
                "UPDATE productos SET stock = stock - ? WHERE id=?");
            psUpdate.setInt(1, cantidad);
            psUpdate.setInt(2, productoId);
            psUpdate.executeUpdate();

            System.out.println("✅ Detalle agregado");
            return true;

        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }


    // LISTAR DETALLES
    public ArrayList<DetallePedido> listarDetalles(){

        ArrayList<DetallePedido> lista = new ArrayList<>();

        try(Connection con = ConexionDB.getConnection()){

            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM detalle_pedido");
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                DetallePedido d = new DetallePedido();
                d.setId(rs.getInt("id"));
                d.setPedidoId(rs.getInt("pedido_id"));
                d.setProductoId(rs.getInt("producto_id"));
                d.setCantidad(rs.getInt("cantidad"));
                d.setSubtotal(rs.getDouble("subtotal"));
                lista.add(d);
            }

        }catch(SQLException e){
            e.printStackTrace();
        }

        return lista;
    }


    // ACTUALIZAR DETALLE
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


    // ELIMINAR DETALLE
    public boolean eliminarDetalle(int id){

        try(Connection con = ConexionDB.getConnection()){

            PreparedStatement ps = con.prepareStatement(
                "DELETE FROM detalle_pedido WHERE id=?");
            ps.setInt(1, id);

            if(ps.executeUpdate() > 0){
                System.out.println("🗑 Detalle eliminado");
                return true;
            }

        }catch(SQLException e){
            e.printStackTrace();
        }

        return false;
    }
}
