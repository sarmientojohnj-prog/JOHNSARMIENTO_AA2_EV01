package servicio;
import java.sql.*;
import java.util.ArrayList;

import conexion.ConexionDB;
import modelo.Factura;

/**
 * SERVICIO: FacturaService
 * Esta clase maneja la creación y administración de las facturas finales.
 * Calcula el monto total basándose en los productos registrados en un pedido.
 */
public class FacturaService {

    // CREAR FACTURA
    public boolean generarFactura(int pedidoId) {
        try (Connection con = ConexionDB.getConnection()) {

            // Verificar que el pedido realmente exista antes de cobrarlo
            PreparedStatement psPedido = con.prepareStatement(
                "SELECT * FROM pedidos WHERE Id_Pedido = ?");   // Prepara la consulta SQL para buscar el pedido por su número de identificación (ID)
            psPedido.setInt(1, pedidoId);           //  Reemplaza el signo '?' con el número de pedido real recibido
            ResultSet rs = psPedido.executeQuery();                 // Ejecuta la búsqueda y guarda el resultado en 'rs' 

            if(!rs.next()) {                                        //si el resultado de rs esta vacio envia mensaje de error
                System.out.println("❌ Pedido no existe");
                return false;                                       // Se detiene el proceso porque no se puede facturar lo que no existe
            }

            PreparedStatement psTotal = con.prepareStatement(
                "SELECT SUM(Precio_Unitario) AS total FROM detalle_pedido WHERE Id_Pedido_FK = ?");     //// se le pide a MySQL que sume (SUM) todos los precios de los productos de este pedido.
            psTotal.setInt(1, pedidoId);
            ResultSet rsTotal = psTotal.executeQuery();                 //declara rsTotal como el valor de la suma de los valores incluidos en detalle_pedido

            double total = 0;                                       //establece "total" como numero con centavos
            if(rsTotal.next()) {
                total = rsTotal.getDouble("total");         //declara "total" como el valor almacenado en rstotal obtenido en la consulta de mysql y lo aloja en la columna total en java
            }
            
            //agrega los resultados en la tabla facturas segun las columnas establecidas
            PreparedStatement psInsert = con.prepareStatement(
                "INSERT INTO facturas (Id_Pedido_FK, Total) VALUES (?, ?)");
            psInsert.setInt(1, pedidoId);
            psInsert.setDouble(2, total);
            psInsert.executeUpdate();

            System.out.println("✅ Factura generada con total: " + total);
            return true;

        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // LISTAR FACTURAS (MÉTODO PARA TRAER TODAS LAS FACTURAS DE LA BASE DE DATOS)
    public ArrayList<Factura> listarFacturas() {
        ArrayList<Factura> lista = new ArrayList<>();           // se crea una lista vacía para ir guardando las facturas que encontremos

        try (Connection con = ConexionDB.getConnection()) {

            PreparedStatement ps = con.prepareStatement(        // se le pide a MySQL que devuelva todos los registros de la tabla facturas
                "SELECT * FROM facturas");
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {                          // Mientras haya una fila siguiente en el resultado de la base de datos...
                Factura f = new Factura();              // se crea un objeto Factura para meter los datos
                
                // Saca los datos de las columnas de MySQL y los guarda en el objeto Java
                f.setId(rs.getInt("Id_Factura"));
                f.setPedidoId(rs.getInt("Id_Pedido_FK"));
                f.setTotal(rs.getDouble("Total"));
                lista.add(f);                           // agrega esa factura completa a nuestra lista
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }

        return lista;                                   // Al final, devuelve la lista llena con todas las facturas encontradas
    }


    // ACTUALIZAR FACTURA (CAMBIA EL VALOR DE UNA FACTURA EXISTENTE)
    public boolean actualizarFactura(int id, double nuevoTotal) {
        try (Connection con = ConexionDB.getConnection()) {

            
            // Prepara la instrucción: "Cambia el total donde el ID coincida"
            PreparedStatement ps = con.prepareStatement(
                "UPDATE facturas SET total=? WHERE Id_Factura=?");
            
            // Se llenan los espacios con la nueva información
            ps.setDouble(1, nuevoTotal);
            ps.setInt(2, id);

            // Si se logró actualizar al menos una fila, el sistema avisa que fue éxito
            if(ps.executeUpdate() > 0) {
                System.out.println("✏ Factura actualizada");
                return true;
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    // ELIMINAR FACTURA
    public boolean eliminarFactura(int id) {
        try (Connection con = ConexionDB.getConnection()) {

            // Instrucción SQL para eliminar el registro según su ID único
            PreparedStatement ps = con.prepareStatement(
                "DELETE FROM facturas WHERE Id_Factura=?");
            ps.setInt(1, id);

            if(ps.executeUpdate() > 0) {                                    // Si executeUpdate devuelve más de 0, significa que sí encontró el ID y lo borró
                System.out.println("🗑 Factura eliminada");
                return true;
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
