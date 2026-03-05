import java.sql.*;
import java.util.ArrayList;

/**
 * SERVICIO: PedidoService
 * Es el corazón del sistema de ventas. Maneja la creación de pedidos, 
 * el control de inventario (stock) y la gestión de los detalles de compra.
 */

public class PedidoService {

    // CREAR PEDIDO
    public int crearPedido(int clienteId) {
        try (Connection con = ConexionDB.getConnection()) {

            // se verificamos si el cliente existe antes de abrirle un pedido
            PreparedStatement psCliente = con.prepareStatement(
                "SELECT * FROM cliente WHERE Id_Cliente = ?");
            psCliente.setInt(1, clienteId);
            ResultSet rs = psCliente.executeQuery();

            if(!rs.next()) {
                System.out.println("❌ Cliente no existe");
                return -1;
            }

            // se inserta el pedido. se usa NULL para que MySQL asigne el ID solo y NOW() para la fecha actual.
            String sql = "INSERT INTO pedidos (Id_Pedido, Id_Cliente_FK, Fecha_Hora_Pedido, Tipo_Entrega, Notas_Cliente, Total, Estado_Pedido) VALUES (NULL, ?, NOW(), ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, clienteId);              // Id_Cliente_FK
            ps.setString(2, "En local");       // Tipo_Entrega
            ps.setString(3, "Sin notas");      // Notas_Cliente
            ps.setDouble(4, 0.0);              // Total
            ps.setString(5, "En preparacion"); // Estado_Pedido

            ps.executeUpdate();

            // se recupera el ID que MySQL le asignó a este nuevo pedido
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

            // Se recorren los resultados de la consulta y se almacenan en la colección "lista" 
            // convirtiendo cada fila en un objeto de tipo "Pedido".
            while(rs.next()) {
                Pedido p = new Pedido();                        // Se crea una nueva instancia del objeto Pedido
                p.setId(rs.getInt("id"));           // Se asigna el valor de la columna 'id' al objeto
                p.setClienteId(rs.getInt("cliente_id"));            // Se asigna el ID del cliente al objeto
                lista.add(p);                                                   // Se agrega el objeto configurado a la lista general

                
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }


    // ACTUALIZAR PEDIDO
    public boolean actualizarProducto(int id, String nombre, double precio, int stock) {

        try (Connection con = ConexionDB.getConnection()) {

            String sql = "UPDATE productos SET Nombre=?, Precio=?, Disponibilidad=? WHERE Id_Producto=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nombre);
            ps.setDouble(2, precio);
            ps.setInt(3, stock);
            ps.setInt(4, id);

            int filas = ps.executeUpdate();

            if(filas > 0) {
                System.out.println("✏ Producto actualizado: " + nombre + " ahora cuesta $" + precio);
                return true;
            } else {
            System.out.println("⚠️ No se encontró el producto con ID: " + id);
            return false;
        }

        } catch(SQLException e) {
            System.out.println("❌ Error al actualizar: " + e.getMessage());
        return false;
    }
    }


    // ELIMINAR PEDIDO
    public boolean eliminarPedido(int idPedido) {
    try (Connection con = ConexionDB.getConnection()) {
        
        // Paso A: Se eliminan primero los registros en la tabla detalle_pedido para evitar conflictos con las restricciones de llave foránea.
        String sqlDetalles = "DELETE FROM detalle_pedido WHERE Id_Pedido_FK = ?";
        PreparedStatement psDetalles = con.prepareStatement(sqlDetalles);
        psDetalles.setInt(1, idPedido);         // Se vincula el ID del pedido al parámetro de la consulta.
        psDetalles.executeUpdate();                             // Se ejecuta el borrado de los detalles.

        // Paso B: Se procede a eliminar el registro principal en la tabla pedidos.
        String sqlPedido = "DELETE FROM pedidos WHERE Id_Pedido = ?"; //Se define una cadena de texto con la sentencia SQL con la instruccion de eliminar el pedido correspondiente al id_pedido requerido, el objeto se guarda en java  
        PreparedStatement psPedido = con.prepareStatement(sqlPedido); //se crea el objeto que contiene la instruccion para eliminar el pedido obtenido en el sqlpedido
        psPedido.setInt(1, idPedido);                   

        int filas = psPedido.executeUpdate();                       //se ejecuta el objeto creado
        if (filas > 0) {
            System.out.println("🗑️ Pedido #" + idPedido + " ha sido eliminado.");
            return true;
        }
        return false;
    } catch (SQLException e) {
        System.out.println("❌ Error técnico al borrar: " + e.getMessage());
        return false;
    }
}


    // AGREGAR DETALLE
    public boolean agregarDetallePedido(int pedidoId, int productoId, int cantidad) {

        try (Connection con = ConexionDB.getConnection()) {

            // Paso 1: Se consulta la disponibilidad (stock) y el precio unitario del producto seleccionado.
            PreparedStatement psProd = con.prepareStatement(
                "SELECT Disponibilidad, Precio FROM productos WHERE Id_Producto = ?");
            psProd.setInt(1, productoId);
            ResultSet rs = psProd.executeQuery();

            // Paso 2: Se valida que la cantidad solicitada sea válida y no exceda la existencia en bodega.
            if(!rs.next()) {
                System.out.println("❌ Producto no existe");
                return false;
            }

            int stock = rs.getInt("Disponibilidad");
            double precio = rs.getDouble("Precio");

            if(cantidad <= 0 || cantidad > stock) {
                System.out.println("❌ Cantidad inválida o stock insuficiente");
                return false;
            }

            // Paso 3: Se registra el producto y sus datos asociados en la tabla detalle_pedido.
            String sql = "INSERT INTO detalle_pedido (Id_Pedido_FK, Id_Producto_FK, Cantidad, Precio_Unitario) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, pedidoId);
            ps.setInt(2, productoId);
            ps.setInt(3, cantidad);
            ps.setDouble(4, cantidad * precio);         // Se calcula el subtotal de la línea.
            ps.executeUpdate();

            
            // Paso 4: Se descuenta la cantidad vendida de la columna Disponibilidad en la tabla productos.
            PreparedStatement psUpdate = con.prepareStatement(
                "UPDATE productos SET Disponibilidad = Disponibilidad - ? WHERE id_Producto = ?");
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

