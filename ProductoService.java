import java.sql.*;
import java.util.ArrayList;

public class ProductoService {

    // INSERTAR
    public int agregarProducto(String nombre, String categoria, double precio, int stock) {         //declarar las variables que se van a tener en cuenta según tabla productos
    try (Connection con = ConexionDB.getConnection()) {
        String sql = "INSERT INTO productos (Nombre, Categoria, Precio, Disponibilidad) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, nombre);
        ps.setString(2, categoria);
        ps.setDouble(3, precio);
        ps.setInt(4, stock);
        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            int idProd = rs.getInt(1);
            System.out.println("✅ Producto agregado con ID: " + idProd);
            return idProd;                                                                  // recibir el ID real del producto
        }
        return -1;
    } catch (SQLException e) {
        e.printStackTrace();
        return -1;
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
                //atributos de los productos del pedido sacados de las columnas de MySQL y se guardan en el objeto 'p'
                p.setId(rs.getInt("Id_Producto"));
                p.setNombre(rs.getString("Nombre"));
                p.setPrecio(rs.getDouble("Precio"));
                p.setStock(rs.getInt("Disponibilidad"));
                lista.add(p);
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    // ACTUALIZAR UN PRODUCTO
    public boolean actualizarProducto(int id, String nombre, double precio, int stock) {

        if(stock < 0 || precio < 0) {
            System.out.println("❌ Valores inválidos");     //proteccion en caso de ingresar valores incorrectos
            return false;
        }

        try (Connection con = ConexionDB.getConnection()) {
            String sql = "UPDATE productos SET Nombre=?, Precio=?, Disponibilidad=? WHERE id_Producto=?";
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
            String sql = "DELETE FROM productos WHERE id_Producto=?";
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
