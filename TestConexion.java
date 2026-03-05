import java.sql.Connection;

import conexion.ConexionDB;

public class TestConexion {

    public static void main(String[] args) {
        Connection con = ConexionDB.getConnection();

        if (con != null) {
            System.out.println("✅ Conectado a MySQL");
        } else {
            System.out.println("❌ Error de conexión");
        }
    }
}
