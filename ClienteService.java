import java.sql.*;
import java.util.ArrayList;

public class ClienteService {

    // INSERTAR
    public boolean agregarCliente(String nombre, String email, String telefono) {
        try (Connection con = ConexionDB.getConnection()) {
            String sql = "INSERT INTO clientes (nombre, email, telefono) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nombre);
            ps.setString(2, email);
            ps.setString(3, telefono);
            ps.executeUpdate();
            System.out.println("✅ Cliente agregado correctamente");
            return true;
        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // CONSULTAR TODOS
    public ArrayList<Cliente> listarClientes() {
        ArrayList<Cliente> lista = new ArrayList<>();

        try (Connection con = ConexionDB.getConnection()) {
            String sql = "SELECT * FROM clientes";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Cliente c = new Cliente();
                c.setId(rs.getInt("id"));
                c.setNombre(rs.getString("nombre"));
                c.setEmail(rs.getString("email"));
                c.setTelefono(rs.getString("telefono"));
                lista.add(c);
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    // ACTUALIZAR
    public boolean actualizarCliente(int id, String nombre, String email, String telefono) {
        try (Connection con = ConexionDB.getConnection()) {
            String sql = "UPDATE clientes SET nombre=?, email=?, telefono=? WHERE id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nombre);
            ps.setString(2, email);
            ps.setString(3, telefono);
            ps.setInt(4, id);

            int filas = ps.executeUpdate();

            if(filas > 0) {
                System.out.println("✏ Cliente actualizado");
                return true;
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ELIMINAR
    public boolean eliminarCliente(int id) {
        try (Connection con = ConexionDB.getConnection()) {
            String sql = "DELETE FROM clientes WHERE id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            int filas = ps.executeUpdate();

            if(filas > 0) {
                System.out.println("🗑 Cliente eliminado");
                return true;
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
