import java.sql.*;
import java.util.ArrayList;

public class FacturaService {

    // CREAR FACTURA
    public boolean generarFactura(int pedidoId) {
        try (Connection con = ConexionDB.getConnection()) {

            PreparedStatement psPedido = con.prepareStatement(
                "SELECT * FROM pedidos WHERE id = ?");
            psPedido.setInt(1, pedidoId);
            ResultSet rs = psPedido.executeQuery();

            if(!rs.next()) {
                System.out.println("❌ Pedido no existe");
                return false;
            }

            PreparedStatement psTotal = con.prepareStatement(
                "SELECT SUM(subtotal) AS total FROM detalle_pedido WHERE pedido_id = ?");
            psTotal.setInt(1, pedidoId);
            ResultSet rsTotal = psTotal.executeQuery();

            double total = 0;
            if(rsTotal.next()) {
                total = rsTotal.getDouble("total");
            }

            PreparedStatement psInsert = con.prepareStatement(
                "INSERT INTO facturas (pedido_id, total) VALUES (?, ?)");
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


    // LISTAR FACTURAS
    public ArrayList<Factura> listarFacturas() {
        ArrayList<Factura> lista = new ArrayList<>();

        try (Connection con = ConexionDB.getConnection()) {

            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM facturas");
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Factura f = new Factura();
                f.setId(rs.getInt("id"));
                f.setPedidoId(rs.getInt("pedido_id"));
                f.setTotal(rs.getDouble("total"));
                lista.add(f);
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }


    // ACTUALIZAR FACTURA
    public boolean actualizarFactura(int id, double nuevoTotal) {
        try (Connection con = ConexionDB.getConnection()) {

            PreparedStatement ps = con.prepareStatement(
                "UPDATE facturas SET total=? WHERE id=?");
            ps.setDouble(1, nuevoTotal);
            ps.setInt(2, id);

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

            PreparedStatement ps = con.prepareStatement(
                "DELETE FROM facturas WHERE id=?");
            ps.setInt(1, id);

            if(ps.executeUpdate() > 0) {
                System.out.println("🗑 Factura eliminada");
                return true;
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
