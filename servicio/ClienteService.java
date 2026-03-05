package servicio;
import java.sql.*;
import java.util.ArrayList;

import conexion.ConexionDB;
import modelo.Cliente;

public class ClienteService {

    // INSERTAR (Modificado para tu base de datos profesional)
    public int agregarCliente(String nombre, String apellidos, String identificacion, String direccion, String telefono, String email) {
        try (Connection con = ConexionDB.getConnection()) {
            
            // 1. Guardar los datos en la tabla PERSONA
            String sqlPersona = "INSERT INTO persona (Nombre, Apellidos, Identificacion, Direccion, Telefono, Correo_Electronico) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement psPersona = con.prepareStatement(sqlPersona, Statement.RETURN_GENERATED_KEYS);
            psPersona.setString(1, nombre);
            psPersona.setString(2, apellidos);
            psPersona.setString(3, identificacion);
            psPersona.setString(4, direccion);
            psPersona.setString(5, telefono);
            psPersona.setString(6, email);
            psPersona.executeUpdate();
            
            // 2. Obtener el ID que MySQL le dio a esa Persona
            ResultSet rsPersona = psPersona.getGeneratedKeys();
            if (rsPersona.next()) {
                int idPersonaGenerado = rsPersona.getInt(1);
                // (se emplea el ID de persona para el siguiente paso)

                // 3. Registrar a esa persona en la tabla CLIENTE
                String sqlCliente = "INSERT INTO cliente (Id_Persona) VALUES (?)";
                PreparedStatement psCliente = con.prepareStatement(sqlCliente, Statement.RETURN_GENERATED_KEYS);
                psCliente.setInt(1, idPersonaGenerado);
                psCliente.executeUpdate();

                // 4. Obtenemos el ID real del CLIENTE (el que necesita el Pedido)
                ResultSet rsCli = psCliente.getGeneratedKeys();
                if (rsCli.next()) {
                    int idClienteReal = rsCli.getInt(1);
                    System.out.println("✅ Cliente registrado con ID: " + idClienteReal);
                    return idClienteReal; // Este es el número que usaremos en Main
                }
            }
            return -1;
        } catch(SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    // CONSULTAR TODOS
    public ArrayList<Cliente> listarClientes() {
        ArrayList<Cliente> lista = new ArrayList<>();

        try (Connection con = ConexionDB.getConnection()) {
            String sql = "SELECT * FROM persona";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Cliente c = new Cliente();
                c.setId(rs.getInt("idPersona"));
                c.setNombre(rs.getString("Nombre"));
                c.setEmail(rs.getString("Correo_Electronico"));
                c.setTelefono(rs.getString("Telefono"));
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
            String sql = "UPDATE persona SET Nombre=?, Correo_Electronico=?, Telefono=? WHERE idPersona=?";
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
            String sql = "DELETE FROM persona WHERE idPersona=?";
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
