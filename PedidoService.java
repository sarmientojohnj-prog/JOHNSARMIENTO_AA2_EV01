import java.sql.*;
import java.util.ArrayList;

public class PedidoService {

    // CREAR PEDIDO
    public int crearPedido(int clienteId) {
        try (Connection con = ConexionDB.getConnection()) {

            PreparedStatement psCliente = con.prepareStatement(
                "SELECT * FROM clientes WHERE id = ?");
            psCliente.setInt(1, clienteId);
            ResultSet rs = psCliente.executeQuery();

            if(!rs.next()) {
                System.out.println("❌ Cliente no existe");
                return -1;
            }

            String sql = "INSERT INTO pedidos (cliente_id) VALUES (?)";
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, clienteId);
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if(keys.next()) {
                int id = keys.getInt(1);
                System.out.println("✅ Pedido creado con ID: " + id);
                return id;
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }


    // LISTAR PEDIDOS
    public ArrayList<Pedido> listarPedidos() {
        ArrayList<Pedido> lista = new ArrayList<>();

        try (Connection con = ConexionDB.getConnection()) {

            String sql = "SELECT * FROM pedidos";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Pedido p = new Pedido();
                p.setId(rs.getInt("id"));
                p.setClienteId(rs.getInt("cliente_id"));
                lista.add(p);
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }


    // ACTUALIZAR PEDIDO
    public boolean actualizarPedido(int id, int nuevoClienteId) {
        try (Connection con = ConexionDB.getConnection()) {

            String sql = "UPDATE pedidos SET cliente_id=? WHERE id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, nuevoClienteId);
            ps.setInt(2, id);

            int filas = ps.executeUpdate();

            if(filas > 0) {
                System.out.println("✏ Pedido actualizado");
                return true;
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    // ELIMINAR PEDIDO
    public boolean eliminarPedido(int id) {
        try (Connection con = ConexionDB.getConnection()) {

            String sql = "DELETE FROM pedidos WHERE id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            int filas = ps.executeUpdate();

            if(filas > 0) {
                System.out.println("🗑 Pedido eliminado");
                return true;
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    // AGREGAR DETALLE
    public boolean agregarDetallePedido(int pedidoId, int productoId, int cantidad) {

        try (Connection con = ConexionDB.getConnection()) {

            PreparedStatement psProd = con.prepareStatement(
                "SELECT stock, precio FROM productos WHERE id = ?");
            psProd.setInt(1, productoId);
            ResultSet rs = psProd.executeQuery();

            if(!rs.next()) {
                System.out.println("❌ Producto no existe");
                return false;
            }

            int stock = rs.getInt("stock");
            double precio = rs.getDouble("precio");

            if(cantidad <= 0 || cantidad > stock) {
                System.out.println("❌ Cantidad inválida o stock insuficiente");
                return false;
            }

            String sql = "INSERT INTO detalle_pedido (pedido_id, producto_id, cantidad, subtotal) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, pedidoId);
            ps.setInt(2, productoId);
            ps.setInt(3, cantidad);
            ps.setDouble(4, cantidad * precio);
            ps.executeUpdate();

            PreparedStatement psUpdate = con.prepareStatement(
                "UPDATE productos SET stock = stock - ? WHERE id = ?");
            psUpdate.setInt(1, cantidad);
            psUpdate.setInt(2, productoId);
            psUpdate.executeUpdate();

            System.out.println("✅ Detalle agregado correctamente");
            return true;

        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

