import java.sql.*;
import java.util.ArrayList;

public class ProductoService {

    // INSERTAR
    public boolean agregarProducto(String nombre, double precio, int stock) {
        if(stock < 0 || precio < 0) {
            System.out.println("❌ Stock o precio inválido");
            return false;
        }

        try (Connection con = ConexionDB.getConnection()) {
            String sql = "INSERT INTO productos (nombre, precio, stock) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nombre);
            ps.setDouble(2, precio);
            ps.setInt(3, stock);
            ps.executeUpdate();

            System.out.println("✅ Producto agregado correctamente");
            return true;

        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // LISTAR
    public ArrayList<Producto> listarProductos() {
        ArrayList<Producto> lista = new ArrayList<>();

        try (Connection con = ConexionDB.getConnection()) {
            String sql = "SELECT * FROM productos";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Producto p = new Producto();
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                p.setPrecio(rs.getDouble("precio"));
                p.setStock(rs.getInt("stock"));
                lista.add(p);
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    // ACTUALIZAR
    public boolean actualizarProducto(int id, String nombre, double precio, int stock) {

        if(stock < 0 || precio < 0) {
            System.out.println("❌ Valores inválidos");
            return false;
        }

        try (Connection con = ConexionDB.getConnection()) {
            String sql = "UPDATE productos SET nombre=?, precio=?, stock=? WHERE id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nombre);
            ps.setDouble(2, precio);
            ps.setInt(3, stock);
            ps.setInt(4, id);

            int filas = ps.executeUpdate();

            if(filas > 0) {
                System.out.println("✏ Producto actualizado");
                return true;
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // ELIMINAR
    public boolean eliminarProducto(int id) {
        try (Connection con = ConexionDB.getConnection()) {
            String sql = "DELETE FROM productos WHERE id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            int filas = ps.executeUpdate();

            if(filas > 0) {
                System.out.println("🗑 Producto eliminado");
                return true;
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
